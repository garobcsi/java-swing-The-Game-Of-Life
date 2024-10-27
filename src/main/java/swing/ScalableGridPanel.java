package swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import game.BufferedMatrix;

/**
 * ScalableGridPanel is a custom JPanel designed to display a grid based on a BufferedMatrix
 * of Boolean values. This panel supports zooming, panning, and modifying the grid cells via
 * mouse interactions and key presses. It is mainly used for visualizing and interacting with
 * cellular automata in the context of Conway's Game of Life or similar grid-based games.
 *
 * The class allows users to zoom in and out using the mouse wheel, pan around the grid
 * by dragging with the right mouse button, and toggle cells on or off by clicking them.
 * Additional functionality includes resetting the grid, randomizing cells, and moving the
 * viewport using keyboard inputs.
 */
public class ScalableGridPanel extends JPanel implements MouseWheelListener, KeyListener, ActionListener, MouseListener,
        MouseMotionListener, ComponentListener {

    /**
     * The matrix representing the grid where each cell holds a Boolean value
     * indicating whether the cell is alive (true) or dead (false).
     */
    private final BufferedMatrix<Boolean> matrix;

    /**
     * Random instance used for randomizing the grid.
     */
    private final Random random = new Random();

    /**
     * The size of each grid cell in pixels.
     */
    private static final int CELL_SIZE = 50;

    /**
     * The current zoom scale factor applied to the grid.
     */
    private double scale = 1.0;

    /**
     * The target zoom scale factor for smooth zooming transitions.
     */
    private double targetScale = 1.0;

    /**
     * The current horizontal and vertical offsets for panning.
     */
    private double offsetX = 0, offsetY = 0;

    /**
     * The target horizontal and vertical offsets for smooth panning transitions.
     */
    private double targetOffsetX = 0, targetOffsetY = 0;

    /**
     * The last recorded mouse coordinates used for panning the grid.
     */
    private int lastMouseX, lastMouseY;

    /**
     * A flag indicating whether panning mode is currently active.
     */
    private boolean panning = false;

    /**
     * A flag indicating whether draw-panning mode is active for drawing cells by dragging.
     */
    private boolean drawPanning = false;

    /**
     * Constructs a new ScalableGridPanel with the given BufferedMatrix.
     * 
     * @param bufferedMatrix The matrix representing the state of the grid.
     */
    public ScalableGridPanel(BufferedMatrix<Boolean> bufferedMatrix) {
        this.matrix = bufferedMatrix;
        addMouseWheelListener(this);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addComponentListener(this);
        setFocusable(true);
        
        Timer movementTimer = new Timer(16, this);
        movementTimer.start();

        setDoubleBuffered(true);
    }

    /**
     * Converts screen coordinates (mouse X, Y) to grid coordinates (row, column).
     * 
     * @param mouseX The X-coordinate of the mouse.
     * @param mouseY The Y-coordinate of the mouse.
     * @return An array with row and column coordinates, or null if outside the grid.
     */
    private int[] getCellCoordinates(int mouseX, int mouseY) {
        int realX = (int) ((mouseX - offsetX) / scale);
        int realY = (int) ((mouseY - offsetY) / scale);

        int col = realX / CELL_SIZE;
        int row = realY / CELL_SIZE;

        if (row >= 0 && row < matrix.getSizeX() && col >= 0 && col < matrix.getSizeY()) {
            return new int[] { row, col };
        }
        return null;
    }

    /**
     * Toggles the state of the cell (alive or dead) at the given mouse position.
     * 
     * @param mouseX The X-coordinate of the mouse.
     * @param mouseY The Y-coordinate of the mouse.
     */
    private void toggleCell(int mouseX, int mouseY) {
        int[] coordinates = getCellCoordinates(mouseX, mouseY);
        if (coordinates != null) {
            int row = coordinates[0];
            int col = coordinates[1];
            matrix.update(row,col,!matrix.get(row,col));
        }
    }

    /**
     * Sets the value of the cell at the given mouse position to a specified value.
     * 
     * @param mouseX The X-coordinate of the mouse.
     * @param mouseY The Y-coordinate of the mouse.
     * @param cellValue The value to set the cell to (true for alive, false for dead).
     */
    private void setCell(int mouseX, int mouseY, boolean cellValue) {
        int[] coordinates = getCellCoordinates(mouseX, mouseY);
        if (coordinates != null) {
            int row = coordinates[0];
            int col = coordinates[1];
            matrix.update(row,col,cellValue);
        }
    }

    /**
     * Resets the zoom and pan position to the default view.
     */
    private void resetPosition() {
        targetOffsetX = 0;
        targetOffsetY = 0;
        targetScale = 1.0;
        calculateInitialFit();
    }

    /**
     * Automatically calculates the best initial zoom to fit the entire grid within the panel.
     */
    private void calculateInitialFit() {
        double panelWidth = getWidth();
        double panelHeight = getHeight();
        double gridHeight = matrix.getSizeX() * CELL_SIZE;
        double gridWidth = matrix.getSizeY() * CELL_SIZE;

        double scaleX = panelWidth / gridWidth;
        double scaleY = panelHeight / gridHeight;

        targetScale = Math.min(scaleX, scaleY);
    }

    /**
     * Paints the grid of cells onto the panel. Cells are drawn as black (alive) or white (dead).
     * 
     * @param g The Graphics object used for drawing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform transform = new AffineTransform();
        transform.translate(offsetX, offsetY);
        transform.scale(scale, scale);
        g2d.setTransform(transform);

        int startCol = Math.max(0, (int) (-offsetX / scale / CELL_SIZE));
        int startRow = Math.max(0, (int) (-offsetY / scale / CELL_SIZE));
        int endRow = Math.min(matrix.getSizeX(), (int) ((getHeight() - offsetY) / scale / CELL_SIZE) + 1);
        int endCol = Math.min(matrix.getSizeY(), (int) ((getWidth() - offsetX) / scale / CELL_SIZE) + 1);

        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                int x = col * CELL_SIZE;
                int y = row * CELL_SIZE;

                if (matrix.get(row, col)) {
                    g2d.setColor(Color.BLACK);
                } else {
                    g2d.setColor(Color.WHITE);
                }
                g2d.fillRect(x, y, CELL_SIZE, CELL_SIZE);

                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    /**
     * Smoothly updates the zoom and pan offsets in response to user interactions.
     * 
     * @param e The ActionEvent triggered by the movement timer.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        offsetX += ((targetOffsetX - offsetX) * 0.1);
        offsetY += ((targetOffsetY - offsetY) * 0.1);
        scale += (targetScale - scale) * 0.1;

        repaint();
    }

    /**
     * Responds to mouse wheel scrolling to zoom in or out of the grid.
     * 
     * @param e The MouseWheelEvent triggered by scrolling.
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getPreciseWheelRotation() < 0) {
            targetScale += 0.1;
        } else {
            targetScale -= 0.1;
        }
    }

    /**
     * Handles mouse press events. Depending on the button pressed,
     * it either toggles the cell at the mouse location, enables draw-panning,
     * or activates panning mode.
     *
     * @param e The MouseEvent triggered by the mouse press.
     */
    @Override
    public void mousePressed(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1: {
                toggleCell(e.getX(), e.getY());
                break;
            }
            case MouseEvent.BUTTON2: {
                drawPanning = true;
                break;
            }
            case MouseEvent.BUTTON3: {
                lastMouseX = e.getX();
                lastMouseY = e.getY();
                panning = true;
                break;
            }
        }
    }

    /**
     * Handles mouse release events. It deactivates panning or draw-panning
     * mode when the corresponding mouse buttons are released.
     *
     * @param e The MouseEvent triggered by the mouse release.
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON2: {
                drawPanning = false;
                break;
            }
            case MouseEvent.BUTTON3: {
                panning = false;
                break;
            }
        }
    }

    /**
     * Handles mouse drag events. If panning is active, it updates the target offsets
     * based on the mouse movement. If draw-panning is active, it sets the cell state
     * at the current mouse position to alive.
     *
     * @param e The MouseEvent triggered by dragging the mouse.
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (panning) {
            int deltaX = e.getX() - lastMouseX;
            int deltaY = e.getY() - lastMouseY;
            targetOffsetX += deltaX;
            targetOffsetY += deltaY;

            lastMouseX = e.getX();
            lastMouseY = e.getY();
        } else if (drawPanning) {
            setCell(e.getX(), e.getY(), true);
        }
    }

    /**
     * Responds to mouse click events. Currently does nothing.
     *
     * @param e The MouseEvent triggered by a mouse click.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * Responds to mouse move events. Currently does nothing.
     *
     * @param e The MouseEvent triggered by moving the mouse.
     */
    @Override
    public void mouseMoved(MouseEvent e) {
    }

    /**
     * Responds to mouse enter events. Currently does nothing.
     *
     * @param e The MouseEvent triggered when the mouse enters the component.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * Responds to mouse exit events. Currently does nothing.
     *
     * @param e The MouseEvent triggered when the mouse exits the component.
     */
    @Override
    public void mouseExited(MouseEvent e) {
    }

    /**
     * Handles key press events. It allows the user to move the view using
     * arrow keys or WASD, reset the view with the Home key, clear the grid
     * with the 'R' key, or randomize the grid with the 'F' key.
     *
     * @param e The KeyEvent triggered by a key press.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int moveAmount = 20;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A: {
                targetOffsetX -= moveAmount;
                break;
            }
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D: {
                targetOffsetX += moveAmount;
                break;
            }
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W: {
                targetOffsetY -= moveAmount;
                break;
            }
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S: {
                targetOffsetY += moveAmount;
                break;
            }
            case KeyEvent.VK_HOME: {
                resetPosition();
                break;
            }
            case KeyEvent.VK_R: {
                matrix.clear();
                break;
            }
            case KeyEvent.VK_F: {
                for (int i = 0; i < matrix.getSizeX(); i++) {
                    for (int j = 0; j < matrix.getSizeY(); j++) {
                        matrix.update(i,j,random.nextBoolean());
                    }
                }
                break;
            }
        }
    }

    /**
     * Responds to key release events. Currently does nothing.
     *
     * @param e The KeyEvent triggered by a key release.
     */
    @Override
    public void keyReleased(KeyEvent e) {
    }

    /**
     * Responds to key typed events. Currently does nothing.
     *
     * @param e The KeyEvent triggered by a key typed action.
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Handles component resize events. Resets the position of the view
     * to fit the resized component.
     *
     * @param e The ComponentEvent triggered by resizing the component.
     */
    @Override
    public void componentResized(ComponentEvent e) {
        resetPosition();
    }

    /**
     * Responds to component moved events. Currently does nothing.
     *
     * @param e The ComponentEvent triggered when the component is moved.
     */
    @Override
    public void componentMoved(ComponentEvent e) {
    }

    /**
     * Responds to component shown events. Resets the position of the view
     * when the component becomes visible.
     *
     * @param e The ComponentEvent triggered when the component is shown.
     */
    @Override
    public void componentShown(ComponentEvent e) {
        resetPosition();
    }

    /**
     * Responds to component hidden events. Currently does nothing.
     *
     * @param e The ComponentEvent triggered when the component is hidden.
     */
    @Override
    public void componentHidden(ComponentEvent e) {
    }
}
