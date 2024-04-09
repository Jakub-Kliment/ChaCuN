package ch.epfl.chacun;

import java.util.*;

/**
 * Represents a message board that keeps track of the messages that are displayed to the players
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 *
 * @param textMaker creates the text for the message
 * @param messages list of all messages of the message board
 */
public record MessageBoard(TextMaker textMaker, List<Message> messages) {

    /**
     * Immutable constructor
     */
    public MessageBoard {
        messages = List.copyOf(messages);
    }

    /**
     * Returns the points of each player
     *
     * @return a map that maps each player to their points
     */
    public Map<PlayerColor, Integer> points() {
        Map<PlayerColor, Integer> map = new HashMap<>();

        for (Message message : messages)
            for (PlayerColor player : message.scorers)
                if (map.containsKey(player))
                    map.put(player, message.points + map.get(player));
                else
                    map.put(player, message.points);

        return map;
    }

    /**
     * Returns the message board with a new message added for a closed forest
     *
     * @param forest the closed forest
     * @return message board
     */
    public MessageBoard withScoredForest(Area<Zone.Forest> forest) {
        if (!forest.isOccupied())
            return this;

        int points = Points.forClosedForest(forest.tileIds().size(), Area.mushroomGroupCount(forest));
        List<Message> newMessages = new ArrayList<>(messages);

        newMessages.add(new Message(
                textMaker.playersScoredForest(
                        forest.majorityOccupants(),
                        points,
                        Area.mushroomGroupCount(forest),
                        forest.tileIds().size()),
                points,
                forest.majorityOccupants(),
                forest.tileIds()));

        return new MessageBoard(textMaker, newMessages);
    }

    /**
     * Returns the message board with a new message added for a closed forest with a menhir
     *
     * @param player the player that closed the forest
     * @param forest the closed forest containing the menhir
     * @return message board
     */
    public MessageBoard withClosedForestWithMenhir(PlayerColor player, Area<Zone.Forest> forest) {
        List<Message> newMessages = new ArrayList<>(messages);

        newMessages.add(new Message(
                textMaker.playerClosedForestWithMenhir(player),
                0,
                new HashSet<>(),
                forest.tileIds()));

        return new MessageBoard(textMaker, newMessages);
    }

    /**
     * Returns the message board with a new message added for a closed river
     *
     * @param river the closed river
     * @return message board
     */
    public MessageBoard withScoredRiver(Area<Zone.River> river) {
        if (!river.isOccupied())
            return this;

        int points = Points.forClosedRiver(river.tileIds().size(), Area.riverFishCount(river));
        List<Message> newMessages = new ArrayList<>(messages);

        newMessages.add(new Message(
                textMaker.playersScoredRiver(
                        river.majorityOccupants(),
                        points,
                        Area.riverFishCount(river),
                        river.tileIds().size()),
                points,
                river.majorityOccupants(),
                river.tileIds()));

        return new MessageBoard(textMaker, newMessages);
    }

    /**
     * Returns the message board with a new message added for a scored hunting trap
     *
     * @param scorer the player that scored the hunting trap
     * @param adjacentMeadow the meadow adjacent to the hunting trap
     * @return message board
     */
    public MessageBoard withScoredHuntingTrap(PlayerColor scorer, Area<Zone.Meadow> adjacentMeadow) {
        Map<Animal.Kind, Integer> animalMap = animalCountMap(adjacentMeadow, new HashSet<>());
        int points = meadowPoints(animalMap);

        if (points <= 0)
            return this;

        List<Message> newMessages = new ArrayList<>(messages);
        newMessages.add(new Message(
                textMaker.playerScoredHuntingTrap(scorer, points, animalMap),
                points,
                Set.of(scorer),
                adjacentMeadow.tileIds()));

        return new MessageBoard(textMaker, newMessages);
    }

    /**
     * Returns the message board with a new message added for a scored logboat
     *
     * @param scorer the player that scored the logboat
     * @param riverSystem the river system containing the logboat
     * @return message board
     */
    public MessageBoard withScoredLogboat(PlayerColor scorer, Area<Zone.Water> riverSystem) {
        int point = Points.forLogboat(Area.lakeCount(riverSystem));
        List<Message> newMessages = new ArrayList<>(messages);

        newMessages.add(new Message(
                textMaker.playerScoredLogboat(
                        scorer,
                        point,
                        Area.lakeCount(riverSystem)),
                point,
                Set.of(scorer),
                riverSystem.tileIds()));

        return new MessageBoard(textMaker, newMessages);
    }

    /**
     * Returns the message board with a new message added for a scored meadow
     *
     * @param meadow the meadow that was scored
     * @param cancelledAnimals the animals that were cancelled
     * @return message board
     */
    public MessageBoard withScoredMeadow(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        Map<Animal.Kind, Integer> animalCount = animalCountMap(meadow, cancelledAnimals);
        int points = meadowPoints(animalCount);

        if (!meadow.isOccupied() || points == 0)
            return this;

        List<Message> newMessages = new ArrayList<>(messages);

        newMessages.add(new Message(
                textMaker.playersScoredMeadow(
                        meadow.majorityOccupants(),
                        points,
                        animalCount),
                points,
                meadow.majorityOccupants(),
                meadow.tileIds()));

        return new MessageBoard(textMaker, newMessages);
    }

