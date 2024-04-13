package ch.epfl.chacun;

import java.util.*;

public class TextMakerFr implements TextMaker {

    private final Map<PlayerColor, String> playerNames;

    public TextMakerFr(Map<PlayerColor, String> playerNames) {
        this.playerNames = Collections.unmodifiableMap(playerNames);
    }

    @Override
    public String playerName(PlayerColor playerColor) {
        Preconditions.checkArgument(playerNames.containsKey(playerColor));
        return playerNames.get(playerColor);
    }

    @Override
    public String points(int points) {
        Preconditions.checkArgument(points >= 0);
        return STR."\{points}";
    }

    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return STR."\{playerName(player)} a fermé une forêt contenant un menhir et peut donc placer une tuile menhir.";
    }

    @Override
    public String playersScoredForest(Set<PlayerColor> scorers, int points, int mushroomGroupCount, int tileCount) {
        Preconditions.checkArgument(!scorers.isEmpty());
        String mushrooms = STR." et de \{points(mushroomGroupCount)} groupe\{plural(mushroomGroupCount)} de champignons";
        return STR."\{playersObtained(scorers)} \{points(points)} points en tant qu'\{majorityOccupants(scorers)} d'une forêt composée de \{points(tileCount)} tuiles\{addIfGainedPoints(mushrooms, mushroomGroupCount)}.";
    }

    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers, int points, int fishCount, int tileCount) {
        Preconditions.checkArgument(!scorers.isEmpty());
        String fish = STR." et contenant \{points(fishCount)} poisson\{plural(fishCount)}";
        return STR."\{playersObtained(scorers)} \{points(points)} points en tant qu'\{majorityOccupants(scorers)} d'une rivière composée de \{points(tileCount)} tuiles\{addIfGainedPoints(fish, fishCount)}.";
    }

    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer, int points, Map<Animal.Kind, Integer> animals) {
        Preconditions.checkArgument(!animals.isEmpty());
        return STR."\{playersObtained(Set.of(scorer))} \{points(points)} points en plaçant la fosse à pieux dans un pré dans lequel elle est entourée de \{animalPoints(animals)}.";
    }

    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return null;
    }

    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return null;
    }

    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return null;
    }

    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers, int points, Map<Animal.Kind, Integer> animals) {
        return null;
    }

    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        return null;
    }

    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        return null;
    }

    @Override
    public String clickToOccupy() {
        return null;
    }

    @Override
    public String clickToUnoccupy() {
        return null;
    }

    private String playersObtained(Set<PlayerColor> players) {
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
        List<Animal.Kind> sortedAnimals = animals.keySet().stream()
                .sorted(Animal.Kind::compareTo)
                .toList();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sortedAnimals.size(); i++) {
            int score = animals.get(sortedAnimals.get(i));
            sb.append(STR."\{points(score)} \{animal(sortedAnimals.get(i))}\{plural(score)}");
            if (i < sortedAnimals.size() - 2)
                sb.append(", ");
            else if (i == sortedAnimals.size() - 2)
                sb.append(" et ");
        }
        return sb.toString();
    }

    private String animal(Animal.Kind animalKind) {
        return switch (animalKind) {
            case MAMMOTH -> "mammouth";
            case AUROCHS -> "auroch";
            case DEER -> "cerf";
            default -> "";
        };
    }

    private String majorityOccupants(Set<PlayerColor> players) {
        return players.size() == 1
                ? "occupant·e majoritaire"
                : "occupant·e·s majoritaires";
    }

    private String plural(int points) {
        return points > 1 ? "s" : "";
    }

    private String addIfGainedPoints(String s, int points) {
        return points > 0 ? s : "";
    }

}
