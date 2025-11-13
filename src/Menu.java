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

    /**
     * Opens a HTML file that explains how to play this game
     * @param pPath Path to How To Play HTML file
     */
    protected void openHowToPlay(String pPath) {
        fHtp = new JFrame();
        fHtp.setTitle("Wie man spielt");
        fHtp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fHtp.setSize(500, 500);
        fHtp.setResizable(false);
        fHtp.setLocationRelativeTo(null); // Center the window
        fHtp.setIconImage(new ImageIcon("images\\daLogo.png").getImage());
        fHtp.setLayout(new BorderLayout());

        JEditorPane content = new JEditorPane();
        content.setEditable(false);
        content.setContentType("text/html");
        File htmlFile = new File(pPath);
        try {
            content.setPage(htmlFile.toURI().toURL());
        } catch(IOException e) {
            e.printStackTrace();
        }

        JScrollPane scorllableContent = new JScrollPane(content);

        JButton bBack = new JButton("Verstanden!");
        bBack.addActionListener(_ -> {
            fHtp.dispose();
        });

        fHtp.add(scorllableContent, BorderLayout.CENTER);
        fHtp.add(bBack, BorderLayout.SOUTH);
        fHtp.setVisible(true);
    }

    /**
     * Reads settings from JSON file
     * @param filepath path to settings JSON file
     * @return Settings object with data from JSON file
     */
    protected Settings readSettings(String filepath) {
        Settings settingsFromJson = new Settings();
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filepath)) {
            settingsFromJson = gson.fromJson(reader, Settings.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return settingsFromJson;
    }

    /**
     * Overrides settings in settings.json file
     * @param pSettings settings object with parameters to override settings
     */
    protected void saveSettings(Settings pSettings) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter("data\\settings.json")) {
            gson.toJson(pSettings, writer);
            System.out.println("Saved settings to \"data/settings.json\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}