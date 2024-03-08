package ch.epfl.chacun;



import ch.epfl.chacun.Animal;
import ch.epfl.chacun.Area;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.Zone;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.util.*;

import static ch.epfl.chacun.Animal.Kind.DEER;
import static ch.epfl.chacun.Area.*;
import static ch.epfl.chacun.Area.lakeCount;
import static ch.epfl.chacun.PlayerColor.*;
import static ch.epfl.chacun.Zone.SpecialPower.SHAMAN;
import static org.junit.jupiter.api.Assertions.*;
class MyAreaTest3 {
    @Test
    public void testAreaConstructorSortsOccupants() {
        Set<Zone> zones = Set.of();
        List<PlayerColor> unsortedOccupants = List.of(BLUE, RED, GREEN,PlayerColor.PURPLE,PlayerColor.YELLOW);
        List<PlayerColor> unsorted = List.of(BLUE, RED, RED,PlayerColor.PURPLE, GREEN);
        Area<Zone> area = new Area<>(zones, unsortedOccupants, 0);
        Area<Zone> area1 = new Area<>(zones, unsorted,0);
        assertEquals(List.of(RED,RED,BLUE,GREEN,PURPLE),area1.occupants(),"guez");
        assertEquals(List.of(RED, BLUE, GREEN,PlayerColor.YELLOW,PlayerColor.PURPLE), area.occupants(), "c'est mal trié mgl");
    }
    @Test
    public void testHasMenhirWithEmptyForest() {
        Area<Zone.Forest> emptyForest = new Area<>(Collections.emptySet(), Collections.emptyList(), 0);
        assertFalse(Area.hasMenhir(emptyForest), "ya pas de zone ");
    }
    @Test
    public void testHasMenhirWithForestWithoutMenhirs() {
        Set<Zone.Forest> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Forest(2, Zone.Forest.Kind.PLAIN));
        Area<Zone.Forest> forestWithoutMenhirs = new Area<>(zones, Collections.emptyList(), 0);
        assertFalse(Area.hasMenhir(forestWithoutMenhirs), "ya pas de menhir dans une foret mgl");
    }
    @Test
    public void testHasMenhirWithForestContainingMenhir() {
        Set<Zone.Forest> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR));
        Area<Zone.Forest> forestWithMenhir = new Area<>(zones, Collections.emptyList(), 0);
        assertTrue(Area.hasMenhir(forestWithMenhir), "Doit retourner true pour une forêt contenant au moins un menhir.");
    }
    @Test
    public void testHasMenhirWithMixedZones() {
        Set<Zone.Forest> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR), new Zone.Forest(2, Zone.Forest.Kind.PLAIN));
        Area<Zone.Forest> mixedForest = new Area<>(zones, Collections.emptyList(), 0);
        assertTrue(Area.hasMenhir(mixedForest), "Doit retourner true pour une forêt avec des zones mixtes, incluant au moins un menhir.");
    }
    @Test
    public void testHasMenhirWithMultipleMenhirZones() {
        Set<Zone.Forest> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR), new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR));
        Area<Zone.Forest> forestWithMultipleMenhirs = new Area<>(zones, Collections.emptyList(), 0);
        assertTrue(Area.hasMenhir(forestWithMultipleMenhirs), "Doit retourner true pour une forêt avec plusieurs zones de menhirs.");
    }
    @Test
    public void testHasMenhirWithForestContainingMenhirAndOtherFeatures() {
        Set<Zone.Forest> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR), new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS));
        Area<Zone.Forest> forestWithMenhirAndOtherFeatures = new Area<>(zones, Collections.emptyList(), 0);
        assertTrue(Area.hasMenhir(forestWithMenhirAndOtherFeatures), "Doit retourner true pour une forêt contenant des menhirs, même avec d'autres caractéristiques.");
    }
    @Test
    public void testHasMenhirWithManyNonMenhirZonesBeforeMenhirZone() {
        Set<Zone.Forest> zones = new HashSet<>();
        for (int i = 1; i <= 100; i++) {
            zones.add(new Zone.Forest(i, Zone.Forest.Kind.PLAIN));
        }
        zones.add(new Zone.Forest(101, Zone.Forest.Kind.WITH_MENHIR));
        Area<Zone.Forest> largeForest = new Area<>(zones, Collections.emptyList(), 0);
        assertTrue(Area.hasMenhir(largeForest), "Doit retourner true même si de nombreuses zones sans menhirs précèdent une zone avec menhir.");
    }
    @Test
    public void testHasMenhirWithManyNonMenhirZonesBefoeMenhirZone() {
        Set<Zone.Forest> zones = new HashSet<>();
        for (int i = 1; i <= 100; i++) {
            zones.add(new Zone.Forest(i, Zone.Forest.Kind.PLAIN));
        }
        Area<Zone.Forest> largeForest = new Area<>(zones, Collections.emptyList(), 0);
        assertFalse(Area.hasMenhir(largeForest), "ya aucune zone avec menhir parmi les 100");
    }
    @Test
    public void testHasMenhirWithAllZonesContainingMenhirs() {
        Set<Zone.Forest> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MENHIR), new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR));
        Area<Zone.Forest> forestAllMenhirs = new Area<>(zones, Collections.emptyList(), 0);
        assertTrue(Area.hasMenhir(forestAllMenhirs), "Doit retourner true pour une forêt où toutes les zones contiennent des menhirs.");
    }

    @Test
    public void testMushroomGroupCountWithEmptyForest() {
        Area<Zone.Forest> emptyForest = new Area<>(Collections.emptySet(), Collections.emptyList(), 0);
        assertEquals(0, Area.mushroomGroupCount(emptyForest), "Doit retourner 0 pour une forêt sans zones.");
    }
    @Test
    public void testMushroomGroupCountWithForestWithoutMushrooms() {
        Set<Zone.Forest> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.PLAIN), new Zone.Forest(2, Zone.Forest.Kind.WITH_MENHIR));
        Area<Zone.Forest> forestWithoutMushrooms = new Area<>(zones, Collections.emptyList(), 0);
        assertEquals(0, Area.mushroomGroupCount(forestWithoutMushrooms), "Doit retourner 0 pour une forêt sans groupes de champignons.");
    }
    @Test
    public void testMushroomGroupCountWithSingleMushroomGroup() {
        Set<Zone.Forest> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MUSHROOMS));
        Area<Zone.Forest> forestWithOneMushroomGroup = new Area<>(zones, Collections.emptyList(), 0);
        assertEquals(1, Area.mushroomGroupCount(forestWithOneMushroomGroup), "Doit retourner 1 pour une forêt contenant un seul groupe de champignons.");
    }
    @Test
    public void testMushroomGroupCountWithMultipleMushroomGroups() {
        Set<Zone.Forest> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MUSHROOMS), new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS));
        Area<Zone.Forest> forestWithMultipleMushroomGroups = new Area<>(zones, Collections.emptyList(), 0);
        assertEquals(2, Area.mushroomGroupCount(forestWithMultipleMushroomGroups), "Doit retourner le nombre correct de groupes de champignons pour une forêt avec plusieurs groupes.");
    }
    @Test
    public void testMushroomGroupCountWithMixedZones() {
        Set<Zone.Forest> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MUSHROOMS), new Zone.Forest(2, Zone.Forest.Kind.PLAIN));
        Area<Zone.Forest> mixedForest = new Area<>(zones, Collections.emptyList(), 0);
        assertEquals(1, Area.mushroomGroupCount(mixedForest), "Doit retourner le nombre correct de groupes de champignons pour une forêt avec des zones mixtes.");
    }
    @Test
    public void testMushroomGroupCountWithNullZones() {
        Set<Zone.Forest> zones = Collections.singleton(new Zone.Forest(10, Zone.Forest.Kind.WITH_MUSHROOMS));
        Area<Zone.Forest> forestWithNullZones = new Area<>(zones, Collections.emptyList(), 0);
        assertEquals(1, Area.mushroomGroupCount(forestWithNullZones), "Doit gérer correctement les zones nulles et compter les groupes de champignons existants.");
    }
    @Test
    public void testMushroomGroupCountWithAdjacentMushroomGroups() {
        Set<Zone.Forest> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MUSHROOMS), new Zone.Forest(2, Zone.Forest.Kind.WITH_MUSHROOMS));
        Area<Zone.Forest> forestWithAdjacentMushroomGroups = new Area<>(zones, Collections.emptyList(), 0);
        assertEquals(2, Area.mushroomGroupCount(forestWithAdjacentMushroomGroups), "Devrait compter chaque groupe de champignons séparément, même s'ils sont adjacents.");
    }
    @Test
    public void testMushroomGroupCountWithInterleavedZones() {
        Set<Zone.Forest> zones = Set.of(new Zone.Forest(1, Zone.Forest.Kind.WITH_MUSHROOMS), new Zone.Forest(2, Zone.Forest.Kind.PLAIN), new Zone.Forest(3, Zone.Forest.Kind.WITH_MUSHROOMS));
        Area<Zone.Forest> forestWithInterleavedZones = new Area<>(zones, Collections.emptyList(), 0);
        assertEquals(2, Area.mushroomGroupCount(forestWithInterleavedZones), "Devrait compter chaque groupe de champignons séparément, même s'ils sont entrecoupés de zones sans champignons.");
    }
    @Test
    public void testMushroomGroupCountWithLargeForest() {
        Set<Zone.Forest> zones = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            zones.add(new Zone.Forest(i, Zone.Forest.Kind.WITH_MUSHROOMS));
        }
        Area<Zone.Forest> largeForest = new Area<>(zones, Collections.emptyList(), 0);
        assertEquals(10000, Area.mushroomGroupCount(largeForest), "Devrait pouvoir gérer une grande quantité de groupes de champignons sans problème de performance.");
    }
    @Test
    public void testMushroomGroupCountWithLargeNumberOfNonMushroomZones() {
        Set<Zone.Forest> zones = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            zones.add(new Zone.Forest(i, Zone.Forest.Kind.PLAIN));
        }
        Area<Zone.Forest> forest = new Area<>(zones, Collections.emptyList(), 0);
        assertEquals(0, Area.mushroomGroupCount(forest), "Doit gérer efficacement un grand nombre de zones sans ralentir, retournant 0 si aucune n'a de champignons.");
    }
    @Test
    public void testMushroomGroupCountWithDuplicateZones() {
        Zone.Forest mushroomZone = new Zone.Forest(1, Zone.Forest.Kind.WITH_MUSHROOMS);
        Set<Zone.Forest> zones = new HashSet<>(Arrays.asList(mushroomZone, mushroomZone)); // Dupliqué intentionnellement
        Area<Zone.Forest> forest = new Area<>(zones, Collections.emptyList(), 0);
        assertEquals(1, Area.mushroomGroupCount(forest), "Doit compter chaque zone unique avec des champignons une seule fois, même en cas de duplication.");
    }
    @Test
    public void testAnimalsWithEmptyMeadow() {
        Area<Zone.Meadow> emptyMeadow = new Area<>(Collections.emptySet(), Collections.emptyList(), 0);
        Set<Animal> cancelledAnimals = Collections.emptySet();
        assertTrue(Area.animals(emptyMeadow, cancelledAnimals).isEmpty(), "Doit retourner un ensemble vide pour une prairie sans zones.");
    }
    @Test
    public void testMajorityOccupantsIdentifiesCorrectMajority() {
        Set<Zone> zones = Set.of();
        List<PlayerColor> occupants = List.of(RED, RED, GREEN, GREEN, GREEN, BLUE);
        Area<Zone> area = new Area<>(zones, occupants, 0);

        Set<PlayerColor> majority = area.majorityOccupants();
        assertEquals(Set.of(GREEN), majority, "GREEN devrait être la couleur majoritaire");
    }

    @Test
    public void testRiverFishCountWithNoRiversOrLakes() {
        Area<Zone.River> emptyRiverArea = new Area<>(new HashSet<>(), new ArrayList<>(), 0);
        assertEquals(0, riverFishCount(emptyRiverArea), "Aucun poisson ne devrait être compté dans une aire sans rivières ni lacs");
    }
    @Test
    public void testAnimalsAllCancelled() {
        Animal deer = new Animal(100, DEER);
        Animal tiger = new Animal(102, Animal.Kind.TIGER);
        Set<Zone.Meadow> zones = Set.of(new Zone.Meadow( 10, Collections.singletonList(deer),SHAMAN));
        Set<Animal> cancelledAnimals = Set.of(deer);
        Area<Zone.Meadow> meadow = new Area<>(zones, Collections.emptyList(), 0);
        assertTrue(Area.animals(meadow, cancelledAnimals).isEmpty(), "Doit retourner un ensemble vide si tous les animaux sont annulés.");
    }
    @Test
    public void testRiverFishCountWithoutLakes() {
        Zone.River riverOnly = new Zone.River(1, 5, null); // Riv avec 5 poissons
        Set<Zone.River> rivers = Set.of(riverOnly);
        Area<Zone.River> riverArea = new Area<>(rivers, Collections.emptyList(), 0);
        assertEquals(5, riverFishCount(riverArea), "Doit retourner le nombre correct de poissons lorsque la rivière n'a pas de lacs.");
    }
    @Test
    public void testRiverFishCountWithOneLake() {
        Zone.Lake lake = new Zone.Lake(2, 10, null); // Lac avec 10 poissons
        Zone.River riverWithLake = new Zone.River(1, 3, lake); // Riv avec 3 poissons, co au lac
        Set<Zone.River> rivers = Set.of(riverWithLake);
        Area<Zone.River> riverArea = new Area<>(rivers, Collections.emptyList(), 0);
        assertEquals(13, riverFishCount(riverArea), "Doit compter les poissons dans le lac et la rivière.");
    }
    @Test
    public void testRiverFishCountWithDuplicateLakes() {
        Zone.Lake lake = new Zone.Lake(2, 7, null); // Lac com avec 7 poissons
        Zone.River river1 = new Zone.River(1, 4, lake); // P rivière aboutissant au lac commun
        Zone.River river2 = new Zone.River(3, 5, lake); // S rivière aboutissant au même lac
        Set<Zone.River> rivers = Set.of(river1, river2);
        Area<Zone.River> riverArea = new Area<>(rivers, Collections.emptyList(), 0);
        assertEquals(16, riverFishCount(riverArea), "Doit compter les poissons du lac commun une seule fois, en plus de ceux des rivières.");
    }
    @Test
    public void testRiverFishCountWithUniqueLakesAtEachEnd() {
        Zone.Lake lake1 = new Zone.Lake(2, 6, null);
        Zone.River riverBetweenLakes = new Zone.River(1, 2, lake1);
        Set<Zone.River> rivers = Set.of(riverBetweenLakes);
        Area<Zone.River> riverArea = new Area<>(rivers, Collections.emptyList(), 0);
        assertEquals(8, riverFishCount(riverArea), "Doit compter correctement les poissons dans les deux lacs uniques et la rivière entre eux.");
    }
    @Test
    public void testRiverFishCountWithNoRivers() {
        Set<Zone.River> noRivers = Collections.emptySet();
        Area<Zone.River> emptyRiverArea = new Area<>(noRivers, Collections.emptyList(), 0);
        assertEquals(0, riverFishCount(emptyRiverArea), "Doit retourner 0 lorsque l'aire ne contient aucune rivière.");
    }
    @Test
    public void testRiverFishCountWithNoFishInSystem() {
        Zone.Lake emptyLake = new Zone.Lake(10, 0, null);
        Zone.River fishlessRiver = new Zone.River(11, 0, emptyLake);
        Set<Zone.Water> waterZones = Set.of(emptyLake,fishlessRiver);
        Area<Zone.Water> fishlessSystem = new Area<>(waterZones, Collections.emptyList(), 0);
        assertEquals(0, riverSystemFishCount(fishlessSystem), "Doit retourner 0 lorsque ni les rivières ni les lacs ne contiennent de poissons.");
    }
    @Test
    public void testRiverFishCountWithMultipleRiversToSameLake() {
        Zone.Lake centralLake = new Zone.Lake(7, 7, null); // Lac avec 7 poissons
        Zone.River river1 = new Zone.River(8, 2, centralLake); // P riv aboutissant au lac central
        Zone.River river2 = new Zone.River(9, 3, centralLake); // S riv aboutissant au lac central
        Set<Zone.Water> waterZones = Set.of(centralLake, river1, river2);
        Area<Zone.Water> riverSystem = new Area<>(waterZones, Collections.emptyList(), 0);
        assertEquals(12, riverSystemFishCount(riverSystem), "Doit compter correctement les poissons lorsque plusieurs rivières aboutissent au même lac.");
    }
    @Test
    public void testRiverFishCountWithIndirectlyConnectedLakes() {
        Zone.Lake lake1 = new Zone.Lake(12, 6, null);
        Zone.River riverConnectingToLake1 = new Zone.River(14, 4, lake1);
        Set<Zone.Water> waterZones = Set.of(lake1, riverConnectingToLake1);
        Area<Zone.Water> riverSystemWithIndirectConnection = new Area<>(waterZones, Collections.emptyList(), 0);
        assertEquals(10, riverSystemFishCount(riverSystemWithIndirectConnection), "Doit compter les poissons du lac directement connecté, mais ignorer les lacs indirectement connectés.");
    }
    @Test
    public void testRiverSystemFishCountWithEmptySystem() {
        Area<Zone.Water> emptySystem = new Area<>(Collections.emptySet(), Collections.emptyList(), 0);
        assertEquals(0, riverSystemFishCount(emptySystem), "Doit retourner 0 pour un système hydrographique vide.");
    }
    @Test
    public void testRiverSystemFishCountWithRiversOnly() {
        Zone.River river1 = new Zone.River(1, 2, null);
        Zone.River river2 = new Zone.River(2, 3, null);
        Set<Zone.Water> rivers = Set.of(river1, river2);
        Area<Zone.Water> riverOnlySystem = new Area<>(rivers, Collections.emptyList(), 0);
        assertEquals(5, riverSystemFishCount(riverOnlySystem), "Doit compter les poissons de toutes les rivières.");
    }
    @Test
    public void testRiverSystemFishCountWithLakesOnly() {
        Zone.Lake lake1 = new Zone.Lake(3, 5, null);
        Zone.Lake lake2 = new Zone.Lake(4, 4, null);
        Set<Zone.Water> lakes = Set.of(lake1, lake2);
        Area<Zone.Water> lakeOnlySystem = new Area<>(lakes, Collections.emptyList(), 0);
        assertEquals(9, riverSystemFishCount(lakeOnlySystem), "Doit compter les poissons de tous les lacs.");
    }
    @Test
    public void testRiverSystemFishCountWithRiversAndConnectedLakes() {
        Zone.Lake lake = new Zone.Lake(5, 7, null);
        Zone.River river1 = new Zone.River(1, 3, lake);
        Zone.River river2 = new Zone.River(2, 2, lake);
        Set<Zone.Water> waterZones = Set.of(lake, river1, river2);
        Area<Zone.Water> complexSystem = new Area<>(waterZones, Collections.emptyList(), 0);
        assertEquals(12, riverSystemFishCount(complexSystem), "Doit correctement compter les poissons dans les rivières et le lac connecté.");
    }


    public void testRiverSystemFishCountWithIsolatedLakes() {
        Zone.Lake isolatedLake1 = new Zone.Lake(8, 5, null);
        Zone.Lake isolatedLake2 = new Zone.Lake(9, 10, null);
        Zone.River riverWithNoLakes = new Zone.River(4, 4, null);
        Set<Zone.Water> waterZones = Set.of(isolatedLake1, isolatedLake2, riverWithNoLakes);
        Area<Zone.Water> systemWithIsolatedLakes = new Area<>(waterZones, Collections.emptyList(), 0);
        assertEquals(4, riverSystemFishCount(systemWithIsolatedLakes), "Doit seulement compter les poissons dans la rivière, ignorer les lacs isolés.");
    }
    @Test
    public void testLakeCountWithNoLakes() {
        Set<Zone.Water> riversOnly = Set.of(new Zone.River(1, 3, null));
        Area<Zone.Water> riverSystem = new Area<>(riversOnly, Collections.emptyList(), 0);
        assertEquals(0, lakeCount(riverSystem), "Doit retourner 0 quand il n'y a pas de lacs dans le système.");
    }
    @Test
    public void testLakeCountWithMultipleUniqueLakes() {
        Set<Zone.Water> lakes = Set.of(new Zone.Lake(2, 5, null), new Zone.Lake(3, 6, null));
        Area<Zone.Water> lakeSystem = new Area<>(lakes, Collections.emptyList(), 0);
        assertEquals(2, lakeCount(lakeSystem), "Doit correctement compter tous les lacs uniques dans le système.");
    }
    @Test
    public void testLakeCountWithDuplicateLakes() {
        Zone.Lake duplicateLake = new Zone.Lake(4, 7, null);
        Set<Zone.Water> duplicateLakes = new HashSet<>(Arrays.asList(duplicateLake, duplicateLake)); // Duplication intentionnelle
        Area<Zone.Water> systemWithDuplicateLakes = new Area<>(duplicateLakes, Collections.emptyList(), 0);
        assertEquals(1, lakeCount(systemWithDuplicateLakes), "Doit ignorer les duplications et compter chaque lac une seule fois.");
    }
    @Test
    public void testLakeCountWithRiversAndLakes() {
        Zone.Lake lake1 = new Zone.Lake(5, 8, null);
        Zone.River river1 = new Zone.River(6, 2, lake1); // Rivière connectée à un lac
        Set<Zone.Water> riversAndLakes = Set.of(lake1, river1);
        Area<Zone.Water> mixedSystem = new Area<>(riversAndLakes, Collections.emptyList(), 0);
        assertEquals(1, lakeCount(mixedSystem), "Doit compter correctement les lacs dans un système mixte de rivières et de lacs.");
    }
    @Test
    public void testLakeCountWithDirectlyConnectedLakes() {
        Zone.Lake lake1 = new Zone.Lake(10, 11, null);
        Zone.Lake lake2 = new Zone.Lake(11, 12, null);
        Set<Zone.Water> directlyConnectedLakes = Set.of(lake1, lake2);
        Area<Zone.Water> systemWithDirectlyConnectedLakes = new Area<>(directlyConnectedLakes, Collections.emptyList(), 0);
        assertEquals(2, lakeCount(systemWithDirectlyConnectedLakes), "Doit compter correctement les lacs même lorsqu'ils sont connectés directement entre eux.");
    }
    @Test
    public void testIsClosedWithNoOpenConnections() {
        Area<Zone.Water> closedArea = new Area<>(Collections.emptySet(), Collections.emptyList(), 0);
        assertTrue(closedArea.isClosed(), "L'aire devrait être considérée comme fermée si elle n'a aucune connexion ouverte.");
    }
    @Test
    public void testIsClosedWithOpenConnections() {
        Area<Zone.Water> openArea = new Area<>(Collections.emptySet(), Collections.emptyList(), 1); // Supposons que 1 représente une connexion ouverte
        assertFalse(openArea.isClosed(), "L'aire ne devrait pas être considérée comme fermée s'il y a des connexions ouvertes.");
    }
    @Test
    public void testIsOccupiedWithNoOccupants() {
        Area<Zone.Water> unoccupiedArea = new Area<>(Collections.emptySet(), Collections.emptyList(), 0);
        assertFalse(unoccupiedArea.isOccupied(), "L'aire devrait être considérée comme non occupée si elle n'a aucun occupant.");
    }
    @Test
    public void testIsOccupiedWithOneOccupant() {
        List<PlayerColor> oneOccupant = Collections.singletonList(RED);
        Area<Zone.Water> singleOccupiedArea = new Area<>(Collections.emptySet(), oneOccupant, 0);
        assertTrue(singleOccupiedArea.isOccupied(), "L'aire devrait être considérée comme occupée s'il y a au moins un occupant.");
    }
    @Test
    public void testIsOccupiedWithMultipleOccupants() {
        List<PlayerColor> multipleOccupants = Arrays.asList(RED, BLUE);
        Area<Zone.Water> multipleOccupiedArea = new Area<>(Collections.emptySet(), multipleOccupants, 0);
        assertTrue(multipleOccupiedArea.isOccupied(), "L'aire devrait être considérée comme occupée s'il y a plusieurs occupants.");
    }
    @Test
    public void testMajorityOccupantsWithNoOccupants() {
        Area<Zone.Water> emptyArea = new Area<>(Collections.emptySet(), Collections.emptyList(), 0);
        assertTrue(emptyArea.majorityOccupants().isEmpty(), "Doit retourner un ensemble vide pour une aire sans occupants.");
    }
    @Test
    public void testMajorityOccupantsWithEqualOccupants() {
        List<PlayerColor> equalOccupants = Arrays.asList(RED, BLUE, RED, BLUE);
        Area<Zone.Water> equalArea = new Area<>(Collections.emptySet(), equalOccupants, 0);
        assertEquals(2, equalArea.majorityOccupants().size(), "Doit retourner tous les occupants quand ils sont présents en nombre égal.");
    }
    @Test
    public void testMajorityOccupantsWithSingleMajorityOccupant() {
        List<PlayerColor> occupants = Arrays.asList(RED, BLUE, RED);
        Area<Zone.Water> areaWithMajority = new Area<>(Collections.emptySet(), occupants, 0);
        Set<PlayerColor> majorityOccupants = areaWithMajority.majorityOccupants();
        assertEquals(1, majorityOccupants.size(), "Doit retourner un seul occupant majoritaire.");
        assertTrue(majorityOccupants.contains(RED), "L'occupant majoritaire devrait être RED.");
    }
    @Test
    public void testMajorityOccupantsWithMultipleMajorityOccupants() {
        List<PlayerColor> multipleMajorities = Arrays.asList(RED, RED, BLUE, BLUE, GREEN, GREEN);
        Area<Zone.Water> areaWithMultipleMajors = new Area<>(Collections.emptySet(), multipleMajorities, 0);
        Set<PlayerColor> majorityOccupants = areaWithMultipleMajors.majorityOccupants();
        assertEquals(3, majorityOccupants.size(), "Doit inclure tous les occupants avec la même fréquence majoritaire.");
        assertTrue(majorityOccupants.containsAll(Arrays.asList(RED, BLUE, GREEN)), "Les occupants majoritaires devraient inclure RED, BLUE, et GREEN.");
    }


    public void testMajorityOccupantsWithSuccessiveAdditions() {
        List<PlayerColor> initialOccupants = new ArrayList<>(Arrays.asList(PlayerColor.RED, PlayerColor.BLUE));
        Area<Zone.Water> area = new Area<>(Collections.emptySet(), initialOccupants, 0);

        // Ajout d'occupants supplémentaires.
        initialOccupants.addAll(Arrays.asList(PlayerColor.RED, PlayerColor.RED)); // Supposons une méthode ou une action qui permet cela.
        Set<PlayerColor> majorityOccupants = area.majorityOccupants();
        System.out.println(initialOccupants);
        System.out.println(majorityOccupants);



        assertEquals(1, majorityOccupants.size(), "Doit retourner un seul occupant majoritaire après ajout.");
        assertTrue(majorityOccupants.contains(PlayerColor.RED), "RED devrait être l'occupant majoritaire après les ajouts.");
    }
    @Test
    public void testMajorityOccupantsWithUnbalancedInitialOccupants() {
        List<PlayerColor> unbalancedOccupants = Arrays.asList(RED, RED, RED, BLUE);
        Area<Zone.Water> area = new Area<>(Collections.emptySet(), unbalancedOccupants, 0);
        Set<PlayerColor> majorityOccupants = area.majorityOccupants();

        assertEquals(1, majorityOccupants.size(), "Doit retourner un seul ensemble d'occupants majoritaires dans une répartition déséquilibrée.");
        assertTrue(majorityOccupants.contains(RED), "RED devrait être l'occupant majoritaire dans une liste déséquilibrée.");
    }
    @Test
    public void testConnectToWithTwoUnoccupiedAreas() {
        Area<Zone.Water> area1 = new Area<>(Set.of(new Zone.Lake(1, 10, null)), Collections.emptyList(), 0);
        Area<Zone.Water> area2 = new Area<>(Set.of(new Zone.Lake(2, 20, null)), Collections.emptyList(), 0);
        Area<Zone.Water> connectedArea = area1.connectTo(area2);
        assertTrue(connectedArea.zones().size() == 2, "La nouvelle aire devrait contenir les zones des deux aires originales.");
        assertTrue(connectedArea.occupants().isEmpty(), "La nouvelle aire devrait être non occupée.");
    }
    @Test
    public void testConnectToWithOccupiedAreas() {
        List<PlayerColor> occupantsArea1 = Arrays.asList(RED);
        List<PlayerColor> occupantsArea2 = Arrays.asList(BLUE);
        Area<Zone.Water> area1 = new Area<>(Set.of(new Zone.Lake(1, 10, null)), occupantsArea1, 0);
        Area<Zone.Water> area2 = new Area<>(Set.of(new Zone.Lake(2, 20, null)), occupantsArea2, 0);
        Area<Zone.Water> connectedArea = area1.connectTo(area2);
        assertEquals(2, connectedArea.occupants().size(), "La nouvelle aire devrait inclure les occupants des deux aires originales.");
        assertTrue(connectedArea.occupants().containsAll(Arrays.asList(RED, BLUE)), "La nouvelle aire devrait contenir les occupants RED et BLUE.");
    }

    public void testConnectToWithItself() {
        List<PlayerColor> occupants = Arrays.asList(RED);
        Area<Zone.Water> area = new Area<>(Set.of(new Zone.Lake(1, 10, null)), occupants, 2);
        Area<Zone.Water> connectedArea = area.connectTo(area);
        assertEquals(2, connectedArea.occupants().size(), "La nouvelle aire devrait conserver les mêmes occupants.");
        assertTrue(connectedArea.openConnections() < area.openConnections(), "Les connexions ouvertes devraient être ajustées.");
    }
    @Test
    public void testConnectToAdjustingOpenConnections() {
        Area<Zone.Water> area1 = new Area<>(Set.of(new Zone.Lake(1, 10, null)), Collections.emptyList(), 1);
        Area<Zone.Water> area2 = new Area<>(Set.of(new Zone.Lake(2, 20, null)), Collections.emptyList(), 2);
        Area<Zone.Water> connectedArea = area1.connectTo(area2);
        int expectedOpenConnections = Math.max(0, 1 + 2 - 2);
        assertEquals(expectedOpenConnections, connectedArea.openConnections(), "Les connexions ouvertes devraient être correctement ajustées.");
    }
    @Test
    public void testOriginalAreaUnchangedAfterWithInitialOccupant() {
        Area<Zone.Water> originalArea = new Area<>(Collections.emptySet(), Collections.emptyList(), 0);
        try {
            originalArea.withInitialOccupant(RED);
        } catch (IllegalArgumentException e) {
        }
        assertFalse(originalArea.isOccupied(), "L'aire originale devrait rester inchangée et non occupée.");
    }
    @Test
    public void testWithoutOccupantsOnAlreadyEmptyArea() {
        Area<Zone.Water> emptyArea = new Area<>(Collections.emptySet(), Collections.emptyList(), 0);
        Area<Zone.Water> resultArea = emptyArea.withoutOccupants();

        assertTrue(resultArea.occupants().isEmpty(), "L'aire résultante devrait être sans occupants.");
        assertNotSame(resultArea, emptyArea, "Une nouvelle instance de l'aire devrait être retournée, même si elle est identique.");
    }
    @Test
    public void testWithoutOccupantsRemovesAllOccupants() {
        List<PlayerColor> initialOccupants = Arrays.asList(PlayerColor.RED, PlayerColor.BLUE, PlayerColor.GREEN);
        Area<Zone.Water> occupiedArea = new Area<>(Collections.emptySet(), initialOccupants, 0);
        Area<Zone.Water> clearedArea = occupiedArea.withoutOccupants();

        assertTrue(clearedArea.occupants().isEmpty(), "L'aire résultante devrait être complètement dénuée d'occupants.");
    }
    @Test
    public void testOriginalAreaUnchangedAfterWithoutOccupants() {
        List<PlayerColor> initialOccupants = Arrays.asList(PlayerColor.RED, PlayerColor.BLUE);
        Area<Zone.Water> originalArea = new Area<>(Collections.emptySet(), initialOccupants, 0);
        originalArea.withoutOccupants();

        assertFalse(originalArea.occupants().isEmpty(), "L'aire originale ne devrait pas être modifiée après l'appel de withoutOccupants().");
    }
    @Test
    public void testNewInstanceReturnedEvenIfAreaAlreadyEmpty() {
        Area<Zone.Water> emptyArea = new Area<>(Collections.emptySet(), Collections.emptyList(), 0);
        Area<Zone.Water> resultArea = emptyArea.withoutOccupants();

        assertNotSame(resultArea, emptyArea, "Une nouvelle instance devrait être retournée, assurant l'immuabilité de l'aire originale.");
    }
    @Test
    public void testOtherAreaAttributesUnchangedAfterWithoutOccupants() {
        Set<Zone.Water> initialZones = Set.of(new Zone.Lake(1, 10, null));
        List<PlayerColor> initialOccupants = Arrays.asList(PlayerColor.RED, PlayerColor.BLUE);
        Area<Zone.Water> originalArea = new Area<>(initialZones, initialOccupants, 2); // Supposons 2 connexions ouvertes
        Area<Zone.Water> updatedArea = originalArea.withoutOccupants();

        assertEquals(2, updatedArea.openConnections(), "Le nombre de connexions ouvertes devrait rester inchangé.");
        assertEquals(initialZones, updatedArea.zones(), "Les zones de l'aire devraient rester inchangées.");
        assertTrue(updatedArea.occupants().isEmpty(), "Les occupants devraient être supprimés de l'aire mise à jour.");
    }
    @Test
    public void testWithoutOccupantsRemovesDuplicateOccupants() {
        List<PlayerColor> initialOccupants = new ArrayList<>(Arrays.asList(PlayerColor.RED, PlayerColor.RED, PlayerColor.BLUE));
        Area<Zone.Water> areaWithDuplicates = new Area<>(Collections.emptySet(), initialOccupants, 0);
        Area<Zone.Water> clearedArea = areaWithDuplicates.withoutOccupants();

        assertTrue(clearedArea.occupants().isEmpty(), "Tous les occupants, y compris les doublons, devraient être supprimés.");
    }
    @Test
    public void testWithoutOccupantsDoesNotAlterOpenConnections() {
        Area<Zone.Water> areaWithOpenConnections = new Area<>(Collections.emptySet(), Arrays.asList(PlayerColor.RED), 3); // 3 connexions ouvertes
        Area<Zone.Water> updatedArea = areaWithOpenConnections.withoutOccupants();

        assertEquals(3, updatedArea.openConnections(), "Le nombre de connexions ouvertes devrait rester le même après avoir enlevé les occupants.");
    }
    @Test
    public void testTileIdsWithNoZones() {
        Area<Zone> areaWithoutZones = new Area<>(Collections.emptySet(), Collections.emptyList(), 0);
        assertTrue(areaWithoutZones.tileIds().isEmpty(), "L'ensemble des identifiants de tuiles devrait être vide pour une aire sans zones.");
    }
    @Test
    public void testZoneWithSpecialPowerNone() {
        Set<Zone> zones = Set.of(new Zone.Meadow(10, List.of(), null), new Zone.Lake(20, 5, null));
        Area<Zone> area = new Area<>(zones, Collections.emptyList(), 0);

        assertNull(area.zoneWithSpecialPower(Zone.SpecialPower.SHAMAN), "Doit retourner null lorsqu'aucune zone ne possède le pouvoir spécial donné.");
    }
    @Test
    public void testZoneWithSpecialPowerExists() {
        Zone.Meadow meadowWithPower = new Zone.Meadow(10, List.of(), Zone.SpecialPower.SHAMAN);
        Set<Zone> zones = Set.of(meadowWithPower, new Zone.Lake(20, 5, null));
        Area<Zone> area = new Area<>(zones, Collections.emptyList(), 0);

        assertEquals(meadowWithPower, area.zoneWithSpecialPower(Zone.SpecialPower.SHAMAN), "Doit retourner la zone qui possède le pouvoir spécial donné.");
    }
    @Test
    public void testZoneWithSpecialPowerMultipleZones() {
        Zone.Meadow firstMeadowWithPower = new Zone.Meadow(10, List.of(), Zone.SpecialPower.SHAMAN);
        Zone.Meadow secondMeadowWithPower = new Zone.Meadow(11, List.of(), Zone.SpecialPower.SHAMAN);
        Set<Zone> zones = Set.of(firstMeadowWithPower, secondMeadowWithPower);
        Area<Zone> area = new Area<>(zones, Collections.emptyList(), 0);

        Zone result = area.zoneWithSpecialPower(Zone.SpecialPower.SHAMAN);
        assertTrue(result.equals(firstMeadowWithPower) || result.equals(secondMeadowWithPower), "Doit retourner l'une des zones qui possèdent le pouvoir spécial donné.");
    }
    @Test
    public void testZoneWithSpecialPowerDistinctPowers() {
        Zone.Meadow meadowWithShaman = new Zone.Meadow(10, List.of(), Zone.SpecialPower.SHAMAN);
        Zone.Lake lakeWithLogboat = new Zone.Lake(20, 5, Zone.SpecialPower.LOGBOAT);
        Set<Zone> zones = Set.of(meadowWithShaman, lakeWithLogboat);
        Area<Zone> area = new Area<>(zones, Collections.emptyList(), 0);

        assertEquals(meadowWithShaman, area.zoneWithSpecialPower(Zone.SpecialPower.SHAMAN), "Doit retourner la zone avec le pouvoir de SHAMAN.");
        assertEquals(lakeWithLogboat, area.zoneWithSpecialPower(Zone.SpecialPower.LOGBOAT), "Doit retourner la zone avec le pouvoir de LOGBOAT.");
        assertNull(area.zoneWithSpecialPower(Zone.SpecialPower.RAFT), "Doit retourner null si le pouvoir spécial donné n'est pas présent.");
    }

    @Test
    public void testZoneWithNullSpecialPower() {
        Zone.Meadow meadowWithNullPower = new Zone.Meadow(10, List.of(), null);
        Set<Zone> zones = Set.of(meadowWithNullPower);
        Area<Zone> area = new Area<>(zones, Collections.emptyList(), 0);

        assertNull(area.zoneWithSpecialPower(Zone.SpecialPower.SHAMAN), "Doit retourner null pour une zone dont le pouvoir spécial est explicitement défini comme nul.");
    }
    @Test
    public void testSpecificZoneWithSpecialPowerAbsent() {
        Zone.Meadow meadowWithDifferentPower = new Zone.Meadow(10, List.of(), Zone.SpecialPower.LOGBOAT);
        Zone.Lake lakeWithAnotherPower = new Zone.Lake(20, 3, Zone.SpecialPower.RAFT);
        Set<Zone> zones = Set.of(meadowWithDifferentPower, lakeWithAnotherPower);
        Area<Zone> area = new Area<>(zones, Collections.emptyList(), 0);

        assertNull(area.zoneWithSpecialPower(Zone.SpecialPower.SHAMAN), "Doit retourner null lorsque la zone spécifique avec le pouvoir spécial demandé est absente.");
    }
}
