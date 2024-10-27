import game.BufferedMatrix;
import game.CellularAutomata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CellularAutomataTest {

    private BufferedMatrix<Boolean> matrix;
    private CellularAutomata automata;

    @BeforeEach
    void setUp() {
        matrix = new BufferedMatrix<>(5, 5,false);
        automata = new CellularAutomata(matrix);
    }

    @Test
    void testInitialState() {
        for (int i = 0; i < matrix.getSizeX(); i++) {
            for (int j = 0; j < matrix.getSizeY(); j++) {
                assertFalse(matrix.get(i, j), "Cell at (" + i + "," + j + ") should be dead initially.");
            }
        }
    }

    @Test
    void testNextGenerationLiveCellSurvives() {
        matrix.update(1, 1, true);
        matrix.update(1, 2, true);
        matrix.update(2, 1, true);
        matrix.update(2, 2, true);

        automata.next();

        assertTrue(matrix.get(1, 1), "Cell at (1,1) should be alive after next generation.");
        assertTrue(matrix.get(1, 2), "Cell at (1,2) should be alive after next generation.");
        assertTrue(matrix.get(2, 1), "Cell at (2,1) should be alive after next generation.");
        assertTrue(matrix.get(2, 2), "Cell at (2,2) should be alive after next generation.");
    }

    @Test
    void testNextGenerationDeadCellBecomesAlive() {
        matrix.update(0, 1, true);
        matrix.update(1, 0, true);
        matrix.update(1, 1, true);

        automata.next();

        assertTrue(matrix.get(0, 0), "Cell at (0,0) should become alive after next generation.");
    }

    @Test
    void testNextGenerationLiveCellDies() {
        matrix.update(0, 0, true);
        matrix.update(2, 2, true);

        automata.next();

        assertFalse(matrix.get(0, 0), "Cell at (0,0) should die after next generation.");
        assertFalse(matrix.get(2, 2), "Cell at (2,2) should die after next generation.");
    }

    @Test
    void testWrappingAroundEdges() {
        matrix.update(0, 0, true);
        matrix.update(4, 4, true);
        matrix.update(0,4,true);

        automata.next();

        assertTrue(matrix.get(4, 0), "Cell at (4,0) should be alive after wrapping.");
    }

    @Test
    void testLotsOfNeighbors() {
        matrix.update(0,0,true);
        matrix.update(0,1,true);
        matrix.update(0,2,true);
        matrix.update(1,0,true);
        matrix.update(1,1,true);
        matrix.update(1,2,true);
        matrix.update(2,0,true);
        matrix.update(2,1,true);
        matrix.update(2,2,true);

        matrix.next();

        assertFalse(matrix.get(1, 1), "Cell at (1,1) should be dead.");

    }
}
