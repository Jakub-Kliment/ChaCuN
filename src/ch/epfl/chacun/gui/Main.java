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
        if (! parameters.isEmpty() && parameters.get("seed") != null) {
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

        //Deck de base
        TileDecks tileDecks = new TileDecks(
                groupedTiles.get(Tile.Kind.START),
                groupedTiles.get(Tile.Kind.NORMAL),
                groupedTiles.get(Tile.Kind.MENHIR));

//        //Logboat Deck Normale
//        TileDecks tileDecks = new TileDecks(
//                List.of(Tiles.TILES.get(56)),
//                List.of(Tiles.TILES.get(3), Tiles.TILES.get(4), Tiles.TILES.get(5), Tiles.TILES.get(6), Tiles.TILES.get(7), Tiles.TILES.get(11), Tiles.TILES.get(1)),
//                List.of(Tiles.TILES.get(93)));

//        //LogBoat limit
//        TileDecks tileDecks = new TileDecks(
//                List.of(Tiles.TILES.get(56)),
//                List.of( Tiles.TILES.get(14), Tiles.TILES.get(1), Tiles.TILES.get(60)),
//                List.of(Tiles.TILES.get(93)));

//        //Chaman Deck
//        TileDecks tileDecks = new TileDecks(
//                List.of(Tiles.TILES.get(56)),
//                List.of(Tiles.TILES.get(36), Tiles.TILES.get(35), Tiles.TILES.get(27), Tiles.TILES.get(39), Tiles.TILES.get(37)),
//                List.of(Tiles.TILES.get(88)));

//        //Hunting trap full animaux
//        TileDecks tileDecks = new TileDecks(
//                List.of(Tiles.TILES.get(56)),
//                List.of(Tiles.TILES.get(61), Tiles.TILES.get(62), Tiles.TILES.get(15), Tiles.TILES.get(35), Tiles.TILES.get(16), Tiles.TILES.get(36),  Tiles.TILES.get(37), Tiles.TILES.get(76), Tiles.TILES.get(64), Tiles.TILES.get(68), Tiles.TILES.get(1)),
//                List.of(Tiles.TILES.get(94)));

//        //Hunting trap no animal
//        TileDecks tileDecks = new TileDecks(
//                List.of(Tiles.TILES.get(56)),
//                List.of(Tiles.TILES.get(4), Tiles.TILES.get(3), Tiles.TILES.get(15), Tiles.TILES.get(46), Tiles.TILES.get(29), Tiles.TILES.get(12), Tiles.TILES.get(18), Tiles.TILES.get(1), Tiles.TILES.get(37)),
//                List.of(Tiles.TILES.get(94)));

//        //Hunting trap tiger
//        TileDecks tileDecks = new TileDecks(
//                List.of(Tiles.TILES.get(56)),
//                List.of(Tiles.TILES.get(61), Tiles.TILES.get(62), Tiles.TILES.get(18), Tiles.TILES.get(35), Tiles.TILES.get(16), Tiles.TILES.get(36),  Tiles.TILES.get(37), Tiles.TILES.get(31), Tiles.TILES.get(64), Tiles.TILES.get(68), Tiles.TILES.get(1)),
//                List.of(Tiles.TILES.get(94)));
//        //Take out tile
//        TileDecks tileDecks = new TileDecks(
//                List.of(Tiles.TILES.get(56)),
//                List.of(Tiles.TILES.get(63), Tiles.TILES.get(61), Tiles.TILES.get(8)),
//                List.of(Tiles.TILES.get(94)));
//        //Pit trap full animaux
//        TileDecks tileDecks = new TileDecks(
//                List.of(Tiles.TILES.get(56)),
//                List.of(Tiles.TILES.get(61), Tiles.TILES.get(62), Tiles.TILES.get(15), Tiles.TILES.get(35), Tiles.TILES.get(16), Tiles.TILES.get(36),  Tiles.TILES.get(37), Tiles.TILES.get(76), Tiles.TILES.get(64), Tiles.TILES.get(68), Tiles.TILES.get(1)),
//                List.of(Tiles.TILES.get(92)));

//        //Pit trap tiger et PitFire
//        TileDecks tileDecks = new TileDecks(
//                List.of(Tiles.TILES.get(56)),
//                List.of(Tiles.TILES.get(61), Tiles.TILES.get(62), Tiles.TILES.get(18), Tiles.TILES.get(35), Tiles.TILES.get(16), Tiles.TILES.get(36),  Tiles.TILES.get(37), Tiles.TILES.get(31), Tiles.TILES.get(64), Tiles.TILES.get(68), Tiles.TILES.get(15), Tiles.TILES.get(26)),
//                List.of(Tiles.TILES.get(92), Tiles.TILES.get(85)));





        TextMaker textMaker = new TextMakerFr(players);
        GameState state = GameState.initial(colors, tileDecks, textMaker);


        ObjectProperty<List<String>> listAction = new SimpleObjectProperty<>(List.of());
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
                        List<String> newList = new ArrayList<>(listAction.getValue());
                        newList.add(ActionEncoder.withPlacedTile(gameStateO.getValue(), placedTile).action());
                        listAction.setValue(List.copyOf(newList));
                        gameStateO.setValue(gameState.withPlacedTile(placedTile));
                        rotation.setValue(Rotation.NONE);
                    }
                },
                occupant -> {

                    GameState gameState = gameStateO.getValue();
                    if (gameState.nextAction() == GameState.Action.OCCUPY_TILE) {
                        List<String> newList = new ArrayList<>(listAction.getValue());
                        newList.add(ActionEncoder.withNewOccupant(gameState, occupant).action());
                        listAction.setValue(List.copyOf(newList));
                        gameStateO.setValue(gameState.withNewOccupant(occupant));
                    }
                    if (gameState.nextAction() == GameState.Action.RETAKE_PAWN){
                        if (gameState.board().tileWithId(Zone.tileId(occupant.zoneId())).placer() == gameState.currentPlayer()){
                            List<String> newList = new ArrayList<>(listAction.getValue());
                            newList.add(ActionEncoder.withOccupantRemoved(gameStateO.getValue(), occupant).action());
                            listAction.setValue(List.copyOf(newList));
                            gameStateO.setValue(gameState.withOccupantRemoved(occupant));
                        }
                    }
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


        Node action = ActionsUI.create(listAction, s -> {
            ActionEncoder.StateAction stateAction = ActionEncoder.decodeAndApply(gameStateO.getValue(), s);
            if (stateAction != null) {
                GameState gs = stateAction.state();
                gameStateO.setValue(gs);
                List<String> newList = new ArrayList<>(listAction.getValue());
                newList.add(stateAction.action());
                listAction.setValue(List.copyOf(newList));
            }
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
            if (gameStateO.getValue().nextAction() == GameState.Action.OCCUPY_TILE){
                //Faire méthode
                List<String> newList = new ArrayList<>(listAction.getValue());
                newList.add(ActionEncoder.withNewOccupant(gameStateO.getValue(), o).action());
                listAction.setValue(List.copyOf(newList));
                gameStateO.setValue(gameStateO.getValue().withNewOccupant(o));
            }
            else if (gameStateO.getValue().nextAction() == GameState.Action.RETAKE_PAWN) {
                List<String> newList = new ArrayList<>(listAction.getValue());
                newList.add(ActionEncoder.withOccupantRemoved(gameStateO.getValue(), o).action());
                listAction.setValue(List.copyOf(newList));
                gameStateO.setValue(gameStateO.getValue().withOccupantRemoved(o));
            }
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
