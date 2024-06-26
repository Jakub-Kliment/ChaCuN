package ch.epfl.chacun.mytests;

import ch.epfl.chacun.*;
import ch.epfl.chacun.Tiles;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MyGameStateTest {


    private List<PlayerColor> players() {
        List<PlayerColor> playerColors = new ArrayList<>();
        playerColors.addAll(PlayerColor.ALL);
        return playerColors;
    }
    List<PlayerColor> players = players();

    private List<Tile> normalTileDeck() {
        List<Tile> normal = new ArrayList<>();
        for (Tile tile : Tiles.TILES)
            if (tile.kind().equals(Tile.Kind.NORMAL))
                normal.add(tile);
        return normal;
    }

    private List<Tile> menhirTileDeck() {
        List<Tile> menhir = new ArrayList<>();
        for (Tile tile : Tiles.TILES)
            if (tile.kind().equals(Tile.Kind.MENHIR))
                menhir.add(tile);
        return menhir;
    }
    List<Tile> startingTiles = List.of(Tiles.TILES.get(56));
    List<Tile> normalTiles = normalTileDeck();
    List<Tile> menhirTiles = menhirTileDeck();
    TileDecks tileDecks = new TileDecks(startingTiles, normalTiles, menhirTiles);
    TextMaker textMaker = new TextMaker() {
        @Override
        public String playerName(PlayerColor playerColor) {
            return null;
        }

        @Override
        public String points(int points) {
            return null;
        }

        @Override
        public String playerClosedForestWithMenhir(PlayerColor player) {
            return null;
        }

        @Override
        public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
            return null;
        }

        @Override
        public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
            return null;
        }

        @Override
        public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
            return null;
        }

        @Override
        public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
            return null;
        }

        @Override
        public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
            return null;
        }

        @Override
        public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
            return null;
        }

        @Override
        public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
            return null;
        }

        @Override
        public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
            return null;
        }

        @Override
        public String playersWon(Set<PlayerColor> winners, int points) {
            return null;
        }

        @Override
        public String clickToOccupy() {
            return null;
        }

        @Override
        public String clickToUnoccupy() {
            return null;
        }
    };
    

    @Test
    void gameStateConstructorIsImmutable() {
        GameState gameState = GameState.initial(players, tileDecks, textMaker);
        players.remove(PlayerColor.PURPLE);
        assertNotEquals(gameState.players(), players);
        players.add(PlayerColor.PURPLE);
    }

    @Test
    void constructorThrowsIllegalArgumentExceptionForNullValues() {
        assertThrows(NullPointerException.class, () -> new GameState(players, null, null, Board.EMPTY,
                GameState.Action.START_GAME, new MessageBoard(textMaker, new ArrayList<>())));
        assertThrows(NullPointerException.class, () -> new GameState(players, tileDecks, null, null,
                GameState.Action.START_GAME, new MessageBoard(textMaker, new ArrayList<>())));
        assertThrows(NullPointerException.class, () -> new GameState(players, tileDecks, null, Board.EMPTY,
                null, new MessageBoard(textMaker, new ArrayList<>())));
        assertThrows(NullPointerException.class, () -> new GameState(players, tileDecks, null, Board.EMPTY,
                GameState.Action.START_GAME, null));

        assertThrows(IllegalArgumentException.class, () -> new GameState(players, tileDecks, null, Board.EMPTY,
                GameState.Action.PLACE_TILE, new MessageBoard(textMaker, new ArrayList<>())));
        assertThrows(IllegalArgumentException.class, () -> new GameState(List.of(PlayerColor.RED), tileDecks, null,
                Board.EMPTY, GameState.Action.START_GAME, new MessageBoard(textMaker, new ArrayList<>())));
        assertThrows(IllegalArgumentException.class, () -> new GameState(players, tileDecks, tileDecks.topTile(Tile.Kind.START),
                Board.EMPTY, GameState.Action.START_GAME, new MessageBoard(textMaker, new ArrayList<>())));
    }

    @Test
    void initialGameStateWorksAsIntended() {
        GameState initial = GameState.initial(players, tileDecks, textMaker);
        assertEquals(initial.players(), players);
        assertEquals(initial.board(), Board.EMPTY);
        assertEquals(initial.tileDecks(), tileDecks);
        assertNull(initial.tileToPlace());
        assertEquals(initial.messageBoard().textMaker(), textMaker);
        assertEquals(initial.messageBoard().messages(), new ArrayList<>());
    }

    @Test
    void currentPlayerReturnsCorrectCurrentPlayer() {
        GameState gameState = new GameState(players, tileDecks, null, Board.EMPTY,
                GameState.Action.OCCUPY_TILE, new MessageBoard(textMaker, new ArrayList<>()));
        assertEquals(gameState.currentPlayer(), players.getFirst());
        players.remove(0);
        assertNotEquals(gameState.currentPlayer(), players.getFirst());
        players = players();
    }

    @Test
    void currentPlayerReturnsNullForStartOrEnd() {
        GameState gameState = new GameState(players, tileDecks, null, Board.EMPTY,
                GameState.Action.START_GAME, new MessageBoard(textMaker, new ArrayList<>()));
        GameState gameState2 = new GameState(players, tileDecks, null, Board.EMPTY,
                GameState.Action.END_GAME, new MessageBoard(textMaker, new ArrayList<>()));

        assertNull(gameState.currentPlayer());
        assertNull(gameState2.currentPlayer());
    }

    @Test
    void freeOccupantCountReturnsCorrectOccupantCountForNewGame() {
        GameState gamestate = new GameState(players, tileDecks, tileDecks.topTile(Tile.Kind.NORMAL),
                Board.EMPTY, GameState.Action.PLACE_TILE, new MessageBoard(textMaker, new ArrayList<>()));
        for (PlayerColor player : players) {
            assertEquals(gamestate.freeOccupantsCount(player, Occupant.Kind.HUT), Occupant.occupantsCount(Occupant.Kind.HUT));
            assertEquals(gamestate.freeOccupantsCount(player, Occupant.Kind.PAWN), Occupant.occupantsCount(Occupant.Kind.PAWN));
        }
    }

    @Test
    void freeOccupantCountReturnsCorrectOccupantCountForNormalGame() {
        GameState gameState = GameState.initial(players, tileDecks, textMaker);
        gameState = gameState.withStartingTilePlaced();
        Occupant occupant = new Occupant(Occupant.Kind.PAWN, Tiles.TILES.get(0).w().zones().get(0).id());
        PlacedTile placedTile = new PlacedTile(Tiles.TILES.get(0), players.get(0), Rotation.NONE, new Pos(1, 0));
        gameState = gameState.withPlacedTile(placedTile);
        gameState = gameState.withNewOccupant(occupant);
        assertEquals(gameState.freeOccupantsCount(PlayerColor.RED, Occupant.Kind.HUT), Occupant.occupantsCount(Occupant.Kind.HUT));
        assertEquals(gameState.freeOccupantsCount(PlayerColor.RED, Occupant.Kind.PAWN), Occupant.occupantsCount(Occupant.Kind.PAWN) - 1);
    }

    @Test
    void lastTilePotentialOccupantsWorksForOrdinaryGame() {
        GameState gameState = GameState.initial(players, tileDecks, textMaker);
        gameState = gameState.withStartingTilePlaced();
        PlacedTile placedTile = new PlacedTile(Tiles.TILES.get(0), players.getFirst(), Rotation.NONE, new Pos(1, 0));
        gameState = gameState.withPlacedTile(placedTile);
        gameState = gameState.withNewOccupant(new Occupant(Occupant.Kind.PAWN, Tiles.TILES.get(0).w().zones().get(0).id()));
        PlacedTile placedTile2 = new PlacedTile(Tiles.TILES.get(2), players.getFirst(), Rotation.RIGHT, new Pos(0, 1));
        gameState = gameState.withPlacedTile(placedTile2);
        assertNotEquals(gameState.lastTilePotentialOccupants(),  placedTile2.potentialOccupants());
    }

    @Test
    void withStartingTilePlacedThrowsException() {
        GameState gameState = new GameState(players, tileDecks, Tiles.TILES.get(0), Board.EMPTY,
                GameState.Action.PLACE_TILE, new MessageBoard(textMaker, new ArrayList<>()));
        assertThrows(IllegalArgumentException.class, gameState::withStartingTilePlaced);
    }

    @Test
    void withStartingTilePlacedWorksCorrectly() {
        GameState gameState = GameState.initial(players, tileDecks, textMaker);
        gameState = gameState.withStartingTilePlaced();

        assertEquals(gameState.board().tileAt(Pos.ORIGIN).tile(), Tiles.TILES.get(56));
        assertTrue(gameState.tileDecks().startTiles().isEmpty());
        assertEquals(gameState.tileToPlace(), normalTiles.getFirst());
        assertEquals(gameState.tileDecks().deckSize(Tile.Kind.NORMAL), normalTiles.size() - 1);
    }

    @Test
    void withPlacedTileThrowsExceptionWhenItShould() {
        GameState gameState = new GameState(players, tileDecks, null, Board.EMPTY,
                GameState.Action.START_GAME, new MessageBoard(textMaker, new ArrayList<>()));
        PlacedTile placedTile = new PlacedTile(normalTiles.get(0), gameState.currentPlayer(), Rotation.NONE, new Pos(1, 0));
        assertThrows(IllegalArgumentException.class, () -> gameState.withPlacedTile(placedTile));

        GameState gameState2 = new GameState(players, tileDecks, normalTiles.get(0), Board.EMPTY,
                GameState.Action.PLACE_TILE, new MessageBoard(textMaker, new ArrayList<>()));
        PlacedTile placedTile2 = new PlacedTile(normalTiles.get(0), gameState.currentPlayer(),
                Rotation.NONE, new Pos(1, 0), new Occupant(Occupant.Kind.PAWN, 1));
        assertThrows(IllegalArgumentException.class, () -> gameState2.withPlacedTile(placedTile2));
    }
}