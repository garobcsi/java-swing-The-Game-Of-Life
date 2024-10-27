package game;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A generic class representing a two-dimensional buffered matrix that supports
 * operations such as getting and setting values, updating the current matrix,
 * and saving/loading the matrix to/from JSON files.
 *
 * @param <T> the type of elements in the matrix
 */
public class BufferedMatrix<T> {
    /**
     * The number of rows in the matrix.
     */
    private int x;

    /**
     * The number of columns in the matrix.
     */
    private int y;

    /**
     * The current state of the matrix.
     */
    private T[][] currentMatrix;

    /**
     * The next state of the matrix, used for double buffering.
     */
    private transient T[][] nextMatrix;

    /**
     * The default value used to fill the matrix.
     */
    private T defaultValue;

    /**
     * Constructs a new BufferedMatrix with the specified dimensions and default value.
     *
     * @param x            the number of rows in the matrix
     * @param y            the number of columns in the matrix
     * @param defaultValue the default value to fill the matrix
     */
    public BufferedMatrix(int x, int y, T defaultValue) {
        this.x = x;
        this.y = y;
        this.defaultValue = defaultValue;
        this.currentMatrix = (T[][]) new Object[x][y];
        this.nextMatrix = (T[][]) new Object[x][y];
        fillMatrix(currentMatrix, defaultValue);
        fillMatrix(nextMatrix, defaultValue);
    }

    /**
     * Fills the specified matrix with the given value.
     *
     * @param matrix the matrix to fill
     * @param value  the value to fill the matrix with
     */
    private void fillMatrix(T[][] matrix, T value) {
        for (int i = 0; i < x; i++) {
            Arrays.fill(matrix[i], value);
        }
    }

    /**
     * Validates the specified indices to ensure they are within the matrix bounds.
     *
     * @param x the row index
     * @param y the column index
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     */
    private void validateIndices(int x, int y) {
        if (x < 0 || x >= this.x || y < 0 || y >= this.y) {
            throw new IndexOutOfBoundsException("Invalid matrix indices.");
        }
    }

    /**
     * Retrieves the value at the specified position in the current matrix.
     *
     * @param x the row index
     * @param y the column index
     * @return the value at the specified position
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     */
    public T get(int x, int y) {
        validateIndices(x, y);
        return currentMatrix[x][y];
    }

    /**
     * Updates the value at the specified position in the current matrix.
     *
     * @param x     the row index
     * @param y     the column index
     * @param value the new value to set
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     */
    public void update(int x, int y, T value) {
        validateIndices(x, y);
        currentMatrix[x][y] = value;
    }

    /**
     * Sets a value at the specified position in the next matrix.
     *
     * @param x     the row index
     * @param y     the column index
     * @param value the value to set
     * @throws IndexOutOfBoundsException if the indices are out of bounds
     */
    public void set(int x, int y, T value) {
        validateIndices(x, y);
        nextMatrix[x][y] = value;
    }

    /**
     * Advances the current matrix to the next state and clears the next matrix.
     */
    public void next() {
        currentMatrix = nextMatrix;
        nextMatrix = (T[][]) new Object[this.x][this.y];
        fillMatrix(nextMatrix, defaultValue);
    }

    /**
     * Clears both the current and next matrices, resetting them to the default value.
     */
    public void clear() {
        fillMatrix(currentMatrix, defaultValue);
        fillMatrix(nextMatrix, defaultValue);
    }

    /**
     * Changes the size of the matrix to the specified dimensions, clearing the current
     * and next matrices.
     *
     * @param x the new number of rows
     * @param y the new number of columns
     * @throws IndexOutOfBoundsException if the new size is less than 1
     */
    public void changeSize(int x, int y) {
        if (x < 1 || y < 1) throw new IndexOutOfBoundsException("Invalid matrix size.");
        this.x = x;
        this.y = y;
        this.currentMatrix = (T[][]) new Object[x][y];
        this.nextMatrix = (T[][]) new Object[x][y];
        fillMatrix(currentMatrix, defaultValue);
        fillMatrix(nextMatrix, defaultValue);
    }

    /**
     * Returns the current number of rows in the matrix.
     *
     * @return the number of rows
     */
    public int getSizeX() {
        return x;
    }

    /**
     * Returns the current number of columns in the matrix.
     *
     * @return the number of columns
     */
    public int getSizeY() {
        return y;
    }

    /**
     * Serializes the current state of the matrix to a JSON file.
     *
     * @param file the file to which the matrix should be saved
     */
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

    /**
     * Deserializes the matrix state from a JSON file, updating the current instance.
     *
     * @param file the file from which to load the matrix
     */
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
