package views.screen.admin;

import entity.user.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;

import java.io.IOException;
import java.util.logging.Logger;

public class AdminHandler extends BaseScreenHandler{
    private static Logger LOGGER = Utils.getLogger(AdminMediaHandler.class.getName());

    @FXML
    private MenuItem menuMedia;

    @FXML
    private MenuItem menuAccount;

    @FXML
    private MenuItem menuOrder;

    @FXML
    private AnchorPane menuPane;

    private User.UserAccount userAccount;

    public AdminHandler(Stage stage, String screenPath, User.UserAccount userAccount) throws IOException {
        super(stage, screenPath);
        this.userAccount = userAccount;

        AdminMediaHandler defaultMenuHandler = new AdminMediaHandler(this.stage, Configs.ADMIN_MEDIA_PANE, userAccount);
        defaultMenuHandler.setScreenTitle("Home Screen");
        menuPane.getChildren().add(defaultMenuHandler.getContent());

        menuMedia.setOnAction(event -> {
            try {
                AdminMediaHandler adminMediaHandler = new AdminMediaHandler(this.stage, Configs.ADMIN_MEDIA_PANE, userAccount);
                adminMediaHandler.setScreenTitle("Admin Media Screen");
                menuPane.getChildren().clear();
                menuPane.getChildren().add(adminMediaHandler.getContent());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

        menuAccount.setOnAction(event -> {
            try {
                AdminAccountHandler adminAccountHandler = new AdminAccountHandler(this.stage, Configs.ADMIN_ACCOUNT_PANE, userAccount);
                adminAccountHandler.setScreenTitle("Admin Account Screen");
                menuPane.getChildren().clear();
                menuPane.getChildren().add(adminAccountHandler.getContent());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

        menuOrder.setOnAction(event -> {

        });
    }
}
