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

public class AerztleMenu extends JFrame {
    // Settings for the settings frame
    private Settings settings;

    /**
     * Crates a GUI for starting the game and configuring settings
     */
    public AerztleMenu() {
        settings = readSettings("data\\settings.json");

        this.setTitle("Ärztle");
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
        ImageIcon icon = new ImageIcon("images\\aerztle.png");
        Image img = icon.getImage().getScaledInstance(150, 35, Image.SCALE_SMOOTH);
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
            new AerztleGame(settings);
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
        settingsFrame.setSize(500, 250);
        settingsFrame.setResizable(false);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setLayout(new GridLayout(4, 2)); 

        // Tries settings
        JLabel lTries = new JLabel("Versuche:");
        JTextField tfTries = new JTextField(settings.getAeTries() + "");
        JPanel panTries = new JPanel(new GridLayout(2, 1));
        panTries.add(lTries);  panTries.add(tfTries);

        // Bonus library settings
        JCheckBox cbFarin = new JCheckBox("Füge Farins Diskografie hinzu", settings.isAeFarinEnabled());
        JCheckBox cbBela = new JCheckBox("Füge Belas Diskografie hinzu", settings.isAeBelaEnabled());
        JCheckBox cbSahnie = new JCheckBox("Füge Sahnies Diskografie hinzu", settings.isAeSahnieEnabled());

        // Font type settings
        JPanel fontTypePanel = new JPanel(new GridLayout(2, 1));
        JComboBox<String> ddFont = new JComboBox<>();
        ddFont.addItem("Folio Extra");
        ddFont.addItem("Arial");
        ddFont.addItem("Comic Sans MS");
        ddFont.setSelectedItem(settings.getFontType());
        JLabel lFont = new JLabel("Schriftart:");
        fontTypePanel.add(lFont);
        fontTypePanel.add(ddFont);

        // Font size settings
        JPanel fontSizePanel = new JPanel(new GridLayout(2, 1));
        JTextField tfFontSize = new JTextField();
        tfFontSize.setText(settings.getFontSize() + "");
        JLabel lFontSize = new JLabel("Schriftgröße:");
        fontSizePanel.add(lFontSize); fontSizePanel.add(tfFontSize);

        // Type of input settings
        JPanel typeOfInputPanel = new JPanel(new GridLayout(2, 1));
        JComboBox<String> ddTypeOfInput = new JComboBox<>();
        ddTypeOfInput.addItem("Suchleiste");
        ddTypeOfInput.addItem("Dropdown Menü");
        ddTypeOfInput.addItem("Manuelle Eingabe");
        ddTypeOfInput.setSelectedItem(settings.getAeTypeOfInput());
        JLabel lTypeOfInput = new JLabel("Eingabemethode:");
        typeOfInputPanel.add(lTypeOfInput);
        typeOfInputPanel.add(ddTypeOfInput);

        // Save button
        JButton bSave = new JButton("Speichern");
        bSave.addActionListener(_ -> {
            try {
                settings.setAeTries(Integer.parseInt(tfTries.getText()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    bSave,
                    "Eingabe der Versuche ist ungültig.\nMuss eine Ganzzahl sein!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if(settings.getAeTries() > 15 || settings.getAeTries() < 5) {
                JOptionPane.showMessageDialog(
                    bSave,
                    "Eingabe der Versuche ist ungültig.\nDas Limit liegt zwischen 5 und 15!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            try {
                settings.setFontSize(Integer.parseInt(tfFontSize.getText().trim()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    bSave,
                    "Eingabe der Schriftgröße ist ungültig.\nMuss eine Ganzzahl sein!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if(settings.getFontSize() > 18 || settings.getFontSize() < 4) {
                settings.setFontSize(12);
                JOptionPane.showMessageDialog(
                    bSave,
                    "Eingabe der Schriftgröße ist ungültig.\nDie Schriftgröße muss zwischen 4 und 18 liegen!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            settings.setAeFarin(cbFarin.isSelected());
            settings.setAeBela(cbBela.isSelected());
            settings.setAeSahnie(cbSahnie.isSelected());
            settings.setFontType(ddFont.getSelectedItem().toString());
            settings.setAeTypeOfInput(ddTypeOfInput.getSelectedItem().toString());

            saveSettings(settings);
            settingsFrame.dispose();
        });

        // Add all to frame
        settingsFrame.add(fontTypePanel);
        settingsFrame.add(cbFarin);
        settingsFrame.add(fontSizePanel);
        settingsFrame.add(cbBela);
        settingsFrame.add(panTries);
        settingsFrame.add(cbSahnie);
        settingsFrame.add(typeOfInputPanel);
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
/*
 * TODO:
 * - Stats
 * - Settings reset button
 */
