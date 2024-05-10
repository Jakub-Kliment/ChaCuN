package ch.epfl.chacun;

import java.util.*;

/**
 * Represents a message board that keeps track of
 * the messages that are displayed to the players.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 *
 * @param textMaker creates the text for the message
 * @param messages list of all messages of the message board
 */
public record MessageBoard(TextMaker textMaker, List<Message> messages) {

    /**
     * Compact constructor that ensures immutability
     * by making a defensive copy of the messages.
     */
    public MessageBoard {
        messages = List.copyOf(messages);
    }

    /**
     * Returns the points of each player based on
     * previous messages in the message board.
     *
     * @return a map that maps each player to their points
     */
    public Map<PlayerColor, Integer> points() {
        Map<PlayerColor, Integer> points = new HashMap<>();
        for (Message message : messages)
            for (PlayerColor player : message.scorers)
                points.put(player, points.getOrDefault(player, 0) + message.points);
        return points;
    }

    /**
     * Returns a message board with a new message added for a closed forest
     * that gave points to the majority occupant(s) if the latter is occupied.
     *
     * @param forest the closed forest
     * @return message board
     */
    public MessageBoard withScoredForest(Area<Zone.Forest> forest) {
        if (!forest.isOccupied()) return this;

        int mushrooms = Area.mushroomGroupCount(forest);
        Set<Integer> tileIds = forest.tileIds();
        Set<PlayerColor> scorers = forest.majorityOccupants();
        int points = Points.forClosedForest(tileIds.size(), mushrooms);
        String text = textMaker.playersScoredForest(scorers, points, mushrooms, tileIds.size());

        return new MessageBoard(textMaker, withNewMessage(text, points, scorers, tileIds));
    }

    /**
     * Returns a message board with a new message added for a closed forest
     * with a menhir that allows the player to play one more time.
     *
     * @param player the player that closed the forest
     * @param forest the closed forest containing the menhir
     * @return message board with a new message
     */
    public MessageBoard withClosedForestWithMenhir(PlayerColor player, Area<Zone.Forest> forest) {
        String text = textMaker.playerClosedForestWithMenhir(player);
        return new MessageBoard(textMaker, withNewMessage(text, 0, Set.of(), forest.tileIds()));
    }

    /**
     * Returns a message board with a new message added for a closed river
     * that gave points to the majority occupant(s) if the latter is occupied.
     *
     * @param river the closed river
     * @return message board
     */
    public MessageBoard withScoredRiver(Area<Zone.River> river) {
        if (!river.isOccupied()) return this;

        Set<PlayerColor> scorers = river.majorityOccupants();
        Set<Integer> tileIds = river.tileIds();
        int fish = Area.riverFishCount(river);
        int points = Points.forClosedRiver(tileIds.size(), fish);
        String text = textMaker.playersScoredRiver(scorers, points, fish, tileIds.size());

        return new MessageBoard(textMaker, withNewMessage(text, points, scorers, tileIds));
    }

    /**
     * Returns a message board with a new message added for a scored hunting trap
     * that gave points to the placer for animals in the adjacent meadow.
     *
     * @param scorer the player that scored the hunting trap
     * @param adjacentMeadow the meadow adjacent to the hunting trap
     * @return message board
     */
    public MessageBoard withScoredHuntingTrap(PlayerColor scorer,
                                              Area<Zone.Meadow> adjacentMeadow,
                                              Set<Animal> cancelledAnimals) {
        Map<Animal.Kind, Integer> animalCount = animalCount(adjacentMeadow, cancelledAnimals);
        int points = meadowPoints(animalCount);
        if (points == 0) return this;

        String text = textMaker.playerScoredHuntingTrap(scorer, points, animalCount);
        Set<Integer> tileIds = adjacentMeadow.tileIds();

        return new MessageBoard(textMaker, withNewMessage(text, points, Set.of(scorer), tileIds));
    }

    /**
     * Returns the message board with a new message added for
     * a scored logboat with given points the placer of the latter.
     *
     * @param scorer the player that scored the logboat
     * @param riverSystem the river system containing the logboat
     * @return message board with new message
     */
    public MessageBoard withScoredLogboat(PlayerColor scorer, Area<Zone.Water> riverSystem) {
        int lakes = Area.lakeCount(riverSystem);
        int points = Points.forLogboat(lakes);
        String text = textMaker.playerScoredLogboat(scorer, points, lakes);
        Set<Integer> tileIds = riverSystem.tileIds();

        return new MessageBoard(textMaker, withNewMessage(text, points, Set.of(scorer), tileIds));
    }

    /**
     * Returns a message board with a new message added for a scored meadow
     * to the majority occupant(s) if there are any, and they gained points.
     *
     * @param meadow the meadow that was scored
     * @param cancelledAnimals the animals that were cancelled
     * @return message board
     */
    public MessageBoard withScoredMeadow(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        Map<Animal.Kind, Integer> animalCount = animalCount(meadow, cancelledAnimals);
        int points = meadowPoints(animalCount);
        if (!meadow.isOccupied() || points == 0) return this;

        Set<PlayerColor> scorers = meadow.majorityOccupants();
        String text = textMaker.playersScoredMeadow(scorers, points, animalCount);
        Set<Integer> tileIds = meadow.tileIds();

        return new MessageBoard(textMaker, withNewMessage(text, points, scorers, tileIds));
    }

