package ch.epfl.chacun;

import ch.epfl.chacun.tile.Tiles;
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
            if (tile.kind().equals(Tile.Kind.NORMAL))
                menhir.add(tile);
        return menhir;
    }
    List<Tile> startingTiles = List.of(Tiles.TILES.get(56));
    List<Tile> normalTiles = normalTileDeck();
    List<Tile> menhirTiles = menhirTileDeck();
    TileDecks tileDecks = new TileDecks(startingTiles, normalTiles, menhirTiles);

    TextMaker textMaker = new BasicTextMaker();



    @Test
    void cancelledAnimal() {
        Area<Zone.Meadow> area = new Area<>(new HashSet<>(Set.of(new Zone.Meadow(1, List.of(new Animal(1, Animal.Kind.DEER), new Animal(2, Animal.Kind.DEER), new Animal(3, Animal.Kind.DEER), new Animal(4, Animal.Kind.MAMMOTH), new Animal(5, Animal.Kind.TIGER)), null))), new ArrayList<>(), 0);
        Set<Animal> setAnimal = GameState.cancelAnimalUpdate(area, new HashSet<>());
        Set<Animal> setSearch = Set.of(new Animal(1, Animal.Kind.DEER));
        assertEquals(setSearch, setAnimal);
    }

    @Test
    void cancelledAnimal2() {
        Area<Zone.Meadow> area = new Area<>(new HashSet<>(Set.of(new Zone.Meadow(1, List.of(new Animal(1, Animal.Kind.MAMMOTH), new Animal(2, Animal.Kind.TIGER), new Animal(3, Animal.Kind.MAMMOTH), new Animal(4, Animal.Kind.MAMMOTH), new Animal(5, Animal.Kind.TIGER)), null))), new ArrayList<>(), 0);
        Set<Animal> setAnimal = GameState.cancelAnimalUpdate(area, new HashSet<>());
        Set<Animal> setSearch = Set.of();
        assertEquals(setSearch, setAnimal);
    }

    @Test
    void cancelledAnimal1() {
        Area<Zone.Meadow> area = new Area<>(new HashSet<>(Set.of(new Zone.Meadow(1, List.of(new Animal(1, Animal.Kind.DEER), new Animal(2, Animal.Kind.DEER), new Animal(3, Animal.Kind.DEER), new Animal(4, Animal.Kind.MAMMOTH), new Animal(5, Animal.Kind.MAMMOTH)), null))), new ArrayList<>(), 0);
        Set<Animal> setAnimal = GameState.cancelAnimalUpdate(area, new HashSet<>());
        Set<Animal> setSearch = Set.of();
        assertEquals(setSearch, setAnimal);
    }
    @Test
    void cancelledAnimal3() {
        Area<Zone.Meadow> area = new Area<>(new HashSet<>(Set.of(new Zone.Meadow(1, List.of(new Animal(1, Animal.Kind.DEER), new Animal(2, Animal.Kind.DEER), new Animal(3, Animal.Kind.DEER), new Animal(4, Animal.Kind.TIGER), new Animal(5, Animal.Kind.TIGER)), null))), new ArrayList<>(), 0);
        Set<Animal> setAnimal = GameState.cancelAnimalUpdate(area, new HashSet<>());
        Set<Animal> setSearch = Set.of(new Animal(1, Animal.Kind.DEER), new Animal(2, Animal.Kind.DEER));
        assertEquals(setSearch, setAnimal);
    }

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
    void freeOccupantCountReturnsCorrectOccupantCount() {
    }
}