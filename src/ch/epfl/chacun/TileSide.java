package ch.epfl.chacun;

import java.util.List;

public sealed interface TileSide {
    List<Zone> zones();

    boolean isSameKindAs(TileSide that);

    record Forest(Zone.Forest forest) implements TileSide {

        @Override
        public List<Zone> zones() {
            return List.of(forest);
        }

        @Override
        public boolean isSameKindAs(TileSide that) {
            // return that instanceof Forest; !!!!!!!!!
            return this.getClass() == that.getClass();
        }
    }

    record Meadow(Zone.Meadow meadow) implements TileSide {

        @Override
        public List<Zone> zones() {
            return List.of(meadow);
        }

        @Override
        public boolean isSameKindAs(TileSide that) {
            // return that instanceof Meadow; !!!!!!!!!
            return this.getClass() == that.getClass();
        }
    }

    record River(Zone.Meadow meadow1, Zone.River river, Zone.Meadow meadow2) implements TileSide {

        @Override
        public List<Zone> zones() {
            return List.of(meadow1, river, meadow2);
        }

        @Override
        public boolean isSameKindAs(TileSide that) {
            // return that instanceof River; !!!!!!!!!
            return this.getClass() == that.getClass();
        }
    }
}
