package ch.epfl.chacun.oldTests;

import ch.epfl.chacun.Animal;
import ch.epfl.chacun.Area;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.Zone;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class MyAreaTest {

    Zone.Forest forestWithMenhir = new Zone.Forest(913, Zone.Forest.Kind.WITH_MENHIR);
    Zone.Forest forestWithoutMenhir = new Zone.Forest(551, Zone.Forest.Kind.PLAIN);
    Zone.Meadow meadowWithAnimals = new Zone.Meadow(549, List.of(new Animal(4022, Animal.Kind.MAMMOTH), new Animal(4021, Animal.Kind.AUROCHS)), null);
    Zone.River riverWithFish = new Zone.River(542, 10, new Zone.Lake(5, 20, null));
    Zone.Lake lakeWithFish = new Zone.Lake(514, 15, null);
    Zone.Forest forestWithMushrooms = new Zone.Forest(543, Zone.Forest.Kind.WITH_MUSHROOMS);

    @Test
    void hasMenhir() {
        Area<Zone.Forest> areaWithMenhir = new Area<>(Set.of(forestWithMenhir), List.of(), 0);
        Area<Zone.Forest> areaWithoutMenhir = new Area<>(Set.of(forestWithoutMenhir), List.of(), 0);

        assertTrue(Area.hasMenhir(areaWithMenhir));
        assertFalse(Area.hasMenhir(areaWithoutMenhir));
    }

    @Test
    void mushroomGroupCount() {
        Zone.Forest forestWithMushrooms1 = new Zone.Forest(7, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forestWithMushrooms2 = new Zone.Forest(8, Zone.Forest.Kind.WITH_MUSHROOMS);
        Area<Zone.Forest> forestArea = new Area<>(Set.of(forestWithMushrooms1, forestWithMushrooms2), List.of(), 0);

        assertEquals(2, Area.mushroomGroupCount(forestArea));
    }

    @Test
    void animals() {
        Set<Animal> cancelledAnimals = Set.of(new Animal(4022, Animal.Kind.MAMMOTH));
        Area<Zone.Meadow> meadowArea = new Area<>(Set.of(meadowWithAnimals), List.of(), 0);

        assertEquals(Set.of(new Animal(4021, Animal.Kind.AUROCHS)), Area.animals(meadowArea, cancelledAnimals));
    }

    @Test
    void riverFishCount() {
        Area<Zone.River> riverArea = new Area<>(Set.of(riverWithFish), List.of(), 0);

        assertEquals(30, Area.riverFishCount(riverArea));
    }

    @Test
    void riverSystemFishCount() {
        Area<Zone.Water> riverSystemArea = new Area<>(Set.of(riverWithFish, lakeWithFish), List.of(), 0);

        assertEquals(25, Area.riverSystemFishCount(riverSystemArea));
    }

    @Test
    void lakeCount() {
        Area<Zone.Water> riverSystemAreaWithLakes = new Area<>(Set.of(lakeWithFish, riverWithFish), List.of(), 0);

        assertEquals(1, Area.lakeCount(riverSystemAreaWithLakes));
    }

    @Test
    void isClosed() {
        Area<?> closedArea = new Area<>(Set.of(), List.of(), 0);
        Area<?> openArea = new Area<>(Set.of(), List.of(), 1);

        assertTrue(closedArea.isClosed());
        assertFalse(openArea.isClosed());
    }

    @Test
    void isOccupied() {
        Area<Zone.Forest> occupiedArea = new Area<>(Set.of(forestWithMenhir), List.of(PlayerColor.RED,PlayerColor.RED), 0);
        Area<Zone.Forest> unoccupiedArea = new Area<>(Set.of(), List.of(), 0);

        assertTrue(occupiedArea.isOccupied());
        assertFalse(unoccupiedArea.isOccupied());
    }
    @Test
    void majorityOccupants() {
        Area<Zone.Forest> areaWithMultipleOccupants = new Area<>(Set.of(), Arrays.asList(PlayerColor.BLUE, PlayerColor.BLUE, PlayerColor.RED), 0);
        assertEquals(Set.of(PlayerColor.BLUE), areaWithMultipleOccupants.majorityOccupants());
    }

    @Test
    void connectTo() {
        Area<Zone.Forest> area1 = new Area<>(Set.of(forestWithMenhir), List.of(PlayerColor.BLUE), 2);
        Area<Zone.Forest> area2 = new Area<>(Set.of(forestWithMushrooms), List.of(PlayerColor.RED), 3);
        Area<Zone.Forest> connectedArea = area1.connectTo(area2);

        assertEquals(3, connectedArea.openConnections());
        assertTrue(connectedArea.occupants().containsAll(Arrays.asList(PlayerColor.BLUE, PlayerColor.RED)));
    }

    @Test
    void withInitialOccupant() {
        Area<Zone> area = (new Area<>(Set.of(forestWithMenhir,riverWithFish), List.of(), 0));
        area.withInitialOccupant(PlayerColor.BLUE);
        assertTrue(area.withInitialOccupant(PlayerColor.BLUE).occupants().contains(PlayerColor.BLUE));
    }

    @Test
    void withoutOccupant() {
        Area<Zone> area = new Area<>(Set.of(forestWithMenhir,riverWithFish), Arrays.asList(PlayerColor.BLUE, PlayerColor.RED), 0);
        area.withoutOccupant(PlayerColor.RED);
        assertTrue(area.withoutOccupant(PlayerColor.RED).occupants().contains(PlayerColor.BLUE));
        assertFalse(area.withoutOccupant(PlayerColor.RED).occupants().contains(PlayerColor.RED));
    }

    @Test
    void withoutOccupants() {
        Area<Zone> area = new Area<>(Set.of(), Arrays.asList(PlayerColor.BLUE, PlayerColor.RED), 0).withoutOccupants();
        assertTrue(area.occupants().isEmpty());
    }

    @Test
    void tileIds() {
        Area<Zone.Forest> area = new Area<>(Set.of(forestWithMenhir, forestWithMushrooms), List.of(), 0);
        assertEquals(Set.of(54,91), area.tileIds());
    }

    @Test
    void zoneWithSpecialPower() {
        Zone.Forest forestWithoutSpecialPower = new Zone.Forest(7, Zone.Forest.Kind.WITH_MUSHROOMS);
        Area<Zone.Forest> area = new Area<>(Set.of(forestWithoutSpecialPower), List.of(), 0);
        assertNull(area.zoneWithSpecialPower(Zone.SpecialPower.LOGBOAT));

        Zone.Meadow meadowWithSpecialPower = new Zone.Meadow(7, List.of(), Zone.SpecialPower.RAFT);
        Area<Zone.Meadow> area2 = new Area<>(Set.of(meadowWithSpecialPower), List.of(), 0);
        assertNull(area2.zoneWithSpecialPower(Zone.SpecialPower.LOGBOAT));
    }
}