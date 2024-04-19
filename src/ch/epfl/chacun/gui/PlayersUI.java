package ch.epfl.chacun.gui;

import ch.epfl.chacun.GameState;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.TextMaker;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

public class PlayersUI {

    private PlayersUI() {}

    public static Node create(ObservableValue<GameState> gameState,
                              TextMaker textMaker) {
        SVGPath svg = new SVGPath();

        for (PlayerColor color : PlayerColor.ALL) {
            VBox vBox = new VBox();
            if (textMaker.playerName(color) != null) {
                svg.setStyle(vBox.getStylesheets().toString());

                vBox.setId(textMaker.playerName(color));

                vBox.getStyleClass();
            }
        }

        ObservableValue<GameState> gameStateO = gameState;
        ObservableValue<PlayerColor> currentPlayerO =
                gameStateO.map(GameState::currentPlayer);

        svg.setContent(currentPlayerO.toString());


        return svg;
    }

}
