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
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static ch.epfl.chacun.gui.ImageLoader.*;

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

        // Demander !!!!!!!!!!
        Preconditions.checkArgument(reach > 0);

        WritableImage emptyTileImage = new WritableImage(1, 1);
        emptyTileImage
                .getPixelWriter()
                .setColor(0, 0, Color.gray(0.98));

        Map<Integer, Image> cache = new HashMap<>();

        ScrollPane boardScrollPane = new ScrollPane();
        boardScrollPane.getStylesheets().add("board.css");
        boardScrollPane.setId("board-scroll-pane");

        GridPane gridPane = new GridPane();
        gridPane.setId("board-grid");
        boardScrollPane.setContent(gridPane);

        for (int x = -reach; x <= reach; x++) {
            for (int y = -reach; y <= reach; y++) {
                Group group = new Group();
                Pos currentPos = new Pos(x, y);

                ObservableBooleanValue hoverProperty = group.hoverProperty();

                ObservableValue<PlacedTile> observableTile = gameState.map(
                        state -> state.board().tileAt(currentPos));

                ObservableValue<CellData> data = Bindings.createObjectBinding(() -> {
                    GameState gs = gameState.getValue();
                    Set<Integer> currentIds = tileIds.getValue();

                    Image imageData;
                    Color colorData;
                    Rotation rotationData;

                    PlacedTile tile = gs.board().tileAt(currentPos);

                    if (tile != null) {
                        rotationData = tile.rotation();
                        cache.putIfAbsent(tile.id(), largeImageForTile(tile.id()));
                        imageData = cache.get(tile.id());

                        if (!currentIds.isEmpty() && !currentIds.contains(tile.id()))
                            colorData = Color.BLACK;
                        else
                            colorData = Color.TRANSPARENT;

                    } else if (gs.board().insertionPositions().contains(currentPos)
                            && gs.nextAction() == GameState.Action.PLACE_TILE) {

                        if (hoverProperty.get()) {
                            cache.putIfAbsent(gs.tileToPlace().id(), largeImageForTile(gs.tileToPlace().id()));
                            imageData = cache.get(gs.tileToPlace().id());
                            rotationData = observableRotation.getValue();

                            if (gs.board().canAddTile(new PlacedTile(gs.tileToPlace(), gs.currentPlayer(), rotationData, currentPos)))
                                colorData = Color.TRANSPARENT;
                            else
                                colorData = Color.WHITE;

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
                }, gameState, tileIds, observableRotation, hoverProperty);

                observableTile.addListener((tile, old, next) -> {
                    if (old == null) {
                        for (Occupant tileOccupant : next.potentialOccupants()) {
                            Node occupantImage = Icon.newFor(next.placer(), tileOccupant.kind());
                            occupantImage.visibleProperty().bind(visibleOccupants.map(list -> list.contains(tileOccupant)));

                            String kind = tileOccupant.kind() == Occupant.Kind.PAWN ? "pawn" : "hut";
                            occupantImage.getStyleClass().add(STR."\{kind}_\{tileOccupant.zoneId()}");
                            occupantImage.setId(STR."\{kind}_\{tileOccupant.zoneId()}");

                            occupantImage.setOnMouseClicked(event -> occupant.accept(tileOccupant));

                            occupantImage.rotateProperty().bind(data.map(dt -> dt.rotation.negated().degreesCW()));
                            group.getChildren().add(occupantImage);
                        }

                        for (Zone.Meadow meadow : next.meadowZones()) {
                            for (Animal animal : meadow.animals()) {
                                ImageView marker = new ResizedImageView(MARKER_FIT_SIZE);
                                marker.getStyleClass().add("marker");
                                marker.setId(STR."marker_\{animal.id()}");

                                marker.visibleProperty().bind(gameState.map(
                                        state -> state.board().cancelledAnimals().contains(animal)));
                                group.getChildren().add(marker);
                            }
                        }
                    }
                });

                group.rotateProperty().bind(data.map((dt) -> dt.rotation.degreesCW()));

                //Image monochrome de l'effet
                ColorInput plain = new ColorInput();
                plain.paintProperty().bind(data.map(dt -> dt.color));
                plain.setHeight(NORMAL_TILE_FIT_SIZE);
                plain.setWidth(NORMAL_TILE_FIT_SIZE);

                Blend blend = new Blend(BlendMode.SRC_OVER);
                blend.setOpacity(0.5);
                blend.setTopInput(plain);
                group.setEffect(blend);

                //Image
                ImageView imageTile = new ResizedImageView(NORMAL_TILE_FIT_SIZE);
                imageTile.imageProperty().bind(data.map(dt -> dt.image));
                imageTile.visibleProperty().bind(data.map(dt -> dt.image != null));
                group.getChildren().add(imageTile);

                group.setOnMouseClicked((e) -> {
                    if (gameState.getValue().board().insertionPositions().contains(currentPos)
                            && e.isStillSincePress()) {
                        if (e.getButton() == MouseButton.PRIMARY)
                            position.accept(currentPos);

                        if (e.getButton() == MouseButton.SECONDARY) {
                            Rotation rot = e.isAltDown() ? Rotation.RIGHT : Rotation.LEFT;
                            consumerRotation.accept(rot);
                        }
                    }
                });

                gridPane.add(group, x + reach, y + reach);
            }
        }

        boardScrollPane.setVvalue(0.5);
        boardScrollPane.setHvalue(0.5);
        return boardScrollPane;
    }

    private record CellData(Image image, Rotation rotation, Color color) {}
}
