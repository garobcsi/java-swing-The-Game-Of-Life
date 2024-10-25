package swing;

import javax.swing.*;

public class MatrixSizeMenuPanel extends JPanel {
    private final CardLayoutSwitcherPanel switcher;
    private final boolean[][] matrix;

    MatrixSizeMenuPanel(CardLayoutSwitcherPanel switcher, boolean[][] matrix) {
        this.switcher = switcher;
        this.matrix = matrix;
        setFocusable(true);
        render();
    }

    private void render() {

    }
}
