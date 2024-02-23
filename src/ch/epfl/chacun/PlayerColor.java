package ch.epfl.chacun;

import java.util.List;

/**
 * Represents player colors.
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

    // The list of all player colors
    public static final List<PlayerColor> ALL = List.of(values());
}