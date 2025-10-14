import javax.swing.*;
import javax.swing.border.LineBorder;

import com.google.gson.Gson;

import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class for the game main menu
 * Contains paths to all games
 */
public class MainMenu extends JFrame {
    private final String VERSION = "0.3.6";
    /**
     * Constructor of main menu
     * Contains paths to all games
     */
    public MainMenu() {
        this.setTitle("ÄrzteGuessr");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, /*480*/440);
        this.setResizable(false);
        this.setLocationRelativeTo(null); // Center the window
        this.setIconImage(new ImageIcon("images\\daLogo.png").getImage());

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40)); // Padding

        // Heading
        JPanel headingPanel = new JPanel();
        ImageIcon icon = new ImageIcon("images\\AerzteGuessr.png");
        Image img = icon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);
        JLabel lHeading = new JLabel(new ImageIcon(img));
        headingPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        headingPanel.add(lHeading);
        this.add(headingPanel, BorderLayout.NORTH);

        // Button panel different game modes
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // Buttons
        icon = new ImageIcon("images\\StraightOutta.png");
        img = icon.getImage().getScaledInstance(270, 35, Image.SCALE_SMOOTH);
        JButton bGTO = new JButton(new ImageIcon(img));
        bGTO.addActionListener(_ -> {
            this.dispose(); // Close the current gui
            new StraightOuttaMenu();  // Start the game
        });
        bGTO.setBorder(new LineBorder(new Color(150, 100, 100), 2, true));
        bGTO.setBackground(new Color(255, 220, 220));

        icon = new ImageIcon("images\\Lueckenfueller.png");
        img = icon.getImage().getScaledInstance(260, 35, Image.SCALE_SMOOTH);
        JButton bCTL = new JButton(new ImageIcon(img));
        bCTL.addActionListener(_ -> {
            this.dispose(); 
            new LueckenfuellerMenu();
        });
        bCTL.setBorder(new LineBorder(new Color(100, 100, 150), 2, true));
        bCTL.setBackground(new Color(220, 220, 255));

        icon = new ImageIcon("images\\aerztle.png");
        img = icon.getImage().getScaledInstance(140, 35, Image.SCALE_SMOOTH);
        JButton bAerztle = new JButton(new ImageIcon(img));
        bAerztle.addActionListener(_ -> {
            this.dispose();
            new AerztleMenu();
        });
        bAerztle.setBorder(new LineBorder(new Color(100, 150, 100), 2, true));
        bAerztle.setBackground(new Color(220, 255, 220));

        /* TODO
        icon = new ImageIcon("images\\MNEM.png");
        img = icon.getImage().getScaledInstance(200, 35, Image.SCALE_SMOOTH);
        JButton bMnem = new JButton(new ImageIcon(img));
        bMnem.addActionListener(_ -> {
            JOptionPane.showMessageDialog(null, "Dieser Spielmodus ist noch in Arbeit.");
            this.dispose();
            new MnemMenu();
            
        });
        bMnem.setBorder(new LineBorder(new Color(125, 125, 100), 2, true));
        bMnem.setBackground(new Color(255, 255, 220));
        */

        JButton bSettings = new JButton("Einstellungen");
        bSettings.setFont(new Font("Folio Extra", Font.BOLD, 20));
        bSettings.addActionListener(_ -> {
            openSettings();
        });
        bSettings.setBorder(new LineBorder(new Color(170, 170, 170), 2, true));
        bSettings.setBackground(new Color(200, 200, 200));

        Dimension buttonSize = new Dimension(300, 40);
        bGTO.setMaximumSize(buttonSize);
        bCTL.setMaximumSize(buttonSize);
        bAerztle.setMaximumSize(buttonSize);
        // TODO bMnem.setMaximumSize(buttonSize);
        bSettings.setMaximumSize(buttonSize);

        bGTO.setAlignmentX(Component.CENTER_ALIGNMENT);
        bCTL.setAlignmentX(Component.CENTER_ALIGNMENT);
        bAerztle.setAlignmentX(Component.CENTER_ALIGNMENT);
        // TODO bMnem.setAlignmentX(Component.CENTER_ALIGNMENT);
        bSettings.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Author + Version
        JPanel authorVersionPanel = new JPanel(new GridLayout(1, 2));
        JLabel lVersion = new JLabel("Version " + VERSION, SwingConstants.LEFT);
        JLabel lAuthor = new JLabel("@ShafiLP", SwingConstants.RIGHT);
        authorVersionPanel.setBorder(BorderFactory.createEmptyBorder(3, 15, 3, 15));
        authorVersionPanel.add(lVersion);
        authorVersionPanel.add(lAuthor);
        this.add(authorVersionPanel, BorderLayout.SOUTH);

        // Add with vertical padding
        buttonPanel.add(bGTO);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(bCTL);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonPanel.add(bAerztle);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        /* TODO
        buttonPanel.add(bMnem);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        */
        buttonPanel.add(bSettings);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        this.add(mainPanel);
        this.setVisible(true);
    }
    
    /**
     * Open universal settings for the game
     */
    private void openSettings() {
        // Load settings from file
        Settings settings = readSettings("data\\settings.json");

        // JFrame settings
        JFrame settingsFrame = new JFrame();
        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingsFrame.setSize(500, 200);
        settingsFrame.setResizable(false);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setLayout(new GridLayout(4, 2));

        // Icon settings
        JCheckBox cbShowIcons = new JCheckBox("Icons anzeigen", settings.isShowIconsEnabled());

        // Bonus library settings
        JCheckBox cbFarin = new JCheckBox("Füge Farins Diskografie hinzu", settings.isFarinEnabled());
        JCheckBox cbBela = new JCheckBox("Füge Belas Diskografie hinzu", settings.isBelaEnabled());
        JCheckBox cbSahnie = new JCheckBox("Füge Sahnies Diskografie hinzu", settings.isSahnieEnabled());

        // Colourful GUI settings
        JCheckBox cbColourfulGui = new JCheckBox("Farbiges Design", settings.isColourfulGuiEnabled());

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

        // Save button
        JButton bSave = new JButton("Speichern");
        bSave.addActionListener(_ -> {
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
            settings.setFontType(ddFont.getSelectedItem().toString());
            settings.setShowIcons(cbShowIcons.isSelected());
            settings.setFarinLibrary(cbFarin.isSelected());
            settings.setBelaLibrary(cbBela.isSelected());
            settings.setSahnieLibrary(cbSahnie.isSelected());
            settings.setColourfulGui(cbColourfulGui.isSelected());

            saveSettings(settings);
            settingsFrame.dispose();
        });

        // Add all to frame
        settingsFrame.add(fontTypePanel);
        settingsFrame.add(cbFarin);
        settingsFrame.add(fontSizePanel);
        settingsFrame.add(cbBela);
        settingsFrame.add(cbShowIcons);
        settingsFrame.add(cbSahnie);
        settingsFrame.add(cbColourfulGui);
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
 * TODO
 * - Setting for colourful GUI
 */