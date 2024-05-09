package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import ch.epfl.chacun.gui.MessageBoardUI;
import ch.epfl.chacun.gui.PlayersUI;
import ch.epfl.chacun.tile.Tiles;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static javafx.application.Application.launch;

public class ActionsUITest extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {

        SimpleObjectProperty<List<String>> messages = new SimpleObjectProperty<>(List.of("A3", "K6", "M", "3E", "I8"));
        var actionsNode = ActionsUI.create(messages, s -> System.out.println(STR."String :\{s}"));
        var root2Node = new ScrollPane(actionsNode);
        primaryStage.setScene(new Scene(root2Node));

        primaryStage.setTitle("ChaCuN test");
        primaryStage.show();

    }
}
