import javax.swing.*;
import javax.swing.border.LineBorder;

import org.json.JSONArray;
import org.json.JSONObject;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.google.gson.Gson;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;

/**
 * Class for the game main menu
 * Contains paths to all games
 */
public class MainMenu extends JFrame {
    private final String VERSION = "0.5.2";
    private Settings settings;
    private JButton bSettings;
    /**
     * Constructor of main menu
     * Contains paths to all games
     */
    public MainMenu() {
        // Load settings from file
        settings = readSettings("data\\settings.json");

        // Set accent colours
        FlatLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor", "#8f8ffeff"));
        setup();

        this.setTitle("ÄrzteGuessr");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 440);
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

        bSettings = new JButton("Einstellungen");
        bSettings.setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize() * 2));
        bSettings.setForeground(Color.BLACK);
        bSettings.addActionListener(_ -> {
            openSettings();
        });
        bSettings.setBorder(new LineBorder(new Color(170, 170, 170), 2, true));
        bSettings.setBackground(new Color(200, 200, 200));

        Dimension buttonSize = new Dimension(300, 40);
        bGTO.setMaximumSize(buttonSize);
        bCTL.setMaximumSize(buttonSize);
        bAerztle.setMaximumSize(buttonSize);
        bSettings.setMaximumSize(buttonSize);

        bGTO.setAlignmentX(Component.CENTER_ALIGNMENT);
        bCTL.setAlignmentX(Component.CENTER_ALIGNMENT);
        bAerztle.setAlignmentX(Component.CENTER_ALIGNMENT);
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
        buttonPanel.add(bSettings);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        this.add(mainPanel);
        this.setVisible(true);
    }
    
    /**
     * Open universal settings for the game
     */
    private void openSettings() {
        // JFrame settings
        JFrame settingsFrame = new JFrame();
        settingsFrame.setTitle("Einstellungen");
        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        settingsFrame.setSize(500, 250);
        settingsFrame.setResizable(false);
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setLayout(new GridLayout(6, 2));

        // Icon settings
        JCheckBox cbShowIcons = new JCheckBox("Icons anzeigen", settings.isShowIconsEnabled());

        // Bonus library settings
        JCheckBox cbFarin = new JCheckBox("Füge Farins Diskografie hinzu", settings.isFarinEnabled());
        JCheckBox cbBela = new JCheckBox("Füge Belas Diskografie hinzu", settings.isBelaEnabled());
        JCheckBox cbSahnie = new JCheckBox("Füge Sahnies Diskografie hinzu", settings.isSahnieEnabled());

        // Font type settings
        JPanel fontTypePanel = new JPanel(new GridLayout(2, 1));
        JComboBox<String> ddFont = new JComboBox<>();
        ddFont.addItem("Arial");
        ddFont.addItem("Berlin Sans FB");
        ddFont.addItem("Comic Sans MS");
        ddFont.addItem("Folio Extra");
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

        // Colourful GUI settings
        JCheckBox cbColourfulGui = new JCheckBox("Farbiges Design", settings.isColourfulGuiEnabled());

        // Automatic search for updates
        JCheckBox cbSearchForUpdates = new JCheckBox("Suche nach Updates", settings.isSearchForUpdatesEnabled());

        // Darkmode settings
        JCheckBox cbDarkMode = new JCheckBox("Dunkler Modus", settings.isDarkMode());

        // Reset all settings button
        JButton bReset = new JButton("Einstellungen zurücksetzen");
        bReset.addActionListener(_ -> {
            settings = new Settings();
            saveSettings(settings);
            settingsFrame.dispose();
        });

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
            settings.setSearchForUpdates(cbSearchForUpdates.isSelected());
            settings.setDarkMode(cbDarkMode.isSelected());
            
            bSettings.setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize() * 2));
            setup();

            saveSettings(settings);
            settingsFrame.dispose();
        });

        // Add all to frame
        settingsFrame.add(cbDarkMode);
        settingsFrame.add(new JLabel());
        settingsFrame.add(fontTypePanel);
        settingsFrame.add(cbFarin);
        settingsFrame.add(fontSizePanel);
        settingsFrame.add(cbBela);
        settingsFrame.add(cbShowIcons);
        settingsFrame.add(cbSahnie);
        settingsFrame.add(cbColourfulGui);
        settingsFrame.add(cbSearchForUpdates);
        settingsFrame.add(bReset);
        settingsFrame.add(bSave);

        // Set frame visible
        settingsFrame.setVisible(true);
    }

    /**
     * Initializes FlatLaf GUI
     */
    protected void setup() {
        // Set accent colours
        FlatLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor", settings.getAccentColour()));

        // Set Theme
        if(settings.isDarkMode()){
            FlatDarkLaf.setup();
            com.formdev.flatlaf.FlatDarkLaf.updateUI();
        } else {
            FlatLightLaf.setup();
            com.formdev.flatlaf.FlatLaf.updateUI();
        }
    }

    /**
     * Check if a newer version is available on GitHub.
     * Called when Starting the application.
     */
    public void checkForUpdate() {
        if(settings.isSearchForUpdatesEnabled()) {
            final String REPOSITORY_API = "https://api.github.com/repos/ShafiLP/Die-Aerzte-Guessr/releases";
            try {
                URL url = new URL(REPOSITORY_API);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("User-Agent", "Java-Version-Checker");
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);

                int status = conn.getResponseCode();
                if (status != 200) {
                    System.out.println("HTTP-Fehler: " + status);
                    return;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONArray releases = new JSONArray(response.toString());
                if(releases.length() == 0)
                return;

                JSONObject json = releases.getJSONObject(0);
                
                String latestVersion = json.getString("tag_name").replace("v", "");
                if(isNewerVersionAvailable(latestVersion, VERSION)) {
                    System.out.println("Neue Version verfügbar: " + latestVersion);
                    System.out.println("Lade sie runter unter: https://github.com/ShafiLP/Die-Aerzte-Guessr");
                    openNewerVersionNotification();
                } else {
                    System.out.println("Neuste Version installiert: v" + VERSION);
                }
            } catch (Exception e) {
                System.out.println("Fehler beim Überprüfen der Version: " + e.getMessage());
            }
        }
    }

    /**
     * Compares current version with newest version
     * @param pLatest Latest version from GitHub repository
     * @param pCurrent Current version
     * @return true if newer version is available, return false if current version is the same or higher as latest
     */
    private boolean isNewerVersionAvailable(String pLatest, String pCurrent) {
        String[] latestParts = pLatest.split("\\.");
        String[] currentParts = pCurrent.split("\\.");
        for(int i = 0; i < Math.max(latestParts.length, currentParts.length); i++) {
            int l = i < latestParts.length ? Integer.parseInt(latestParts[i]) : 0;
            int c = i < currentParts.length ? Integer.parseInt(currentParts[i]) : 0;
            if (l > c) return true;
            if (l < c) return false;
        }
        return false;
    }

    private void openNewerVersionNotification() {
            Object[] options = {"Neue Version runterladen", "Erinnere mich später"};
            int n = JOptionPane.showOptionDialog(
                null,
                "Eine neue Version von Ärzte-Guessr ist verfügbar!\nMöchtest du sie runterladen?",
                "Neue Version verfügbar",
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION,
                null, // Icon
                options,
                options[0]
            );
            switch(n) {
                case 0:
                    try {
                        Desktop.getDesktop().browse(new URL("https://github.com/ShafiLP/Die-Aerzte-Guessr").toURI());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    break;
            }

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
