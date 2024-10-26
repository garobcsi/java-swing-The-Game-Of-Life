package game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class BufferedMatrix<T> {
    private int x;
    private int y;
    private T[][] currentMatrix;
    private transient T[][] nextMatrix;
    private T defaultValue;

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

    public void toJson(File file) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(this,writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fromJson(File file) {
        Gson gson = new GsonBuilder().create();
        try (FileReader reader = new FileReader(file)) {
            BufferedMatrix<T> matrix = gson.fromJson(reader,BufferedMatrix.class);
            this.x = matrix.x;
            this.y = matrix.y;
            this.defaultValue = matrix.defaultValue;
            this.currentMatrix = (T[][]) new Object[x][y];
            this.nextMatrix = (T[][]) new Object[x][y];
            fillMatrix(nextMatrix, defaultValue);

            for (int i = 0; i< matrix.x; i++) {
                for (int j = 0; j < matrix.y; j++) {
                    this.currentMatrix[i][j] = matrix.currentMatrix[i][j];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
