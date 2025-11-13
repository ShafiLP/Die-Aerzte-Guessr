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

public class AerztleMenu extends Menu {
    private JFrame settingsFrame;

    /**
     * Crates a GUI for starting the game and configuring settings
     */
    public AerztleMenu() {
        // Read settings
        settings = readSettings("data\\settings.json");
        Color backgroundColor = Color.WHITE;

        if(settings.isColourfulGuiEnabled()) {
            backgroundColor = new Color(220, 255, 220);
            if(settings.isDarkMode())
            backgroundColor = new Color(40, 90, 40);
        }

        // Set accent colours
        FlatLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor", "#68ff68ff"));
        setup();

        this.setTitle("Ärztle");
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
        ImageIcon icon = new ImageIcon("images\\aerztle.png");
        Image img = icon.getImage().getScaledInstance(150, 35, Image.SCALE_SMOOTH);
        JLabel lHeading = new JLabel(new ImageIcon(img));
        headingPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        headingPanel.add(lHeading);
        headingPanel.setBackground(backgroundColor);
        this.add(headingPanel, BorderLayout.NORTH);

        // Button panel for start, settings, etc.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

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
            new AerztleGame(settings);
        });
        buttons[1] = new JButton("Einstellungen");
        buttons[1].addActionListener(_ -> {
            openSettings();
        });
        buttons[2] = new JButton("Wie man spielt");
        buttons[2].addActionListener(_ -> {
            openHowToPlay("html\\howToPlayAerztle.html");
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

        buttons[0].setBorder(new LineBorder(new Color(100, 150, 100), 2, true));
        buttons[0].setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));
        if(settings.isDarkMode()) {
            buttons[0].setBackground(Color.BLACK);
        } else {
            buttons[0].setBackground(Color.WHITE);
        }

        buttons[1].setBorder(new LineBorder(new Color(100, 150, 100), 2, true));
        buttons[1].setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));
        if(settings.isDarkMode()) {
            buttons[1].setBackground(Color.BLACK);
        } else {
            buttons[1].setBackground(Color.WHITE);
        }

        buttons[2].setBorder(new LineBorder(new Color(100, 150, 100), 2, true));
        buttons[2].setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));
        if(settings.isDarkMode()) {
            buttons[2].setBackground(Color.BLACK);
        } else {
            buttons[2].setBackground(Color.WHITE);
        }

        buttons[3].setBorder(new LineBorder(new Color(100, 150, 100), 2, true));
        buttons[3].setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));
        if(settings.isDarkMode()) {
            buttons[3].setBackground(Color.BLACK);
        } else {
            buttons[3].setBackground(Color.WHITE);
        }

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
        settingsFrame.setSize(500, 150);
        settingsFrame.setResizable(false);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setLayout(new GridLayout(2, 2)); 

        // Tries settings
        JLabel lTries = new JLabel("Versuche:");
        JTextField tfTries = new JTextField(settings.getAeTries() + "");
        JPanel panTries = new JPanel(new GridLayout(2, 1));
        panTries.add(lTries);  panTries.add(tfTries);

        // Type of input settings
        JPanel typeOfInputPanel = new JPanel(new GridLayout(2, 1));
        JComboBox<String> ddTypeOfInput = new JComboBox<>();
        ddTypeOfInput.addItem("Suchleiste");
        ddTypeOfInput.addItem("Dropdown Menü");
        ddTypeOfInput.setSelectedItem(settings.getAeTypeOfInput());
        JLabel lTypeOfInput = new JLabel("Eingabemethode:");
        typeOfInputPanel.add(lTypeOfInput);
        typeOfInputPanel.add(ddTypeOfInput);

        // Save button
        JButton bSave = new JButton("Speichern");
        bSave.addActionListener(_ -> {
            // Check if tries input is valid
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

            settings.setAeTypeOfInput(ddTypeOfInput.getSelectedItem().toString());

            saveSettings(settings);
            settingsFrame.dispose();
        });

        // Add all to frame
        settingsFrame.add(panTries);
        settingsFrame.add(typeOfInputPanel);
        settingsFrame.add(new JLabel());
        settingsFrame.add(bSave);

        // Set frame visible
        settingsFrame.setVisible(true);
    }
}
