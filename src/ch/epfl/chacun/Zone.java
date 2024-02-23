package ch.epfl.chacun;

public interface Zone {
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
        return id();
    }

    default public int localId() {
        return id() % 10;
    }

    default public SpecialPower specialPower() {
        return null;
    }
}
