import javax.swing.*;
import javax.swing.border.LineBorder;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.util.Collections;

public class LueckenfuellerMenu extends Menu {
    private final Settings settings;

    /**
     * Crates a GUI for starting the game and configuring settings
     */
    public LueckenfuellerMenu(Settings settings) {
        this.settings = settings;

        // Apply background colour
        Color backgroundColor = Color.WHITE;
        if (settings.isColourfulGuiEnabled()) {
            backgroundColor = new Color(220, 220, 255);
            if (settings.isDarkMode()) backgroundColor = new Color(40, 40, 90);
        }

        // Apply accent colours
        FlatLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor", "#5959ffff"));
        if(settings.isDarkMode()){
            FlatDarkLaf.setup();
            com.formdev.flatlaf.FlatDarkLaf.updateUI();
        } else {
            FlatLightLaf.setup();
            com.formdev.flatlaf.FlatLaf.updateUI();
        }

        this.setTitle("Lückenfüller");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(310, 400);
        this.setResizable(false);
        this.setLocationRelativeTo(null); // Center window
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
            this.dispose();
            new LueckenfuellerGame(settings, false, null, null);
        });
        buttons[1] = new JButton("Vs. (BETA)");
        buttons[1].addActionListener(_ -> {
            // Create new JFrame
            JFrame frNames = new JFrame() {
                @Override
                public void dispose() {
                    super.dispose();
                    LueckenfuellerMenu.this.setEnabled(true);
                    LueckenfuellerMenu.this.requestFocus();
                }
            };
            frNames.setTitle("Straight Outta...");
            frNames.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frNames.setSize(300, 150);
            frNames.setResizable(false);
            frNames.setLocationRelativeTo(null);
            frNames.setLayout(new BorderLayout());
            LueckenfuellerMenu.this.setEnabled(false);

            JPanel panCenter = new JPanel(new GridBagLayout());
            panCenter.add(new JLabel("Namen eingeben!", SwingConstants.CENTER) {{
                setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));
            }}, new GridBagConstraints() {{
                gridx = 0;
                gridy = 0;
                gridwidth = 2;
                insets = new Insets(5, 5, 5, 5);
                anchor = GridBagConstraints.SOUTH;
                fill = GridBagConstraints.HORIZONTAL;
            }});
            panCenter.add(new JLabel("Spieler 1: ") {{
                setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));
            }}, new GridBagConstraints() {{
                gridx = 0;
                gridy = 1;
                insets = new Insets(0, 5, 0, 0);
                anchor = GridBagConstraints.WEST;
                fill = GridBagConstraints.NONE;
            }});
            JTextField tfPlayer1 =  new JTextField();
            panCenter.add(tfPlayer1, new GridBagConstraints() {{
                gridx = 1;
                gridy = 1;
                insets = new Insets(0, 0, 0, 5);
                anchor = GridBagConstraints.CENTER;
                fill = GridBagConstraints.HORIZONTAL;
            }});
            panCenter.add(new JLabel("Spieler 2: ") {{
                setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));
            }}, new GridBagConstraints() {{
                gridx = 0;
                gridy = 2;
                insets = new Insets(0, 5, 0, 0);
                anchor = GridBagConstraints.WEST;
                fill = GridBagConstraints.NONE;
            }});
            JTextField tfPlayer2 =  new JTextField();
            panCenter.add(tfPlayer2, new GridBagConstraints() {{
                gridx = 1;
                gridy = 2;
                insets = new Insets(0, 0, 0, 5);
                anchor = GridBagConstraints.CENTER;
                fill = GridBagConstraints.HORIZONTAL;
            }});
            frNames.add(panCenter, BorderLayout.CENTER);

            // Confirm & Return buttons
            JPanel panButtons = new JPanel();
            panButtons.setLayout(new GridBagLayout());

            JButton bReturn = new JButton("X");
            bReturn.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
            bReturn.addActionListener(e -> {
                frNames.dispose();
            });
            panButtons.add(bReturn, new GridBagConstraints() {{
                gridy = 0;
                gridx = 0;
                weightx = 0.1;
                insets = new Insets(0, 0, 5, 5);
                anchor = GridBagConstraints.EAST;
                fill = GridBagConstraints.NONE;
            }});
            JButton bConfirm = new JButton("✓");
            bConfirm.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_ROUND_RECT);
            bConfirm.addActionListener(_ -> {
                frNames.dispose();
                LueckenfuellerMenu.this.dispose();
                new LueckenfuellerGame(settings, true, tfPlayer1.getText(), tfPlayer2.getText());
            });
            panButtons.add(bConfirm, new GridBagConstraints() {{
                gridy = 0;
                gridx = 1;
                weightx = 0.1;
                insets = new Insets(0, 5, 5, 0);
                anchor = GridBagConstraints.WEST;
                fill = GridBagConstraints.NONE;
            }});
            frNames.add(panButtons, BorderLayout.SOUTH);

            frNames.setVisible(true);
        });
        buttons[2] = new JButton("Einstellungen");
        buttons[2].addActionListener(_ -> {
            openSettings();
        });
        buttons[3] = new JButton("Wie man spielt");
        buttons[3].addActionListener(_ -> {
            new HowToPlayFrame(this, "html\\howToPlayLueckenfueller.html");
        });
        buttons[4] = new JButton("Hauptmenü");
        buttons[4].addActionListener(_ -> {
            this.dispose();
            new MainMenu();
        });

        // Add with vertical padding
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBorder(new LineBorder(new Color(100, 100, 150), 2, true));
            buttons[i].setBackground(Color.WHITE);
            buttons[i].setFont(settings.getFont());
            buttons[i].setMaximumSize(new Dimension(200, 60));
            buttons[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            buttonPanel.add(buttons[i]);
            if (i != buttons.length - 1) buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        // Make buttons dark if darkmode is enabled
        if (settings.isDarkMode()) darkmodeButtons();

        // Highscore
        JPanel highscorePanel = new JPanel();
        JLabel lHighscore = new JLabel("Highscore: " + settings.getCtlHighscore());
        highscorePanel.setBorder(BorderFactory.createEmptyBorder(3, 15, 3, 15));
        highscorePanel.add(lHighscore);
        highscorePanel.setBackground(backgroundColor);
        this.add(highscorePanel, BorderLayout.SOUTH);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        this.add(mainPanel);
        this.setVisible(true);
    }

    /**
     * Opens the settings menu
     * Called when user clicks on settings button
     */
    private void openSettings() {
        // Frame settings
        JFrame settingsFrame = new JFrame() {
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
        JPanel panTimeLimit = new JPanel(new GridLayout(2, 1));
        panTimeLimit.add(new JLabel("Zeitlimit (in Sekunden):") {{
            setFont(settings.getFont());
        }});
        JTextField tfTimeLimit = new JTextField(settings.getCtlTimeLimit() + "");
        tfTimeLimit.setFont(settings.getFont());
        tfTimeLimit.setEnabled(!settings.isCtlUnlimitedTimeEnabled());
        panTimeLimit.add(tfTimeLimit);
        JCheckBox cbUnlimitedTime = new JCheckBox("Ohne Zeitlimit", settings.isCtlUnlimitedTimeEnabled());
        cbUnlimitedTime.setFont(settings.getFont());
        cbUnlimitedTime.addActionListener(_ -> {
            tfTimeLimit.setEnabled(!cbUnlimitedTime.isSelected());
        });

        // Health bar settings
        JPanel panLives = new JPanel(new GridLayout(2, 1));
        panLives.add(new JLabel("Anzahl der Leben:") {{
            setFont(settings.getFont());
        }});
        JSlider slLives = new JSlider(1, 10, settings.getCtlLiveCount());
        slLives.setPaintLabels(true);
        slLives.setLabelTable(slLives.createStandardLabels(1));
        slLives.setSnapToTicks(true);
        slLives.setEnabled(!settings.isCtlUnlimitedLivesEnabled());
        slLives.setFont(settings.getFont());
        panLives.add(slLives);
        JCheckBox cbUnlimitedLives = new JCheckBox("Ohne Leben", settings.isCtlUnlimitedLivesEnabled());
        cbUnlimitedLives.setFont(settings.getFont());
        cbUnlimitedLives.addActionListener(_ -> {
            slLives.setEnabled(!cbUnlimitedLives.isSelected());
        });

        // Number or hints settings
        JPanel panHints = new JPanel(new GridLayout(2, 1));
        panHints.add(new JLabel("Anzahl der Hinweise") {{
            setFont(settings.getFont());
        }});
        JSlider slHints = new JSlider(0, 10, settings.getCtlHintCount());
        slHints.setPaintLabels(true);
        slHints.setLabelTable(slHints.createStandardLabels(1));
        slHints.setSnapToTicks(true);
        slHints.setFont(settings.getFont());
        panHints.add(slHints);

        // Hardmode settings
        JCheckBox cbHardmode = new JCheckBox("Schwieriger Modus", settings.isCtlHardmodeEnabled());
        cbHardmode.setFont(settings.getFont());

        // Highscore reset button
        JButton bResetHighscore = new JButton("Highscore zurücksetzen");
        bResetHighscore.setFont(settings.getFont());
        bResetHighscore.addActionListener(_ -> {
            settings.setCtlHighscore(0);
        });

        // Save button
        JButton bSave = new JButton("Speichern");
        bSave.setFont(settings.getFont());
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
