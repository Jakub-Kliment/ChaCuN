package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Set;
import java.util.function.Consumer;

public class BoardUI {

    private BoardUI() {}

    public static Node create(int reach,
                              ObservableValue<GameState> gameState,
                              ObservableValue<Rotation> observableRotation,
                              ObservableValue<Set<Occupant>> visibleOccupants,
                              ObservableValue<Set<Integer>> tileIds,
                              Consumer<Rotation> consumerRotation,
                              Consumer<Pos> position,
                              Consumer<Occupant> occupant) {

        VBox box = new VBox();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStylesheets().add("board.css");
        scrollPane.getStyleClass().add("board-scroll-pane");
        scrollPane.setContent(box);

        Pane gridPane = new GridPane();
        gridPane.getStyleClass().add("board-grid");

        WritableImage emptyTileImage = new WritableImage(
                ImageLoader.NORMAL_TILE_FIT_SIZE,
                ImageLoader.NORMAL_TILE_FIT_SIZE);
        emptyTileImage
                .getPixelWriter()
                .setColor(0, 0, Color.gray(0.98));

        for (int i = 0; i < reach; i++) {
            for (int j = 0; j < reach; j++) {
                ImageView image = new ImageView(emptyTileImage);

                ObservableValue<Tile> tile = gameState.map()
                Group group = new Group(image);
                gridPane.getChildren().add(group);
            }
        }
        box.getChildren().add(gridPane);


        return box;
    }
}
