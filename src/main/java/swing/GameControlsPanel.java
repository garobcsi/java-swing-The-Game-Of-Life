package swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameControlsPanel extends JPanel implements KeyListener {
    private final CardLayoutSwitcherPanel switcher;

    public GameControlsPanel(CardLayoutSwitcherPanel switcher) {
        this.switcher = switcher;
        setFocusable(true);
        addKeyListener(this);

        render();
    }

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

    private JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 28));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JLabel createControlLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 24));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            switcher.switchTo("pause");
            switcher.registerKeyListener("pause");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}