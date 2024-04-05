package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the state of the game at a given moment.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 */
public record GameState(List<PlayerColor> players, TileDecks tileDecks, Tile tileToPlace,
                        Board board, Action nextAction, MessageBoard messageBoard) {

    /**
     * Constructs a new game state.
     *
     * @param players      the players of the game
     * @param tileDecks    the decks of tiles
     * @param tileToPlace  the tile to place
     * @param board        the board of the game
     * @param nextAction   the next action to perform
     * @param messageBoard the message board
     */
    public GameState {
        Preconditions.checkArgument(players.size() >= 2);
        players = List.copyOf(players);
        Preconditions.checkArgument(tileToPlace == null ^ nextAction.equals(Action.PLACE_TILE));
        Objects.requireNonNull(tileDecks);
        Objects.requireNonNull(board);
        Objects.requireNonNull(nextAction);
        Objects.requireNonNull(messageBoard);
    }

    /**
     * Represents the possible actions that can be performed in the game.
     */
    public enum Action {
        START_GAME,
        PLACE_TILE,
        RETAKE_PAWN,
        OCCUPY_TILE,
        END_GAME
    }

    /**
     * Creates the initial game state.
     *
     * @param players    the players of the game
     * @param tileDecks  the decks of tiles
     * @param textMaker  the text maker
     * @return the initial game state
     */
    public static GameState initial(List<PlayerColor> players, TileDecks tileDecks, TextMaker textMaker) {
        return new GameState(players, tileDecks, null, Board.EMPTY,
                Action.START_GAME, new MessageBoard(textMaker, new ArrayList<>()));
    }

    /**
     * Returns the current player of the game.
     *
     * @return the current player
     */
    public PlayerColor currentPlayer() {
        if (nextAction.equals(Action.START_GAME) || nextAction.equals(Action.END_GAME))
            return null;
        return players.getFirst();
    }

    /**
     * Returns the number of free occupants of a given kind for a given player.
     *
     * @param player the player
     * @param kind   the kind of occupant
     * @return the number of free occupants
     */
    public int freeOccupantsCount(PlayerColor player, Occupant.Kind kind) {
        return Occupant.occupantsCount(kind) - board.occupantCount(player, kind);
    }

    /**
     * Returns the potential occupants for the last placed tile.
     *
     * @return the potential occupants
     */
    public Set<Occupant> lastTilePotentialOccupants() {
        Preconditions.checkArgument(!board.equals(Board.EMPTY));
       Set<Occupant> potentialOccupants = board.lastPlacedTile().potentialOccupants();
       // Remove occupants if there are no free ones left
       potentialOccupants.removeIf(
               o -> freeOccupantsCount(currentPlayer(), o.kind()) == 0);

       // Remove occupants if the area is already occupied
       potentialOccupants.removeIf((occupant) -> switch (board.lastPlacedTile().zoneWithId(occupant.zoneId())) {
           case Zone.Meadow meadow
                   when occupant.kind().equals(Occupant.Kind.PAWN) -> board.meadowArea(meadow).isOccupied();
           case Zone.Forest forest
                   when occupant.kind().equals(Occupant.Kind.PAWN) -> board.forestArea(forest).isOccupied();
           case Zone.River river
                   when occupant.kind().equals(Occupant.Kind.PAWN) -> board.riverArea(river).isOccupied();
           case Zone.Water water
                   when occupant.kind().equals(Occupant.Kind.HUT) -> board.riverSystemArea(water).isOccupied();
           default -> false;
       });
       return potentialOccupants;
    }

    /**
     * Returns a new game state with the starting tile placed.
     *
     * @return new game state with the starting tile placed
     */
    public GameState withStartingTilePlaced() {
        Preconditions.checkArgument(nextAction.equals(Action.START_GAME));
        Board newBoard = board.withNewTile(new PlacedTile(tileDecks.topTile(Tile.Kind.START),
                null, Rotation.NONE, Pos.ORIGIN));
        Tile nextTile = tileDecks.topTile(Tile.Kind.NORMAL);
        TileDecks newTileDecks = tileDecks.withTopTileDrawn(Tile.Kind.START).withTopTileDrawn(Tile.Kind.NORMAL);
        return new GameState(players, newTileDecks, nextTile, newBoard, Action.PLACE_TILE, messageBoard);
    }

    /**
     * Returns a new game state with the given tile placed.
     *
     * @param tile the tile to place
     * @return new game state with the given tile placed
     */
    public GameState withPlacedTile(PlacedTile tile) {
        Preconditions.checkArgument(nextAction.equals(Action.PLACE_TILE) && tile.occupant() == null);
        MessageBoard newMessageBoard = messageBoard;
        Board newBoard = board.withNewTile(tile);
        Action newAction;

        // Check if the tile contains a special power
        switch (tile.specialPowerZone()) {
            case Zone.Meadow meadow
                    when meadow.specialPower().equals(Zone.SpecialPower.HUNTING_TRAP) -> {
                Area<Zone.Meadow> adjacentMeadow = newBoard.adjacentMeadow(tile.pos(), meadow);
                newBoard = newBoard.withMoreCancelledAnimals(Area.animals(adjacentMeadow, newBoard.cancelledAnimals()));
                newMessageBoard = newMessageBoard.withScoredHuntingTrap(currentPlayer(), adjacentMeadow);
                newAction = Action.OCCUPY_TILE;
            }
            case Zone.Lake lake
                    when lake.specialPower().equals(Zone.SpecialPower.LOGBOAT) -> {
                newMessageBoard = newMessageBoard.withScoredLogboat(currentPlayer(), newBoard.riverSystemArea(lake));
                newAction = Action.OCCUPY_TILE;
            }
            case Zone.Meadow meadow
                    when meadow.specialPower().equals(Zone.SpecialPower.SHAMAN) ->
                    newAction = Action.RETAKE_PAWN;
            case null, default ->
                    newAction = Action.OCCUPY_TILE;
        }

        GameState newGameState = new GameState(players, tileDecks, null, newBoard, newAction, newMessageBoard);
        // Check if the player can occupy the tile
        if (newAction.equals(Action.OCCUPY_TILE) ||
                freeOccupantsCount(currentPlayer(), Occupant.Kind.PAWN) == Occupant.occupantsCount(Occupant.Kind.PAWN))
            return newGameState.withTurnFinishedIfOccupationImpossible();
        // If player can retake a pawn
        return newGameState;
    }

    /**
     * Returns a new game state with the given occupant removed.
     *
     * @param occupant the occupant to remove
     * @return new game state with the given occupant removed
     */
    public GameState withOccupantRemoved(Occupant occupant) {
        Preconditions.checkArgument(nextAction.equals(Action.RETAKE_PAWN)
                && (occupant == null || occupant.kind().equals(Occupant.Kind.PAWN)));

        if (occupant == null || board.occupantCount(currentPlayer(), occupant.kind()) == 0)
            return withTurnFinishedIfOccupationImpossible();

        Board newBoard = board.withoutOccupant(occupant);
        return new GameState(players, tileDecks, null, newBoard, Action.OCCUPY_TILE, messageBoard);
    }

    /**
     * Returns a new game state with the given occupant placed.
     *
     * @param occupant the occupant to place
     * @return new game state with the given occupant placed
     */
    public GameState withNewOccupant(Occupant occupant) {
        Preconditions.checkArgument(nextAction.equals(Action.OCCUPY_TILE));
        if (occupant == null)
            return withTurnFinished();
        Board newBoard = board.withOccupant(occupant);
        return new GameState(players, tileDecks, null, newBoard, nextAction, messageBoard).withTurnFinished();
    }

    /**
     * Private methode that returns a new game state with the turn finished if the occupation is impossible,
     * otherwise returns a new game state with the action set to OCCUPY_TILE.
     *
     * @return new game state with the turn finished
     */
    private GameState withTurnFinishedIfOccupationImpossible() {
        if (lastTilePotentialOccupants().isEmpty())
            return withTurnFinished();
        else
            return new GameState(players, tileDecks, null, board, Action.OCCUPY_TILE, messageBoard);
    }

    /**
     * Private method that finishes a turn of a player.
     *
     * @return new game state with the turn finished
     */
    private GameState withTurnFinished() {
        MessageBoard newMessageBoard = messageBoard;
        Board newBoard = board;

        // Score the forests and rivers closed by the last tile
        for (Area<Zone.Forest> forestArea : newBoard.forestsClosedByLastTile()) {
            if (Area.hasMenhir(forestArea))
                newMessageBoard = newMessageBoard.withClosedForestWithMenhir(currentPlayer(), forestArea);
            newMessageBoard = newMessageBoard.withScoredForest(forestArea);
        }

        for (Area<Zone.River> riverArea : newBoard.riversClosedByLastTile())
            newMessageBoard = newMessageBoard.withScoredRiver(riverArea);

        newBoard = newBoard.withoutGatherersOrFishersIn(
                newBoard.forestsClosedByLastTile(), newBoard.riversClosedByLastTile());

        TileDecks newTileDecks;
        // Check if the next tile is a menhir tile or a normal tile
        Tile.Kind kind = (newBoard.forestsClosedByLastTile().stream().anyMatch(Area::hasMenhir) &&
                newBoard.lastPlacedTile().kind().equals(Tile.Kind.NORMAL)) ? Tile.Kind.MENHIR : Tile.Kind.NORMAL;

        if (kind.equals(Tile.Kind.MENHIR)) {
            newTileDecks = tileDecks.withTopTileDrawnUntil(kind, newBoard::couldPlaceTile);
            if (newTileDecks.deckSize(Tile.Kind.MENHIR) != 0)
                return new GameState(players, newTileDecks.withTopTileDrawn(Tile.Kind.MENHIR),
                        newTileDecks.topTile(Tile.Kind.MENHIR), newBoard, Action.PLACE_TILE, newMessageBoard);
        }

        List<PlayerColor> newPlayers = new LinkedList<>(players);
        newPlayers.add(newPlayers.removeFirst());
        newTileDecks = tileDecks.withTopTileDrawnUntil(Tile.Kind.NORMAL, newBoard::couldPlaceTile);

        if (newTileDecks.deckSize(Tile.Kind.NORMAL) != 0)
            return new GameState(newPlayers, newTileDecks.withTopTileDrawn(Tile.Kind.NORMAL),
                    newTileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.PLACE_TILE, newMessageBoard);

        return new GameState(newPlayers, newTileDecks, null, newBoard,
                Action.END_GAME, newMessageBoard).withFinalPointsCounted();
    }

    /**
     * Private method that returns a new game state with the final points counted.
     *
     * @return new game state with the final points counted
     */
    private GameState withFinalPointsCounted() {
        Board newBoard = board;
        MessageBoard newMessageBoard = messageBoard;

        // Score the meadows and river systems
        for (Area<Zone.Meadow> meadowArea : newBoard.meadowAreas()) {
            Set<Animal> animals = Area.animals(meadowArea, newBoard.cancelledAnimals());
            List<Animal> deer = animals.stream()
                    .filter(animal -> animal.kind().equals(Animal.Kind.DEER))
                    .collect(Collectors.toList());
            long tigerCount = animals.stream()
                    .filter(animal -> animal.kind().equals(Animal.Kind.TIGER))
                    .count();

            // Sort the deer by distance to the pit trap
            if (meadowArea.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP) != null) {
                Pos pitTrapPos = newBoard.tileWithId(meadowArea.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP).tileId()).pos();
                deer.sort(Comparator.comparingInt(animal ->
                        Math.max(Math.abs(pitTrapPos.x() - board.tileWithId(animal.tileId()).pos().x()),
                                Math.abs(pitTrapPos.y() - board.tileWithId(animal.tileId()).pos().y()))));
            }

            if (meadowArea.zoneWithSpecialPower(Zone.SpecialPower.WILD_FIRE) != null)
                tigerCount = 0L;

            // Cancel the deer that are furthest to the pit trap
            Set<Animal> newlyCancelledAnimals = deer.stream()
                    .limit(tigerCount)
                    .collect(Collectors.toSet());
            newBoard = newBoard.withMoreCancelledAnimals(newlyCancelledAnimals);
            newMessageBoard = newMessageBoard.withScoredMeadow(meadowArea, newBoard.cancelledAnimals());

            if (meadowArea.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP) != null) {
                Pos pitTrapPos = newBoard.tileWithId(meadowArea.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP).tileId()).pos();
                newMessageBoard = newMessageBoard.withScoredPitTrap(
                        newBoard.adjacentMeadow(pitTrapPos,
                                (Zone.Meadow) meadowArea.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP)),
                        newBoard.cancelledAnimals());
            }
        }

        for (Area<Zone.Water> waterArea : newBoard.riverSystemAreas()) {
            if (waterArea.zoneWithSpecialPower(Zone.SpecialPower.RAFT) != null)
                newMessageBoard = newMessageBoard.withScoredRaft(waterArea);
            newMessageBoard = newMessageBoard.withScoredRiverSystem(waterArea);
        }

        // Count the final points and determine the winners
        int maxPoints = Collections.max(newMessageBoard.points().values());
        Set<PlayerColor> winners = newMessageBoard.points().entrySet().stream()
                .filter(entry -> entry.getValue() == maxPoints)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        newMessageBoard = newMessageBoard.withWinners(winners, maxPoints);
        return new GameState(players, tileDecks, null, board, Action.END_GAME, newMessageBoard);
    }
}