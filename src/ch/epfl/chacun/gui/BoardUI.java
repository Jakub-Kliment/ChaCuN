package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import ch.epfl.chacun.tile.Tiles;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
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

        GridPane gridPane = new GridPane();
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
//                    Set<Occupant> visibleOccupant = visibleOccupants.getValue();
                    Image imageData;
                    Color colorData;
                    Rotation rotationData;
                    PlacedTile tile = gs.board().tileAt(pos);

                    if (tile != null){
                        rotationData = tile.rotation();
                        imageData = cache.get(tile.id());
//                        for (Occupant tileOccupant : tile.potentialOccupants()){
//                            Node occupantImage = Icon.newFor(tile.placer(), tileOccupant.kind());
//                            occupantImage.visibleProperty().set(visibleOccupant.contains(tileOccupant));
//                            occupantImage.getStyleClass().add(STR."\{tileOccupant.kind()}_\{tileOccupant.zoneId()}");
//                            occupantImage.setOnMouseClicked(event -> occupant.accept(tileOccupant));
//                            occupantImage.rotateProperty().set(rotationData.negated().degreesCW());
//                            group.getChildren().add(occupantImage);
//                        }
//
//                        for (Zone.Meadow meadow : tile.meadowZones()){
//                            for (Animal animal : meadow.animals()){
//                                ImageView marker= new ImageView("/marker.png");
//                                marker.visibleProperty().set(gs.board().cancelledAnimals().contains(animal));
//                                marker.rotateProperty().set(rotationData.negated().degreesCW());
//                                group.getChildren().add(marker);
//                            }
//                        }
                        if (!tileId.isEmpty() && !tileId.contains(tile.id()))
                            colorData = Color.BLACK;
                        else
                            colorData = Color.TRANSPARENT;
                    } else if (gs.board().insertionPositions().contains(pos)) {
                        if (group.hoverProperty().get()) {
                            imageData = cache.get(gs.tileToPlace().id());
                            rotationData = rotation;
                            if (gs.board().canAddTile(new PlacedTile(gs.tileToPlace(), gs.currentPlayer(), rotation, pos))) {
                                colorData = Color.TRANSPARENT;
                            } else {
                                colorData = Color.WHITE;
                            }
                        } else {
                            imageData = emptyTileImage;
                            colorData = ColorMap.fillColor(gs.currentPlayer());
                            rotationData = Rotation.NONE;
                        }
                    } else {
                        imageData = null;
                        colorData = Color.TRANSPARENT;
                        rotationData = Rotation.NONE;
                    }
                    return new CellData(imageData, rotationData, colorData);
                }, gameState, tileIds, observableRotation, visibleOccupants);

                group.rotateProperty().bind(data.map((dt) -> dt.rotation.degreesCW()));

                //Image monochrome
                ColorInput plain = new ColorInput();
                plain.paintProperty().bind(data.map(dt -> dt.color));
                plain.setHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
                plain.setWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);


                Blend blend = new Blend(BlendMode.SRC_OVER);
                blend.setOpacity(0.5);
                blend.setTopInput(plain);
                group.setEffect(blend);

                //Image
                ImageView imageTile = new ImageView();
                imageTile.imageProperty().bind(data.map(dt -> dt.image));
                imageTile.setFitHeight(ImageLoader.NORMAL_TILE_FIT_SIZE);
                imageTile.setFitWidth(ImageLoader.NORMAL_TILE_FIT_SIZE);
                imageTile.visibleProperty().bind(data.map(dt -> dt.image != null));
                group.getChildren().add(imageTile);







//                gameState.addListener((o, old, next) -> {
//                    PlacedTile tile = next.board().tileAt(pos);
//                    PlacedTile lastPlaceTile = next.board().lastPlacedTile();
//                    if(tile != null && lastPlaceTile != null && tile.id() == lastPlaceTile.id()){
//
//                        for (Occupant tileOccupant : tile.potentialOccupants()){
//                            Node occupantImage = Icon.newFor(tile.placer(), tileOccupant.kind());
//                            occupantImage.visibleProperty().bind(visibleOccupants.map(list -> list.contains(tileOccupant)));
//                            occupantImage.getStyleClass().add(STR."\{tileOccupant.kind()}_\{tileOccupant.zoneId()}");
//                            occupantImage.setOnMouseClicked(event -> occupant.accept(tileOccupant));
//                            occupantImage.rotateProperty().bind(observableRotation.map(rot -> rot.negated().degreesCW()));
//                            group.getChildren().add(occupantImage);
//                        }
//
//                        for (Zone.Meadow meadow : tile.meadowZones()){
//                            for (Animal animal : meadow.animals()){
//                                ImageView marker= new ImageView("/marker.png");
//                                marker.visibleProperty().bind(gameState.map(gs -> gs.board().cancelledAnimals().contains(animal)));
//                                marker.rotateProperty().bind(observableRotation.map(rot -> rot.negated().degreesCW()));
//                                group.getChildren().add(marker);
//                            }
//                        }
//                    }
//                });









                //TODO Ajouter a la bonne pos
                gridPane.add(group, x+reach, y+reach);
            }
        }
        return scrollPane;
    }
    private record CellData(Image image, Rotation rotation, Color color){}
}
