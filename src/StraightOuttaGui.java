import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

import java.util.LinkedList;

/**
 * Class for the GUI of the game "Guess The Origin"
 */
class StraightOuttaGui extends Gui {
    private StraightOuttaGame game;
    private final boolean multiplayer;
    private JPanel centerPanel;
    private JPanel infoBar;
    private JComboBox<DropdownItem> songDropdown;
    private AutoCompleteTextField songSearchBar;
    private JLabel lyricLabel = new JLabel();
    private JLabel timerLabel;
    private JLabel currentSongLabel = new JLabel();
    private JLabel scoreLabel;
    private JLabel[] healthBar;
    private JLabel[] healthBarP2;
    private JLabel lSolution;
    private JButton bHint;
    private JButton bSubmit;
    private Color backgroundColor;
    private Color infobarColor;

    //* For Multiplayer
    private JLabel lPlayer; // Label displaying current player

    /**
     * Constructor for the GTGGui class
     * Creates a GUI for the game "Guess The Origin"
     */
    public StraightOuttaGui(StraightOuttaGame pGame, String pLyric, Settings pSettings, boolean multiplayer) {
        game = pGame;
        this.multiplayer = multiplayer;

        // Read settings
        settings = pSettings;
        timerLabel = new JLabel("Timer: " + settings.getGtoTimeLimit() + "s");
        healthBar = new JLabel[settings.getGtoLiveCount()];
        healthBarP2 = new JLabel[settings.getGtoLiveCount()];
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
        scoreLabel = new JLabel(multiplayer ? "Frage Nr. 0" : "Puntzahl: 0", SwingConstants.RIGHT);

        // JFrame settings
        this.setTitle("Straight Outta...");
        this.setLayout(new BorderLayout());
        this.setSize(600, 300);
        this.setLocationRelativeTo(null); // Center window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon("images\\daLogo.png").getImage());

        currentSongLabel.setFont(new Font(settings.getFontType(), Font.PLAIN, 12));

        // Health bar
        if (multiplayer) {
            if(settings.isShowIconsEnabled()) {
                ImageIcon healthIcon = new ImageIcon("images\\health.png");
                Image healtImg = healthIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                for(int i = 0; i < healthBar.length; i++) {
                    healthBar[i] = new JLabel(new ImageIcon(healtImg));
                    healthBar[i].setBounds(i * 25 + 2, 25, 25, 25);
                    healthBarP2[i] = new JLabel(new ImageIcon(healtImg));
                    healthBarP2[i].setBounds(i * 25 + 2, 25, 25, 25);
                }
            } else {
                for(int i = 0; i < healthBar.length; i++) {
                    healthBar[i] = new JLabel("❤️");
                    healthBar[i].setBounds(i * 15 + 2, 25, 25, 25);
                    healthBarP2[i] = new JLabel("️❤️️");
                    healthBarP2[i].setBounds(i * 15 + 2, 25, 25, 25);
                }
            }
        } else {
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
        }

        // Create GUI center, where heading and panel with lyrics will be displayed
        centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(backgroundColor);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        // Heading
        JPanel panHeading = new  JPanel(new GridBagLayout());
        panHeading.setOpaque(false);
        JLabel lHeading = new JLabel("", SwingConstants.CENTER);
        ImageIcon icon = new ImageIcon("images\\StraightOutta.png");
        Image img = icon.getImage().getScaledInstance(280, 40, Image.SCALE_SMOOTH);
        lHeading.setIcon(new ImageIcon(img));
        panHeading.add(lHeading, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            fill = GridBagConstraints.NONE;
            anchor = GridBagConstraints.CENTER;
        }});
        lPlayer = new JLabel(multiplayer ? "Am Zug: " + game.getPlayerName(1) : "", SwingConstants.CENTER);
        lPlayer.setFont(new Font(settings.getFontType(), Font.PLAIN, settings.getFontSize()));
        lPlayer.setForeground(Color.RED);
        lPlayer.setOpaque(false);
        panHeading.add(lPlayer, new  GridBagConstraints() {{
            gridx = 0;
            gridy = 1;
            fill = GridBagConstraints.HORIZONTAL;
            anchor = GridBagConstraints.CENTER;
        }});
        centerPanel.add(panHeading, BorderLayout.NORTH);

        // Lyrics label with lyrics and hint
        JPanel lyricsPanel = new JPanel();
        lyricsPanel.setLayout(new GridBagLayout());

        // Lyrics to guess in the center
        lyricLabel = new JLabel("„" + pLyric + "“");
        lyricLabel.setFont(new Font(settings.getFontType(), Font.PLAIN, settings.getFontSize()));
        lyricsPanel.setOpaque(false);

        lSolution = new JLabel();
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
        bHint = new JButton(multiplayer ? "Hinweis (P1: " + game.getHintCount() + " / P2: " + game.getHintsCountP2() + ")" : "Hinweis (Noch " + settings.getGtoHintCount() + ")");
        bHint.addActionListener(_ -> {
            if(!lSolution.isVisible()) {
                lSolution.setText("Hinweis: " + game.requestHint() + "...");
                lSolution.setVisible(true);
            }
        });
        bHint.setPreferredSize(new Dimension(150, 30));
        bHint.setFont(new Font(settings.getFontType(), Font.PLAIN, settings.getFontSize()));
        guessBar.add(bHint, new GridBagConstraints() {{
            gridx = 1;
            gridy = 0;
            weightx = 0.3;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Submit button
        bSubmit = new JButton("Raten");
        bSubmit.setFont(new Font(settings.getFontType(), Font.PLAIN, settings.getFontSize()));
        bSubmit.addActionListener(_ -> {
            submitButtonPressed();
        });
        bSubmit.setPreferredSize(new Dimension(150, 30));
        guessBar.add(bSubmit, new GridBagConstraints() {{
            gridx = 2;
            gridy = 0;
            weightx = 0.1;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Health bar in the top left corner
        JPanel panHealthP1 = new JPanel(new GridLayout(1, healthBar.length));
        for (JLabel l : healthBar) {
            panHealthP1.add(l);
        }
        panHealthP1.setOpaque(false);
        JPanel panHealthP2 = new JPanel();
        if (multiplayer) {
            panHealthP2 = new JPanel(new GridLayout(1, healthBarP2.length));
            for (JLabel l :  healthBarP2) {
                panHealthP2.add(l);
            }
            panHealthP2.setOpaque(false);
        }

        JPanel panHealth;
        if (multiplayer) {
            panHealth = new JPanel(new GridBagLayout());
            panHealth.setOpaque(false);
            GridBagConstraints gbcHealth = new GridBagConstraints() {{
                gridx = 0;
                gridy = 0;
                fill = GridBagConstraints.NONE;
                anchor = GridBagConstraints.LINE_START;
            }};
            panHealth.add(new JLabel(game.getPlayerName(1) + ": ") {{
                setFont(new Font(settings.getFontType(), Font.PLAIN, settings.getFontSize()));
            }}, gbcHealth);
            gbcHealth.gridx++;
            panHealth.add(panHealthP1, gbcHealth);
            gbcHealth.gridy = 1;
            gbcHealth.gridx = 0;
            panHealth.add(new JLabel(game.getPlayerName(2) + ": ") {{
                setFont(new Font(settings.getFontType(), Font.PLAIN, settings.getFontSize()));
            }}, gbcHealth);
            gbcHealth.gridx++;
            panHealth.add(panHealthP2, gbcHealth);
        } else {
            panHealth = new JPanel(new GridLayout(1, healthBar.length));
            panHealth.setOpaque(false);
            for (JLabel l : healthBar) {
                panHealth.add(l);
            }
        }


        // Upper left corner of infoBar
        JPanel upperLeft = new JPanel(new GridBagLayout());
        upperLeft.setOpaque(false);

        upperLeft.add(panHealth, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 1.0;
            anchor = GridBagConstraints.LINE_START;
            fill = GridBagConstraints.NONE;
        }});

        // Upper right corner of infoBar
        JPanel upperRight = new JPanel(new GridBagLayout());
        upperRight.setOpaque(false);

        scoreLabel.setFont(new Font(settings.getFontType(), Font.PLAIN, settings.getFontSize()));
        upperRight.add(scoreLabel, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 1.0;
            gbc.anchor = GridBagConstraints.LINE_END;
            gbc.fill = GridBagConstraints.NONE;
        }});

        timerLabel.setFont(new Font(settings.getFontType(), Font.PLAIN, settings.getFontSize()));
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
            fill = GridBagConstraints.HORIZONTAL;
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
        if (multiplayer) {
            if (game.getActivePlayer() == 1) {
                for (int i = healthBar.length - 1; i >= 0; i--) {
                    if(healthBar[i].isVisible()) {
                        healthBar[i].setVisible(false);
                        return;
                    }
                }
            } else {
                for (int i = healthBarP2.length - 1; i >= 0; i--) {
                    if(healthBarP2[i].isVisible()) {
                        healthBarP2[i].setVisible(false);
                        return;
                    }
                }
            }
        } else {
            for (int i = healthBar.length - 1; i >= 0; i--) {
                if (healthBar[i].isVisible()) {
                    healthBar[i].setVisible(false);
                    return;
                }
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
        scoreLabel.setText(multiplayer ? "Frage Nr." + pScore : "Punktzahl : " + pScore);
    }

    /**
     * Sets the info bar to red and displays the current song name
     * This is called when the user makes an incorrect guess
     */
    public void infoBarWrong() {
        if (settings.isDarkMode()) {
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
        if (settings.isDarkMode()) {
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

    /**
     * Changes text displayed on hints button
     * @param pHints New text to display on hints button
     */
    public void setHints(String pHints) {
        bHint.setText(multiplayer ? "Hinweis (" + pHints + ")" : "Hinweis (Noch: " + pHints + ")");
    }

    /**
     * Changes the label that displays the current active player
     * @param pName Name of active player
     * @param pPlayer Number of active player
     */
    public void setActivePlayer(String pName) {
        lPlayer.setText("Am Zug: " + pName);
    }

    /**
     * Deactivates or activates ways to do an input on the GUI
     * @param pInteractable If true all elements are interactable (enabled), if false all elements are not interactabe (disabled)
     */
    public void setInteractable(boolean pInteractable) {
        if (songDropdown != null) songDropdown.setEnabled(pInteractable);

        if (songSearchBar != null) songSearchBar.setEnabled(pInteractable);
        
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
