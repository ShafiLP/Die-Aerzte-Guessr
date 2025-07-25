import javax.swing.*;
import java.awt.*;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GTOPreGui extends JFrame {
    // Settings for the settings frame
    private Settings settings;

    public GTOPreGui() {
        settings = readJson("data\\settings.json");

        setTitle("ÄrzteGuessr");
        pack();
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // JLabel for the title
        JLabel titleLabel = new JLabel("ERRATE DIE HERKUNFT", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Folio Extra BT", Font.BOLD, 20));
        titleLabel.setForeground(Color.RED);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(titleLabel, gbc);

        // JButton for single player
        JButton playButton = new JButton("Spielen");
        playButton.addActionListener(_ -> {
            dispose(); // Close the current gui
            new GTOGame(settings); // Start the game
        });
        gbc.gridy = 1;
        gbc.weightx = 0.9;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel.add(playButton, gbc);

        // JButton for settings
        JButton settingsButton = new JButton("Einstellungen");
        settingsButton.addActionListener(_ -> {
            openSettings();
        });
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel.add(settingsButton, gbc);

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void openSettings() {
        JFrame settingsFrame = new JFrame();
        settingsFrame.pack();
        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingsFrame.setSize(500, 500);
        settingsFrame.setResizable(false);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setLayout(new GridLayout(5, 2));

        // Timer settings
        JLabel lTimeLimit = new JLabel("Zeitlimit (in Sekunden):");
        JTextField tfTimeLimit = new JTextField(settings.getTimeLimit() + "");
        JPanel panTimeLimit = new JPanel(new GridLayout(2, 1));
        panTimeLimit.add(lTimeLimit);  panTimeLimit.add(tfTimeLimit);
        JCheckBox cbUnlimitedTime = new JCheckBox("Ohne Zeitlimit", settings.isUnlimitedTimeEnabled());

        // Health bar settings
        JLabel lLives = new JLabel("Anzahl Leben:");
        JTextField tfLives = new JTextField(settings.getLiveCount() + "");
        JPanel panLives = new JPanel(new GridLayout(2, 1));
        panLives.add(lLives); panLives.add(tfLives);
        JCheckBox cbUnlimitedLives = new JCheckBox("Ohne Leben", settings.isUnlimitedLivesEnabled());

        // Icon settings
        JCheckBox cbShowIcons = new JCheckBox("Icons anzeigen", settings.isShowIconsEnabled());

        // Bonus library settings
        JCheckBox cbFarin = new JCheckBox("Füge Farins Diskografie hinzu", settings.isFarinEnabled());
        JCheckBox cbBela = new JCheckBox("Füge Belas Diskografie hinzu", settings.isBelaEnabled());
        JCheckBox cbSahnie = new JCheckBox("Füge Sahnies Diskografie hinzu", settings.isSahnieEnabled());

        // Sahnie's Collective Wisdom
        JButton bSahniesWisdom = new JButton("Öffne \"Sahnie's Collective Wisdom\"");
        bSahniesWisdom.addActionListener(_ -> {
            JFrame sahniesWisdom = new JFrame();
            sahniesWisdom.pack();
            sahniesWisdom.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            sahniesWisdom.setSize(500, 500);
            sahniesWisdom.setResizable(false);
            sahniesWisdom.setLocationRelativeTo(null);
            sahniesWisdom.setLayout(new GridLayout(1, 1));

            JLabel lSahnie = new JLabel(new ImageIcon("images\\sahne.png"));
            sahniesWisdom.add(lSahnie);

            sahniesWisdom.setVisible(true);
        });

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

            saveSettings(settings);
            settingsFrame.dispose();
        });

        // Add all to frame
        settingsFrame.add(panTimeLimit);
        settingsFrame.add(cbFarin);
        settingsFrame.add(cbUnlimitedTime);
        settingsFrame.add(cbBela);
        settingsFrame.add(panLives);
        settingsFrame.add(cbSahnie);
        settingsFrame.add(cbUnlimitedLives);
        settingsFrame.add(bSahniesWisdom);
        settingsFrame.add(cbShowIcons);
        settingsFrame.add(bSave);

        // Set frame visible
        settingsFrame.setVisible(true);
    }

    private Settings readJson(String filepath) {
        Settings settingsFromJson = new Settings();
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filepath)) {
            settingsFromJson = gson.fromJson(reader, Settings.class);
        } catch (IOException e) {
            e.printStackTrace();;
        }
        return settingsFromJson;
    }

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