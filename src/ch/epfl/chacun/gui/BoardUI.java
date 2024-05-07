package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import ch.epfl.chacun.tile.Tiles;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Blend;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

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


        //J'ai oulie d'utiliser le cache
        Map<Integer,Image> cache = Tiles.TILES
                .stream()
                .collect(Collectors.toMap(
                    Tile::id,
                    tile -> ImageLoader.largeImageForTile(tile.id())));


        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStylesheets().add("board.css");
        scrollPane.getStyleClass().add("board-scroll-pane");

        Pane gridPane = new GridPane();
        gridPane.getStyleClass().add("board-grid");
        scrollPane.setContent(gridPane);



        for (int x = -reach; x <= reach; x++) {
            for (int y = -reach; y <= reach; y++) {

                Group group = new Group();
                Pos pos = new Pos(x, y);


                ObservableValue<CellData> data = Bindings.createObjectBinding(() -> {
                    GameState gs = gameState.getValue();
                    Set<Integer> tileId = tileIds.getValue();
                    Rotation rotation = observableRotation.getValue();
                    Image imageData;
                    Color colorData;
                    Rotation rotationData;

                    if (gs.board().tileAt(pos) != null){
                        rotationData = gs.board().tileAt(pos).rotation();
                        imageData = ImageLoader.largeImageForTile(gs.board().tileAt(pos).id());
                        if (!tileId.isEmpty() && !tileId.contains(gs.board().tileAt(pos).id()))
                            colorData = Color.BLACK;
                        else
                            colorData = Color.TRANSPARENT;
                    } else if (gs.board().insertionPositions().contains(pos) && gs.tileToPlace() != null) {
                        if (group.hoverProperty().get()) {
                            if (gs.board().canAddTile(new PlacedTile(gs.tileToPlace(), gs.currentPlayer(), rotation, pos))) {
                                imageData = ImageLoader.largeImageForTile(gs.tileToPlace().id());
                                colorData = Color.TRANSPARENT;
                                rotationData = rotation;
                            } else {
                                imageData = emptyTileImage;
                                colorData = Color.WHITE;
                                rotationData = null;// ????????
                            }
                        } else {
                            imageData = emptyTileImage;
                            colorData = ColorMap.fillColor(gs.currentPlayer());
                            rotationData = null;
                        }
                    } else {
                        imageData = emptyTileImage;
                        colorData = Color.TRANSPARENT;
                        rotationData = null;
                    }
                    return new CellData(imageData, rotationData, colorData);
                }, gameState, tileIds, observableRotation);

                group.rotateProperty().bind(data.map((dt) -> dt.rotation.degreesCW()));
                ImageView imageTile = new ImageView();
                imageTile.imageProperty().bind(data.map(dt -> dt.image));
                group.getChildren().add(imageTile);







                gameState.addListener((o, old, next) -> {
                    PlacedTile tile = next.board().tileAt(pos);
                    PlacedTile lastPlaceTile = next.board().lastPlacedTile();
                    if(tile != null && lastPlaceTile != null && tile.id() == lastPlaceTile.id()){
                        image.setImage(cache.get(tile.id()));

                        for (Occupant tileOccupant : tile.potentialOccupants()){
                            Node occupantImage = Icon.newFor(tile.placer(), tileOccupant.kind());
                            occupantImage.visibleProperty().bind(visibleOccupants.map(list -> list.contains(tileOccupant)));
                            occupantImage.getStyleClass().add(STR."\{tileOccupant.kind()}_\{tileOccupant.zoneId()}");
                            occupantImage.setOnMouseClicked(event -> occupant.accept(tileOccupant));
                            occupantImage.rotateProperty().bind(observableRotation.map(rot -> rot.negated().degreesCW()));
                            group.getChildren().add(occupantImage);
                        }

                        for (Zone.Meadow meadow : tile.meadowZones()){
                            for (Animal animal : meadow.animals()){
                                ImageView marker= new ImageView("/marker.png");
                                marker.visibleProperty().bind(gameState.map(gs -> gs.board().cancelledAnimals().contains(animal)));
                                marker.rotateProperty().bind(observableRotation.map(rot -> rot.negated().degreesCW()));
                                group.getChildren().add(marker);
                            }
                        }
                    }
                });










                //Ajouter a la bonne pos
                gridPane.getChildren().add(group);
            }
        }
        return scrollPane;
    }
    private record CellData(Image image, Rotation rotation, Color color){}
}
