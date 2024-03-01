package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MyTileSideTest {
    @Test
    void sameSide(){
        assertTrue(new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN)).isSameKindAs(new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN))));
        assertTrue(new TileSide.Meadow(new Zone.Meadow(0, new ArrayList<>(), null)).isSameKindAs(new TileSide.Meadow(new Zone.Meadow(1, new ArrayList<>(), null))));
        assertTrue(new TileSide.River(new Zone.Meadow(0, new ArrayList<>(), null), new Zone.River(1, 0, null), new Zone.Meadow(2, new ArrayList<>(), null)).isSameKindAs(new TileSide.River(new Zone.Meadow(3, new ArrayList<>(), null), new Zone.River(4, 0, null), new Zone.Meadow(5, new ArrayList<>(), null))));
        assertFalse(new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN)).isSameKindAs(new TileSide.Meadow(new Zone.Meadow(1, new ArrayList<>(), null))));
        assertFalse(new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN)).isSameKindAs(new TileSide.River(new Zone.Meadow(1, new ArrayList<>(), null), new Zone.River(2, 0, null), new Zone.Meadow(3, new ArrayList<>(), null))));
        assertFalse(new TileSide.Meadow(new Zone.Meadow(0, new ArrayList<>(), null)).isSameKindAs(new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.PLAIN))));
        assertFalse(new TileSide.Meadow(new Zone.Meadow(0, new ArrayList<>(), null)).isSameKindAs(new TileSide.River(new Zone.Meadow(1, new ArrayList<>(), null), new Zone.River(2, 0, null), new Zone.Meadow(3, new ArrayList<>(), null))));
        assertFalse(new TileSide.River(new Zone.Meadow(0, new ArrayList<>(), null), new Zone.River(1, 0, null), new Zone.Meadow(2, new ArrayList<>(), null)).isSameKindAs(new TileSide.Forest(new Zone.Forest(3, Zone.Forest.Kind.PLAIN))));
        assertFalse(new TileSide.River(new Zone.Meadow(0, new ArrayList<>(), null), new Zone.River(1, 0, null), new Zone.Meadow(2, new ArrayList<>(), null)).isSameKindAs(new TileSide.Meadow(new Zone.Meadow(0, new ArrayList<>(), null))));
    }

}