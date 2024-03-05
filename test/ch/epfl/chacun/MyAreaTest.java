package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MyAreaTest {
    @Test
    void constructorTestArea(){
        for (int j = -1; j>-100; j--){
            final int i = j;
            assertThrows(IllegalArgumentException.class, () -> new Area<>(new HashSet<>(), new ArrayList<>(), i));
        }
        Zone.Forest forest1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest3 = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);
        Set<Zone.Forest> forestSetTest = new HashSet<>(Set.of(forest1, forest2));
        Set<Zone.Forest> forestSet = Set.copyOf(forestSetTest);
        Set<Zone.Forest> forestSetVerif = new HashSet<>(Set.of(forest1, forest2, forest3));
        Area<Zone.Forest> areaSur = new Area<>(forestSet, new ArrayList<>(), 0);
        Area<Zone.Forest> areaTest = new Area<>(forestSetTest, new ArrayList<>(), 0);
        forestSetTest.add(forest3);
        assertEquals(forestSetVerif, forestSetTest);
        assertEquals(areaSur, areaTest);
    }

    @Test
    void menhirTest(){
        Zone.Forest forestMenhir = new Zone.Forest(3, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Forest forest1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
        HashSet<Zone.Forest> setTrue = new HashSet<>(Set.of(forest1,forest2,forestMenhir));
        HashSet<Zone.Forest> setFalse = new HashSet<>(Set.of(forest1,forest2));
        Area<Zone.Forest> areaTrue = new Area<>(setTrue, new ArrayList<>(), 0);
        Area<Zone.Forest> areaFalse = new Area<>(setFalse, new ArrayList<>(), 0);
        assertTrue(Area.hasMenhir(areaTrue));
        assertFalse(Area.hasMenhir(areaFalse));
    }

    @Test
    void MushroomTest(){
        Zone.Forest forestMushroom1 = new Zone.Forest(3, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forestMushroom2 = new Zone.Forest(4, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
        HashSet<Zone.Forest> setNull = new HashSet<>(Set.of(forest1,forest2));
        HashSet<Zone.Forest> set = new HashSet<>(Set.of(forest1,forest2, forestMushroom1, forestMushroom2));
        Area<Zone.Forest> areaNull = new Area<>(setNull, new ArrayList<>(), 0);
        Area<Zone.Forest> area = new Area<>(set, new ArrayList<>(), 0);
        assertEquals(2, Area.mushroomGroupCount(area));
        assertEquals(0, Area.mushroomGroupCount(areaNull));
    }

    @Test
    void isClosedTest(){
        Area<Zone.Forest> areaTrue = new Area<>(new HashSet<>(), new ArrayList<>(), 0);
        Area<Zone.Forest> areaFalse = new Area<>(new HashSet<>(), new ArrayList<>(), 3);
        assertTrue(areaTrue.isClosed());
        assertFalse(areaFalse.isClosed());
    }

    @Test
    void isOccupiedTest(){
        Area<Zone.Forest> areaTrue = new Area<>(new HashSet<>(), new ArrayList<PlayerColor>(List.of(PlayerColor.RED)), 0);
        Area<Zone.Forest> areaFalse = new Area<>(new HashSet<>(), new ArrayList<>(), 0);
        assertTrue(areaTrue.isOccupied());
        assertFalse(areaFalse.isOccupied());
    }

    @Test
    void MajorityOccupantTest(){
        Area<Zone.Forest> areaBase = new Area<>(new HashSet<>(), new ArrayList<PlayerColor>(List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.GREEN, PlayerColor.PURPLE, PlayerColor.BLUE)), 0);
        assertEquals(new HashSet<PlayerColor>(Set.of(PlayerColor.RED)), areaBase.majorityOccupants());
        Area<Zone.Forest> areaEqualAll = new Area<>(new HashSet<>(), new ArrayList<PlayerColor>(List.of(PlayerColor.RED, PlayerColor.GREEN, PlayerColor.PURPLE, PlayerColor.BLUE)), 0);
        assertEquals(new HashSet<PlayerColor>(Set.of(PlayerColor.RED, PlayerColor.GREEN, PlayerColor.PURPLE, PlayerColor.BLUE)), areaEqualAll.majorityOccupants());
        Area<Zone.Forest> areaEquals2 = new Area<>(new HashSet<>(), new ArrayList<PlayerColor>(List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.PURPLE, PlayerColor.PURPLE)), 0);
        assertEquals(new HashSet<PlayerColor>(Set.of(PlayerColor.RED, PlayerColor.PURPLE)), areaEquals2.majorityOccupants());
        Area<Zone.Forest> areaNothings = new Area<>(new HashSet<>(), new ArrayList<PlayerColor>(), 0);
        assertEquals(new HashSet<PlayerColor>(), areaNothings.majorityOccupants());
    }
}