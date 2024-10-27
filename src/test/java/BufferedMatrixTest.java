import game.BufferedMatrix;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class BufferedMatrixTest {

    private BufferedMatrix<Boolean> matrix;
    private final String testFilePath = "testMatrix.json";

    @BeforeEach
    void setUp() {
        matrix = new BufferedMatrix<>(3, 3, false);
    }

    @AfterEach
    void tearDown() {
        File file = new File(testFilePath);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testInitialValues() {
        for (int i = 0; i < matrix.getSizeX(); i++) {
            for (int j = 0; j < matrix.getSizeY(); j++) {
                assertFalse(matrix.get(i, j), "Initial value should be false.");
            }
        }
    }

    @Test
    void testUpdateValue() {
        matrix.update(1, 1, true);
        assertTrue(matrix.get(1, 1), "Value at (1, 1) should be true after update.");
    }

    @Test
    void testSetValue() {
        matrix.set(1, 1, true);
        assertFalse(matrix.get(1, 1), "Value at (1, 1) should still be false in current matrix.");
        matrix.next();
        assertTrue(matrix.get(1, 1), "Value at (1, 1) should be true after next call.");
    }

    @Test
    void testClear() {
        matrix.update(0, 0, true);
        matrix.clear();
        assertFalse(matrix.get(0, 0), "Value at (0, 0) should be false after clear.");
    }

    @Test
    void testChangeSize() {
        matrix.changeSize(2, 2);
        assertEquals(2, matrix.getSizeX(), "Matrix should have 2 rows after change.");
        assertEquals(2, matrix.getSizeY(), "Matrix should have 2 columns after change.");
        assertFalse(matrix.get(0, 0), "New matrix value should be default false.");
    }

    @Test
    void testToJsonAndFromJson() {
        matrix.update(0, 0, true);
        File file = new File(testFilePath);
        matrix.toJson(file);

        BufferedMatrix<Boolean> newMatrix = new BufferedMatrix<>(1, 1, false);
        newMatrix.fromJson(file);
        assertTrue(newMatrix.get(0, 0), "Value at (0, 0) should be true after loading from JSON.");
    }

    @Test
    void testInvalidIndices() {
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            matrix.get(3, 3); 
        });
        assertEquals("Invalid matrix indices.", exception.getMessage());
    }

    @Test
    void testInvalidChangeSize() {
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            matrix.changeSize(0, 0); 
        });
        assertEquals("Invalid matrix size.", exception.getMessage());
    }
}
