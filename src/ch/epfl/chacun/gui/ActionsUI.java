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
import java.util.stream.Collectors;

public class ActionsUI {

    private ActionsUI() {}

    public static Node create(ObservableValue<List<String>> actions,
                              Consumer<String> eventHandler) {

        HBox actionsBox = new HBox();
        actionsBox.getStylesheets().add("actions.css");
        actionsBox.getStyleClass().add("actions");
        actionsBox.setId("actions");

        Text text = new Text();
        text.textProperty().bind(actions.map((list) -> {
            StringBuilder txt = new StringBuilder();
            for (int i = 4; i > 0; i--) {
                if (list.size() >= i)
                    txt.append(STR."\{(list.size() + 1 - i)}:\{list.get(list.size() - i)}");
                if (!txt.isEmpty() && i > 1)
                    txt.append(", ");
            }
            return txt.toString();
        }));
        actionsBox.getChildren().add(text);

        TextField textField = new TextField();
        textField.setId("action-field");


        textField.setTextFormatter(new TextFormatter<>(change -> {
            // Obtenez un flux de caractères à partir du texte entré
            String newText = change.getText();

            newText = newText.chars() // Flux de caractères
                    .mapToObj(c -> (char) c)
                    .map(Character::toUpperCase)// Convertir les entiers en caractères
                    .filter(c -> Base32.isValid(c.toString())) // Filtrer les caractères autorisés
                    .map(String::valueOf) // Convertir les caractères en chaînes de caractères
                    .collect(Collectors.joining()); // Rassembler les caractères filtrés en une seule chaîne

            // Mettez à jour le texte du champ
            change.setText(newText);

            // Retournez le changement
            return change;
        }));

        actionsBox.getChildren().add(textField);

        return actionsBox;
    }
}
