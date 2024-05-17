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

public class DecksUI {

    private DecksUI() {}

    public static Node create(ObservableValue<Tile> tileToPlace,
                              ObservableValue<Integer> normalTile,
                              ObservableValue<Integer> menhirTile,
                              ObservableValue<String> text,
                              Consumer<Occupant> event) {

        VBox mainBox = new VBox();
        mainBox.getStylesheets().add("decks.css");

        HBox decksBox = new HBox();
        decksBox.setId("decks");
        mainBox.getChildren().add(decksBox);

        // Normal Tile
        StackPane tileNormal = new StackPane();
        decksBox.getChildren().add(tileNormal);

        ImageView imageNormal = new ImageView(new Image("/256/NORMAL.jpg"));
        imageNormal.setId("NORMAL");
        imageNormal.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
        imageNormal.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
        tileNormal.getChildren().add(imageNormal);

        Text textNormal = new Text();
        textNormal.textProperty().bind(normalTile.map(String::valueOf));
        tileNormal.getChildren().add(textNormal);

        // Menhir Tile
        StackPane tileMenhir = new StackPane();
        decksBox.getChildren().add(tileMenhir);

        ImageView imageMenhir = new ImageView(new Image("/256/MENHIR.jpg"));
        imageMenhir.setId("MENHIR");
        imageMenhir.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
        imageMenhir.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
        tileMenhir.getChildren().add(imageMenhir);

        Text textMenhir = new Text();
        textMenhir.textProperty().bind(menhirTile.map(String::valueOf));
        tileMenhir.getChildren().add(textMenhir);


        //Current Tile
        StackPane currentTile = new StackPane();
        currentTile.setId("next-tile");
        mainBox.getChildren().add(currentTile);

        ImageView imageCurrent = new ImageView();
        ObservableValue<Image> image = tileToPlace.map(t -> ImageLoader.largeImageForTile(t.id()));
        imageCurrent.setImage(image.getValue());
        image.addListener((i, old, next) -> imageCurrent.setImage(next));

        imageCurrent.setFitHeight(ImageLoader.LARGE_TILE_FIT_SIZE);
        imageCurrent.setFitWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
        imageCurrent.visibleProperty().bind(text.map(String::isEmpty));
        currentTile.getChildren().add(imageCurrent);


        Text textCurrent = new Text();
        textCurrent.textProperty().bind(text);
        textCurrent.visibleProperty().bind(text.map(txt -> !txt.isEmpty()));
        textCurrent.setOnMouseClicked(e -> event.accept(null));
        textCurrent.setWrappingWidth(0.8 * ImageLoader.LARGE_TILE_FIT_SIZE);
        currentTile.getChildren().add(textCurrent);

        return mainBox;
    }
}

