package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class TestUi {
    private TestUi(){}
    public static Node create(ObservableValue<GameState> gameState, TextMaker textMaker,
                              ObservableValue<List<MessageBoard.Message>> messageList,
                              ObjectProperty<Set<Integer>> tileId,
                              ObservableValue<Tile> tileToPlace,
                              ObservableValue<Integer> normalTile,
                              ObservableValue<Integer> menhirTile,
                              ObservableValue<String> text,
                              Consumer<Occupant> event
                              ){

        Pane box = new VBox();

//        Node player = PlayersUI.create(gameState, textMaker);
//        box.getChildren().add(player);

        Node message = MessageBoardUI.create(messageList, tileId);
        box.getChildren().add(message);

//        Node decks = DecksUI.create(tileToPlace, normalTile, menhirTile, text, event);
//        box.getChildren().add(decks);

        return box;

    }
}
