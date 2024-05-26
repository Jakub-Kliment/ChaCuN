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

    private final static String PAWN_SVG_PATH = "M -10 10 H -4 L 0 2 L 6 10 H 12 L 5 0 L " +
            "12 -2 L 12 -4 L 6 -6 L 6 -10 L 0 -10 L -2 -4 L -6 -2 L -8 -10 L -12 -10 L -8 6 Z";

    private final static String HUT_SVG_PATH = "M -8 10 H 8 V 2 H 12 L 0 -10 L -12 2 H -8 Z";
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
        SVGPath svgPath = new SVGPath();
        svgPath.setContent(kind == Occupant.Kind.PAWN ? PAWN_SVG_PATH : HUT_SVG_PATH);
        svgPath.setFill(ColorMap.fillColor(color));
        svgPath.setStroke(ColorMap.strokeColor(color));
        return svgPath;
    }
}

