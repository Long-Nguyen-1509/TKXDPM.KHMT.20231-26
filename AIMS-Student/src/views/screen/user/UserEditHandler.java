package views.screen.user;

import dto.UserDto;
import entity.user.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.admin.AdminAccountHandler;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class UserEditHandler extends BaseScreenHandler {
    private static Logger LOGGER = Utils.getLogger(UserEditHandler.class.getName());
    private User user;
    private AdminAccountHandler parent;

    @FXML
    private TextField tfName;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfAddress;

    @FXML
    private TextField tfPhoneNumber;

    @FXML
    private TextField tfUsername;

    @FXML
    private TextField tfPassword;

    @FXML
    protected Button btnSave;

    @FXML
    protected Button btnDelete;

    @FXML
    protected Label labelWarning;

    public UserEditHandler(AdminAccountHandler parent, String screenPath, User user) throws IOException {
        super(screenPath);
        this.user = user == null ? new User() : user;
        this.parent = parent;

        try {
            labelWarning.setVisible(false);
            btnDelete.setVisible(false);
            setUserInfo();
            btnSave.setOnMouseClicked(e -> {
                try{
                    User upsertUser = new User();
                    upsertUser.setName(tfName.getText());
                    upsertUser.setEmail(tfEmail.getText());
                    upsertUser.setAddress(tfAddress.getText());
                    upsertUser.setPhone(tfPhoneNumber.getText());
                    upsertUser.setUsername(tfUsername.getText());
                    upsertUser.setPassword(tfPassword.getText());
                    upsertUser.setRole(User.UserRole.USER.getValue());

                    if(this.user != null && this.user.getId() != 0) {
                        upsertUser.setId(this.user.getId());
                        upsertUser.setRole(this.user.getRole());
                    }
                    this.user.save(upsertUser);
                    this.closed();
                    parent.reload();
                } catch (Exception ex) {
                    labelWarning.setText("Có lỗi xảy ra xin vui lòng thử lại");
                    labelWarning.setVisible(true);
                    ex.printStackTrace();
                }
            });

            btnDelete.setOnMouseClicked(e -> {
                try {
                    this.user.deleteById(this.user.getId());
                    this.closed();
                    parent.reload();
                } catch (Exception ex) {
                    labelWarning.setText("Có lỗi xảy ra xin vui lòng thử lại");
                    labelWarning.setVisible(true);
                    ex.printStackTrace();
                }
            });
        } catch (Exception ex) {
            labelWarning.setText("Có lỗi xảy ra xin vui lòng thử lại");
            labelWarning.setVisible(true);
            ex.printStackTrace();
        }

    }

    private void setUserInfo() {
        if(Objects.nonNull(user) && user.getId() != 0) {
            tfName.setText(user.getName());
            tfEmail.setText(user.getEmail());
            tfAddress.setText(user.getAddress());
            tfPhoneNumber.setText(user.getPhone());
            tfUsername.setText(user.getUsername());
            tfPassword.setText(user.getPassword());
            btnDelete.setVisible(true);
        }
    }
}
