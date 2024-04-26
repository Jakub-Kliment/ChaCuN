package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import ch.epfl.chacun.tile.Tiles;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

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


        WritableImage emptyTileImage = new WritableImage(1, 1);
        emptyTileImage
                .getPixelWriter()
                .setColor(0, 0, Color.gray(0.98));

        Map<Integer,Image> cache = Tiles.TILES
                .stream()
                .collect(Collectors.toMap(
                    Tile::id,
                    tile -> ImageLoader.largeImageForTile(tile.id())));

        ObservableValue<Set<Pos>> insertionPosition = gameState.map(gs -> gs.board().insertionPositions());
        ObservableValue<Set<Animal>> cancelAnimal = gameState.map(gs -> gs.board().cancelledAnimals());




        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStylesheets().add("board.css");
        scrollPane.getStyleClass().add("board-scroll-pane");

        Pane gridPane = new GridPane();
        gridPane.getStyleClass().add("board-grid");
        scrollPane.setContent(gridPane);



        for (int x = 0; x < reach; x++) {
            for (int y = 0; y < reach; y++) {
                Group group = new Group();
                ImageView image = new ImageView(emptyTileImage);
                group.getChildren().add(image);
                image.setImage(emptyTileImage);
                int finalX = x;
                int finalY = y;
                gameState.addListener((o, old, next) -> {
                    PlacedTile tile = next.board().tileAt(new Pos(finalX, finalY));
                    PlacedTile lastPlaceTile = next.board().lastPlacedTile();
                    if(tile != null && lastPlaceTile != null && tile.id() == lastPlaceTile.id()){
                        image.setImage(cache.get(tile.id()));
                        for (Occupant tileOccupant : tile.potentialOccupants()){
                            Node occupantImage = Icon.newFor(next.currentPlayer(), tileOccupant.kind());

                        }
                    }

                });
                cancelAnimal.addListener((o, old, next) -> {
                    for (Animal animal : next){

                    }
                });











                gridPane.getChildren().add(group);
            }
        }


        return scrollPane;
    }
}
