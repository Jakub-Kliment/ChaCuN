package ch.epfl.chacun;


import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TextMakerFrTest {

    private final Map<PlayerColor, String> playerNames = Map.of(
            PlayerColor.RED, "Dalia",
            PlayerColor.BLUE, "Claude",
            PlayerColor.GREEN, "Bachir",
            PlayerColor.YELLOW, "Alice");
    private final TextMakerFr textMakerFr = new TextMakerFr(playerNames);
    @Test
    void testPlayerName() {
        assertEquals("Dalia", textMakerFr.playerName(PlayerColor.RED));
        assertEquals("Claude", textMakerFr.playerName(PlayerColor.BLUE));
        assertEquals("Bachir", textMakerFr.playerName(PlayerColor.GREEN));
        assertEquals("Alice", textMakerFr.playerName(PlayerColor.YELLOW));

        assertNotEquals("Dalia", textMakerFr.playerName(PlayerColor.BLUE));
        assertNotEquals("Claude", textMakerFr.playerName(PlayerColor.GREEN));
        assertNotEquals("Bachir", textMakerFr.playerName(PlayerColor.YELLOW));
        assertNotEquals("Alice", textMakerFr.playerName(PlayerColor.RED));
    }

    @Test
    void playerNameThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> textMakerFr.playerName(PlayerColor.PURPLE));
    }

    @Test
    void testPoints() {
        assertEquals("5", textMakerFr.points(5));
        assertEquals("10", textMakerFr.points(10));
        assertEquals("15", textMakerFr.points(15));
        assertEquals("20", textMakerFr.points(20));

        assertNotEquals("5", textMakerFr.points(10));
        assertNotEquals("10", textMakerFr.points(15));
    }

    @Test
    void testPlayerClosedForestWithMenhir() {
        String expected = "Dalia a fermé une forêt contenant un menhir et peut donc placer une tuile menhir.";
        assertEquals(expected, textMakerFr.playerClosedForestWithMenhir(PlayerColor.RED));
    }

    @Test
    void testPlayersScoredForestForNormalCase() {
        String expected = "Dalia, Claude, Bachir et Alice ont remporté 5 points en tant qu'occupant·e·s majoritaires d'une forêt composée de 10 tuiles et de 10 groupes de champignons.";
        String actual = textMakerFr.playersScoredForest(Set.of(PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.RED, PlayerColor.YELLOW), 5, 10, 10);
        System.out.println(actual);
        assertEquals(expected, actual);
    }

    @Test
    void profTestPlayersScoredForest() {
        String expected = "Dalia et Alice ont remporté 9 points en tant qu'occupant·e·s majoritaires d'une forêt composée de 3 tuiles et de 1 groupe de champignons.";
        String actual = textMakerFr.playersScoredForest(Set.of(PlayerColor.RED, PlayerColor.YELLOW), 9, 1, 3);
        assertEquals(expected, actual);
    }

    @Test
    void testPlayersScoredForestForOnePlayer() {
        String expected = "Dalia a remporté 5 points en tant qu'occupant·e majoritaire d'une forêt composée de 10 tuiles et de 1 groupe de champignons.";
        String actual = textMakerFr.playersScoredForest(Set.of(PlayerColor.RED), 5, 1, 10);
        assertEquals(expected, actual);
    }

    @Test
    void testPlayersScoredForestForTwoPlayers() {
        String expected = "Dalia et Alice ont remporté 10 points en tant qu'occupant·e·s majoritaires d'une forêt composée de 20 tuiles.";
        String actual = textMakerFr.playersScoredForest(Set.of(PlayerColor.YELLOW, PlayerColor.RED), 10, 0, 20);
        assertEquals(expected, actual);
    }

    @Test
    void playerScoredForestThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> textMakerFr.playersScoredForest(Set.of(), 5, 10, 10));
    }

    @Test
    void testPlayersScoredForestForNoMushroom() {
        String expected = "Dalia, Claude, Bachir et Alice ont remporté 5 points en tant qu'occupant·e·s majoritaires d'une forêt composée de 10 tuiles.";
        String actual = textMakerFr.playersScoredForest(Set.of(PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.RED, PlayerColor.YELLOW), 5, 0, 10);
        assertEquals(expected, actual);
    }

    @Test
    void testPlayersScoredForestForOneMushroom() {
        String expected = "Bachir a remporté 5 points en tant qu'occupant·e majoritaire d'une forêt composée de 10 tuiles et de 1 groupe de champignons.";
        String actual = textMakerFr.playersScoredForest(Set.of(PlayerColor.GREEN), 5, 1, 10);
        assertEquals(expected, actual);
    }

    @Test
    void testPlayersScoredRiverForNormalCase() {
        String expected = "Dalia, Claude, Bachir et Alice ont remporté 5 points en tant qu'occupant·e·s majoritaires d'une rivière composée de 10 tuiles et contenant 10 poissons.";
        String actual = textMakerFr.playersScoredRiver(Set.of(PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.RED, PlayerColor.YELLOW), 5, 10, 10);
        assertEquals(expected, actual);
    }

    @Test
    void profTestPlayersScoredRiver() {
        String expected = "Alice a remporté 8 points en tant qu'occupant·e majoritaire d'une rivière composée de 3 tuiles et contenant 5 poissons.";
        String actual = textMakerFr.playersScoredRiver(Set.of(PlayerColor.YELLOW), 8, 5, 3);
        assertEquals(expected, actual);
    }

    @Test
    void testPlayersScoredRiverForOnePlayer() {
        String expected = "Dalia a remporté 5 points en tant qu'occupant·e majoritaire d'une rivière composée de 10 tuiles et contenant 10 poissons.";
        String actual = textMakerFr.playersScoredRiver(Set.of(PlayerColor.RED), 5, 10, 10);
        assertEquals(expected, actual);
    }

    @Test
    void testPlayersScoredRiverForTwoPlayersWithoutFish() {
        String expected = "Dalia et Alice ont remporté 10 points en tant qu'occupant·e·s majoritaires d'une rivière composée de 20 tuiles.";
        String actual = textMakerFr.playersScoredRiver(Set.of(PlayerColor.YELLOW, PlayerColor.RED), 10, 0, 20);
        assertEquals(expected, actual);
    }

    @Test
    void testPlayersScoredRiverForNoFish() {
        String expected = "Dalia, Claude, Bachir et Alice ont remporté 5 points en tant qu'occupant·e·s majoritaires d'une rivière composée de 10 tuiles.";
        String actual = textMakerFr.playersScoredRiver(Set.of(PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.RED, PlayerColor.YELLOW), 5, 0, 10);
        assertEquals(expected, actual);
    }

    @Test
    void playerScoredRiverThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> textMakerFr.playersScoredRiver(Set.of(), 5, 10, 10));
    }

    @Test
    void testPlayerScoredHuntingTrap() {
        String expected = "Dalia a remporté 5 points en plaçant la fosse à pieux dans un pré dans lequel elle est entourée de 1 mammouth.";
        Map<Animal.Kind, Integer> animals = Map.of(Animal.Kind.MAMMOTH, 1);
        String actual = textMakerFr.playerScoredHuntingTrap(PlayerColor.RED, 5, animals);
        assertEquals(expected, actual);
    }

    @Test
    void testPlayerScoredHuntingTrapForTwoAnimals() {
        String expected = "Dalia a remporté 5 points en plaçant la fosse à pieux dans un pré dans lequel elle est entourée de 2 aurochs et 1 cerf.";
        Map<Animal.Kind, Integer> animals = Map.of(Animal.Kind.DEER, 1, Animal.Kind.AUROCHS, 2);
        String actual = textMakerFr.playerScoredHuntingTrap(PlayerColor.RED, 5, animals);
        assertEquals(expected, actual);
    }

    @Test
    void playerScoredHuntingTrapWithThreeAnimals() {
        String expected = "Alice a remporté 10 points en plaçant la fosse à pieux dans un pré dans lequel elle est entourée de 4 mammouths, 1 auroch et 2 cerfs.";
        Map<Animal.Kind, Integer> animals = Map.of(Animal.Kind.DEER, 2, Animal.Kind.AUROCHS, 1, Animal.Kind.MAMMOTH, 4);
        String actual = textMakerFr.playerScoredHuntingTrap(PlayerColor.YELLOW, 10, animals);
        assertEquals(expected, actual);
    }
    @Test
    void playerScoredHuntingTrapWithThreeAnimalsButOneDoesNotGainPoints() {
        String expected = "Alice a remporté 10 points en plaçant la fosse à pieux dans un pré dans lequel elle est entourée de 4 mammouths et 2 cerfs.";
        Map<Animal.Kind, Integer> animals = Map.of(Animal.Kind.DEER, 2, Animal.Kind.AUROCHS, 0, Animal.Kind.MAMMOTH, 4);
        String actual = textMakerFr.playerScoredHuntingTrap(PlayerColor.YELLOW, 10, animals);
        assertEquals(expected, actual);
    }

    @Test
    void playerScoredHuntingTrapThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> textMakerFr.playerScoredHuntingTrap(PlayerColor.RED, 5, Map.of()));
    }
}
