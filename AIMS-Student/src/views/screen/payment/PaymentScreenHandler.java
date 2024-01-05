package views.screen.payment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Delayed;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import controller.PaymentController;
import entity.cart.Cart;
import common.exception.PlaceOrderException;
import entity.invoice.Invoice;
import entity.order.Order;
import javafx.application.Platform;
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

		btnQRGeneration.setOnMouseClicked(e -> {
			try {
				updateQRImage();
			} catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

		btnConfirmPayment.setOnMouseClicked(e -> {
			try {
				if (qrImage.getImage() != null) {
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
					confirmToPayOrder();
					((PaymentController) getBController()).emptyCart();
				} else {
					PopupScreen.error("You have to get the QR code first");
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

void confirmToPayOrder() throws IOException, SQLException {

	PaymentController ctrl = (PaymentController) getBController();
	int transactionId = ctrl.confirmToPayOrder(invoice);
	try (ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor()) {
		executorService.scheduleAtFixedRate(() -> {
			try {
				String res = ctrl.fetchTransactionStatusById(transactionId);
				if (!res.equals("Pending")) {
					try {
						BaseScreenHandler resultScreen = new ResultScreenHandler(this.stage, Configs.RESULT_SCREEN_PATH, "Result!", res);
						resultScreen.setPreviousScreen(this);
						resultScreen.setHomeScreenHandler(homeScreenHandler);
						resultScreen.setScreenTitle("Result Screen");
						resultScreen.show();
						executorService.shutdown();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}, 0, 5, TimeUnit.SECONDS);
	}
}

	private void updateQRImage() throws Exception {
		PaymentController ctrl = (PaymentController) getBController();
		qrImage.setImage(SwingFXUtils.toFXImage(ctrl.generateVNPQR(invoice,(int) qrImage.getFitWidth(),(int) qrImage.getFitHeight()), null));
	}
}