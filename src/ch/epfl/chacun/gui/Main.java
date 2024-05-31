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
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;

import static ch.epfl.chacun.ActionEncoder.*;


/**
 * The main class of the ChaCuN game containing the main method.
 * It creates the GUI from all UI components by putting all together,
 * creates the game state and starts the game.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public final class Main extends Application {

    /**
     * The title of the game.
     */
    private final static String TITLE = "ChaCuN";

    /**
     * The width of the screen.
     */
    private final static int SCREEN_WIDTH = 1440;

    /**
     * The height of the screen.
     */
    private final static int SCREEN_HEIGHT = 1080;

    /**
     * The main method of the game which launches the game.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Starts the game by creating the GUI and the game state.
     *
     * @param primaryStage the primary stage of the game
     * @throws Exception if any exception occurs
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // Get the names of the players from the command line arguments and assign them colors
        List<String> names = getParameters().getUnnamed();
        List<PlayerColor> colors = PlayerColor.ALL
                .stream()
                .limit(names.size())
                .toList();

        Map<PlayerColor, String> players = new HashMap<>();
        for (int i = 0; i < names.size(); ++i)
            players.put(colors.get(i), names.get(i));

        // Create the tile decks and shuffle the tiles randomly
        Map<String, String> parameters = getParameters().getNamed();
        RandomGenerator randomGenerator;
        if (! parameters.isEmpty() && parameters.get("seed") != null) {
            long seed = Long.parseUnsignedLong(parameters.get("seed"));
            randomGenerator = RandomGeneratorFactory.getDefault().create(seed);
        } else {
            randomGenerator = RandomGeneratorFactory.getDefault().create();
        }

        List<Tile> tiles = new ArrayList<>(Tiles.TILES);
        Collections.shuffle(tiles, randomGenerator);

        Map<Tile.Kind, List<Tile>> groupedTiles = tiles
                .stream()
                .collect(Collectors.groupingBy(Tile::kind));

//        // Create the tile decks
//        TileDecks tileDecks = new TileDecks(
//                groupedTiles.get(Tile.Kind.START),
//                groupedTiles.get(Tile.Kind.NORMAL),
//                groupedTiles.get(Tile.Kind.MENHIR));

        TileDecks tileDecks = new TileDecks(
                List.of(Tiles.TILES.get(56)),
                List.of(Tiles.TILES.get(68), Tiles.TILES.get(37), Tiles.TILES.get(35)),
                List.of(Tiles.TILES.get(83)));

        // Create the text maker and game state
        TextMaker textMaker = new TextMakerFr(players);
        GameState state = GameState.initial(colors, tileDecks, textMaker);

        // Create the main pane of the game
        BorderPane mainPane = new BorderPane();

        ObjectProperty<List<String>> actionsList = new SimpleObjectProperty<>(List.of());
        ObjectProperty<Set<Integer>> tileIds = new SimpleObjectProperty<>(Set.of());

        SimpleObjectProperty<GameState> gameStateO = new SimpleObjectProperty<>(state);
        ObservableValue<List<MessageBoard.Message>> messageList = gameStateO.map(
                gameState -> gameState.messageBoard().messages());

        ObservableValue<Set<Occupant>> visibleOccupants = gameStateO.map(gameState-> {
            Set<Occupant> occupants = gameState.board().occupants();
            if (gameState.nextAction() == GameState.Action.OCCUPY_TILE)
                occupants.addAll(gameState.lastTilePotentialOccupants());
            return occupants;
        });

        SimpleObjectProperty<Rotation> rotation = new SimpleObjectProperty<>(Rotation.NONE);

        // Create the board of the game and set it to the center
        Node board = BoardUI.create(
                Board.REACH,
                gameStateO,
                rotation,
                visibleOccupants,
                tileIds,
                consumerRot -> rotation.setValue(rotation.getValue().add(consumerRot)),
                consumerPos -> {
                    GameState gameState = gameStateO.getValue();
                    PlacedTile placedTile = new PlacedTile(
                            gameState.tileToPlace(),
                            gameState.currentPlayer(),
                            rotation.getValue(),
                            consumerPos);

                    StateAction stateAction = withPlacedTile(gameState, placedTile);
                    if (stateAction != null) {
                        withNewAction(actionsList, stateAction, gameStateO);
                        rotation.setValue(Rotation.NONE);
                    }
                },
                occupant -> occupantConsumer(occupant, actionsList, gameStateO)
                );
        mainPane.setCenter(board);

        // Create the right side of the GUI
        BorderPane right = new BorderPane();
        mainPane.setRight(right);

        // Create the player UI and set it to the top (right side)
        Node player = PlayersUI.create(gameStateO, textMaker);
        right.setTop(player);

        // Create the message board UI and set it to the center (right side)
        Node messageBoard = MessageBoardUI.create(messageList, tileIds);
        right.setCenter(messageBoard);

        // Create the actions UI and set it under the message board (right side)
        VBox vbox = new VBox();
        right.setBottom(vbox);

        Node action = ActionUI.create(actionsList, string -> {
            StateAction stateAction = decodeAndApply(gameStateO.getValue(), string);
            if (stateAction != null) {
                gameStateO.setValue(stateAction.state());
                withNewAction(actionsList, stateAction, gameStateO);
            }
        });
        vbox.getChildren().add(action);

        ObservableValue<Tile> tileToPlace = gameStateO.map(GameState::tileToPlace);
        ObservableValue<Integer> normalTiles = gameStateO.map(gameState ->
                gameState.tileDecks().deckSize(Tile.Kind.NORMAL));

        ObservableValue<Integer> menhirTiles = gameStateO.map(gameState ->
                gameState.tileDecks().deckSize(Tile.Kind.MENHIR));

        ObservableValue<String> text = gameStateO.map(gameState -> {
            if (gameState.nextAction() == GameState.Action.RETAKE_PAWN)
                return gameState.messageBoard().textMaker().clickToUnoccupy();
            if (gameState.nextAction() == GameState.Action.OCCUPY_TILE)
                return gameState.messageBoard().textMaker().clickToOccupy();
            return "";
        });

        // Create the decks UI and set it on the bottom (right side)
        Node decks = DecksUI.create(tileToPlace, normalTiles, menhirTiles, text, occupant ->
            occupantConsumer(occupant, actionsList, gameStateO)
        );
        vbox.getChildren().add(decks);

        // Create the scene of the game and set it to the primary stage
        Scene scene = new Scene(mainPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle(TITLE);
        primaryStage.setHeight(SCREEN_HEIGHT);
        primaryStage.setWidth(SCREEN_WIDTH);

        // Set the starting tile to be placed to start the game
        gameStateO.setValue(gameStateO.getValue().withStartingTilePlaced());
        primaryStage.show();
    }

    /**
     * Private static helper method that adds a new
     * action to the list of actions.
     *
     * @param actionsList the old observable list of actions
     * @param stateAction the new state action with the new action and state
     * @param state the observable state of the game
     */
    private static void withNewAction(ObjectProperty<List<String>> actionsList,
                                      StateAction stateAction,
                                      SimpleObjectProperty<GameState> state) {

        List<String> list = new ArrayList<>(actionsList.getValue());
        list.add(stateAction.action());
        actionsList.setValue(list);
        state.setValue(stateAction.state());
    }

    /**
     * Private static helper method that consumes an occupant
     * and adds a new action to the list of actions.
     *
     * @param occupant the occupant to consume
     * @param actionsList the list of actions
     * @param observableState the observable state of the game
     */
    private static void occupantConsumer(Occupant occupant,
                                         ObjectProperty<List<String>> actionsList,
                                         SimpleObjectProperty<GameState> observableState) {

        GameState gameState = observableState.getValue();
        StateAction stateAction;
        switch (gameState.nextAction()) {
            case OCCUPY_TILE -> stateAction = withNewOccupant(gameState, occupant);
            case RETAKE_PAWN -> stateAction = withOccupantRemoved(gameState, occupant);
            default -> stateAction = null;
        }
        if (stateAction != null)
            withNewAction(actionsList, stateAction, observableState);
    }
}
