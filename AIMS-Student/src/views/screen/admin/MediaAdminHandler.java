package views.screen.admin;

import entity.media.Media;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.Configs;
import utils.Utils;
import views.screen.FXMLScreenHandler;
import views.screen.media.MediaEditHandler;

import java.io.File;
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

    @FXML
    protected Button btnEdit;

    private static Logger LOGGER = Utils.getLogger(MediaAdminHandler.class.getName());
    private Media media;
    private AdminMediaHandler admin;

    public MediaAdminHandler(String screenPath, Media media, AdminMediaHandler admin) throws IOException, SQLException {
        super(screenPath);
        this.media = media;
        this.admin = admin;
        setMediaInfo();
        btnEdit.setOnMouseClicked(e -> {
            try{
                MediaEditHandler mediaEditHandler = new MediaEditHandler(admin, Configs.ADMIN_MEDIA_EDIT_PATH, media);
                mediaEditHandler.setScreenTitle("Edit Media");
                mediaEditHandler.show();
            }catch (IOException | SQLException ex){
                LOGGER.info("Errors occured: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

    public Media getMedia(){
        return media;
    }

    private void setMediaInfo() throws SQLException {
        // set the cover image of media
        File file = new File(media.getImageURL());
        Image image = new Image(file.toURI().toString());
        mediaImage.setFitHeight(160);
        mediaImage.setFitWidth(152);
        mediaImage.setImage(image);

        mediaTitle.setText(media.getTitle());
        mediaPrice.setText(Utils.getCurrencyFormat(media.getPrice()));
        mediaAvail.setText(Integer.toString(media.getQuantityById()));

        setImage(mediaImage, media.getImageURL());
    }
}
