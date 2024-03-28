package ch.epfl.chacun;

import java.util.*;

public record GameState(List<PlayerColor> players, TileDecks tileDecks, Tile tileToPlace,
                        Board board, Action nextAction, MessageBoard messageBoard) {

    public GameState {
        //Peut-être être truc à faire pour être immuable !!!!!
        Preconditions.checkArgument(players.size() >= 2);
        players = List.copyOf(players);
        Preconditions.checkArgument((tileToPlace == null) ^ nextAction.equals(Action.PLACE_TILE));
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
        Preconditions.checkArgument(board.equals(Board.EMPTY));
        return board.lastPlacedTile().potentialOccupants();
    }

    public GameState withStartingTilePlaced() {
        Preconditions.checkArgument(nextAction.equals(Action.START_GAME));
        Board newBoard = board.withNewTile(new PlacedTile(tileDecks.topTile(Tile.Kind.START),
                null, Rotation.NONE, Pos.ORIGIN));
        Tile nextTile = tileDecks.topTile(Tile.Kind.NORMAL);
        TileDecks nextDecks = tileDecks.withTopTileDrawn(Tile.Kind.START).withTopTileDrawn(Tile.Kind.NORMAL);
        return new GameState(players, nextDecks, nextTile, newBoard, Action.PLACE_TILE, messageBoard);
    }

    public GameState withPlacedTile(PlacedTile tile) {
        Preconditions.checkArgument(nextAction.equals(Action.PLACE_TILE) && tile.occupant() != null);
        MessageBoard newMessageBoard = new MessageBoard(messageBoard.textMaker(), messageBoard.messages());
        Board newBoard = board.withNewTile(tile);
        Action newAction;

        switch (tile.specialPowerZone()) {
            case Zone.Meadow meadow
                    when meadow.specialPower().equals(Zone.SpecialPower.HUNTING_TRAP) -> {
                Area<Zone.Meadow> adjacentMeadow = newBoard.adjacentMeadow(tile.pos(), meadow);
                Set<Animal> animals = Area.animals(adjacentMeadow, board.cancelledAnimals());
                newBoard = newBoard.withMoreCancelledAnimals(animals);
                newMessageBoard = messageBoard.withScoredHuntingTrap(currentPlayer(), adjacentMeadow);
                newAction = Action.OCCUPY_TILE;
            }
            case Zone.Lake lake
                    when lake.specialPower().equals(Zone.SpecialPower.LOGBOAT) -> {
                newMessageBoard = messageBoard.withScoredLogboat(currentPlayer(), newBoard.riverSystemArea(lake));
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
        if (board.lastPlacedTile().potentialOccupants().isEmpty())
            return withTurnFinished();
        else
            return new GameState(players, tileDecks, null, board, Action.OCCUPY_TILE, messageBoard);
    }

    private GameState withTurnFinished() {
        MessageBoard newMessageBoard = messageBoard;
        Board newBoard = board;
        boolean hasMenhir = false;

        for (Area<Zone.Forest> forestArea : board.forestsClosedByLastTile()) {
            if (Area.hasMenhir(forestArea)) {
                newMessageBoard = newMessageBoard.withClosedForestWithMenhir(currentPlayer(), forestArea);
                hasMenhir = true;
            }
            else
                newMessageBoard = newMessageBoard.withScoredForest(forestArea);
            newBoard = newBoard.withoutGatherersOrFishersIn(Set.of(forestArea), new HashSet<>());
        }
        for (Area<Zone.River> riverArea : board.riversClosedByLastTile()) {
            newMessageBoard = newMessageBoard.withScoredRiver(riverArea);
            newBoard = newBoard.withoutGatherersOrFishersIn(new HashSet<>(), Set.of(riverArea));
        }

        TileDecks newTileDecks = tileDecks;
        Tile.Kind kind = (hasMenhir && newBoard.lastPlacedTile().kind().equals(Tile.Kind.NORMAL)) ? Tile.Kind.MENHIR : Tile.Kind.NORMAL;
        Board finalNewBoard = newBoard;

        if (kind.equals(Tile.Kind.MENHIR) && newTileDecks.deckSize(Tile.Kind.MENHIR) != 0) {
            newTileDecks = tileDecks.withTopTileDrawnUntil(kind, (tile) -> finalNewBoard.couldPlaceTile(tileDecks.topTile(kind)));
            return new GameState(players, newTileDecks, newTileDecks.topTile(kind), newBoard, Action.PLACE_TILE, newMessageBoard);
        }

        players.add(players.removeFirst());
        newTileDecks = tileDecks.withTopTileDrawnUntil(Tile.Kind.NORMAL, (tile) -> finalNewBoard.couldPlaceTile(tileDecks.topTile(Tile.Kind.NORMAL)));
        if (newTileDecks.deckSize(Tile.Kind.NORMAL) != 0)
            return new GameState(players, newTileDecks, newTileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.PLACE_TILE, newMessageBoard);

        return withFinalPointsCounted();
    }
    private GameState withFinalPointsCounted() {
        for (Area<Zone.Meadow> meadowArea : board.meadowAreas()) {
            Map<Animal.Kind, Integer> animalCount = new HashMap<>();
            List<Zone.SpecialPower> specialPowers = new ArrayList<>();
            for (Zone.Meadow meadow : meadowArea.zones()) {
                if (meadow.specialPower() != null)
                    specialPowers.add(meadow.specialPower());
                for (Animal animal : meadow.animals())
                    animalCount.put(animal.kind(), animalCount.getOrDefault(animal.kind(), 0) + 1);
            }
            for (Zone.SpecialPower specialPower : specialPowers) {
                if (specialPower.equals(Zone.SpecialPower.WILD_FIRE))
                    animalCount.put(Animal.Kind.TIGER, 0);
                else if (specialPower.equals(Zone.SpecialPower.HUNTING_TRAP)) {

                }
            }
        }

        for (Area<Zone.Water> waterArea : board.riverSystemAreas()) {
        }


        return new GameState(players, tileDecks, null, board, Action.END_GAME, messageBoard);
    }

    public static Set<Animal> cancelAnimalUpdate(Area<Zone.Meadow> area, Set<Animal> cancelledAnimal){
        int tigerCount = 0;
        Set<Animal> nextCancel = new HashSet<>();
        for (Animal animal : Area.animals(area, cancelledAnimal)){
            if (animal.kind() == Animal.Kind.TIGER){
                tigerCount++;
            }
        }
        while(tigerCount>0) {
            for (Animal animal : Area.animals(area, cancelledAnimal)){
                if (animal.kind() == Animal.Kind.DEER && !nextCancel.contains(animal)){
                    nextCancel.add(animal);
                    break;
                }
            }
            tigerCount--;
        }
        return nextCancel;
    }
}
