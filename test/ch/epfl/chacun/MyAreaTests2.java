package ch.epfl.chacun;

import ch.epfl.chacun.Animal;
import ch.epfl.chacun.Area;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.Zone;
import org.junit.jupiter.api.Test;

import java.util.*;

import static ch.epfl.chacun.Area.*;
import static org.junit.jupiter.api.Assertions.*;

public class MyAreaTests2 {

    /**
     * Compact constructor sorts the PlayerColor list correctly when it's empty
     */
    @Test
    void CompactConstructorWorksOnTrivialOccupantList() {

        Set<Zone> zoneSet = new HashSet<>();
        List<PlayerColor> playerColList = new ArrayList<>();
        int openCo = 2;

        Area<Zone> testArea = new Area<>(zoneSet, playerColList,openCo);
        List<PlayerColor> expected = new ArrayList<>();

        assertEquals(expected, testArea.occupants());

    }

    /**
     * The compact constructor of Area sorts the occupant list correctly when occupants (PlayerColor) are out of order
     */
    @Test
    void CompactConstructorWorksOnNonTrivialList() {

        Animal animal1 = new Animal(0, Animal.Kind.AUROCHS);
        Animal animal2 = new Animal(1, Animal.Kind.TIGER);
        List<Animal> animalsList = new ArrayList<>();
        animalsList.add(animal1);
        animalsList.add(animal2);

        Set<Zone> ZoneSet = new HashSet<>();

        PlayerColor PC1 = PlayerColor.RED;
        PlayerColor PC2 = PlayerColor.GREEN;
        PlayerColor PC3 = PlayerColor.BLUE;
        PlayerColor PC4 = PlayerColor.YELLOW;
        PlayerColor PC5 = PlayerColor.PURPLE;
        PlayerColor PC6 = PlayerColor.BLUE;

        List<PlayerColor> playerColList = new ArrayList<>();
        Collections.addAll(playerColList, PC1, PC2, PC3, PC4, PC5, PC6);

        int openCo = 2;

        Area<Zone> testArea = new Area<>(ZoneSet, playerColList,openCo);

        List<PlayerColor> expectedPlayerCol = new ArrayList<>();
        Collections.addAll(expectedPlayerCol, PC1, PC3, PC6, PC2, PC4, PC5);

        assertEquals(expectedPlayerCol, testArea.occupants());
    }

    @Test
    void hasMenhirWorksOnFalse() {

        Zone.Forest forestZone1 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
        Zone.Forest forestZone2 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
        Zone.Forest forestZone3 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);

        Set<Zone.Forest> zoneSet = new HashSet<>();
        Collections.addAll(zoneSet, forestZone1, forestZone2, forestZone3);
        List<PlayerColor> playerColList = new ArrayList<>();
        int openCo = 2;

        Area<Zone.Forest> testArea = new Area<>(zoneSet, playerColList, openCo);

