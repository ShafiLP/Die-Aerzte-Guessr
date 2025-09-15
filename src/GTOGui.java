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
class GTOGui extends JFrame implements EnterKeyListener{
    private GTOGame game;
    private Settings settings;
    private JPanel centerPanel;
    private JPanel infoBar;
    private JComboBox<DropdownItem> songDropdown;
    private AutoCompleteTextField songSearchBar;
    private JTextField manualInput;
    private JLabel lyricLabel = new JLabel();
    private JLabel timerLabel;
    private JLabel currentSongLabel = new JLabel();
    private JLabel scoreLabel = new JLabel("Punktzahl: 0", SwingConstants.RIGHT);
    private JLabel[] healthBar;
    private JLabel lSolution;

    /**
     * Constructor for the GTGGui class
     * Creates a GUI for the game "Guess The Origin"
     */
    public GTOGui(GTOGame pGame, String pLyric, Settings pSettings) {
        game = pGame;

        // Read settings
        settings = pSettings;
        timerLabel = new JLabel("Timer: " + settings.getGtoTimeLimit());
        healthBar = new JLabel[settings.getGtoLiveCount()];

        // JFrame settings
        this.setTitle("Straight Outta...");
        this.setLayout(new BorderLayout());
        this.setSize(600, 300);
        this.setLocationRelativeTo(null); // Center the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon("images\\daLogo.png").getImage());

        currentSongLabel.setFont(new Font(settings.getFontType(), Font.PLAIN, 12));

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

        // Create GUI center, where the song text will be displayed
        centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(255, 220, 220));

        // Lyrics to guess in the center
        lyricLabel = new JLabel("„" + pLyric + "“");
        lyricLabel.setFont(new Font(settings.getFontType(), Font.PLAIN, settings.getFontSize()));

        lSolution = new JLabel("Hineweis: " + game.requestHint() + "...");
        lSolution.setFont(new Font(settings.getFontType(), Font.PLAIN, settings.getFontSize()));
        lSolution.setForeground(Color.RED);
        lSolution.setVisible(false);

