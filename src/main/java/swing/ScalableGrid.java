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
import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ScalableGrid extends JPanel implements MouseWheelListener, KeyListener, ActionListener, MouseListener,
        MouseMotionListener, ComponentListener {
    private static final int CELL_SIZE = 50;
    private final boolean[][] matrix;
    private double scale = 1.0;
    private double targetScale = 1.0;
    private double offsetX = 0, offsetY = 0;
    private double targetOffsetX = 0, targetOffsetY = 0;
    private int lastMouseX, lastMouseY;
    private boolean panning = false, drawPanning = false;

    public ScalableGrid(boolean[][] matrix) {
        this.matrix = matrix;
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

    private int[] getCellCoordinates(int mouseX, int mouseY) {
        int realX = (int) ((mouseX - offsetX) / scale);
        int realY = (int) ((mouseY - offsetY) / scale);

        int col = realX / CELL_SIZE;
        int row = realY / CELL_SIZE;

        if (row >= 0 && row < matrix.length && col >= 0 && col < matrix[0].length) {
            return new int[] { row, col };
        }
        return null;
    }

    private void toggleCell(int mouseX, int mouseY) {
        int[] coordinates = getCellCoordinates(mouseX, mouseY);
        if (coordinates != null) {
            int row = coordinates[0];
            int col = coordinates[1];
            matrix[row][col] = !matrix[row][col];
        }
    }

    private void setCell(int mouseX, int mouseY, boolean cellValue) {
        int[] coordinates = getCellCoordinates(mouseX, mouseY);
        if (coordinates != null) {
            int row = coordinates[0];
            int col = coordinates[1];
            matrix[row][col] = cellValue;
        }
    }

    private void resetPosition() {
        targetOffsetX = 0;
        targetOffsetY = 0;
        targetScale = 1.0;
        calculateInitialFit();
    }

    private void calculateInitialFit() {
        double panelWidth = getWidth();
        double panelHeight = getHeight();
        double gridWidth = matrix[0].length * CELL_SIZE;
        double gridHeight = matrix.length * CELL_SIZE;

        double scaleX = panelWidth / gridWidth;
        double scaleY = panelHeight / gridHeight;

        targetScale = Math.min(scaleX, scaleY);
    }

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
        int endCol = Math.min(matrix[0].length, (int) ((getWidth() - offsetX) / scale / CELL_SIZE) + 1);
        int endRow = Math.min(matrix.length, (int) ((getHeight() - offsetY) / scale / CELL_SIZE) + 1);

        for (int row = startRow; row < endRow; row++) {
            for (int col = startCol; col < endCol; col++) {
                int x = col * CELL_SIZE;
                int y = row * CELL_SIZE;

                if (matrix[row][col]) {
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

    @Override
    public void actionPerformed(ActionEvent e) {
        offsetX += ((targetOffsetX - offsetX) * 0.1);
        offsetY += ((targetOffsetY - offsetY) * 0.1);
        scale += (targetScale - scale) * 0.1;

        repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getPreciseWheelRotation() < 0) {
            targetScale += 0.1;
        } else {
            targetScale -= 0.1;
        }
    }

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

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

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
                Arrays.stream(matrix).forEach(x -> Arrays.fill(x, false));
                break;
            }
            case KeyEvent.VK_F: {
                Random random = new Random();
                Arrays.stream(matrix).forEach(row -> {
                    for (int i = 0; i < row.length; i++) {
                        row[i] = random.nextBoolean();
                    }
                });
                break;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
        resetPosition();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
}