        assertFalse(hasMenhir(testArea));
    }

    @Test
    void hasMenhirWorksOnTrue() {

        Zone.Forest forestZone1 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
        Zone.Forest forestZone2 = new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Forest forestZone3 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
        Zone.Forest forestZone4 = new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR);

        Set<Zone.Forest> zoneSet = new HashSet<>();
        Collections.addAll(zoneSet, forestZone1, forestZone2, forestZone3, forestZone4);
        List<PlayerColor> playerColList = new ArrayList<>();
        int openCo = 2;

        Area<Zone.Forest> testArea = new Area<>(zoneSet, playerColList, openCo);

        assertTrue(hasMenhir(testArea));
    }

    /**
     * The tested are has no forest zone
     */
    @Test
    void hasMenhirWorksOnTrivialCase() {
        Set<Zone.Forest> zoneSet = new HashSet<>();
        List<PlayerColor> playerColList = new ArrayList<>();
        int openCo = 2;

        Area<Zone.Forest> testArea = new Area<>(zoneSet, playerColList, openCo);

        assertFalse(hasMenhir(testArea));
    }

    @Test
    void mushroomGroupCountWorksOnTrivialCase() {

        Zone.Forest forestZone1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Forest forestZone2 = new Zone.Forest(2, Zone.Forest.Kind.PLAIN);
        Zone.Forest forestZone3 = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);
        Zone.Forest forestZone4 = new Zone.Forest(4, Zone.Forest.Kind.WITH_MENHIR);

        Set<Zone.Forest> zoneSet = new HashSet<>();
        Collections.addAll(zoneSet, forestZone1, forestZone2, forestZone3, forestZone4);
        List<PlayerColor> playerColList = new ArrayList<>();
        int openCo = 2;

        Area<Zone.Forest> testArea = new Area<>(zoneSet, playerColList, openCo);

        int expected = 0;

        assertEquals(expected, mushroomGroupCount(testArea));
    }

    /**
    Set zones --> le set qui contient toutes les zones de l'air
    n'enregistre les zones (par exemple les Zone.Forest) que si
    leur id sont différents, normal ?
     */
    @Test
    void mushroomGroupCountWorksOnNonTrivialCase() {

        Zone.Forest forestZone1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Forest forestZone4 = new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forestZone2 = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);
        Zone.Forest forestZone3 = new Zone.Forest(4, Zone.Forest.Kind.WITH_MUSHROOMS);

        Set<Zone.Forest> zoneSet = new HashSet<>();
        Collections.addAll(zoneSet, forestZone1, forestZone2, forestZone3, forestZone4);
        List<PlayerColor> playerColList = new ArrayList<>();
        int openCo = 2;

        Area<Zone.Forest> testArea = new Area<>(zoneSet, playerColList, openCo);

        int expected = 2;

        /*for (Zone.Forest zone : zoneSet) {
            System.out.println("test");
        }
        */
        assertEquals(expected, mushroomGroupCount(testArea));
    }

    /**
     * Méthode animals() est ce que la méthode doit renvoyer un set vide si le pré donné ne contient pas d'animaux
     *  ou doit il renvoyer null ?
     * si au lieu de animals on a null
     */
    @Test
    void animalWorksOnTrivialCase() {

        List<Animal> animals = new ArrayList<>();
        Zone.Meadow meadowZone1 = new Zone.Meadow(1, animals, null);
        Zone.Meadow meadowZone2 = new Zone.Meadow(2, animals, null);
        Zone.Meadow meadowZone3 = new Zone.Meadow(3, animals, null);
        Zone.Meadow meadowZone4 = new Zone.Meadow(4, animals, null);


        Set<Zone.Meadow> zoneSet = new HashSet<>();
        Collections.addAll(zoneSet, meadowZone1, meadowZone3, meadowZone4, meadowZone2);
        List<PlayerColor> playerColList = new ArrayList<>();
        int openCo = 2;
        Area<Zone.Meadow> testArea = new Area<>(zoneSet, playerColList, openCo);

        Set<Animal> expected = new HashSet<>();

        assertEquals(expected, animals(testArea, null));
    }
    @Test
    void animalsWorksOnNonTrivialCase() {

        Animal animal1 = new Animal(0, Animal.Kind.AUROCHS);
        Animal animal2 = new Animal(1, Animal.Kind.TIGER);
        List<Animal> animalsList = new ArrayList<>();
        animalsList.add(animal1);
        animalsList.add(animal2);

        Animal animal3 = new Animal(1, Animal.Kind.MAMMOTH);
        Animal animal4 = new Animal(0, Animal.Kind.DEER);
        List<Animal> animalsList2 = new ArrayList<>();
        animalsList2.add(animal3);
        animalsList2.add(animal4);


        Zone.Meadow meadowZone1 = new Zone.Meadow(1, animalsList, null);
        Zone.Meadow meadowZone2 = new Zone.Meadow(2, animalsList2, null);
        Zone.Meadow meadowZone3 = new Zone.Meadow(3, animalsList, null);
        Zone.Meadow meadowZone4 = new Zone.Meadow(4, animalsList, null);


        Set<Zone.Meadow> zoneSet = new HashSet<>();
        Collections.addAll(zoneSet, meadowZone1, meadowZone3, meadowZone4, meadowZone2);
        List<PlayerColor> playerColList = new ArrayList<>();
        int openCo = 2;

        Area<Zone.Meadow> testArea = new Area<>(zoneSet, playerColList, openCo);

        Set<Animal> cancelledAnimals= new HashSet<>();
        cancelledAnimals.add(animal1);

        Set<Animal> expected = new HashSet<>();
        Collections.addAll(expected, animal2, animal3, animal4);

        assertEquals(expected, animals(testArea, cancelledAnimals));
    }

    /**
     * The rivers do not share a lake
     */
    @Test
    void riverFishCountWorksOnNonRepetitiveCase() {

        Zone.Lake lake1 = new Zone.Lake(8, 2, null);

        Zone.River riverZone1 = new Zone.River(1, 3, lake1);
        Zone.River riverZone2 = new Zone.River(3, 2, null);

        Set<Zone.River> zoneSet = new HashSet<>();
        Collections.addAll(zoneSet, riverZone1, riverZone2);

        List<PlayerColor> occupants = new ArrayList<>();

        Area<Zone.River> testArea = new Area<>(zoneSet, occupants, 1);

        assertEquals(7, riverFishCount(testArea));

    }

    /**
     * The rivers share a lake
     */
    @Test
    void riverFishCountWorksOnRepetitiveCase() {

        Zone.Lake lake1 = new Zone.Lake(8, 2, null);

        Zone.River riverZone1 = new Zone.River(1, 3, lake1);
        Zone.River riverZone2 = new Zone.River(3, 2, lake1);

        Set<Zone.River> zoneSet = new HashSet<>();
        Collections.addAll(zoneSet, riverZone1, riverZone2);

        List<PlayerColor> occupants = new ArrayList<>();

        Area<Zone.River> testArea = new Area<>(zoneSet, occupants, 1);

        assertEquals(7, riverFishCount(testArea));

    }

    /**
     * No lake involved
     */
    @Test
    void riverFishCountWorksWhenNoLake() {

        Zone.River riverZone1 = new Zone.River(1, 3, null);
        Zone.River riverZone2 = new Zone.River(3, 2, null);

        Set<Zone.River> zoneSet = new HashSet<>();
        Collections.addAll(zoneSet, riverZone1, riverZone2);

        List<PlayerColor> occupants = new ArrayList<>();

        Area<Zone.River> testArea = new Area<>(zoneSet, occupants, 1);

        assertEquals(5, riverFishCount(testArea));

    }

    /**
     * Multiple lakes involved
     */
    @Test
    void riverFishCountWorksWhenMultipleLakes() {

        Zone.Lake lake1 = new Zone.Lake(8, 2, null);
        Zone.Lake lake2 = new Zone.Lake(7, 6, null);

        Zone.River riverZone1 = new Zone.River(1, 3, lake1);
        Zone.River riverZone2 = new Zone.River(3, 2, lake2);

        Set<Zone.River> zoneSet = new HashSet<>();
        Collections.addAll(zoneSet, riverZone1, riverZone2);

        List<PlayerColor> occupants = new ArrayList<>();

        Area<Zone.River> testArea = new Area<>(zoneSet, occupants, 1);

        assertEquals(13, riverFishCount(testArea));
    }

    /**
     * No river, no lake
     */
    @Test
    void riverSystemFishCountWorksOnTrivialCase() {
        Set<Zone.Water> waterZones = new HashSet<>();
        List<PlayerColor> occupants = new ArrayList<>();
        Area<Zone.Water> testArea = new Area<>(waterZones, occupants, 2);

        assertEquals(0, riverSystemFishCount(testArea));
    }

    @Test
    void riverSystemFishCountWorksOnNonTrivialCase() {

        Zone.Lake lake1 = new Zone.Lake(8, 2, null);
        Zone.Lake lake2 = new Zone.Lake(7, 6, null);
        Zone.Lake lake3 = new Zone.Lake(9, 4, null);

        Zone.River riverZone1 = new Zone.River(1, 3, lake1);
        Zone.River riverZone2 = new Zone.River(3, 2, lake2);
        Set<Zone.Water> waterZones = new HashSet<>();
        Collections.addAll(waterZones, lake1, lake2, lake3, riverZone1, riverZone2);

        List<PlayerColor> occupants = new ArrayList<>();

        Area<Zone.Water> testArea = new Area<>(waterZones, occupants, 2);

        assertEquals(17, riverSystemFishCount(testArea));
    }

    @Test
    void lakeCountWorksOnTrivialCase() {

        Set<Zone.Water> waterZones = new HashSet<>();
        List<PlayerColor> occupants = new ArrayList<>();
        Area<Zone.Water> testArea = new Area<>(waterZones, occupants, 2);

        assertEquals(0, lakeCount(testArea));
    }

    @Test
    void lakeCountWorksOnNonTrivialCase() {
        Zone.Lake lake1 = new Zone.Lake(8, 2, null);
        Zone.Lake lake2 = new Zone.Lake(7, 6, null);
        Zone.Lake lake3 = new Zone.Lake(9, 4, null);

        Zone.River riverZone1 = new Zone.River(1, 3, lake1);
        Zone.River riverZone2 = new Zone.River(3, 2, lake2);
        Set<Zone.Water> waterZones = new HashSet<>();
        Collections.addAll(waterZones, lake1, lake2, lake3, riverZone1, riverZone2);

        List<PlayerColor> occupants = new ArrayList<>();

        Area<Zone.Water> testArea = new Area<>(waterZones, occupants, 2);

        assertEquals(3, lakeCount(testArea));

    }

    @Test
    void isClosedWorksWhenFalse() {
        Zone.Lake lake1 = new Zone.Lake(8, 2, null);
        Zone.Lake lake2 = new Zone.Lake(7, 6, null);
        Zone.Lake lake3 = new Zone.Lake(9, 4, null);

        Zone.River riverZone1 = new Zone.River(1, 3, lake1);
        Zone.River riverZone2 = new Zone.River(3, 2, lake2);
        Set<Zone.Water> waterZones = new HashSet<>();
        Collections.addAll(waterZones, lake1, lake2, lake3, riverZone1, riverZone2);

        List<PlayerColor> occupants = new ArrayList<>();

        Area<Zone.Water> testArea = new Area<>(waterZones, occupants, 2);

        assertFalse(testArea.isClosed());
    }

    @Test
    void isClosedWorksWhenTrue() {
        Zone.Lake lake1 = new Zone.Lake(8, 2, null);
        Zone.Lake lake2 = new Zone.Lake(7, 6, null);
        Zone.Lake lake3 = new Zone.Lake(9, 4, null);

        Zone.River riverZone1 = new Zone.River(1, 3, lake1);
        Zone.River riverZone2 = new Zone.River(3, 2, lake2);
        Set<Zone.Water> waterZones = new HashSet<>();
        Collections.addAll(waterZones, lake1, lake2, lake3, riverZone1, riverZone2);

        List<PlayerColor> occupants = new ArrayList<>();

        Area<Zone.Water> testArea = new Area<>(waterZones, occupants, 0);

        assertTrue(testArea.isClosed());
    }

    @Test
    void isOccupiedWorksWhenTrue() {
        Zone.Lake lake1 = new Zone.Lake(8, 2, null);
        Zone.Lake lake2 = new Zone.Lake(7, 6, null);
        Zone.Lake lake3 = new Zone.Lake(9, 4, null);

        Zone.River riverZone1 = new Zone.River(1, 3, lake1);
        Zone.River riverZone2 = new Zone.River(3, 2, lake2);
        Set<Zone.Water> waterZones = new HashSet<>();
        Collections.addAll(waterZones, lake1, lake2, lake3, riverZone1, riverZone2);

        List<PlayerColor> occupants = new ArrayList<>();

        Area<Zone.Water> testArea = new Area<>(waterZones, occupants, 0);

        assertFalse(testArea.isOccupied());
    }

    @Test
    void isOccupiedWorksWhenFalse() {
        Zone.Lake lake1 = new Zone.Lake(8, 2, null);
        Zone.Lake lake2 = new Zone.Lake(7, 6, null);
        Zone.Lake lake3 = new Zone.Lake(9, 4, null);

        Zone.River riverZone1 = new Zone.River(1, 3, lake1);
        Zone.River riverZone2 = new Zone.River(3, 2, lake2);
        Set<Zone.Water> waterZones = new HashSet<>();
        Collections.addAll(waterZones, lake1, lake2, lake3, riverZone1, riverZone2);

        PlayerColor PC1 = PlayerColor.RED;
        PlayerColor PC2 = PlayerColor.GREEN;
        PlayerColor PC3 = PlayerColor.BLUE;
        PlayerColor PC4 = PlayerColor.YELLOW;
        PlayerColor PC5 = PlayerColor.PURPLE;
        PlayerColor PC6 = PlayerColor.BLUE;

        List<PlayerColor> occupants = new ArrayList<>();
        Collections.addAll(occupants, PC1, PC2, PC3, PC4, PC5, PC6);

        Area<Zone.Water> testArea = new Area<>(waterZones, occupants, 0);

        assertTrue(testArea.isOccupied());
    }

    /**
     * No occupants in the area
     */
    @Test
    void majorityOccupantsWorksOnTrivialCase() {
        Zone.Lake lake1 = new Zone.Lake(8, 2, null);
        Zone.Lake lake2 = new Zone.Lake(7, 6, null);
        Zone.Lake lake3 = new Zone.Lake(9, 4, null);

        Zone.River riverZone1 = new Zone.River(1, 3, lake1);
        Zone.River riverZone2 = new Zone.River(3, 2, lake2);
        Set<Zone.Water> waterZones = new HashSet<>();
        Collections.addAll(waterZones, lake1, lake2, lake3, riverZone1, riverZone2);

        List<PlayerColor> occupants = new ArrayList<>();

        Area<Zone.Water> testArea = new Area<>(waterZones, occupants, 0);

        Set<PlayerColor> expected = new HashSet<>();

        assertEquals(expected, testArea.majorityOccupants());
    }

    @Test
    void majorityOccupantsWorksWhen1MajorityOccupant() {
        Zone.Lake lake1 = new Zone.Lake(8, 2, null);
        Zone.Lake lake2 = new Zone.Lake(7, 6, null);
        Zone.Lake lake3 = new Zone.Lake(9, 4, null);

        Zone.River riverZone1 = new Zone.River(1, 3, lake1);
        Zone.River riverZone2 = new Zone.River(3, 2, lake2);
        Set<Zone.Water> waterZones = new HashSet<>();
        Collections.addAll(waterZones, lake1, lake2, lake3, riverZone1, riverZone2);

        PlayerColor PC1 = PlayerColor.RED;
        PlayerColor PC2 = PlayerColor.GREEN;
        PlayerColor PC3 = PlayerColor.BLUE;
        PlayerColor PC4 = PlayerColor.YELLOW;
        PlayerColor PC5 = PlayerColor.PURPLE;
        PlayerColor PC6 = PlayerColor.BLUE;

        List<PlayerColor> occupants = new ArrayList<>();
        Collections.addAll(occupants, PC1, PC2, PC3, PC4, PC5, PC6);

        Area<Zone.Water> testArea = new Area<>(waterZones, occupants, 0);

        Set<PlayerColor> expected = new HashSet<>();
        expected.add(PlayerColor.BLUE);

        assertEquals(expected, testArea.majorityOccupants());
    }

    @Test
    void majorityOccupantsWorksWhenMultipleMajorityOccupants() {
        Zone.Lake lake1 = new Zone.Lake(8, 2, null);
        Zone.Lake lake2 = new Zone.Lake(7, 6, null);
        Zone.Lake lake3 = new Zone.Lake(9, 4, null);

        Zone.River riverZone1 = new Zone.River(1, 3, lake1);
        Zone.River riverZone2 = new Zone.River(3, 2, lake2);
        Set<Zone.Water> waterZones = new HashSet<>();
        Collections.addAll(waterZones, lake1, lake2, lake3, riverZone1, riverZone2);

        PlayerColor PC1 = PlayerColor.RED;
        PlayerColor PC2 = PlayerColor.GREEN;
        PlayerColor PC3 = PlayerColor.BLUE;
        PlayerColor PC4 = PlayerColor.YELLOW;
        PlayerColor PC5 = PlayerColor.PURPLE;
        PlayerColor PC6 = PlayerColor.BLUE;
        PlayerColor PC7 = PlayerColor.RED;

        List<PlayerColor> occupants = new ArrayList<>();
        Collections.addAll(occupants, PC1, PC2, PC3, PC4, PC5, PC6, PC7);

        Area<Zone.Water> testArea = new Area<>(waterZones, occupants, 0);

        Set<PlayerColor> expected = new HashSet<>();
        expected.add(PlayerColor.BLUE);
        expected.add(PlayerColor.RED);

        assertEquals(expected, testArea.majorityOccupants());
    }

    @Test
    void connectToWorksWhenDifferentAreas() {

        Zone.Forest forestZone1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Forest forestZone2 = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);
        Zone.Forest forestZone3 = new Zone.Forest(4, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forestZone4 = new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS);
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Collections.addAll(zoneSet1, forestZone1, forestZone2);

        PlayerColor PC1 = PlayerColor.RED;
        PlayerColor PC2 = PlayerColor.GREEN;
        List<PlayerColor> occupants = new ArrayList<>();
        Collections.addAll(occupants, PC1, PC2);

        Area<Zone.Forest> testArea1 = new Area<>(zoneSet1, occupants, 2);


        Set<Zone.Forest> zoneSet2 = new HashSet<>();
        Collections.addAll(zoneSet2, forestZone3, forestZone4);

        PlayerColor PC3 = PlayerColor.BLUE;
        PlayerColor PC4 = PlayerColor.YELLOW;
        PlayerColor PC5 = PlayerColor.PURPLE;
        List<PlayerColor> occupants2 = new ArrayList<>();
        Collections.addAll(occupants2, PC3, PC4, PC5);

        Area<Zone.Forest> testArea2 = new Area<>(zoneSet2, occupants2, 1);

        Area<Zone.Forest> actual = testArea1.connectTo(testArea2);

        Set<Zone.Forest> allZones = new HashSet<>();
        Collections.addAll(allZones, forestZone1, forestZone2, forestZone3, forestZone4);
        List<PlayerColor> allOccupants = new ArrayList<>();
        Collections.addAll(allOccupants, PC1, PC2, PC3, PC4, PC5);
        Area<Zone.Forest> expected = new Area<>(allZones, allOccupants, 1);

        assertEquals(expected, actual);
    }

    @Test
    void withInitialOccupantWorksOnNonTrivialCase() {

        Zone.Forest forestZone1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Forest forestZone2 = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Collections.addAll(zoneSet1, forestZone1, forestZone2);


        List<PlayerColor> occupants = new ArrayList<>();


        Area<Zone.Forest> testArea1 = new Area<>(zoneSet1, occupants, 2);

        PlayerColor PC1 = PlayerColor.BLUE;
        List<PlayerColor> newOccupants = new ArrayList<>();
        newOccupants.add(PC1);
        Area<Zone.Forest> expected = new Area<>(zoneSet1, newOccupants, 2);

        Area<Zone.Forest> actual = testArea1.withInitialOccupant(PC1);

        assertEquals(expected, actual);

    }

    @Test
    void withInitialOccupantThrowsWhenAlreadyOccupied() {
        Zone.Forest forestZone1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Forest forestZone2 = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Collections.addAll(zoneSet1, forestZone1, forestZone2);

        PlayerColor PC1 = PlayerColor.BLUE;
        List<PlayerColor> occupants = new ArrayList<>();
        occupants.add(PC1);

        Area<Zone.Forest> testArea1 = new Area<>(zoneSet1, occupants, 2);

        assertThrows(IllegalArgumentException.class, () -> {
            testArea1.withInitialOccupant(PlayerColor.RED);
        });
    }

    @Test
    void withoutOccupantThrowsWhenEmpty() {

        Zone.Forest forestZone1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Forest forestZone2 = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Collections.addAll(zoneSet1, forestZone1, forestZone2);

        PlayerColor PC1 = PlayerColor.BLUE;
        List<PlayerColor> occupants = new ArrayList<>();
        occupants.add(PC1);

        Area<Zone.Forest> testArea1 = new Area<>(zoneSet1, occupants, 2);

        assertThrows(IllegalArgumentException.class, () -> {
            testArea1.withoutOccupant(PlayerColor.RED);
        });
    }

    @Test
    void withoutOccupantWorksOnNonTrivialCase() {
        Zone.Forest forestZone1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Forest forestZone2 = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Collections.addAll(zoneSet1, forestZone1, forestZone2);

        PlayerColor PC1 = PlayerColor.BLUE;
        List<PlayerColor> occupants = new ArrayList<>();
        occupants.add(PC1);

        Area<Zone.Forest> testArea1 = new Area<>(zoneSet1, occupants, 2);

        List<PlayerColor> noOccupant = new ArrayList<>();
        Area<Zone.Forest> expected = new Area<>(zoneSet1, noOccupant, 2);

        assertEquals(expected, testArea1.withoutOccupant(PlayerColor.BLUE));
    }

    @Test
    void withoutOccupantWorksWhenMultipleOccupants() {

        Zone.Forest forestZone1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Forest forestZone2 = new Zone.Forest(3, Zone.Forest.Kind.PLAIN);
        Set<Zone.Forest> zoneSet1 = new HashSet<>();
        Collections.addAll(zoneSet1, forestZone1, forestZone2);

        PlayerColor PC1 = PlayerColor.BLUE;
        PlayerColor PC2 = PlayerColor.GREEN;
        PlayerColor PC3 = PlayerColor.BLUE;
        PlayerColor PC4 = PlayerColor.YELLOW;
        PlayerColor PC5 = PlayerColor.PURPLE;
        PlayerColor PC6 = PlayerColor.BLUE;

        List<PlayerColor> occupants = new ArrayList<>();
        Collections.addAll(occupants, PC1, PC2, PC3, PC4, PC5, PC6);

        Area<Zone.Forest> testArea1 = new Area<>(zoneSet1, occupants, 2);

        List<PlayerColor> newOccupants = new ArrayList<>();
        Collections.addAll(newOccupants, PC2, PC3, PC4, PC5, PC6);
        Area<Zone.Forest> expected = new Area<>(zoneSet1, newOccupants, 2);

        assertEquals(expected, testArea1.withoutOccupant(PlayerColor.BLUE));


    }
}
