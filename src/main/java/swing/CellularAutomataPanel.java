package swing;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import game.CellularAutomata;

/**
 * A JPanel that visualizes and controls a Cellular Automaton.
 * It allows users to start and stop the simulation, as well as step through
 * the simulation one generation at a time using keyboard input.
 *
 * <p>This panel listens for key events and responds to specific key presses:
 * <ul>
 *     <li>P: Toggles the play/pause state of the simulation.</li>
 *     <li>N: Advances the simulation to the next generation.</li>
 * </ul>
 * </p>
 *
 * @see CellularAutomata
 */
public class CellularAutomataPanel extends JPanel implements KeyListener {
    /**
     * The instance of the CellularAutomata that this panel visualizes and controls.
     */
    CellularAutomata cellularAutomata;

    /**
     * A flag indicating whether the simulation is currently playing (true) or paused (false).
     */
    boolean playingToggle = false;

    /**
     * The thread that runs the simulation when playing.
     */
    Thread playThread = null;

    /**
     * Constructs a CellularAutomataPanel with the specified CellularAutomata.
     *
     * @param cellularAutomata the CellularAutomata instance to be visualized and controlled by this panel.
     */
    public CellularAutomataPanel(CellularAutomata cellularAutomata) {
        this.cellularAutomata = cellularAutomata;
        addKeyListener(this);
        setFocusable(true);
    }

    /**
     * Invoked when a key has been typed. This implementation does not perform any action.
     *
     * @param e the key event to be processed.
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Invoked when a key has been pressed. Responds to specific key events:
     * <ul>
     *     <li>P: Toggles the play/pause state of the simulation.</li>
     *     <li>N: Advances the simulation to the next generation.</li>
     * </ul>
     *
     * @param e the key event to be processed.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_P -> {
                if (!playingToggle) {
                    startPlaying();
                } else {
                    stopPlaying();
                }
                break;
            }
            case KeyEvent.VK_N -> cellularAutomata.next();
        }
    }

    /**
     * Invoked when a key has been released. This implementation does not perform any action.
     *
     * @param e the key event to be processed.
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * Starts the simulation in a separate thread, updating the CellularAutomata instance
     * at regular intervals.
     */
    public void startPlaying() {
        playThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                cellularAutomata.next();

                try {
                    Thread.sleep(60);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        playThread.start();

        playingToggle = true;
    }

    /**
     * Stops the simulation and waits for the playing thread to terminate.
     */
    public void stopPlaying() {
        if (playThread != null) {
            playThread.interrupt();
            try {
                playThread.join();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            playThread = null;
        }

        playingToggle = false;
    }
}

