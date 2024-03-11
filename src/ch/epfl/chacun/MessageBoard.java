package ch.epfl.chacun;

import java.util.*;

public record MessageBoard(TextMaker textMaker, List<Message> messages) {

    public MessageBoard {
        // Besoin immuabilite !!!!!!!
        messages = List.copyOf(messages);
    }

    public Map<PlayerColor, Integer> points() {
        return null;
    }

    public MessageBoard withScoredForest(Area<Zone.Forest> forest) {

        if (!forest.isOccupied())
            return this;

        // essayer de trouver mieux pour tileCount !!!!!!!
        int points = Points.forClosedForest(forest.tileIds().size(), Area.mushroomGroupCount(forest));
        messages.add(new Message(
                textMaker.playersScoredForest(forest.majorityOccupants(),
                        points,
                        Area.mushroomGroupCount(forest),
                        forest.tileIds().size()),
                points,
                forest.majorityOccupants(),
                forest.tileIds()));

        return new MessageBoard(textMaker, messages);
    }

    public MessageBoard withClosedForestWithMenhir(PlayerColor player, Area<Zone.Forest> forest) {
        int points = Points.forClosedForest(forest.tileIds().size(), Area.mushroomGroupCount(forest));
        messages.add(new Message(
                textMaker.playerClosedForestWithMenhir(player),
                points,
                forest.majorityOccupants(),
                forest.tileIds()));

        return new MessageBoard(textMaker, messages);
    }

    public MessageBoard withScoredRiver(Area<Zone.River> river) {
        if (!river.isOccupied())
            return this;

        // essayer de trouver mieux pour tileCount !!!!!!!
        int points = Points.forClosedRiver(river.tileIds().size(), Area.riverFishCount(river));
        messages.add(new Message(
                textMaker.playersScoredRiver(river.majorityOccupants(),
                        points,
                        Area.riverFishCount(river),
                        river.tileIds().size()),
                points,
                river.majorityOccupants(),
                river.tileIds()));

        return new MessageBoard(textMaker, messages);
    }

    public MessageBoard withScoredHuntingTrap(PlayerColor scorer, Area<Zone.Meadow> adjacentMeadow) {
        return null;
    }

    public MessageBoard withScoredLogboat(PlayerColor scorer, Area<Zone.Water> riverSystem) {
        return null;
    }

    public MessageBoard withScoredMeadow(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        return null;
    }

    public MessageBoard withScoredRiverSystem(Area<Zone.Water> riverSystem) {
        return null;
    }

    public MessageBoard withScoredPitTrap(Area<Zone.Meadow> adjacentMeadow, Set<Animal> cancelledAnimals) {
        return null;
    }

    public MessageBoard withScoredRaft(Area<Zone.Water> riverSystem) {
        return null;
    }

    public MessageBoard withWinners(Set<PlayerColor> winners, int points) {
        return null;
    }

    public record Message(String text, int points, Set<PlayerColor> scorers, Set<Integer> tileIds) {
        public Message {
            Objects.requireNonNull(text);
            Preconditions.checkArgument(points >= 0);
            scorers = Set.copyOf(scorers);
            tileIds = Set.copyOf(tileIds);
        }
    }
}