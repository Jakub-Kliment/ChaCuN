package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableBooleanValue;
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
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
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


        Map<Integer,Image> cache = Tiles.TILES
                .stream()
                .collect(Collectors.toMap(
                    Tile::id,
                    tile -> ImageLoader.largeImageForTile(tile.id())));


        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStylesheets().add("board.css");
        scrollPane.getStyleClass().add("board-scroll-pane");
        scrollPane.setId("board-scroll-pane");

        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("board-grid");
        gridPane.setId("board-grid");
        scrollPane.setContent(gridPane);


        for (int x = -reach; x <= reach; x++) {
            for (int y = -reach; y <= reach; y++) {

                Group group = new Group();
                Pos pos = new Pos(x, y);

                ObservableBooleanValue observableBooleanValue = group.hoverProperty();

                ObservableValue<PlacedTile> tileObservableValue = gameState.map(gs -> gs.board().tileAt(pos));


                ObservableValue<CellData> data = Bindings.createObjectBinding(() -> {
                    GameState gs = gameState.getValue();
                    Set<Integer> tileId = tileIds.getValue();
                    Rotation rotation = observableRotation.getValue();
                    boolean hoverProperty = observableBooleanValue.get();

                    Image imageData;
                    Color colorData;
                    Rotation rotationData;

                    PlacedTile tile = gs.board().tileAt(pos);

                    if (tile != null){
                        rotationData = tile.rotation();
                        imageData = cache.get(tile.id());
                        if (!tileId.isEmpty() && !tileId.contains(tile.id()))
                            colorData = Color.BLACK;
                        else
                            colorData = Color.TRANSPARENT;
                    } else if (gs.board().insertionPositions().contains(pos) &&
                            gs.nextAction() == GameState.Action.PLACE_TILE) {
                        if (hoverProperty) {
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
                        imageData = emptyTileImage;
                        colorData = Color.TRANSPARENT;
                        rotationData = Rotation.NONE;
                    }
                    return new CellData(imageData, rotationData, colorData);
                }, gameState, tileIds, observableRotation, observableBooleanValue);

                tileObservableValue.addListener((t, old, next) -> {
                    for (Occupant tileOccupant : next.potentialOccupants()){
                        Node occupantImage = Icon.newFor(next.placer(), tileOccupant.kind());
                        occupantImage.visibleProperty().bind(visibleOccupants.map(list -> list.contains(tileOccupant)));

                        String kind = tileOccupant.kind() == Occupant.Kind.PAWN ? "pawn" : "hut";
                        occupantImage.getStyleClass().add(STR."\{kind}_\{tileOccupant.zoneId()}");
                        occupantImage.setId(STR."\{kind}_\{tileOccupant.zoneId()}");

                        occupantImage.setOnMouseClicked(event -> {
                            occupant.accept(tileOccupant);
                        });
                        occupantImage.rotateProperty().bind(data.map(dt -> dt.rotation.negated().degreesCW()));
                        group.getChildren().add(occupantImage);
                    }
                    for (Zone.Meadow meadow : next.meadowZones()) {
                        for (Animal animal : meadow.animals()){
                            ImageView marker = new ImageView();
                            marker.getStyleClass().add("marker");
                            marker.setId(STR."marker_\{animal.id()}");
                            marker.visibleProperty().bind(gameState.map(gs -> gs.board().cancelledAnimals().contains(animal)));
                            group.getChildren().add(marker);
                        }
                    }
                });

                group.rotateProperty().bind(data.map((dt) -> dt.rotation.degreesCW()));

                //Image monochrome de l'effet
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


                group.setOnMouseClicked((e) -> {
                    if (gameState.getValue().tileToPlace() != null && gameState.getValue().board().insertionPositions().contains(pos)) {
                        if (e.getButton() == MouseButton.PRIMARY)
                            position.accept(pos);
                        if (e.getButton() == MouseButton.SECONDARY)
                            if (e.isAltDown()) {
                                consumerRotation.accept(Rotation.RIGHT);
                            } else {
                                consumerRotation.accept(Rotation.LEFT);
                            }
                    }
                });

                gridPane.add(group, x+reach, y+reach);

            }
        }
        scrollPane.setVvalue(0.5);
        scrollPane.setHvalue(0.5);
        return scrollPane;
    }
    private record CellData(Image image, Rotation rotation, Color color) {}
}
