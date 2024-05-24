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

/**
 * A class to create the Decks UI. It contains the decks of tiles and the current tile to place.
 * The current tile is displayed as an image if there is no message to display, otherwise
 * there is a message displayed as a text that tells the player what the next step is.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public final class DecksUI {

    /**
     * Private constructor to prevent instantiation.
     */
    private DecksUI() {}

    /**
     * Creates a UI element displaying the decks of tiles (normal and menhir) with the number
     * of tiles left of their kind and the current tile to place. The current tile is displayed
     * as an image if there is no message to display, otherwise the message is displayed as a text
     * that tells the player how to continue.
     *
     * @param tileToPlace the observable value of the tile to place
     * @param normalTile the observable value of the number of normal tiles left
     * @param menhirTile the observable value of the number of menhir tiles left
     * @param message the observable value of the message to display
     * @param occupant the consumer that handles the action with the occupant
     * @return the created UI element
     */
    public static Node create(ObservableValue<Tile> tileToPlace,
                              ObservableValue<Integer> normalTile,
                              ObservableValue<Integer> menhirTile,
                              ObservableValue<String> message,
                              Consumer<Occupant> occupant) {

        // Main container for the graphical element
        VBox mainBox = new VBox();
        mainBox.getStylesheets().add("decks.css");

        // Decks of tiles (normal and menhir)
        HBox decksBox = new HBox();
        decksBox.setId("decks");
        StackPane normalTilePane = tilePane("NORMAL", normalTile);
        StackPane menhirTilePane = tilePane("MENHIR", menhirTile);
        decksBox.getChildren().addAll(normalTilePane, menhirTilePane);

        // Current tile to place
        StackPane nextTile = new StackPane();
        nextTile.setId("next-tile");

        // Image of the current tile
        ImageView currentTileImage = new ResizedImageView(LARGE_TILE_FIT_SIZE);
        ObservableValue<Image> image = tileToPlace.map(
                tile -> ImageLoader.largeImageForTile(tile.id()));

        image.addListener((current, oldImage, nextImage) -> currentTileImage.setImage(nextImage));
        currentTileImage.setImage(image.getValue());
        currentTileImage.visibleProperty().bind(message.map(String::isEmpty));

        // Text of the message with the next move which is displayed to the player
        Text currentText = new Text();
        currentText.textProperty().bind(message);
        currentText.setWrappingWidth(0.8 * LARGE_TILE_FIT_SIZE);
        currentText.visibleProperty().bind(message.map(text -> !text.isEmpty()));
        currentText.setOnMouseClicked(event -> occupant.accept(null));

        nextTile.getChildren().addAll(currentTileImage, currentText);
        mainBox.getChildren().addAll(decksBox, nextTile);
        return mainBox;
    }

    /**
     * Private helper method to create a stack pane with an image of the back of the tile
     * and a text on top of it which displays the number of tiles left.
     *
     * @param id the id of the tile
     * @param count the observable value of the number of tiles left
     * @return the created stack pane
     */
    private static StackPane tilePane(String id, ObservableValue<Integer> count) {
        // Stack pane which represents the tile deck with the number of tiles left
        StackPane pane = new StackPane();
        ImageView imageView = new ResizedImageView(NORMAL_TILE_FIT_SIZE);
        imageView.setId(id);

        // Text displaying the number of tiles left
        Text text = new Text();
        text.textProperty().bind(count.map(String::valueOf));

        pane.getChildren().addAll(imageView, text);
        return pane;
    }
}
