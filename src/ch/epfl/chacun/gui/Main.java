package ch.epfl.chacun.gui;

import ch.epfl.chacun.*;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.*;
import java.util.function.Consumer;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;


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
        Collections.shuffle(tiles, rg);

        Map<Tile.Kind, List<Tile>> groupedTiles = tiles
                .stream()
                .collect(Collectors.groupingBy(Tile::kind));

        TileDecks tileDecks = new TileDecks(
                groupedTiles.get(Tile.Kind.START),
                groupedTiles.get(Tile.Kind.NORMAL),
                groupedTiles.get(Tile.Kind.MENHIR));

        TextMaker textMaker = new TextMakerFr(players);
        GameState state = GameState.initial(colors, tileDecks, textMaker);



        ObjectProperty<Set<Integer>> tileIds = new SimpleObjectProperty<>(Set.of());
        SimpleObjectProperty<GameState> gameStateO = new SimpleObjectProperty<>(state);
        ObservableValue<List<MessageBoard.Message>> listObservable = gameStateO.map(gs -> gs.messageBoard().messages());

        BorderPane main = new BorderPane();

        ObservableValue<Set<Occupant>> visibleOccupant = gameStateO.map(gs-> {
            Set<Occupant> o = gs.board().occupants();
            if (gs.nextAction() == GameState.Action.OCCUPY_TILE)
                o.addAll(gs.lastTilePotentialOccupants());
            return o;
        });

        SimpleObjectProperty<Rotation> rotation = new SimpleObjectProperty<>(Rotation.NONE);

        Node board = BoardUI.create(Board.REACH, gameStateO, rotation, visibleOccupant, tileIds,
                rot -> rotation.setValue(rotation.getValue().add(rot)),
                pos -> {
                    GameState gameState = gameStateO.getValue();
                    PlacedTile placedTile = new PlacedTile(gameState.tileToPlace(), gameState.currentPlayer(), rotation.getValue(), pos);
                    if (gameState.board().canAddTile(placedTile)){
                        gameStateO.setValue(gameState.withPlacedTile(placedTile));
                        rotation.setValue(Rotation.NONE);
                    }
                },
                occupant -> {
                    GameState gameState = gameStateO.getValue();
                    if (gameState.nextAction() == GameState.Action.OCCUPY_TILE) {
                        gameStateO.setValue(gameState.withNewOccupant(occupant));
                    }
                    if (gameState.nextAction() == GameState.Action.RETAKE_PAWN)
                        gameStateO.setValue(gameState.withOccupantRemoved(occupant));
                });

        main.setCenter(board);

        BorderPane right = new BorderPane();
        main.setRight(right);



        Node player = PlayersUI.create(gameStateO, textMaker);
        right.setTop(player);


        Node messageBoard = MessageBoardUI.create(listObservable, tileIds);
        right.setCenter(messageBoard);

        VBox vbox = new VBox();
        right.setBottom(vbox);


        ObjectProperty<List<String>> listAction = new SimpleObjectProperty<>(new ArrayList<>());
        Node action = ActionsUI.create(listAction, s -> {
            GameState gs = ActionEncoder.decodeAndApply(gameStateO.getValue(), s).state();
            gameStateO.setValue(gs);
            List<String> newListAction = listAction.getValue();
            newListAction.add(s);
            listAction.setValue(newListAction);
        });
        vbox.getChildren().add(action);


        ObservableValue<Tile> tileToPlace = gameStateO.map(GameState::tileToPlace);

        ObservableValue<Integer> normalTiles = gameStateO.map(gs -> gs.tileDecks().deckSize(Tile.Kind.NORMAL));

        ObservableValue<Integer> menhirTiles = gameStateO.map(gs -> gs.tileDecks().deckSize(Tile.Kind.MENHIR));


        ObservableValue<String> text = gameStateO.map(gs -> {
            if (gs.nextAction() == GameState.Action.RETAKE_PAWN)
                return gs.messageBoard().textMaker().clickToUnoccupy();
            if (gs.nextAction() == GameState.Action.OCCUPY_TILE)
                return gs.messageBoard().textMaker().clickToOccupy();
            return "";
        });


        Node decks = DecksUI.create(tileToPlace, normalTiles, menhirTiles, text, o -> {
            if (gameStateO.getValue().nextAction() == GameState.Action.OCCUPY_TILE)
                gameStateO.setValue(gameStateO.getValue().withNewOccupant(o));
            else if (gameStateO.getValue().nextAction() == GameState.Action.RETAKE_PAWN)
                gameStateO.setValue(gameStateO.getValue().withOccupantRemoved(o));
        });
        vbox.getChildren().add(decks);


        Scene scene = new Scene(main);
        primaryStage.setScene(scene);
        primaryStage.setTitle("ChaCuN");
        primaryStage.setHeight(1080);
        primaryStage.setWidth(1440);

        gameStateO.setValue(gameStateO.getValue().withStartingTilePlaced());
        primaryStage.show();
    }
}
