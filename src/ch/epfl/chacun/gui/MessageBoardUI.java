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

/**
 * A class that creates a message board UI element.
 * It is a graphical representation of a scrollable list of all messages of the game.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public class MessageBoardUI {

    /**
     * Private constructor to prevent instantiation.
     */
    private MessageBoardUI() {}

    /**
     * Creates a message board UI element. The graphical representation of the message board
     * is a scrollable list of all messages of the game that are displayed in a vertical box
     * throughout the game. The messages are displayed in the order they are added to the list
     * (as the game progresses, the list of messages grows).
     *
     * @param messageList the observable list of messages to display
     * @param tileIds the set of tile ids that are linked to the message
     *                and are highlighted when the mouse is over the message
     * @return the message board UI element
     */
    public static Node create(ObservableValue<List<MessageBoard.Message>> messageList,
                              ObjectProperty<Set<Integer>> tileIds) {

        // Create a scrollable pane to display the messages
        ScrollPane messagePane = new ScrollPane();

        // Create a vertical box to put the scrollable pane into
        VBox messageBox = new VBox();
        messageBox.getStylesheets().add("message-board.css");
        // demander !!!!!!!
        //messageBox.setId("message-board");
        messagePane.setContent(messageBox);

        // Listener that updates the message board as soon as a new message is added
        messageList.addListener((list, oldList, nextList) -> {
            // Loop through the new messages and add them to the message board in reverse order
            for (int i = nextList.size() - oldList.size(); i > 0 ; --i) {
                MessageBoard.Message boardMessage = nextList.get(nextList.size() - i);

                // Create a text element for the message
                Text message = new Text(boardMessage.text());
                message.setWrappingWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
                // Highlight the tiles linked to the message when the mouse hovers over the message
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
