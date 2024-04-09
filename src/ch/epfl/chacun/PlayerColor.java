package ch.epfl.chacun;

import java.util.List;

/**
 * Represents all player colors.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public enum PlayerColor {
    RED,
    BLUE,
    GREEN,
    YELLOW,
    PURPLE
    ;

    /**
     * Immutable list of all player colors in order of definition.
     */
    public static final List<PlayerColor> ALL = List.of(values());
}