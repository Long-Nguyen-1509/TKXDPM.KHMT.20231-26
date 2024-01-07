package views.screen.admin;

import dto.UserDto;
import entity.user.User;
import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.media.MediaEditHandler;
import views.screen.user.UserEditHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class AdminAccountHandler extends BaseScreenHandler {
    private static Logger LOGGER = Utils.getLogger(AdminAccountHandler.class.getName());
    private User.UserAccount userAccount;

    @FXML
    private Label txtUsername;

    @FXML
    private ImageView aimsImage;

    @FXML
    private ImageView avatarImage;

    @FXML
    private TextField tfSearch;

    @FXML
    private Button btnAddAccount;

    @FXML
    private SplitMenuButton splitMenuBtnSearch;

    @FXML
    private TableView<UserDto> tableAccount;

    @FXML
    private TableColumn<UserDto, Integer> userId;

    @FXML
    private TableColumn<UserDto, String> userFullname;

    @FXML
    private TableColumn<UserDto, String> userEmail;

    @FXML
    private TableColumn<UserDto, String> userAddress;

    @FXML
    private TableColumn<UserDto, String> userPhoneNumber;

    @FXML
    private TableColumn<UserDto, String> username;

    @FXML
    private TableColumn<UserDto, String> password;

    @FXML
    private TableColumn<UserDto, String> userRole;

    public AdminAccountHandler(Stage stage, String screenPath, User.UserAccount userAccount) throws IOException, SQLException {
        super(stage, screenPath);
        this.userAccount = userAccount;
        this.txtUsername.setText(userAccount.getName());

        userId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        userFullname.setCellValueFactory(new PropertyValueFactory<>("userFullname"));
        userEmail.setCellValueFactory(new PropertyValueFactory<>("userEmail"));
        userAddress.setCellValueFactory(new PropertyValueFactory<>("userAddress"));
        userPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("userPhoneNumber"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        password.setCellValueFactory(new PropertyValueFactory<>("password"));
        userRole.setCellValueFactory(new PropertyValueFactory<>("userRole"));

        List<User> users = User.getAllUser();
        List<UserDto> userDtos = users.stream()
                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail(), user.getAddress(),
                        user.getPhone() ,user.getUsername(), user.getPassword(), user.getRole(), user))
                .collect(Collectors.toList());
        ObservableList<UserDto> userData = FXCollections.observableArrayList(userDtos);
        tableAccount.setItems(userData);

        this.setImage();
        splitMenuBtnSearch.setOnMouseClicked(e -> searchAccount());

        btnAddAccount.setOnMouseClicked(e -> {
            try{
                UserEditHandler userEditHandler = new UserEditHandler(this, Configs.ADMIN_USER_EDIT_PATH, null);
                userEditHandler.setScreenTitle("Add User");
                userEditHandler.show();
            }catch (IOException ex){
                LOGGER.info("Errors occured: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        tableAccount.setRowFactory(tv -> {
            TableRow<UserDto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton()== MouseButton.PRIMARY
                        && event.getClickCount() == 2) {

                    UserDto clickedUser = row.getItem();
                    User user = clickedUser.getSrc();
                    try{
                        UserEditHandler userEditHandler = new UserEditHandler(this, Configs.ADMIN_USER_EDIT_PATH, user);
                        userEditHandler.setScreenTitle("Edit User");
                        userEditHandler.show();
                    }catch (IOException ex){
                        LOGGER.info("Errors occured: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            });
            return row ;
        });
    }

    private void setImage(){
        // fix image path caused by fxml
        File file1 = new File(Configs.IMAGE_PATH + "/" + "Logo.png");
        Image img1 = new Image(file1.toURI().toString());
        aimsImage.setImage(img1);

        File file3 = new File(Configs.IMAGE_PATH + "/" + "avatar.png");
        Image img3 = new Image(file3.toURI().toString());
        avatarImage.setImage(img3);
    }

    private void searchAccount(){
        try {
            String nameSearch = tfSearch.getText().trim();
            if (StringUtils.isBlank(nameSearch)) {
                reload();
            } else {
                List<User> users = User.getAllUserLike(nameSearch);
                List<UserDto> userDtos = users.stream()
                        .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail(), user.getAddress(),
                                user.getPhone() ,user.getUsername(), user.getPassword(), user.getRole(), user))
                        .collect(Collectors.toList());
                ObservableList<UserDto> userData = FXCollections.observableArrayList(userDtos);
                tableAccount.setItems(userData);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reload() throws SQLException {
        List<User> users = User.getAllUser();
        List<UserDto> userDtos = users.stream()
                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail(), user.getAddress(),
                        user.getPhone() ,user.getUsername(), user.getPassword(), user.getRole(), user))
                .collect(Collectors.toList());
        ObservableList<UserDto> userData = FXCollections.observableArrayList(userDtos);
        tableAccount.setItems(userData);
    }
}
