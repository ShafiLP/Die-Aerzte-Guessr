import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import org.json.JSONArray;
import org.json.JSONObject;
import java.awt.event.*;
import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//Gson must be in /lib folder
import com.google.gson.Gson;

/**
 * Class for the settings menu of "Guess The Origin" game
 * Creates a new GTOGame class after pressing the start button
 */
public class GuessTheOrigin extends JFrame {
    // Settings for the settings frame
    private Settings settings;

    /**
     * Crates a GUI for starting the game and configuring settings
     */
    public GuessTheOrigin() {
        settings = readJson("data\\settings.json");

        this.setTitle("Errate den Ursprung");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(300, 300);
        this.setResizable(false);
        this.setLocationRelativeTo(null); // Center the window


        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 30, 40)); // Padding

        // Heading
        JPanel headingPanel = new JPanel();
        ImageIcon icon = new ImageIcon("images\\GTO.png");
        Image img = icon.getImage().getScaledInstance(270, 35, Image.SCALE_SMOOTH);
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
            this.dispose();             // Close the current gui
            new GTOGame(settings); // Start the game
        });
        JButton bSettings = new JButton("Einstellungen");
        bSettings.addActionListener(_ -> {
            openSettings();
        });
        JButton bBack = new JButton("Hauptmenü");
        //TODO: addActionListener

        Dimension buttonSize = new Dimension(200, 40);
        bPlay.setMaximumSize(buttonSize);
        bSettings.setMaximumSize(buttonSize);
        bBack.setMaximumSize(buttonSize);

        bPlay.setAlignmentX(Component.CENTER_ALIGNMENT);
        bSettings.setAlignmentX(Component.CENTER_ALIGNMENT);
        bBack.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Highscore
        JPanel highscorePanel = new JPanel();
        JLabel lHighscore = new JLabel("Highscore: " + settings.getHighscore());
        highscorePanel.setBorder(BorderFactory.createEmptyBorder(3, 15, 3, 15));
        highscorePanel.add(lHighscore);
        this.add(highscorePanel, BorderLayout.SOUTH);

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
            e.printStackTrace();
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











/**
 * Class for the game "Guess The Origin"
 * Implements TimerEvents to handle timer
 */
class GTOGame implements TimerEvents {
    private Settings settings;
    private GTOGui gui;
    private int score = 0;
    private int lives = 3; 
    private boolean blockWrongGuesses = false;
    private SongText currentSongText;
    private LinkedList<SongText> songTexts = new LinkedList<>();
    private int timeLimit;

    /**
     * Constructor for the GTOGame class
     * @param pSettings Settings for the game
     */
    public GTOGame(Settings pSettings) {
        // Apply settings
        settings = pSettings;
        lives = settings.getLiveCount();
        timeLimit = settings.getTimeLimit();

        // Create a LinkedList containing all available song lyrics
        songTexts = readSongsFromJson("data\\lyrics.json", songTexts);

        // Add Farin songs if pFarin = true
        if(settings.isFarinEnabled()) songTexts = readSongsFromJson("data\\farinLyrics.json", songTexts);

        // Add Bela songs if pBela = true
        if(settings.isBelaEnabled()) songTexts = readSongsFromJson("data\\belaLyrics.json", songTexts);

        // Add Sahnie songs if pSahnie = true
        if(settings.isSahnieEnabled()) songTexts = readSongsFromJson("data\\sahnieLyrics.json", songTexts);
 
        // Get a random song text part
        currentSongText = getRandomSongText();

        // Create GUI
        gui = new GTOGui(this, currentSongText.getText(), settings);

        // Start timer
        if(!settings.isUnlimitedTimeEnabled()) {
            TimerEventManager timer = new TimerEventManager(this);
            timer.start();
        } else {
            gui.setTimerLabel("Kein Timer");
        }
    }

    /**
     * Rerolls the current lyric and updates the GUI
     * This method is called when the user guesses the song correctly
     */
    public void songGuessed() {
        score++;
        blockWrongGuesses = false;
        currentSongText = getRandomSongText();
        if(currentSongText == null) return;
        gui.guessTheOriginUpdate(currentSongText.getText());
        timeLimit = settings.getTimeLimit(); // Reset the timer
        gui.setTimerLabel(timeLimit + "s");
        gui.updateScore(score);
    }

    /**
     * Handles the case when the user guesses incorrectly
     * Displays a dialog with the score and options to start a new game or exit
     */
    public void wrongGuess() {
        if(!settings.isUnlimitedLivesEnabled()) {
            lives--;
            gui.removeHealth();
            blockWrongGuesses = true;
        }

        if(lives <= 0) {
            if(settings.getHighscore() < score) {
                settings.setHighscore(score);
                saveSettings(settings);
            }
            String[] options = {"Neues Spiel", "Beenden"};
            int n = JOptionPane.showOptionDialog(
                gui,
                "Du hast falsch geraten!\nDu hast " + score + " Punkte erreicht.",
                "ÄrzteGuessr",
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION,
                null, // Icon
                options,
                options[0]
            );
            switch(n) {
                case 0:
                    gui.dispose(); // Close the current GUI
                    new GTOGame(settings); // Restart the game
                    return;
                case 1:
                    gui.dispose(); // Close the GUI & exit the game
                    System.exit(0);
                    return;
            }
        }
    }

