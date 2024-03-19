package ch.epfl.chacun;

import java.util.List;
import java.util.function.Predicate;

/**
 * Lists of all decks of tiles (for each kind of tile)
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public record TileDecks(List<Tile> startTiles, List<Tile> normalTiles, List<Tile> menhirTiles) {

    /**
     * Immutable constructor
     *
     * @param startTiles list of starting tiles
     * @param normalTiles list of normal tiles
     * @param menhirTiles list of tiles with a menhir
     */
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

    /**
     * The size of the deck of a given tile kind.
     *
     * @param kind the kind of the tile
     * @return the size of the deck of the given kind
     */
    public int deckSize(Tile.Kind kind) {
        return deckKind(kind).size();
    }

    /**
     * The top tile of a given tile kind.
     *
     * @param kind the kind of the tile
     * @return the top tile of the given kind
     */
    public Tile topTile(Tile.Kind kind) {
        if (deckSize(kind) == 0)
            return null;

        return deckKind(kind).getFirst();
    }

    /**
     * The deck of a given tile kind without its top tile.
     *
     * @param kind the kind of the tile
     * @throws IllegalArgumentException if the deck size of a kind is not 0
     * @return the deck of the given kind without its top tile
     */
    public TileDecks withTopTileDrawn(Tile.Kind kind) {
        Preconditions.checkArgument(deckSize(kind) != 0);

        return switch (kind) {
            case START -> new TileDecks(startTiles.subList(1, startTiles.size()), normalTiles, menhirTiles);
            case NORMAL -> new TileDecks(startTiles, normalTiles.subList(1, normalTiles.size()), menhirTiles);
            case MENHIR -> new TileDecks(startTiles, normalTiles, menhirTiles.subList(1, menhirTiles.size()));
        };
    }

    /**
     * Returns a deck of a given tile kind without its top tile, until a given predicate is satisfied.
     *
     * @param kind the kind of the tile
     * @param predicate the predicate to satisfy
     * @return the deck of the given kind without its top tile, until the predicate is satisfied
     */
    public TileDecks withTopTileDrawnUntil(Tile.Kind kind, Predicate<Tile> predicate) {
        if (!predicate.test(this.topTile(kind)) && deckSize(kind) != 0)
            return this.withTopTileDrawn(kind).withTopTileDrawnUntil(kind, predicate);

        return this;
    }
}
