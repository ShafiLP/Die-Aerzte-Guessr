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
        settings = Settings.read();
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
            new HowToPlayFrame(this, "html\\howToPlayLueckenfueller.html");
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
        settingsFrame = new JFrame() {
            @Override
            public void dispose() {
                super.dispose();
                LueckenfuellerMenu.this.setEnabled(true);
                LueckenfuellerMenu.this.requestFocus();
            }
        };
        settingsFrame.setTitle("Einstellungen");
        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingsFrame.setSize(500, 320);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setLayout(new GridLayout(4, 2));
        this.setEnabled(false);

        // Timer settings
        JLabel lTimeLimit = new JLabel("Zeitlimit (in Sekunden):");
        JTextField tfTimeLimit = new JTextField(settings.getCtlTimeLimit() + "");
        tfTimeLimit.setEnabled(!settings.isCtlUnlimitedTimeEnabled());
        JPanel panTimeLimit = new JPanel(new GridLayout(2, 1));
        panTimeLimit.add(lTimeLimit);  panTimeLimit.add(tfTimeLimit);
        JCheckBox cbUnlimitedTime = new JCheckBox("Ohne Zeitlimit", settings.isCtlUnlimitedTimeEnabled());
        cbUnlimitedTime.addActionListener(_ -> {
            tfTimeLimit.setEnabled(!cbUnlimitedTime.isSelected());
        });

        // Health bar settings
        JPanel panLives = new JPanel(new GridLayout(2, 1));
        panLives.add(new JLabel("Anzahl Leben:"));
        JSlider slLives = new JSlider(1, 10, settings.getCtlLiveCount());
        slLives.setPaintLabels(true);
        slLives.setLabelTable(slLives.createStandardLabels(1));
        slLives.setSnapToTicks(true);
        slLives.setEnabled(!settings.isCtlUnlimitedLivesEnabled());
        panLives.add(slLives);
        JCheckBox cbUnlimitedLives = new JCheckBox("Ohne Leben", settings.isCtlUnlimitedLivesEnabled());
        cbUnlimitedLives.addActionListener(_ -> {
            slLives.setEnabled(!cbUnlimitedLives.isSelected());
        });

        // Number or hints settings
        JPanel panHints = new JPanel(new GridLayout(2, 1));
        panHints.add(new JLabel("Anzahl der Hinweise"));
        JSlider slHints = new JSlider(0, 10, settings.getCtlHintCount());
        slHints.setPaintLabels(true);
        slHints.setLabelTable(slHints.createStandardLabels(1));
        slHints.setSnapToTicks(true);
        panHints.add(slHints);

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

            settings.setCtlUnlimitedLives(cbUnlimitedLives.isSelected());
            settings.setCtlLiveCount(slLives.getValue());
            settings.setCtlUnlimitedTime(cbUnlimitedTime.isSelected());
            settings.setCtlHintCount(slHints.getValue());
            settings.setCtlHardmode(cbHardmode.isSelected());

            Settings.write(settings);
            settingsFrame.dispose();
        });

        // Add all to frame
        settingsFrame.add(panTimeLimit);
        settingsFrame.add(panLives);
        settingsFrame.add(cbUnlimitedTime);
        settingsFrame.add(cbUnlimitedLives);
        settingsFrame.add(panHints);
        settingsFrame.add(cbHardmode);
        settingsFrame.add(bResetHighscore);
        settingsFrame.add(bSave);

        // Set frame visible
        settingsFrame.setVisible(true);
    }
}
