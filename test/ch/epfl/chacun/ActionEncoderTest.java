package ch.epfl.chacun;

import javafx.scene.media.EqualizerBand;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ActionEncoderTest {
    Map<PlayerColor, String> playerNames = Map.of(PlayerColor.BLUE, "Mario",
            PlayerColor.YELLOW, "Warrio",
            PlayerColor.GREEN, "Luigi",
            PlayerColor.PURPLE, "Waluigi");
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
}