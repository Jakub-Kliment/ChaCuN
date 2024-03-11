package ch.epfl.chacun;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public record MessageBoard(TextMaker textMaker, List<Message> messages) {

    public MessageBoard {
        // Besoin immuabilite !!!!!!!
        messages = List.copyOf(messages);
    }

    public Map<PlayerColor, Integer> points() {
        return null;
    }

    public MessageBoard withScoredForest(Area<Zone.Forest> forest) {
        return null;
    }

    public MessageBoard withClosedForestWithMenhir(PlayerColor player, Area<Zone.Forest> forest) {
        return null;
    }

    public MessageBoard withScoredRiver(Area<Zone.River> river) {
        return null;
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