import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class GameGui extends JFrame{
    private JPanel center;
    private JLabel lyricLabel = new JLabel();

    /**
     * Constructor for the GameGui class
     */
    public GameGui() {
        this.setTitle("ÄrzteGuessr");
        this.setLayout(new BorderLayout());
        this.pack();
        this.setSize(600, 200);
        this.setLocationRelativeTo(null); // Center the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Creates a GUI for the game "Guess The Origin"
     */
    public void guessTheOriginWindow(GuessTheOriginGame game, String pLyric) {
        center = new JPanel();
        center.setLayout(new GridBagLayout());

        //Dropdown menu with all the songs from the file
        String[] allSongs = readSongsFromFile("data\\songs.txt"); 
        JComboBox<String> songDropdown = new JComboBox<>(allSongs);

        //Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(_ -> {
            if(songDropdown.getSelectedItem().equals(game.getCurrentSong())) {
                game.songGuessed();
            } else {
                System.out.println("Wrong!");
            }
        });

        // Top bar for dropdown menu and submit button
        JPanel topBar = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        topBar.add(songDropdown, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        topBar.add(submitButton, gbc);

        //Lyrics to guess in the center
        lyricLabel.setText(pLyric);
        lyricLabel.setHorizontalAlignment(SwingConstants.CENTER);
        center.add(lyricLabel);
        
        this.add(topBar, BorderLayout.NORTH);
        this.add(center, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void guessTheOriginUpdate(String pLyric) {
        lyricLabel.setText(pLyric);
    }

    private String[] readSongsFromFile(String filename) {
        //Create a dropdown menu with all the songs from the file
        LinkedList<String> songs = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                songs.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return songs.toArray(new String[0]);
    }
}
