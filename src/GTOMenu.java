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

public class GTOMenu extends JFrame {
    // Settings for the settings frame
    private Settings settings;

    /**
     * Crates a GUI for starting the game and configuring settings
     */
    public GTOMenu() {
        settings = readSettings("data\\settings.json");

        this.setTitle("Errate den Ursprung");
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
        ImageIcon icon = new ImageIcon("images\\GTO.png");
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
            this.dispose();        // Close the current gui
            new GTOGame(settings); // Start the game
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
        JLabel lHighscore = new JLabel("Highscore: " + settings.getHighscore());
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
        settingsFrame.pack();
        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingsFrame.setSize(500, 500);
        settingsFrame.setResizable(false);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setLayout(new GridLayout(6, 2));

        // Timer settings
        JLabel lTimeLimit = new JLabel("Zeitlimit (in Sekunden):");
        JTextField tfTimeLimit = new JTextField(settings.getTimeLimit() + "");
        JPanel panTimeLimit = new JPanel(new GridLayout(2, 1));
        panTimeLimit.add(lTimeLimit);  panTimeLimit.add(tfTimeLimit);
        JCheckBox cbUnlimitedTime = new JCheckBox("Ohne Zeitlimit", settings.isUnlimitedTimeEnabled());
        cbUnlimitedTime.addActionListener(_ -> {
            if(cbUnlimitedTime.isSelected()) {
                tfTimeLimit.setEnabled(false);
            } else {
                tfTimeLimit.setEnabled(true);
            }
        });

        // Health bar settings
        JLabel lLives = new JLabel("Anzahl Leben:");
        JTextField tfLives = new JTextField(settings.getLiveCount() + "");
        JPanel panLives = new JPanel(new GridLayout(2, 1));
        panLives.add(lLives); panLives.add(tfLives);
        JCheckBox cbUnlimitedLives = new JCheckBox("Ohne Leben", settings.isUnlimitedLivesEnabled());
        cbUnlimitedLives.addActionListener(_ -> {
            if(cbUnlimitedLives.isSelected()) {
                tfLives.setEnabled(false);
            } else {
                tfLives.setEnabled(true);
            }
        });

        // Icon settings
        JCheckBox cbShowIcons = new JCheckBox("Icons anzeigen", settings.isShowIconsEnabled());

        // Highscore reset button
        JButton bResetHighscore = new JButton("Highscore zurücksetzen");
        bResetHighscore.addActionListener(_ -> {
            settings.setHighscore(0);
        });

        // Bonus library settings
        JCheckBox cbFarin = new JCheckBox("Füge Farins Diskografie hinzu", settings.isFarinEnabled());
        JCheckBox cbBela = new JCheckBox("Füge Belas Diskografie hinzu", settings.isBelaEnabled());
        JCheckBox cbSahnie = new JCheckBox("Füge Sahnies Diskografie hinzu", settings.isSahnieEnabled());

        // Type of input dropdown
        JPanel typeOfInputPanel = new JPanel(new GridLayout(2, 1));
        JComboBox<String> ddTypeOfInput = new JComboBox<>();
        ddTypeOfInput.addItem("Dropdown Menü");
        ddTypeOfInput.addItem("Suchleiste");
        ddTypeOfInput.addItem("Manuelle Eingabe");
        ddTypeOfInput.setSelectedItem(settings.getTypeOfInput());
        JLabel lTypeOfInput = new JLabel("Eingabemethode:");
        typeOfInputPanel.add(lTypeOfInput);
        typeOfInputPanel.add(ddTypeOfInput);

        // Supportive Sahnie
        JCheckBox cbSupportSahnie = new JCheckBox("Aktiviere Unterstüzungs-Sahnie", settings.isSupportiveSahnieEnabled());

        // Save button
        JButton bSave = new JButton("Speichern");
        bSave.addActionListener(_ -> {
            try {
                settings.setTimeLimit(Integer.parseInt(tfTimeLimit.getText()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    bSave,
                    "Eingabe des Zeitlimits ist ungültig.\nMuss eine Ganzzahl sein!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if(settings.getTimeLimit() > 1000) {
                JOptionPane.showMessageDialog(
                    bSave,
                    "Eingabe des Zeitlimits ist ungültig.\nDas Limit darf nicht höher als 1000 sein!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            settings.setUnlimitedTime(cbUnlimitedTime.isSelected());
            try {
                settings.setLiveCount(Integer.parseInt(tfLives.getText()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    bSave,
                    "Eingabe der Lebensanzahl ist ungültig.\nMuss eine Ganzzahl sein!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if(settings.getLiveCount() > 10) {
                settings.setLiveCount(3); 
                JOptionPane.showMessageDialog(
                    bSave,
                    "Eingabe der Lebensanzahl ist ungültig.\nDie Leben dürfen maximal 10 sein!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            settings.setUnlimitedLives(cbUnlimitedLives.isSelected());

            settings.setShowIcons(cbShowIcons.isSelected());
            settings.setFarinLibrary(cbFarin.isSelected());
            settings.setBelaLibrary(cbBela.isSelected());
            settings.setSahnieLibrary(cbSahnie.isSelected());
            settings.setSupportiveSahnie(cbSupportSahnie.isSelected());
            settings.setTypeOfInput(ddTypeOfInput.getSelectedItem().toString());

            saveSettings(settings);
            settingsFrame.dispose();
        });

        // Add all to frame
        settingsFrame.add(panTimeLimit);
        settingsFrame.add(typeOfInputPanel);
        settingsFrame.add(cbUnlimitedTime);
        settingsFrame.add(cbFarin);
        settingsFrame.add(panLives);
        settingsFrame.add(cbBela);
        settingsFrame.add(cbUnlimitedLives);
        settingsFrame.add(cbSahnie);
        settingsFrame.add(cbShowIcons);
        settingsFrame.add(cbSupportSahnie);
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

//TODO:
// - Being able to customize album pool
