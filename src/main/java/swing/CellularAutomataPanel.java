package swing;

import game.CellularAutomata;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CellularAutomataPanel extends JPanel implements KeyListener {
    CellularAutomata cellularAutomata;
    boolean playingToggle = false;
    Thread playThread = null;

    public CellularAutomataPanel(CellularAutomata cellularAutomata) {
        this.cellularAutomata = cellularAutomata;
        addKeyListener(this);
        setFocusable(true);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_P -> {
                if (!playingToggle) {
                    startPlaying();
                } else {
                    stopPlaying();
                }
                playingToggle = !playingToggle;
                break;
            }
            case KeyEvent.VK_N -> cellularAutomata.next();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void startPlaying() {
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
    }

    private void stopPlaying() {
        if (playThread != null) {
            playThread.interrupt();
            try {
                playThread.join();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            playThread = null;
        }
    }
}