    /**
     * Gets the current song name from the current lyric
     * @return the current song name
     */
    public String getCurrentSong() {
        return currentSongText.getSongName();
    }

    /**
     * Gets a SongText object from the lyrics.json file
     * Removes the SongText from the List to avoid reputition
     * Checks if the List is empty
     * @return Random SongText object
     */
    private SongText getRandomSongText() {
        // Checks if all songs were guessed
        if(songTexts.isEmpty()) {
            if(settings.getHighscore() < score) {
                settings.setHighscore(score);
                saveSettings(settings);
            }
            Object[] options = {"Neues Spiel", "Beenden"};
            int n = JOptionPane.showOptionDialog(
                gui,
                "Du hast ALLE verfügbaren Songausschnitte erraten!\nDeine Punktzahl beträgt: " + score,
                "Errate den Ursprung",
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION,
                null, // Icon
                options,
                options[0]
            );
            switch(n) {
                case 0:
                    gui.dispose(); // Close the current GUI
                    new GTOGame(settings); // Restart the game
                    return null;
                case 1:
                    gui.dispose(); // Close the GUI & exit the game
                    System.exit(0);
                    return null;
            }
        }
        int randomLyricIndex = (int) (Math.random() * songTexts.size());
        SongText randomSongText =  songTexts.get(randomLyricIndex);
        songTexts.remove(randomLyricIndex); // Removes the object from the List to avoid reputition
        return randomSongText;
    }

    public LinkedList<SongText> readSongsFromJson(String filepath, LinkedList<SongText> songList) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filepath)));
            JSONArray arr = new JSONArray(content);
            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                songList.add(new SongText(obj.getString("lyric"), obj.getString("song")));
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songList;
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

    /**
     * Handles the timer event
     */
    public void timerEvent() {
        timeLimit--;
        gui.setTimerLabel(timeLimit + "s");

        if(timeLimit == 0 & blockWrongGuesses == false) {
            gui.infoBarWrong();
            wrongGuess(); // Count as a wrong guess if the timer runs out
        }
    }
}








/**
 * Class for the GUI of the game "Guess The Origin"
 */
class GTOGui extends JFrame{
    private GTOGame game;
    private Settings settings;
    private JPanel center;
    private JPanel infoBar;
    private JComboBox<DropdownItem> songDropdown;
    private JLabel lyricLabel = new JLabel();
    private JLabel timerLabel = new JLabel("Timer: 30s", SwingConstants.RIGHT);
    private JLabel currentSongLabel = new JLabel();
    private JLabel scoreLabel = new JLabel("Punktzahl: 0", SwingConstants.LEFT);
    private JLabel[] healthBar;

