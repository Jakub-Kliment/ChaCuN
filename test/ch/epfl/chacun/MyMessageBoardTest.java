package ch.epfl.chacun;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MyMessageBoardTest {
    @Test
    void Constructor() {
        List<MessageBoard.Message> messageList = new ArrayList<>(List.of(new MessageBoard.Message("test1", 0, Set.of(PlayerColor.RED), Set.of(0))));
        MessageBoard messageBoard = new MessageBoard(new textTest(), messageList);
        messageList.add(new MessageBoard.Message("test2", 0, Set.of(PlayerColor.RED), Set.of(0)));
        assertNotEquals(messageBoard.messages(), messageList);
    }

    @Test
    void point() {
        List<MessageBoard.Message> messageList = new ArrayList<>(List.of(
                new MessageBoard.Message("test1", 2, Set.of(PlayerColor.RED), Set.of(0)),
                new MessageBoard.Message("test2", 3, Set.of(PlayerColor.BLUE), Set.of(0)),
                new MessageBoard.Message("test3", 4, Set.of(PlayerColor.GREEN), Set.of(0)),
                new MessageBoard.Message("test4", 5, Set.of(PlayerColor.YELLOW), Set.of(0))));
        MessageBoard messageBoard = new MessageBoard(new textTest(), messageList);
        Map<PlayerColor, Integer> map = new HashMap<>();
        map.put(PlayerColor.RED, 2);
        map.put(PlayerColor.BLUE, 3);
        map.put(PlayerColor.GREEN, 4);
        map.put(PlayerColor.YELLOW, 5);
        assertEquals(messageBoard.points(), map);
    }

    @Test
    void forestWithoutOccupant() {
        MessageBoard messageBoard = new MessageBoard(new textTest(), new ArrayList<>());
        Area<Zone.Forest> forest = new Area<>(new HashSet<>(Set.of(new Zone.Forest(1 ,Zone.Forest.Kind.WITH_MUSHROOMS))), List.of(), 0);
        MessageBoard newMessageBoard = messageBoard.withScoredForest(forest);
        assertEquals(newMessageBoard, messageBoard);
    }

    @Test
    void forestWithOccupant() {
        MessageBoard messageBoard = new MessageBoard(new textTest(), new ArrayList<>());
        Area<Zone.Forest> forest = new Area<>(new HashSet<>(Set.of(new Zone.Forest(10 ,Zone.Forest.Kind.WITH_MUSHROOMS), new Zone.Forest(20, Zone.Forest.Kind.PLAIN))), List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.GREEN), 0);
        MessageBoard newMessageBoard = messageBoard.withScoredForest(forest);
        assertEquals(newMessageBoard.messages().size(), 1);
        assertEquals(newMessageBoard.messages().getFirst().points(), 7);
        assertEquals(newMessageBoard.messages().getFirst().scorers(), Set.of(PlayerColor.RED));
        assertEquals(newMessageBoard.messages().getFirst().tileIds(), Set.of(1, 2));
    }

    @Test
    void forestWithMultipleOccupant() {
        MessageBoard messageBoard = new MessageBoard(new textTest(), new ArrayList<>());
        Area<Zone.Forest> forest = new Area<>(new HashSet<>(Set.of(new Zone.Forest(10 ,Zone.Forest.Kind.WITH_MUSHROOMS), new Zone.Forest(20, Zone.Forest.Kind.PLAIN))), List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.GREEN, PlayerColor.GREEN), 0);
        MessageBoard newMessageBoard = messageBoard.withScoredForest(forest);
        assertEquals(newMessageBoard.messages().size(), 1);
        assertEquals(newMessageBoard.messages().getFirst().points(), 7);
        assertEquals(newMessageBoard.messages().getFirst().scorers(), Set.of(PlayerColor.RED, PlayerColor.GREEN));
        assertEquals(newMessageBoard.messages().getFirst().tileIds(), Set.of(1, 2));
    }

    @Test
    void riverWithoutOccupant() {
        MessageBoard messageBoard = new MessageBoard(new textTest(), new ArrayList<>());
        Area<Zone.River> river = new Area<>(new HashSet<>(Set.of(new Zone.River(20, 2, null), new Zone.River(10, 1, null))), new ArrayList<>(), 0);
        MessageBoard newMessageBoard = messageBoard.withScoredRiver(river);
        assertEquals(newMessageBoard, messageBoard);
    }

    @Test
    void riverWithOccupant() {
        MessageBoard messageBoard = new MessageBoard(new textTest(), new ArrayList<>());
        Area<Zone.River> river = new Area<>(new HashSet<>(Set.of(new Zone.River(20, 2, null), new Zone.River(10, 1, null))), List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.GREEN), 0);
        MessageBoard newMessageBoard = messageBoard.withScoredRiver(river);
        assertEquals(newMessageBoard.messages().size(), 1);
        assertEquals(newMessageBoard.messages().getFirst().points(), 5);
        assertEquals(newMessageBoard.messages().getFirst().scorers(), Set.of(PlayerColor.RED));
        assertEquals(newMessageBoard.messages().getFirst().tileIds(), Set.of(1, 2));
    }

    @Test
    void riverWithOccupantMultipleWinner() {
        MessageBoard messageBoard = new MessageBoard(new textTest(), new ArrayList<>());
        Area<Zone.River> river = new Area<>(new HashSet<>(Set.of(new Zone.River(20, 2, null), new Zone.River(10, 1, null))), List.of(PlayerColor.RED, PlayerColor.PURPLE), 0);
        MessageBoard newMessageBoard = messageBoard.withScoredRiver(river);
        assertEquals(newMessageBoard.messages().size(), 1);
        assertEquals(newMessageBoard.messages().getFirst().points(), 5);
        assertEquals(newMessageBoard.messages().getFirst().scorers(), Set.of(PlayerColor.RED, PlayerColor.PURPLE));
        assertEquals(newMessageBoard.messages().getFirst().tileIds(), Set.of(1, 2));
    }

    @Test
    void withScoredHuntingTrap() {
        MessageBoard messageBoard = new MessageBoard(new textTest(), new ArrayList<>());
        Area<Zone.Meadow> meadow = new Area<>(new HashSet<>(Set.of(new Zone.Meadow(10, List.of(new Animal(101, Animal.Kind.DEER)), null), new Zone.Meadow(20, List.of(new Animal(201, Animal.Kind.DEER), new Animal(202, Animal.Kind.AUROCHS)), null))), new ArrayList<>(), 0);
        MessageBoard newMessageBoard = messageBoard.withScoredHuntingTrap(PlayerColor.RED, meadow);
        assertEquals(newMessageBoard.messages().size(), 1);
        assertEquals(newMessageBoard.messages().getFirst().points(), 4);
        assertEquals(newMessageBoard.messages().getFirst().scorers(), Set.of(PlayerColor.RED));
        assertEquals(newMessageBoard.messages().getFirst().tileIds(), Set.of(1, 2));
    }

    @Test
    void withScoredHuntingTrapNoPoint() {
        MessageBoard messageBoard = new MessageBoard(new textTest(), new ArrayList<>());
        Area<Zone.Meadow> meadow = new Area<>(new HashSet<>(Set.of(new Zone.Meadow(10, List.of(), null), new Zone.Meadow(20, List.of(), null))), new ArrayList<>(), 0);
        MessageBoard newMessageBoard = messageBoard.withScoredHuntingTrap(PlayerColor.RED, meadow);
        assertEquals(messageBoard, newMessageBoard);
    }

    @Test
    void withScoredMeadowNoOccupant() {
        MessageBoard messageBoard = new MessageBoard(new textTest(), new ArrayList<>());
        Area<Zone.Meadow> meadow = new Area<>(new HashSet<>(Set.of(new Zone.Meadow(10, List.of(new Animal(101, Animal.Kind.DEER)), null), new Zone.Meadow(20, List.of(new Animal(201, Animal.Kind.DEER), new Animal(202, Animal.Kind.AUROCHS)), null))), new ArrayList<>(), 0);
        MessageBoard newMessageBoard = messageBoard.withScoredMeadow(meadow, new HashSet<>());
        assertEquals(messageBoard, newMessageBoard);
    }

    @Test
    void withScoredMeadowNoPoint() {
        MessageBoard messageBoard = new MessageBoard(new textTest(), new ArrayList<>());
        Area<Zone.Meadow> meadow = new Area<>(new HashSet<>(Set.of(new Zone.Meadow(10, new ArrayList<>(), null), new Zone.Meadow(20, new ArrayList<>(), null))), List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.GREEN), 0);
        MessageBoard newMessageBoard = messageBoard.withScoredMeadow(meadow, new HashSet<>());
        assertEquals(messageBoard, newMessageBoard);
    }

    @Test
    void withScoredMeadow1Winner() {
        MessageBoard messageBoard = new MessageBoard(new textTest(), new ArrayList<>());
        Area<Zone.Meadow> meadow = new Area<>(new HashSet<>(Set.of(new Zone.Meadow(10, List.of(new Animal(101, Animal.Kind.DEER)), null), new Zone.Meadow(20, List.of(new Animal(201, Animal.Kind.DEER), new Animal(202, Animal.Kind.AUROCHS)), null))), List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.GREEN), 0);
        MessageBoard newMessageBoard = messageBoard.withScoredMeadow(meadow, new HashSet<>(Set.of(new Animal(101, Animal.Kind.DEER))));
        assertEquals(newMessageBoard.messages().size(), 1);
        assertEquals(newMessageBoard.messages().getFirst().points(), 3);
        assertEquals(newMessageBoard.messages().getFirst().scorers(), Set.of(PlayerColor.RED));
        assertEquals(newMessageBoard.messages().getFirst().tileIds(), Set.of(1, 2));
    }

    @Test
    void withScoredMeadow2Winner() {
        MessageBoard messageBoard = new MessageBoard(new textTest(), new ArrayList<>());
        Area<Zone.Meadow> meadow = new Area<>(new HashSet<>(Set.of(new Zone.Meadow(10, List.of(new Animal(101, Animal.Kind.DEER)), null), new Zone.Meadow(20, List.of(new Animal(201, Animal.Kind.DEER), new Animal(202, Animal.Kind.AUROCHS)), null))), List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.GREEN, PlayerColor.GREEN), 0);
        MessageBoard newMessageBoard = messageBoard.withScoredMeadow(meadow, new HashSet<>(Set.of(new Animal(101, Animal.Kind.DEER), new Animal(202, Animal.Kind.AUROCHS))));
        assertEquals(newMessageBoard.messages().size(), 1);
        assertEquals(newMessageBoard.messages().getFirst().points(), 1);
        assertEquals(newMessageBoard.messages().getFirst().scorers(), Set.of(PlayerColor.RED, PlayerColor.GREEN));
        assertEquals(newMessageBoard.messages().getFirst().tileIds(), Set.of(1, 2));
    }

    @Test
    void withScoredMeadowAllAnimalDelete() {
        MessageBoard messageBoard = new MessageBoard(new textTest(), new ArrayList<>());
        Area<Zone.Meadow> meadow = new Area<>(new HashSet<>(Set.of(new Zone.Meadow(10, List.of(new Animal(101, Animal.Kind.DEER)), null), new Zone.Meadow(20, List.of(new Animal(201, Animal.Kind.DEER), new Animal(202, Animal.Kind.AUROCHS)), null))), List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.GREEN, PlayerColor.GREEN), 0);
        MessageBoard newMessageBoard = messageBoard.withScoredMeadow(meadow, new HashSet<>(Set.of(new Animal(101, Animal.Kind.DEER), new Animal(201, Animal.Kind.DEER), new Animal(202, Animal.Kind.AUROCHS))));
        assertEquals(messageBoard, newMessageBoard);
    }

    @Test
    void withScoredRiverSystemNoOccupant() {
        MessageBoard messageBoard = new MessageBoard(new textTest(), new ArrayList<>());
        Area<Zone.Water> water = new Area<>(new HashSet<>(Set.of(new Zone.River(10, 1, null), new Zone.Lake(20, 2, null))), new ArrayList<>(), 0);
        MessageBoard newMessageBoard = messageBoard.withScoredRiverSystem(water);
        assertEquals(messageBoard, newMessageBoard);
    }

    @Test
    void withScoredRiverSystemNoPoint() {
        MessageBoard messageBoard = new MessageBoard(new textTest(), new ArrayList<>());
        Area<Zone.Water> water = new Area<>(new HashSet<>(Set.of(new Zone.River(10, 0, null), new Zone.Lake(20, 0, null))), List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.GREEN), 0);
        MessageBoard newMessageBoard = messageBoard.withScoredRiverSystem(water);
        assertEquals(messageBoard, newMessageBoard);
    }

    @Test
    void withScoredRiverSystem3Winner() {
        MessageBoard messageBoard = new MessageBoard(new textTest(), new ArrayList<>());
        Area<Zone.Water> water = new Area<>(new HashSet<>(Set.of(new Zone.River(10, 1, null), new Zone.Lake(20, 2, null), new Zone.River(40, 1, null))), List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.GREEN, PlayerColor.GREEN, PlayerColor.PURPLE, PlayerColor.PURPLE), 0);
        MessageBoard newMessageBoard = messageBoard.withScoredRiverSystem(water);
        assertEquals(newMessageBoard.messages().size(), 1);
        assertEquals(newMessageBoard.messages().getFirst().points(), 4);
        assertEquals(newMessageBoard.messages().getFirst().scorers(), Set.of(PlayerColor.RED, PlayerColor.GREEN, PlayerColor.PURPLE));
        assertEquals(newMessageBoard.messages().getFirst().tileIds(), Set.of(1, 2, 4));
    }

    @Test
    void withScoredRiverSystem() {
        MessageBoard messageBoard = new MessageBoard(new textTest(), new ArrayList<>());
        Area<Zone.Water> water = new Area<>(new HashSet<>(Set.of(new Zone.River(10, 1, null), new Zone.Lake(20, 2, null))), List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.GREEN), 0);
        MessageBoard newMessageBoard = messageBoard.withScoredRiverSystem(water);
        assertEquals(newMessageBoard.messages().size(), 1);
        assertEquals(newMessageBoard.messages().getFirst().points(), 3);
        assertEquals(newMessageBoard.messages().getFirst().scorers(), Set.of(PlayerColor.RED));
        assertEquals(newMessageBoard.messages().getFirst().tileIds(), Set.of(1, 2));
    }

    @Test
    void withScoredPitTrapNoOccupant() {
        MessageBoard messageBoard = new MessageBoard(new textTest(), new ArrayList<>());
        Area<Zone.Meadow> meadow = new Area<>(new HashSet<>(Set.of(new Zone.Meadow(10, List.of(new Animal(101, Animal.Kind.DEER)), null), new Zone.Meadow(20, List.of(new Animal(201, Animal.Kind.DEER), new Animal(202, Animal.Kind.AUROCHS)), null))), new ArrayList<>(), 0);
        MessageBoard newMessageBoard = messageBoard.withScoredPitTrap(meadow, new HashSet<>());
        assertEquals(messageBoard, newMessageBoard);
    }

    @Test
    void withScoredPitTrapAllAnimalCancelled() {
        MessageBoard messageBoard = new MessageBoard(new textTest(), new ArrayList<>());
        Area<Zone.Meadow> meadow = new Area<>(new HashSet<>(Set.of(new Zone.Meadow(10, List.of(new Animal(101, Animal.Kind.DEER)), null), new Zone.Meadow(20, List.of(new Animal(201, Animal.Kind.DEER), new Animal(202, Animal.Kind.AUROCHS)), null))), List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.GREEN, PlayerColor.GREEN), 0);
        MessageBoard newMessageBoard = messageBoard.withScoredPitTrap(meadow, new HashSet<>(Set.of(new Animal(101, Animal.Kind.DEER), new Animal(201, Animal.Kind.DEER), new Animal(202, Animal.Kind.AUROCHS))));
        assertEquals(messageBoard, newMessageBoard);
    }

    @Test
    void withScoredPitTrap() {
        MessageBoard messageBoard = new MessageBoard(new textTest(), new ArrayList<>());
        Area<Zone.Meadow> meadow = new Area<>(new HashSet<>(Set.of(new Zone.Meadow(10, List.of(new Animal(101, Animal.Kind.DEER)), null), new Zone.Meadow(20, List.of(new Animal(201, Animal.Kind.DEER), new Animal(202, Animal.Kind.AUROCHS)), null))), List.of(PlayerColor.RED, PlayerColor.RED, PlayerColor.GREEN), 0);
        MessageBoard newMessageBoard = messageBoard.withScoredPitTrap(meadow, new HashSet<>(Set.of(new Animal(101, Animal.Kind.DEER))));
        assertEquals(newMessageBoard.messages().size(), 1);
        assertEquals(newMessageBoard.messages().getFirst().points(), 3);
        assertEquals(newMessageBoard.messages().getFirst().scorers(), Set.of(PlayerColor.RED));
        assertEquals(newMessageBoard.messages().getFirst().tileIds(), Set.of(1, 2));
    }

    private static class textTest implements TextMaker{

        @Override
        public String playerName(PlayerColor playerColor) {
            return new StringJoiner(" ")
                    .add(playerColor.toString())
                    .toString();
        }

        @Override
        public String points(int points) {
            return new StringJoiner(" ")
                    .add(String.valueOf(points))
                    .toString();
        }

        @Override
        public String playerClosedForestWithMenhir(PlayerColor player) {
            return new StringJoiner(" ")
                    .add(player.toString())
                    .toString();
        }

        @Override
        public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
            return new StringJoiner(" ")
                    .add(scorers.toString())
                    .add(String.valueOf(points))
                    .add(String.valueOf(mushroomGroupCount))
                    .add(String.valueOf(tileCount))
                    .toString();
        }

        @Override
        public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
            return new StringJoiner(" ")
                    .add(scorers.toString())
                    .add(String.valueOf(points))
                    .add(String.valueOf(fishCount))
                    .add(String.valueOf(tileCount))
                    .toString();
        }

        @Override
        public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
            return new StringJoiner(" ")
                    .add(scorer.toString())
                    .add(String.valueOf(points))
                    .add(animals.toString())
                    .toString();
        }

        @Override
        public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
            return new StringJoiner(" ")
                    .add(scorer.toString())
                    .add(String.valueOf(points))
                    .add(String.valueOf(lakeCount))
                    .toString();
        }

        @Override
        public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
            return new StringJoiner(" ")
                    .add(scorers.toString())
                    .add(String.valueOf(points))
                    .add(animals.toString())
                    .toString();
        }

        @Override
        public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
            return new StringJoiner(" ")
                    .add(scorers.toString())
                    .add(String.valueOf(points))
                    .add(String.valueOf(fishCount))
                    .toString();
        }

        @Override
        public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
            return new StringJoiner(" ")
                    .add(scorers.toString())
                    .add(String.valueOf(points))
                    .add(animals.toString())
                    .toString();
        }

        @Override
        public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
            return new StringJoiner(" ")
                    .add(scorers.toString())
                    .add(String.valueOf(points))
                    .add(String.valueOf(lakeCount))
                    .toString();
        }

        @Override
        public String playersWon(Set<PlayerColor> winners, int points) {
            return new StringJoiner(" ")
                    .add(winners.toString())
                    .add(String.valueOf(points))
                    .toString();
        }

        @Override
        public String clickToOccupy() {
            return "clickToOccupy";
        }

        @Override
        public String clickToUnoccupy() {
            return "clickToUnoccupy";
        }
    }
}