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

public class MessageBoardUI {

    private MessageBoardUI() {}

    public static Node create(ObservableValue<List<MessageBoard.Message>> messageList,
                              ObjectProperty<Set<Integer>> tileIds) {

        ScrollPane messagePane = new ScrollPane();

        VBox messageBox = new VBox();
        messageBox.getStylesheets().add("message-board.css");
        // demander !!!!!!!
        //messageBox.setId("message-board");
        messagePane.setContent(messageBox);

        messageList.addListener((list, oldList, nextList) -> {
            for (int i = nextList.size() - oldList.size(); i > 0 ; --i) {
                MessageBoard.Message boardMessage = nextList.get(nextList.size() - i);

                Text message = new Text(boardMessage.text());
                message.setWrappingWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
                message.setOnMouseEntered(event -> tileIds.setValue(boardMessage.tileIds()));
                message.setOnMouseExited(event -> tileIds.setValue(Set.of()));
                messageBox.getChildren().add(message);

                messagePane.layout();
                messagePane.setVvalue(1);
            }
        });
        return messagePane;
    }
}
