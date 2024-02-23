package ch.epfl.chacun;

import java.util.List;
import java.util.Set;

/**
 * Represents a tile in the game.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public record Tile(int id, Kind kind, TileSide n, TileSide e, TileSide s, TileSide w) {

    // The tile kinds
    enum Kind {
        START,
        NORMAL,
        MENHIR
    }

    /**
     * List of all sides of the tile.
     *
     * @return the list of all sides of the tile
     */
    public List<TileSide> sides() {
        return List.of(n, e, s, w);
    }

    public Set<Zone> sideZones() {
        return null;
    }

    public Set<Zone> zones() {
        return null;
    }
}
