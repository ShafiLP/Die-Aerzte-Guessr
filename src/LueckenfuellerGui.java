import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.Image;

public class LueckenfuellerGui extends Gui {
    private final LueckenfuellerGame game;
    private SongTextWithGap currentSong;

    private JPanel infoBar;

    private JLabel[] healthBar;
    private JLabel lTimer;
    private JLabel lScore;
    private JLabel lSongName;
    private JLabel lAlbum;
    private JLabel lSolution;
    private JLabel lBefore;
    private JLabel lAfter;
    private JButton bHint;
    private JButton bSubmit;
    private JTextField tfInput;
    private Color backgroundColor;
    private Color infobarColor;

    //* For Multiplayer
    private final boolean multiplayer;
    private JLabel[] healthBarP2;   // Health bar for player 2
    private JLabel lPlayer;         // For displaying current active player

    /**
     * Constructor for LueckenfuellerGui
     * @param game reference to the game control
     * @param pSettings Settings object with all settings parameters
     * @param multiplayer boolean if multiplayer game is enabled
     * @param pCurrentSong SongTextWithGap object containing "before", "gap" and "after" Strings
     */
    public LueckenfuellerGui(LueckenfuellerGame game, Settings pSettings, boolean multiplayer, SongTextWithGap pCurrentSong) {
        this.game = game;
        this.multiplayer = multiplayer;
        currentSong = pCurrentSong;

        // Read settings
        settings = pSettings;
        lTimer = new JLabel("Timer: " + settings.getCtlTimeLimit() + "s", SwingConstants.RIGHT);
        lTimer.setFont(settings.getFont());
        healthBar = new JLabel[settings.getCtlLiveCount()];
        healthBarP2 = new JLabel[settings.getCtlLiveCount()];
        if (settings.isColourfulGuiEnabled()) {
            backgroundColor = new Color(220, 220, 255);
            infobarColor = new Color(100, 100, 230);
            if (settings.isDarkMode()) {
                backgroundColor = new Color(40, 40, 90);
                infobarColor = new Color(20, 20, 50);
            }
        } else {
            backgroundColor = Color.WHITE;
            infobarColor = Color.LIGHT_GRAY;
        }
        lScore = new JLabel(multiplayer ? "Frage Nr. 0" : "Puntzahl: 0", SwingConstants.RIGHT);
        lScore.setFont(settings.getFont());

        // Initialize input
        tfInput = new JTextField();
        tfInput.setHorizontalAlignment(JTextField.CENTER);
        tfInput.setFont(settings.getFont());
        tfInput.addKeyListener(new SubmitKeyListener(this));
        lBefore = new JLabel(currentSong.getBeforeGap(), SwingConstants.CENTER);
        lBefore.setFont(settings.getFont());
        lAfter = new JLabel(currentSong.getAfterGap(), SwingConstants.CENTER);
        lAfter.setFont(settings.getFont());
        lSolution = new JLabel();
        lSolution.setFont(settings.getFont());
        lSolution.setForeground(Color.RED);
        lSolution.setVisible(false);

        // JFrame settings
        this.setTitle("Lückenfüller");
        this.setSize(600, 320);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null); // Center window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon("images\\daLogo.png").getImage());

        // LyricPanel for GridLayout
        JPanel lyricPanel = new JPanel(new GridLayout(5, 1));
        lyricPanel.setBackground(backgroundColor);
        lyricPanel.add(lBefore);
        lyricPanel.add(tfInput);
        lyricPanel.add(lAfter);

