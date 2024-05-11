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

import java.util.Objects;
import java.util.function.Consumer;

public class DecksUI {

    private DecksUI() {}

    public static Node create(ObservableValue<Tile> tileToPlace,
                              ObservableValue<Integer> normalTile,
                              ObservableValue<Integer> menhirTile,
                              ObservableValue<String> text,
                              Consumer<Occupant> event) {

        VBox box = new VBox();
        box.getStylesheets().add("decks.css");
        box.setId("decks");

        HBox hBoxDecks = new HBox();
        hBoxDecks.getStyleClass().add("decks");
        hBoxDecks.setId("decks");
        box.getChildren().add(hBoxDecks);

        //Normale Tile
        StackPane tileNormal = new StackPane();
        hBoxDecks.getChildren().add(tileNormal);

        ImageView imageNormal = new ImageView(new Image("/512/NORMAL.jpg"));
        imageNormal.getStyleClass().add("NORMAL");
        imageNormal.setId("NORMAL");
        imageNormal.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
        imageNormal.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
        tileNormal.getChildren().add(imageNormal);

        Text textNormal = new Text();
        textNormal.textProperty().bind(normalTile.map(i -> STR."\{i}"));
        tileNormal.getChildren().add(textNormal);

        //Mehnir Tile
        StackPane tileMenhir = new StackPane();
        hBoxDecks.getChildren().add(tileMenhir);

        ImageView imageMenhir = new ImageView(new Image("/512/MENHIR.jpg"));
        imageMenhir.getStyleClass().add("MENHIR");
        imageMenhir.setId("MENHIR");
        imageMenhir.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
        imageMenhir.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
        tileMenhir.getChildren().add(imageMenhir);

        Text textMenhir = new Text();
        textMenhir.textProperty().bind(menhirTile.map(i -> STR."\{i}"));
        tileMenhir.getChildren().add(textMenhir);


        //Current Tile
        StackPane currentTile = new StackPane();
        currentTile.getStyleClass().add("next-tile");
        currentTile.setId("next-tile");
        box.getChildren().add(currentTile);

        ImageView imageCurrent = new ImageView();
        ObservableValue<Image> image = tileToPlace.map(t -> ImageLoader.largeImageForTile(t.id()));
        imageCurrent.setImage(image.getValue());
        image.addListener((i, old, next) -> {
            imageCurrent.setImage(next);
        });

        imageCurrent.setFitHeight(ImageLoader.LARGE_TILE_FIT_SIZE);
        imageCurrent.setFitWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
        imageCurrent.visibleProperty().bind(text.map(txt -> txt.isEmpty()));
        currentTile.getChildren().add(imageCurrent);


        Text textCurrent = new Text();
        //textCurrent.getStyleClass().add("next-tile");
        textCurrent.setId("next-tile");
        textCurrent.textProperty().bind(text);
        textCurrent.visibleProperty().bind(text.map(txt -> !txt.isEmpty()));
        textCurrent.setOnMouseClicked(e -> event.accept(null));
        textCurrent.setWrappingWidth(0.8 * ImageLoader.LARGE_TILE_FIT_SIZE);
        currentTile.getChildren().add(textCurrent);

        return box;
    }
}

