import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.google.gson.Gson;

public abstract class Menu extends JFrame {
    protected JButton[] buttons = new JButton[5];

    /**
     * Changes background colour of all buttons to dark grey
     */
    protected void darkmodeButtons() {
        for (JButton button : buttons) {
            button.setBackground(new Color(50, 50, 50));
        }
    }
}