package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
    Zone.Meadow meadowWithSuperpower = new Zone.Meadow(560, List.of(new Animal(1, Animal.Kind.AUROCHS)), Zone.SpecialPower.HUNTING_TRAP);
    Zone.Forest forest = new Zone.Forest(561, Zone.Forest.Kind.WITH_MENHIR);
    Zone.Meadow meadowWithNoSuperpower = new Zone.Meadow(562, List.of(), null);
    Zone.River river = new Zone.River(563, 1, null);

    TileSide n = new TileSide.Meadow(meadowWithSuperpower);
    TileSide e = new TileSide.Forest(forest);
    TileSide s = new TileSide.Forest(forest);
    TileSide w = new TileSide.River(meadowWithNoSuperpower, river, meadowWithSuperpower);
    TileSide n1 = new TileSide.Meadow(meadowWithNoSuperpower);

    @Test
    void zonesWorksOnForest(){
        assertEquals(List.of(forest), e.zones());
    }

    @Test
    void zonesWorksOnMeadow(){
        assertEquals(List.of(meadowWithSuperpower), n.zones());
    }

    @Test
    void zonesWorksOnRiver(){
        assertEquals(List.of(meadowWithNoSuperpower, river, meadowWithSuperpower), w.zones());
    }

    @Test
    void isSameKindAsWorksOnForest(){
        assertTrue(e.isSameKindAs(s));
    }

    @Test
    void isSameKindAsWorksOnMeadow(){
        assertTrue(n.isSameKindAs(n1));
    }

    @Test
    void isSameKindAsWorksOnRiver(){
        assertTrue(w.isSameKindAs(w));
    }

}