package ch.epfl.chacun;

import java.util.Objects;

/**
 * Represents the occupants of the game.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public record Occupant(Kind kind, int zoneId) {

    // The kind of the occupant
    public enum Kind {
        PAWN,
        HUT
    }

    /**
     * Constructs an occupant of the given kind and the zone where he is located.
     *
     * @param kind the kind of the occupant
     * @param zoneId the zone id where the occupant is located
     * @throws NullPointerException if kind is null
     * @throws IllegalArgumentException if zoneId is negative
     */
    public Occupant {
        Objects.requireNonNull(kind);
        if (zoneId < 0) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * The number of occupants of a given kind.
     *
     * @param kind the kind of the occupant
     * @return the number of occupants of the given kind
     */
    public static int occupantsCount(Kind kind) {
        return switch (kind) {
            case PAWN -> 5;
            case HUT -> 3;
        };
    }
}
