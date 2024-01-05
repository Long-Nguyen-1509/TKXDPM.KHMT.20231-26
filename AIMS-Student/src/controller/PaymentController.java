package controller;

import entity.cart.Cart;
import entity.db.AIMSDB;
import entity.invoice.Invoice;
import entity.payment.CreditCard;
import entity.payment.Transaction;
import subsystem.InterbankInterface;
import subsystem.InterbankSubsystem;
import utils.QRGenerator;

import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;


/**
 * This {@code PaymentController} class control the flow of the payment process
 * in our AIMS Software.
 * 
 * @author longnh
 *
 */
public class PaymentController extends BaseController {
	private final InterbankInterface interbank = new InterbankSubsystem();

	public BufferedImage generateVNPQR(Invoice invoice, int width, int height) throws Exception {
		String url = interbank.generateVNPUrl(invoice);
		return QRGenerator.generateQRCodeImage(url, width, height);
	}

	public int confirmToPayOrder(Invoice invoice) throws SQLException {

		Transaction transaction = new Transaction();
		Random random = new Random();
		int transactionID = Math.abs(random.nextInt());
		transaction.setGateway("VNP");
		transaction.setId(transactionID);
		transaction.setOrder(invoice.getOrder());
		transaction.saveTransaction();
		invoice.getOrder().setTransaction(transaction);
		invoice.getOrder().saveOrder();

		return transaction.getId();
	}

	public String fetchTransactionStatusById(int id) throws SQLException {
		try (Connection connection = AIMSDB.getConnection()) {
			String sql = "SELECT * FROM Transaction WHERE id = ?";
			try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
				preparedStatement.setInt(1, id);

				try (ResultSet resultSet = preparedStatement.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getString("status");
					} else {
						System.out.println("Transaction not found");
						return null;
					}
				}
			}
		} catch (SQLException e) {
			throw new SQLException(e.getMessage());
		}
	}

	public void emptyCart(){
        Cart.getCart().emptyCart();
    }
}