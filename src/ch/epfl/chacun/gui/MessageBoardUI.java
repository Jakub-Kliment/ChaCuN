package ch.epfl.chacun.gui;

import ch.epfl.chacun.MessageBoard;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Set;

import static ch.epfl.chacun.gui.ImageLoader.*;
import static javafx.application.Platform.runLater;

public class MessageBoardUI {

    private MessageBoardUI() {}

    public static Node create(ObservableValue<List<MessageBoard.Message>> messageList,
                              ObjectProperty<Set<Integer>> tileId) {

        ScrollPane messagePane = new ScrollPane();

        VBox box = new VBox();
        box.getStylesheets().add("message-board.css");
        box.setId("message-board");
        messagePane.setContent(box);

        messageList.addListener((m, old, next) -> {
            for (int i = next.size() - old.size(); i > 0 ; i--) {
                MessageBoard.Message boardMessage = next.get(next.size() - i);
                Text message = new Text(boardMessage.text());
                message.setWrappingWidth(LARGE_TILE_FIT_SIZE);
                message.setOnMouseEntered(e -> tileId.setValue(boardMessage.tileIds()));
                message.setOnMouseExited(e -> tileId.setValue(Set.of()));
                box.getChildren().add(message);
            }
        });

        runLater(() -> messagePane.setVvalue(1));

        return messagePane;
    }
}
