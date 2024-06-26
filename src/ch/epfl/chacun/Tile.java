package ch.epfl.chacun;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a tile in the game.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 *
 * @param id the id of the tile
 * @param kind the kind of the tile
 * @param n the north side of the tile
 * @param e the east side of the tile
 * @param s the south side of the tile
 * @param w the west side of the tile
 */

public record Tile(int id, Kind kind, TileSide n, TileSide e, TileSide s, TileSide w) {

    /**
     * The kind of the tile.
     */
    public enum Kind {
        START,
        NORMAL,
        MENHIR
    }

    /**
     * List of all sides of the tile in order of their directions (clockwise).
     *
     * @return the list of all sides of the tile
     */
    public List<TileSide> sides() {
        return List.of(n, e, s, w);
    }


    /**
     * Set of all zones on the sides of the tile.
     * (All zones except lakes)
     *
     * @return the set of all zones on the sides of the tile
     */
    public Set<Zone> sideZones() {
        Set<Zone> zones = new HashSet<>();
        for (TileSide side : sides())
            zones.addAll(side.zones());
        return zones;
    }


    /**
     * Set of all zones on the tile.
     * (Including lakes)
     *
     * @return the set of all zones on the tile
     */
    public Set<Zone> zones() {
        Set<Zone> zones = new HashSet<>();
        for (Zone zone : sideZones()) {
            zones.add(zone);
            if (zone instanceof Zone.River river && river.hasLake())
                zones.add(river.lake());
        }
        return zones;
    }
}
