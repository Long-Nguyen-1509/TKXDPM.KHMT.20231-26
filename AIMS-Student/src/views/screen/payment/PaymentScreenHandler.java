package views.screen.payment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.*;

import controller.PaymentController;
import entity.cart.Cart;
import common.exception.PlaceOrderException;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.payment.Transaction;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.popup.PopupScreen;

public class PaymentScreenHandler extends BaseScreenHandler {

	@FXML
	private Button btnConfirmPayment;

	@FXML
	private ImageView loadingImage;

	@FXML
	private Button btnQRGeneration;

	private Invoice invoice;

	public PaymentScreenHandler(Stage stage, String screenPath, int amount, String contents) throws IOException {
		super(stage, screenPath);
	}

	public PaymentScreenHandler(Stage stage, String screenPath, Invoice invoice) throws Exception {
		super(stage, screenPath);
		this.invoice = invoice;

		btnConfirmPayment.setOnMouseClicked(e -> {
			try {
				// Create and configure the ProgressIndicator
				ProgressIndicator pi = new ProgressIndicator();

				StackPane overlayPane = new StackPane();
				overlayPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2)");

				rootPane.setDisable(true);
				overlayPane.getChildren().add(pi);
				pi.setPrefSize(50,50);
				rootPane.getChildren().add(overlayPane);

				// Center the overlay pane within the rootPane
				AnchorPane.setTopAnchor(overlayPane, 0.0);
				AnchorPane.setRightAnchor(overlayPane, 0.0);
				AnchorPane.setBottomAnchor(overlayPane, 0.0);
				AnchorPane.setLeftAnchor(overlayPane, 0.0);
				Transaction transaction = ((PaymentController) getBController()).confirmToPayOrder(invoice);
				CompletableFuture<Void> qrCodeFuture = CompletableFuture.runAsync(() -> {
					// Generate QR code logic
					try {
						generateVNPQR(transaction.getId());
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				});
				qrCodeFuture.thenRun(() -> {
					try {
						confirmToPayOrderHandler(transaction);
					} catch (Exception ex) {
						throw new RuntimeException(ex);
					}
				});
				try {
					qrCodeFuture.get();

				} catch (InterruptedException | ExecutionException exp) {
					exp.printStackTrace(); // Handle exceptions
				}
			} catch (Exception exp) {
				System.out.println(exp.getStackTrace());
			}
		});
	}

	@FXML
	private Label pageTitle;

	@FXML
	private TextField cardNumber;

	@FXML
	private TextField holderName;

	@FXML
	private TextField expirationDate;

	@FXML
	private TextField securityCode;

	@FXML
	private ImageView qrImage;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private Label qrLabel;

	void confirmToPayOrderHandler(Transaction transaction) throws Exception {
		PaymentController ctrl = (PaymentController) getBController();
		long startTime = System.currentTimeMillis();
		long timeout = 10*1000;
		while (true) {
			if (System.currentTimeMillis() - startTime >= timeout) break;
		}
		String res = ctrl.processPaymentStatus(transaction);
		Platform.runLater(() -> {
			ctrl.emptyCart();
			BaseScreenHandler resultScreen = null;
			try {
				resultScreen = new ResultScreenHandler(this.stage, Configs.RESULT_SCREEN_PATH, res);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			resultScreen.setPreviousScreen(this);
			resultScreen.setHomeScreenHandler(homeScreenHandler);
			resultScreen.setScreenTitle("Result Screen");
			resultScreen.show();
		});

	}

	private void generateVNPQR(int transactionId) throws Exception {
		PaymentController ctrl = (PaymentController) getBController();
		qrLabel.setVisible(true);
		Image image = SwingFXUtils.toFXImage(ctrl.generateVNPQR(invoice, transactionId), null);
		qrImage.setPreserveRatio(true);
		qrImage.setImage(image);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}