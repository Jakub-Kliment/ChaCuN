package ch.epfl.chacun.gui;

import ch.epfl.chacun.Base32;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * A UI utility class that consists of a single public static function create.
 * The main goal of this class is to create a part of the
 * GUI that displays the actions that the players take.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public final class ActionsUI {

    /**
     * Private constructor to prevent instantiation.
     */
    private ActionsUI() {}

    /**
     * Creates a UI element displaying the actions that the players took.
     * The actions are displayed as a text that shows the last 4 actions taken
     * and a text field where the player can input the action they want to take.
     *
     * @param actions the observable value of the list of actions
     * @param eventHandler the consumer that handles the action
     * @return the created UI element displaying the actions
     */
    public static Node create(ObservableValue<List<String>> actions,
                              Consumer<String> eventHandler) {

        // Main container for the graphical element
        HBox actionsBox = new HBox();
        actionsBox.getStylesheets().add("actions.css");
        actionsBox.setId("actions");

        // Text displaying the last 4 actions taken
        Text text = new Text();
        text.textProperty().bind(actions.map(list -> {
            StringJoiner txt = new StringJoiner(", ", "", " ");
            for (int i = Math.min(4, list.size()); i > 0; i--)
                txt.add(STR."\{list.size() - i + 1}:\{list.get(list.size() - i)}");
            return txt.toString();
        }));

        // Text field for the player to input the action
        TextField textField = getTextField(eventHandler);
        actionsBox.getChildren().addAll(text, textField);
        return actionsBox;
    }

    /**
     * Creates a text field for the players to input the next action they want to take.
     * The text field only accepts valid base-32 characters and accepts the action when
     * the action can be taken in the actual state of the game.
     *
     * @param eventHandler the consumer that handles the action
     * @return the created text field
     */
    private static TextField getTextField(Consumer<String> eventHandler) {
        // Text field for the player to input the action
        TextField textField = new TextField();
        textField.setId("action-field");

        // Text field only accepts valid base-32 characters and converts them to uppercase
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getText();
            newText = newText.chars()
                    .mapToObj(c -> (char) c)
                    .map(Character::toUpperCase)
                    .map(String::valueOf)
                    .filter(Base32::isValid)
                    .collect(Collectors.joining());

            change.setText(newText);
            return change;
        }));

        // When the player presses enter, the action is accepted and the text field is cleared
        textField.setOnAction(event -> {
            eventHandler.accept(textField.getText());
            textField.clear();
        });

        return textField;
    }
}