package views.screen.admin;

import controller.AdminController;
import entity.media.Media;
import entity.user.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import utils.Configs;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.media.MediaEditHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class AdminMediaHandler extends BaseScreenHandler {

    private static Logger LOGGER = Utils.getLogger(AdminMediaHandler.class.getName());

    @FXML
    private Label txtUsername;

    @FXML
    private ImageView aimsImage;

    @FXML
    private ImageView avatarImage;

    @FXML
    private TextField tfSearch;

    @FXML
    private Button btnAddMedia;

    @FXML
    private VBox vboxMedia1;

    @FXML
    private VBox vboxMedia2;

    @FXML
    private VBox vboxMedia3;

    @FXML
    private HBox hboxMedia;

    @FXML
    private SplitMenuButton splitMenuBtnSearch;

    private List adminItems;
    private User.UserAccount userAccount;

    public AdminMediaHandler(Stage stage, String screenPath, User.UserAccount userAccount) throws IOException {
        super(stage, screenPath);
        this.userAccount = userAccount;
        this.txtUsername.setText(userAccount.getName());
        setBController(new AdminController());
        try{
            List medium = getBController().getAllMedia();
            this.adminItems = new ArrayList<>();
            for (Object object : medium) {
                Media media = (Media)object;
                MediaAdminHandler mediaAdminHandler = new MediaAdminHandler(Configs.ADMIN_MEDIA_PATH, media, this);
                this.adminItems.add(mediaAdminHandler);
            }
        }catch (SQLException | IOException e){
            LOGGER.info("Errors occured: " + e.getMessage());
            e.printStackTrace();
        }
        addMediaHome(this.adminItems);
        addMenuItem(0, "All", splitMenuBtnSearch);
        addMenuItem(1, "Book", splitMenuBtnSearch);
        addMenuItem(2, "DVD", splitMenuBtnSearch);
        addMenuItem(3, "CD", splitMenuBtnSearch);
        this.setImage();

        aimsImage.setOnMouseClicked(e -> {
            addMediaHome(this.adminItems);
        });

        btnAddMedia.setOnMouseClicked(e -> {
            try{
                MediaEditHandler mediaEditHandler = new MediaEditHandler(this, Configs.ADMIN_MEDIA_EDIT_PATH, null);
                mediaEditHandler.setScreenTitle("Add Media");
                mediaEditHandler.show();
            }catch (IOException | SQLException ex){
                LOGGER.info("Errors occured: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        splitMenuBtnSearch.setOnMouseClicked(e -> searchMedia());
    }

    public AdminController getBController() {
        return (AdminController) super.getBController();
    }

    public void reload() {
        try{
            List medium = getBController().getAllMedia();
            this.adminItems = new ArrayList<>();
            for (Object object : medium) {
                Media media = (Media)object;
                MediaAdminHandler mediaAdminHandler = new MediaAdminHandler(Configs.ADMIN_MEDIA_PATH, media, this);
                this.adminItems.add(mediaAdminHandler);
            }
        }catch (SQLException | IOException e){
            LOGGER.info("Errors occured: " + e.getMessage());
            e.printStackTrace();
        }
        addMediaHome(this.adminItems);
    }

    public void setImage(){
        // fix image path caused by fxml
        File file1 = new File(Configs.IMAGE_PATH + "/" + "Logo.png");
        Image img1 = new Image(file1.toURI().toString());
        aimsImage.setImage(img1);

        File file3 = new File(Configs.IMAGE_PATH + "/" + "avatar.png");
        Image img3 = new Image(file3.toURI().toString());
        avatarImage.setImage(img3);
    }

    public void addMediaHome(List items){
        ArrayList mediaItems = (ArrayList)((ArrayList) items).clone();
        hboxMedia.getChildren().forEach(node -> {
            VBox vBox = (VBox) node;
            vBox.getChildren().clear();
        });

        int row = (mediaItems.size() % 4 == 0) ? (mediaItems.size() / 4) : ((mediaItems.size() / 4) + 1);
        for(int i = 1; i <= row; i++) {
            hboxMedia.getChildren().forEach(node -> {
                if(!mediaItems.isEmpty()) {
                    VBox vBox = (VBox) node;
                    MediaAdminHandler mediaAdmin = (MediaAdminHandler) mediaItems.get(0);
                    vBox.getChildren().add(mediaAdmin.getContent());
                    mediaItems.remove(mediaAdmin);
                }
            });
        }
    }

    private void addMenuItem(int position, String text, MenuButton menuButton){
        MenuItem menuItem = new MenuItem();
        Label label = new Label();
        label.prefWidthProperty().bind(menuButton.widthProperty().subtract(31));
        label.setText(text);
        label.setTextAlignment(TextAlignment.RIGHT);
        menuItem.setGraphic(label);
        menuItem.setOnAction(e -> {
            // empty home media
            hboxMedia.getChildren().forEach(node -> {
                VBox vBox = (VBox) node;
                vBox.getChildren().clear();
            });

            // filter only media with the choosen category
            List filteredItems = new ArrayList<>();
            adminItems.forEach(me -> {
                MediaAdminHandler mediaAdmin = (MediaAdminHandler) me;
                if (StringUtils.equals(text, "All")) {
                    filteredItems.add(mediaAdmin);
                } else if (mediaAdmin.getMedia().getTitle().toLowerCase().startsWith(text.toLowerCase())){
                    filteredItems.add(mediaAdmin);
                }
            });

            // fill out the home with filted media as category
            addMediaHome(filteredItems);
        });
        menuButton.getItems().add(position, menuItem);
    }

    private void searchMedia() {
        String mediaName = tfSearch.getText().trim();
        if(StringUtils.isBlank(mediaName)) {
            addMediaHome(adminItems);
        }
        else {
            List filteredItems = new ArrayList<>();
            adminItems.forEach(me -> {
                MediaAdminHandler mediaAdmin = (MediaAdminHandler) me;
                if (StringUtils.contains(mediaAdmin.getMedia().getTitle().toLowerCase(), mediaName.toLowerCase())){
                    filteredItems.add(mediaAdmin);
                }
            });
            addMediaHome(filteredItems);
        }
    }
}
