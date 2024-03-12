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
        // besoin de copie de messages ou pas
        messages.add(new Message(
                textMaker.playersScoredForest(
                        forest.majorityOccupants(),
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
                textMaker.playersScoredRiver(
                        river.majorityOccupants(),
                        points,
                        Area.riverFishCount(river),
                        river.tileIds().size()),
                points,
                river.majorityOccupants(),
                river.tileIds()));

        return new MessageBoard(textMaker, messages);
    }

    public MessageBoard withScoredHuntingTrap(PlayerColor scorer, Area<Zone.Meadow> adjacentMeadow) {
/*
        Set<Animal> cancelledAnimals = new HashSet<>();
        for (Zone.Meadow meadow : adjacentMeadow.zones()) {
            if (meadow.animals().contains(Animal.Kind.TIGER)) {
                cancelledAnimals.add(meadow.animals());
            }
        }

        Set<Animal> animals = Area.animals(adjacentMeadow, );

        int mammothCount = 0;
        int aurochsCount = 0;
        int deerCount = 0;

        for (Animal animal : animals) {
            if (animal.kind().equals(Animal.Kind.MAMMOTH))
                mammothCount++;
            else if (animal.kind().equals(Animal.Kind.AUROCHS))
                aurochsCount++;
            else if (animal.kind().equals(Animal.Kind.DEER))
                deerCount++;
        }



        int points = Points.forMeadow(mammothCount, aurochsCount, deerCount);


 */
        return null;
    }

    public MessageBoard withScoredLogboat(PlayerColor scorer, Area<Zone.Water> riverSystem) {
        int points = Points.forRiverSystem(Area.riverSystemFishCount(riverSystem));
        messages.add(new Message(
                textMaker().playerScoredLogboat(
                        scorer,
                        points,
                        Area.riverSystemFishCount(riverSystem)),
                points,
                new HashSet<>(Set.of(scorer)),
                riverSystem.tileIds()));
        return new MessageBoard(textMaker, messages);
    }

    public MessageBoard withScoredMeadow(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {

        if (!meadow.isOccupied())
            return this;

        return null;
    }

    public MessageBoard withScoredRiverSystem(Area<Zone.Water> riverSystem) {

        int points = Points.forRiverSystem(Area.riverSystemFishCount(riverSystem));

        if(!riverSystem.isOccupied() || points == 0)
            return this;

        messages.add(new Message(
                textMaker().playersScoredRiverSystem(
                        riverSystem.majorityOccupants(),
                        points,
                        Area.riverSystemFishCount(riverSystem)),
                points,
                riverSystem.majorityOccupants(),
                riverSystem.tileIds()
                ));
        return new MessageBoard(textMaker, messages);
    }

    public MessageBoard withScoredPitTrap(Area<Zone.Meadow> adjacentMeadow, Set<Animal> cancelledAnimals) {
        return null;
    }

    public MessageBoard withScoredRaft(Area<Zone.Water> riverSystem) {

        if (!riverSystem.isOccupied())
            return this;

        int points = Points.forRiverSystem(Area.riverSystemFishCount(riverSystem));
        messages.add(new Message(
                textMaker.playersScoredRaft(
                        riverSystem.majorityOccupants(),
                        points,
                        Area.lakeCount(riverSystem)),
                points,
                riverSystem.majorityOccupants(),
                riverSystem.tileIds()));
        return new MessageBoard(textMaker, messages);
    }

    public MessageBoard withWinners(Set<PlayerColor> winners, int points) {
        // no tiles !!!!!!!
        messages.add(new Message(textMaker.playersWon(winners, points), points, winners, new HashSet<>()));
        return new MessageBoard(textMaker, messages);
    }

    public record Message(String text, int points, Set<PlayerColor> scorers, Set<Integer> tileIds) {
        public Message {
            Objects.requireNonNull(text);
            // !!!!!!!! pas erreur
            Preconditions.checkArgument(points >= 0);
            scorers = Set.copyOf(scorers);
            tileIds = Set.copyOf(tileIds);
        }
    }
}