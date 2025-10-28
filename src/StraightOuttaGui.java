import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

import java.util.LinkedList;

/**
 * Class for the GUI of the game "Guess The Origin"
 */
class StraightOuttaGui extends Gui {
    private StraightOuttaGame game;
    private JPanel centerPanel;
    private JPanel infoBar;
    private JComboBox<DropdownItem> songDropdown;
    private AutoCompleteTextField songSearchBar;
    private JLabel lyricLabel = new JLabel();
    private JLabel timerLabel;
    private JLabel currentSongLabel = new JLabel();
    private JLabel scoreLabel = new JLabel("Punktzahl: 0", SwingConstants.RIGHT);
    private JLabel[] healthBar;
    private JLabel lSolution;
    private JButton bHint;
    private JButton bSubmit;
    private Color backgroundColor;
    private Color infobarColor;

    /**
     * Constructor for the GTGGui class
     * Creates a GUI for the game "Guess The Origin"
     */
    public StraightOuttaGui(StraightOuttaGame pGame, String pLyric, Settings pSettings) {
        game = pGame;

        // Read settings
        settings = pSettings;
        timerLabel = new JLabel("Timer: " + settings.getGtoTimeLimit());
        healthBar = new JLabel[settings.getGtoLiveCount()];
        if(settings.isColourfulGuiEnabled()) {
            backgroundColor = new Color(255, 220, 220);
            infobarColor = new Color(230, 100, 100);
            if(settings.isDarkMode()) {
                backgroundColor = new Color(90, 40, 40);
                infobarColor = new Color(50, 20, 20);
            }
        } else {
            backgroundColor = Color.WHITE;
            infobarColor = Color.LIGHT_GRAY;
        }

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

        // Create GUI center, where heading and panel with lyrics will be displayed
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(backgroundColor);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        // Heading
        JLabel lHeading = new JLabel("", SwingConstants.CENTER);
        ImageIcon icon = new ImageIcon("images\\StraightOutta.png");
        Image img = icon.getImage().getScaledInstance(280, 40, Image.SCALE_SMOOTH);
        lHeading.setIcon(new ImageIcon(img));
        centerPanel.add(lHeading, BorderLayout.NORTH);

        // Lyrics label with lyrics and hint
        JPanel lyricsPanel = new JPanel();
        lyricsPanel.setLayout(new GridBagLayout());

        // Lyrics to guess in the center
        lyricLabel = new JLabel("„" + pLyric + "“");
        lyricLabel.setFont(new Font(settings.getFontType(), Font.PLAIN, settings.getFontSize()));
        lyricsPanel.setOpaque(false);

        lSolution = new JLabel(/*"Hineweis: " + game.requestHint() + "..."*/);
        lSolution.setFont(new Font(settings.getFontType(), Font.PLAIN, settings.getFontSize()));
        lSolution.setForeground(Color.RED);
        lSolution.setVisible(false);

        lyricsPanel.add(lyricLabel, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 0;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.VERTICAL;
        }});
        lyricsPanel.add(lSolution, new GridBagConstraints() {{
            gridx = 0;
            gridy = 1;
            gridwidth = 1;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.NONE;
        }});

        centerPanel.add(lyricsPanel, BorderLayout.CENTER);

        // Guessing bar for guess input and submit button
        JPanel guessBar = new JPanel(new GridBagLayout());
        guessBar.setBackground(backgroundColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        switch(settings.getGtoTypeOfInput()) {
            case "Dropdown Menü":
                songDropdown = initializeJComboBox();
                songDropdown.addKeyListener(new SubmitKeyListener(this));
                guessBar.add(songDropdown, gbc);
                break;
            case "Suchleiste":
                LinkedList<DropdownItem> suggestions = dropdownListFromJson();
                songSearchBar = new AutoCompleteTextField(suggestions);
                songSearchBar.addKeyListener(new SubmitKeyListener(this));
                guessBar.add(songSearchBar, gbc);
                break;
        }

        // Hint button
        bHint = new JButton("Hinweis (Noch " + settings.getGtoHintCount() + ")");
        bHint.addActionListener(_ -> {
            if(!lSolution.isVisible()) {
                lSolution.setText("Hinweis: " + game.requestHint() + "...");
                lSolution.setVisible(true);
            }
        });
        bHint.setPreferredSize(new Dimension(150, 30));
        guessBar.add(bHint, new GridBagConstraints() {{
            gridx = 1;
            gridy = 0;
            weightx = 0;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Submit button
        bSubmit = new JButton("Raten");
        bSubmit.addActionListener(_ -> {
            submitButtonPressed();
        });
        bSubmit.setPreferredSize(new Dimension(150, 30));
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
        infoBar.setBackground(infobarColor);

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
        if(settings.isDarkMode()) {
            infoBar.setBackground(new Color(150, 40, 40));
        } else {
            infoBar.setBackground(Color.RED);
        }
        currentSongLabel.setText("Aktuelles Lied: " + game.getCurrentSong());
    }

    /**
     * Sets the info bar to green and clears the current song label
     * This is called when the user guesses the song correctly
     */
    public void infoBarRight() {
        currentSongLabel.setText("");
        if(settings.isDarkMode()) {
            infoBar.setBackground(new Color(40, 150, 40));
        } else {
            infoBar.setBackground(Color.GREEN);
        }
        infoBar.paintImmediately(infoBar.getVisibleRect()); // Update GUI immediately
        try {
            Thread.sleep(1000); // Pause for 1 second to show the green info bar
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        infoBar.setBackground(infobarColor); // Reset to default color
        lSolution.setVisible(false);
    }

    public void setHints(int pHints) {
        bHint.setText("Hinweis (Noch " + pHints + ")");
    }

    /**
     * Deactivates or activates all ways to do an input on the GUI
     * @param pInteractable If true all elements are interactable (enabled), if false all elements are not interactabe (disabled)
     */
    public void setInteractable(boolean pInteractable) {
        if(songDropdown != null)
        songDropdown.setEnabled(pInteractable);

        if(songSearchBar != null)
        songSearchBar.setEnabled(pInteractable);
        
        bHint.setEnabled(pInteractable);
        bSubmit.setEnabled(pInteractable);
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
        }
    }
}
