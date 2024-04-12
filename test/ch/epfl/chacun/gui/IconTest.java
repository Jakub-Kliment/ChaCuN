package ch.epfl.chacun.gui;

import ch.epfl.chacun.Occupant;
import ch.epfl.chacun.PlayerColor;
import javafx.scene.shape.SVGPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IconTest {

    @Test
    void newForWorksCorrectly() {
        for (PlayerColor color : PlayerColor.ALL) {
            for (Occupant.Kind kind : Occupant.Kind.values()) {
                SVGPath actual = (SVGPath) Icon.newFor(color, kind);

                SVGPath expected = new SVGPath();
                if (kind == Occupant.Kind.PAWN)
                    expected.setContent("M -10 10 H -4 L 0 2 L 6 10 H 12 L 5 0 L 12 -2 L 12 -4 L " +
                            "6 -6 L 6 -10 L 0 -10 L -2 -4 L -6 -2 L -8 -10 L -12 -10 L -8 6 Z");
                else expected.setContent("M -8 10 H 8 V 2 H 12 L 0 -10 L -12 2 H -8 Z");
                expected.setFill(ColorMap.fillColor(color));
                expected.setStroke(ColorMap.strokeColor(color));

                assertEquals(expected.getContent(), actual.getContent());
            }
        }
    }
}