        centerPanel.add(lyricLabel, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 0;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.VERTICAL;
        }});
        centerPanel.add(lSolution, new GridBagConstraints() {{
            gridx = 0;
            gridy = 1;
            gridwidth = 1;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.NONE;
        }});

        // Guessing bar for guess input and submit button
        JPanel guessBar = new JPanel(new GridBagLayout());
        guessBar.setBackground(new Color(255, 220, 220));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        switch(settings.getGtoTypeOfInput()) {
            case "Dropdown Menü":
                songDropdown = initializeJComboBox();
                guessBar.add(songDropdown, gbc);
                break;
            case "Suchleiste":
                songSearchBar = initializAutoCompleteTextField();
                songSearchBar.addKeyListener(new SubmitKeyListener(this));
                guessBar.add(songSearchBar, gbc);
                break;
            case "Manuelle Eingabe":
                manualInput = new JTextField();
                manualInput.addKeyListener(new SubmitKeyListener(this));
                guessBar.add(manualInput, gbc);
                break;
        }

        // Hint button
        JButton bHint = new JButton("Hinweis");
        bHint.addActionListener(_ -> {
            lSolution.setText("Hinweis: " + game.requestHint() + "...");
            lSolution.setVisible(true);
        });
        bHint.setPreferredSize(new Dimension(120, 30));
        guessBar.add(bHint, new GridBagConstraints() {{
            gridx = 1;
            gridy = 0;
            weightx = 0;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Submit button
        JButton bSubmit = new JButton("Raten");
        bSubmit.addActionListener(_ -> {
            submitButtonPressed();
        });
        bSubmit.setPreferredSize(new Dimension(120, 30));
        guessBar.add(bSubmit, new GridBagConstraints() {{
            gridx = 2;
            gridy = 0;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Health bar at the top left corner
        JPanel healthPanel = new JPanel(new GridLayout(1, healthBar.length));
        for(int i = 0; i < healthBar.length; i++) {
            healthPanel.add(healthBar[i]);
        }
        healthPanel.setOpaque(false);

        // Upper left corner of infoBar
        JPanel upperLeft = new JPanel(new GridBagLayout());
        upperLeft.setOpaque(false);

        upperLeft.add(healthPanel, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 1.0;
            anchor = GridBagConstraints.LINE_START;
            fill = GridBagConstraints.NONE;
        }});

        // Upper right corner of infoBar
        JPanel upperRight = new JPanel(new GridBagLayout());
        upperRight.setOpaque(false);

        upperRight.add(scoreLabel, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 1.0;
            gbc.anchor = GridBagConstraints.LINE_END;
            gbc.fill = GridBagConstraints.NONE;
        }});

        upperRight.add(timerLabel, new GridBagConstraints() {{
            gridx = 0;
            gridy = 1;
            weightx = 1.0;
            gbc.anchor = GridBagConstraints.LINE_END;
            gbc.fill = GridBagConstraints.NONE;
        }});

        // Initialize infoBar Panel
        infoBar = new JPanel(new GridBagLayout());
        infoBar.setBackground(new Color(230, 100, 100));

        infoBar.add(upperLeft, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 1.0;
            anchor = GridBagConstraints.LINE_START;
            fill = GridBagConstraints.NONE;
        }});

        infoBar.add(currentSongLabel, new GridBagConstraints() {{
            gridx = 1;
            gridy = 0;
            weightx = 1.0;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.NONE;
        }});

        infoBar.add(upperRight, new GridBagConstraints() {{
            gridx = 2;
            gridy = 0;
            weightx = 1.0;
            anchor = GridBagConstraints.LINE_END;
            fill = GridBagConstraints.NONE;
        }});

        infoBar.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // Add all components to the main frame
        this.add(guessBar, BorderLayout.SOUTH);
        this.add(infoBar, BorderLayout.NORTH);
        this.add(centerPanel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    /**
     * Updates the lyric label with a new lyric
     * @param pLyric the new lyric to display
     */
    public void guessTheOriginUpdate(String pLyric) {
        lyricLabel.setText("„" + pLyric + "“");
    }

    /**
     * Removes one health point from the health bar
     */
    public void removeHealth() {
        for(int i = healthBar.length - 1; i >= 0; i--) {
            if(healthBar[i].isVisible()) {
                healthBar[i].setVisible(false);
                return;
            }
        }
    }

    /**
     * Sets the text on the timer label
     * @param text new text on the timer label
     */
    public void setTimerLabel(String text) {
        timerLabel.setText("Timer: " + text);
    }

    /**
     * Sets the text on the score label
     * @param pScore new text on the score label
     */
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
        lSolution.setVisible(false);
    }

    /**
     * Handles the submit button press event and the enter key press event
     */
    public void submitButtonPressed() {
        switch(settings.getGtoTypeOfInput()) {
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
                songSearchBar.setText("");
                songSearchBar.requestFocusInWindow();
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
                manualInput.setText("");
                manualInput.requestFocusInWindow();
                break;
        }
    }

    /**
     * Creates a JComboBox object with all all items from song files as DropdownItem objects
     * @return JComboBox with all songs from .json files
     */
    private JComboBox<DropdownItem> initializeJComboBox() {
        JComboBox<DropdownItem> comboBox = new JComboBox<>(dropdownArrayFromJson("data\\songs.json"));

        // Add Farin songs if pFarin = true
        if(settings.isFarinEnabled()) {
            JComboBox<DropdownItem> farinDropdown = new JComboBox<>(dropdownArrayFromJson("data\\farinSongs.json"));
            for(int i = 0; i < farinDropdown.getItemCount(); i++) {
                comboBox.addItem(farinDropdown.getItemAt(i));
            }
            farinDropdown = null;
        }

        // Add Bela songs if pBela = true
        if(settings.isBelaEnabled()) {
                JComboBox<DropdownItem> belaDropdown = new JComboBox<>(dropdownArrayFromJson("data\\belaSongs.json"));
            for(int i = 0; i < belaDropdown.getItemCount(); i++) {
                comboBox.addItem(belaDropdown.getItemAt(i));
            }
            belaDropdown = null;
        }

        // Add Sahnie songs if pSahnie = true
        if(settings.isSahnieEnabled()) {
                JComboBox<DropdownItem> sahnieDropdown = new JComboBox<>(dropdownArrayFromJson("data\\sahnieSongs.json"));
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

        // Override KeyEvents for the dropdown menu that the space bar works
        comboBox.addKeyListener(new KeyAdapter() {
            private static String keyBuffer = "";
            private static long lastKeyTime = 0;

            @Override
            public void keyTyped(KeyEvent e) {
                char ch = e.getKeyChar();

                // Space will be a valid char
                if (ch == ' ') {
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
        comboBox.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
        .put(KeyStroke.getKeyStroke("SPACE"), "none"); // Prevents closing the dropdown menu by pressing space bar

        comboBox.addKeyListener(new SubmitKeyListener(this));
        return comboBox;
    }

    /**
     * Intitializes an AutoCompleteTextField object with all song names based on the settings parameters
     * @return initialized AutoCompleteTextField with all song objects
     */
    private AutoCompleteTextField initializAutoCompleteTextField() {
        LinkedList<DropdownItem> suggestions = dropdownListFromJson("data\\songs.json");
        if(settings.isFarinEnabled()) {
            LinkedList<DropdownItem> farinSuggestions = dropdownListFromJson("data\\farinSongs.json");
            suggestions.addAll(farinSuggestions);
        }
        if(settings.isBelaEnabled()) {
            LinkedList<DropdownItem> belaSuggestions = dropdownListFromJson("data\\belaSongs.json");
            suggestions.addAll(belaSuggestions);
        }
        if(settings.isSahnieEnabled()) {
            LinkedList<DropdownItem> sahnieSuggestions = dropdownListFromJson("data\\sahnieSongs.json");
            suggestions.addAll(sahnieSuggestions);
        }

        return new AutoCompleteTextField(suggestions, settings.isShowIconsEnabled());
    }

    /**
     * Creates an array of DropdownItems containing song name and icon from the given .json file
     * @param filename path where the song names with icons are located (must contain "song" and "icon" key)
     * @return an array of DropdownItems containing song names and icons
     */
    private DropdownItem[] dropdownArrayFromJson(String filename) {
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

    /**
     * Creates a LinkedList of DropdownItems with all songs in the given JSON file
     * @param filename path to the JSON file with the song data
     * @return LinkedList with song elements, containing song name and album cover as icon
     */
    private LinkedList<DropdownItem> dropdownListFromJson(String filename) {
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
        return songs;
    }
}
