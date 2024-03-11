package ch.epfl.chacun;

public record ZonePartitions(ZonePartition<Zone.Forest> forests, ZonePartition<Zone.Meadow> meadows,
                             ZonePartition<Zone.River> rivers, ZonePartition<Zone.Water> riverSystems) {

    public final static ZonePartitions EMPTY = new ZonePartitions(
            new ZonePartition<>(), new ZonePartition<>(),
            new ZonePartition<>(), new ZonePartition<>());

    public final static class Builder {
        private ZonePartition.Builder<Zone.Forest> forests;
        private ZonePartition.Builder<Zone.Meadow> meadows;
        private ZonePartition.Builder<Zone.River> rivers;
        private ZonePartition.Builder<Zone.Water> riverSystem;

        public Builder(ZonePartitions initial) {
            forests = new ZonePartition.Builder<>(initial.forests());
            meadows = new ZonePartition.Builder<>(initial.meadows());
            rivers = new ZonePartition.Builder<>(initial.rivers());
            riverSystem = new ZonePartition.Builder<>(initial.riverSystems());
        }

        public void addTile(Tile tile) {
            int[] localOpenZones = new int[10];

            for (TileSide side : tile.sides()) {

                for (Zone zone : side.zones()) {
                    localOpenZones[zone.localId()]++;

                    if (zone instanceof Zone.River river && river.hasLake()) {
                        localOpenZones[river.localId()]++;
                        localOpenZones[river.lake().localId()]++;
                    }
                }
            }

            for (Zone zone : tile.zones()) {

                if (zone instanceof Zone.Forest forest)
                    forests.addSingleton(forest, localOpenZones[forest.localId()]);

                else if (zone instanceof Zone.Meadow meadow)
                    meadows.addSingleton(meadow, localOpenZones[meadow.localId()]);

                else if (zone instanceof Zone.Water water) {

                    if (water instanceof Zone.River river && !river.hasLake())
                        rivers.addSingleton(river, localOpenZones[river.localId()]);

                    else if (water instanceof Zone.River river) {
                        rivers.addSingleton(river, localOpenZones[river.localId()] - 1);
                        riverSystem.addSingleton(river, localOpenZones[river.localId()]);
                    }

                    else
                        riverSystem.addSingleton(water, localOpenZones[water.localId()]);
                }
            }

            for (Zone zone : tile.zones()) {
                if (zone instanceof Zone.River river && river.hasLake())
                    riverSystem.union(river, river.lake());
            }
        }

        public void connectSides(TileSide s1, TileSide s2) {
            switch (s1) {
                case TileSide.Forest(Zone.Forest f1)
                        when s2 instanceof TileSide.Forest(Zone.Forest f2) -> forests.union(f1, f2);

                case TileSide.Meadow(Zone.Meadow m1)
                        when s2 instanceof TileSide.Meadow(Zone.Meadow m2) -> meadows.union(m1, m2);

                case TileSide.River(Zone.Meadow m3, Zone.River r1, Zone.Meadow m4)
                        when s2 instanceof TileSide.River(Zone.Meadow m5, Zone.River r2, Zone.Meadow m6) -> {
                    // pas sure si il faut verifier la rotation !!!!!!!!!!
                    meadows.union(m3, m5);
                    meadows.union(m4, m6);

                    // faut verifier si les rivieres ont plusieurs lacs !!!!!!!
                    if (r1.hasLake() || r2.hasLake())
                        riverSystem.union(r1, r2);

                    else
                        rivers.union(r1, r2);

                }
                default -> throw new IllegalArgumentException();
            }
        }

        public void addInitialOccupant(PlayerColor player, Occupant.Kind occupantKind, Zone occupiedZone) {
            switch (occupiedZone) {
                case Zone.Forest forest -> {
                    Preconditions.checkArgument(occupantKind.equals(Occupant.Kind.PAWN));
                    forests.addInitialOccupant(forest, player);
                }

                case Zone.Meadow meadow -> {
                    Preconditions.checkArgument(occupantKind.equals(Occupant.Kind.PAWN));
                    meadows.addInitialOccupant(meadow, player);
                }

                case Zone.River river -> {
                    if (!river.hasLake())
                        rivers.addInitialOccupant(river, player);

                    else {
                        riverSystem.addInitialOccupant(river, player);
                        riverSystem.addInitialOccupant(river.lake(), player);
                    }
                }

                default -> throw new IllegalArgumentException();
            }
        }

        public void removePawn(PlayerColor player, Zone occupiedZone) {
            switch (occupiedZone) {
                case Zone.Forest forest ->
                    forests.removeOccupant(forest, player);

                case Zone.Meadow meadow ->
                    meadows.removeOccupant(meadow, player);

                case Zone.River river -> {
                    rivers.removeOccupant(river, player);
                    riverSystem.removeOccupant(river, player);
                }

                default -> throw new IllegalArgumentException();
            }
        }

        public void clearGatherers(Area<Zone.Forest> forest) {
            forests.removeAllOccupantsOf(forest);
        }

        public void clearFishers(Area<Zone.River> river) {
            rivers.removeAllOccupantsOf(river);
        }

        public ZonePartitions build() {
            return new ZonePartitions(forests.build(), meadows.build(),
                    rivers.build(), riverSystem.build());
        }
    }
}
