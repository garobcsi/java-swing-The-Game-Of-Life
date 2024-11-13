package swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import game.BufferedMatrix;

/**
 * This class represents the menu panel for setting the size of the matrix in the Game of Life application.
 * It allows the user to specify the number of rows and columns for the grid.
 * The panel includes buttons for navigation and input fields for user interaction.
 */
public class MatrixSizeMenuPanel extends JPanel implements KeyListener {
    
    /**
     * The CardLayoutSwitcherPanel that manages the switching between different panels.
     */
    private final CardLayoutSwitcherPanel switcher;
    
    /**
     * The BufferedMatrix that holds the state of the grid.
     */
    private final BufferedMatrix<Boolean> matrix;

    /**
     * Constructs a new MatrixSizeMenuPanel with the specified switcher and matrix.
     *
     * @param switcher The CardLayoutSwitcherPanel used for navigating between panels.
     * @param matrix   The BufferedMatrix to be configured with user-defined dimensions.
     */
    public MatrixSizeMenuPanel(CardLayoutSwitcherPanel switcher, BufferedMatrix<Boolean> matrix) {
        this.switcher = switcher;
        this.matrix = matrix;
        setFocusable(true);
        addKeyListener(this);

        render();
    }

    /**
     * Initializes and arranges the components of the panel, including labels, spinners,
     * and buttons for user interaction.
     */
    private void render() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton backButton = createButton("Back");
        backButton.addActionListener(e -> switcher.switchTo("home"));

        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 15, 15, 15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(createLabel("Number of Rows:"), gbc);

        JSpinner rowsSpinner = createSpinner();
        gbc.gridx = 1;
        contentPanel.add(rowsSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(createLabel("Number of Columns:"), gbc);

        JSpinner colsSpinner = createSpinner();
        gbc.gridx = 1;
        contentPanel.add(colsSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton submitButton = createButton("Set Matrix Size");
        submitButton.addActionListener(e -> {
            matrix.changeSize((Integer) rowsSpinner.getValue(), (Integer) colsSpinner.getValue());
            switcher.switchTo("grid");
            switcher.registerKeyListener("pause");
            switcher.registerKeyListener("nextStep");
        });
        contentPanel.add(submitButton, gbc);

        add(contentPanel, BorderLayout.CENTER);
    }

    /**
     * Creates a JButton with the specified text and default font settings.
     *
     * @param text The text to be displayed on the button.
     * @return A JButton configured with the specified text.
     */
    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 24));
        return button;
    }

    /**
     * Creates a JLabel with the specified text and default font settings.
     *
     * @param text The text to be displayed on the label.
     * @return A JLabel configured with the specified text.
     */
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 24));
        return label;
    }

    /**
     * Creates a JSpinner configured to allow numeric input for specifying matrix dimensions.
     *
     * @return A JSpinner configured with a default value and range.
     */
    private JSpinner createSpinner() {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(40, 1, 100, 1));
        spinner.setFont(new Font("Arial", Font.PLAIN, 20));
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setColumns(5);
        return spinner;
    }

    /**
     * Processes a key typed event.
     *
     * @param e The key event to be processed.
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    /**
     * Processes a key pressed event. Allows the user to navigate back to the home panel using the ESC key.
     *
     * @param e The key event to be processed.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) switcher.switchTo("home");
    }

    /**
     * Processes a key released event.
     *
     * @param e The key event to be processed.
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
