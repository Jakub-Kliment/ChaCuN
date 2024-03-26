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
        Board newBoard = board.withNewTile(tile);
        MessageBoard newMessageBoard = new MessageBoard(messageBoard.textMaker(), messageBoard.messages());
        boolean hasShaman = false;
        int points;

        if (tile.specialPowerZone() != null) {
            if (tile.specialPowerZone() instanceof Zone.Meadow meadow
                    && tile.specialPowerZone().specialPower().equals(Zone.SpecialPower.HUNTING_TRAP)) {
                Area<Zone.Meadow> adjacentMeadow = newBoard.adjacentMeadow(tile.pos(), meadow);
                Set<Animal> animals = Area.animals(adjacentMeadow, board.cancelledAnimals());
                Map<Animal.Kind, Integer> animalCount = new HashMap<>();

                for (Animal animal : animals)
                    animalCount.put(animal.kind(), animalCount.getOrDefault(animal.kind(), 0) + 1);

                int deerPoints = animalCount.get(Animal.Kind.TIGER) >= animalCount.get(Animal.Kind.DEER) ?
                        0 : animalCount.get(Animal.Kind.DEER) - animalCount.get(Animal.Kind.TIGER);
                points = Points.forMeadow(animalCount.getOrDefault(Animal.Kind.MAMMOTH, 0),
                        animalCount.getOrDefault(Animal.Kind.AUROCHS, 0), deerPoints);

                newBoard = newBoard.withMoreCancelledAnimals(animals);
                newMessageBoard = messageBoard.withScoredHuntingTrap(currentPlayer(), adjacentMeadow);

            } else if (tile.specialPowerZone() instanceof Zone.Lake lake
                    && tile.specialPowerZone().specialPower().equals(Zone.SpecialPower.LOGBOAT)) {
                points = Area.lakeCount(newBoard.riverSystemArea(lake));
                newMessageBoard = messageBoard.withScoredLogboat(currentPlayer(), newBoard.riverSystemArea(lake));
            }
            hasShaman = tile.specialPowerZone().specialPower().equals(Zone.SpecialPower.SHAMAN);
        }
        Action nextAction = hasShaman ? Action.RETAKE_PAWN : Action.OCCUPY_TILE;

        boolean canContinue = false;
        for (Zone zone : tile.tile().zones()) {
            switch (zone) {
                case Zone.Meadow meadow -> {
                    if (!newBoard.meadowArea(meadow).isOccupied())
                        canContinue = true;
                }
                case Zone.Forest forest -> {
                    if (!newBoard.forestArea(forest).isOccupied())
                        canContinue = true;
                }
                case Zone.Water water -> {
                    if (!newBoard.riverSystemArea(water).isOccupied())
                        canContinue = true;

                    if (water instanceof Zone.River river && !newBoard.riverArea(river).isOccupied())
                        canContinue = true;
                }
            }
        }

        if (!canContinue)
            return withTurnFinishedIfOccupationImpossible();

        return new GameState(players, tileDecks.withTopTileDrawn(tile.kind()), null,
                newBoard, nextAction, newMessageBoard);
    }

    public GameState withOccupantRemoved(Occupant occupant) {
        Preconditions.checkArgument(nextAction.equals(Action.RETAKE_PAWN)
                && (occupant == null || occupant.kind().equals(Occupant.Kind.PAWN)));

        if (occupant == null || freeOccupantsCount(currentPlayer(), occupant.kind()) == Occupant.occupantsCount(occupant.kind()))
            return withTurnFinishedIfOccupationImpossible();

        Board newBoard = board.withoutOccupant(occupant);
        return new GameState(players, tileDecks, null,
                newBoard, Action.OCCUPY_TILE, messageBoard);
    }

    public GameState withNewOccupant(Occupant occupant) {
        Preconditions.checkArgument(nextAction.equals(Action.OCCUPY_TILE));
        if (occupant == null)
            return withTurnFinished(board);
        Board newBoard = board.withOccupant(occupant);
        return withTurnFinished(newBoard);
    }

    private GameState withTurnFinishedIfOccupationImpossible() {
        return null;
    }

    private GameState withTurnFinished(Board modifiedBoard) {
        Board newBoard = modifiedBoard;
        MessageBoard newMessageBoard = messageBoard();
        boolean replay = false;
        for (Zone zone : newBoard.lastPlacedTile().tile().zones()){
            switch (zone){
                case Zone.Meadow meadow-> {
                    if (modifiedBoard.meadowArea(meadow).isClosed()){
                        newBoard = newBoard.withMoreCancelledAnimals(cancelAnimalUpdate(newBoard.meadowArea(meadow), newBoard.cancelledAnimals()));
                        newMessageBoard = newMessageBoard.withScoredMeadow(modifiedBoard.meadowArea(meadow), newBoard.cancelledAnimals());
                    }
                }
                case Zone.Forest forest-> {
                    if (modifiedBoard.forestArea(forest).isClosed()){
                        if (Area.hasMenhir(modifiedBoard.forestArea(forest)))
                            replay = true;
                        newMessageBoard = newMessageBoard.withScoredForest(modifiedBoard.forestArea(forest));
                    }
                }
                case Zone.River river -> {
                    if (modifiedBoard.riverArea(river).isClosed() && !river.hasLake()){
                        newMessageBoard = newMessageBoard.withScoredRiver(modifiedBoard.riverArea(river));
                    }
                }
                case Zone.Water water -> {
                    if (modifiedBoard.riverSystemArea(water).isClosed()){
                        newMessageBoard = newMessageBoard.withScoredRiverSystem(modifiedBoard.riverSystemArea(water));
                    }
                }
            }
        }

        Board finalNewBoard = newBoard;
        if (replay){
            TileDecks newTileDecks = tileDecks.withTopTileDrawnUntil(Tile.Kind.MENHIR, tile -> finalNewBoard.couldPlaceTile(tileDecks.topTile(Tile.Kind.MENHIR)));
            if (newTileDecks.deckSize(Tile.Kind.MENHIR) != 0){
                return new GameState(players, newTileDecks, newTileDecks.topTile(Tile.Kind.MENHIR), newBoard, Action.PLACE_TILE, newMessageBoard);
            }
        }
        TileDecks newTileDecks = tileDecks.withTopTileDrawnUntil(Tile.Kind.NORMAL, tile -> finalNewBoard.couldPlaceTile(tileDecks.topTile(Tile.Kind.NORMAL)));
        players.add(players.removeFirst());
        if (newTileDecks.deckSize(Tile.Kind.NORMAL) != 0){
            return new GameState(players, newTileDecks, newTileDecks.topTile(Tile.Kind.NORMAL), newBoard, Action.PLACE_TILE, newMessageBoard);
        } else {
            return new GameState(players, newTileDecks, null, newBoard, Action.END_GAME, newMessageBoard);
        }
    }
    private GameState withFinalPointsCounted() {
        return null;
    }

    private Set<Animal> cancelAnimalUpdate(Area<Zone.Meadow> area, Set<Animal> cancelledAnimal){
        int deerCount = 0;
        int tigerCount = 0;
        Set<Animal> nextCancel = new HashSet<>();
        for (Animal animal : Area.animals(area, cancelledAnimal)){
            switch (animal.kind()){
                case DEER -> deerCount++;
                case TIGER -> tigerCount++;
            }
        }
        for (int i = 0; i < deerCount-tigerCount; i++){
            for (Animal animal : Area.animals(area, cancelledAnimal)){
                if (animal.kind() == Animal.Kind.DEER){
                    nextCancel.add(animal);
                }
            }
        }
        return nextCancel;
    }
}
