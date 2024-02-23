package ch.epfl.chacun;

import java.util.List;

/**
 * Represents the four cardinal directions.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public enum Direction {
    N,
    E,
    S,
    W
    ;

    // The four cardinal directions in order
    public static final List<Direction> ALL = List.of(values());

    // The number of directions
    public static final int COUNT = ALL.size();

    /**
     * Direction obtained by rotating by any given rotation.
     *
     * @param rotation the rotation to apply (add)
     * @return the final direction we get by rotating this direction by the given rotation
     */
    public Direction rotated(Rotation rotation) {
        return ALL.get((this.ordinal() + rotation.ordinal()) % COUNT);
    }

    /**
     * Opposite direction of this direction.
     *
     * @return the opposite direction of this direction
     */
    public Direction opposite() {
        return ALL.get((this.ordinal() + 2) % COUNT);
    }
}
