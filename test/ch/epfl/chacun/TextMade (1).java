package ch.epfl.chacun;

import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

public class TextMade implements TextMaker{
    @Override
    public String playerName(PlayerColor playerColor) {
        return new StringJoiner(" ")
                .add(String.valueOf(playerColor))
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
                .add("Forest closed with menhir")
                .add(String.valueOf(player))
                .toString();
    }

    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        return new StringJoiner(" ")
                .add("Forest closed")
                .add(scorers.toString())
                .add(String.valueOf(points))
                .add(String.valueOf(mushroomGroupCount))
                .add(String.valueOf(tileCount))
                .toString();
    }

    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        return new StringJoiner(" ")
                .add("River closed")
                .add(scorers.toString())
                .add(String.valueOf(points))
                .add(String.valueOf(fishCount))
                .add(String.valueOf(tileCount))
                .toString();
    }

    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        int total = 0;
        for(Animal.Kind animalKind : animals.keySet()){
            total += animals.get(animalKind);
        }

        return new StringJoiner(" ")
                .add("Hunting trap")
                .add(String.valueOf(scorer))
                .add(String.valueOf(points))
                //.add(animals.toString())
                .add(String.valueOf(animals.keySet().size()) + "different animals")
                .add(String.valueOf(total) + "in total")
                .toString();
    }

    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return new StringJoiner(" ")
                .add("Log boat")
                .add(String.valueOf(scorer))
                .add(String.valueOf(points))
                .add(String.valueOf(lakeCount))
                .toString();
    }

    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        int total = 0;
        for(Animal.Kind animalKind : animals.keySet()){
            total += animals.get(animalKind);
        }

        return new StringJoiner(" ")
                .add("Meadow closed")
                .add(scorers.toString())
                .add(String.valueOf(points))
                //.add(animals.toString())
                .add(String.valueOf(animals.keySet().size()) + "different animals")
                .add(String.valueOf(total) + "in total")
                .toString();
    }

    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return new StringJoiner(" ")
                .add("River system")
                .add(scorers.toString())
                .add(String.valueOf(points))
                .add(String.valueOf(fishCount))
                .toString();
    }

    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        int total = 0;
        for(Animal.Kind animalKind : animals.keySet()){
            total += animals.get(animalKind);
        }

        return new StringJoiner(" ")
                .add("Pit trap")
                .add(scorers.toString())
                .add(String.valueOf(points))
                //.add(animals.toString())
                .add(String.valueOf(animals.keySet().size()) + "different animals")
                .add(String.valueOf(total) + "in total")
                .toString();
    }

    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        return new StringJoiner(" ")
                .add("Raft")
                .add(scorers.toString())
                .add(String.valueOf(points))
                .add(String.valueOf(lakeCount))
                .toString();
    }

    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        return new StringJoiner(" ")
                .add("Winners")
                .add(winners.toString())
                .add(String.valueOf(points))
                .toString();
    }

    @Override
    public String clickToOccupy() {
        return null;
    }

    @Override
    public String clickToUnoccupy() {
        return null;
    }
}
//
