package game;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CellularAutomata {
    private final boolean[][] matrix;

    int sizeX = 0, sizeY = 0;

    public CellularAutomata(boolean[][] matrix) throws NotMatrixException {
        this.matrix = matrix;

        sizeX = matrix.length;
        sizeY = matrix[0].length;
        for (boolean[] i : matrix) {
            if (i.length != sizeY) throw new NotMatrixException("2D array is not a matrix !");
        }
    }

    private int countNeighbors(int x, int y) {
        int count = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;

                int nx = (x + i + sizeX) % sizeX;
                int ny = (y + j + sizeY) % sizeY;

                if (matrix[nx][ny]) count++;
                if (count > 3) return count;
            }
        }
        return count;
    }

    public void next() {
        int sizeX= matrix.length, sizeY = matrix[0].length;

        boolean[][] nextMatrix = new boolean[sizeX][sizeY];

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                int neighbors = countNeighbors(i, j);

                if (matrix[i][j]) {
                    nextMatrix[i][j] = (neighbors == 2 || neighbors == 3);
                } else {
                    nextMatrix[i][j] = (neighbors == 3);
                }
            }
        }

        for (int i = 0; i < sizeX; i++) {
            System.arraycopy(nextMatrix[i], 0, matrix[i], 0, sizeY);
        }

        System.gc();
    }
}
