package ch.epfl.chacun;

/**
 * Represents all the zone partitions.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 *
 * @param forests the partition of forest zones
 * @param meadows the partition of meadow zones
 * @param rivers the partition of river zones
 * @param riverSystems the partition of river system zones
 */
public record ZonePartitions(ZonePartition<Zone.Forest> forests,
                             ZonePartition<Zone.Meadow> meadows,
                             ZonePartition<Zone.River> rivers,
                             ZonePartition<Zone.Water> riverSystems) {

    /**
     * The empty zone partitions.
     */
    public final static ZonePartitions EMPTY = new ZonePartitions(
            new ZonePartition<>(), new ZonePartition<>(),
            new ZonePartition<>(), new ZonePartition<>());

    /**
     * Builder for the zone partitions.
     */
    public final static class Builder {
        private final ZonePartition.Builder<Zone.Forest> forests;
        private final ZonePartition.Builder<Zone.Meadow> meadows;
        private final ZonePartition.Builder<Zone.River> rivers;
        private final ZonePartition.Builder<Zone.Water> riverSystem;

        /**
         * Builder constructor
         *
         * @param initial zone partitions
         */
        public Builder(ZonePartitions initial) {
            forests = new ZonePartition.Builder<>(initial.forests());
            meadows = new ZonePartition.Builder<>(initial.meadows());
            rivers = new ZonePartition.Builder<>(initial.rivers());
            riverSystem = new ZonePartition.Builder<>(initial.riverSystems());
        }

        /**
         * Adds a tile to the zone partitions.
         *
         * @param tile the tile to add
         */
        public void addTile(Tile tile) {
            int[] localOpenZones = new int[10];

            // Count the number of open zones for each zone
            for (TileSide side : tile.sides()) {

                for (Zone zone : side.zones()) {
                    localOpenZones[zone.localId()]++;

                    if (zone instanceof Zone.River river && river.hasLake()) {
                        localOpenZones[river.localId()]++;
                        localOpenZones[river.lake().localId()]++;
                    }
                }
            }

            // Add the zones to the partitions
            for (Zone zone : tile.zones()) {

                if (zone instanceof Zone.Forest forest)
                    forests.addSingleton(forest, localOpenZones[forest.localId()]);

                else if (zone instanceof Zone.Meadow meadow)
                    meadows.addSingleton(meadow, localOpenZones[meadow.localId()]);

                else if (zone instanceof Zone.Lake lake)
                    riverSystem.addSingleton(lake, localOpenZones[lake.localId()]);

                else if (zone instanceof Zone.River river) {
                    if (river.hasLake())
                        rivers.addSingleton(river, localOpenZones[river.localId()] - 1);
                    else
                        rivers.addSingleton(river, localOpenZones[river.localId()]);

                    riverSystem.addSingleton(river, localOpenZones[river.localId()]);
                }
            }

            // Connect the rivers to the lakes
            for (Zone zone : tile.zones())
                if (zone instanceof Zone.River river && river.hasLake())
                    riverSystem.union(river, river.lake());
        }

        /**
         * Connects two sides of a tiles.
         *
         * @param s1 the side of the first tile
         * @param s2 the side of the second tile
         */
        public void connectSides(TileSide s1, TileSide s2) {
            switch (s1) {
                case TileSide.Forest(Zone.Forest f1)
                        when s2 instanceof TileSide.Forest(Zone.Forest f2) -> forests.union(f1, f2);

                case TileSide.Meadow(Zone.Meadow m1)
                        when s2 instanceof TileSide.Meadow(Zone.Meadow m2) -> meadows.union(m1, m2);

                case TileSide.River(Zone.Meadow m3, Zone.River r1, Zone.Meadow m4)
                        when s2 instanceof TileSide.River(Zone.Meadow m5, Zone.River r2, Zone.Meadow m6) -> {
                    meadows.union(m3, m6);
                    meadows.union(m4, m5);
                    rivers.union(r1, r2);
                    riverSystem.union(r1, r2);
                }

                default -> throw new IllegalArgumentException();
            }
        }

        /**
         * Adds an initial occupant to the zone partitions.
         *
         * @param player the color of the player
         * @param occupantKind the kind of the occupant
         * @param occupiedZone the zone the occupant is in
         * @throws IllegalArgumentException if the occupant cannot be added to the zone
         */
        public void addInitialOccupant(PlayerColor player, Occupant.Kind occupantKind, Zone occupiedZone) {
            switch (occupiedZone) {
                case Zone.Forest forest when occupantKind.equals(Occupant.Kind.PAWN) ->
                    forests.addInitialOccupant(forest, player);

                case Zone.Meadow meadow when occupantKind.equals(Occupant.Kind.PAWN) ->
                    meadows.addInitialOccupant(meadow, player);

                case Zone.Water water when occupantKind.equals(Occupant.Kind.HUT) ->
                    riverSystem.addInitialOccupant(water, player);

                case Zone.River river when occupantKind.equals(Occupant.Kind.PAWN) ->
                    rivers.addInitialOccupant(river, player);

                default -> throw new IllegalArgumentException();
            }
        }

        /**
         * Removes a pawn from the occupied zone
         *
         * @param player the color of the player
         * @param occupiedZone the zone the occupant is in
         * @throws IllegalArgumentException if there is no occupant in the zone or zone is a lake
         */
        public void removePawn(PlayerColor player, Zone occupiedZone) {
            switch (occupiedZone) {
                case Zone.Forest forest ->
                    forests.removeOccupant(forest, player);

                case Zone.Meadow meadow ->
                    meadows.removeOccupant(meadow, player);

                case Zone.River river ->
                    rivers.removeOccupant(river, player);

                default -> throw new IllegalArgumentException();
            }
        }

        /**
         * Removes all gatherers from the forest area.
         *
         * @param forest the forest to remove the gatherers from
         * @throws IllegalArgumentException if there are no gatherers in the forest area
         */
        public void clearGatherers(Area<Zone.Forest> forest) {
            forests.removeAllOccupantsOf(forest);
        }

        /**
         * Removes all fishers from the river area.
         *
         * @param river the river to remove the fishers from
         * @throws IllegalArgumentException if there are no fishers in the river area
         */
        public void clearFishers(Area<Zone.River> river) {
            rivers.removeAllOccupantsOf(river);
        }

        /**
         * Builds the zone partitions.
         *
         * @return the ZonePartitions
         */
        public ZonePartitions build() {
            return new ZonePartitions(
                    forests.build(),
                    meadows.build(),
                    rivers.build(),
                    riverSystem.build());
        }
    }
}
