package ch.epfl.chacun.gui;

import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.Occupant;
import javafx.scene.Node;
import javafx.scene.shape.SVGPath;

/**
 * A utility class that creates icons for the GUI.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public final class Icon {

    /**
     * Private constructor that prevents instantiation.
     */
    private Icon() {}

    /**
     * Creates a new icon for the given player and occupant kind.
     * Generates a new SVGPath node with the corresponding content and colors.
     *
     * @param color the color of the player
     * @param kind the kind of the occupant
     * @return the new icon of the occupant that belongs to the player
     */
    public static Node newFor(PlayerColor color, Occupant.Kind kind) {
        SVGPath svg = new SVGPath();
        if (kind == Occupant.Kind.PAWN)
            svg.setContent("M -10 10 H -4 L 0 2 L 6 10 H 12 L 5 0 L 12 -2 L 12 -4 L " +
                    "6 -6 L 6 -10 L 0 -10 L -2 -4 L -6 -2 L -8 -10 L -12 -10 L -8 6 Z");
        else
            svg.setContent("M -8 10 H 8 V 2 H 12 L 0 -10 L -12 2 H -8 Z");

        svg.setFill(ColorMap.fillColor(color));
        svg.setStroke(ColorMap.strokeColor(color));
        return svg;
    }
}

