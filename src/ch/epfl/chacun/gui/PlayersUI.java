package ch.epfl.chacun.gui;

import ch.epfl.chacun.GameState;
import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.TextMaker;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Map;

public class PlayersUI {

    private PlayersUI() {}

    public static Node create(ObservableValue<GameState> gameState,
                              TextMaker textMaker) {
        Pane box = new VBox();
        box.getStylesheets().add("players.css");
        box.setId("players");

        ObservableValue<Map<PlayerColor, Integer>> points =
                gameState.map(gs -> gs.messageBoard().points());
        ObservableValue<PlayerColor> currentPlayer =
                gameState.map(GameState::currentPlayer);

        for (PlayerColor player : gameState.getValue().players()) {
            if (textMaker.playerName(player) == null) continue;
            Pane textFlow = new TextFlow();
            textFlow.getStyleClass().add("player");
            if (player == currentPlayer.getValue())
                textFlow.getStyleClass().add("current");
            currentPlayer.addListener((o, old, next) -> {
                if (next == player) textFlow.getStyleClass().add("current");
                if (old == player) textFlow.getStyleClass().remove("current");
            });

            textFlow.getChildren().add(new Circle(5, ColorMap.fillColor(player)));

            ObservableValue<String> playerPoints = points
                    .map(point -> STR." \{textMaker.playerName(player)} : " +
                            STR."\{textMaker.points(point.getOrDefault(player, 0))}\n");

            Text text = new Text();
            text.textProperty().bind(playerPoints);
            textFlow.getChildren().add(text);

            for (int i = 1; i <= 3; i++) {
                Node hut = Icon.newFor(player, Occupant.Kind.HUT);
                int index = i;
                ObservableValue<Double> opacity = gameState
                        .map(gs -> gs.freeOccupantsCount(player, Occupant.Kind.HUT) >= index ? 1 : 0.1);
                hut.opacityProperty().bind(opacity);
                textFlow.getChildren().add(hut);
            }

            textFlow.getChildren().add(new Text("   "));

            for (int i = 1; i <= 5; i++) {
                Node pawn = Icon.newFor(player, Occupant.Kind.PAWN);
                int index = i;
                ObservableValue<Double> opacity = gameState
                        .map((c -> c.freeOccupantsCount(player, Occupant.Kind.PAWN) >= index ? 1 : 0.1));
                pawn.opacityProperty().bind(opacity);
                textFlow.getChildren().add(pawn);
            }
            box.getChildren().add(textFlow);
        }
        return box;
    }
}