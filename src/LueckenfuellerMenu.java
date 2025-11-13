import javax.swing.*;
import javax.swing.border.LineBorder;

import com.formdev.flatlaf.FlatLaf;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.Collections;

public class LueckenfuellerMenu extends Menu {
    private JFrame settingsFrame;

    /**
     * Crates a GUI for starting the game and configuring settings
     */
    public LueckenfuellerMenu() {
        // Read settings
        settings = readSettings("data\\settings.json");
        Color backgroundColor = Color.WHITE;

        if(settings.isColourfulGuiEnabled()) {
            backgroundColor = new Color(220, 220, 255);
            if(settings.isDarkMode())
            backgroundColor = new Color(40, 40, 90);
        }

        // Set accent colours
        FlatLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor", "#5959ffff"));
        setup();

        this.setTitle("Lückenfüller");
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
        ImageIcon icon = new ImageIcon("images\\Lueckenfueller.png");
        Image img = icon.getImage().getScaledInstance(270, 35, Image.SCALE_SMOOTH);
        JLabel lHeading = new JLabel(new ImageIcon(img));
        headingPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        headingPanel.add(lHeading);
        headingPanel.setBackground(backgroundColor);
        this.add(headingPanel, BorderLayout.NORTH);

        // Button panel for start, settings, etc.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);;

        // Buttons
        buttons[0] = new JButton("Spielen");
        buttons[0].addActionListener(_ -> {
            if(fHtp != null && fHtp.isVisible()) {
                fHtp.dispose();
            }
            if(settingsFrame != null && settingsFrame.isVisible()) {
                settingsFrame.dispose();
            }
            this.dispose();
            new LueckenfuellerGame(settings);
        });
        buttons[1] = new JButton("Einstellungen");
        buttons[1].addActionListener(_ -> {
            openSettings();
        });
        buttons[2] = new JButton("Wie man spielt");
        buttons[2].addActionListener(_ -> {
            openHowToPlay("html\\howToPlayLueckenfueller.html");
        });
        buttons[3] = new JButton("Hauptmenü");
        buttons[3].addActionListener(_ -> {
            if(fHtp != null && fHtp.isVisible()) {
                fHtp.dispose();
            }
            if(settingsFrame != null && settingsFrame.isVisible()) {
                settingsFrame.dispose();
            }
            this.dispose();
            new MainMenu();
        });

        buttons[0].setBorder(new LineBorder(new Color(100, 100, 150), 2, true));
        buttons[0].setBackground(Color.WHITE);
        buttons[0].setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));

        buttons[1].setBorder(new LineBorder(new Color(100, 100, 150), 2, true));
        buttons[1].setBackground(Color.WHITE);
        buttons[1].setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));

        buttons[2].setBorder(new LineBorder(new Color(100, 100, 150), 2, true));
        buttons[2].setBackground(Color.WHITE);
        buttons[2].setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));

        buttons[3].setBorder(new LineBorder(new Color(100, 100, 150), 2, true));
        buttons[3].setBackground(Color.WHITE);
        buttons[3].setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));

        Dimension buttonSize = new Dimension(200, 40);
        buttons[0].setMaximumSize(buttonSize);
        buttons[1].setMaximumSize(buttonSize);
        buttons[2].setMaximumSize(buttonSize);
        buttons[3].setMaximumSize(buttonSize);

        buttons[0].setAlignmentX(Component.CENTER_ALIGNMENT);
        buttons[1].setAlignmentX(Component.CENTER_ALIGNMENT);
        buttons[2].setAlignmentX(CENTER_ALIGNMENT);
        buttons[3].setAlignmentX(Component.CENTER_ALIGNMENT);

        // Make buttons dark if darkmode is enabled
        if(settings.isDarkMode())
        darkmodeButtons();

        // Highscore
        JPanel highscorePanel = new JPanel();
        JLabel lHighscore = new JLabel("Highscore: " + settings.getCtlHighscore());
        highscorePanel.setBorder(BorderFactory.createEmptyBorder(3, 15, 3, 15));
        highscorePanel.add(lHighscore);
        highscorePanel.setBackground(backgroundColor);
        this.add(highscorePanel, BorderLayout.SOUTH);

        // Add with vertical padding
        buttonPanel.add(buttons[0]);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(buttons[1]);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(buttons[2]);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(buttons[3]);

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
        settingsFrame.setTitle("Einstellungen");
        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingsFrame.setSize(500, 200);
        settingsFrame.setResizable(false);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setLayout(new GridLayout(4, 2));

        // Timer settings
        JLabel lTimeLimit = new JLabel("Zeitlimit (in Sekunden):");
        JTextField tfTimeLimit = new JTextField(settings.getCtlTimeLimit() + "");
        tfTimeLimit.setEnabled(!settings.isCtlUnlimitedLivesEnabled());
        JPanel panTimeLimit = new JPanel(new GridLayout(2, 1));
        panTimeLimit.add(lTimeLimit);  panTimeLimit.add(tfTimeLimit);
        JCheckBox cbUnlimitedTime = new JCheckBox("Ohne Zeitlimit", settings.isCtlUnlimitedTimeEnabled());
        cbUnlimitedTime.addActionListener(_ -> {
            tfTimeLimit.setEnabled(!cbUnlimitedTime.isSelected());
        });

        // Health bar settings
        JLabel lLives = new JLabel("Anzahl Leben:");
        JTextField tfLives = new JTextField(settings.getCtlLiveCount() + "");
        tfLives.setEnabled(!settings.isCtlUnlimitedLivesEnabled());
        JPanel panLives = new JPanel(new GridLayout(2, 1));
        panLives.add(lLives); panLives.add(tfLives);
        JCheckBox cbUnlimitedLives = new JCheckBox("Ohne Leben", settings.isCtlUnlimitedLivesEnabled());
        cbUnlimitedLives.addActionListener(_ -> {
            tfLives.setEnabled(!cbUnlimitedLives.isSelected());
        });

        // Number or hints settings
        JPanel hintPanel = new JPanel(new GridLayout(2, 1));
        JLabel lHints = new JLabel("Anzahl der Hinweise");
        JTextField tfHints = new JTextField(settings.getCtlHintCount() + "");
        hintPanel.add(lHints);
        hintPanel.add(tfHints);

        // Hardmode settings
        JCheckBox cbHardmode = new JCheckBox("Schwieriger Modus", settings.isCtlHardmodeEnabled());

        // Highscore reset button
        JButton bResetHighscore = new JButton("Highscore zurücksetzen");
        bResetHighscore.addActionListener(_ -> {
            settings.setCtlHighscore(0);
        });

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

            settings.setCtlUnlimitedTime(cbUnlimitedTime.isSelected());
            settings.setCtlUnlimitedLives(cbUnlimitedLives.isSelected());
            settings.setCtlHintCount(Integer.parseInt(tfHints.getText()));
            settings.setCtlHardmode(cbHardmode.isSelected());

            saveSettings(settings);
            settingsFrame.dispose();
        });

        // Add all to frame
        settingsFrame.add(panTimeLimit);
        settingsFrame.add(panLives);
        settingsFrame.add(cbUnlimitedTime);
        settingsFrame.add(cbUnlimitedLives);
        settingsFrame.add(hintPanel);
        settingsFrame.add(cbHardmode);
        settingsFrame.add(bResetHighscore);
        settingsFrame.add(bSave);

        // Set frame visible
        settingsFrame.setVisible(true);
    }
}
