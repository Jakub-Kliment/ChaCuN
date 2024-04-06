package ch.epfl.chacun.mytests;

import ch.epfl.chacun.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MyTileDecksTest {
    Zone.Meadow meadowWithSuperpower = new Zone.Meadow(560, List.of(new Animal(1, Animal.Kind.AUROCHS)), Zone.SpecialPower.HUNTING_TRAP);
    Zone.Forest forest = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
    Zone.Meadow meadowWithNoSuperpower = new Zone.Meadow(562, List.of(), null);
    Zone.River river = new Zone.River(563, 1, null);

    TileSide n = new TileSide.Meadow(meadowWithSuperpower);
    TileSide e = new TileSide.Forest(forest);
    TileSide s = new TileSide.Forest(forest);
    TileSide w = new TileSide.River(meadowWithNoSuperpower, river, meadowWithSuperpower);
    TileSide n1 = new TileSide.Meadow(meadowWithNoSuperpower);

    public Tile startTile = new Tile(1, Tile.Kind.START, n, e, s, w);

    public Tile tileWithNoSuperpower = new Tile(1, Tile.Kind.START, n1, n1, n1, n1);

    public Tile tileWithNoForest = new Tile(1, Tile.Kind.START, n, n, n, n);

    public Tile tileWithNoMeadow = new Tile(1, Tile.Kind.START, e, e, e, e);

    public Tile tileWithNoRiver = new Tile(1, Tile.Kind.START, s, s, s, s);

    @Test
    void deckSizeWorksOnEveryKind(){
        TileDecks tileDecks = new TileDecks(List.of(startTile, tileWithNoForest), List.of(), List.of(tileWithNoMeadow, tileWithNoRiver, tileWithNoSuperpower));
        assertEquals(2, tileDecks.deckSize(Tile.Kind.START));
        assertEquals(0, tileDecks.deckSize(Tile.Kind.NORMAL));
        assertEquals(3, tileDecks.deckSize(Tile.Kind.MENHIR));
    }

    @Test
    void topTileWorksOnEveryKind(){
        TileDecks tileDecks = new TileDecks(List.of(startTile, tileWithNoForest), List.of(tileWithNoForest), List.of(tileWithNoMeadow, tileWithNoRiver, tileWithNoSuperpower));
        assertEquals(startTile, tileDecks.topTile(Tile.Kind.START));
        assertEquals(tileWithNoForest, tileDecks.topTile(Tile.Kind.NORMAL));
        assertEquals(tileWithNoMeadow, tileDecks.topTile(Tile.Kind.MENHIR));
    }

    @Test
    void topTileReturnsNullWhenEmpty(){
        TileDecks tileDecks = new TileDecks(List.of(), List.of(), List.of());
        assertNull(tileDecks.topTile(Tile.Kind.START));
        assertNull(tileDecks.topTile(Tile.Kind.NORMAL));
        assertNull(tileDecks.topTile(Tile.Kind.MENHIR));
    }

    @Test
    void withTopTileDrawnWorksOnEveryKind(){
        TileDecks tileDecks = new TileDecks(List.of(startTile, tileWithNoForest), List.of(tileWithNoForest), List.of(tileWithNoMeadow, tileWithNoRiver, tileWithNoSuperpower));
        TileDecks newTileDecks = tileDecks.withTopTileDrawn(Tile.Kind.START);
        TileDecks newTileDecks2 = tileDecks.withTopTileDrawn(Tile.Kind.NORMAL);
        TileDecks newTileDecks3 = tileDecks.withTopTileDrawn(Tile.Kind.MENHIR);
        assertEquals(1, newTileDecks.deckSize(Tile.Kind.START));
    }

}
