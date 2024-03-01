package ch.epfl.chacun;
/*
 *	Author:      Jakub Kliment
 *	Date:
 */

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

public class TileTest {
    private List<Animal> animalList = new ArrayList<>();
    private List<Animal> animalList1 = new ArrayList<>();
    private List<Animal> animalList2 = new ArrayList<>();


    private TileSide forestA = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN));
    private TileSide forestD = new TileSide.Forest(new Zone.Forest(6, Zone.Forest.Kind.PLAIN));
    private TileSide meadowB = new TileSide.Meadow(new Zone.Meadow(1,animalList, Zone.SpecialPower.HUNTING_TRAP));
    private TileSide meadowC = new TileSide.Meadow(new Zone.Meadow(100,animalList, Zone.SpecialPower.SHAMAN));
    private TileSide riverC = new TileSide.River(new Zone.Meadow(2,animalList1, Zone.SpecialPower.PIT_TRAP),
            new Zone.River(3,13,new Zone.Lake(4,7, Zone.SpecialPower.LOGBOAT))
            ,new Zone.Meadow(5,animalList2, Zone.SpecialPower.WILD_FIRE));
    private TileSide riverE = new TileSide.River(new Zone.Meadow(7,animalList1, Zone.SpecialPower.PIT_TRAP), new
            Zone.River(8,13,null) ,new Zone.Meadow(10,animalList2, Zone.SpecialPower.WILD_FIRE));
    private TileSide.Forest forestSide = new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.PLAIN));

    private TileSide.Meadow meadowSide = new TileSide.Meadow(new Zone.Meadow(1, new ArrayList<>(),
            Zone.SpecialPower.HUNTING_TRAP));
    private TileSide.River riverSide = new TileSide.River(new Zone.Meadow(2,new ArrayList<>(),
            Zone.SpecialPower.LOGBOAT), new Zone.River(3,4,new Zone.Lake(1,3,
            Zone.SpecialPower.LOGBOAT)), new Zone.Meadow(2,new ArrayList<>(), Zone.SpecialPower.SHAMAN));
    private Zone.Lake lake = new Zone.Lake(2,3,null);

    @Test
    void zoneMethodForForestWorksCorrectly(){
        assertTrue(forestA.zones().get(0) instanceof Zone.Forest);
    }
    @Test
    void zoneMethodForMeadowWorksCorrectly(){
        assertTrue(meadowB.zones().get(0) instanceof Zone.Meadow);
    }
    @Test
    void zoneMethodForRiverWorksCorrectly(){
        assertTrue(riverC.zones().get(0) instanceof Zone.Meadow);
        assertTrue(riverC.zones().get(1) instanceof Zone.River);
        assertTrue(riverC.zones().get(2) instanceof Zone.Meadow);

    }
    @Test
    void isSameKindAsMethodWorks(){
        assertTrue(forestA.isSameKindAs(forestD));
        assertFalse(forestA.isSameKindAs(riverC));
        assertTrue(riverC.isSameKindAs(riverE));
        assertFalse(riverE.isSameKindAs(meadowB));
    }
    @Test
    void listSidesWorks(){
        Tile tileTest = new Tile(11, Tile.Kind.NORMAL,riverC,meadowB,riverE,forestD);
        assertEquals(4,tileTest.sides().size());
        assertEquals(riverC,tileTest.sides().get(0));
        assertEquals(meadowB,tileTest.sides().get(1));
        assertEquals(riverE,tileTest.sides().get(2));
        assertEquals(forestD,tileTest.sides().get(3));
    }
    @Test
    void worksForest(){
        TileSide forest1 = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN));
        TileSide forest2 = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN));
        assertTrue(forest1.isSameKindAs(forest2));

    }

    @Test
    void worksMeadow(){
        List<Animal> animalList1 = new ArrayList<>();
        animalList1.add(new Animal(0, Animal.Kind.MAMMOTH));
        List<Animal> animalList2 = new ArrayList<>();
        animalList2.add(new Animal(0, Animal.Kind.TIGER));
        TileSide meadow1 = new TileSide.Meadow(new Zone.Meadow(0,animalList1, Zone.SpecialPower.SHAMAN));
        TileSide meadow2 = new TileSide.Meadow(new Zone.Meadow(0,animalList2, Zone.SpecialPower.SHAMAN));
        assertTrue(meadow1.isSameKindAs(meadow2));
    }

    @Test
    void worksRiver(){
        List<Animal> animalList1 = new ArrayList<>();
        animalList1.add(new Animal(0, Animal.Kind.MAMMOTH));
        List<Animal> animalList2 = new ArrayList<>();
        animalList2.add(new Animal(0, Animal.Kind.TIGER));
        List<Animal> animalList3 = new ArrayList<>();
        animalList2.add(new Animal(0, Animal.Kind.TIGER));
        Zone.Meadow meadow1 = new Zone.Meadow(0,animalList1, Zone.SpecialPower.SHAMAN);
        Zone.Meadow meadow2 = new Zone.Meadow(0,animalList2, Zone.SpecialPower.SHAMAN);
        Zone.Meadow meadow3 = new Zone.Meadow(0,animalList3, Zone.SpecialPower.SHAMAN);
        Zone.Lake lake = new Zone.Lake(0,0, Zone.SpecialPower.SHAMAN);
        Zone.River river = new Zone.River(0,0,lake);

        TileSide river1 = new TileSide.River(meadow1,river,meadow2);
        TileSide river2 = new TileSide.River(meadow1 , river,meadow3);

        assertTrue(river1.isSameKindAs(river2));
    }
    @Test
    public void testId() {
        // Étant donné une tuile placée
        TileSide side = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN));
        TileSide side2 = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR));
        TileSide side3 = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.WITH_MUSHROOMS));
        TileSide side4 = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN));
        PlacedTile placedTile = new PlacedTile(new Tile(1, Tile.Kind.NORMAL, side,side2,side3,side4),
                PlayerColor.RED,Rotation.NONE,new Pos(0, 0),new Occupant(Occupant.Kind.PAWN,0));

        // Le test vérifie que l'identifiant de la tuile est correct
        assertEquals(1, placedTile.id());
    }

    @Test
    public void testKind() {
        // Étant donné une tuile placée
        TileSide side = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN));
        TileSide side2 = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR));
        TileSide side3 = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.WITH_MUSHROOMS));
        TileSide side4 = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN));
        PlacedTile placedTile = new PlacedTile(new Tile(1, Tile.Kind.NORMAL, side,side2,side3,side4),PlayerColor.RED
                ,Rotation.NONE,new Pos(0, 0),new Occupant(Occupant.Kind.PAWN,0));

        // Le test vérifie que la sorte de la tuile est correcte
        assertEquals(Tile.Kind.NORMAL, placedTile.kind());
    }


    @Test
    public void testSide() {
        // Étant donné une tuile placée
        TileSide side = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN));
        TileSide side2 = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR));
        TileSide side3 = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.WITH_MUSHROOMS));
        TileSide side4 = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN));
        PlacedTile placedTile = new PlacedTile(new Tile(1, Tile.Kind.NORMAL, side,side2,side3,side4),PlayerColor.RED
                ,Rotation.NONE,new Pos(0, 0),new Occupant(Occupant.Kind.PAWN,0));


        // Le test vérifie que le côté de la tuile dans une direction donnée est correct
        assertEquals(side,placedTile.side(Direction.N)
        );
    }


    @Test
    public void testZoneWithId() {
        // Étant donné une tuile placée
        Zone.Forest forest = new Zone.Forest(0, Zone.Forest.Kind.PLAIN);
        TileSide side = new TileSide.Forest(forest);
        TileSide side2 = new TileSide.Forest(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR));
        TileSide side3 = new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS));
        TileSide side4 = new TileSide.Forest(new Zone.Forest(3, Zone.Forest.Kind.PLAIN));
        PlacedTile placedTile = new PlacedTile(new Tile(1, Tile.Kind.NORMAL, side,side2,side3,side4),PlayerColor.RED
                ,Rotation.NONE,new Pos(0, 0),new Occupant(Occupant.Kind.PAWN,0));


        // Le test vérifie que la zone de la tuile avec un identifiant donné est correcte
        assertEquals(forest,placedTile.zoneWithId(0));

        // Le test vérifie qu'une exception est levée si l'identifiant de la zone est invalide
        assertThrows(IllegalArgumentException.class, () -> placedTile.zoneWithId(56));
    }


    @Test
    public void testSpecialPowerZone() {

        TileSide side = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN));
        TileSide side2 = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.WITH_MENHIR));
        TileSide side3 = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.WITH_MUSHROOMS));
        TileSide side4 = new TileSide.Forest(new Zone.Forest(0, Zone.Forest.Kind.PLAIN));
        PlacedTile placedTile = new PlacedTile(new Tile(1, Tile.Kind.NORMAL, side,side2,side3,side4),PlayerColor.RED
                ,Rotation.NONE,new Pos(0, 0),new Occupant(Occupant.Kind.PAWN,0));


        assertNull(placedTile.specialPowerZone());

    }




    @Test
    public void testSideZones() {
        // Create instances of different TileSide implementations

        TileSide.Forest forestSide = new TileSide.Forest(new Zone.Forest(2, Zone.Forest.Kind.PLAIN));
        TileSide.Meadow meadowSide = new TileSide.Meadow(new Zone.Meadow(1, new ArrayList<>(),
                Zone.SpecialPower.HUNTING_TRAP));
        TileSide.River riverSide = new TileSide.River(new Zone.Meadow(2,new ArrayList<>(),
                Zone.SpecialPower.LOGBOAT), new Zone.River(3,4,new Zone.Lake(1,
                3, Zone.SpecialPower.LOGBOAT)),
                new Zone.Meadow(2,new ArrayList<>(), Zone.SpecialPower.SHAMAN));

        // Create a Tile object with the specified sides
        Tile tile = new Tile(1, Tile.Kind.NORMAL, forestSide, meadowSide, riverSide, meadowSide);

        // Calculate the expected side zones
        Set<Zone> expectedSideZones = new HashSet<>();
        expectedSideZones.addAll(forestSide.zones());
        expectedSideZones.addAll(meadowSide.zones());
        expectedSideZones.addAll(riverSide.zones());
        expectedSideZones.addAll(meadowSide.zones());

        // Call the sideZones() method
        Set<Zone> actualSideZones = tile.sideZones();

        // Check if the returned set matches the expected side zones
        assertEquals(expectedSideZones, actualSideZones);
    }

    @Test
    public void testSideZones_SameZonesOnAllSides() {
        Tile tile = new Tile(1, Tile.Kind.NORMAL, forestSide, forestSide, forestSide, forestSide);
        Set<Zone> expected = new HashSet<>(forestSide.zones());
        Set<Zone> actual = tile.sideZones();
        assertEquals(expected, actual);
    }

    @Test
    public void testSideZones_DifferentZonesOnSides() {

        Tile tile = new Tile(1, Tile.Kind.NORMAL, forestSide, meadowSide, riverSide, forestSide);

        Set<Zone> expected = new HashSet<>();
        expected.addAll(forestSide.zones());
        expected.addAll(meadowSide.zones());
        expected.addAll(riverSide.zones());

        Set<Zone> actual = tile.sideZones();
        assertEquals(expected, actual);
    }
    @Test
    public void testZonesWithAndWithoutLake() {
        // Setup pour créer des zones et des côtés de tuile
        Zone.Forest forest = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Meadow meadow = new Zone.Meadow(2, new ArrayList<>(), Zone.SpecialPower.SHAMAN);
        Zone.Meadow meadow1 = new Zone.Meadow(2, new ArrayList<>(), Zone.SpecialPower.WILD_FIRE);
        Zone.Meadow meadow2 = new Zone.Meadow(2, new ArrayList<>(), Zone.SpecialPower.HUNTING_TRAP);
        Zone.River riverWithoutLake = new Zone.River(3, 10, null); // Pas de lac
        Zone.Lake lake = new Zone.Lake(4, 5, Zone.SpecialPower.LOGBOAT);
        Zone.River riverWithLake = new Zone.River(5, 15, lake); // Avec lac

        TileSide forestSide = new TileSide.Forest(forest);
        TileSide meadowSide = new TileSide.Meadow(meadow);
        TileSide riverSideWithoutLake = new TileSide.River(meadow1,riverWithoutLake,meadow);
        TileSide riverSideWithLake = new TileSide.River(meadow,riverWithLake,meadow2);

        Tile tile = new Tile(1, Tile.Kind.NORMAL, forestSide, meadowSide, riverSideWithoutLake, riverSideWithLake);

        // Action : Appel de la méthode zones()
        Set<Zone> actualZones = tile.zones();

        // Vérification que toutes les zones, y compris celle avec lac, sont incluses
        assertTrue(actualZones.contains(forest), "The forest zone should be included.");
        assertTrue(actualZones.contains(meadow), "The meadow zone should be included.");
        assertTrue(actualZones.contains(riverWithoutLake), "The river without lake should be included.");
        assertTrue(actualZones.contains(riverWithLake), "The river with lake should be included.");
    }
    @Test
    public void testPlacedTileConstructorValidation() {
        Tile tile = new Tile(1, Tile.Kind.NORMAL, null, null, null, null);
        // Supposition sur la signature du constructeur
        Rotation rotation = Rotation.NONE; // Supposition sur l'existence de l'énumération
        Pos pos = new Pos(0, 0); // Supposition sur la signature du constructeur

        assertThrows(NullPointerException.class, () -> new PlacedTile(null, PlayerColor.RED, rotation, pos));
        assertThrows(NullPointerException.class, () -> new PlacedTile(tile, PlayerColor.RED, null, pos));
        assertThrows(NullPointerException.class, () -> new PlacedTile(tile, PlayerColor.RED, rotation, null));
    }
    @Test
    public void testIdAndKindMethods() {
        Tile tile = new Tile(1, Tile.Kind.NORMAL, null, null, null, null);
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(0, 0));

        assertEquals(1, placedTile.id());
        assertEquals(Tile.Kind.NORMAL, placedTile.kind());
    }
    @Test
    public void testOccupantManipulation() {
        PlacedTile placedTile = new PlacedTile( new Tile(1, Tile.Kind.NORMAL, null, null, null, null),
                PlayerColor.RED,Rotation.RIGHT,new Pos(2,4)); // Initialisation avec les arguments nécessaires
        Occupant occupant = new Occupant(Occupant.Kind.PAWN, 0);

        PlacedTile occupiedTile = placedTile.withOccupant(occupant);
        assertNotNull(occupiedTile.occupant());
        assertEquals(occupant, occupiedTile.occupant());

        PlacedTile tileWithoutOccupant = occupiedTile.withNoOccupant();
        assertNull(tileWithoutOccupant.occupant());

        assertThrows(IllegalArgumentException.class, () -> occupiedTile.withOccupant(new Occupant(Occupant.Kind.HUT,
                1)));
    }
    @Test
    public void testSideWithRotation() {
        // Initialisation des zones pour les côtés de la tuile
        Zone.Forest forestZone = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        Zone.Meadow meadowZone = new Zone.Meadow(2, List.of(), Zone.SpecialPower.SHAMAN);
        Zone.River riverZone = new Zone.River(3, 10, null);

        // Création des côtés de la tuile avec les zones correspondantes
        TileSide northSide = new TileSide.Forest(forestZone);
        TileSide eastSide = new TileSide.Meadow(meadowZone);
        TileSide southSide = new TileSide.River(meadowZone, riverZone, meadowZone);
        TileSide westSide = new TileSide.Forest(forestZone);

        // Création d'une tuile et placement avec une rotation
        Tile tile = new Tile(1, Tile.Kind.NORMAL, northSide, eastSide, southSide, westSide);
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.RIGHT, new Pos(0, 0));

        // Après une rotation à droite, le côté nord devrait maintenant correspondre à l'ancien côté ouest
        TileSide actualNorthSide = placedTile.side(Direction.S.opposite());

        assertTrue(actualNorthSide.isSameKindAs(westSide), "After a right rotation, " +
                "the north side should be the same kind as the original west side.");
    }
    @Test
    public void testForestZones() {
        Zone.Forest forest1 = new Zone.Forest(20, Zone.Forest.Kind.PLAIN);
        Zone.Forest forest2 = new Zone.Forest(21, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest3 = new Zone.Forest(29, Zone.Forest.Kind.WITH_MUSHROOMS);
        Zone.Forest forest4 = new Zone.Forest(210, Zone.Forest.Kind.WITH_MUSHROOMS);
        TileSide forestSide1 = new TileSide.Forest(forest1);
        TileSide forestSide2 = new TileSide.Forest(forest2);
        TileSide forestSide3 = new TileSide.Forest(forest3);
        TileSide forestSide4 = new TileSide.Forest(forest4);

        Tile tile = new Tile(3, Tile.Kind.NORMAL, forestSide1, forestSide2, forestSide3, forestSide4);
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(2, 2));

        Set<Zone.Forest> expectedForests = Set.of(forest1, forest2,forest3,forest4);
        assertEquals(expectedForests, placedTile.forestZones(), "Should return all forest zones");
    }
    @Test
    public void testPotentialOccupants() {
        // Création de zones stubs pour éviter les null
        Zone.Forest forestStub = new Zone.Forest(100, Zone.Forest.Kind.PLAIN); // Stub pour éviter null
        TileSide forestSideStub = new TileSide.Forest(forestStub);

        // Utilisation d'une zone Meadow avec un pouvoir spécial pour le test
        Zone.Meadow meadow = new Zone.Meadow(50, List.of(), Zone.SpecialPower.SHAMAN);
        TileSide meadowSide = new TileSide.Meadow(meadow);

        // Initialisation de la tuile avec des côtés non null
        Tile tile = new Tile(6, Tile.Kind.NORMAL, meadowSide, forestSideStub, forestSideStub, forestSideStub);
        PlacedTile placedTile = new PlacedTile(tile, null, Rotation.NONE, new Pos(5, 5)); // Tuile de départ, placer est null

        assertTrue(placedTile.potentialOccupants().isEmpty(), "Should return an empty set for the starting tile without occupants");

        // Création d'une tuile placée avec un placeur non null pour tester la liste des occupants potentiels
        PlacedTile occupiedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(5, 5));
        // Le comportement exact de potentialOccupants() dépend de votre implémentation, ajustez cette partie du test en conséquence
        assertFalse(occupiedTile.potentialOccupants().isEmpty(), "Should not return an empty set for a tile with a placer");
    }
    @Test
    public void testMeadowZones() {
        Zone.Meadow meadow1 = new Zone.Meadow(30, List.of(), Zone.SpecialPower.HUNTING_TRAP);
        Zone.Meadow meadow2 = new Zone.Meadow(31, List.of(), Zone.SpecialPower.PIT_TRAP);
        Zone.Meadow meadow3 = new Zone.Meadow(32, List.of(), Zone.SpecialPower.WILD_FIRE);
        Zone.Meadow meadow4 = new Zone.Meadow(33, List.of(), Zone.SpecialPower.SHAMAN);
        TileSide meadowSide1 = new TileSide.Meadow(meadow1);
        TileSide meadowSide2 = new TileSide.Meadow(meadow2);
        TileSide meadowSide3 = new TileSide.Meadow(meadow3);
        TileSide meadowSide4 = new TileSide.Meadow(meadow4);
        Tile tile = new Tile(4, Tile.Kind.NORMAL, meadowSide1, meadowSide2, meadowSide3, meadowSide4);
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(3, 3));

        Set<Zone.Meadow> expectedMeadows = Set.of(meadow1, meadow2,meadow3,meadow4 );
        assertEquals(expectedMeadows, placedTile.meadowZones(), "Should return all meadow zones");
    }
    @Test
    public void testRiverZones() {
        Zone.River river1 = new Zone.River(40, 5, new Zone.Lake(29,2, Zone.SpecialPower.RAFT));
        Zone.River river2 = new Zone.River(41, 10, new Zone.Lake(42, 15, Zone.SpecialPower.LOGBOAT));
        Zone.River river3 = new Zone.River(90, 5,new Zone.Lake(42, 15, Zone.SpecialPower.LOGBOAT) );
        Zone.River river4 = new Zone.River(23, 10, new Zone.Lake(49, 15, Zone.SpecialPower.LOGBOAT));
        Zone.Meadow meadow1 = new Zone.Meadow(30, List.of(), Zone.SpecialPower.HUNTING_TRAP);
        Zone.Meadow meadow2 = new Zone.Meadow(31, List.of(), Zone.SpecialPower.PIT_TRAP);
        Zone.Meadow meadow3 = new Zone.Meadow(32, List.of(), Zone.SpecialPower.WILD_FIRE);
        Zone.Meadow meadow4 = new Zone.Meadow(33, List.of(), Zone.SpecialPower.SHAMAN);
        TileSide riverSide1 = new TileSide.River(meadow1, river1, meadow2);
        TileSide riverSide2 = new TileSide.River(meadow3, river2, meadow4);
        TileSide riverSide3 = new TileSide.River(meadow2, river3, meadow1);
        TileSide riverSide4 = new TileSide.River(meadow4, river4, meadow3);
        Tile tile = new Tile(5, Tile.Kind.NORMAL, riverSide1, riverSide2,riverSide3,riverSide4);
        PlacedTile placedTile = new PlacedTile(tile, PlayerColor.RED, Rotation.NONE, new Pos(4, 4));

        Set<Zone.River> expectedRivers = Set.of(river1, river2,river3,river4);
        assertEquals(expectedRivers, placedTile.riverZones(), "Should return all river zones including one with a lake");
    }
    @Test
    public void testDeckSize() {
        List<Tile> startTiles = List.of(new Tile(1, Tile.Kind.START, null, null, null, null));
        List<Tile> normalTiles = List.of(new Tile(2, Tile.Kind.NORMAL, null, null, null, null),
                new Tile(3, Tile.Kind.NORMAL, null, null, null, null));
        List<Tile> menhirTiles = List.of(new Tile(4, Tile.Kind.MENHIR, null, null, null, null));

        TileDecks decks = new TileDecks(startTiles, normalTiles, menhirTiles);

        assertEquals(1, decks.deckSize(Tile.Kind.START), "Start deck size should be 1");
        assertEquals(2, decks.deckSize(Tile.Kind.NORMAL), "Normal deck size should be 2");
        assertEquals(1, decks.deckSize(Tile.Kind.MENHIR), "Menhir deck size should be 1");
    }
    @Test
    public void testTopTile() {
        Tile startTile = new Tile(1, Tile.Kind.START, null, null, null, null);
        Tile normalTile = new Tile(2, Tile.Kind.NORMAL, null, null, null, null);
        Tile menhirTile = new Tile(4, Tile.Kind.MENHIR, null, null, null, null);

        TileDecks decks = new TileDecks(List.of(startTile), List.of(normalTile), List.of(menhirTile));

        assertEquals(startTile, decks.topTile(Tile.Kind.START), "Top start tile should match");
        assertEquals(normalTile, decks.topTile(Tile.Kind.NORMAL), "Top normal tile should match");
        assertEquals(menhirTile, decks.topTile(Tile.Kind.MENHIR), "Top menhir tile should match");

    }
    @Test
    public void testWithTopTileDrawn() {
        Tile startTile = new Tile(1, Tile.Kind.START, null, null, null, null);
        Tile normalTile1 = new Tile(2, Tile.Kind.NORMAL, null, null, null, null);
        Tile normalTile2 = new Tile(3, Tile.Kind.NORMAL, null, null, null, null);

        TileDecks decks = new TileDecks(List.of(startTile), List.of(normalTile1, normalTile2), List.of());

        TileDecks newDecks = decks.withTopTileDrawn(Tile.Kind.NORMAL);
        assertEquals(normalTile2, newDecks.topTile(Tile.Kind.NORMAL), "After drawing top normal tile, next tile should be on top");
        assertEquals(1, newDecks.deckSize(Tile.Kind.NORMAL), "Normal deck size should decrease by 1 after drawing");

        assertThrows(IllegalArgumentException.class, () -> newDecks.withTopTileDrawn(Tile.Kind.MENHIR), "Drawing from an empty deck should throw IllegalArgumentException");
    }
    @Test
    public void testWithTopTileDrawnUntil() {
        Tile normalTile1 = new Tile(2, Tile.Kind.NORMAL, null, null, null, null);
        Tile normalTile2 = new Tile(3, Tile.Kind.NORMAL, null, null, null, null);
        Tile normalTile3 = new Tile(4, Tile.Kind.NORMAL, null, null, null, null);
        Predicate<Tile> predicate = tile -> tile.id() == 4; // Test returns true only for tile with id 3

        TileDecks decks = new TileDecks(List.of(), List.of(normalTile1, normalTile2, normalTile3), List.of());
        TileDecks newDecks = decks.withTopTileDrawnUntil(Tile.Kind.NORMAL, predicate);

        assertEquals(1, newDecks.deckSize(Tile.Kind.NORMAL), "Should remove tiles until predicate is true");
        assertEquals(normalTile3, newDecks.topTile(Tile.Kind.NORMAL), "Tile matching predicate should be on top");
    }
}