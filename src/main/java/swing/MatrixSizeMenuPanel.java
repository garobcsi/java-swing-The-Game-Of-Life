package swing;

import game.BufferedMatrix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MatrixSizeMenuPanel extends JPanel implements KeyListener {
    private final CardLayoutSwitcherPanel switcher;
    private final BufferedMatrix<Boolean> matrix;

    MatrixSizeMenuPanel(CardLayoutSwitcherPanel switcher, BufferedMatrix<Boolean> matrix) {
        this.switcher = switcher;
        this.matrix = matrix;
        setFocusable(true);
        addKeyListener(this);

        render();
    }

    private void render() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton backButton = createButton("Back", 24);
        backButton.addActionListener(e -> switcher.switchTo("home"));

        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 15, 15, 15);

        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(createLabel("Number of Rows:", 24), gbc);

        JSpinner rowsSpinner = createSpinner(40);
        gbc.gridx = 1;
        contentPanel.add(rowsSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(createLabel("Number of Columns:", 24), gbc);

        JSpinner colsSpinner = createSpinner(40);
        gbc.gridx = 1;
        contentPanel.add(colsSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton submitButton = createButton("Set Matrix Size", 26);
        submitButton.addActionListener(e -> {
            matrix.changeSize((Integer) colsSpinner.getValue(), (Integer) rowsSpinner.getValue());
            switcher.switchTo("grid");
            switcher.registerKeyListener("pause");
            switcher.registerKeyListener("nextStep");
        });
        contentPanel.add(submitButton, gbc);

        add(contentPanel, BorderLayout.CENTER);
    }

    private JButton createButton(String text, int fontSize) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, fontSize));
        return button;
    }

    private JLabel createLabel(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, fontSize));
        return label;
    }

    private JSpinner createSpinner(int initialValue) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(initialValue, 1, 100, 1));
        spinner.setFont(new Font("Arial", Font.PLAIN, 20));
        ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField().setColumns(5);
        return spinner;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) switcher.switchTo("home");
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
