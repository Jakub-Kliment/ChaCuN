package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static ch.epfl.chacun.Area.*;
import static ch.epfl.chacun.Area.riverSystemFishCount;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MyAreaTest {
    @Test
    void hasMenhirWorksOnStarter() {
        var zoneForest1 = new Zone.Forest(12, Zone.Forest.Kind.WITH_MENHIR);
        var zoneForest2 = new Zone.Forest(13, Zone.Forest.Kind.PLAIN);

        var f0 = new HashSet<Zone.Forest>();
        f0.add(zoneForest1);
        f0.add(zoneForest2);
        Area<Zone.Forest> forestArea = new Area<>(f0, new ArrayList<>(), 0);

        assertTrue(hasMenhir(forestArea));
    }


    @Test
    void FishCountWorks(){
        var l0 = new Zone.Lake(1_8, 3, null);
        var l1 = new Zone.Lake(1_8, 2, null);
        var z2 = new Zone.River(1_1, 1, l0);
        var z3 = new Zone.River(1_2, 1, l0);

        var h0 = new HashSet<Zone.River>();
        h0.add(z2);
        h0.add(z3);

        var rA1 = new Area<Zone.River>(h0, new ArrayList<>(), 0);

        var h1 = new HashSet<Zone.Water>();
        h1.add(l0);
        h1.add(l1);
        h1.add(z2);
        h1.add(z3);

        var h2 = new HashSet<Zone.Water>();
        h2.add(l0);
        h2.add(z2);
        h2.add(z3);
        h2.add(l0);

        var rA2 = new Area<Zone.Water>(h1, new ArrayList<>(), 0);
        var rA3 = new Area<Zone.Water>(h2, new ArrayList<>(), 0);

        assertEquals(5, riverFishCount(rA1));
        assertEquals(7, riverSystemFishCount(rA2));
        assertEquals(5, riverSystemFishCount(rA3));
        assertEquals(2, lakeCount(rA2));
        assertEquals(1, lakeCount(rA3));
    }
}
