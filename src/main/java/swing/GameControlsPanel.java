package swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The GameControlsPanel class provides a user interface for displaying 
 * the controls of a game. It extends JPanel and implements KeyListener
 * to handle keyboard events.
 *
 * <p>This panel includes a back button that allows the user to return 
 * to the pause menu and displays labels for keyboard and mouse controls, 
 * including movement commands, game controls, and mouse interaction commands.</p>
 * 
 * <p>The panel listens for key events and responds to the ESC key by switching 
 * to the pause screen.</p>
 */
public class GameControlsPanel extends JPanel implements KeyListener {
    private final CardLayoutSwitcherPanel switcher;

    /**
     * Constructs a GameControlsPanel with the specified CardLayoutSwitcherPanel.
     * 
     * @param switcher The CardLayoutSwitcherPanel used to switch between 
     * different views of the application.
     */
    public GameControlsPanel(CardLayoutSwitcherPanel switcher) {
        this.switcher = switcher;
        setFocusable(true);
        addKeyListener(this);

        render();
    }

    /**
     * Renders the user interface components of the panel, including the back 
     * button and control labels. This method sets the layout and adds all 
     * necessary components to the panel.
     */
    private void render() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 20));
        backButton.addActionListener(e -> {
            switcher.switchTo("pause");
            switcher.registerKeyListener("pause");
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);
        topPanel.setOpaque(false);
        add(topPanel, BorderLayout.NORTH);

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.setOpaque(false);

        JLabel keyboardLabel = createTitleLabel("Keyboard Controls:");
        JLabel movementLabel = createControlLabel("WASD / Arrow Keys - Screen Movement");
        JLabel playPauseLabel = createControlLabel("P - Play/Pause the Game");
        JLabel nextStepLabel = createControlLabel("N - Next Step");
        JLabel randomizeLabel = createControlLabel("F - Randomize");
        JLabel resetLabel = createControlLabel("R - Reset");
        JLabel homeLabel = createControlLabel("HOME - Return Display To Home");

        JLabel mouseLabel = createTitleLabel("Mouse Controls:");
        JLabel toggleCellLabel = createControlLabel("Left Click - Toggle Cell");
        JLabel drawLabel = createControlLabel("Middle Click - Draw");
        JLabel panLabel = createControlLabel("Right Click - Pan Display");

        controlsPanel.add(keyboardLabel);
        controlsPanel.add(movementLabel);
        controlsPanel.add(playPauseLabel);
        controlsPanel.add(nextStepLabel);
        controlsPanel.add(randomizeLabel);
        controlsPanel.add(resetLabel);
        controlsPanel.add(homeLabel);

        controlsPanel.add(Box.createVerticalStrut(20));

        controlsPanel.add(mouseLabel);
        controlsPanel.add(toggleCellLabel);
        controlsPanel.add(drawLabel);
        controlsPanel.add(panLabel);

        add(controlsPanel, BorderLayout.CENTER);
    }

    /**
     * Creates a JLabel for section titles within the controls panel.
     * 
     * @param text The text to display in the label.
     * @return A JLabel with the specified text and a bold font style.
     */
    private JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    /**
     * Creates a JLabel for individual control instructions within the controls panel.
     * 
     * @param text The text to display in the label.
     * @return A JLabel with the specified text and a plain font style.
     */
    private JLabel createControlLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 24));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    /**
     * Processes key typed events. This method is empty and can be overridden 
     * in subclasses to provide specific functionality.
     * 
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Processes key pressed events. If the ESC key is pressed, this method 
     * switches the view to the pause screen.
     * 
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            switcher.switchTo("pause");
            switcher.registerKeyListener("pause");
        }
    }

    /**
     * Processes key released events. This method is empty and can be overridden 
     * in subclasses to provide specific functionality.
     * 
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }
}