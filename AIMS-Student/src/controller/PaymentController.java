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
	private static final InterbankInterface interbank = new InterbankSubsystem();

	public BufferedImage generateVNPQR(Invoice invoice, int transactionId) throws Exception {
		String url = interbank.buildVNPTransaction(invoice, transactionId);
		return QRGenerator.generateQRCodeImage(url);
	}

	public Transaction confirmToPayOrder(Invoice invoice) throws SQLException {

		Transaction transaction = new Transaction();
		Random random = new Random();
		int transactionId = Math.abs(random.nextInt());
		transaction.setGateway("VNP");
		transaction.setAmount(invoice.getAmount());
		transaction.setId(transactionId);
		transaction.setOrder(invoice.getOrder());
		invoice.getOrder().setTransaction(transaction);
		invoice.getOrder().saveOrder();
		interbank.registerTransaction(transactionId, invoice.getAmount());
		System.out.println("Created transaction with id" + transactionId);
		return transaction;
	}

	public String processPaymentStatus(Transaction transaction) throws SQLException {
		String status = interbank.fetchTransactionStatus(transaction.getId());
		transaction.updateTransactionStatus(status);
		return status;
	}

	public void emptyCart() {
        Cart.getCart().emptyCart();
    }
}