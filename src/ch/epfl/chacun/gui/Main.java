package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.*;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collector;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        List<String> names = getParameters().getUnnamed();
        List<PlayerColor> colors = PlayerColor.ALL
                .stream()
                .limit(names.size())
                .toList();
        Map<PlayerColor, String> players = new HashMap<>();
        for (int i = 0; i < names.size(); ++i)
            players.put(colors.get(i), names.get(i));


        Map<String, String> parameters = getParameters().getNamed();
        RandomGenerator rg;
        if (! parameters.isEmpty()) {
            long seed = Long.parseUnsignedLong(parameters.get("seed"));
            rg = RandomGeneratorFactory.getDefault().create(seed);
        } else {
            rg = RandomGeneratorFactory.getDefault().create();
        }
        List<Tile> tiles = new ArrayList<>(Tiles.TILES);
        Collections.shuffle(tiles);

        List<Tile> startingTile = Collector.of()

        TileDecks decks = new TileDecks();
        TextMaker textMaker = new TextMakerFr(players);
        GameState state = GameState.initial(colors, decks, textMaker);


        BorderPane pane = new BorderPane();

        Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
    }
}
