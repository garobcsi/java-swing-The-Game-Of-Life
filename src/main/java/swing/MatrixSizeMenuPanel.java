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

        render();
    }

    private void render() {

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton backButton = new JButton("Back");
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);

        backButton.addActionListener(e -> {
            switcher.switchTo("home");
        });

        JPanel contentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel rowsLabel = new JLabel("Number of Rows:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(rowsLabel, gbc);

        JSpinner rowsSpinner = new JSpinner(new SpinnerNumberModel(40, 1, 100, 1));
        gbc.gridx = 1;
        gbc.gridy = 0;
        contentPanel.add(rowsSpinner, gbc);

        JLabel colsLabel = new JLabel("Number of Columns:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        contentPanel.add(colsLabel, gbc);

        JSpinner colsSpinner = new JSpinner(new SpinnerNumberModel(40, 1, 100, 1));
        gbc.gridx = 1;
        gbc.gridy = 1;
        contentPanel.add(colsSpinner, gbc);

        JButton submitButton = new JButton("Set Matrix Size");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        contentPanel.add(submitButton, gbc);

        submitButton.addActionListener(e -> {
            matrix.changeSize((Integer) colsSpinner.getValue(), (Integer) rowsSpinner.getValue());

            switcher.switchTo("grid");
            switcher.registerKeyListener("pause");
            switcher.registerKeyListener("nextStep");
        });

        add(contentPanel, BorderLayout.CENTER);
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
