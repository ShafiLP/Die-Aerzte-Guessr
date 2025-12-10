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
    protected Settings settings;
    protected JButton[] buttons = new JButton[4];
    protected JFrame fHtp;

    /**
     * Initializes FlatLaf GUI
     */
    protected void setup() {
        // Set Theme
        if(settings.isDarkMode()){
            FlatDarkLaf.setup();
            com.formdev.flatlaf.FlatDarkLaf.updateUI();
        } else {
            FlatLightLaf.setup();
            com.formdev.flatlaf.FlatLaf.updateUI();
        }
    }

    /**
     * Changes background colour of all buttons to dark grey
     */
    protected void darkmodeButtons() {
        for(int i = 0; i < buttons.length; i++) {
            buttons[i].setBackground(new Color(50, 50, 50));
        }
    }
}