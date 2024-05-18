package ch.epfl.chacun;

import java.util.*;

/**
 * TextMaker implementation for the French language.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public class TextMakerFr implements TextMaker {

    /**
     * The map of player colors to player names.
     */
    private final Map<PlayerColor, String> playerNames;

    /**
     * Constructs a French text maker with the given map of player
     * colors to player names. The map is copied and made unmodifiable
     * to ensure immutability.
     *
     * @param playerNames the map of player colors to player names
     */
    public TextMakerFr(Map<PlayerColor, String> playerNames) {
        this.playerNames = Map.copyOf(playerNames);
    }

    /**
     * Returns the name of the player by the given color that was
     * associated with the player color when the text maker was created.
     *
     * @param playerColor player color of the player
     * @return the name of the player
     */
    @Override
    public String playerName(PlayerColor playerColor) {
        return playerNames.getOrDefault(playerColor, null);
    }

    /**
     * Returns the textual representation of the given number of points.
     *
     * @param points the number of points
     * @return the textual representation of the number of points
     */
    @Override
    public String points(int points) {
        return STR."\{numberOf(points, "point")}";
    }

    /**
     * Returns the text of a message declaring that a player has closed
     * a forest with a menhir.
     *
     * @param player the player who closed the forest
     * @return the message for the player who closed the forest with a menhir
     */
    @Override
    public String playerClosedForestWithMenhir(PlayerColor player) {
        return STR."\{playerName(player)} a fermé une forêt contenant " +
                "un menhir et peut donc placer une tuile menhir.";
    }

    /**
     * Returns the text of a message declaring that the majority occupants of a newly closed forest,
     * consisting of a certain number of tiles and containing a certain number of mushroom groups,
     * have scored the corresponding points.
     *
     * @param scorers the majority occupants of the forest, who scored the points
     * @param points the points scored
     * @param mushroomGroupCount the number of mushroom groups in the forest
     * @param tileCount the number of tiles that make up the forest
     * @return the message of gained points for the majority occupants of the forest
     */
    @Override
    public String playersScoredForest(Set<PlayerColor> scorers,
                                      int points,
                                      int mushroomGroupCount,
                                      int tileCount) {
        String mushrooms = STR." et de \{numberOf(mushroomGroupCount, "groupe")} de champignons";
        return STR."\{playersObtained(scorers)} \{points(points)} " +
                STR."\{majorityOccupants(scorers)} d'une forêt composée de " +
                STR."\{numberOf(tileCount, "tuile")}" +
                STR."\{addIfGainedPoints(mushrooms, mushroomGroupCount)}.";
    }

    /**
     * Returns the text of a message declaring that the majority occupants of a closed river,
     * consisting of a certain number of tiles and containing a certain number of fish,
     * have scored the corresponding points.
     *
     * @param scorers the majority occupants of the river, who scored the points
     * @param points the points scored
     * @param fishCount the number of fish swimming in the river
     * @param tileCount the number of tiles that make up the river
     * @return the message of gained points for the majority occupants of the river
     */
    @Override
    public String playersScoredRiver(Set<PlayerColor> scorers,
                                     int points,
                                     int fishCount,
                                     int tileCount) {
        String fish = STR." et contenant \{numberOf(fishCount, "poisson")}";
        return STR."\{playersObtained(scorers)} \{points(points)} " +
                STR."\{majorityOccupants(scorers)} d'une rivière composée de " +
                STR."\{numberOf(tileCount, "tuile")}" +
                STR."\{addIfGainedPoints(fish, fishCount)}.";
    }

    /**
     * Returns the text of a message declaring that a player has placed the hunting trap in a meadow
     * containing, on the 8 neighboring tiles of the trap, certain animals, and scored the corresponding points.
     *
     * @param scorer the player who placed the hunting trap
     * @param points the points scored
     * @param animals the animals present in the adjacent meadow to the trap
     * @return the message of gained points for the player who placed the hunting trap
     */
    @Override
    public String playerScoredHuntingTrap(PlayerColor scorer,
                                          int points,
                                          Map<Animal.Kind, Integer> animals) {
        return STR."\{playersObtained(Set.of(scorer))} \{points(points)} " +
                "en plaçant la fosse à pieux dans un pré dans lequel elle est " +
                STR."entourée de \{animalPoints(animals)}.";
    }

    /**
     * Returns the text of a message declaring that a player has scored points by placing
     * the logboat in a water zone containing a certain number of lakes.
     *
     * @param scorer the player who placed the logboat
     * @param points the points scored
     * @param lakeCount the number of lakes in the water zone
     * @return the message of gained points for the player who placed the logboat
     */
    @Override
    public String playerScoredLogboat(PlayerColor scorer, int points, int lakeCount) {
        return STR."\{playersObtained(Set.of(scorer))} \{points(points)} " +
                "en plaçant la pirogue dans un réseau hydrographique contenant " +
                STR."\{numberOf(lakeCount, "lac")}.";
    }

    /**
     * Returns the text of a message declaring that the majority occupants of a meadow
     * containing different types of animals have scored the corresponding points.
     *
     * @param scorers the majority occupants of the meadow
     * @param points the points scored
     * @param animals the animals present in the meadow
     * @return the message of gained points for the majority occupants of the meadow
     */
    @Override
    public String playersScoredMeadow(Set<PlayerColor> scorers,
                                      int points,
                                      Map<Animal.Kind, Integer> animals) {
        return STR."\{playersObtained(scorers)} \{points(points)} " +
                STR."\{majorityOccupants(scorers)} d'un pré contenant " +
                STR."\{animalPoints(animals)}.";
    }

    /**
     * Returns the text of a message declaring that the majority occupants of a river system
     * have scored the corresponding points.
     *
     * @param scorers the majority occupants of the river system
     * @param points the points scored
     * @param fishCount the number of fish in the river
     * @return the message of gained points for the majority occupants of the river system
     */
    @Override
    public String playersScoredRiverSystem(Set<PlayerColor> scorers, int points, int fishCount) {
        return STR."\{playersObtained(scorers)} \{points(points)} " +
                STR."\{majorityOccupants(scorers)} d'un réseau hydrographique contenant " +
                STR."\{numberOf(fishCount, "poisson")}.";
    }

    /**
     * Returns the text of a message declaring that the majority occupants of a meadow
     * containing the pit trap and, on the 8 neighboring tiles of it, certain animals,
     * have scored the corresponding points.
     *
     * @param scorers the majority occupants of the meadow containing the pit trap
     * @param points the points scored
     * @param animals the animals present on the neighboring tiles of the trap
     * @return the message of gained points for the majority occupants of the adjacent meadow to the pit trap
     */
    @Override
    public String playersScoredPitTrap(Set<PlayerColor> scorers,
                                       int points,
                                       Map<Animal.Kind, Integer> animals) {
        return STR."\{playersObtained(scorers)} \{points(points)} " +
                STR."\{majorityOccupants(scorers)} d'un pré contenant la grande fosse à pieux " +
                STR."entourée de \{animalPoints(animals)}.";
    }

    /**
     * Returns the text of a message declaring that the majority occupants of a river system
     * containing the raft have scored the corresponding points.
     *
     * @param scorers the majority occupants of the river system containing the raft
     * @param points the points scored
     * @param lakeCount the number of lakes in the river system
     * @return the message of gained points for the majority occupants of the river system containing the raft
     */
    @Override
    public String playersScoredRaft(Set<PlayerColor> scorers, int points, int lakeCount) {
        return STR."\{playersObtained(scorers)} \{points(points)} " +
                STR."\{majorityOccupants(scorers)} d'un réseau hydrographique " +
                STR."contenant le radeau et \{ numberOf(lakeCount, "lac")}.";
    }

    /**
     * Returns the text of a message declaring that the game
     * has ended and the given player(s) have won.
     *
     * @param winners the players who have won
     * @param points the number of points the winners have scored
     * @return the message of the end of the game and the winners
     */
    @Override
    public String playersWon(Set<PlayerColor> winners, int points) {
        return STR."\{playersObtained(winners)} la partie avec \{points(points)} !";
    }

    /**
     * Returns the text of a message that asks the player to click on a tile
     * if they want to occupy it or not.
     *
     * @return the message asking the player to click to occupy a tile
     */
    @Override
    public String clickToOccupy() {
        return "Cliquez sur le pion ou la hutte que vous désirez placer, ou ici pour ne pas en placer.";
    }

    /**
     * Returns the text of a message that asks the player to click on a pawn
     * or hut if they want to un occupy it or not.
     *
     * @return the message asking the player to click to un occupy a pawn or hut
     */
    @Override
    public String clickToUnoccupy() {
        return "Cliquez sur le pion que vous désirez reprendre, ou ici pour ne pas en reprendre.";
    }

    /**
     * Private helper method that returns the textual representation of the players
     * who have obtained the points. The players are sorted by their color in order
     * of the enumeration (PlayerColor).
     *
     * @param players the set of players who have obtained the points
     * @return the textual representation of the players who have obtained the points
     */
    private String playersObtained(Set<PlayerColor> players) {
        // Sort the players by their color
        List<PlayerColor> sortedPlayers = players.stream()
                .sorted(PlayerColor::compareTo)
                .toList();

        // Create the textual representation of the players
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

    /**
     * Private helper method that returns the textual representation of the animals
     * present in the meadow. The animals are sorted by their kind in order of the
     * enumeration (Animal.Kind).
     *
     * @param animals the map of animal kinds to the number of animals present in the meadow
     * @return the textual representation of the animals present in the meadow and their number
     */
    private String animalPoints(Map<Animal.Kind, Integer> animals) {
        // Sort the animals by their kind and keep only keep those who scored points
        List<Animal.Kind> sortedAnimals = animals.keySet()
                .stream()
                .filter(kind -> kind != Animal.Kind.TIGER)
                .filter(kind -> animals.get(kind) > 0)
                .sorted(Animal.Kind::compareTo)
                .toList();

        // Create the textual representation of the animals and their number
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sortedAnimals.size(); i++) {
            sb.append(numberOf(animals.get(sortedAnimals.get(i)),
                    animalName(sortedAnimals.get(i))));
            if (i < sortedAnimals.size() - 2)
                sb.append(", ");
            else if (i == sortedAnimals.size() - 2)
                sb.append(" et ");
        }
        return sb.toString();
    }

    /**
     * Private helper method that returns the name of the animal kind in French.
     *
     * @param animalKind the kind of the animal
     * @return the name of the animal kind in French
     */
    private String animalName(Animal.Kind animalKind) {
        return switch (animalKind) {
            case MAMMOTH -> "mammouth";
            case AUROCHS -> "auroch";
            case DEER -> "cerf";
            default -> "";
        };
    }

    /**
     * Private helper method that returns the textual representation of the majority occupants
     * of a meadow, forest, river  or river system. The representation is in the singular form
     * if there is only one occupant, and in the plural form if there are more than one.
     *
     * @param players the set of majority occupants
     * @return the textual representation of the majority occupants
     */
    private String majorityOccupants(Set<PlayerColor> players) {
        return players.size() == 1
                ? "en tant qu'occupant·e majoritaire"
                : "en tant qu'occupant·e·s majoritaires";
    }

    /**
     * Private helper method that returns the textual representation of the given number
     * and the given string in French. The string is in the singular form if the number
     * is equal to one, and in the plural form if the number is greater than one.
     *
     * @param number the number
     * @param s the string
     * @return the textual representation of the number and the string
     */
    private String numberOf(int number, String s) {
        return STR."\{number} \{s}\{plural(number)}";
    }

    /**
     * Private method that returns the plural form of the string
     * if the given number of points  is greater than one.
     *
     * @param points the number of points
     * @return the plural form of the string if the number is greater than one
     */
    private String plural(int points) {
        return points > 1 ? "s" : "";
    }

    /**
     * Private helper method that returns the string if the given number of points
     * is greater than zero. That is, the string is added only if the text has a meaning.
     *
     * @param s the string
     * @param points the number of points
     * @return the string to add if the number of points is greater than zero
     */
    private String addIfGainedPoints(String s, int points) {
        return points > 0 ? s : "";
    }

}
