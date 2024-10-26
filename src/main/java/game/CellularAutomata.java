package game;

public class CellularAutomata {
    private final BufferedMatrix<Boolean> matrix;

    public CellularAutomata(BufferedMatrix<Boolean> matrix) {
        this.matrix = matrix;
    }

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
