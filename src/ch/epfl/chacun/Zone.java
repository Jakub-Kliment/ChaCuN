package ch.epfl.chacun;

import java.util.List;

public sealed interface Zone {
    enum SpecialPower {
        SHAMAN,
        LOGBOAT,
        HUNTING_TRAP,
        PIT_TRAP,
        WILD_FIRE,
        RAFT
    }

    public static int tileId(int zoneId) {
        return zoneId / 10;
    }

    public static int localId(int zoneId) {
        return zoneId % 10;
    }

    public abstract int id();

    default public int tileId() {
        return tileId(id());
    }

    default public int localId() {
        return id() % 10;
    }

    default public SpecialPower specialPower() {
        return null;
    }


    public record Forest(int id, Kind kind) implements Zone {
        public enum Kind {
            PLAIN,
            WITH_MENHIR,
            WITH_MUSHROOMS
        }
    }

    public record Meadow(int id, List<Animal> animals, SpecialPower specialPower) implements Zone {
        public Meadow {
            animals = List.copyOf(animals);
        }
    }

    public sealed interface Water extends Zone {
        int fishCount();
    }

    public record Lake(int id, int fishCount, SpecialPower specialPower) implements Water {
    }

    public record River(int id, int fishCount, Lake lake) implements Water {
        public boolean hasLake() {
            return lake != null;
        }
    }
}
