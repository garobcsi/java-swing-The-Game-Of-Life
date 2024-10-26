package game;

import java.util.Arrays;

public class BufferedMatrix<T> {
    private int x;
    private int y;
    private T[][] currentMatrix;
    private T[][] nextMatrix;
    private final T defaultValue;

    public BufferedMatrix(int x, int y, T defaultValue) {
        this.x = x;
        this.y = y;
        this.defaultValue = defaultValue;
        this.currentMatrix = (T[][]) new Object[x][y];
        this.nextMatrix = (T[][]) new Object[x][y];
        fillMatrix(currentMatrix, defaultValue);
        fillMatrix(nextMatrix, defaultValue);
    }

    private void fillMatrix(T[][] matrix, T value) {
        for (int i = 0; i < x; i++) {
            Arrays.fill(matrix[i], value);
        }
    }

    private void validateIndices(int x, int y) {
        if (x < 0 || x >= this.x || y < 0 || y >= this.y) {
            throw new IndexOutOfBoundsException("Invalid matrix indices.");
        }
    }

    public T get(int x, int y) {
        validateIndices(x, y);
        return currentMatrix[x][y];
    }

    public void update(int x, int y, T value) {
        validateIndices(x, y);
        currentMatrix[x][y] = value;
    }

    public void set(int x, int y, T value) {
        validateIndices(x, y);
        nextMatrix[x][y] = value;
    }

    public void next() {
        currentMatrix = nextMatrix;
        nextMatrix = (T[][]) new Object[this.x][this.y];
        fillMatrix(nextMatrix, defaultValue);
    }

    public void clear() {
        fillMatrix(currentMatrix, defaultValue);
        fillMatrix(nextMatrix, defaultValue);
    }

    public void changeSize(int x, int y) {
        if (x < 1 || y < 1) throw new IndexOutOfBoundsException("Invalid matrix size.");
        this.x = x;
        this.y = y;
        this.currentMatrix = (T[][]) new Object[x][y];
        this.nextMatrix = (T[][]) new Object[x][y];
        fillMatrix(currentMatrix, defaultValue);
        fillMatrix(nextMatrix, defaultValue);
    }

    public int getSizeX() {
        return x;
    }

    public int getSizeY() {
        return y;
    }
}
