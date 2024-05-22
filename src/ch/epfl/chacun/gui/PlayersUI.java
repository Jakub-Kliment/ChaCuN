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

/**
 * A UI utility class that consists of a single public function.
 * The goal of this class (its function) is to create a part of the
 * GUI that displays the information about the players in the game.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public final class PlayersUI {

    /**
     * Private constructor to prevent instantiation.
     */
    private PlayersUI() {}

    /**
     * Creates a UI element displaying the players' points and the number of
     * occupants of the game (huts and pawns) they have (composed of used and
     * not yet used occupants).
     *
     * @param gameState the observable value of the game state
     * @param textMaker the text maker used to create the text elements
     * @return the created UI element
     */
    public static Node create(ObservableValue<GameState> gameState,
                              TextMaker textMaker) {
        // Main container for the graphical element
        Pane box = new VBox();
        box.getStylesheets().add("players.css");
        box.setId("players");

        // Observable values of the players and their points
        ObservableValue<Map<PlayerColor, Integer>> points =
                gameState.map(state -> state.messageBoard().points());
        ObservableValue<PlayerColor> currentPlayer = gameState.map(GameState::currentPlayer);

        // For each player, create a text flow with the player's name, points and occupants
        for (PlayerColor player : gameState.getValue().players()) {
            Pane textFlow = new TextFlow();
            textFlow.getStyleClass().add("player");
            box.getChildren().add(textFlow);

            // Current player is highlighted
            currentPlayer.addListener((current, oldPlayer, nextPlayer) -> {
                if (nextPlayer == player) textFlow.getStyleClass().add("current");
                if (oldPlayer == player) textFlow.getStyleClass().remove("current");
            });

            // Circle with the player's color to improve visibility
            Circle circle = new Circle(5, ColorMap.fillColor(player));

            ObservableValue<String> playerPoints = points.map(
                    individualPoints -> STR." \{textMaker.playerName(player)} : " +
                            STR."\{textMaker.points(individualPoints.getOrDefault(player, 0))}\n");

            // Create the occupants for the player
            Text textPoints = new Text();
            textPoints.textProperty().bind(playerPoints);
            textFlow.getChildren().addAll(circle, textPoints);

            int pawnCount = occupantsCount(Kind.PAWN);
            int hutCount = occupantsCount(Kind.HUT);

            for (int i = 1; i <= hutCount + pawnCount; i++) {
                Occupant.Kind kind = i <= hutCount ? Kind.HUT : Kind.PAWN;

                int index = i;
                // Opacity of the occupant depends on the number of free occupants
                ObservableValue<Double> opacity = gameState.map(state -> {
                    int count = index - (kind == Kind.HUT ? 0 : hutCount);
                    return state.freeOccupantsCount(player, kind) >= count ? 1 : 0.1;
                });

                // Occupant visual representation
                Node occupant = Icon.newFor(player, kind);
                occupant.opacityProperty().bind(opacity);
                textFlow.getChildren().add(occupant);

                // Add a space between huts and pawns for better visibility
                if (index == hutCount)
                    textFlow.getChildren().add(new Text("   "));
            }
        }
        return box;
    }
}