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


public class ZonePartitionBuildermyTest {
    private ZonePartition.Builder<Zone> builder;
    private Zone testZone;
    private final PlayerColor testColor = PlayerColor.RED; // Supposons que PlayerColor est une énumération

    @BeforeEach
    void setUp() {
        // Initialisation de la partition avec une zone et un occupant initial
        testZone = new Zone.Forest(1, Zone.Forest.Kind.PLAIN);
        HashSet<Area<Zone>> areas = new HashSet<>();
        Area<Zone> initialArea = new Area<>(Set.of(testZone), List.of(testColor), 0); // Aire avec un occupant
        areas.add(initialArea);
        ZonePartition<Zone> partition = new ZonePartition<>(areas);

        builder = new ZonePartition.Builder<>(partition);
    }

    @Test
    void testRemoveOccupantSuccessfully() {
        // Supprimez l'occupant et vérifiez qu'aucune exception n'est levée
        assertDoesNotThrow(() -> builder.removeOccupant(testZone, testColor),
                "La suppression de l'occupant ne devrait pas lever d'exception.");

        // Vérification supplémentaire pour s'assurer que l'occupant est effectivement supprimé
        // Ceci dépend de la manière dont vous pouvez inspecter les occupants dans votre implémentation
    }

    @Test
    void testRemoveOccupantFromNonExistingZone() {
        Zone nonExistingZone = new Zone.Forest(99, Zone.Forest.Kind.PLAIN); // Zone non présente dans la partition

        assertThrows(IllegalArgumentException.class, () -> builder.removeOccupant(nonExistingZone, testColor),
                "Devrait lever IllegalArgumentException pour une zone non existante.");
    }

    @Test
    void testRemoveNonExistingOccupantColor() {
        // Essayez de supprimer un occupant d'une couleur non présente
        assertThrows(IllegalArgumentException.class, () -> builder.removeOccupant(testZone, PlayerColor.BLUE),
                "Devrait lever IllegalArgumentException si l'aire n'est pas occupée par un occupant de la couleur donnée.");
    }
}

