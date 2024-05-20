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

public class ActionsUI {

    private ActionsUI() {}

    public static Node create(ObservableValue<List<String>> actions,
                              Consumer<String> eventHandler) {

        HBox actionsBox = new HBox();
        actionsBox.getStylesheets().add("actions.css");
        actionsBox.setId("actions");

        Text text = new Text();
        text.textProperty().bind(actions.map(list -> {
            StringJoiner txt = new StringJoiner(", ", "", " ");
            for (int i = Math.min(4, list.size()); i > 0; i--)
                txt.add(STR."\{list.size() - i + 1}:\{list.get(list.size() - i)}");
            return txt.toString();
        }));

        TextField textField = getTextField(eventHandler);
        actionsBox.getChildren().addAll(text, textField);
        return actionsBox;
    }

    private static TextField getTextField(Consumer<String> eventHandler) {
        TextField textField = new TextField();
        textField.setId("action-field");

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

        textField.setOnAction(event -> {
            eventHandler.accept(textField.getText());
            textField.clear();
        });

        return textField;
    }
}