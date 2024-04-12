package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the state of the game at a given moment.
 * It is an immutable record that keeps track of the players,
 * the decks of tiles, the tile to place, the board, the next action
 * to perform and the message board.
 *
 * @author Alexis Grillet-Aubert (381587)
 * @author Jakub Kliment (380660)
 *
 * @param players the players of the game
 * @param tileDecks the decks of tiles
 * @param tileToPlace the tile to place
 * @param board the board of the game
 * @param nextAction the next action to perform
 * @param messageBoard the message board
 */
public record GameState(List<PlayerColor> players,
                        TileDecks tileDecks,
                        Tile tileToPlace,
                        Board board,
                        Action nextAction,
                        MessageBoard messageBoard) {

    /**
     * Compact constructor of game state that copies the list of players to keep the class immutable.
     * It also verifies that the number of players is at least 2, because the game cannot be played
     * with less than 2 players. It also verifies that the tile to place is not null if the next action is
     * PLACE_TILE and vice versa to keep the logic of the game consistent. Also verifies that the tile decks,
     * board, next action and message board are not null.
     *
     * @throws IllegalArgumentException if the number of players is smaller than 2
     * @throws IllegalArgumentException if the tile to place is null and the next action is PLACE_TILE or
     *                                  if the tile to place is not null and the next action is not PLACE_TILE
     * @throws NullPointerException if the tile decks, board, next action or board is null
     */
    public GameState {
        Preconditions.checkArgument(players.size() >= 2);
        Preconditions.checkArgument(tileToPlace == null ^ nextAction.equals(Action.PLACE_TILE));
        players = List.copyOf(players);
        Objects.requireNonNull(tileDecks);
        Objects.requireNonNull(board);
        Objects.requireNonNull(nextAction);
        Objects.requireNonNull(messageBoard);
    }

    /**
     * Represents all possible actions that can be performed in the game.
     */
    public enum Action {
        START_GAME,
        PLACE_TILE,
        RETAKE_PAWN,
        OCCUPY_TILE,
        END_GAME
    }

    /**
     * Creates the initial game state where the game has not started yet
     * by adding an empty board, adding the action to start the game and
     * creating an empty message board. All other parameters are passed
     * as arguments.
     *
     * @param players    the players of the game
     * @param tileDecks  the decks of tiles
     * @param textMaker  the text maker
     * @return the initial game state
     */
    public static GameState initial(List<PlayerColor> players,
                                    TileDecks tileDecks,
                                    TextMaker textMaker) {
        return new GameState(
                players,
                tileDecks,
                null,
                Board.EMPTY,
                Action.START_GAME,
                new MessageBoard(textMaker, new ArrayList<>()));
    }

    /**
     * Returns the current player of the game that is on turn.
     * If the game is starting or has ended, it returns null,
     * since there is no player on turn.
     *
     * @return the current player or null if
     *         the game is starting or has ended
     */
    public PlayerColor currentPlayer() {
        if (nextAction == Action.START_GAME
                || nextAction == Action.END_GAME)
            return null;
        return players.getFirst();
    }

    /**
     * Returns the number of free occupants of a given kind for a given player.
     * It is the number of occupants of the given kind that the player has
     * in hand and can still place on the board.
     *
     * @param player the player
     * @param kind   the kind of occupant
     * @return the number of free occupants of the given kind for the given player
     */
    public int freeOccupantsCount(PlayerColor player, Occupant.Kind kind) {
        return Occupant.occupantsCount(kind) - board.occupantCount(player, kind);
    }

    /**
     * Returns the potential occupants for the last placed tile.
     * Makes sure that the player has free occupants of the given kind in hand
     * and that the area of a potential occupant is not already occupied.
     *
     * @return the potential occupants for the last placed tile
     */
    public Set<Occupant> lastTilePotentialOccupants() {
        Preconditions.checkArgument(board.lastPlacedTile() != null);
        Set<Occupant> potentialOccupants = board.lastPlacedTile().potentialOccupants();

        // Remove occupants if there are no free ones left of a given kind
        potentialOccupants.removeIf(
                o -> freeOccupantsCount(currentPlayer(), o.kind()) == 0);

        // Remove occupants if the area is already occupied
        potentialOccupants.removeIf(
                o -> switch (board.lastPlacedTile().zoneWithId(o.zoneId())) {
                        case Zone.Meadow meadow
                                when o.kind() == Occupant.Kind.PAWN ->
                                    board.meadowArea(meadow).isOccupied();
                        case Zone.Forest forest
                                when o.kind() == Occupant.Kind.PAWN ->
                                    board.forestArea(forest).isOccupied();
                        case Zone.River river
                                when o.kind() == Occupant.Kind.PAWN ->
                                    board.riverArea(river).isOccupied();
                        case Zone.Water water
                                when o.kind() == Occupant.Kind.HUT ->
                                    board.riverSystemArea(water).isOccupied();
                        default -> false;
                });
        return potentialOccupants;
    }

    /**
     * Returns a new game state with the starting tile placed.
     * Creates a new board with the starting tile placed and the next tile drawn
     * for the player to place. The next action is set to PLACE_TILE.
     *
     * @return new game state with the starting tile placed
     * @throws IllegalArgumentException if the next action is not START_GAME
     */
    public GameState withStartingTilePlaced() {
        Preconditions.checkArgument(nextAction == Action.START_GAME);
        Board startingBoard = board.withNewTile(
                new PlacedTile(
                        tileDecks.topTile(Tile.Kind.START),
                        null,
                        Rotation.NONE,
                        Pos.ORIGIN));
        Tile tileToPlace = tileDecks.topTile(Tile.Kind.NORMAL);
        TileDecks newTileDecks = tileDecks
                .withTopTileDrawn(Tile.Kind.START)
                .withTopTileDrawn(Tile.Kind.NORMAL);
        return new GameState(players, newTileDecks, tileToPlace,
                startingBoard, Action.PLACE_TILE, messageBoard);
    }

    /**
     * Returns a new game state with the given tile placed.
     * Adds the tile to the board and checks if the tile contains a special power.
     * In the case of a special power, it acts accordingly and sets the next action.
     *
     * @param tile the tile to place
     * @return new game state with the given tile placed
     * @throws IllegalArgumentException if the next action is not PLACE_TILE
     *                                  or if the tile is already occupied
     */
    public GameState withPlacedTile(PlacedTile tile) {
        Preconditions.checkArgument(nextAction == Action.PLACE_TILE && tile.occupant() == null);
        MessageBoard newMessageBoard = messageBoard;
        Board newBoard = board.withNewTile(tile);

        // Check if the tile contains a special power
        switch (tile.specialPowerZone()) {
            case Zone.Meadow meadow
                    when meadow.specialPower() == Zone.SpecialPower.HUNTING_TRAP -> {
                Area<Zone.Meadow> adjacentMeadow = newBoard.adjacentMeadow(tile.pos(), meadow);

                newBoard = newBoard.withMoreCancelledAnimals(
                        Area.animals(adjacentMeadow, newBoard.cancelledAnimals()));

                newMessageBoard = newMessageBoard.withScoredHuntingTrap(
                        currentPlayer(), adjacentMeadow);
            }
            case Zone.Lake lake
                    when lake.specialPower() == Zone.SpecialPower.LOGBOAT ->
                newMessageBoard = newMessageBoard
                        .withScoredLogboat(currentPlayer(), newBoard.riverSystemArea(lake));

            case Zone.Meadow meadow
                    when meadow.specialPower() == Zone.SpecialPower.SHAMAN -> {
                if (newBoard.occupantCount(currentPlayer(), Occupant.Kind.PAWN) != 0)
                    return new GameState(players, tileDecks, null,
                            newBoard, Action.RETAKE_PAWN, newMessageBoard);
            }
            case null, default -> {}
        }
        return new GameState(players, tileDecks, null,
                newBoard, Action.OCCUPY_TILE, newMessageBoard)
                .withTurnFinishedIfOccupationImpossible();
    }

    /**
     * Returns a new game state with the given occupant removed, if
     * the given player has a pawn on the board and wants to remove one
     * and proceeds to the next action.
     *
     * @param occupant the occupant to remove (can be null)
     * @return new game state with the given occupant removed
     */
    public GameState withOccupantRemoved(Occupant occupant) {
        Preconditions.checkArgument(nextAction == Action.RETAKE_PAWN
                && (occupant == null || occupant.kind() == Occupant.Kind.PAWN));

        if (occupant == null) return withTurnFinishedIfOccupationImpossible();

        Board newBoard = board.withoutOccupant(occupant);
        return new GameState(players, tileDecks, null,
                newBoard, Action.OCCUPY_TILE, messageBoard)
                .withTurnFinishedIfOccupationImpossible();
    }

    /**
     * Returns a new game state with the given occupant placed if the player
     * desires to place an occupant on the board and proceeds to the next action.
     *
     * @param occupant the occupant to place
     * @return new game state with the given occupant placed
     */
    public GameState withNewOccupant(Occupant occupant) {
        Preconditions.checkArgument(nextAction == Action.OCCUPY_TILE);
        if (occupant == null) return withTurnFinished();
        Board newBoard = board.withOccupant(occupant);
        return new GameState(players, tileDecks, null,
                newBoard, nextAction, messageBoard)
                .withTurnFinished();
    }

    /**
     * Private methode that returns a new game state with the turn finished
     * if the occupation is impossible either because the player has no free
     * occupants of the given kind or because the area is already occupied.
     * Otherwise, proceeds to next action, that is OCCUPY_TILE.
     *
     * @return new game state with the turn finished
     */
    private GameState withTurnFinishedIfOccupationImpossible() {
        if (lastTilePotentialOccupants().isEmpty())
            return withTurnFinished();
        else return new GameState(players, tileDecks, null,
                    board, Action.OCCUPY_TILE, messageBoard);
    }

    /**
     * Private method that finishes a turn of a player.
     * It scores the forests and rivers closed by the last tile,
     * and removes the gatherers and fishers from them.
     *
     *
     * @return new game state with the turn finished
     */
    private GameState withTurnFinished() {
        MessageBoard newMessageBoard = messageBoard;
        Board newBoard = board;

        // Score the forests and rivers closed by the last tile
        for (Area<Zone.Forest> forestArea : newBoard.forestsClosedByLastTile()) {
            if (Area.hasMenhir(forestArea))
                newMessageBoard = newMessageBoard
                        .withClosedForestWithMenhir(currentPlayer(), forestArea);
            newMessageBoard = newMessageBoard.withScoredForest(forestArea);
        }

        for (Area<Zone.River> riverArea : newBoard.riversClosedByLastTile())
            newMessageBoard = newMessageBoard.withScoredRiver(riverArea);

        newBoard = newBoard.withoutGatherersOrFishersIn(
                newBoard.forestsClosedByLastTile(),
                newBoard.riversClosedByLastTile());

        TileDecks newTileDecks = tileDecks;
        // Check if the next tile is a menhir tile or a normal tile
        Tile.Kind kind = (newBoard.forestsClosedByLastTile()
                .stream()
                .anyMatch(Area::hasMenhir) &&
                newBoard.lastPlacedTile().kind() == Tile.Kind.NORMAL)
                ? Tile.Kind.MENHIR : Tile.Kind.NORMAL;

        if (kind == Tile.Kind.MENHIR) {
            newTileDecks = newTileDecks.withTopTileDrawnUntil(kind, newBoard::couldPlaceTile);
            if (newTileDecks.deckSize(Tile.Kind.MENHIR) != 0)
                return new GameState(players, newTileDecks.withTopTileDrawn(Tile.Kind.MENHIR),
                        newTileDecks.topTile(Tile.Kind.MENHIR), newBoard, Action.PLACE_TILE, newMessageBoard);
        }

        List<PlayerColor> newPlayers = new LinkedList<>(players);
        newPlayers.add(newPlayers.removeFirst());
        newTileDecks = newTileDecks.withTopTileDrawnUntil(Tile.Kind.NORMAL, newBoard::couldPlaceTile);

        if (newTileDecks.deckSize(Tile.Kind.NORMAL) == 0)
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
                    .filter(animal -> animal.kind() == (Animal.Kind.DEER))
                    .collect(Collectors.toList());

            // Count the deer in the area or set the count to 0 if there is a wildfire
            long tigerCount = meadowArea.zoneWithSpecialPower(Zone.SpecialPower.WILD_FIRE) != null
                    ? 0L : animals.stream()
                    .filter(animal -> animal.kind() == (Animal.Kind.TIGER))
                    .count();

            Zone pitTrapZone = meadowArea.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP);
            if (pitTrapZone != null) {
                Pos pitTrapPos = newBoard.tileWithId(pitTrapZone.tileId()).pos();

                // Sort the deer by distance to the pit trap using Chebyshev distance in reverse order
                deer.sort(Comparator.comparingInt(d ->
                        -Math.max(Math.abs(pitTrapPos.x() - board.tileWithId(d.tileId()).pos().x()),
                                Math.abs(pitTrapPos.y() - board.tileWithId(d.tileId()).pos().y()))));
            }
            newBoard = newBoard.withMoreCancelledAnimals(deer.stream()
                    .limit(tigerCount)
                    .collect(Collectors.toSet()));

            if (pitTrapZone != null)
                newMessageBoard = newMessageBoard.withScoredPitTrap(
                        newBoard.adjacentMeadow(newBoard.tileWithId(
                                pitTrapZone.tileId()).pos(),
                                (Zone.Meadow) pitTrapZone),
                        newBoard.cancelledAnimals());
            newMessageBoard = newMessageBoard.withScoredMeadow(meadowArea, newBoard.cancelledAnimals());
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