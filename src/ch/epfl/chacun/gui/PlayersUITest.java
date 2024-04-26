package ch.epfl.chacun.gui;


import ch.epfl.chacun.*;
import ch.epfl.chacun.tile.Tiles;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class PlayersUITest extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        var playerNames = Map.of(PlayerColor.RED, "Mario",
                PlayerColor.BLUE, "Peach",
                PlayerColor.YELLOW, "Warrio",
                PlayerColor.GREEN, "Luigi",
                PlayerColor.PURPLE, "Waluigi");
        var playerColors = playerNames.keySet().stream()
                .sorted()
                .toList();

        var tilesByKind = Tiles.TILES.stream()
                .collect(Collectors.groupingBy(Tile::kind));
        var tileDecks =
                new TileDecks(tilesByKind.get(Tile.Kind.START),
                        tilesByKind.get(Tile.Kind.NORMAL),
                        tilesByKind.get(Tile.Kind.MENHIR));

        var textMaker = new TextMakerFr(playerNames);

        var gameState =
                GameState.initial(playerColors,
                        tileDecks,
                        textMaker);

        gameState = gameState.withStartingTilePlaced();
        gameState = gameState.withPlacedTile(new PlacedTile(Tiles.TILES.get(54), gameState.currentPlayer(),
                Rotation.NONE, new Pos(1, 0)));
        gameState = gameState.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 54_0));

        gameState = gameState.withPlacedTile(new PlacedTile(Tiles.TILES.getFirst(), gameState.currentPlayer(),
                Rotation.RIGHT, new Pos(-1, 0)));
        gameState = gameState.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 1));

        var gameStateO = new SimpleObjectProperty<>(gameState);

        var playersNode = PlayersUI.create(gameStateO, textMaker);
        var rootNode = new BorderPane(playersNode);
        primaryStage.setScene(new Scene(rootNode));

        primaryStage.setTitle("ChaCuN test");
        primaryStage.show();
    }
}