    /**
     * Constructor for the GTGGui class
     * Creates a GUI for the game "Guess The Origin"
     */
    public GTOGui(GTOGame pGame, String pLyric, Settings pSettings) {
        // Read settings
        game = pGame;
        settings = pSettings;
        healthBar = new JLabel[settings.getLiveCount()];

        // JFrame settings
        this.setTitle("ÄrzteGuessr");
        this.setLayout(new BorderLayout());
        this.pack();
        this.setSize(600, 200);
        this.setLocationRelativeTo(null); // Center the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Health bar
        if(settings.isShowIconsEnabled()) {
            ImageIcon healthIcon = new ImageIcon("images\\health.png");
            Image healtImg = healthIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            for(int i = 0; i < healthBar.length; i++) {
                healthBar[i] = new JLabel(new ImageIcon(healtImg));
                healthBar[i].setBounds(i * 25 + 2, 25, 25, 25);
            }
        } else {
            for(int i = 0; i < healthBar.length; i++) {
                healthBar[i] = new JLabel("❤️");
                healthBar[i].setBounds(i * 15 + 2, 25, 25, 25);
            }
        }

        // Create GUI center, where the lyric will be displayed
        center = new JPanel();
        center.setLayout(new GridBagLayout());

        // Dropdown menu with all the songs from the file
        songDropdown = new JComboBox<>(readSongsFromJson("data\\songs.json"));

        // Add Farin songs if pFarin = true
        if(settings.isFarinEnabled()) {
            JComboBox<DropdownItem> farinDropdown = new JComboBox<>(readSongsFromJson("data\\farinSongs.json"));
        for(int i = 0; i < farinDropdown.getItemCount(); i++) {
            songDropdown.addItem(farinDropdown.getItemAt(i));
        }
        farinDropdown = null;
        }

        // Add Bela songs if pBela = true
        if(settings.isBelaEnabled()) {
            JComboBox<DropdownItem> belaDropdown = new JComboBox<>(readSongsFromJson("data\\belaSongs.json"));
        for(int i = 0; i < belaDropdown.getItemCount(); i++) {
            songDropdown.addItem(belaDropdown.getItemAt(i));
        }
        belaDropdown = null;
        }

        // Add Sahnie songs if pSahnie = true
        if(settings.isSahnieEnabled()) {
            JComboBox<DropdownItem> sahnieDropdown = new JComboBox<>(readSongsFromJson("data\\sahnieSongs.json"));
        for(int i = 0; i < sahnieDropdown.getItemCount(); i++) {
            songDropdown.addItem(sahnieDropdown.getItemAt(i));
        }
        sahnieDropdown = null;
        }

        // Override the renderer to display items next to the dropdown items
        if(settings.isShowIconsEnabled()) // Only override if icons should be displayed
        songDropdown.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof DropdownItem) {
                    DropdownItem item = (DropdownItem) value;
                    label.setText(item.getDropdownText());
                    label.setIcon(item.getDropdownIcon());
                }
                return label;
            }
        });
        songDropdown.addKeyListener(new GTOKeyListener(this));

        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(_ -> {
            submitButtonPressed();
        });

        // Guessing bar for dropdown menu and submit button
        JPanel guessBar = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        guessBar.add(songDropdown, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        guessBar.add(submitButton, gbc);

        // Lyrics to guess in the center
        lyricLabel.setText("„" + pLyric + "“");
        lyricLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lyricLabel.setFont(new Font("Folio Extra BT", Font.BOLD, 15));
        center.add(lyricLabel);

        // Health bar at the top left corner
        for(int i = 0; i < healthBar.length; i++) {
            this.add(healthBar[i]);
        }

        // Info bar with score and timer
        infoBar = new JPanel(new GridLayout(1, 3));
        infoBar.add(scoreLabel);
        infoBar.add(currentSongLabel); // Placeholder
        infoBar.add(timerLabel);
        infoBar.setBorder(new EmptyBorder(5, 5, 5, 5));
        infoBar.setBackground(Color.LIGHT_GRAY);
        
        // Add all components to the main frame
        this.add(guessBar, BorderLayout.SOUTH);
        this.add(infoBar, BorderLayout.NORTH);
        this.add(center, BorderLayout.CENTER);
        this.setVisible(true);
    }

    /**
     * Updates the lyric label with a new lyric
     * @param pLyric the new lyric to display
     */
    public void guessTheOriginUpdate(String pLyric) {
        lyricLabel.setText("„" + pLyric + "“");
    }

    public void removeHealth() {
        for(int i = healthBar.length - 1; i >= 0; i--) {
            if(healthBar[i].isVisible()) {
                healthBar[i].setVisible(false);
                return;
            } //TODO: Not satisfied with this solution, but it works for now. Arrange health bar in a better way
        }
    }

    public void setTimerLabel(String text) {
        timerLabel.setText("Timer: " + text);
    }

    public void updateScore(int pScore) {
        scoreLabel.setText("Punktzahl : " + pScore);
    }

    /**
     * Sets the info bar to red and displays the current song name
     * This is called when the user makes an incorrect guess
     */
    public void infoBarWrong() {
        infoBar.setBackground(Color.RED);
        currentSongLabel.setText("Aktuelles Lied: " + game.getCurrentSong());
    }

    /**
     * Sets the info bar to green and clears the current song label
     * This is called when the user guesses the song correctly
     */
    public void infoBarRight() {
        currentSongLabel.setText("");
        infoBar.setBackground(Color.GREEN);
        infoBar.paintImmediately(infoBar.getVisibleRect()); // Update GUI immediately
        try {
            Thread.sleep(1000); // Pause for 1 second to show the green info bar
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        infoBar.setBackground(Color.LIGHT_GRAY); // Reset to default color
    }

    /**
     * Handles the submit button press event and the enter key press event
     */
    public void submitButtonPressed() {
        DropdownItem selected = (DropdownItem) songDropdown.getSelectedItem();
        if(selected.getDropdownText().trim().equals(game.getCurrentSong().trim())) {
            infoBarRight();
            game.songGuessed();
        } else {
            infoBarWrong();
            game.wrongGuess();
        }
    }

    /**
     * Reads songs from a JSON file and returns them as an array of DropdownItem
     * @param filename
     * @return an array of DropdownItem containing song names and icons
     */
    private DropdownItem[] readSongsFromJson(String filename) {
        LinkedList<DropdownItem> songs = new LinkedList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            JSONArray arr = new JSONArray(content);
            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String songName = obj.getString("song");
                String iconPath = obj.getString("icon");
                Icon icon = new ImageIcon("images\\" + iconPath + ".png");
                songs.add(new DropdownItem(songName, icon));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songs.toArray(new DropdownItem[0]);
    }
}











/**
 * Key Listener to confirm dropdown selection with enter key.
 */
class GTOKeyListener extends Thread implements KeyListener {
    private GTOGui gui;

    public GTOKeyListener(GTOGui pGui) {
        gui = pGui;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Check if the pressed key is Enter
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            gui.submitButtonPressed();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No action needed on key release
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // No action needed on key typed
    }
}

//TODO:
// - Add a highscore system
// - QoL improvements for the dropdown menu
// - Fix lyric display (currently can be too long for the window)
// - Score reset button, Dropdown or search bar toggle