    /**
     * Returns the message board with a new message added for a scored river system
     *
     * @param riverSystem the river system that was scored
     * @return message board
     */
    public MessageBoard withScoredRiverSystem(Area<Zone.Water> riverSystem) {
        int points = Points.forRiverSystem(Area.riverSystemFishCount(riverSystem));

        if(!riverSystem.isOccupied() || points == 0)
            return this;

        List<Message> newMessages = new ArrayList<>(messages);

        newMessages.add(new Message(
                textMaker.playersScoredRiverSystem(
                        riverSystem.majorityOccupants(),
                        points,
                        Area.riverSystemFishCount(riverSystem)),
                points,
                riverSystem.majorityOccupants(),
                riverSystem.tileIds()));

        return new MessageBoard(textMaker, newMessages);
    }

    /**
     * Returns the message board with a new message added for a scored pit trap
     *
     * @param adjacentMeadow the meadow adjacent to the pit trap
     * @param cancelledAnimals the animals that were cancelled
     * @return message board
     */
    public MessageBoard withScoredPitTrap(Area<Zone.Meadow> adjacentMeadow, Set<Animal> cancelledAnimals) {
        Map<Animal.Kind, Integer> animalCount = animalCountMap(adjacentMeadow, cancelledAnimals);
        int points = meadowPoints(animalCount);

        if (!adjacentMeadow.isOccupied() || points == 0)
            return this;

        List<Message> newMessages = new ArrayList<>(messages);

        newMessages.add(new Message(
                textMaker.playersScoredPitTrap(
                        adjacentMeadow.majorityOccupants(),
                        points,
                        animalCount),
                points,
                adjacentMeadow.majorityOccupants(),
                adjacentMeadow.tileIds()));

        return new MessageBoard(textMaker, newMessages);
    }

    /**
     * Returns the message board with a new message added for a scored raft
     *
     * @param riverSystem the river system containing the raft
     * @return message board
     */
    public MessageBoard withScoredRaft(Area<Zone.Water> riverSystem) {
        if (!riverSystem.isOccupied())
            return this;

        int points = Points.forRaft(Area.lakeCount(riverSystem));
        List<Message> newMessages = new ArrayList<>(messages);

        newMessages.add(new Message(
                textMaker.playersScoredRaft(
                        riverSystem.majorityOccupants(),
                        points,
                        Area.lakeCount(riverSystem)),
                points,
                riverSystem.majorityOccupants(),
                riverSystem.tileIds()));

        return new MessageBoard(textMaker, newMessages);
    }

    /**
     * Returns the message board with a new message added for the end of the game
     *
     * @param winners the winner(s) of the game
     * @param points the points of the winner(s)
     * @return message board
     */
    public MessageBoard withWinners(Set<PlayerColor> winners, int points) {
        List<Message> newMessages = new ArrayList<>(messages);

        newMessages.add(new Message(
                textMaker.playersWon(winners, points),
                0,
                new HashSet<>(),
                new HashSet<>()));

        return new MessageBoard(textMaker, newMessages);
    }

    /**
     * Private helper method that returns a map that maps each animal kind to the number of animals of that kind
     *
     * @param meadow the meadow to count the animals in
     * @param cancelledAnimals the animals that were cancelled
     * @return a map that maps each animal kind to the number of animals of that kind
     */
    private Map<Animal.Kind, Integer> animalCountMap(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        Set<Animal> animals = Area.animals(meadow, cancelledAnimals);
        Map<Animal.Kind, Integer> animalCount = new HashMap<>();

        for (Animal animal : animals)
            animalCount.put(animal.kind(), animalCount.getOrDefault(animal.kind(), 0) + 1);

        return animalCount;
    }

    /**
     * Private helper method that returns the points of a meadow
     *
     * @param animalPoints a map that maps each animal kind to the number of animals of that kind
     * @return the points of the meadow
     */
    private int meadowPoints(Map<Animal.Kind, Integer> animalPoints) {
        return Points.forMeadow(
                animalPoints.getOrDefault(Animal.Kind.MAMMOTH, 0),
                animalPoints.getOrDefault(Animal.Kind.AUROCHS, 0),
                animalPoints.getOrDefault(Animal.Kind.DEER, 0));
    }

    /**
     * Represents a message that is displayed to the players
     *
     * @param text the text of the message
     * @param points the points that the action gains
     * @param scorers the player(s) that scored the points
     * @param tileIds the ids of the tiles that the message is about
     */
    public record Message(String text,
                          int points,
                          Set<PlayerColor> scorers,
                          Set<Integer> tileIds) {

        /**
         * Immutable constructor
         *
         * @throws IllegalArgumentException if the number of points is smaller than 0
         * @throws NullPointerException if the text is null
         */
        public Message {
            Preconditions.checkArgument(points >= 0);
            Objects.requireNonNull(text);
            scorers = Set.copyOf(scorers);
            tileIds = Set.copyOf(tileIds);
        }
    }
}