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

    public TileDecks {
        startTiles = List.copyOf(startTiles);
        normalTiles = List.copyOf(normalTiles);
        menhirTiles = List.copyOf(menhirTiles);
    }

    /**
     * The list of deck of a given tile kind.
     *
     * @param kind the kind of the tile
     * @return the list of tiles of the given kind
     */
    private List<Tile> deckKind(Tile.Kind kind) {
        return switch (kind) {
            case START -> startTiles;
            case NORMAL -> normalTiles;
            case MENHIR -> menhirTiles;
        };
    }
    public int deckSize(Tile.Kind kind) {
        return deckKind(kind).size();
    }

    public Tile topTile(Tile.Kind kind) {
        if (deckSize(kind) == 0) {
            return null;
        }
        return deckKind(kind).getFirst();
    }

    public TileDecks withTopTileDrawn(Tile.Kind kind) {
        Preconditions.checkArgument(deckSize(kind) != 0);

        return switch (kind) {
            case START -> new TileDecks(startTiles.subList(1, startTiles.size()), normalTiles, menhirTiles);
            case NORMAL -> new TileDecks(startTiles, normalTiles.subList(1, normalTiles.size()), menhirTiles);
            case MENHIR -> new TileDecks(startTiles, normalTiles, menhirTiles.subList(1, menhirTiles.size()));
        };
    }

    public TileDecks withTopTileDrawnUntil(Tile.Kind kind, Predicate<Tile> predicate) {
        TileDecks tileDecks = this;
        if (!predicate.test(tileDecks.topTile(kind)) && deckSize(kind) != 0) {
            return tileDecks.withTopTileDrawn(kind).withTopTileDrawnUntil(kind, predicate);
        }
        return tileDecks;
    }
}
