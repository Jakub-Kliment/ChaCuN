package ch.epfl.chacun;

import java.util.*;

public record MessageBoard(TextMaker textMaker, List<Message> messages) {

    public MessageBoard {
        // Besoin immuabilite !!!!!!!
        messages = List.copyOf(messages);
    }

    public Map<PlayerColor, Integer> points() {
        Map<PlayerColor, Integer> map = new HashMap<>();

        for (Message message : messages) {
            for (PlayerColor player : message.scorers) {
                if (map.containsKey(player))
                    map.put(player, message.points + map.get(player));
                else
                    map.put(player, message.points);
            }
        }
        return map;
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
        Set<Animal> animals = Area.animals(adjacentMeadow, new HashSet<>());
        Map<Animal.Kind, Integer> animalPoints = new HashMap<>();

        for (Animal animal : animals)
            animalPoints.put(animal.kind(), animalPoints.getOrDefault(animal.kind(), 0) + 1);

        int points = Points.forMeadow(
                animalPoints.get(Animal.Kind.MAMMOTH),
                animalPoints.get(Animal.Kind.AUROCHS),
                animalPoints.get(Animal.Kind.DEER));

        if (points <= 0)
            return this;

        messages.add(new Message(
                textMaker.playerScoredHuntingTrap(scorer, points, animalPoints),
                points,
                new HashSet<>(Set.of(scorer)),
                adjacentMeadow.tileIds()));

        return new MessageBoard(textMaker, messages);
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

        Set<Animal> animals = Area.animals(meadow, cancelledAnimals);
        Map<Animal.Kind, Integer> animalPoints = new HashMap<>();

        for (Animal animal : animals)
            animalPoints.put(animal.kind(), animalPoints.getOrDefault(animal.kind(), 0) + 1);

        int points = Points.forMeadow(
                animalPoints.get(Animal.Kind.MAMMOTH),
                animalPoints.get(Animal.Kind.AUROCHS),
                animalPoints.get(Animal.Kind.DEER));

        if (points <= 0)
            return this;

        messages.add(new Message(
                textMaker().playersScoredMeadow(meadow.majorityOccupants(), points, animalPoints),
                points,
                meadow.majorityOccupants(),
                meadow.tileIds()));

        return new MessageBoard(textMaker, messages);
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
        messages.add(new Message(textMaker.playersWon(winners, points), points, winners, new HashSet<>()));
        return new MessageBoard(textMaker, messages);
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