package ch.epfl.chacun;

import java.util.List;

public enum Direction {
    N,
    E,
    S,
    W
    ;
    public static final List<Direction> ALL = List.of(values());
    public static final int COUNT = ALL.size();
    public Direction rotated(Rotation rotation) {
        return ALL.get((this.ordinal() + rotation.ordinal()) % COUNT);
    }
    public Direction opposite() {
        return ALL.get((this.ordinal() + 2) % COUNT);
    }
}
