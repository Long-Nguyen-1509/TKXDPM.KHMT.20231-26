package views.screen.order_history;

import controller.OrderHistoryController;
import entity.order.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import views.screen.BaseScreenHandler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class OrderHistoryScreenHandler extends BaseScreenHandler {

    @FXML
    private ListView<Order> orderListView;

    @FXML
    private ImageView aimsImage;

    public OrderHistoryScreenHandler(Stage stage, String screenPath) throws IOException, SQLException {
        super(stage, screenPath);
        setBController(new OrderHistoryController());
        aimsImage.setOnMouseClicked(e -> {
            homeScreenHandler.show();
        });
        updateHistory();
    }

    private void updateHistory() throws SQLException {
        // Retrieve the OrderHistoryController from BaseScreenHandler
        OrderHistoryController ctrl = (OrderHistoryController) getBController();

        List<Order> userOrderHistory = ctrl.getUserOrderHistory(2);
        System.out.println(userOrderHistory);

        ObservableList<Order> observableOrders = FXCollections.observableArrayList(userOrderHistory);

        orderListView.setItems(observableOrders);
    }
}
