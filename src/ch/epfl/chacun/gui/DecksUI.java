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

        VBox box = new VBox();
        box.getStylesheets().add("decks.css");
        box.setId("decks");


        StackPane currentTile = new StackPane();
        currentTile.getStyleClass().add("next-tile");
        box.getChildren().add(currentTile);

        Text textCurrent = new Text();
        textCurrent.visibleProperty().bind(text.map(txt -> !txt.isEmpty()));
        textCurrent.setOnMouseClicked(e -> event.accept(null));
        textCurrent.setWrappingWidth(0.8 * ImageLoader.LARGE_TILE_FIT_SIZE);
        currentTile.getChildren().add(textCurrent);


        ImageView imageCurrent = new ImageView();
        ObservableValue<Image> image = tileToPlace.map(t -> ImageLoader.largeImageForTile(t.id()));
        image.addListener((i, old, next) -> {
            imageCurrent.setImage(next);
        });

        imageCurrent.setFitHeight(ImageLoader.LARGE_TILE_PIXEL_SIZE);
        imageCurrent.setFitWidth(ImageLoader.LARGE_TILE_PIXEL_SIZE);
        imageCurrent.visibleProperty().bind(text.map(String::isEmpty));
        currentTile.getChildren().add(imageCurrent);


        HBox hBoxDecks = new HBox();
        hBoxDecks.getStyleClass().add("decks");
        box.getChildren().add(hBoxDecks);

        StackPane tileMenhir = new StackPane();
        hBoxDecks.getChildren().add(tileMenhir);

        ImageView imageMenhir = new ImageView(new Image("/512/MENHIR.jpg"));
        imageMenhir.getStyleClass().add("MENHIR");
        imageMenhir.setFitWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
        imageMenhir.setFitHeight(ImageLoader.LARGE_TILE_FIT_SIZE);
        tileMenhir.getChildren().add(imageMenhir);

        Text textMenhir = new Text();
        textMenhir.textProperty().bind(menhirTile.map(i -> STR."\{i}"));
        tileMenhir.getChildren().add(textMenhir);


        StackPane tileNormal = new StackPane();
        hBoxDecks.getChildren().add(tileNormal);

        ImageView imageNormal = new ImageView(new Image("/512/NORMAL.jpg"));
        imageNormal.getStyleClass().add("NORMAL");
        imageNormal.setFitWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
        imageNormal.setFitHeight(ImageLoader.LARGE_TILE_FIT_SIZE);
        tileNormal.getChildren().add(imageNormal);

        Text textNormal = new Text();
        textNormal.textProperty().bind(normalTile.map(i -> STR."\{i}"));
        tileNormal.getChildren().add(textNormal);

        return box;
    }
}

