package ch.epfl.chacun;

import java.util.*;

public class TextMakerFr implements TextMaker {

    private final Map<PlayerColor, String> playerNames;

    public TextMakerFr(Map<PlayerColor, String> playerNames) {
        this.playerNames = Collections.unmodifiableMap(playerNames);
    }

    @Override
    public String playerName(PlayerColor playerColor) {
        Preconditions.checkArgument(playerColor != null && playerNames.containsKey(playerColor));
        return playerNames.get(playerColor);
    }

    @Override
    public String points(int points) {
        Preconditions.checkArgument(points >= 0);
        return STR."\{points} point\{plural(points)}";
    }

    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return STR."\{playerName(player)} a fermé une forêt contenant un menhir et peut donc placer une tuile menhir.";
    }

    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        String mushrooms = STR." et de \{numberOf(mushroomGroupCount, "groupe")} de champignons";
        return STR."\{playersObtained(scorers)} \{points(points)} \{majorityOccupants(scorers)} d'une forêt composée de \{numberOf(tileCount, "tuile")}\{addIfGainedPoints(mushrooms, mushroomGroupCount)}.";
    }

    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        String fish = STR." et contenant \{numberOf(fishCount, "poisson")}";
        return STR."\{playersObtained(scorers)} \{points(points)} \{majorityOccupants(scorers)} d'une rivière composée de \{numberOf(tileCount, "tuile")}\{addIfGainedPoints(fish, fishCount)}.";
    }

    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        Preconditions.checkArgument(scorer != null);
        return STR."\{playersObtained(Set.of(scorer))} \{points(points)} en plaçant la fosse à pieux dans un pré dans lequel elle est entourée de \{animalPoints(animals)}.";
    }

    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        Preconditions.checkArgument(scorer != null);
        return STR."\{playersObtained(Set.of(scorer))} \{points(points)} en plaçant la pirogue dans un réseau hydrographique contenant \{numberOf(lakeCount, "lac")}.";
    }

    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{playersObtained(scorers)} \{points(points)} \{majorityOccupants(scorers)} d'un pré contenant \{animalPoints(animals)}.";
    }

    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return STR."\{playersObtained(scorers)} \{points(points)} \{majorityOccupants(scorers)} d'un réseau hydrographique contenant \{numberOf(fishCount, "poisson")}.";
    }

    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return STR."\{playersObtained(scorers)} \{points(points)} \{majorityOccupants(scorers)} d'un pré contenant la grande fosse à pieux entourée de \{animalPoints(animals)}.";
    }

    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        return STR."\{playersObtained(scorers)} \{points(points)} \{majorityOccupants(scorers)} d'un réseau hydrographique contenant le radeau et \{ numberOf(lakeCount, "lac")}.";
    }

    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        return STR."\{playersObtained(winners)} la partie avec \{points(points)}!";
    }

    @Override
    public String clickToOccupy() {
        return "Cliquez sur le pion ou la hutte que vous désirez placer, ou ici pour ne pas en placer.";
    }

    @Override
    public String clickToUnoccupy() {
        return "Cliquez sur le pion que vous désirez reprendre, ou ici pour ne pas en reprendre.";
    }

    private String playersObtained(Set<PlayerColor> players) {
        Preconditions.checkArgument(!players.isEmpty());
        List<PlayerColor> sortedPlayers = players.stream()
                .sorted(PlayerColor::compareTo)
                .toList();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sortedPlayers.size(); i++) {
            sb.append(playerName(sortedPlayers.get(i)));
            if (i < sortedPlayers.size() - 2)
                sb.append(", ");
            else if (i == sortedPlayers.size() - 2)
                sb.append(" et ");
        }
        return sortedPlayers.size() > 1 ?
                sb.append(" ont remporté").toString()
                : sb.append(" a remporté").toString();
    }

    private String animalPoints(Map<Animal.Kind, Integer> animals) {
        Preconditions.checkArgument(!animals.isEmpty());
        List<Animal.Kind> sortedAnimals = animals.keySet().stream()
                .filter(animal -> animals.get(animal) > 0)
                .sorted(Animal.Kind::compareTo)
                .toList();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sortedAnimals.size(); i++) {
            sb.append(STR."\{numberOf(animals.get(sortedAnimals.get(i)),
                    animalName(sortedAnimals.get(i)))}");
            if (i < sortedAnimals.size() - 2)
                sb.append(", ");
            else if (i == sortedAnimals.size() - 2)
                sb.append(" et ");
        }
        return sb.toString();
    }

    private String animalName(Animal.Kind animalKind) {
        return switch (animalKind) {
            case MAMMOTH -> "mammouth";
            case AUROCHS -> "auroch";
            case DEER -> "cerf";
            default -> "";
        };
    }

    private String majorityOccupants(Set<PlayerColor> players) {
        return players.size() == 1
                ? "en tant qu'occupant·e majoritaire"
                : "en tant qu'occupant·e·s majoritaires";
    }

    private String numberOf(int number, String s) {
        return STR."\{number} \{s}\{plural(number)}";
    }
    private String plural(int points) {
        return points > 1 ? "s" : "";
    }

    private String addIfGainedPoints(String s, int points) {
        return points > 0 ? s : "";
    }

}
