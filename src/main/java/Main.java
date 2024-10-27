import javax.swing.JFrame;

import game.*;
import swing.*;

/**
 * The Main class serves as the entry point for the Game of Life application.
 * It sets up the main frame and the panels for different game states, such as the main menu,
 * pause menu, matrix size selection, the game grid, and game controls. 
 * The game uses a card layout system to switch between these different panels.
 * 
 * <p>The application is based on John Conway's Game of Life, a cellular automaton that simulates
 * the evolution of a grid of cells according to specific rules. This class initializes
 * the main game grid and control panels, handling the flow between different game states.
 */
public class Main {
    
    /**
     * The main method is the entry point for the Game of Life application.
     * It creates the main application window, sets its size, and initializes
     * the various panels that allow users to control the game.
     *
     * <p>This includes:
     * <ul>
     *     <li>A main menu for starting or loading a game</li>
     *     <li>A pause menu for saving and controlling game behavior</li>
     *     <li>A matrix size selection panel to configure the game grid dimensions</li>
     *     <li>The main game grid where the simulation takes place</li>
     *     <li>Controls for running or pausing the simulation</li>
     * </ul>
     *
     * <p>The game uses a {@link CardLayoutSwitcherPanel} to switch between different
     * screens in the game, such as the main menu, the game grid, and other control panels.
     * 
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame("The Game Of Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);

        frame.setLocationRelativeTo(null);

        BufferedMatrix<Boolean> bufferedMatrix = new BufferedMatrix<>(40,40,false);

        CardLayoutSwitcherPanel switcher = new CardLayoutSwitcherPanel();
        switcher.addPanel("home", new MainMenuPanel(switcher,bufferedMatrix));
        switcher.addPanel("pause", new PauseMenuPanel(switcher,bufferedMatrix));
        switcher.addPanel("matrixSize", new MatrixSizeMenuPanel(switcher, bufferedMatrix));
        switcher.addPanel("grid", new ScalableGridPanel(bufferedMatrix));
        switcher.addPanel("nextStep",new CellularAutomataPanel(new CellularAutomata(bufferedMatrix)));
        switcher.addPanel("gameControls",new GameControlsPanel(switcher));

        switcher.switchTo("home");

        frame.add(switcher);

        frame.setVisible(true);
    }
}
