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
        ObservableValue<Map<PlayerColor, Integer>> points = gameState.map(gs -> gs.messageBoard().points());
        ObservableValue<PlayerColor> currentPlayer = gameState.map(GameState::currentPlayer);
        for (PlayerColor color : PlayerColor.ALL) {
            if (textMaker.playerName(color) == null) continue;
            Pane flow = new TextFlow();
            flow.getStyleClass().add("player");
            currentPlayer.addListener( (o, old, next) -> {
                if (old == color)
                    flow.getStyleClass().remove("current");
                if (next == color){
                    flow.getStyleClass().add("current");
                }
            } );


            Node circle = new Circle(5, ColorMap.fillColor(color));
            flow.getChildren().add(circle);


            ObservableValue<String> string = points.map(mapPoint -> STR." \{textMaker.playerName(color)} : \{mapPoint.get(color)} point\{mapPoint.get(color) > 1 ? "s" : ""}\n");
            Text text = new Text();
            text.textProperty().bind(string);
            flow.getChildren().add(text);



            for (int i = 1; i <= 3; i++) {
                Node hut = Icon.newFor(color, Occupant.Kind.HUT);
                int finalI = i;
                ObservableValue<Double> opacity = gameState.map(gs -> gs.freeOccupantsCount(color, Occupant.Kind.HUT) >= finalI ? 1 : 0.1);
                hut.opacityProperty().bind(opacity);
                flow.getChildren().add(hut);
            }

            Node espace = new Text("   ");
            box.getChildren().add(espace);

            for (int i = 1; i <= 5; i++) {
                Node pawn = Icon.newFor(color, Occupant.Kind.PAWN);
                int finalI = i;
                ObservableValue<Double> opacity = gameState.map(((c) -> c.freeOccupantsCount(color, Occupant.Kind.PAWN) >= finalI ? 1 : 0.1));
                pawn.opacityProperty().bind(opacity);
                flow.getChildren().add(pawn);
            }

            box.getChildren().add(flow);
        }
        return box;
    }
}
