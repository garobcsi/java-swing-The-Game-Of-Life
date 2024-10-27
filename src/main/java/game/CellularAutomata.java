package game;

/**
 * The {@code CellularAutomata} class represents a cellular automaton system,
 * where each cell can be either alive or dead. This class implements the rules
 * of Conway's Game of Life on a 2D matrix of boolean values.
 * 
 * <p>
 * The state of the cells is updated in discrete time steps, where the next 
 * state is determined by the current state and the number of alive neighbors.
 * </p>
 *
 * <p>
 * The rules for updating the state of each cell are as follows:
 * <ul>
 * <li>Any live cell with two or three live neighbors survives.</li>
 * <li>Any dead cell with exactly three live neighbors becomes a live cell.</li>
 * <li>All other live cells die in the next generation. Similarly, all other dead cells remain dead.</li>
 * </ul>
 * </p>
 * 
 * @see BufferedMatrix
 */
public class CellularAutomata {
    
    /**
     * The matrix representing the current state of the cellular automaton,
     * where each cell is either alive (true) or dead (false).
     */
    private final BufferedMatrix<Boolean> matrix;

    /**
     * Constructs a {@code CellularAutomata} instance with the specified matrix.
     *
     * @param matrix the initial state of the cellular automaton, represented 
     *               as a {@link BufferedMatrix} of Boolean values.
     */
    public CellularAutomata(BufferedMatrix<Boolean> matrix) {
        this.matrix = matrix;
    }

    /**
     * Counts the number of alive neighbors around a specified cell in the matrix.
     *
     * @param x the x-coordinate of the cell whose neighbors are to be counted.
     * @param y the y-coordinate of the cell whose neighbors are to be counted.
     * @return the number of alive neighbors surrounding the cell at (x, y).
     *         The count includes wrapping around the edges of the matrix.
     */
    private int countNeighbors(int x, int y) {
        int count = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;

                int nx = (x + i + matrix.getSizeX()) % matrix.getSizeX();
                int ny = (y + j + matrix.getSizeY()) % matrix.getSizeY();

                if (matrix.get(nx,ny)) count++;
                if (count > 3) return count;
            }
        }
        return count;
    }

    /**
     * Updates the state of the cellular automaton to the next generation.
     * <p>
     * This method applies the rules of the cellular automaton to all cells 
     * in the matrix and updates their states accordingly. After updating 
     * the state, it invokes the {@code next} method of the matrix to 
     * prepare for the next generation.
     * </p>
     */
    public void next() {
        for (int i = 0; i < matrix.getSizeX(); i++) {
            for (int j = 0; j < matrix.getSizeY(); j++) {
                int neighbors = countNeighbors(i, j);

                if (matrix.get(i,j)) {
                    matrix.set(i,j,(neighbors == 2 || neighbors == 3));
                } else {
                    matrix.set(i,j,(neighbors == 3));
                }
            }
        }
        matrix.next();

        System.gc();
    }
}
