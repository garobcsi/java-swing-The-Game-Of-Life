package swing;

import javax.swing.*;

public class MatrixSizeMenu extends JPanel {
    private final CardLayoutSwitcher switcher;
    private final boolean[][] matrix;

    MatrixSizeMenu(CardLayoutSwitcher switcher,boolean[][] matrix) {
        this.switcher = switcher;
        this.matrix = matrix;
        setFocusable(true);
        render();
    }

    private void render() {

    }
}
