package com.aerzteguessr;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.gradle.internal.impldep.com.fasterxml.jackson.core.JsonProcessingException;
import org.gradle.internal.impldep.com.fasterxml.jackson.databind.JsonNode;
import org.gradle.internal.impldep.com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;

import com.aerzteguessr.design.UI;
import com.aerzteguessr.models.Song;
import com.aerzteguessr.services.Database;
import com.aerzteguessr.services.Log;
import com.aerzteguessr.views.LoadingScreen;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;

/**
 * Class for the game main menu
 * Contains paths to all games
 */
public class MainMenu extends JFrame {
    private final String VERSION = "0.6.1.3";
    private final Settings settings;

    /**
     * Constructor of main menu
     * Contains paths to all games
     */
    public MainMenu() {
        // Load settings from file
        settings = Settings.read();

        // Check for updates
        checkForUpdate();

        // Set accent colours
        FlatLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor", "#8f8ffeff"));
        setup();

        this.setTitle("ÄrzteGuessr");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(450, 550);
        this.setResizable(false);
        this.setLocationRelativeTo(null); // Center the window
        this.setIconImage(new ImageIcon("images\\daLogo.png").getImage());

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 25, 25, 25));
        this.setContentPane(mainPanel);

        // ===================================
        // TOP PANEL (Logo, Exit, Settings)
        // ===================================
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Exit button
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setOpaque(false);
        left.add(UI.quitButton());
        topBar.add(left, BorderLayout.WEST);

        // Logo
        ImageIcon logoIcon = new ImageIcon("images\\aerzteGuessr.png");
        Image logoImg = logoIcon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(logoImg));
        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER));
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0)); // 👈 WICHTIG: Abstand nach unten
        center.add(logo);
        topBar.add(center, BorderLayout.CENTER);

        // Settigns button
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setOpaque(false);
        right.add(UI.settingsButton());
        topBar.add(right, BorderLayout.EAST);

        mainPanel.add(topBar, BorderLayout.NORTH);

        // ===================================
        // GAME SELECTION
        // ===================================
        Image bg = new ImageIcon("images/auch.png").getImage();

        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        gridPanel.setOpaque(false);

        // STRAIGHT OUTTA
        gridPanel.add(UI.createGameModeCard(
            new ImageIcon("app/src/main/resources/images/gamemodeTitles/straightoutta.png").getImage(),
            new ImageIcon("app/src/main/resources/images/gamemodeBackgrounds/belaAlbtraum.png").getImage(),
            new ImageIcon("app/src/main/resources/images/gamemodeImages/debil-cutout.png").getImage(),
            new Color(200, 70, 70),
            () -> {
                this.dispose();
                new StraightOuttaMenu(settings);
            })
        );

        // LUECKENFUELLER
        gridPanel.add(UI.createGameModeCard(
            new ImageIcon("app/src/main/resources/images/gamemodeTitles/lueckenfueller.png").getImage(),
            new ImageIcon("app/src/main/resources/images/gamemodeBackgrounds/alteSaecke.png").getImage(),
            new ImageIcon("app/src/main/resources/images/gamemodeImages/schatten-cutout.png").getImage(),
            new Color(70, 70, 200),
            () -> {
                this.dispose();
                new LueckenfuellerMenu(settings);
            }
        ));

        // ÄRZTLE
        gridPanel.add(UI.createGameModeCard(
            new ImageIcon("app/src/main/resources/images/gamemodeTitles/aerztle.png").getImage(),
            bg,
            new ImageIcon("app/src/main/resources/images/gamemodeImages/jazz-cutout.png").getImage(),
            new Color(70, 200, 70),
            () -> {
                this.dispose();
                new AerztleMenu(settings);
            }
        ));

        // Platzhalter
        gridPanel.add(
            UI.createGameModeCard(new ImageIcon("app/src/main/resources/images/gamemodeTitles/comingsoon.png").getImage(),
            new ImageIcon("app/src/main/resources/images/gamemodeImages/empty.png").getImage(),
            new ImageIcon("app/src/main/resources/images/gamemodeImages/empty.png").getImage(),
            new Color(30, 30, 30),
            () -> System.out.println("Coming Soon!")
        ));

        gridPanel.add(
            UI.createGameModeCard(new ImageIcon("app/src/main/resources/images/gamemodeTitles/comingsoon.png").getImage(),
            new ImageIcon("app/src/main/resources/images/gamemodeImages/empty.png").getImage(),
            new ImageIcon("app/src/main/resources/images/gamemodeImages/empty.png").getImage(),
            new Color(30, 30, 30),
            () -> System.out.println("Coming Soon!")
        ));

        gridPanel.add(
            UI.createGameModeCard(new ImageIcon("app/src/main/resources/images/gamemodeTitles/comingsoon.png").getImage(),
            new ImageIcon("app/src/main/resources/images/gamemodeImages/empty.png").getImage(),
            new ImageIcon("app/src/main/resources/images/gamemodeImages/empty.png").getImage(),
            new Color(30, 30, 30),
            () -> System.out.println("Coming Soon!")
        ));

        mainPanel.add(gridPanel, BorderLayout.CENTER);

        // ===================================
        // INFO BAR (Author, Version)
        // ===================================
        JPanel lowerBar = new JPanel(new BorderLayout());

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root;
            root = mapper.readTree("app/appInfo.json");

            lowerBar.add(new JLabel(root.path("author").asText()), BorderLayout.LINE_START);
            lowerBar.add(new JLabel(root.path("version").asText()), BorderLayout.CENTER);
        } catch (JsonProcessingException e) {
            Log.Error(e.getMessage());
        }

        mainPanel.add(lowerBar, BorderLayout.SOUTH);
        

        // Make frame visible
        this.setVisible(true);


        // Initialize Database and display loading screen
        LoadingScreen loadingOverlay = new LoadingScreen();
        this.setGlassPane(loadingOverlay);
        loadingOverlay.start();

        Database.initializeDatabase();

        // DEBUG: Print all songs
        List<Song> songs = Database.getAllSongs();
        for (Song s : songs) {
            System.out.println(
                s.id + " | " +
                s.name + " | " +
                s.albumId + " | " +
                s.releaseYear + " | " +
                s.streams + " | " +
                s.duration + " | " +
                s.liveplays + " | " +
                s.singer + " | " +
                s.hasSingle
            );
        }  

        loadingOverlay.stop();
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
     * Compares current version with latest version
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
}
