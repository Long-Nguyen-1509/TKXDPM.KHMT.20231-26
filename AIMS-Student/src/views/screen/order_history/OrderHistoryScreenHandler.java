package views.screen.order_history;

import controller.OrderHistoryController;
import entity.order.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import views.screen.BaseScreenHandler;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;


public class OrderHistoryScreenHandler extends BaseScreenHandler {

    @FXML
    private ListView<Order> orderListView;

    @FXML
    private ImageView aimsImage;

    public OrderHistoryScreenHandler(Stage stage, String screenPath) throws IOException, SQLException {
        super(stage, screenPath);
        File file = new File("assets/images/Logo.png");
        Image im = new Image(file.toURI().toString());
        aimsImage.setImage(im);
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
        orderListView.setCellFactory(param -> new ListCell<Order>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item==null) {
                    setGraphic(null);
                    setText(null);
                    // other stuff to do...

                }else{

                    // set the width's
                    setMinWidth(param.getWidth());
                    setMaxWidth(param.getWidth());
                    setPrefWidth(param.getWidth());

                    // allow wrapping
                    setWrapText(true);

                    setText(item.toString());


                }
            }
        });
    }
}
