package ch.epfl.chacun;

import java.util.*;
import java.util.stream.Collectors;

public record GameState(List<PlayerColor> players, TileDecks tileDecks, Tile tileToPlace,
                        Board board, Action nextAction, MessageBoard messageBoard) {

    public GameState {
        Preconditions.checkArgument(players.size() >= 2);
        players = List.copyOf(players);
        Preconditions.checkArgument(tileToPlace == null ^ nextAction.equals(Action.PLACE_TILE));
        Objects.requireNonNull(tileDecks);
        Objects.requireNonNull(board);
        Objects.requireNonNull(nextAction);
        Objects.requireNonNull(messageBoard);
    }

    public enum Action {
        START_GAME,
        PLACE_TILE,
        RETAKE_PAWN,
        OCCUPY_TILE,
        END_GAME
    }

    public static GameState initial(List<PlayerColor> players, TileDecks tileDecks, TextMaker textMaker) {
        return new GameState(players, tileDecks, null, Board.EMPTY,
                Action.START_GAME, new MessageBoard(textMaker, new ArrayList<>()));
    }

    public PlayerColor currentPlayer() {
        if (nextAction.equals(Action.START_GAME) || nextAction.equals(Action.END_GAME))
            return null;
        return players.getFirst();
    }

    public int freeOccupantsCount(PlayerColor player, Occupant.Kind kind) {
        return Occupant.occupantsCount(kind) - board.occupantCount(player, kind);
    }

    public Set<Occupant> lastTilePotentialOccupants() {
        Preconditions.checkArgument(!board.equals(Board.EMPTY));
       Set<Occupant> potentialOccupants = board.lastPlacedTile().potentialOccupants();
       potentialOccupants.removeIf(
               o -> freeOccupantsCount(currentPlayer(), o.kind()) == 0);

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

    public GameState withStartingTilePlaced() {
        Preconditions.checkArgument(nextAction.equals(Action.START_GAME));
        Board newBoard = board.withNewTile(new PlacedTile(tileDecks.topTile(Tile.Kind.START),
                null, Rotation.NONE, Pos.ORIGIN));
        Tile nextTile = tileDecks.topTile(Tile.Kind.NORMAL);
        TileDecks newTileDecks = tileDecks.withTopTileDrawn(Tile.Kind.START).withTopTileDrawn(Tile.Kind.NORMAL);
        return new GameState(players, newTileDecks, nextTile, newBoard, Action.PLACE_TILE, messageBoard);
    }

    public GameState withPlacedTile(PlacedTile tile) {
        Preconditions.checkArgument(nextAction.equals(Action.PLACE_TILE) && tile.occupant() == null);
        MessageBoard newMessageBoard = messageBoard;
        Board newBoard = board.withNewTile(tile);
        Action newAction;

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
        if (newAction.equals(Action.OCCUPY_TILE))
            return newGameState.withTurnFinishedIfOccupationImpossible();
        return newGameState;
    }

    public GameState withOccupantRemoved(Occupant occupant) {
        Preconditions.checkArgument(nextAction.equals(Action.RETAKE_PAWN)
                && (occupant == null || occupant.kind().equals(Occupant.Kind.PAWN)));

        if (occupant == null || board.occupantCount(currentPlayer(), occupant.kind()) == 0)
            return withTurnFinishedIfOccupationImpossible();

        Board newBoard = board.withoutOccupant(occupant);
        return new GameState(players, tileDecks, null, newBoard, Action.OCCUPY_TILE, messageBoard);
    }

    public GameState withNewOccupant(Occupant occupant) {
        Preconditions.checkArgument(nextAction.equals(Action.OCCUPY_TILE));
        if (occupant == null)
            return withTurnFinished();
        Board newBoard = board.withOccupant(occupant);
        return new GameState(players, tileDecks, null, newBoard, nextAction, messageBoard).withTurnFinished();
    }

    private GameState withTurnFinishedIfOccupationImpossible() {
        if (lastTilePotentialOccupants().isEmpty())
            return withTurnFinished();
        else
            return new GameState(players, tileDecks, null, board, Action.OCCUPY_TILE, messageBoard);
    }

    private GameState withTurnFinished() {
        MessageBoard newMessageBoard = messageBoard;
        Board newBoard = board;

        for (Area<Zone.Forest> forestArea : board.forestsClosedByLastTile()) {
            if (Area.hasMenhir(forestArea))
                newMessageBoard = newMessageBoard.withClosedForestWithMenhir(currentPlayer(), forestArea);
            newMessageBoard = newMessageBoard.withScoredForest(forestArea);
            newBoard = newBoard.withoutGatherersOrFishersIn(Set.of(forestArea), new HashSet<>());
        }

        for (Area<Zone.River> riverArea : board.riversClosedByLastTile()) {
            newMessageBoard = newMessageBoard.withScoredRiver(riverArea);
            newBoard = newBoard.withoutGatherersOrFishersIn(new HashSet<>(), Set.of(riverArea));
        }

        TileDecks newTileDecks;
        Tile.Kind kind = (newBoard.forestsClosedByLastTile().stream().anyMatch(Area::hasMenhir) &&
                newBoard.lastPlacedTile().kind().equals(Tile.Kind.NORMAL)) ? Tile.Kind.MENHIR : Tile.Kind.NORMAL;
        Board finalNewBoard = newBoard;

        if (kind.equals(Tile.Kind.MENHIR)) {
            newTileDecks = tileDecks.withTopTileDrawnUntil(kind,
                    (tile) -> finalNewBoard.couldPlaceTile(tileDecks.topTile(kind)));
            if (newTileDecks.deckSize(Tile.Kind.MENHIR) != 0)
                return new GameState(players, newTileDecks.withTopTileDrawn(Tile.Kind.MENHIR), newTileDecks.topTile(Tile.Kind.MENHIR),
                        newBoard, Action.PLACE_TILE, newMessageBoard);
        }

        List<PlayerColor> newPlayers = new ArrayList<>(players);
        newPlayers.add(newPlayers.removeFirst());
        newTileDecks = tileDecks.withTopTileDrawnUntil(Tile.Kind.NORMAL,
                (tile) -> finalNewBoard.couldPlaceTile(tileDecks.topTile(Tile.Kind.NORMAL)));

        if (newTileDecks.deckSize(Tile.Kind.NORMAL) != 0)
            return new GameState(newPlayers, newTileDecks.withTopTileDrawn(Tile.Kind.NORMAL), newTileDecks.topTile(Tile.Kind.NORMAL),
                    newBoard, Action.PLACE_TILE, newMessageBoard);

        return new GameState(newPlayers, newTileDecks, null, newBoard,
                Action.END_GAME, newMessageBoard).withFinalPointsCounted();
    }
    private GameState withFinalPointsCounted() {
        Board newBoard = board;
        MessageBoard newMessageBoard = messageBoard;

        for (Area<Zone.Meadow> meadowArea : newBoard.meadowAreas()) {
            Set<Animal> animals = Area.animals(meadowArea, newBoard.cancelledAnimals());
            List<Animal> deer = animals.stream()
                    .filter(animal -> animal.kind().equals(Animal.Kind.DEER))
                    .collect(Collectors.toList());
            long tigerCount = animals.stream()
                    .filter(animal -> animal.kind().equals(Animal.Kind.TIGER))
                    .count();

            if (meadowArea.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP) != null) {
                Pos pitTrapPos = newBoard.tileWithId(meadowArea.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP).tileId()).pos();
                deer.sort(Comparator.comparingInt(animal ->
                        Math.max(Math.abs(pitTrapPos.x() - board.tileWithId(animal.tileId()).pos().x()),
                                Math.abs(pitTrapPos.y() - board.tileWithId(animal.tileId()).pos().y()))));
                newMessageBoard = newMessageBoard.withScoredPitTrap(
                        newBoard.adjacentMeadow(pitTrapPos,
                                (Zone.Meadow) meadowArea.zoneWithSpecialPower(Zone.SpecialPower.PIT_TRAP)),
                        newBoard.cancelledAnimals());
            }

            if (meadowArea.zoneWithSpecialPower(Zone.SpecialPower.WILD_FIRE) != null)
                tigerCount = 0L;

            Set<Animal> newlyCancelledAnimals = deer.stream()
                    .limit(tigerCount)
                    .collect(Collectors.toSet());
            newBoard = newBoard.withMoreCancelledAnimals(newlyCancelledAnimals);
            newMessageBoard = newMessageBoard.withScoredMeadow(meadowArea, newBoard.cancelledAnimals());
        }

        for (Area<Zone.Water> waterArea : newBoard.riverSystemAreas()) {
            if (waterArea.zoneWithSpecialPower(Zone.SpecialPower.RAFT) != null)
                newMessageBoard = newMessageBoard.withScoredRaft(waterArea);
            newMessageBoard = newMessageBoard.withScoredRiverSystem(waterArea);
        }

        int maxPoints = Collections.max(newMessageBoard.points().values());
        Set<PlayerColor> winners = newMessageBoard.points().entrySet().stream()
                .filter(entry -> entry.getValue() == maxPoints)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        newMessageBoard = newMessageBoard.withWinners(winners, maxPoints);
        return new GameState(players, tileDecks, null, board, Action.END_GAME, newMessageBoard);
    }
}