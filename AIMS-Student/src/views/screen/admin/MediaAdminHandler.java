package views.screen.admin;

import common.exception.MediaNotAvailableException;
import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.media.Media;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.ImageView;
import utils.Utils;
import views.screen.FXMLScreenHandler;
import views.screen.home.HomeScreenHandler;
import views.screen.home.MediaHandler;
import views.screen.popup.PopupScreen;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public class MediaAdminHandler extends FXMLScreenHandler {

    @FXML
    protected ImageView mediaImage;

    @FXML
    protected Label mediaTitle;

    @FXML
    protected Label mediaPrice;

    @FXML
    protected Label mediaAvail;


    private static Logger LOGGER = Utils.getLogger(MediaHandler.class.getName());
    private Media media;
    private AdminHandler admin;

    public MediaAdminHandler(String screenPath, Media media, AdminHandler admin) throws IOException{
        super(screenPath);
        this.media = media;
        this.admin = admin;
    }
}
