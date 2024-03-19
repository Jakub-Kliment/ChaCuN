package ch.epfl.chacun;

import java.util.List;

/**
 * Represents different kinds of ine game (tile) zones .
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public sealed interface Zone {

    /**
     * All special powers of a zone
     */
    enum SpecialPower {
        SHAMAN,
        LOGBOAT,
        HUNTING_TRAP,
        PIT_TRAP,
        WILD_FIRE,
        RAFT
    }

    /**
     * The id of the tile the zone is located in.
     *
     * @param zoneId the id of the zone
     * @return the id of the tile the zone is on
     */
    static int tileId(int zoneId) {
        return zoneId / 10;
    }

    /**
     * The local id of a specific zone in the tile.
     *
     * @param zoneId the id of the zone
     * @return the local id of the specific zone in the tile
     */
    static int localId(int zoneId) {
        return zoneId % 10;
    }

    /**
     * Abstract method for the id of the zone.
     *
     * @return the id of the zone
     */
    int id();

    /**
     * The id of the tile the zone is located in.
     *
     * @return the id of the tile the zone is on
     */
    default int tileId() {
        return tileId(id());
    }

    /**
     * The local id of a specific zone in the tile.
     *
     * @return the local id of the specific zone in the tile
     */
    default int localId() {
        return id() % 10;
    }

    /**
     * The special power of the zone.
     *
     * @return the special power of the zone (by default null)
     */
    default SpecialPower specialPower() {
        return null;
    }


    /**
     * Forest zone
     *
     * @param id the id of the zone
     * @param kind the kind of the forest
     */
    record Forest(int id, Kind kind) implements Zone {

        /**
         * All kinds of a forest
         */
        public enum Kind {
            PLAIN,
            WITH_MENHIR,
            WITH_MUSHROOMS
        }
    }

    /**
     * Meadow zone
     *
     * @param id the id of the zone
     * @param animals list of animals in the meadow
     * @param specialPower the special power of the meadow (can be null)
     */
    record Meadow(int id, List<Animal> animals, SpecialPower specialPower) implements Zone {

        /**
         * Compact constructor
         */
        public Meadow {
            // Defensive copy of animals
            animals = List.copyOf(animals);
        }
    }

    /**
     * Water zone
     */
    sealed interface Water extends Zone {
        // The number of fish in the water
        int fishCount();
    }

    /**
     * Lake zone
     *
     * @param id the id of the zone
     * @param fishCount the number of fish in the lake
     * @param specialPower the special power of the lake (can be null)
     */
    record Lake(int id, int fishCount, SpecialPower specialPower) implements Water {
    }

    /**
     * River zone
     *
     * @param id the id of the zone
     * @param fishCount the number of fish in the river
     * @param lake the lake the river is connected to (can be null)
     */
    record River(int id, int fishCount, Lake lake) implements Water {

        /**
         * Boolean that checks whether a river is connected to a lake.
         *
         * @return true if the river is connected to a lake, false otherwise
         */
        public boolean hasLake() {
            return lake != null;
        }
    }
}
