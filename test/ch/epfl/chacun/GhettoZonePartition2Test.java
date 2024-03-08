package ch.epfl.chacun.Etape3;

import ch.epfl.chacun.Area;
import ch.epfl.chacun.PlayerColor;
import ch.epfl.chacun.Zone;
import ch.epfl.chacun.ZonePartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ZonePartition2Test {
    private ZonePartition.Builder<Zone> builder;
    private Zone testZone;
    private final PlayerColor testColor = PlayerColor.RED; // Supposer que PlayerColor est une énumération

    @BeforeEach
    void setUp() {
        // Initialisation de la partition avec une zone
        testZone = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        HashSet<Area<Zone>> areas = new HashSet<>();
        areas.add(new Area<>(Set.of(testZone), List.of(), 0)); // Aucun occupant, 0 connexions ouvertes
        ZonePartition<Zone> partition = new ZonePartition<>(areas);

        builder = new ZonePartition.Builder<>(partition);
    }

    @Test
    void testAddInitialOccupantSuccessfully() {
        assertDoesNotThrow(() -> builder.addInitialOccupant(testZone, testColor),
                "Ajouter un occupant initial ne devrait pas lever d'exception.");

        // Vérification supplémentaire si possible, par exemple en inspectant l'état de la partition
    }

    @Test
    void testAddInitialOccupantToNonExistingZone() {
        Zone nonExistingZone = new Zone.Forest(99, Zone.Forest.Kind.PLAIN); // Zone non présente dans la partition

        assertThrows(IllegalArgumentException.class, () -> builder.addInitialOccupant(nonExistingZone, testColor),
                "Devrait lever IllegalArgumentException pour une zone non existante.");
    }

    @Test
    void testAddInitialOccupantToOccupiedArea() {
        // Ajoutez d'abord un occupant pour rendre l'aire occupée
        builder.addInitialOccupant(testZone, testColor);

        // Essayez d'ajouter un autre occupant à la même aire
        assertThrows(IllegalArgumentException.class, () -> builder.addInitialOccupant(testZone, PlayerColor.BLUE),
                "Devrait lever IllegalArgumentException si l'aire est déjà occupée.");
    }
}
