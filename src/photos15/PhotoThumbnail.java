package photos15;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Photo;

public class PhotoThumbnail extends ListCell<Photo> {
    AnchorPane aPane = new AnchorPane();
    StackPane sPane = new StackPane();
    ImageView imageView = new ImageView();
    Label captionText = new Label(), caption = new Label();

    public PhotoThumbnail(){
        super();
        caption.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        captionText.setFont(Font.font("Arial",14.0));

        imageView.setFitHeight(50.0);
        imageView.setFitWidth(50.0);
        imageView.setPreserveRatio(true);

        StackPane.setAlignment(imageView, Pos.CENTER);
        sPane.getChildren().add(imageView);
        sPane.setPrefHeight(55.0);
        sPane.setPrefWidth(55.0);

        AnchorPane.setLeftAnchor(caption, 65.0);
        AnchorPane.setTopAnchor(caption, 10.0);
        AnchorPane.setLeftAnchor(captionText, 65.0);
        AnchorPane.setTopAnchor(captionText, 25.0);

        AnchorPane.setLeftAnchor(sPane, 0.0);
        aPane.getChildren().addAll(sPane, caption,captionText);
        aPane.setPrefHeight(50.0);
        aPane.setPrefWidth(220);
        setGraphic(aPane);
    }

    @Override
    public void updateItem(Photo photo, boolean empty){
        super.updateItem(photo, empty);
        if(photo == null){
            caption.setText("");
            captionText.setText("");
            imageView.setImage(null);
        }
        else{
            caption.setText("Caption: ");
            captionText.setText(photo.getCaption());
            imageView.setImage(new Image(photo.getPhotoFile().toURI().toString(),100,100,false,true));
        }

    }
}
