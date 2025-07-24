import java.awt.event.*;

public class GuessTheOriginKeyListener extends Thread implements KeyListener {
    private GTOGui gui;

    public GuessTheOriginKeyListener(GTOGui pGui) {
        gui = pGui;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Check if the pressed key is Enter
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            gui.submitButtonPressed();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No action needed on key release
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No action needed on key typed
    }
}
