package ch.epfl.chacun.Etape3;



import ch.epfl.chacun.Animal;
import ch.epfl.chacun.Area;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.Zone;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AreaTest {
    @Test
    void testPersoAreaCopies() {
        var immutableZones = Set.of(new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR));
        var immutableOccupants = List.of(PlayerColor.RED, PlayerColor.BLUE);
        var mutableZones = new HashSet(immutableZones);
        var mutableOccupants = new ArrayList<>(immutableOccupants);

        Area<Zone.Forest> area = new Area(mutableZones, mutableOccupants, 0);
        mutableZones.clear();
        mutableOccupants.clear();

        assertEquals(immutableZones, area.zones());
        assertEquals(immutableOccupants, area.occupants());
    }
    @Test
    void testPersoAreaThrows(){
        assertThrows(IllegalArgumentException.class , ()-> {new Area(Set.of() , List.of() , -1);});
    }
    @Test
    void testPersoAreaSorts(){
        var unsorted  =new ArrayList<PlayerColor>(List.of(PlayerColor.PURPLE , PlayerColor.BLUE , PlayerColor.YELLOW , PlayerColor.BLUE , PlayerColor.GREEN , PlayerColor.PURPLE));
        Area area = new Area<>(Set.of() , unsorted , 1);

        assertEquals(PlayerColor.BLUE , area.occupants().get(0));
        assertEquals(PlayerColor.BLUE , area.occupants().get(1));
        assertEquals(PlayerColor.GREEN , area.occupants().get(2));
        assertEquals(PlayerColor.YELLOW , area.occupants().get(3));
        assertEquals(PlayerColor.PURPLE , area.occupants().get(4));
        assertEquals(PlayerColor.PURPLE , area.occupants().get(5));
    }




    @Test
    void persoTestHasMenhir() {
        // Arrange
        Zone.Forest forest1 = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR);
        Zone.Forest forest3 = new Zone.Forest(3, Zone.Forest.Kind.WITH_MUSHROOMS);
        Area<Zone.Forest> forestArea = new Area<>(Set.of(forest1, forest2, forest3), List.of(), 2);

        // Act
        boolean result1 = Area.hasMenhir(forestArea);
        boolean result2 = Area.hasMenhir(new Area<>(Set.of(forest1,forest3), List.of(), 1));

        // Assert
        assertTrue(result1);
        assertFalse(result2);
    }


    @Test
    void testMyMushroomGroupCount() {
        // given
        Zone.Forest forest1 = new Zone.Forest(1, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest1Bis = new Zone.Forest(4, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest2 = new Zone.Forest(2,Zone.Forest.Kind.WITH_MENHIR);
        Zone.Forest forest3 = new Zone.Forest( 3,Zone.Forest.Kind.PLAIN);
        Zone.River river1 = new Zone.River(1, 3, null);
        Area<Zone.Forest> forestArea1 = new Area<>(Set.of(forest1), List.of(), 0);
        Area<Zone.Forest> forestArea2 = new Area<>(Set.of(forest2), List.of(), 0);
        Area<Zone.Forest> forestArea3 = new Area<>(Set.of(forest3), List.of(), 0);
        Area<Zone.Forest> forestArea4 = new Area<>(Set.of(forest1 , forest1Bis), List.of() , 0);
        Area<Zone.Forest> forestArea5 = new Area<>(Set.of(), List.of() , 0);


        // when
        int groupCount1 = Area.mushroomGroupCount(forestArea1);
        int groupCount2 = Area.mushroomGroupCount(forestArea2);
        int groupCount3 = Area.mushroomGroupCount(forestArea3);
        int groupCount4 = Area.mushroomGroupCount(forestArea4);

        // then
        assertEquals(1, groupCount1, "Incorrect number of mushroom groups for forest area 1.");
        assertEquals(0, groupCount2, "Incorrect number of mushroom groups for forest area 2.");
        assertEquals(0, groupCount3, "Incorrect number of mushroom groups for forest area 3.");
        assertEquals(2 , groupCount4);
        assertEquals(0 , Area.mushroomGroupCount(forestArea5));

    }


    @Test
    void testMyRiverFishCount() {
        // Arrange
        Zone.River river1 = new Zone.River(1, 2,new Zone.Lake(4, 5, Zone.SpecialPower.PIT_TRAP));
        Zone.River river2 = new Zone.River(1, 2,null );
        Zone.River river3 = new Zone.River(1, 0,null );
        Area<Zone.River> riverArea1 = new Area<>(Set.of(river1), List.of(), 1);
        Area<Zone.River> riverArea2 = new Area<>(Set.of(river1, river2), List.of(), 1);
        Area<Zone.River> riverArea3 = new Area<>(Set.of(river3), List.of(), 1);

        // Act
        int result = Area.riverFishCount(riverArea1);

        // Assert
        assertEquals(7, result);
        assertEquals(9, Area.riverFishCount(riverArea2));
        assertEquals(0, Area.riverFishCount(riverArea3));
        assertEquals(0, Area.riverFishCount(new Area<>(Set.of(), List.of(), 1)));
    }


    @Test
    void testPersoAnimals(){
        var a1 = new Animal(1 * 100, Animal.Kind.AUROCHS);
        var a2 = new Animal(2 * 100, Animal.Kind.MAMMOTH);
        var a3 = new Animal(3 * 100, Animal.Kind.DEER);
        var a4 = new Animal(4 * 100, Animal.Kind.MAMMOTH);
        var a5 = new Animal(5 * 100, Animal.Kind.TIGER);
        var a6 = new Animal(6 * 100, Animal.Kind.DEER);


        Zone.Meadow meadow1 = new Zone.Meadow(1, List.of(a1, a2), Zone.SpecialPower.WILD_FIRE);
        Zone.Meadow meadow2 = new Zone.Meadow(1, List.of(a3, a5), Zone.SpecialPower.WILD_FIRE);
        Zone.Meadow meadow3 = new Zone.Meadow(2, List.of(), Zone.SpecialPower.WILD_FIRE);

        Area<Zone.Meadow> meadowArea1 = new Area<>(Set.of(meadow1, meadow2), List.of(), 2);
        Area<Zone.Meadow> meadowArea2 = new Area<>(Set.of(meadow3), List.of(), 2);


        Set<Animal> listcancelledAnimals1 = new HashSet<>(List.of(a1, a2, a3));
        Set<Animal> listcancelledAnimals2 = new HashSet<>(List.of(a1, a2, a6));

        Set<Animal> animalSet1 = new HashSet<>(List.of( a5));
        Set<Animal> animalSet2 = new HashSet<>(List.of( a3, a5));

        assertEquals(animalSet1, Area.animals(meadowArea1, listcancelledAnimals1));
        assertEquals(animalSet2, Area.animals(meadowArea1, listcancelledAnimals2));
        assertEquals(Set.of(), Area.animals(meadowArea2, listcancelledAnimals1));
    }
    @Test
    void testPersoRiverSystemFishCount(){
        Zone.River river1 = new Zone.River(1, 2,null);
        Zone.River river2 = new Zone.River(2, 2,null );
        Zone.River river3 = new Zone.River(1, 0,null );
        Zone.Lake lake1 = new Zone.Lake(4, 5, Zone.SpecialPower.PIT_TRAP);

        Area<Zone.Water> area = new Area<>(Set.of(river1,river2 , river3) , List.of() , 2);
        Area<Zone.Water> area2 = new Area<>(Set.of(river1,river2 , river3 , lake1) , List.of() , 2);
        Area<Zone.Water> area3 = new Area<>(Set.of() , List.of() , 2);
        Area<Zone.Water> area4 = new Area<>(Set.of(river3) , List.of() , 2);


        assertEquals(4 , Area.riverSystemFishCount(area));
        assertEquals(9 , Area.riverSystemFishCount(area2));
        assertEquals(0 , Area.riverSystemFishCount(area3));
        assertEquals(0 , Area.riverSystemFishCount(area4));
    }
    @Test
    void testPersoLakeCountWorks(){
        Zone.River river1 = new Zone.River(1, 2,null);
        Zone.River river2 = new Zone.River(2, 2,null );
        Zone.River river3 = new Zone.River(1, 0,null );
        Zone.Lake lake1 = new Zone.Lake(4, 5, Zone.SpecialPower.PIT_TRAP);
        Zone.Lake lake2 = new Zone.Lake(5, 5, Zone.SpecialPower.PIT_TRAP);

        Area<Zone.Water> area = new Area<>(Set.of(river1,river2 , river3) , List.of() , 2);
        Area<Zone.Water> area2 = new Area<>(Set.of(river1,river2 , river3 , lake1) , List.of() , 2);
        Area<Zone.Water> area3 = new Area<>(Set.of() , List.of() , 2);
        Area<Zone.Water> area4 = new Area<>(Set.of(river3, lake1 , lake2) , List.of() , 2);

        assertEquals(0 , Area.lakeCount(area));
        assertEquals(1 , Area.lakeCount(area2));
        assertEquals(0 , Area.lakeCount(area3));
        assertEquals(2 , Area.lakeCount(area4));
    }
    @Test
    void testPersoIsClosedWorks(){
        Area<Zone.Water> area = new Area(Set.of() , List.of() , 1);
        Area<Zone.Water> area2 = new Area(Set.of() , List.of() , 0);

        assertTrue(area2.isClosed());
        assertFalse(area.isClosed());
    }
    @Test
    void testPersoIsOccupiedWorks(){
        Area<Zone.Water> area = new Area(Set.of() , List.of(PlayerColor.RED) , 1);
        Area<Zone.Water> area2 = new Area(Set.of() , List.of() , 0);

        assertTrue(area.isOccupied());
        assertFalse(area2.isOccupied());
    }
    @Test
    void testPersoMajorityOccupantsWorks(){
        List colorsBlue = new ArrayList(List.of(PlayerColor.RED, PlayerColor.BLUE , PlayerColor.BLUE , PlayerColor.YELLOW ,PlayerColor.PURPLE));
        List colorsBandP = new ArrayList(List.of(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.BLUE , PlayerColor.GREEN , PlayerColor.YELLOW ,PlayerColor.PURPLE , PlayerColor.PURPLE));

        Area<Zone.Water> area1 = new Area<>(Set.of() , colorsBlue , 5);
        Area<Zone.Forest> area2 = new Area<>(Set.of() , colorsBandP , 5 );
        Area<Zone.Water> area3 = new Area(Set.of() , List.of() , 0);

        assertEquals(Set.of(PlayerColor.BLUE) , area1.majorityOccupants());
        assertEquals(Set.of(PlayerColor.BLUE , PlayerColor.PURPLE) , area2.majorityOccupants());
        assertEquals(Set.of() , area3.majorityOccupants());
    }
    @Test
    void testPersoConnectToWorks(){
        List colors1 = new ArrayList(List.of(PlayerColor.RED, PlayerColor.BLUE ));
        List colors2 = new ArrayList(List.of(PlayerColor.RED, PlayerColor.GREEN));
        List bothColors = new ArrayList<>(List.of(PlayerColor.RED , PlayerColor.RED , PlayerColor.BLUE , PlayerColor.GREEN));

        Zone.Lake lake1 = new Zone.Lake(4, 5, Zone.SpecialPower.PIT_TRAP);
        Zone.River river1 = new Zone.River(1, 2,null);
        Zone.River river2 = new Zone.River(2, 2,null );
        Zone.River river3 = new Zone.River(1, 0,lake1);
        Zone.River river4 = new Zone.River(1, 7, lake1);

        Set<Zone.Water> zones1 = new HashSet(Set.of(river1 , river4));
        Set<Zone.Water> zones2 = new HashSet<>(Set.of(river2, river3));
        Set<Zone.Water> zones3 = new HashSet(Set.of(river1 , river3 , lake1));

        Area<Zone.Water> area1 = new Area<>(zones1 , colors1 , 10);
        Area<Zone.Water> area2 = new Area<>(zones2 , colors2 , 20);
        Area<Zone.Water> area3 = new Area<>(zones3 , List.of() , 30);


        assertEquals(area1.connectTo(area2) , area2.connectTo(area1) );
        assertEquals(area1.connectTo(area3) , area3.connectTo(area1) );
        assertEquals(area2.connectTo(area3) , area3.connectTo(area2) );

        assertEquals(new Area<>(Set.of(river1 , river2 , river3 , river4),bothColors,28 ) , area1.connectTo(area2));
        assertEquals(new Area<>(Set.of(river1 , river4 , river3 , lake1) , colors1 , 38) , area1.connectTo(area3));
        //assertEquals(new Area<>(zones1 , colors1 , 8), area1.connectTo(area1));

        assertEquals(new Area<>(Set.of(river1, river2 , river3 , lake1) , colors2 , 48) ,area2.connectTo(area3));

    }
    @Test
    void testPersoWithInitialOccupantWorks(){
        Zone.Forest forest = new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR);

        Area<Zone.Forest> area = new Area<>(Set.of(forest) , List.of() , 2);
        Area<Zone.Forest> area2 = new Area<>(Set.of() , List.of() , 2);

        assertEquals(new Area<>(Set.of(forest), List.of(PlayerColor.YELLOW),2 ) ,area.withInitialOccupant(PlayerColor.YELLOW));
        assertEquals(new Area<>(Set.of() , List.of(PlayerColor.PURPLE) , 2) , area2.withInitialOccupant(PlayerColor.PURPLE));
    }
    @Test
    void testPersoWithInitialOccupantThrows(){
        Zone.Forest forest = new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR);

        Area<Zone.Forest> area = new Area<>(Set.of(forest) , List.of(PlayerColor.PURPLE) , 2);
        Area<Zone.Forest> area2 = new Area<>(Set.of() , List.of(PlayerColor.BLUE) , 2);

        assertThrows(IllegalArgumentException.class , ()-> {area.withInitialOccupant(PlayerColor.YELLOW);});
        assertThrows(IllegalArgumentException.class , ()-> {area2.withInitialOccupant(PlayerColor.YELLOW);});

    }
    @Test
    void testPersoWithoutOccupantWorks(){
        Zone.Forest forest = new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR);

        Area<Zone.Forest> area = new Area<>(Set.of(forest) , List.of(PlayerColor.YELLOW) , 2);
        Area<Zone.Forest> area2 = new Area<>(Set.of() , List.of(PlayerColor.PURPLE , PlayerColor.BLUE) , 2);

        assertEquals(new Area<>(Set.of(forest), List.of(),2 ) ,area.withoutOccupant(PlayerColor.YELLOW));
        assertEquals(new Area<>(Set.of() , List.of(PlayerColor.BLUE) , 2) , area2.withoutOccupant(PlayerColor.PURPLE));
    }
    @Test
    void testPersoWithoutOccupantThrows(){
        Zone.Forest forest = new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR);

        Area<Zone.Forest> area = new Area<>(Set.of(forest) , List.of(PlayerColor.PURPLE) , 2);
        Area<Zone.Forest> area2 = new Area<>(Set.of() , List.of() , 2);

        assertThrows(IllegalArgumentException.class , ()-> {area.withoutOccupant(PlayerColor.YELLOW);});
        assertThrows(IllegalArgumentException.class , ()-> {area2.withoutOccupant(PlayerColor.YELLOW);});
    }

    @Test
    void testPersoWithoutOccupantsWorks(){
        Zone.Forest forest = new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR);

        Area<Zone.Forest> area = new Area<>(Set.of(forest) , List.of(PlayerColor.YELLOW, PlayerColor.RED) , 2);
        Area<Zone.Forest> area2 = new Area<>(Set.of() , List.of(PlayerColor.PURPLE) , 2);

        assertEquals(new Area<>(Set.of(forest), List.of(),2 ) ,area.withoutOccupants());
        assertEquals(new Area<>(Set.of() , List.of() , 2) , area2.withoutOccupants());
    }

    @Test
    void tileIds() {
        Set<Integer> expected = Set.of(43, 1, 35,25, 13 );
        Zone.Lake lake1 = new Zone.Lake(434, 5, Zone.SpecialPower.PIT_TRAP);
        Zone.River river1 = new Zone.River(255, 2,null);
        Zone.River river2 = new Zone.River(358, 2,null );
        Zone.Meadow meadow1 = new Zone.Meadow(134 , List.of(), Zone.SpecialPower.PIT_TRAP);
        Zone.Meadow meadow2 = new Zone.Meadow(10 , List.of(), Zone.SpecialPower.PIT_TRAP);
        Area<Zone> area = new Area<>(Set.of(river1, lake1, river2, meadow2, meadow1), List.of(), 0);
        assertEquals(expected, area.tileIds());
    }





    }


