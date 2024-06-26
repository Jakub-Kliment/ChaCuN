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

/**
 * A UI utility class that consists of a single public static function create.
 * The main goal of this class is to create a part of the GUI that displays
 * the board of the game. The board is displayed as a grid of tiles, where each tile
 * can contain a placed tile and its occupants. The board is scrollable and the
 * player can interact with it by clicking on or hovering over the tiles.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public final class BoardUI {

    /**
     * Private constructor to prevent instantiation.
     */
    private BoardUI() {}

    /**
     * Creates a UI element displaying the board of the game.
     * The board is displayed as a grid of tiles, where each tile can contain a placed tile
     * and its occupants. It contains a scroll pane that allows the player to scroll through
     * the board. The player can interact with the board by clicking on or hovering over the tiles.
     * The board is created based on the given parameters and the observable values of the game state,
     * the rotation of the tile to place, the set of visible occupants, and the set of tile ids.
     * The consumers handle the rotation of the tile, the position of the tile, and the occupant.
     *
     *
     * @param reach the reach of the board
     * @param gameStateO the observable value of the game state
     * @param rotationO the observable value of the rotation of the tile to place
     * @param visibleOccupantsO the observable value of the set of visible occupants
     * @param tileIdsO the observable value of the set of tile ids
     * @param consumerRotation the consumer that handles the rotation of the tile
     * @param position the consumer that handles the position of the tile
     * @param occupant the consumer that handles the occupant
     * @return the created UI element displaying the board
     */
    public static Node create(int reach,
                              ObservableValue<GameState> gameStateO,
                              ObservableValue<Rotation> rotationO,
                              ObservableValue<Set<Occupant>> visibleOccupantsO,
                              ObservableValue<Set<Integer>> tileIdsO,
                              Consumer<Rotation> consumerRotation,
                              Consumer<Pos> position,
                              Consumer<Occupant> occupant) {

        Preconditions.checkArgument(reach > 0);
        // Empty tile image in a visible shade of gray
        WritableImage emptyTileImage = new WritableImage(1, 1);
        emptyTileImage
                .getPixelWriter()
                .setColor(0, 0, Color.gray(0.98));

        // Cache for tile images
        Map<Integer, Image> cache = new HashMap<>();

        // Scroll pane for the board
        ScrollPane boardScrollPane = new ScrollPane();
        boardScrollPane.getStylesheets().add("board.css");
        boardScrollPane.setId("board-scroll-pane");

        // Grid pane for the board
        GridPane gridPane = new GridPane();
        gridPane.setId("board-grid");
        boardScrollPane.setContent(gridPane);

        // Loop through all the tiles in the reach of the board
        for (int x = -reach; x <= reach; x++) {
            for (int y = -reach; y <= reach; y++) {
                // Group for one tile
                Group group = new Group();
                Pos currentPos = new Pos(x, y);

                ObservableBooleanValue hoverProperty = group.hoverProperty();

                ObservableValue<PlacedTile> observableTile = gameStateO.map(
                        state -> state.board().tileAt(currentPos));

                // Observable data for the cell changing based on the game state
                ObservableValue<CellData> cellData = Bindings.createObjectBinding(() -> {
                    GameState state = gameStateO.getValue();
                    Set<Integer> currentIds = tileIdsO.getValue();

                    Image imageData;
                    Color colorData;
                    Rotation rotationData;

                    PlacedTile tile = state.board().tileAt(currentPos);

                    // Get the image, rotation, and color of the cell based on the game state
                    if (tile != null) {
                        int id = tile.id();
                        rotationData = tile.rotation();

                        imageData = cache.computeIfAbsent(id, ImageLoader::normalImageForTile);

                        // If the tile is not in the set of tile ids, color it black, else transparent
                        colorData = (!currentIds.isEmpty() && !currentIds.contains(id))
                                ? Color.BLACK
                                : Color.TRANSPARENT;

                    } else if (state.board().insertionPositions().contains(currentPos)
                            && state.nextAction() == GameState.Action.PLACE_TILE) {

                        if (hoverProperty.get()) {
                            Tile tileToPlace = state.tileToPlace();

                            imageData = cache.computeIfAbsent(
                                    tileToPlace.id(),
                                    ImageLoader::normalImageForTile);
                            rotationData = rotationO.getValue();

                            PlacedTile potentialTile = new PlacedTile(
                                    tileToPlace,
                                    state.currentPlayer(),
                                    rotationData,
                                    currentPos);

                            // Change color cellData based on the possibility of placement
                            colorData = state.board().canAddTile(potentialTile)
                                    ? Color.TRANSPARENT
                                    : Color.WHITE;
                        } else {
                            imageData = emptyTileImage;
                            colorData = ColorMap.fillColor(state.currentPlayer());
                            rotationData = Rotation.NONE;
                        }
                    } else {
                        imageData = emptyTileImage;
                        colorData = Color.TRANSPARENT;
                        rotationData = Rotation.NONE;
                    }
                    return new CellData(imageData, rotationData, colorData);
                }, gameStateO, tileIdsO, rotationO, hoverProperty);

                // Add a listener to the observable tile to add occupants and meadow markers
                observableTile.addListener((tile, oldTile, nextTile) -> {
                    if (oldTile == null) {
                        // Add occupants to the tile
                        for (Occupant tileOccupant : nextTile.potentialOccupants()) {
                            Node occupantImage = Icon.newFor(nextTile.placer(), tileOccupant.kind());

                            occupantImage.visibleProperty().bind(visibleOccupantsO.map(
                                    list -> list.contains(tileOccupant)));

                            String kind = tileOccupant.kind() == Occupant.Kind.PAWN ? "pawn" : "hut";
                            occupantImage.setId(STR."\{kind}_\{tileOccupant.zoneId()}");

                            occupantImage.setOnMouseClicked(event -> {
                                    if (event.isStillSincePress())
                                        occupant.accept(tileOccupant);
                            });

                            occupantImage.rotateProperty().bind(cellData.map(
                                    cell -> cell.rotation.negated().degreesCW()));

                            group.getChildren().add(occupantImage);
                        }

                        // Add meadow markers for the animals
                        for (Zone.Meadow meadow : nextTile.meadowZones()) {
                            for (Animal animal : meadow.animals()) {
                                ImageView marker = new ResizedImageView(MARKER_FIT_SIZE);
                                marker.getStyleClass().add("marker");
                                marker.setId(STR."marker_\{animal.id()}");

                                marker.visibleProperty().bind(gameStateO.map(
                                        state -> state.board().cancelledAnimals().contains(animal)));

                                group.getChildren().add(marker);
                            }
                        }
                    }
                });

                // Add the rotation of the tile
                group.rotateProperty().bind(cellData.map(cell -> cell.rotation.degreesCW()));

                // Add the color filters of the tile
                ColorInput plain = new ColorInput();
                plain.paintProperty().bind(cellData.map(cell -> cell.color));
                plain.setHeight(NORMAL_TILE_FIT_SIZE);
                plain.setWidth(NORMAL_TILE_FIT_SIZE);

                Blend blendR = new Blend(BlendMode.SRC_OVER);
                blendR.setOpacity(0.5);
                blendR.setTopInput(plain);

                ObservableValue<Blend> blend = cellData.map(
                        cellData1 -> !cellData1.color.equals(Color.TRANSPARENT)
                                ? blendR
                                : null);
                group.effectProperty().bind(blend);

                // Image view for the tile
                ImageView imageTile = new ResizedImageView(NORMAL_TILE_FIT_SIZE);
                imageTile.imageProperty().bind(cellData.map(cell -> cell.image));
                imageTile.visibleProperty().bind(cellData.map(cell -> cell.image != null));
                group.getChildren().add(imageTile);

                // Handle the player's interaction with the tile (place or rotate it)
                group.setOnMouseClicked(event  -> {
                    if (gameStateO.getValue().board().insertionPositions().contains(currentPos)
                            && event.isStillSincePress()) {

                        if (event.getButton() == MouseButton.PRIMARY)
                            position.accept(currentPos);

                        if (event.getButton() == MouseButton.SECONDARY)
                            consumerRotation.accept(
                                    event.isAltDown() ? Rotation.RIGHT : Rotation.LEFT);
                    }
                });

                gridPane.add(group, x + reach, y + reach);
            }
        }

        // Set the scroll pane to the center of the board
        boardScrollPane.setVvalue(0.5);
        boardScrollPane.setHvalue(0.5);
        return boardScrollPane;
    }

    /**
     * Private record to store the image, rotation, and color of a cell.
     *
     * @param image the image of the cell
     * @param rotation the rotation of the cell
     * @param color the color of the cell
     */
    private record CellData(Image image, Rotation rotation, Color color) {}
}
