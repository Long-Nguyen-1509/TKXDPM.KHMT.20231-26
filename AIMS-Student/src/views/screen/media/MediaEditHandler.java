package views.screen.media;

import dto.MediaDto;
import entity.media.Media;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.Utils;
import views.screen.BaseScreenHandler;
import views.screen.admin.AdminMediaHandler;
import views.screen.home.MediaHandler;

import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Logger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class MediaEditHandler extends BaseScreenHandler {
    private static Logger LOGGER = Utils.getLogger(MediaEditHandler.class.getName());

    @FXML
    protected AnchorPane pane;
    @FXML
    protected ImageView mediaImage;
    @FXML
    protected Button btnChangeImage;
    @FXML
    protected TextField tfName;
    @FXML
    protected ChoiceBox<String> cbType;
    @FXML
    protected TextField tfCategory;
    @FXML
    protected TextField tfValue;
    @FXML
    protected TextField tfPrice;
    @FXML
    protected TextField tfQuantity;
    @FXML
    protected Button btnSave;
    @FXML
    protected Button btnDelete;
    @FXML
    protected Label labelWarning;

    private Media media;
    private AdminMediaHandler parent;
    private boolean isChangeImage = false;

    public MediaEditHandler(AdminMediaHandler parent, String screenPath, Media media) throws IOException, SQLException {
        super(screenPath);
        this.media = media == null ? new Media() : media;
        this.parent = parent;
        try {
            labelWarning.setVisible(false);
            btnDelete.setVisible(false);
            cbType.setItems(FXCollections.observableArrayList(Media.getMediaType()));
            cbType.setValue(Media.getMediaType().get(0));
            tfValue.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    tfPrice.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
            tfPrice.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    tfPrice.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
            tfQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    tfPrice.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
            btnChangeImage.setOnMouseClicked(e -> chooseImage());
            btnSave.setOnMouseClicked(e -> saveMediaInfo());
            btnDelete.setOnMouseClicked(e -> deleteMedia());
            setMediaInfo();
        }catch (Exception e) {
            labelWarning.setText("Có lỗi xảy ra xin vui lòng thử lại");
            labelWarning.setVisible(true);
            e.printStackTrace();
        }
    }

    private void setMediaInfo() {
        if(Objects.nonNull(media) && media.getId() != 0) {
            tfName.setText(media.getTitle());
            cbType.setValue(media.getType());
            tfCategory.setText(String.valueOf(media.getCategory()));
            tfValue.setText(String.valueOf(media.getValue()));
            tfPrice.setText(String.valueOf(media.getPrice()));
            tfQuantity.setText(String.valueOf(media.getQuantity()));

            File file = new File(media.getImageURL());
            Image image = new Image(file.toURI().toString());
            mediaImage.setFitHeight(250);
            mediaImage.setFitWidth(250);
            mediaImage.setImage(image);

            btnDelete.setVisible(true);
        }
        else {
            File file = new File("assets/images/default_images.png");
            Image image = new Image(file.toURI().toString());
            mediaImage.setFitHeight(250);
            mediaImage.setFitWidth(250);
            mediaImage.setImage(image);
        }
    }

    private void saveMediaInfo() {
        try {
            String title = tfName.getText();
            String type = cbType.getValue();
            String category = tfCategory.getText();
            long value = Long.parseLong(tfValue.getText());
            long price = Long.parseLong(tfPrice.getText());
            long quantity = Long.parseLong(tfQuantity.getText());

            String imageUrl = media.getImageURL();
            if(isChangeImage || imageUrl == null) {
                String url = mediaImage.getImage().impl_getUrl();
                URI uri = new URI(url);
                FileInputStream fileInputStream = new FileInputStream(new File(uri));
                imageUrl = "assets/images/" + type + "/" + title + ".jpg";
                Files.copy(fileInputStream, Paths.get(imageUrl), REPLACE_EXISTING);
                fileInputStream.close();
            }
            MediaDto mediaDto = new MediaDto(title, type, category, value, price, quantity, imageUrl);
            if(Objects.nonNull(media) && media.getId() != 0) {
                mediaDto.setId(media.getId());
            }
            media.save(mediaDto);
            this.closed();
            parent.reload();
        }
        catch (Exception e) {
            labelWarning.setText("Có lỗi xảy ra xin vui lòng thử lại");
            labelWarning.setVisible(true);
        }
    }

    private void deleteMedia() {
        try {
            this.media.deleteById(this.media.getId());
            this.closed();
            parent.reload();
        } catch (SQLException ex) {
            labelWarning.setText("Có lỗi xảy ra xin vui lòng thử lại");
            labelWarning.setVisible(true);
            ex.printStackTrace();
        }
    }

    private void chooseImage() {
        Stage stage = (Stage) pane.getScene().getWindow();
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose a image");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
        fc.getExtensionFilters().add(imageFilter);
        File file = fc.showOpenDialog(stage);
        if (file != null){
            Image image = new Image(file.toURI().toString(),320, 213, false, true);
            mediaImage.setImage(image);
        }
        isChangeImage = true;
    }
}
