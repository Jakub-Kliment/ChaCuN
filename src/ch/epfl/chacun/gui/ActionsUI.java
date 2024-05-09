package ch.epfl.chacun.gui;

import ch.epfl.chacun.Base32;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.function.Consumer;

public class ActionsUI {

    private ActionsUI() {}

    public static Node create(ObservableValue<List<String>> actions,
                              Consumer<String> eventHandler) {

        HBox actionsBox = new HBox();
        actionsBox.getStylesheets().add("actions.css");
        actionsBox.getStyleClass().add("actions");
        actionsBox.setId("actions");

        Text text = new Text();
        actionsBox.getChildren().add(text);

        TextField textField = new TextField();
        textField.setId("action-field");
        textField.setTextFormatter(new TextFormatter<>(change -> {
            return change;
        }));

        actionsBox.getChildren().add(textField);

        return actionsBox;
    }
}
