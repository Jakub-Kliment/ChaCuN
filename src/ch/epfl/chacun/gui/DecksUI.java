package ch.epfl.chacun.gui;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.Tile;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.function.Consumer;

import static ch.epfl.chacun.gui.ImageLoader.*;

public class DecksUI {

    private DecksUI() {}

    public static Node create(ObservableValue<Tile> tileToPlace,
                              ObservableValue<Integer> normalTile,
                              ObservableValue<Integer> menhirTile,
                              ObservableValue<String> message,
                              Consumer<Occupant> occupant) {

        VBox mainBox = new VBox();
        mainBox.getStylesheets().add("decks.css");

        HBox decksBox = new HBox();
        decksBox.setId("decks");
        StackPane normalTilePane = tilePane("NORMAL", normalTile);
        StackPane menhirTilePane = tilePane("MENHIR", menhirTile);
        decksBox.getChildren().addAll(normalTilePane, menhirTilePane);

        // Current Tile
        StackPane nextTile = new StackPane();
        nextTile.setId("next-tile");

        ImageView currentTileImage = resizedImageView(LARGE_TILE_FIT_SIZE);
        ObservableValue<Image> image = tileToPlace.map(
                tile -> ImageLoader.largeImageForTile(tile.id()));

        image.addListener((current, oldImage, nextImage) -> currentTileImage.setImage(nextImage));
        currentTileImage.setImage(image.getValue());
        currentTileImage.visibleProperty().bind(message.map(String::isEmpty));

        Text currentText = new Text();
        currentText.textProperty().bind(message);
        currentText.setWrappingWidth(0.8 * LARGE_TILE_FIT_SIZE);
        currentText.visibleProperty().bind(message.map(text -> !text.isEmpty()));
        currentText.setOnMouseClicked(event -> occupant.accept(null));

        nextTile.getChildren().addAll(currentTileImage, currentText);
        mainBox.getChildren().addAll(decksBox, nextTile);
        return mainBox;
    }

    private static StackPane tilePane(String id, ObservableValue<Integer> count) {
        StackPane pane = new StackPane();

        ImageView imageView = resizedImageView(NORMAL_TILE_FIT_SIZE);
        imageView.setId(id);

        Text text = new Text();
        text.textProperty().bind(count.map(String::valueOf));

        pane.getChildren().addAll(imageView, text);
        return pane;
    }

    private static ImageView resizedImageView(int size) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        return imageView;
    }
}