    /**
     * Returns a message board with a new message added for a scored river system
     * to the majority occupants if there are any, and they gained points.
     *
     * @param riverSystem the river system that was scored
     * @return message board
     */
    public MessageBoard withScoredRiverSystem(Area<Zone.Water> riverSystem) {
        int fish = Area.riverSystemFishCount(riverSystem);
        int points = Points.forRiverSystem(fish);
        if(!riverSystem.isOccupied() || points == 0) return this;

        Set<PlayerColor> scorers = riverSystem.majorityOccupants();
        String text = textMaker.playersScoredRiverSystem(scorers, points, fish);
        Set<Integer> tileIds = riverSystem.tileIds();

        return new MessageBoard(textMaker, withNewMessage(text, points, scorers, tileIds));
    }

    /**
     * Returns the message board with a new message added for a scored pit trap
     * to the majority occupants if there are any, and they gained points.
     *
     * @param adjacentMeadow the meadow adjacent to the pit trap
     * @param cancelledAnimals the animals that were cancelled
     * @return message board
     */
    public MessageBoard withScoredPitTrap(Area<Zone.Meadow> adjacentMeadow,
                                          Set<Animal> cancelledAnimals) {
        Map<Animal.Kind, Integer> animalCount = animalCount(adjacentMeadow, cancelledAnimals);
        int points = meadowPoints(animalCount);
        if (!adjacentMeadow.isOccupied() || points == 0) return this;

        Set<PlayerColor> scorers = adjacentMeadow.majorityOccupants();
        String text = textMaker.playersScoredPitTrap(scorers, points, animalCount);
        Set<Integer> tileIds = adjacentMeadow.tileIds();

        return new MessageBoard(textMaker, withNewMessage(text, points, scorers, tileIds));
    }

    /**
     * Returns the message board with a new message added for a scored raft
     * to the majority occupants if there are any.
     *
     * @param riverSystem the river system containing the raft
     * @return message board
     */
    public MessageBoard withScoredRaft(Area<Zone.Water> riverSystem) {
        if (!riverSystem.isOccupied()) return this;

        int lakes = Area.lakeCount(riverSystem);
        int points = Points.forRaft(lakes);
        Set<Integer> tileIds = riverSystem.tileIds();
        Set<PlayerColor> scorers = riverSystem.majorityOccupants();
        String text = textMaker.playersScoredRaft(scorers, points, lakes);

        return new MessageBoard(textMaker, withNewMessage(text, points, scorers, tileIds));
    }

    /**
     * Returns the message board with a new message added for the end
     * of the game that displays the winner(s) and their total points.
     *
     * @param winners the winner(s) of the game
     * @param points the points of the winner(s)
     * @return message board
     */
    public MessageBoard withWinners(Set<PlayerColor> winners, int points) {
        String text = textMaker.playersWon(winners, points);
        return new MessageBoard(textMaker, withNewMessage(text, 0, Set.of(), Set.of()));
    }

    /**
     * Private helper function that returns a new list of messages
     * with a new message added to the end of the list.
     *
     * @param text the text of the message
     * @param points the points that the action gains
     * @param scorers the player(s) that scored the points
     * @param tileIds the ids of the tiles that the message is about
     * @return a new list of messages with the new message added
     */
    private List<Message> withNewMessage(String text,
                                         int points,
                                         Set<PlayerColor> scorers,
                                         Set<Integer> tileIds) {
        List<Message> newMessages = new ArrayList<>(messages);
        newMessages.add(new Message(text, points, scorers, tileIds));
        return newMessages;
    }

    /**
     * Private helper method that returns a map that maps each animal kind to the number
     * of animals (that are not already cancelled) of that kind in the meadow area.
     *
     * @param meadow the meadow to count the animals in
     * @param cancelledAnimals the animals that were cancelled
     * @return a map that maps each animal kind to the number
     *         of their occurrences in the meadow area
     */
    private Map<Animal.Kind, Integer> animalCount(Area<Zone.Meadow> meadow,
                                                  Set<Animal> cancelledAnimals) {
        Set<Animal> animals = Area.animals(meadow, cancelledAnimals);
        Map<Animal.Kind, Integer> animalCount = new HashMap<>();
        for (Animal animal : animals)
            animalCount.put(animal.kind(), animalCount.getOrDefault(animal.kind(), 0) + 1);
        return animalCount;
    }

    /**
     * Private function that helps to count the points scored for the meadow
     *
     * @param animalPoints a map that maps each animal kind to the number
     *                     of animals of that kind int the meadow
     * @return the points scored for the meadow
     */
    private int meadowPoints(Map<Animal.Kind, Integer> animalPoints) {
        return Points.forMeadow(
                animalPoints.getOrDefault(Animal.Kind.MAMMOTH, 0),
                animalPoints.getOrDefault(Animal.Kind.AUROCHS, 0),
                animalPoints.getOrDefault(Animal.Kind.DEER, 0));
    }

    /**
     * Represents a message that is displayed
     * to the players in the message board.
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
         * Immutable constructor that verifies if the number of points
         * is positive or zero and the text is of the message exists.
         * Keeps immutability by copying both sets of scorers and tile ids.
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