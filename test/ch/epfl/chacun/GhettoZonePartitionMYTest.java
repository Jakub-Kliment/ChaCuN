package ch.epfl.chacun.Etape3;
import ch.epfl.chacun.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;
import java.util.HashSet;


public class ZonePartitionMYTest {
    Animal deer = new Animal(2, Animal.Kind.DEER);
    Animal tiger = new Animal(2, Animal.Kind.TIGER);
    Animal mammoth = new Animal(2, Animal.Kind.MAMMOTH);
    Animal aurochs = new Animal(2, Animal.Kind.AUROCHS);

    private Zone.Forest forestZone;
    private Zone.Meadow meadowZone;
    private Zone.Lake lakeZone;
    private ZonePartition<Zone> emptyPartition;
    private ZonePartition<Zone> partition;


    @BeforeEach
    void setUp() {
        emptyPartition = new ZonePartition<>();

        // Création d'instances de différents sous-types de Zone
        forestZone = new Zone.Forest(101, Zone.Forest.Kind.PLAIN);
        meadowZone = new Zone.Meadow(102, List.of(), Zone.SpecialPower.RAFT);
        lakeZone = new Zone.Lake(103, 5, Zone.SpecialPower.WILD_FIRE);

        Set<Area<Zone>> areas = new HashSet<>();
        areas.add(new Area<>((Set.of(forestZone)), List.of(PlayerColor.RED), 0)); // Forest dans une aire
        areas.add(new Area<>((Set.of(meadowZone, lakeZone)), List.of(PlayerColor.RED, PlayerColor.YELLOW), 1)); // Meadow et Lake dans une autre aire
        partition = new ZonePartition<>(areas);
    }

    @Test
    void testAreaContainingWithNull() {
        assertThrows(NullPointerException.class, () -> partition.areaContaining(null),
                "Doit lever une exception NullPointerException si la zone est null.");
    }


    @Test
    void testAreaContainingWithZoneInArea() {
        Area<Zone> areaForForest = partition.areaContaining(forestZone);
        assertTrue(areaForForest.zones().contains(forestZone), "L'aire doit contenir la zone forestière.");

        Area<Zone> areaForMeadow = partition.areaContaining(meadowZone);
        assertTrue(areaForMeadow.zones().contains(meadowZone), "L'aire doit contenir la zone de prairie.");
    }

    @Test
    void testAreaContainingWithEmptyPartition() {
        assertThrows(IllegalArgumentException.class, () -> emptyPartition.areaContaining(forestZone),
                "Doit lever une exception IllegalArgumentException pour une partition vide.");
    }

    @Test
    void testAreaContainingWithZoneNotInAnyArea() {
        Zone.River riverZone = new Zone.River(104, 10, null); // River pas ajoutée à la partition
        assertThrows(IllegalArgumentException.class, () -> partition.areaContaining(riverZone),
                "Doit lever une exception IllegalArgumentException si la zone n'appartient à aucune aire.");
    }

    @Test
    public void testAddSingletonAddsZoneCorrectly() {
        // Créez une instance de Builder sans zones initiales
        ZonePartition.Builder<Zone> builder = new ZonePartition.Builder<>(new ZonePartition<>(new HashSet<>()));

        // Créez une zone de test, en utilisant une des sous-classes concrètes de Zone
        Zone testZone = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);

        // Utilisez addSingleton pour ajouter cette zone à la partition via le builder
        builder.addSingleton(testZone, 5); // Supposons que '5' est le nombre de connexions ouvertes

        // Construisez la partition finale à partir du builder
        ZonePartition<Zone> partition = builder.build();

        // Tentez d'obtenir l'aire contenant la zone ajoutée
        // Si cela ne lève pas d'IllegalArgumentException, cela signifie que la zone a été correctement ajoutée
        assertDoesNotThrow(() -> partition.areaContaining(testZone),
                "La méthode areaContaining ne devrait pas lever d'exception, indiquant que la zone a été ajoutée correctement.");
    }
}
