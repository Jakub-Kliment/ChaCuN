package ch.epfl.chacun;

import javafx.scene.media.EqualizerBand;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ActionEncoderTest {
    Map<PlayerColor, String> playerNames = Map.of(PlayerColor.BLUE, "Mario",
            PlayerColor.YELLOW, "Warrio",
            PlayerColor.GREEN, "Luigi");
    List<PlayerColor> playerColors = playerNames.keySet().stream()
            .sorted()
            .toList();

    Map<Tile.Kind, List<Tile>> tilesByKind = Tiles.TILES.stream()
            .collect(Collectors.groupingBy(Tile::kind));
    TileDecks tileDecks =
            new TileDecks(tilesByKind.get(Tile.Kind.START),
                    tilesByKind.get(Tile.Kind.NORMAL),
                    tilesByKind.get(Tile.Kind.MENHIR));

    TextMaker textMaker = new TextMakerFr(playerNames);

    GameState gameState =
            GameState.initial(playerColors,
                    tileDecks,
                    textMaker);

    @Test
    void withPlacedTileWorksCorrectlyForNormalCase() {
        GameState gs = gameState.withStartingTilePlaced();
        PlacedTile placedTile =  new PlacedTile(
                tileDecks.topTile(Tile.Kind.NORMAL),
                gs.currentPlayer(),
                Rotation.RIGHT,
                new Pos(-1, 0));

        ActionEncoder.StateAction stateAction = ActionEncoder.decodeAndApply(gs, "AB");
        GameState expected = stateAction.state();
        GameState actual = gs.withPlacedTile(placedTile);
        boolean isEqual = expected.equals(actual);
        assertTrue(isEqual);
    }

    @Test
    void withOccupantWorksCorrectly() {
        GameState gs = gameState.withStartingTilePlaced();
        PlacedTile placedTile =  new PlacedTile(
                tileDecks.topTile(Tile.Kind.NORMAL),
                gs.currentPlayer(),
                Rotation.RIGHT,
                new Pos(0, -1));

        gs = gs.withPlacedTile(placedTile);
        GameState expected = ActionEncoder.decodeAndApply(gs, "B").state();
        GameState actual = gs.withNewOccupant(new Occupant(Occupant.Kind.PAWN, 1));
        assertEquals(expected, actual);
    }

    @Test
    void withOccupantRemovedWorksCorrectly() {
        TileDecks tileDecks1 = new TileDecks(
                List.of(Tiles.TILES.get(56)),
                List.of(Tiles.TILES.get(0), Tiles.TILES.get(1), Tiles.TILES.get(2), Tiles.TILES.get(3)),
                List.of(Tiles.TILES.get(88)));

        GameState gs = GameState.initial(playerColors, tileDecks1, textMaker);
        gs = gs.withStartingTilePlaced();

        PlacedTile placedTile =  new PlacedTile(
                Tiles.TILES.getFirst(),
                gs.currentPlayer(),
                Rotation.RIGHT,
                new Pos(0, -1));

        gs = gs.withPlacedTile(placedTile);

        Occupant o = new Occupant(Occupant.Kind.PAWN, 1);
        gs = gs.withNewOccupant(o);

        PlacedTile placedTile2 =  new PlacedTile(
                Tiles.TILES.get(1),
                gs.currentPlayer(),
                Rotation.NONE,
                new Pos(-1, 0));

        gs = gs.withPlacedTile(placedTile2);
        gs = gs.withNewOccupant(null);

        PlacedTile placedTile3 =  new PlacedTile(
                Tiles.TILES.get(2),
                gs.currentPlayer(),
                Rotation.NONE,
                new Pos(1, 0));

        gs = gs.withPlacedTile(placedTile3);
        gs = gs.withNewOccupant(null);

        PlacedTile placedTileShaman =  new PlacedTile(
                Tiles.TILES.get(88),
                gs.currentPlayer(),
                Rotation.RIGHT,
                new Pos(-2, 0));

        gs = gs.withPlacedTile(placedTileShaman);

        GameState expected = ActionEncoder.decodeAndApply(gs, "A").state();

        GameState actual = gs.withOccupantRemoved(o);

        assertEquals(expected, actual);
    }

    @Test
    void withOccupantRemovedWorksCorrectlyForNotTakingPawn() {
        TileDecks tileDecks1 = new TileDecks(
                List.of(Tiles.TILES.get(56)),
                List.of(Tiles.TILES.get(0), Tiles.TILES.get(1), Tiles.TILES.get(2), Tiles.TILES.get(3)),
                List.of(Tiles.TILES.get(88)));

        GameState gs = GameState.initial(playerColors, tileDecks1, textMaker);
        gs = gs.withStartingTilePlaced();

        PlacedTile placedTile =  new PlacedTile(
                Tiles.TILES.getFirst(),
                gs.currentPlayer(),
                Rotation.RIGHT,
                new Pos(0, -1));

        gs = gs.withPlacedTile(placedTile);

        Occupant o = new Occupant(Occupant.Kind.PAWN, 1);
        gs = gs.withNewOccupant(o);

        PlacedTile placedTile2 =  new PlacedTile(
                Tiles.TILES.get(1),
                gs.currentPlayer(),
                Rotation.NONE,
                new Pos(-1, 0));

        gs = gs.withPlacedTile(placedTile2);
        gs = gs.withNewOccupant(null);

        PlacedTile placedTile3 =  new PlacedTile(
                Tiles.TILES.get(2),
                gs.currentPlayer(),
                Rotation.NONE,
                new Pos(1, 0));

        gs = gs.withPlacedTile(placedTile3);
        gs = gs.withNewOccupant(null);

        PlacedTile placedTileShaman =  new PlacedTile(
                Tiles.TILES.get(88),
                gs.currentPlayer(),
                Rotation.RIGHT,
                new Pos(-2, 0));

        gs = gs.withPlacedTile(placedTileShaman);

        GameState expected = ActionEncoder.decodeAndApply(gs, "7").state();

        GameState actual = gs.withOccupantRemoved(null);

        assertEquals(expected, actual);
    }

    @Test
    void withOccupantWorksCorrectlyForNotPlacingAPawn() {
        GameState gs = gameState.withStartingTilePlaced();
        PlacedTile placedTile =  new PlacedTile(
                tileDecks.topTile(Tile.Kind.NORMAL),
                gs.currentPlayer(),
                Rotation.RIGHT,
                new Pos(0, -1));

        gs = gs.withPlacedTile(placedTile);
        GameState expected = ActionEncoder.decodeAndApply(gs, "7").state();

        GameState actual = gs.withNewOccupant(null);
        assertEquals(expected, actual);
    }
}