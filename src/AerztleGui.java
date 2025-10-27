import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class AerztleGui extends Gui {
    private AerztleGame game;
    private final int TRIES;
    private JLabel[][] lTable;
    private Color backgroundColor;
    private Color infobarColor;

    private AutoCompleteTextField songSearchBar;
    private JComboBox<DropdownItem> songDropdown;
    private JButton bSubmit;

    public AerztleGui(AerztleGame pGame, Settings pSettings) {
        game = pGame;
        settings = pSettings;

        // Read settings
        TRIES = pSettings.getAeTries();
        if(settings.isColourfulGuiEnabled()) {
            backgroundColor = new Color(220, 255, 220);
            infobarColor = new Color(100, 230, 100);
        } else {
            backgroundColor = Color.WHITE;
            infobarColor = Color.LIGHT_GRAY;
        }

        // JFrame settings
        this.setTitle("Ärztle");
        this.setSize(900, 700);
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null); // Center the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon("images\\daLogo.png").getImage());

        // Top panel
        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(infobarColor);

        // Heading
        JLabel lHeading = new JLabel("", SwingConstants.CENTER);
        ImageIcon icon = new ImageIcon("images\\aerztle.png");
        Image img = icon.getImage().getScaledInstance(150, 40, Image.SCALE_SMOOTH);
        lHeading.setIcon(new ImageIcon(img));
        topPanel.add(lHeading);

        // JPanel with songs to guess
        JPanel guessingPanel = new JPanel(new GridBagLayout());
        guessingPanel.setBackground(infobarColor);
        guessingPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40)); // Padding

        // Initialize type of input
        switch(settings.getAeTypeOfInput()) {
            case "Suchleiste":
                LinkedList<DropdownItem> suggestions = dropdownListFromJson();
                songSearchBar = new AutoCompleteTextField(suggestions);
                songSearchBar.addKeyListener(new SubmitKeyListener(this));
                guessingPanel.add(songSearchBar, new GridBagConstraints() {{
                    gridx = 0;
                    gridy = 0;
                    weightx = 0.8;
                    anchor = GridBagConstraints.LINE_START;
                    fill = GridBagConstraints.HORIZONTAL;
                }});
                break;
            case "Dropdown Menü":
                songDropdown = initializeJComboBox();
                songDropdown.addKeyListener(new SubmitKeyListener(this));
                guessingPanel.add(songDropdown, new GridBagConstraints() {{
                    gridx = 0;
                    gridy = 0;
                    weightx = 0.8;
                    anchor = GridBagConstraints.LINE_START;
                    fill = GridBagConstraints.HORIZONTAL;
                }});
                break;
        }

        // Submit button
        bSubmit = new JButton("Raten");
        bSubmit.addActionListener(_ -> {
            submitButtonPressed();
        });
        guessingPanel.add(bSubmit, new GridBagConstraints() {{
            gridx = 1;
            gridy = 0;
            weightx = 0.2;
            anchor = GridBagConstraints.LINE_END;
            fill = GridBagConstraints.HORIZONTAL;
        }});

        // Disclaimer label for wrong information
        JLabel lDisclaimer = new JLabel("*Manche Informationen könnten Fehler aufweisen!", SwingConstants.CENTER);
        lDisclaimer.setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));
        guessingPanel.add(lDisclaimer, new GridBagConstraints() {{
            gridx = 0;
            gridy = 1;
            gridwidth = 2;
            weightx = 1.0;
            weighty = 1.0;
            anchor = GridBagConstraints.PAGE_END;
            fill = GridBagConstraints.HORIZONTAL;
            ipady = 10;
        }});
        topPanel.add(guessingPanel);

        // JPanel with categories
        JPanel tablePanel = new JPanel(new GridLayout(TRIES+1, 8));
        tablePanel.setBackground(backgroundColor);

        lTable = new JLabel[TRIES+1][8];
        for(int x = 0; x < lTable.length; x++) {
            for(int y = 0; y < lTable[0].length; y++) {
                lTable[x][y] = new JLabel("", SwingConstants.CENTER);
                lTable[x][y].setFont(new Font(settings.getFontType(), Font.BOLD, settings.getFontSize()));
            }
        }
        lTable[0][0].setText("Name");
        lTable[0][1].setText("Album");
        lTable[0][2].setText("Erscheinungsjahr");
        lTable[0][3].setText("Spotify Streams");
        lTable[0][4].setText("Dauer");
        lTable[0][5].setText("Live-Auftritte");
        lTable[0][6].setText("Sänger");
        lTable[0][7].setText("Single");

        for(int x = 0; x < lTable.length; x++) {
            for(int y = 0; y < lTable[0].length; y++) {
                tablePanel.add(lTable[x][y]);
                lTable[x][y].setOpaque(true);
                lTable[x][y].setBackground(backgroundColor);
                lTable[x][y].setBorder(new LineBorder(getForeground(), 1));
            }
        }

        // Show GUI
        this.add(topPanel, BorderLayout.NORTH);
        this.add(tablePanel, BorderLayout.CENTER);
        this.setVisible(true);
    }

    /**
     * Called when either the submit button or the enter key gets pressed
     * Calls the submit button pressed event inside the game controll class
     */
    public void submitButtonPressed() {
        if(settings.getAeTypeOfInput().equals("Suchleiste")) {
            System.out.println(songSearchBar.getText());
            game.submitButtonPressed(songSearchBar.getText());
            songSearchBar.setText("");
            songSearchBar.requestFocusInWindow();
        } else {
            DropdownItem dmSelected = (DropdownItem) songDropdown.getSelectedItem();
            game.submitButtonPressed(dmSelected.getDropdownText().trim());
        }
    }

    /**
     * Sets text and colour of a JLabel inside the song name column
     * @param pIndex index of the row to repaint
     * @param pColor new colour of the JLabel
     * @param pText new text of the JLabel
     */
    public void paintSongName(int pIndex, Color pColor, String pText) {
        lTable[pIndex][0].setBackground(pColor);
        lTable[pIndex][0].setText(pText);
    }

    /**
     * Sets album cover and colour for a JLabel inside the album column
     * If album is "/" text will be set to "Keins" instead of setting album icon
     * @param pIndex index of the row to repaint
     * @param pColor new colour of the JLabel
     * @param pText name of the album to put the icon on the JLabel. If equals "/", text will be set to "Keins"
     */
    public void paintAlbum(int pIndex, Color pColor, String pText) {
        lTable[pIndex][1].setBackground(pColor);
        if(pText.equals("Single/B-Seite")) {
            lTable[pIndex][1].setText("Single/B-Seite");
            return;
        }
        if(settings.isShowIconsEnabled()) {
            ImageIcon albumIcon = new ImageIcon("images\\" + pText + ".png");
            Image albumImg = albumIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            lTable[pIndex][1].setIcon(new ImageIcon(albumImg));
        } else {
            String album;
            switch(pText) {
                case "debil":
                    album = "Debil/Devil";
                    break;
                case "schatten":
                    album = "Im Schatten Der Ärzte";
                    break;
                case "aerzte":
                    album = "Die Ärzte";
                    break;
                case "abAchtzehn":
                    album = "Ab 18";
                    break;
                case "hoehepunkte":
                    album = "13 Höhepunkte";
                    break;
                case "wahrheit":
                    album = "Das Ist Nicht Die Ganze Wahrheit";
                    break;
                case "frueher":
                    album = "Die Ärzte Früher";
                    break;
                case "bestie":
                    album = "Die Bestie In Menschengestalt";
                    break;
                case "planet":
                    album = "Planet Punk";
                    break;
                case "frisur":
                    album = "Le Frisur";
                    break;
                case "dreizehn":
                    album = "13";
                    break;
                case "spendierhosen":
                    album = "Runter Mit Den Spendierhosen, Unsichtbarer!";
                    break;
                case "geraeusch":
                    album = "Geräusch";
                    break;
                case "jazz":
                    album = "Jazz Ist Anders";
                    break;
                case "auch":
                    album = "auch";
                    break;
                case "hell":
                    album = "HELL";
                    break;
                case "dunkel":
                    album = "DUNKEL";
                    break;
                default:
                    album = "ERROR";
                    break;
            }
            lTable[pIndex][1].setText(album);
        }
        
    }

    public void paintReleaseYear(int pIndex, Color pColor, String pText, Boolean pMoreOrLess) {
        lTable[pIndex][2].setBackground(pColor);
        lTable[pIndex][2].setText(pText);
        if(settings.isShowIconsEnabled() & pMoreOrLess != null) {
            String imagePath = (pMoreOrLess == true) ? "images\\arrowUp.png" : "images\\arrowDown.png";
            ImageIcon arrowIcon = new ImageIcon(imagePath);
            Image arrowImg = arrowIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            lTable[pIndex][2].setIcon(new ImageIcon(arrowImg));
        } else if(pMoreOrLess != null){
            String moreOrLessText = (pMoreOrLess == true) ? " (zu wenig)" : " (zu viel)";
            lTable[pIndex][2].setText(lTable[pIndex][2].getText() + moreOrLessText);
        }
    }

    public void paintStreams(int pIndex, Color pColor, String pText, Boolean pMoreOrLess) {
        lTable[pIndex][3].setBackground(pColor);
        lTable[pIndex][3].setText(pText);
        if(settings.isShowIconsEnabled() & pMoreOrLess != null) {
            String imagePath = (pMoreOrLess == true) ? "images\\arrowUp.png" : "images\\arrowDown.png";
            ImageIcon arrowIcon = new ImageIcon(imagePath);
            Image arrowImg = arrowIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            lTable[pIndex][3].setIcon(new ImageIcon(arrowImg));
        } else if(pMoreOrLess != null){
            String moreOrLessText = (pMoreOrLess == true) ? " (zu wenig)" : " (zu viel)";
            lTable[pIndex][3].setText(lTable[pIndex][3].getText() + moreOrLessText);
        }
    }

    public void paintDuration(int pIndex, Color pColor, String pText, Boolean pMoreOrLess) {
        lTable[pIndex][4].setBackground(pColor);
        lTable[pIndex][4].setText(pText);
        if(settings.isShowIconsEnabled() & pMoreOrLess != null) {
            String imagePath = (pMoreOrLess == true) ? "images\\arrowUp.png" : "images\\arrowDown.png";
            ImageIcon arrowIcon = new ImageIcon(imagePath);
            Image arrowImg = arrowIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            lTable[pIndex][4].setIcon(new ImageIcon(arrowImg));
        } else if(pMoreOrLess != null){
            String moreOrLessText = (pMoreOrLess == true) ? " (zu wenig)" : " (zu viel)";
            lTable[pIndex][4].setText(lTable[pIndex][4].getText() + moreOrLessText);
        }
    }

    public void paintLivePlays(int pIndex, Color pColor, String pText, Boolean pMoreOrLess) {
        lTable[pIndex][5].setBackground(pColor);
        lTable[pIndex][5].setText(pText);
        if(settings.isShowIconsEnabled() & pMoreOrLess != null) {
            String imagePath = (pMoreOrLess == true) ? "images\\arrowUp.png" : "images\\arrowDown.png";
            ImageIcon arrowIcon = new ImageIcon(imagePath);
            Image arrowImg = arrowIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            lTable[pIndex][5].setIcon(new ImageIcon(arrowImg));
        } else if(pMoreOrLess != null){
            String moreOrLessText = (pMoreOrLess == true) ? " (zu wenig)" : " (zu viel)";
            lTable[pIndex][5].setText(lTable[pIndex][5].getText() + moreOrLessText);
        }
    }

    public void paintSinger(int pIndex, Color pColor, String pText) {
        lTable[pIndex][6].setBackground(pColor);
        lTable[pIndex][6].setText(pText);
    }

    public void paintSingle(int pIndex, Color pColor, String pText) {
        lTable[pIndex][7].setBackground(pColor);
        lTable[pIndex][7].setText(pText);
    }

    /**
     * Resets all cells of the table
     */
    public void resetGui() {
        for(int x = 1; x < lTable.length; x++) {
            for(int y  = 0; y < lTable[0].length; y++) {
                lTable[x][y].setText("");
                lTable[x][y].setIcon(null);
                lTable[x][y].setBackground(Color.WHITE);
            }
        }
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
        
        bSubmit.setEnabled(pInteractable);
    }
}
