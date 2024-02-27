package ch.epfl.chacun;

import java.util.List;
import java.util.function.Predicate;

/**
 * Lists of all decks of tiles (for each kind of tile)
 *
 * @param startTiles
 * @param normalTiles
 * @param menhirTiles
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public record TileDecks(List<Tile> startTiles, List<Tile> normalTiles, List<Tile> menhirTiles) {

    // pas sure (doit etre immuable)
    public TileDecks {
        startTiles = List.copyOf(startTiles);
        normalTiles = List.copyOf(normalTiles);
        menhirTiles = List.copyOf(menhirTiles);
    }

    public int deckSize(Tile.Kind kind) {
        return switch (kind) {
            case START -> startTiles.size();
            case NORMAL -> normalTiles.size();
            case MENHIR -> menhirTiles.size();
        };
    }

    public Tile topTile(Tile.Kind kind) {
        if (deckSize(kind) == 0) {
            return null;
        }
        return switch (kind) {
            case START -> startTiles.getFirst();
            case NORMAL -> normalTiles.getFirst();
            case MENHIR -> menhirTiles.getFirst();
        };
    }

    // pas sure si il faut garder l'element enleve ou pas
    public TileDecks withTopTileDrawn(Tile.Kind kind) {
        Preconditions.checkArgument(deckSize(kind) != 0);
        switch (kind) {
            case START -> startTiles.remove(0);
            case NORMAL -> normalTiles.remove(0);
            case MENHIR -> menhirTiles.remove(0);
        }
        return new TileDecks(startTiles, normalTiles, menhirTiles);
    }

    public TileDecks withTopTileDrawnUntil(Tile.Kind kind, Predicate<Tile> predicate) {
        /*
        switch (kind) {
            case START -> {
                while (!predicate.test(top)) {
                    startTiles.remove(0);
                }
            }
            case NORMAL -> {
                while (!predicate.test(normalTiles.getFirst())) {
                    normalTiles.remove(0);
                }
            }
            case MENHIR -> {
                while (!predicate.test(menhirTiles.getFirst())) {
                    normalTiles.remove(0);
                }
            }
        }
        */
        while (!predicate.test(topTile(kind))) {
            withTopTileDrawn(kind);
        }
        return new TileDecks(startTiles, normalTiles, menhirTiles);
    }
}
