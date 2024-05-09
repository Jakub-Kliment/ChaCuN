package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import ch.epfl.chacun.gui.MessageBoardUI;
import ch.epfl.chacun.gui.PlayersUI;
import ch.epfl.chacun.tile.Tiles;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static javafx.application.Application.launch;

public class ActionsUITest extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        var playerNames = Map.of(PlayerColor.RED, "Rose",
                PlayerColor.BLUE, "Bernard");
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

        var gameStateO = new SimpleObjectProperty<>(gameState);

        SimpleObjectProperty<List<String>> messages = new SimpleObjectProperty<>(List.of());
        var actionsNode = ActionsUI.create(messages, s -> System.out.println(STR."String :\{s}"));
        var root2Node = new ScrollPane(actionsNode);
        primaryStage.setScene(new Scene(root2Node));
        primaryStage.setTitle("ChaCuN test");
        primaryStage.show();
    }
}