        // Center panel for lyric display
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(backgroundColor);
        centerPanel.add(lyricPanel, new GridBagConstraints() {{
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

        // Main panel for center + heading
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        // Heading
        JPanel panHeading = new JPanel(new GridBagLayout());
        panHeading.setOpaque(false);
        JLabel lHeading = new JLabel("", SwingConstants.CENTER);
        ImageIcon icon = new ImageIcon("images\\Lueckenfueller.png");
        Image img = icon.getImage().getScaledInstance(300, 40, Image.SCALE_SMOOTH);
        lHeading.setIcon(new ImageIcon(img));
        panHeading.add(lHeading, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            fill = GridBagConstraints.NONE;
            anchor = GridBagConstraints.CENTER;
        }});
        lPlayer = new JLabel(multiplayer ? "Am Zug: " + game.getPlayerName(1) : "", SwingConstants.CENTER);
        lPlayer.setFont(settings.getFont());
        lPlayer.setOpaque(false);
        lPlayer.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Padding
        panHeading.add(lPlayer, new GridBagConstraints() {{
            gridx = 0;
            gridy = 1;
            fill = GridBagConstraints.HORIZONTAL;
            anchor = GridBagConstraints.CENTER;
        }});
        mainPanel.add(panHeading, BorderLayout.NORTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Lower bar for submit and hint button
        JPanel lowerPanel = new JPanel(new GridBagLayout());
        lowerPanel.setBackground(backgroundColor);
        bHint = new JButton(multiplayer ? "Hinweis (P1: " + game.getHintCount() + " / P2: " + game.getHintsCountP2() + ")" : "Hinweis (Noch " + settings.getCtlHintCount() + ")");
        bHint.setFont(settings.getFont());
        bHint.addActionListener(_ -> {
            lSolution.setText("Hinweis: " + game.requestHint() + "...");
            lSolution.setVisible(true);
        });

        bSubmit = new JButton("Raten");
        bSubmit.setFont(settings.getFont());
        bSubmit.addActionListener(_ -> {
            submitButtonPressed();
        });
        lowerPanel.add(bHint, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 1.0;
            gridwidth = 1;
            anchor = GridBagConstraints.LINE_START;
            fill = GridBagConstraints.HORIZONTAL;
        }});
        lowerPanel.add(bSubmit, new GridBagConstraints() {{
            gridx = 1;
            gridy = 0;
            weightx = 1.0;
            gridwidth = 1;
            anchor = GridBagConstraints.LINE_END;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Health bar in top left corner
        // Add health icons to array
        if (settings.isShowIconsEnabled()) {
            ImageIcon healthIcon = new ImageIcon("images\\health.png");
            Image healthImg = healthIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
            for (int i = 0; i < healthBar.length; i++) {
                healthBar[i] = new JLabel(new ImageIcon(healthImg));
                healthBar[i].setBounds(i * 25 + 2, 25, 25, 25);
            }
        } else {
            for(int i = 0; i < healthBar.length; i++) {
                healthBar[i] = new JLabel("❤️");
                healthBar[i].setBounds(i * 15 + 2, 25, 25, 25);
            }
        }
        // Copy if multiplayer is enabled
        if (multiplayer) {
            if (settings.isShowIconsEnabled()) {
                ImageIcon healthIcon = new ImageIcon("images\\health.png");
                Image healthImg = healthIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
                for (int i = 0; i < healthBarP2.length; i++) {
                    healthBarP2[i] = new JLabel(new ImageIcon(healthImg));
                    healthBarP2[i].setBounds(i * 25 + 2, 25, 25, 25);
                }
            } else {
                for(int i = 0; i < healthBarP2.length; i++) {
                    healthBarP2[i] = new JLabel("❤️");
                    healthBarP2[i].setBounds(i * 15 + 2, 25, 25, 25);
                }
            }
        }
        // Add icons to health panels
        JPanel panHealthP1 = new JPanel(new GridLayout(1, healthBar.length));
        for (JLabel l : healthBar) {
            panHealthP1.add(l);
        }
        panHealthP1.setOpaque(false);
        JPanel panHealthP2 = new JPanel();
        if (multiplayer) {
            panHealthP2 = new JPanel(new GridLayout(1, healthBarP2.length));
            for (JLabel l : healthBarP2) {
                panHealthP2.add(l);
            }
            panHealthP2.setOpaque(false);

        }
        // Add to one JPanel
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

        JPanel panUpperLeft = new JPanel(new GridBagLayout());
        panUpperLeft.setOpaque(false);
        panUpperLeft.add(panHealth, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 1.0;
            anchor = GridBagConstraints.LINE_START;
            fill = GridBagConstraints.NONE;
        }});

        // Song name and album cover in the upper middle
        JPanel mPanel = new JPanel(new GridBagLayout());
        if(settings.isCtlShowIconsEnabled()) {
            ImageIcon albumIcon = new ImageIcon((new ImageIcon(currentSong.getAlbum())).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
            lAlbum = new JLabel(albumIcon);
        } else {
            lAlbum = new JLabel();
        }
        
        lSongName = new JLabel(" " + currentSong.getSongName(), SwingConstants.CENTER);
        lSongName.setFont(new Font(settings.getFontType(), Font.PLAIN, 16));
        mPanel.add(lAlbum, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 0;
            anchor = GridBagConstraints.LINE_START;
            fill = GridBagConstraints.HORIZONTAL;
        }});
        mPanel.add(lSongName, new GridBagConstraints() {{
            gridx = 1;
            gridy = 0;
            weightx = 0;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.HORIZONTAL;
        }});
        mPanel.setOpaque(false);
        
        // Deactivate visibility if hardmode is activated
        if(settings.isCtlHardmodeEnabled()) {
            lAlbum.setVisible(false);
            lSongName.setVisible(false);
        }

