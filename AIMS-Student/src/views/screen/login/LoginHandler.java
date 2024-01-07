package views.screen.login;

import common.exception.LoginException;
import controller.LoginController;
import entity.user.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.admin.AdminHandler;
import views.screen.admin.AdminMediaHandler;
import views.screen.home.HomeScreenHandler;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class LoginHandler extends BaseScreenHandler implements Initializable {
    public static Logger LOGGER = Utils.getLogger(LoginHandler.class.getName());

    public LoginHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    @FXML
    private TextField tfUsername;

    @FXML
    private PasswordField tPassword;

    @FXML
    private Text txtDanger;

    @FXML
    private Button btnLogin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setBController(new LoginController());
        btnLogin.setOnMouseClicked(event -> {
            String username = tfUsername.getText();
            String password = tPassword.getText();
            if(StringUtils.isBlank(username)) {
                txtDanger.setText("username không được để trống");
                return;
            }

            if(StringUtils.isBlank(password)) {
                txtDanger.setText("password không được để trống");
                return;
            }

            try {
                // call controller login trả về thông tin tài khoản
                User.UserAccount userAccount = ((LoginController) getBController()).login(username, password);

                // nếu là user thì vào màn khách hàng
                if(Objects.equals(userAccount.getRole(), User.UserRole.USER)) {
                    LOGGER.info("login with client " + userAccount.getUsername());
                    HomeScreenHandler homeHandler = new HomeScreenHandler(this.stage, Configs.HOME_PATH, userAccount);
                    homeHandler.setScreenTitle("Home Screen");
                    homeHandler.show();
                }

                // nếu là admin thì vào màn admin
                if(Objects.equals(userAccount.getRole(), User.UserRole.ADMIN)) {
                    LOGGER.info("login with admin " + userAccount.getUsername());
                    AdminHandler adminHandler = new AdminHandler(this.stage, Configs.ADMIN_PATH, userAccount);
                    adminHandler.setScreenTitle("Home Screen");
                    adminHandler.show();
                }
            }
            catch (LoginException loginException) {
                txtDanger.setText(loginException.getMessage());
            }
            catch (Exception e) {
                txtDanger.setText("có lỗi xảy ra, vui lòng thử lại");
            }
        });
    }
}
