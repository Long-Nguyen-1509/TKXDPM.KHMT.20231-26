package views.screen.admin;

import controller.AdminController;
import controller.HomeController;
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
import views.screen.home.MediaHandler;
import views.screen.invoice.InvoiceScreenHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class AdminHandler extends BaseScreenHandler implements Initializable {

    private static final Logger LOGGER = Utils.getLogger(InvoiceScreenHandler.class.getName());

    @FXML
    private Label txtUsername;

    @FXML
    private ImageView aimsImage;

    @FXML
    private ImageView avatarImage;

    @FXML
    private TextField tfSearch;

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

    public AdminHandler(Stage stage, String screenPath, User.UserAccount userAccount) throws IOException {
        super(stage, screenPath);
        this.txtUsername.setText(userAccount.getName());
    }

    public AdminController getBController() {
        return (AdminController) super.getBController();
    }

//    @Override
//    public void show() {
//        numMediaInCart.setText(String.valueOf(Cart.getCart().getListMedia().size()) + " media");
//        super.show();
//    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        setBController(new HomeController());
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

        this.setImage();

        aimsImage.setOnMouseClicked(e -> {
            addMediaHome(this.adminItems);
        });

//        cartImage.setOnMouseClicked(e -> {
//            CartScreenHandler cartScreen;
//            try {
//                LOGGER.info("User clicked to view cart");
//                cartScreen = new CartScreenHandler(this.stage, Configs.CART_SCREEN_PATH);
//                cartScreen.setHomeScreenHandler(this);
//                cartScreen.setBController(new ViewCartController());
//                cartScreen.requestToViewCart(this);
//            } catch (IOException | SQLException e1) {
//                throw new ViewCartException(Arrays.toString(e1.getStackTrace()).replaceAll(", ", "\n"));
//            }
//        });
        addMediaHome(this.adminItems);
        addMenuItem(0, "All", splitMenuBtnSearch);
        addMenuItem(1, "Book", splitMenuBtnSearch);
        addMenuItem(2, "DVD", splitMenuBtnSearch);
        addMenuItem(3, "CD", splitMenuBtnSearch);

        splitMenuBtnSearch.setOnMouseClicked(e -> searchMedia());
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
        while(!mediaItems.isEmpty()){
            hboxMedia.getChildren().forEach(node -> {
                int vid = hboxMedia.getChildren().indexOf(node);
                VBox vBox = (VBox) node;
                while(vBox.getChildren().size()<3 && !mediaItems.isEmpty()){
                    MediaHandler media = (MediaHandler) mediaItems.get(0);
                    vBox.getChildren().add(media.getContent());
                    mediaItems.remove(media);
                }
            });
            return;
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
                MediaHandler media = (MediaHandler) me;
                if (StringUtils.equals(text, "All")) {
                    filteredItems.add(media);
                } else if (media.getMedia().getTitle().toLowerCase().startsWith(text.toLowerCase())){
                    filteredItems.add(media);
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
                MediaHandler media = (MediaHandler) me;
                if (StringUtils.contains(media.getMedia().getTitle().toLowerCase(), mediaName.toLowerCase())){
                    filteredItems.add(media);
                }
            });
            addMediaHome(filteredItems);
        }
    }
}
