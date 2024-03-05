package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyAreaTest {

    @Test
    void areaConstructorWorksAsIntended() {
        assertThrows(IllegalArgumentException.class, () ->
                new Area<>(new HashSet<>(), new ArrayList<>(), -1));

        ArrayList<PlayerColor> shuffledColors = new ArrayList<>(List.of(PlayerColor.YELLOW,
                PlayerColor.RED, PlayerColor.GREEN, PlayerColor.PURPLE, PlayerColor.BLUE));
        Area<Zone> area = new Area<>(new HashSet<>(), shuffledColors, 0);
        shuffledColors.clear();
        assertEquals(PlayerColor.ALL, area.occupants());
    }

    @Test
    void testImmutableConstructor() {
        // Create initial data for the area
        Set<Zone.Forest> initialZones = new HashSet<>();
        initialZones.add(new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR));
        List<PlayerColor> initialOccupants = new ArrayList<>(List.of(PlayerColor.RED)); // Create mutable list
        int initialOpenConnections = 1;

        // Create an area instance using the constructor
        Area<Zone.Forest> area = new Area<>(initialZones, initialOccupants, initialOpenConnections);

        // Try to modify the properties of the area
        initialZones.clear();
        initialOccupants.clear();
        initialOpenConnections = 0;

        // Assert that the properties of the area instance remain unchanged
        assertNotEquals(initialZones, area.zones());
        assertNotEquals(initialOccupants, area.occupants());
        assertNotEquals(initialOpenConnections, area.openConnections());
    }

    @Test
    void hasMenhirWorksForForestsWithMenhir() {
        Zone.Forest forest = new Zone.Forest(10, Zone.Forest.Kind.WITH_MENHIR);
        Area<Zone.Forest> area1 = new Area<>(new HashSet<>(Set.of(forest)), new ArrayList<>(), 2);
        assertTrue(Area.hasMenhir(area1));
    }

    @Test
    void hasMenhirWorksForForestsWithoutMenhir() {
        Zone.Forest forest = new Zone.Forest(10, Zone.Forest.Kind.WITH_MUSHROOMS);
        Area<Zone.Forest> area1 = new Area<>(new HashSet<>(Set.of(forest)), new ArrayList<>(), 2);
        assertFalse(Area.hasMenhir(area1));
    }


    @Test
    void testHasMenhirWithMenhir() {
        // Create a set of forest zones with menhir
        Set<Zone.Forest> zones = new HashSet<>();
        zones.add(new Zone.Forest(4, Zone.Forest.Kind.WITH_MENHIR));
        Area<Zone.Forest> forestArea = new Area<>(zones, List.of(), 0);

        // Assert that hasMenhir returns true for an area with menhir
        assertTrue(Area.hasMenhir(forestArea));
    }

    @Test
    void testHasMenhirWithoutMenhir() {
        // Create a set of forest zones without menhir
        Set<Zone.Forest> zones = new HashSet<>();
        zones.add(new Zone.Forest(3, Zone.Forest.Kind.PLAIN));
        Area<Zone.Forest> forestArea = new Area<>(zones, List.of(), 0);

        // Assert that hasMenhir returns false for an area without menhir
        assertFalse(Area.hasMenhir(forestArea));
    }

    @Test
    void testHasMenhirEmptyArea() {
        // Create an empty forest area
        Area<Zone.Forest> forestArea = new Area<>(Set.of(), List.of(), 0);

        // Assert that hasMenhir returns false for an empty area
        assertFalse(Area.hasMenhir(forestArea));
    }

    @Test
    void mushroomGroupCountCountsTheRightAmountOfMushrooms() {
        Zone.Forest forest1 = new Zone.Forest(10, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest2 = new Zone.Forest(11, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest3 = new Zone.Forest(12, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest4 = new Zone.Forest(13, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Forest forest5 = new Zone.Forest(14, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest6 = new Zone.Forest(15, Zone.Forest.Kind.WITH_MUSHROOMS);

        Area<Zone.Forest> area1 = new Area<>(new HashSet<>(Set.of(forest1, forest2, forest3, forest4, forest5, forest6)), new ArrayList<>(), 2);
        assertEquals(5, Area.mushroomGroupCount(area1));
    }

    @Test
    void testMushroomGroupCountWithMushrooms() {
        // Create a set of forest zones with mushrooms
        Set<Zone.Forest> zones = new HashSet<>();
        zones.add(new Zone.Forest(1, Zone.Forest.Kind.WITH_MUSHROOMS));
        zones.add(new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS));
        zones.add(new Zone.Forest(3, Zone.Forest.Kind.PLAIN)); // A forest without mushrooms
        Area<Zone.Forest> forestArea = new Area<>(zones, List.of(), 0);

        // Assert that mushroomGroupCount returns the correct count for an area with mushrooms
        assertEquals(2, Area.mushroomGroupCount(forestArea));
    }

    @Test
    void testMushroomGroupCountWithoutMushrooms() {
        // Create a set of forest zones without mushrooms
        Set<Zone.Forest> zones = new HashSet<>();
        zones.add(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR));
        zones.add(new Zone.Forest(2, Zone.Forest.Kind.PLAIN));
        Area<Zone.Forest> forestArea = new Area<>(zones, List.of(), 0);

        // Assert that mushroomGroupCount returns 0 for an area without mushrooms
        assertEquals(0, Area.mushroomGroupCount(forestArea));
    }

    @Test
    void testMushroomGroupCountEmptyArea() {
        // Create an empty forest area
        Area<Zone.Forest> forestArea = new Area<>(Set.of(), List.of(), 0);

        // Assert that mushroomGroupCount returns 0 for an empty area
        assertEquals(0, Area.mushroomGroupCount(forestArea));
    }

    @Test
    void testAnimalsWithCancelledAnimals() {
        // Create a meadow area with animals
        Set<Zone.Meadow> zones = new HashSet<>();
        zones.add(new Zone.Meadow(1, List.of(new Animal(101, Animal.Kind.MAMMOTH), new Animal(102, Animal.Kind.AUROCHS)), null));
        zones.add(new Zone.Meadow(2, List.of(new Animal(103, Animal.Kind.DEER), new Animal(104, Animal.Kind.TIGER)), null));
        Area<Zone.Meadow> meadowArea = new Area<>(zones, List.of(), 0);

        // Create a set of cancelled animals
        Set<Animal> cancelledAnimals = new HashSet<>();
        cancelledAnimals.add(new Animal(102, Animal.Kind.AUROCHS));
        cancelledAnimals.add(new Animal(104, Animal.Kind.TIGER));

        // Call the animals method
        Set<Animal> result = Area.animals(meadowArea, cancelledAnimals);

        // Assert that the result contains only non-cancelled animals
        assertEquals(2, result.size());
        assertTrue(result.contains(new Animal(101, Animal.Kind.MAMMOTH)));
        assertTrue(result.contains(new Animal(103, Animal.Kind.DEER)));
    }

    @Test
    void testAnimalsWithNoCancelledAnimals() {
        // Create a meadow area with animals
        Set<Zone.Meadow> zones = new HashSet<>();
        zones.add(new Zone.Meadow(1, List.of(new Animal(101, Animal.Kind.MAMMOTH), new Animal(102, Animal.Kind.AUROCHS)), null));
        zones.add(new Zone.Meadow(2, List.of(new Animal(103, Animal.Kind.DEER), new Animal(104, Animal.Kind.TIGER)), null));
        Area<Zone.Meadow> meadowArea = new Area<>(zones, List.of(), 0);

        // Create an empty set of cancelled animals
        Set<Animal> cancelledAnimals = new HashSet<>();

        // Call the animals method
        Set<Animal> result = Area.animals(meadowArea, cancelledAnimals);

        // Assert that the result contains all animals
        assertEquals(4, result.size());
        assertTrue(result.contains(new Animal(101, Animal.Kind.MAMMOTH)));
        assertTrue(result.contains(new Animal(102, Animal.Kind.AUROCHS)));
        assertTrue(result.contains(new Animal(103, Animal.Kind.DEER)));
        assertTrue(result.contains(new Animal(104, Animal.Kind.TIGER)));
    }

    /*
    @Test
    void testAnimalsWithNullMeadow() {
        // Create a null meadow area
        Area<Zone.Meadow> meadowArea = null;

        // Create a set of cancelled animals
        Set<Animal> cancelledAnimals = new HashSet<>();
        cancelledAnimals.add(new Animal(102, Animal.Kind.AUROCHS));
        cancelledAnimals.add(new Animal(104, Animal.Kind.TIGER));

        // Call the animals method
        Set<Animal> result = Area.animals(meadowArea, cancelledAnimals);

        // Assert that the result is empty
        assertTrue(result.isEmpty());
    }

    @Test
    void testAnimalsWithNullCancelledAnimals() {
        // Create a meadow area with animals
        Set<Zone.Meadow> zones = new HashSet<>();
        zones.add(new Zone.Meadow(1, List.of(new Animal(101, Animal.Kind.MAMMOTH), new Animal(102, Animal.Kind.AUROCHS)), null));
        zones.add(new Zone.Meadow(2, List.of(new Animal(103, Animal.Kind.DEER), new Animal(104, Animal.Kind.TIGER)), null));
        Area<Zone.Meadow> meadowArea = new Area<>(zones, List.of(), 0);

        // Create a null set of cancelled animals
        Set<Animal> cancelledAnimals = null;

        // Call the animals method
        Set<Animal> result = Area.animals(meadowArea, cancelledAnimals);

        // Assert that the result contains all animals
        assertEquals(4, result.size());
        assertTrue(result.contains(new Animal(101, Animal.Kind.MAMMOTH)));
        assertTrue(result.contains(new Animal(102, Animal.Kind.AUROCHS)));
        assertTrue(result.contains(new Animal(103, Animal.Kind.DEER)));
        assertTrue(result.contains(new Animal(104, Animal.Kind.TIGER)));
    }
     */


    @Test
    void testRiverFishCountWithNoRiver() {
        // Create an empty river area
        Area<Zone.River> riverArea = new Area<>(Set.of(), List.of(), 0);

        // Call the riverFishCount method
        int result = Area.riverFishCount(riverArea);

        // Assert that the result is 0 when there's no river
        assertEquals(0, result);
    }

    @Test
    void testRiverFishCountWithRiverWithoutLake() {
        // Create a river area without any lake
        Set<Zone.River> rivers = new HashSet<>();
        rivers.add(new Zone.River(1, 10, null));
        rivers.add(new Zone.River(2, 5, null));
        Area<Zone.River> riverArea = new Area<>(rivers, List.of(), 0);

        // Call the riverFishCount method
        int result = Area.riverFishCount(riverArea);

        // Assert that the result is the sum of fish count in the river zones
        assertEquals(15, result);
    }

    @Test
    void testRiverFishCountWithRiverAndLakeAtBothEnds() {
        // Create a river area connected to a lake at both ends
        Set<Zone.River> rivers = new HashSet<>();
        Zone.Lake lake = new Zone.Lake(1, 20, null);
        rivers.add(new Zone.River(1, 10, lake));
        rivers.add(new Zone.River(2, 5, lake));
        Area<Zone.River> riverArea = new Area<>(rivers, List.of(), 0);

        // Call the riverFishCount method
        int result = Area.riverFishCount(riverArea);

        // Assert that the result includes fish count from river and lake, counting the lake only once
        assertEquals(35, result);
    }

    @Test
    void testRiverFishCountWithRiverAndDifferentLakesAtBothEnds() {
        // Create a river area connected to different lakes at both ends
        Set<Zone.River> rivers = new HashSet<>();
        Zone.Lake lake1 = new Zone.Lake(1, 20, null);
        Zone.Lake lake2 = new Zone.Lake(2, 30, null);
        rivers.add(new Zone.River(1, 10, lake1));
        rivers.add(new Zone.River(2, 5, lake2));
        Area<Zone.River> riverArea = new Area<>(rivers, List.of(), 0);

        // Call the riverFishCount method
        int result = Area.riverFishCount(riverArea);

        // Assert that the result includes fish count from river and both lakes
        assertEquals(65, result);
    }

    @Test
    void testRiverFishCountWithRiverAndSingleLakeAtOneEnd() {
        // Create a river area connected to a single lake at one end
        Set<Zone.River> rivers = new HashSet<>();
        Zone.Lake lake = new Zone.Lake(1, 20, null);
        rivers.add(new Zone.River(1, 10, lake));
        rivers.add(new Zone.River(2, 5, null));
        Area<Zone.River> riverArea = new Area<>(rivers, List.of(), 0);

        // Call the riverFishCount method
        int result = Area.riverFishCount(riverArea);

        // Assert that the result includes fish count from river and lake
        assertEquals(35, result);
    }

    @Test
    void testRiverFishCountWithRiverAndNoLake() {
        // Create a river area with no lake connected
        Set<Zone.River> rivers = new HashSet<>();
        rivers.add(new Zone.River(1, 10, null));
        rivers.add(new Zone.River(2, 5, null));
        Area<Zone.River> riverArea = new Area<>(rivers, List.of(), 0);

        // Call the riverFishCount method
        int result = Area.riverFishCount(riverArea);

        // Assert that the result is the sum of fish count in the river zones
        assertEquals(15, result);
    }

    @Test
    void testRiverSystemFishCountWithEmptyRiverSystem() {
        // Create an empty river system
        Area<Zone.Water> emptyRiverSystem = new Area<>(Set.of(), List.of(), 0);

        // Call the riverSystemFishCount method
        int result = Area.riverSystemFishCount(emptyRiverSystem);

        // Assert that the result is 0 for an empty river system
        assertEquals(0, result);
    }

    @Test
    void testRiverSystemFishCountWithNoFishInWaterZones() {
        // Create a river system with water zones but no fish
        Set<Zone.Water> waterZones = new HashSet<>();
        waterZones.add(new Zone.Lake(1, 0, null));
        waterZones.add(new Zone.River(2, 0, null));
        Area<Zone.Water> riverSystem = new Area<>(waterZones, List.of(), 0);

        // Call the riverSystemFishCount method
        int result = Area.riverSystemFishCount(riverSystem);

        // Assert that the result is 0 when there are no fish in water zones
        assertEquals(0, result);
    }

    @Test
    void testRiverSystemFishCountWithFishInWaterZones() {
        // Create a river system with water zones containing fish
        Set<Zone.Water> waterZones = new HashSet<>();
        waterZones.add(new Zone.Lake(1, 20, null));
        waterZones.add(new Zone.River(2, 15, null));
        waterZones.add(new Zone.Lake(3, 10, null));
        Area<Zone.Water> riverSystem = new Area<>(waterZones, List.of(), 0);

        // Call the riverSystemFishCount method
        int result = Area.riverSystemFishCount(riverSystem);

        // Calculate the expected total fish count
        int expected = 20 + 15 + 10;

        // Assert that the result matches the expected total fish count
        assertEquals(expected, result);
    }

    // Does not pass, because of a lake inside a river
    // Ask an assistant !!!!!!!!!!!
    @Test
    void testRiverSystemFishCountWithFishInRiverAndLake() {
        // Create a river system with water zones containing fish
        Set<Zone.Water> waterZones = new HashSet<>();
        waterZones.add(new Zone.Lake(1, 20, null));
        waterZones.add(new Zone.River(2, 15, null));
        Area<Zone.Water> riverSystem = new Area<>(waterZones, List.of(), 0);

        // Call the riverSystemFishCount method
        int result = Area.riverSystemFishCount(riverSystem);

        // Calculate the expected total fish count
        int expected = 20 + 15;

        // Assert that the result matches the expected total fish count
        assertEquals(expected, result);
    }


    @Test
    void testLakeCountWithNoLakes() {
        // Create a river system with no lakes
        Area<Zone.Water> riverSystem = new Area<>(Set.of(new Zone.River(1, 0, null)), List.of(), 0);

        // Call the lakeCount method
        int result = Area.lakeCount(riverSystem);

        // Assert that the result is 0 when there are no lakes
        assertEquals(0, result);
    }

    @Test
    void testLakeCountWithSingleLake() {
        // Create a river system with a single lake
        Area<Zone.Water> riverSystem = new Area<>(Set.of(new Zone.Lake(1, 0, null)), List.of(), 0);

        // Call the lakeCount method
        int result = Area.lakeCount(riverSystem);

        // Assert that the result is 1 when there is a single lake
        assertEquals(1, result);
    }

    @Test
    void testLakeCountWithMultipleLakes() {
        // Create a river system with multiple lakes
        Set<Zone.Water> waterZones = new HashSet<>();
        waterZones.add(new Zone.Lake(1, 0, null));
        waterZones.add(new Zone.Lake(2, 0, null));
        waterZones.add(new Zone.River(3, 0, null)); // Add a river for diversity
        waterZones.add(new Zone.Lake(4, 0, null));
        Area<Zone.Water> riverSystem = new Area<>(waterZones, List.of(), 0);

        // Call the lakeCount method
        int result = Area.lakeCount(riverSystem);

        // Assert that the result is the number of lakes in the river system
        assertEquals(3, result);
    }

    @Test
    void testIsClosedWhenAreaIsClosed() {
        // Create a closed area
        Area<Zone.Water> closedArea = new Area<>(Set.of(), List.of(), 0);

        // Call the isClosed method
        boolean result = closedArea.isClosed();

        // Assert that the result is true when the area is closed
        assertTrue(result);
    }

    @Test
    void testIsClosedWhenAreaIsNotClosed() {
        // Create an area with open connections
        Area<Zone.Water> openArea = new Area<>(Set.of(), List.of(), 1);

        // Call the isClosed method
        boolean result = openArea.isClosed();

        // Assert that the result is false when the area is not closed
        assertFalse(result);
    }

    @Test
    void testIsOccupiedWhenAreaIsOccupied() {
        // Create an area with occupants
        List<PlayerColor> occupants = List.of(PlayerColor.RED, PlayerColor.BLUE);
        Area<Zone.Water> occupiedArea = new Area<>(Set.of(), occupants, 0);

        // Call the isOccupied method
        boolean result = occupiedArea.isOccupied();

        // Assert that the result is true when the area is occupied
        assertTrue(result);
    }

    @Test
    void testIsOccupiedWhenAreaIsNotOccupied() {
        // Create an empty area
        Area<Zone.Water> emptyArea = new Area<>(Set.of(), List.of(), 0);

        // Call the isOccupied method
        boolean result = emptyArea.isOccupied();

        // Assert that the result is false when the area is not occupied
        assertFalse(result);
    }


    @Test
    void testMajorityOccupantsWithUnoccupiedArea() {
        // Create an unoccupied area
        Area<Zone.Water> unoccupiedArea = new Area<>(Set.of(), List.of(), 0);

        // Call the majorityOccupants method
        Set<PlayerColor> result = unoccupiedArea.majorityOccupants();

        // Assert that the result is an empty set for an unoccupied area
        assertEquals(Set.of(), result);
    }

    @Test
    void testMajorityOccupantsWithSingleMajorityOccupant() {
        // Create an area with a single majority occupant (RED)
        List<PlayerColor> occupants = List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.BLUE);
        Area<Zone.Water> area = new Area<>(Set.of(), occupants, 0);

        // Call the majorityOccupants method
        Set<PlayerColor> result = area.majorityOccupants();

        // Assert that the result contains only the majority occupant (RED)
        assertEquals(Set.of(PlayerColor.RED), result);
    }

    @Test
    void testMajorityOccupantsWithMultipleMajorityOccupants() {
        // Create an area with multiple majority occupants (RED and BLUE)
        List<PlayerColor> occupants = List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.BLUE, PlayerColor.BLUE, PlayerColor.GREEN);
        Area<Zone.Water> area = new Area<>(Set.of(), occupants, 0);

        // Call the majorityOccupants method
        Set<PlayerColor> result = area.majorityOccupants();

        // Assert that the result contains both majority occupants (RED and BLUE)
        assertEquals(Set.of(PlayerColor.RED, PlayerColor.BLUE), result);
    }

    @Test
    void testConnectToWithNonOverlappingAreas() {
        // Create two non-overlapping areas
        Set<Zone> zones1 = new HashSet<>(Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)));
        Set<Zone> zones2 = new HashSet<>(Set.of(new Zone.Meadow(2, List.of(), null)));
        List<PlayerColor> occupants1 = List.of(PlayerColor.RED, PlayerColor.GREEN);
        List<PlayerColor> occupants2 = List.of(PlayerColor.BLUE);
        int openConnections1 = 2;
        int openConnections2 = 1;
        Area<Zone> area1 = new Area<>(zones1, occupants1, openConnections1);
        Area<Zone> area2 = new Area<>(zones2, occupants2, openConnections2);

        // Connect the areas
        Area<Zone> connectedArea = area1.connectTo(area2);

        // Check the combined zones
        assertEquals(2, connectedArea.zones().size());
        assertTrue(connectedArea.zones().containsAll(zones1));
        assertTrue(connectedArea.zones().containsAll(zones2));

        // Check the combined occupants
        assertEquals(3, connectedArea.occupants().size());
        assertTrue(connectedArea.occupants().containsAll(occupants1));
        assertTrue(connectedArea.occupants().containsAll(occupants2));

        // Check the number of open connections
        assertEquals(1, connectedArea.openConnections());
    }

    @Test
    void testConnectToWithOverlappingAreas() {
        // Create two overlapping areas
        Set<Zone> commonZones = new HashSet<>(Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)));
        Set<Zone> zones1 = new HashSet<>(commonZones);
        Set<Zone> zones2 = new HashSet<>(commonZones);
        List<PlayerColor> occupants1 = List.of(PlayerColor.RED, PlayerColor.GREEN);
        List<PlayerColor> occupants2 = List.of(PlayerColor.BLUE);
        int openConnections1 = 2;
        int openConnections2 = 1;
        Area<Zone> area1 = new Area<>(zones1, occupants1, openConnections1);
        Area<Zone> area2 = new Area<>(zones2, occupants2, openConnections2);

        // Connect the areas
        Area<Zone> connectedArea = area1.connectTo(area2);

        // Check the combined zones
        assertEquals(1, connectedArea.zones().size());
        assertTrue(connectedArea.zones().containsAll(commonZones));

        // Check the combined occupants
        assertEquals(3, connectedArea.occupants().size());
        assertTrue(connectedArea.occupants().containsAll(occupants1));
        assertTrue(connectedArea.occupants().containsAll(occupants2));

        // Check the number of open connections
        assertEquals(1, connectedArea.openConnections());
    }
/*
    @Test
    void testConnectToWithNullArea() {
        // Create an area
        Set<Zone> zones = new HashSet<>(Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN)));
        List<PlayerColor> occupants = List.of(PlayerColor.RED);
        int openConnections = 1;
        Area<Zone> area = new Area<>(zones, occupants, openConnections);

        // Attempt to connect with a null area
        assertThrows(IllegalArgumentException.class, () -> area.connectTo(null));
    }


 */

}