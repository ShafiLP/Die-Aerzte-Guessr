import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

public class LueckenfuellerMenu extends JFrame {
    // Settings for the settings frame
    private Settings settings;

    /**
     * Crates a GUI for starting the game and configuring settings
     */
    public LueckenfuellerMenu() {
        settings = readSettings("data\\settings.json");

        this.setTitle("Lückenfüller");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 300);
        this.setResizable(false);
        this.setLocationRelativeTo(null); // Center the window
        this.setIconImage(new ImageIcon("images\\daLogo.png").getImage()); //TODO: each window gets its own funny icon

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 30, 40)); // Padding

        // Heading
        JPanel headingPanel = new JPanel();
        ImageIcon icon = new ImageIcon("images\\Lueckenfueller.png");
        Image img = icon.getImage().getScaledInstance(270, 35, Image.SCALE_SMOOTH);
        JLabel lHeading = new JLabel(new ImageIcon(img));
        headingPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        headingPanel.add(lHeading);
        this.add(headingPanel, BorderLayout.NORTH);

        // Button panel for start, settings, etc.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // Buttons
        JButton bPlay = new JButton("Spielen");
        bPlay.addActionListener(_ -> {
            this.dispose();
            new LueckenfuellerGame(settings);
        });
        JButton bSettings = new JButton("Einstellungen");
        bSettings.addActionListener(_ -> {
            openSettings();
        });
        JButton bBack = new JButton("Hauptmenü");
        bBack.addActionListener(_ -> {
            this.dispose();
            new MainMenu();
        });

        Dimension buttonSize = new Dimension(200, 40);
        bPlay.setMaximumSize(buttonSize);
        bSettings.setMaximumSize(buttonSize);
        bBack.setMaximumSize(buttonSize);

        bPlay.setAlignmentX(Component.CENTER_ALIGNMENT);
        bSettings.setAlignmentX(Component.CENTER_ALIGNMENT);
        bBack.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Highscore
        JPanel highscorePanel = new JPanel();
        JLabel lHighscore = new JLabel("Highscore: " + settings.getCtlHighscore());
        highscorePanel.setBorder(BorderFactory.createEmptyBorder(3, 15, 3, 15));
        highscorePanel.add(lHighscore);
        this.add(highscorePanel, BorderLayout.SOUTH);

        // Add with vertical padding
        buttonPanel.add(bPlay);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(bSettings);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(bBack);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        this.add(mainPanel);
        this.setVisible(true);
    }

    /**
     * Opens the settings menu
     * Called when user clicks on settings button
     */
    private void openSettings() {
        JFrame settingsFrame = new JFrame();
        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingsFrame.setSize(500, 200);
        settingsFrame.setResizable(false);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setLayout(new GridLayout(4, 2)); // Icons, hardmode, zeit, unendlich zeit, leben, unendlich leben, farin, bela, sahnie, support sahnie, speichern, highscore reset

        // Timer settings
        JLabel lTimeLimit = new JLabel("Zeitlimit (in Sekunden):");
        JTextField tfTimeLimit = new JTextField(settings.getCtlTimeLimit() + "");
        JPanel panTimeLimit = new JPanel(new GridLayout(2, 1));
        panTimeLimit.add(lTimeLimit);  panTimeLimit.add(tfTimeLimit);
        JCheckBox cbUnlimitedTime = new JCheckBox("Ohne Zeitlimit", settings.isCtlUnlimitedTimeEnabled());
        cbUnlimitedTime.addActionListener(_ -> {
            if(cbUnlimitedTime.isSelected()) {
                tfTimeLimit.setEnabled(false);
            } else {
                tfTimeLimit.setEnabled(true);
            }
        });

        // Health bar settings
        JLabel lLives = new JLabel("Anzahl Leben:");
        JTextField tfLives = new JTextField(settings.getCtlLiveCount() + "");
        JPanel panLives = new JPanel(new GridLayout(2, 1));
        panLives.add(lLives); panLives.add(tfLives);
        JCheckBox cbUnlimitedLives = new JCheckBox("Ohne Leben", settings.isCtlUnlimitedLivesEnabled());
        cbUnlimitedLives.addActionListener(_ -> {
            if(cbUnlimitedLives.isSelected()) {
                tfLives.setEnabled(false);
            } else {
                tfLives.setEnabled(true);
            }
        });

        // Highscore reset button
        JButton bResetHighscore = new JButton("Highscore zurücksetzen");
        bResetHighscore.addActionListener(_ -> {
            settings.setCtlHighscore(0);
        });

        // Hardmode settings
        JCheckBox cbHardmode = new JCheckBox("Schwieriger Modus", settings.isCtlHardmodeEnabled());

        // Save button
        JButton bSave = new JButton("Speichern");
        bSave.addActionListener(_ -> {
            // Check if time limit input is valid
            try {
                settings.setCtlTimeLimit(Integer.parseInt(tfTimeLimit.getText()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    bSave,
                    "Eingabe des Zeitlimits ist ungültig.\nMuss eine Ganzzahl sein!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if(settings.getCtlTimeLimit() > 1000) {
                JOptionPane.showMessageDialog(
                    bSave,
                    "Eingabe des Zeitlimits ist ungültig.\nDas Limit darf nicht höher als 1000 sein!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Check if live count input is valid
            try {
                settings.setCtlLiveCount(Integer.parseInt(tfLives.getText()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    bSave,
                    "Eingabe der Lebensanzahl ist ungültig.\nMuss eine Ganzzahl sein!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if(settings.getCtlLiveCount() > 10) {
                settings.setCtlLiveCount(3); 
                JOptionPane.showMessageDialog(
                    bSave,
                    "Eingabe der Lebensanzahl ist ungültig.\nDie Leben dürfen maximal 10 sein!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            settings.setCtlUnlimitedTime(cbUnlimitedTime.isSelected());
            settings.setCtlUnlimitedLives(cbUnlimitedLives.isSelected());
            settings.setCtlHardmode(cbHardmode.isSelected());

            saveSettings(settings);
            settingsFrame.dispose();
        });

        // Add all to frame
        settingsFrame.add(panTimeLimit);
        settingsFrame.add(panLives);
        settingsFrame.add(cbUnlimitedTime);
        settingsFrame.add(cbUnlimitedLives);
        settingsFrame.add(cbHardmode);
        settingsFrame.add(new JLabel());
        settingsFrame.add(bResetHighscore);
        settingsFrame.add(bSave);

        // Set frame visible
        settingsFrame.setVisible(true);
    }

    /**
     * Reads settings from JSON file
     * @param filepath path to settings JSON file
     * @return Settings object with data from JSON file
     */
    private Settings readSettings(String filepath) {
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
    private void saveSettings(Settings pSettings) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter("data\\settings.json")) {
            gson.toJson(pSettings, writer);
            System.out.println("Saved settings to \"data/settings.json\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
