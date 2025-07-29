import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Class for the GUI of the game "Guess The Origin"
 */
class GTOGui extends JFrame{
    private GTOGame game;
    private Settings settings;
    private JPanel center;
    private JPanel infoBar;
    private JComboBox<DropdownItem> songDropdown;
    private AutoCompleteTextField songSearchBar;
    private JTextField manualInput;
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

        // Search bar with auto completion
        // TODO: Custom Renderer for icons to show + add other discographies
        /*if(settings.getTypeOfInput() == "Suchleiste") {
            songSearchBar = new AutoCompleteTextField(songsFromJsonToList("data\\songs.json"));
        }
        
        // Manual input
        if(settings.getTypeOfInput() == "Manuelle Eingabe") {
            manualInput = new JTextField();
        }*/

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
        switch(settings.getTypeOfInput()) {
            case "Dropdown Menü":
                songDropdown = initializeJComboBox();
                guessBar.add(songDropdown, gbc);
                break;
            case "Suchleiste":
                songSearchBar = new AutoCompleteTextField(songsFromJsonToList("data\\songs.json"));
                songSearchBar.addKeyListener(new GTOKeyListener(this));
                guessBar.add(songSearchBar, gbc);
                break;
            case "Manuelle Eingabe":
                manualInput = new JTextField();
                manualInput.addKeyListener(new GTOKeyListener(this));
                guessBar.add(manualInput, gbc);
                break;
        }

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
        switch(settings.getTypeOfInput()) {
            case "Dropdown Menü":
                DropdownItem dmSelected = (DropdownItem) songDropdown.getSelectedItem();
                if(dmSelected.getDropdownText().trim().equals(game.getCurrentSong().trim())) {
                    infoBarRight();
                    game.songGuessed();
                } else {
                    infoBarWrong();
                    game.wrongGuess();
                }
                break;
            case "Suchleiste":
                String sbSelected = songSearchBar.getText().trim();
                if(sbSelected.equals(game.getCurrentSong().trim())) {
                    infoBarRight();
                    game.songGuessed();
                } else {
                    infoBarWrong();
                    game.wrongGuess();
                }
                break;
            case "Manuelle Eingabe":
                String miSelected = manualInput.getText().toLowerCase().trim();
                if(miSelected.equals(game.getCurrentSong().toLowerCase().trim())) {
                    infoBarRight();
                    game.songGuessed();
                } else {
                    infoBarWrong();
                    game.wrongGuess();
                }
                break;
        }
    }

    /**
     * Creates a JComboBox object with all all items from song files as DropdownItem objects
     * @return JComboBox with all songs from files
     */
    private JComboBox<DropdownItem> initializeJComboBox() {
        JComboBox<DropdownItem> comboBox = new JComboBox<>(readSongsFromJson("data\\songs.json"));

        // Add Farin songs if pFarin = true
        if(settings.isFarinEnabled()) {
            JComboBox<DropdownItem> farinDropdown = new JComboBox<>(readSongsFromJson("data\\farinSongs.json"));
            for(int i = 0; i < farinDropdown.getItemCount(); i++) {
                comboBox.addItem(farinDropdown.getItemAt(i));
            }
            farinDropdown = null;
        }

        // Add Bela songs if pBela = true
        if(settings.isBelaEnabled()) {
                JComboBox<DropdownItem> belaDropdown = new JComboBox<>(readSongsFromJson("data\\belaSongs.json"));
            for(int i = 0; i < belaDropdown.getItemCount(); i++) {
                comboBox.addItem(belaDropdown.getItemAt(i));
            }
            belaDropdown = null;
        }

        // Add Sahnie songs if pSahnie = true
        if(settings.isSahnieEnabled()) {
                JComboBox<DropdownItem> sahnieDropdown = new JComboBox<>(readSongsFromJson("data\\sahnieSongs.json"));
            for(int i = 0; i < sahnieDropdown.getItemCount(); i++) {
                comboBox.addItem(sahnieDropdown.getItemAt(i));
            }
            sahnieDropdown = null;
        }

        // Override the renderer to display items next to the dropdown items
        if(settings.isShowIconsEnabled()) // Only override if icons should be displayed
        comboBox.setRenderer(new DefaultListCellRenderer() {
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

        // TODO: Not satisfied with this solution
        // Override KeyEvents for the dropdown menu that "#" can be used as " "
        comboBox.addKeyListener(new KeyAdapter() {
            private static String keyBuffer = "";
            private static long lastKeyTime = 0;

            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();

                // Replace "#" with " "
                if (ch == '#') {
                    ch = ' ';
                }

                long now = System.currentTimeMillis();
                if (now - lastKeyTime > 1000) {
                    keyBuffer = "";
                }
                lastKeyTime = now;

                if (Character.isLetterOrDigit(ch) || Character.isSpaceChar(ch)) {
                    keyBuffer += ch;

                    // Suche Eintrag, der mit dem Buffer beginnt
                    for (int i = 0; i < comboBox.getItemCount(); i++) {
                        String item = comboBox.getItemAt(i).getDropdownText().toLowerCase();
                        if (item.startsWith(keyBuffer.toLowerCase())) {
                            comboBox.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            }
        });
        comboBox.addKeyListener(new GTOKeyListener(this));
        return comboBox;
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

    private LinkedList<String> songsFromJsonToList(String filename) {
        LinkedList<String> songsFromJson = new LinkedList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(filename)));
            JSONArray arr = new JSONArray(content);
            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String songName = obj.getString("song");
                songsFromJson.addLast(songName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return songsFromJson;
    }
}