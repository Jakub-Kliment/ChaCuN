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


        List<Message> newMessages = new ArrayList<>(messages);
        // besoin de copie de messages ou pas
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

    public MessageBoard withClosedForestWithMenhir(PlayerColor player, Area<Zone.Forest> forest) {

        List<Message> newMessages = new ArrayList<>(messages);

        // demander !!!!!
        newMessages.add(new Message(
                textMaker.playerClosedForestWithMenhir(player),
                0,
                new HashSet<>(),
                new HashSet<>()));

        return new MessageBoard(textMaker, newMessages);
    }

    public MessageBoard withScoredRiver(Area<Zone.River> river) {

        if (!river.isOccupied())
            return this;

        // essayer de trouver mieux pour tileCount !!!!!!!
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

    public MessageBoard withScoredHuntingTrap(PlayerColor scorer, Area<Zone.Meadow> adjacentMeadow) {

        Map<Animal.Kind, Integer> animalMap = animalCountMap(adjacentMeadow, new HashSet<>());

        int points = meadowPoints(animalMap);

        if (points <= 0)
            return this;

        List<Message> newMessages = new ArrayList<>(messages);


        newMessages.add(new Message(
                textMaker.playerScoredHuntingTrap(scorer, points, animalMap),
                points,
                new HashSet<>(Set.of(scorer)),
                adjacentMeadow.tileIds()));

        return new MessageBoard(textMaker, newMessages);
    }

    public MessageBoard withScoredLogboat(PlayerColor scorer, Area<Zone.Water> riverSystem) {

        int points = Points.forRiverSystem(Area.riverSystemFishCount(riverSystem));

        List<Message> newMessages = new ArrayList<>(messages);

        newMessages.add(new Message(
                textMaker().playerScoredLogboat(
                        scorer,
                        points,
                        Area.riverSystemFishCount(riverSystem)),
                points,
                new HashSet<>(Set.of(scorer)),
                riverSystem.tileIds()));
        return new MessageBoard(textMaker, newMessages);
    }

    public MessageBoard withScoredMeadow(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {

        if (!meadow.isOccupied())
            return this;

        Map<Animal.Kind, Integer> animalMap = animalCountMap(meadow, cancelledAnimals);

        int points = meadowPoints(animalMap);

        if (points <= 0)
            return this;

        List<Message> newMessages = new ArrayList<>(messages);

        newMessages.add(new Message(
                textMaker().playersScoredMeadow(meadow.majorityOccupants(), points, animalMap),
                points,
                meadow.majorityOccupants(),
                meadow.tileIds()));

        return new MessageBoard(textMaker, newMessages);
    }

    public MessageBoard withScoredRiverSystem(Area<Zone.Water> riverSystem) {

        int points = Points.forRiverSystem(Area.riverSystemFishCount(riverSystem));

        if(!riverSystem.isOccupied() || points == 0)
            return this;

        List<Message> newMessages = new ArrayList<>(messages);

        newMessages.add(new Message(
                textMaker().playersScoredRiverSystem(
                        riverSystem.majorityOccupants(),
                        points,
                        Area.riverSystemFishCount(riverSystem)),
                points,
                riverSystem.majorityOccupants(),
                riverSystem.tileIds()
                ));
        return new MessageBoard(textMaker, newMessages);
    }

    public MessageBoard withScoredPitTrap(Area<Zone.Meadow> adjacentMeadow, Set<Animal> cancelledAnimals) {

        if (!adjacentMeadow.isOccupied())
            return this;

        Map<Animal.Kind, Integer> animalMap = animalCountMap(adjacentMeadow, cancelledAnimals);

        int points = meadowPoints(animalMap);

        if (points <= 0)
            return this;

        List<Message> newMessages = new ArrayList<>(messages);

        newMessages.add(new Message(
                textMaker().playersScoredMeadow(adjacentMeadow.majorityOccupants(), points, animalMap),
                points,
                adjacentMeadow.majorityOccupants(),
                adjacentMeadow.tileIds()));

        return new MessageBoard(textMaker, newMessages);
    }

    public MessageBoard withScoredRaft(Area<Zone.Water> riverSystem) {

        if (!riverSystem.isOccupied())
            return this;

        int points = Points.forRiverSystem(Area.riverSystemFishCount(riverSystem));

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

    public MessageBoard withWinners(Set<PlayerColor> winners, int points) {

        List<Message> newMessages = new ArrayList<>(messages);

        newMessages.add(new Message(textMaker.playersWon(winners, points), points, winners, new HashSet<>()));

        return new MessageBoard(textMaker, newMessages);
    }

    private Map<Animal.Kind, Integer> animalCountMap(Area<Zone.Meadow> meadow, Set<Animal> cancelledAnimals) {
        Set<Animal> animals = Area.animals(meadow, cancelledAnimals);
        Map<Animal.Kind, Integer> animalCount = new HashMap<>();

        for (Animal.Kind kind : Animal.Kind.values())
            animalCount.put(kind, 0);

        for (Animal animal : animals)
            animalCount.put(animal.kind(), animalCount.get(animal.kind()) + 1);

        return animalCount;
    }

    private int meadowPoints(Map<Animal.Kind, Integer> animalPoints) {
        return Points.forMeadow(
                animalPoints.get(Animal.Kind.MAMMOTH),
                animalPoints.get(Animal.Kind.AUROCHS),
                animalPoints.get(Animal.Kind.DEER));
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