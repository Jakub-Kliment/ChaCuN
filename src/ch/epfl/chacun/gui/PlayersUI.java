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

import static ch.epfl.chacun.Occupant.*;

public class PlayersUI {

    private PlayersUI() {}

    public static Node create(ObservableValue<GameState> gameState,
                              TextMaker textMaker) {
        Pane box = new VBox();
        box.getStylesheets().add("players.css");
        box.setId("players");

        ObservableValue<Map<PlayerColor, Integer>> points =
                gameState.map(state -> state.messageBoard().points());

        ObservableValue<PlayerColor> currentPlayer = gameState.map(GameState::currentPlayer);

        for (PlayerColor player : gameState.getValue().players()) {
            Pane textFlow = new TextFlow();
            textFlow.getStyleClass().add("player");
            box.getChildren().add(textFlow);

            currentPlayer.addListener((current, oldPlayer, nextPlayer) -> {
                if (nextPlayer == player) textFlow.getStyleClass().add("current");
                if (oldPlayer == player) textFlow.getStyleClass().remove("current");
            });

            Circle circle = new Circle(5, ColorMap.fillColor(player));

            ObservableValue<String> playerPoints = points.map(
                    individualPoints -> STR." \{textMaker.playerName(player)} : " +
                            STR."\{textMaker.points(individualPoints.getOrDefault(player, 0))}\n");

            Text textPoints = new Text();
            textPoints.textProperty().bind(playerPoints);
            textFlow.getChildren().addAll(circle, textPoints);

            int pawnCount = occupantsCount(Kind.PAWN);
            int hutCount = occupantsCount(Kind.HUT);

            for (int i = 1; i <= hutCount + pawnCount; i++) {
                Occupant.Kind kind = i <= hutCount ? Kind.HUT : Kind.PAWN;

                int index = i;
                ObservableValue<Double> opacity = gameState.map(state -> {
                    int count = index - (kind == Kind.HUT ? 0 : hutCount);
                    return state.freeOccupantsCount(player, kind) >= count ? 1 : 0.1;
                });

                Node occupant = Icon.newFor(player, kind);
                occupant.opacityProperty().bind(opacity);
                textFlow.getChildren().add(occupant);

                if (index == hutCount)
                    textFlow.getChildren().add(new Text("   "));
            }
        }
        return box;
    }
}