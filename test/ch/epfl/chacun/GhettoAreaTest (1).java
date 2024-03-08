package ch.epfl.chacun.test3.mytests;
/*
 *	Author:      Rami El Mallah
 *	Date:
 */

import ch.epfl.chacun.Animal;
import ch.epfl.chacun.Area;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.Zone;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AreaTest {

    @Test
    void constructorSortsOccupantsList() {
        var forest1 = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        var forest2 = new Zone.Forest(11, Zone.Forest.Kind.WITH_MENHIR);
        var forest3 = new Zone.Forest(12, Zone.Forest.Kind.WITH_MUSHROOMS);
        var forestArea1 = new Area<>(Set.of(forest1, forest2, forest3), List.of(PlayerColor.YELLOW, PlayerColor.RED, PlayerColor.BLUE, PlayerColor.RED), 2);
        var forestArea2 = new Area<>(Set.of(forest1, forest3), List.of(PlayerColor.BLUE, PlayerColor.RED), 0);
        var forestArea3 = new Area<>(Set.of(forest1, forest2), List.of(PlayerColor.YELLOW, PlayerColor.RED, PlayerColor.BLUE, PlayerColor.PURPLE, PlayerColor.GREEN, PlayerColor.BLUE), 3);
        var forestArea4 = new Area<>(Set.of(forest1, forest3), List.of(), 0);
        assertEquals(List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.BLUE, PlayerColor.YELLOW), forestArea1.occupants());
        assertEquals(List.of(PlayerColor.RED, PlayerColor.BLUE), forestArea2.occupants());
        assertEquals(List.of(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.YELLOW, PlayerColor.PURPLE), forestArea3.occupants());
        assertEquals(List.of(), forestArea4.occupants());
        assertNotEquals(List.of(PlayerColor.YELLOW, PlayerColor.RED, PlayerColor.BLUE, PlayerColor.PURPLE, PlayerColor.GREEN, PlayerColor.BLUE), forestArea3.occupants());
    }

    @Test
    void constructorThrowsOnNegativeOpenConnections() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Area<Zone.Forest>(Set.of(), List.of(), -1);
        });
        assertDoesNotThrow(() -> {
            new Area<Zone.Forest>(Set.of(), List.of(), 0);
        });
        assertDoesNotThrow(() -> {
            new Area<Zone.Forest>(Set.of(), List.of(), 10);
        });
    }

    @Test
    void hasMenhirWorks() {
        var forest1 = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        var forest2 = new Zone.Forest(11, Zone.Forest.Kind.WITH_MENHIR);
        var forest3 = new Zone.Forest(12, Zone.Forest.Kind.WITH_MUSHROOMS);
        var forestArea1 = new Area<>(Set.of(forest1, forest2, forest3), List.of(PlayerColor.RED), 2);
        var forestArea2 = new Area<>(Set.of(forest1, forest3), List.of(PlayerColor.BLUE), 0);
        assertTrue(Area.hasMenhir(forestArea1));
        assertFalse(Area.hasMenhir(forestArea2));
    }

    @Test
    void mushroomCountWorks() {
        var forest1 = new Zone.Forest(10, Zone.Forest.Kind.PLAIN);
        var forest2 = new Zone.Forest(11, Zone.Forest.Kind.WITH_MENHIR);
        var forest3 = new Zone.Forest(12, Zone.Forest.Kind.WITH_MUSHROOMS);
        var forest4 = new Zone.Forest(13, Zone.Forest.Kind.WITH_MUSHROOMS);
        var forestArea1 = new Area<>(Set.of(forest1, forest2, forest3), List.of(PlayerColor.RED), 2);
        var forestArea2 = new Area<>(Set.of(forest1, forest2, forest3, forest4), List.of(), 3);
        var forestArea3 = new Area<>(Set.of(forest1, forest2), List.of(PlayerColor.BLUE), 0);
        assertEquals(1, Area.mushroomGroupCount(forestArea1));
        assertEquals(2, Area.mushroomGroupCount(forestArea2));
        assertEquals(0, Area.mushroomGroupCount(forestArea3));
    }

    @Test
    void animalsWorks() {
        var animal1 = new Animal(0, Animal.Kind.MAMMOTH);
        var animal2 = new Animal(1, Animal.Kind.AUROCHS);
        var animal3 = new Animal(2, Animal.Kind.TIGER);
        var animal4 = new Animal(3, Animal.Kind.MAMMOTH);
        var meadow1 = new Zone.Meadow(10, List.of(animal1, animal2), Zone.SpecialPower.SHAMAN);
        var meadow2 = new Zone.Meadow(11, List.of(animal3), null);
        var meadowArea = new Area<>(Set.of(meadow1, meadow2), List.of(PlayerColor.YELLOW), 1);
        assertEquals(Set.of(animal1, animal3), Area.animals(meadowArea, Set.of(animal2, animal4)));
    }

    @Test
    void riverFishCountWorks() {
        var lake1 = new Zone.Lake(8, 2, null);
        var lake2 = new Zone.Lake(9, 1, null);
        var river1 = new Zone.River(0, 3, lake1);
        var river2 = new Zone.River(1, 0, null);
        var river3 = new Zone.River(2, 1, lake2);
        var river4 = new Zone.River(3, 1, lake1);
        var riverArea1 = new Area<>(Set.of(river1, river2, river3), List.of(PlayerColor.PURPLE), 2);
        var riverArea2 = new Area<>(Set.of(river1, river2, river4), List.of(PlayerColor.PURPLE, PlayerColor.GREEN), 0);
        assertEquals(7, Area.riverFishCount(riverArea1));
        assertEquals(6, Area.riverFishCount(riverArea2));
    }

    @Test
    void riverSystemFishCountWorks() {
        var lake1 = new Zone.Lake(8, 2, null);
        var lake2 = new Zone.Lake(9, 1, null);
        var river1 = new Zone.River(0, 3, lake1);
        var river2 = new Zone.River(1, 0, null);
        var river3 = new Zone.River(2, 1, lake2);
        var river4 = new Zone.River(3, 1, lake1);
        var riverSystem = new Area<Zone.Water>(Set.of(lake1, lake2, river1, river2, river3, river4), List.of(), 0);
        assertEquals(8, Area.riverSystemFishCount(riverSystem));
    }

    @Test
    void lakeCountWorks() {
        var lake1 = new Zone.Lake(8, 2, null);
        var lake2 = new Zone.Lake(9, 1, null);
        var lake3 = new Zone.Lake(10, 2, null);
        var river1 = new Zone.River(0, 3, lake1);
        var river2 = new Zone.River(1, 0, null);
        var river3 = new Zone.River(2, 1, lake2);
        var river4 = new Zone.River(3, 1, lake3);
        var riverSystem1 = new Area<Zone.Water>(Set.of(lake1, lake2, lake3, river1, river2, river3, river4), List.of(), 0);
        var riverSystem2 = new Area<Zone.Water>(Set.of(river1, river2, river3, river4), List.of(), 0);
        assertEquals(3, Area.lakeCount(riverSystem1));
        assertEquals(0, Area.lakeCount(riverSystem2));
    }

    @Test
    void isClosedWorks() {
        var forest1 = new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR);
        var forest2 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        var forest3 = new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS);
        var areaClosed = new Area<>(Set.of(forest1, forest2, forest3), List.of(PlayerColor.PURPLE, PlayerColor.GREEN, PlayerColor.GREEN), 0);
        var meadow1 = new Zone.Meadow(4, List.of(new Animal(10, Animal.Kind.MAMMOTH)), null);
        var meadow2 = new Zone.Meadow(5, List.of(), Zone.SpecialPower.HUNTING_TRAP);
        var meadow3 = new Zone.Meadow(4, List.of(new Animal(11, Animal.Kind.AUROCHS), new Animal(12, Animal.Kind.DEER)), null);
        var areaOpened = new Area<>(Set.of(meadow1, meadow2, meadow3), List.of(), 1);
        assertTrue(areaClosed.isClosed());
        assertFalse(areaOpened.isClosed());
    }

    @Test
    void isOccupiedWorks() {
        var forest1 = new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR);
        var forest2 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        var forest3 = new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS);
        var areaOccupied = new Area<>(Set.of(forest1, forest2, forest3), List.of(PlayerColor.PURPLE), 0);
        var meadow1 = new Zone.Meadow(4, List.of(new Animal(10, Animal.Kind.MAMMOTH)), null);
        var meadow2 = new Zone.Meadow(5, List.of(), Zone.SpecialPower.HUNTING_TRAP);
        var meadow3 = new Zone.Meadow(4, List.of(new Animal(11, Animal.Kind.AUROCHS), new Animal(12, Animal.Kind.DEER)), null);
        var areaNotOccupied = new Area<>(Set.of(meadow1, meadow2, meadow3), List.of(), 1);
        assertTrue(areaOccupied.isOccupied());
        assertFalse(areaNotOccupied.isOccupied());
    }

    @Test
    void majorityOccupantsWorks() {
        var forest1 = new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR);
        var forest2 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        var forest3 = new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS);
        var area1 = new Area<>(Set.of(forest1, forest2, forest3), List.of(PlayerColor.PURPLE, PlayerColor.PURPLE, PlayerColor.GREEN), 0);
        var area2 = new Area<>(Set.of(forest1, forest2, forest3), List.of(PlayerColor.YELLOW, PlayerColor.BLUE, PlayerColor.RED), 1);
        var area3 = new Area<>(Set.of(forest1, forest2, forest3), List.of(PlayerColor.YELLOW, PlayerColor.YELLOW, PlayerColor.BLUE, PlayerColor.BLUE, PlayerColor.GREEN), 1);
        var meadow1 = new Zone.Meadow(4, List.of(new Animal(10, Animal.Kind.MAMMOTH)), null);
        var meadow2 = new Zone.Meadow(5, List.of(), Zone.SpecialPower.HUNTING_TRAP);
        var meadow3 = new Zone.Meadow(4, List.of(new Animal(11, Animal.Kind.AUROCHS), new Animal(12, Animal.Kind.DEER)), null);
        var area4 = new Area<>(Set.of(meadow1, meadow2, meadow3), List.of(), 1);
        assertEquals(Set.of(PlayerColor.PURPLE), area1.majorityOccupants());
        assertEquals(Set.of(PlayerColor.YELLOW, PlayerColor.RED, PlayerColor.BLUE), area2.majorityOccupants());
        assertEquals(Set.of(PlayerColor.YELLOW, PlayerColor.BLUE), area3.majorityOccupants());
        assertEquals(Set.of(), area4.majorityOccupants());
    }

    @Test
    void connectToWorks() {
        var forest1 = new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR);
        var forest2 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        var forest3 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
        var forest4 = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);
        var forest5 = new Zone.Forest(4, Zone.Forest.Kind.PLAIN);
        var forest6 = new Zone.Forest(5, Zone.Forest.Kind.WITH_MUSHROOMS);
        var forest7 = new Zone.Forest(6, Zone.Forest.Kind.WITH_MENHIR);
        var forest8 = new Zone.Forest(7, Zone.Forest.Kind.PLAIN);
        var area1 = new Area<>(Set.of(forest1, forest2, forest3, forest4, forest5, forest6, forest7, forest8), List.of(PlayerColor.PURPLE, PlayerColor.RED), 4);
        var forest9 = new Zone.Forest(8, Zone.Forest.Kind.PLAIN);
        var area2 = new Area<>(Set.of(forest9), List.of(PlayerColor.PURPLE), 4);
        var resultArea1 = area1.connectTo(area2);
        assertEquals(new Area<>(Set.of(forest1, forest2, forest3, forest4, forest5, forest6, forest7, forest8, forest9), List.of(PlayerColor.PURPLE, PlayerColor.RED, PlayerColor.PURPLE), 6), resultArea1);
        var resultArea2 = resultArea1.connectTo(resultArea1);
        assertEquals(new Area<>(Set.of(forest1, forest2, forest3, forest4, forest5, forest6, forest7, forest8, forest9), List.of(PlayerColor.PURPLE, PlayerColor.RED, PlayerColor.PURPLE), 4), resultArea2);
        var resultArea3 = resultArea2.connectTo(resultArea2);
        assertEquals(new Area<>(Set.of(forest1, forest2, forest3, forest4, forest5, forest6, forest7, forest8, forest9), List.of(PlayerColor.PURPLE, PlayerColor.RED, PlayerColor.PURPLE), 2), resultArea3);
        var resultArea4 = resultArea3.connectTo(resultArea3);
        assertEquals(new Area<>(Set.of(forest1, forest2, forest3, forest4, forest5, forest6, forest7, forest8, forest9), List.of(PlayerColor.PURPLE, PlayerColor.RED, PlayerColor.PURPLE), 0), resultArea4);
    }

    @Test
    void withInitialOccupantWorks() {
        var forest1 = new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR);
        var forest2 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        var forest3 = new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS);
        var area1 = new Area<>(Set.of(forest1, forest2, forest3), List.of(), 4);
        var area2 = new Area<>(Set.of(forest1, forest2, forest3), List.of(PlayerColor.YELLOW), 1);
        assertEquals(new Area<>(Set.of(forest1, forest2, forest3), List.of(PlayerColor.GREEN), 4), area1.withInitialOccupant(PlayerColor.GREEN));
        assertNotEquals(new Area<>(Set.of(forest1, forest2, forest3), List.of(PlayerColor.RED), 4), area1.withInitialOccupant(PlayerColor.GREEN));
        assertThrows(IllegalArgumentException.class, () -> {
            area2.withInitialOccupant(PlayerColor.PURPLE);
        });
    }

    @Test
    void withoutOccupantWorks() {
        var forest1 = new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR);
        var forest2 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        var forest3 = new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS);
        var area1 = new Area<>(Set.of(forest1, forest2, forest3), List.of(), 4);
        var area2 = new Area<>(Set.of(forest1, forest2, forest3), List.of(PlayerColor.YELLOW, PlayerColor.RED, PlayerColor.RED), 1);
        assertEquals(new Area<>(Set.of(forest1, forest2, forest3), List.of(PlayerColor.YELLOW, PlayerColor.RED), 1), area2.withoutOccupant(PlayerColor.RED));
        assertThrows(IllegalArgumentException.class, () -> {
            area2.withoutOccupant(PlayerColor.PURPLE);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            area1.withoutOccupant(PlayerColor.GREEN);
        });
    }

    @Test
    void withoutOccupantsWorks() {
        var forest1 = new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR);
        var forest2 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        var forest3 = new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS);
        var area1 = new Area<>(Set.of(forest1, forest2, forest3), List.of(), 4);
        var area2 = new Area<>(Set.of(forest1, forest2, forest3), List.of(PlayerColor.YELLOW, PlayerColor.RED, PlayerColor.RED), 1);
        assertEquals(area1, area1.withoutOccupants());
        assertEquals(new Area<>(Set.of(forest1, forest2, forest3), List.of(), 1), area2.withoutOccupants());
    }

    @Test
    void tileIdsWorks() {
        var forest1 = new Zone.Forest(920, Zone.Forest.Kind.WITH_MENHIR);
        var forest2 = new Zone.Forest(12, Zone.Forest.Kind.PLAIN);
        var forest3 = new Zone.Forest(243, Zone.Forest.Kind.WITH_MUSHROOMS);
        var forest4 = new Zone.Forest(13, Zone.Forest.Kind.PLAIN);
        var area1 = new Area<>(Set.of(forest1, forest2, forest3, forest4), List.of(PlayerColor.PURPLE), 4);
        assertEquals(Set.of(1,24,92), area1.tileIds());
    }

    @Test
    void zoneWithSpecialPowerWorks() {
        var meadow1 = new Zone.Meadow(4, List.of(new Animal(10, Animal.Kind.MAMMOTH)), null);
        var meadow2 = new Zone.Meadow(5, List.of(), Zone.SpecialPower.HUNTING_TRAP);
        var meadow3 = new Zone.Meadow(4, List.of(new Animal(11, Animal.Kind.AUROCHS), new Animal(12, Animal.Kind.DEER)), null);
        var area1 = new Area<>(Set.of(meadow1, meadow2, meadow3), List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.YELLOW), 2);
        var meadow4 = new Zone.Meadow(4, List.of(new Animal(10, Animal.Kind.TIGER)), Zone.SpecialPower.WILD_FIRE);
        var area2 = new Area<>(Set.of(meadow1, meadow2, meadow3, meadow4), List.of(PlayerColor.YELLOW), 0);
        assertNull(area1.zoneWithSpecialPower(Zone.SpecialPower.LOGBOAT));
        assertEquals(meadow2, area1.zoneWithSpecialPower(Zone.SpecialPower.HUNTING_TRAP));
        assertEquals(meadow4, area2.zoneWithSpecialPower(Zone.SpecialPower.WILD_FIRE));
        assertEquals(meadow2, area2.zoneWithSpecialPower(Zone.SpecialPower.HUNTING_TRAP));
    }
}
