package ch.epfl.chacun;

import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MyBoardTest {

    Board emptyBoard = Board.EMPTY;

    @Test
    void boardEmptyBoardWorks() {
        assertEquals(emptyBoard, Board.EMPTY);
    }


}
