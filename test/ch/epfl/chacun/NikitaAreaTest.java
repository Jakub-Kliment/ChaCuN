package ch.epfl.chacun;
/*
 *	Author:      Maxime Riesen
 *	Date:
 */
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class NikitaAreaTest {
    //TODO a faire plus de test pour le constructor
    @Test
    void areaCompactConstructorListCorrectSorted(){
        Area<Zone.Forest>area = new Area<>(Set.of(new Zone.Forest(1_3, Zone.Forest.Kind.PLAIN)), List.of(PlayerColor.BLUE,PlayerColor.RED),2);
        assertEquals(List.of(PlayerColor.RED,PlayerColor.BLUE),area.occupants());
    }

    @Test
    void correctHasMenhir(){
        Zone.Forest zoneForest1 = new Zone.Forest(1_3, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneForest2 =new Zone.Forest(4_3, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Forest zoneForest3 =new Zone.Forest(4_3, Zone.Forest.Kind.PLAIN);
        Area<Zone.Forest>area = new Area<>(Set.of(zoneForest1, zoneForest2),List.of(PlayerColor.BLUE,PlayerColor.RED),2);
        Area<Zone.Forest>area2 = new Area<>(Set.of(zoneForest1, zoneForest3),List.of(PlayerColor.BLUE,PlayerColor.RED),2);

        assertTrue(Area.hasMenhir(area));
        assertFalse(Area.hasMenhir(area2));
    }
    @Test
    void correctMushroomGroupCount(){
        Zone.Forest zoneForest1 = new Zone.Forest(1_3, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneForest2 =new Zone.Forest(4_3, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest zoneForest3 =new Zone.Forest(4_3, Zone.Forest.Kind.PLAIN);
        Area<Zone.Forest>area1 = new Area<>(Set.of(zoneForest1, zoneForest2),List.of(PlayerColor.BLUE,PlayerColor.RED),1);
        Area<Zone.Forest>area2 = new Area<>(Set.of(zoneForest1, zoneForest3),List.of(PlayerColor.BLUE,PlayerColor.RED),2);
        assertEquals(1,Area.mushroomGroupCount(area1));
        assertEquals(0,Area.mushroomGroupCount(area2));
    }
    @Test
    void correctSetOfAnimals(){
        Animal animal1 = new Animal(4_2_0, Animal.Kind.DEER);
        Animal animal2 = new Animal(4_31, Animal.Kind.MAMMOTH); //A demander quelle tyoe de id c est a mettre ici
        Zone.Meadow meadow1 = new Zone.Meadow(1_2, List.of(animal1), null);
        Zone.Meadow meadow2 = new Zone.Meadow(23, List.of(animal2), null);
        Zone.Meadow meadow3 = new Zone.Meadow(1_5, List.of(), null);
        Area<Zone.Meadow> meadowArea = new Area<>(Set.of(meadow1,meadow2,meadow3), List.of(PlayerColor.BLUE,PlayerColor.RED),1);
        assertEquals(Set.of(animal2),Area.animals(meadowArea,Set.of(animal1)));
    }
    @Test
    void correctRiverFishCount(){
        Zone.Lake lake1 = new Zone.Lake(4_8, 1, null);
        Zone.River river1 = new Zone.River(4_1, 3, lake1);
        Zone.River river2 = new Zone.River(4_3, 1, null);
        Zone.River river3 = new Zone.River(5_1, 0, lake1);
        Area<Zone.River> riverArea1 = new Area<>(Set.of(river1,river2),List.of(PlayerColor.GREEN),1);
        Area<Zone.River> riverArea2 = new Area<>(Set.of(river1,river2,river3),List.of(PlayerColor.GREEN),1);
        assertEquals(5,Area.riverFishCount(riverArea1));
        assertEquals(5,Area.riverFishCount(riverArea2));
    }
    @Test
    void correctRiverSystemFishCount(){
        Zone.Lake lake1 = new Zone.Lake(4_8, 1, null);
        Zone.Lake lake2 = new Zone.Lake(35,2,null);
        Zone.River river1 = new Zone.River(4_1, 3, lake1);
        Zone.River river2 = new Zone.River(4_3, 1, null);
        Zone.River river3 = new Zone.River(5_1, 0, lake1);
        Area<Zone.Water> waterArea = new Area<>(Set.of(lake1,lake2,river1,river2,river3),List.of(PlayerColor.RED),2);
        assertEquals(7,Area.riverSystemFishCount(waterArea));
    }
    @Test
    void correctLakeCount(){
        Zone.Lake lake1 = new Zone.Lake(4_8, 1, null);
        Zone.Lake lake2 = new Zone.Lake(35,2,null);
        Zone.River river1 = new Zone.River(4_1, 3, lake1);
        Zone.River river2 = new Zone.River(4_3, 1, null);
        Zone.River river3 = new Zone.River(5_1, 0, lake1);
        Area<Zone.Water> waterArea = new Area<>(Set.of(lake1,lake2,river1,river2,river3),List.of(PlayerColor.RED),2);
        assertEquals(2,Area.lakeCount(waterArea));
    }
    @Test
    void correctIsClosed(){
        Zone.Forest zoneForest1 = new Zone.Forest(1_3, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneForest2 =new Zone.Forest(4_3, Zone.Forest.Kind.WITH_MUSHROOMS);
        Area<Zone.Forest>area1 = new Area<>(Set.of(zoneForest1, zoneForest2),List.of(PlayerColor.BLUE,PlayerColor.RED),0);
        assertTrue(area1.isClosed());
    }
    @Test
    void correctIsOccupied(){
        Zone.Forest zoneForest1 = new Zone.Forest(1_3, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneForest2 =new Zone.Forest(4_3, Zone.Forest.Kind.WITH_MUSHROOMS);
        Area<Zone.Forest>area1 = new Area<>(Set.of(zoneForest1, zoneForest2),List.of(PlayerColor.BLUE,PlayerColor.RED),0);
        Area<Zone.Forest>area2 = new Area<>(Set.of(zoneForest1, zoneForest2),List.of(),1);
        assertTrue(area1.isOccupied());
        assertFalse(area2.isOccupied());
    }
    @Test
    void correctMajorityOccupants(){
        Zone.Forest zoneForest1 = new Zone.Forest(1_3, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneForest2 =new Zone.Forest(4_3, Zone.Forest.Kind.WITH_MUSHROOMS);
        Area<Zone.Forest>area1 = new Area<>(Set.of(zoneForest1, zoneForest2),List.of(PlayerColor.BLUE,PlayerColor.RED,PlayerColor.GREEN,PlayerColor.BLUE,PlayerColor.RED),1);
        assertEquals(Set.of(PlayerColor.BLUE,PlayerColor.RED),area1.majorityOccupants());
    }
    @Test
    void correctConnectTo(){
        Zone.Forest zoneForest1 = new Zone.Forest(1_3, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneForest2 =new Zone.Forest(4_3, Zone.Forest.Kind.WITH_MUSHROOMS);
        Area<Zone.Forest>area1 = new Area<>(Set.of(zoneForest1, zoneForest2),List.of(PlayerColor.BLUE,PlayerColor.RED,PlayerColor.GREEN),3);
        Zone.Forest zoneForest3 = new Zone.Forest(2_3, Zone.Forest.Kind.PLAIN);
        Area<Zone.Forest>area2 = new Area<>(Set.of(zoneForest3),List.of(PlayerColor.BLUE),1);
        assertEquals(new Area<Zone.Forest>(Set.of(zoneForest1,zoneForest2,zoneForest3),List.of(PlayerColor.BLUE,PlayerColor.RED,PlayerColor.GREEN,PlayerColor.BLUE),2),area1.connectTo(area2));
        assertEquals(new Area<>(Set.of(zoneForest1, zoneForest2),List.of(PlayerColor.BLUE,PlayerColor.RED,PlayerColor.GREEN),1),area1.connectTo(area1));
    }
    @Test
    void correctWithInitialOccupant(){
        Zone.Forest zoneForest1 = new Zone.Forest(1_3, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneForest2 =new Zone.Forest(4_3, Zone.Forest.Kind.WITH_MUSHROOMS);
        Area<Zone.Forest>area1 = new Area<>(Set.of(zoneForest1, zoneForest2),List.of(PlayerColor.BLUE,PlayerColor.RED,PlayerColor.GREEN),3);
        Area<Zone.Forest>area2 = new Area<>(Set.of(zoneForest1, zoneForest2),List.of(),3);

        assertThrows(IllegalArgumentException.class, () -> {area1.withInitialOccupant(PlayerColor.BLUE);});
        assertEquals(new Area<Zone.Forest>(Set.of(zoneForest1,zoneForest2),List.of(PlayerColor.GREEN),3),area2.withInitialOccupant(PlayerColor.GREEN));
    }
    @Test
    void correctWithoutOccupant(){
        Zone.Forest zoneForest1 = new Zone.Forest(1_3, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneForest2 =new Zone.Forest(4_3, Zone.Forest.Kind.WITH_MUSHROOMS);
        Area<Zone.Forest>area1 = new Area<>(Set.of(zoneForest1, zoneForest2),List.of(PlayerColor.BLUE,PlayerColor.RED,PlayerColor.GREEN),3);
        Area<Zone.Forest>area2 = new Area<>(Set.of(zoneForest1, zoneForest2),List.of(),3);
        assertThrows(IllegalArgumentException.class, () -> {area2.withoutOccupant(PlayerColor.GREEN);});
        assertEquals(new Area<Zone.Forest>(Set.of(zoneForest1,zoneForest2),List.of(PlayerColor.BLUE,PlayerColor.GREEN),3),area1.withoutOccupant(PlayerColor.RED));
    }
    @Test
    void correctWithoutOccupants(){
        Zone.Forest zoneForest1 = new Zone.Forest(1_3, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneForest2 =new Zone.Forest(4_3, Zone.Forest.Kind.WITH_MUSHROOMS);
        Area<Zone.Forest>area1 = new Area<>(Set.of(zoneForest1, zoneForest2),List.of(PlayerColor.BLUE,PlayerColor.RED,PlayerColor.GREEN),2);
        assertEquals(new Area<>(Set.of(zoneForest1, zoneForest2),List.of(),2),area1.withoutOccupants());
    }
    @Test
    void correctTileIds(){
        Zone.Forest zoneForest1 = new Zone.Forest(1_3, Zone.Forest.Kind.PLAIN);
        Zone.Forest zoneForest2 =new Zone.Forest(4_3, Zone.Forest.Kind.WITH_MUSHROOMS);
        Area<Zone.Forest>area1 = new Area<>(Set.of(zoneForest1, zoneForest2),List.of(PlayerColor.BLUE,PlayerColor.RED,PlayerColor.GREEN),2);
        assertEquals(Set.of(1,4),area1.tileIds()); // A REVOIR
    }
    @Test
    void correctZoneWithSpecialPower(){
        Animal animal1 = new Animal(4_2_0, Animal.Kind.DEER);
        Animal animal2 = new Animal(4_31, Animal.Kind.MAMMOTH); //A demander quelle tyoe de id c est a mettre ici
        Zone.Meadow meadow1 = new Zone.Meadow(1_2, List.of(animal1), Zone.SpecialPower.HUNTING_TRAP);
        Zone.Meadow meadow2 = new Zone.Meadow(23, List.of(animal2), null);
        Zone.Meadow meadow3 = new Zone.Meadow(1_5, List.of(), null);
        Area<Zone.Meadow> meadowArea = new Area<>(Set.of(meadow1,meadow2,meadow3), List.of(PlayerColor.BLUE,PlayerColor.GREEN),2);
        assertEquals(meadow1, meadowArea.zoneWithSpecialPower(Zone.SpecialPower.HUNTING_TRAP));
        assertEquals(null,meadowArea.zoneWithSpecialPower(Zone.SpecialPower.SHAMAN));
    }

}
