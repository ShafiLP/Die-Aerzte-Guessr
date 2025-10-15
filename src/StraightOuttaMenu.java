import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

public class StraightOuttaMenu extends JFrame {
    private Settings settings;
    private JFrame fHtp;
    private JFrame settingsFrame;

    /**
     * Crates a GUI for starting the game and configuring settings
     */
    public StraightOuttaMenu() {
        // Read settings
        settings = readSettings("data\\settings.json");
        Color backgroundColor = Color.WHITE;

        if(settings.isColourfulGuiEnabled())
        backgroundColor = new Color(255, 220, 220);
        

        this.setTitle("Straight Outta...");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(310, 350);
        this.setResizable(false);
        this.setLocationRelativeTo(null); // Center the window
        this.setIconImage(new ImageIcon("images\\daLogo.png").getImage());


        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 30, 40)); // Padding
        mainPanel.setBackground(backgroundColor);

        // Heading
        JPanel headingPanel = new JPanel();
        ImageIcon icon = new ImageIcon("images\\StraightOutta.png");
        Image img = icon.getImage().getScaledInstance(270, 35, Image.SCALE_SMOOTH);
        JLabel lHeading = new JLabel(new ImageIcon(img));
        headingPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        headingPanel.add(lHeading);
        headingPanel.setBackground(backgroundColor);
        this.add(headingPanel, BorderLayout.NORTH);

        // Button panel for start, settings, etc.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBackground(backgroundColor);

        // Buttons
        JButton bPlay = new JButton("Spielen");
        bPlay.addActionListener(_ -> {
            if(fHtp != null && fHtp.isVisible()) {
                fHtp.dispose();
            }
            if(settingsFrame != null && settingsFrame.isVisible()) {
                settingsFrame.dispose();
            }
            this.dispose();
            new StraightOuttaGame(settings);
        });
        JButton bSettings = new JButton("Einstellungen");
        bSettings.addActionListener(_ -> {
            openSettings();
        });
        JButton bHowToPlay = new JButton("Wie man spielt");
        bHowToPlay.addActionListener(_ -> {
            openHowToPlay();
        });
        JButton bBack = new JButton("Hauptmenü");
        bBack.addActionListener(_ -> {
            if(fHtp != null && fHtp.isVisible()) {
                fHtp.dispose();
            }
            if(settingsFrame != null && settingsFrame.isVisible()) {
                settingsFrame.dispose();
            }
            this.dispose();
            new MainMenu();
        });

        bPlay.setBorder(new LineBorder(new Color(150, 100, 100), 2, true));
        bPlay.setBackground(Color.WHITE);
        bPlay.setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));

        bSettings.setBorder(new LineBorder(new Color(150, 100, 100), 2, true));
        bSettings.setBackground(Color.WHITE);
        bSettings.setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));

        bHowToPlay.setBorder(new LineBorder(new Color(150, 100, 100), 2, true));
        bHowToPlay.setBackground(Color.WHITE);
        bHowToPlay.setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));

        bBack.setBorder(new LineBorder(new Color(150, 100, 100), 2, true));
        bBack.setBackground(Color.WHITE);
        bBack.setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));

        Dimension buttonSize = new Dimension(200, 40);
        bPlay.setMaximumSize(buttonSize);
        bSettings.setMaximumSize(buttonSize);
        bHowToPlay.setMaximumSize(buttonSize);
        bBack.setMaximumSize(buttonSize);

        bPlay.setAlignmentX(Component.CENTER_ALIGNMENT);
        bSettings.setAlignmentX(Component.CENTER_ALIGNMENT);
        bHowToPlay.setAlignmentX(CENTER_ALIGNMENT);
        bBack.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Highscore
        JPanel highscorePanel = new JPanel();
        JLabel lHighscore = new JLabel("Highscore: " + settings.getGtoHighscore());
        highscorePanel.setBorder(BorderFactory.createEmptyBorder(3, 15, 3, 15));
        highscorePanel.add(lHighscore);
        highscorePanel.setBackground(backgroundColor);
        this.add(highscorePanel, BorderLayout.SOUTH);

        // Add with vertical padding
        buttonPanel.add(bPlay);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(bSettings);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(bHowToPlay);
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
        settingsFrame = new JFrame();
        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingsFrame.setSize(400, 200);
        settingsFrame.setResizable(false);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setLayout(new GridLayout(4, 2));

        // Timer settings
        JLabel lTimeLimit = new JLabel("Zeitlimit (in Sekunden):");
        JTextField tfTimeLimit = new JTextField(settings.getGtoTimeLimit() + "");
        tfTimeLimit.setEnabled(!settings.isGtoUnlimitedTimeEnabled());
        JPanel panTimeLimit = new JPanel(new GridLayout(2, 1));
        panTimeLimit.add(lTimeLimit);  panTimeLimit.add(tfTimeLimit);
        JCheckBox cbUnlimitedTime = new JCheckBox("Ohne Zeitlimit", settings.isGtoUnlimitedTimeEnabled());
        cbUnlimitedTime.addActionListener(_ -> {
            tfTimeLimit.setEnabled(!cbUnlimitedTime.isSelected());
        });

        // Health bar settings
        JLabel lLives = new JLabel("Anzahl Leben:");
        JTextField tfLives = new JTextField(settings.getGtoLiveCount() + "");
        tfLives.setEnabled(!settings.isGtoUnlimitedLivesEnabled());
        JPanel panLives = new JPanel(new GridLayout(2, 1));
        panLives.add(lLives); panLives.add(tfLives);
        JCheckBox cbUnlimitedLives = new JCheckBox("Ohne Leben", settings.isGtoUnlimitedLivesEnabled());
        cbUnlimitedLives.addActionListener(_ -> {
            tfLives.setEnabled(!cbUnlimitedLives.isSelected());
        });

        // Type of input settings
        JPanel typeOfInputPanel = new JPanel(new GridLayout(2, 1));
        JComboBox<String> ddTypeOfInput = new JComboBox<>();
        ddTypeOfInput.addItem("Suchleiste");
        ddTypeOfInput.addItem("Dropdown Menü");
        ddTypeOfInput.setSelectedItem(settings.getGtoTypeOfInput());
        JLabel lTypeOfInput = new JLabel("Eingabemethode:");
        typeOfInputPanel.add(lTypeOfInput);
        typeOfInputPanel.add(ddTypeOfInput);

        // Number or hints settings
        JPanel hintPanel = new JPanel(new GridLayout(2, 1));
        JLabel lHints = new JLabel("Anzahl der Hinweise");
        JTextField tfHints = new JTextField(settings.getGtoHintCount() + "");
        hintPanel.add(lHints);
        hintPanel.add(tfHints);

        // Highscore reset button
        JButton bResetHighscore = new JButton("Highscore zurücksetzen");
        bResetHighscore.addActionListener(_ -> {
            settings.setGtoHighscore(0);
        });

        // Save button
        JButton bSave = new JButton("Speichern");
        bSave.addActionListener(_ -> {
            // Check if time limit input is valid
            try {
                int timeLimit = Integer.parseInt(tfLives.getText());
                if(timeLimit > 1000) {
                    JOptionPane.showMessageDialog(
                        bSave,
                        "Eingabe des Zeitlimits ist ungültig.\nDas Limit darf nicht höher als 1000 sein!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    bSave,
                    "Eingabe des Zeitlimits ist ungültig.\nMuss eine Ganzzahl sein!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Check if live count input is valid
            try {
                int liveCount = Integer.parseInt(tfLives.getText());
                if(liveCount > 10) {
                    JOptionPane.showMessageDialog(
                        bSave,
                        "Eingabe der Lebensanzahl ist ungültig.\nDie Leben dürfen maximal 10 sein!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    bSave,
                    "Eingabe der Lebensanzahl ist ungültig.\nMuss eine Ganzzahl sein!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            if(settings.getGtoLiveCount() > 10) {
                settings.setGtoLiveCount(3); 
                
            }

            // Check if hint input is valid
            try {
                int hints = Integer.parseInt(tfHints.getText());
                if(hints > 1000) {
                    JOptionPane.showMessageDialog(
                        bSave,
                        "Eingabe der Hinweisanzahl ungültig.\nDarf nicht höher als 1000 sein!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    bSave,
                    "Eingabe der Hinweisanzahl ungültig.\nMuss eine Ganzzahl sein!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }

            // Save settings
            settings.setGtoTimeLimit(Integer.parseInt(tfTimeLimit.getText()));
            settings.setGtoLiveCount(Integer.parseInt(tfLives.getText()));
            settings.setGtoHintCount(Integer.parseInt(tfHints.getText()));
            settings.setGtoUnlimitedTime(cbUnlimitedTime.isSelected());
            settings.setGtoUnlimitedLives(cbUnlimitedLives.isSelected());
            settings.setGtoTypeOfInput(ddTypeOfInput.getSelectedItem().toString());
            saveSettings(settings);

            // Close frame
            settingsFrame.dispose();
        });

        // Add all to frame
        settingsFrame.add(panTimeLimit);
        settingsFrame.add(panLives);
        settingsFrame.add(cbUnlimitedTime);
        settingsFrame.add(cbUnlimitedLives);
        settingsFrame.add(typeOfInputPanel);
        settingsFrame.add(hintPanel);
        settingsFrame.add(bResetHighscore);
        settingsFrame.add(bSave);

        // Set frame visible
        settingsFrame.setVisible(true);
    }

    /**
     * Opens a HTML file that explains how to play this game
     */
    private void openHowToPlay() {
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
        File htmlFile = new File("html\\howToPlayStraightOutta.html");
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