        // Timer and Score in upper right corner
        JPanel urPanel = new JPanel(new GridLayout(2, 1));
        urPanel.add(lTimer);
        urPanel.add(lScore);
        urPanel.setOpaque(false);

        // Info bar for health, current song, timer, etc.
        infoBar = new JPanel(new GridBagLayout());
        infoBar.setBorder(new EmptyBorder(5, 5, 5, 5));
        infoBar.setBackground(infobarColor);
        infoBar.add(panUpperLeft, new GridBagConstraints() {{
            gridx = 0;
            gridy = 0;
            weightx = 1.0;
            anchor = GridBagConstraints.LINE_START;
            fill = GridBagConstraints.HORIZONTAL;
        }});
        infoBar.add(mPanel, new GridBagConstraints() {{
            gridx = 1;
            gridy = 0;
            weightx = 1.0;
            anchor = GridBagConstraints.CENTER;
            fill = GridBagConstraints.NONE;
        }});
        infoBar.add(urPanel, new GridBagConstraints() {{
            gridx = 2;
            gridy = 0;
            weightx = 1.0;
            anchor = GridBagConstraints.LINE_END;
            fill = GridBagConstraints.NONE;
        }});
        
        // Add all components to the main frame
        this.add(infoBar, BorderLayout.NORTH);
        this.add(lowerPanel, BorderLayout.SOUTH);
        this.add(mainPanel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    /**
     * Handles the action when the submit putton gets pressed
     */
    public void submitButtonPressed() {
        game.submitPressed(tfInput.getText());
    }

    /**
     * Hides the last visible health point
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
     * @param pTime time left in seconds
     */
    public void setTimerLabel(int pTime) {
        lTimer.setText("Timer: " + pTime + "s");
    }

    /**
     * Sets the text on the timer label
     * @param pTime text on timer label
     */
    public void setTimerLabel(String pTime) {
        lTimer.setText(pTime);
    }

    /**
     * Sets the text on the score label
     * @param pScore current score
     */
    public void setScoreLabel(int pScore) {
        lScore.setText(multiplayer ? "Frage Nr." + pScore : "Punktzahl : " + pScore);
    }

    public void setHints(String pHintsLeft) {
        bHint.setText("Hinweis (Noch " + pHintsLeft + ")");
    }

    /**
     * Changes the label that displays the current active player
     * @param pName Name of active player
     */
    public void setActivePlayer(String pName) {
        lPlayer.setText("Am Zug: " + pName);
    }

    /**
     * Sets the text before and after the song text gap
     * @param pBefore text before the gap
     * @param pAfter text after the gap
     */
    public void setNewSongText(String pBefore, String pAfter) {
        lBefore.setText(pBefore);
        lAfter.setText(pAfter);
        tfInput.setText(""); // Clear the current text
    }

    /**
     * Sets the current song indicator and matching album icon on the info bar
     * @param pSong name of the current song
     * @param pAlbum path to the album image of the current song
     */
    public void setSongAndAlbum(String pSong, String pAlbum, String pGap) {
        if(settings.isCtlShowIconsEnabled()) {
            ImageIcon albumIcon = new ImageIcon((new ImageIcon(pAlbum)).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
            lAlbum.setIcon(albumIcon);
        }
        currentSong.setSongName(pSong);
        currentSong.setAlbum(pAlbum);
        currentSong.setGap(pGap);
        
        lSongName.setText(" " + pSong);
        lSolution.setText("Lösung: " + pGap);
    }

    /**
     * Sets the info bar to red
     * This is called when the user makes an incorrect guess
     */
    public void setInfoBarRed() {
        if(settings.isDarkMode()) {
            infoBar.setBackground(new Color(150, 40, 40));
        } else {
            infoBar.setBackground(Color.RED);
        }
        lSongName.setVisible(true);
        lSolution.setText("Lösung: " + currentSong.getGap());
        lSolution.setVisible(true);

        // Show album name and icon if hardmode is activated
        lSongName.setVisible(true);
        lAlbum.setVisible(true);
    }

    /**
     * Sets the info bar to green and clears the solution if user guessed wrong before
     * This is called when the user guesses the gap incorrectly
     */
    public void setInfoBarGreen() {
        lSolution.setVisible(false);
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
        if(settings.isCtlHardmodeEnabled()) {
            lSongName.setVisible(false);
            lAlbum.setVisible(false);
        }
    }

    /**
     * Deactivates or activates ways to do an input on the GUI
     * @param pInteractable If true all elements are interactable (enabled), if false all elements are not interactable (disabled)
     */
    public void setInteractable(boolean pInteractable) {
        tfInput.setEnabled(pInteractable);
        bHint.setEnabled(pInteractable);
        bSubmit.setEnabled(pInteractable);
    }
}
