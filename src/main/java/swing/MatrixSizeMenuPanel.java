package swing;

import game.BufferedMatrix;

import javax.swing.*;

public class MatrixSizeMenuPanel extends JPanel {
    private final CardLayoutSwitcherPanel switcher;
    private final BufferedMatrix<Boolean> matrix;

    MatrixSizeMenuPanel(CardLayoutSwitcherPanel switcher, BufferedMatrix<Boolean> matrix) {
        this.switcher = switcher;
        this.matrix = matrix;
        setFocusable(true);
        render();
    }

    private void render() {

    }
}
