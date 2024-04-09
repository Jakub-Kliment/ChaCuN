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

    /**
     * Immutable list of all directions.
     */
    public static final List<Direction> ALL = List.of(values());

    /**
     * The number of all possible directions.
     */
    public static final int COUNT = ALL.size();

    /**
     * The direction obtained by rotating by any given rotation.
     * (Direction after addition of a rotation).
     *
     * @param rotation the rotation to apply (add)
     * @return the final direction we get by rotating this direction by the given rotation
     */
    public Direction rotated(Rotation rotation) {
        return ALL.get((this.ordinal() + rotation.ordinal()) % COUNT);
    }

    /**
     * Returns the opposite direction of this direction.
     *
     * @return the opposite direction
     */
    public Direction opposite() {
        return ALL.get((this.ordinal() + 2) % COUNT);
    }
}
