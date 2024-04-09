package ch.epfl.chacun;

import java.util.List;

/**
 * Represents the four rotations that can be applied
 * to a tile before placing it on the board.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public enum Rotation {
    NONE,
    RIGHT,
    HALF_TURN,
    LEFT
    ;

    /**
     * Immutable list of all rotations in the order they are defined.
     */
    public static final List<Rotation> ALL = List.of(values());

    /**
     * Number of all possible rotations.
     */
    public static final int COUNT = ALL.size();

    /**
     * Rotation obtained by adding another rotation to the current one.
     *
     * @param that the rotation to add
     * @return the final rotation we get by adding the two rotations
     */
    public Rotation add(Rotation that) {
        return ALL.get((this.ordinal() + that.ordinal()) % COUNT);
    }

    /**
     * Rotation obtained by negating the current one
     * (rotation that undoes the current one).
     *
     * @return the negates rotation
     */
    public Rotation negated() {
        return ALL.get((COUNT - this.ordinal()) % COUNT);
    }

    /**
     * Number of quarter turns of the rotation (clockwise).
     *
     * @return the number of quarter turns
     */
    public int quarterTurnsCW() {
        return this.ordinal();
    }

    /**
     * Returns the rotation in degrees (clockwise).
     *
     * @return the rotation in degrees
     */
    public int degreesCW() {
        return this.ordinal() * 90;
    }
}
