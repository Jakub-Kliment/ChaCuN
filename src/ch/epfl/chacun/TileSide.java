package ch.epfl.chacun;

import java.util.List;

/**
 * Represents the side of a tile.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public sealed interface TileSide {

    /**
     * List of zones of the side of a tile.
     *
     * @return list of zones of the side
     */
    List<Zone> zones();

    /**
     * Method to check if two sides are of the same kind.
     *
     * @param that the side of a tile to compare to
     * @return true if the sides are of the same kind, false otherwise
     */
    boolean isSameKindAs(TileSide that);

    /**
     * Forest type on tile side.
     *
     * @param forest the forest zone of the tile side
     */
    record Forest(Zone.Forest forest) implements TileSide {

        /**
         * The forest zone of the side.
         *
         * @return list of zones (forest) of the side
         */
        @Override
        public List<Zone> zones() {
            return List.of(forest);
        }

        /**
         * Method to check if two sides are of the same kind (forests).
         *
         * @param that the side to compare to
         * @return true if the sides are of the same kind, false otherwise
         */
        @Override
        public boolean isSameKindAs(TileSide that) {
            return that instanceof Forest;
        }
    }

    /**
     * Meadow type on tile side.
     *
     * @param meadow the meadow zone of the tile side
     */
    record Meadow(Zone.Meadow meadow) implements TileSide {

        /**
         * The meadow zone of the side.
         *
         * @return list of zones (meadow) of the side
         */
        @Override
        public List<Zone> zones() {
            return List.of(meadow);
        }

        /**
         * Method to check if two sides are of the same kind (meadows).
         *
         * @param that the side to compare to
         * @return true if the sides are of the same kind, false otherwise
         */
        @Override
        public boolean isSameKindAs(TileSide that) {
            return that instanceof Meadow;
        }
    }

    /**
     * River type on tile side.
     *
     * @param meadow1 the first meadow zone of the tile
     * @param river the river zone of the tile
     * @param meadow2 the second meadow zone of the tile
     */
    record River(Zone.Meadow meadow1, Zone.River river, Zone.Meadow meadow2) implements TileSide {

        /**
         * The zones (two meadows and one river between them) of the tile side.
         *
         * @return list of zones (meadow 1, river, meadow 2) of the side in order
         */
        @Override
        public List<Zone> zones() {
            return List.of(meadow1, river, meadow2);
        }

        /**
         * Method to check if two sides are of the same kind (rivers).
         *
         * @param that the side to compare to
         * @return true if the sides are of the same kind, false otherwise
         */
        @Override
        public boolean isSameKindAs(TileSide that) {
            return that instanceof River;
        }
    }
}
