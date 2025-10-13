import java.awt.Color;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

public class AerztleGame {
    private Settings settings;

    private AerztleGui gui;
    private AerztleObject randomAerztleObject;
    private LinkedList<AerztleObject> aerztleObjects;

    private final int TRIES;
    private int currentGuess = 1;

    public AerztleGame(Settings pSettings) {
        settings = pSettings;
        gui = new AerztleGui(this, settings);

        // Load from settings
        TRIES = pSettings.getAeTries();

        aerztleObjects = new LinkedList<>();
        aerztleObjects = readSongsFromJson("data\\aerztleData.json", aerztleObjects);
        if(settings.isFarinEnabled())
        aerztleObjects = readSongsFromJson("data\\aerztleDataFarin.json", aerztleObjects);

        int randomIndex = (int) (Math.random() * aerztleObjects.size());
        randomAerztleObject =  aerztleObjects.get(randomIndex);

        // DEBUG
        System.out.println(randomAerztleObject.getSongName());
        System.out.println(randomAerztleObject.getAlbum());
        System.out.println(randomAerztleObject.getReleaseYear());
        System.out.println(randomAerztleObject.getStreamsAsText());
        System.out.println(randomAerztleObject.getDurationMinutes());
        System.out.println(randomAerztleObject.getDurationSeconds());
        System.out.println(randomAerztleObject.getLivePlays());
        System.out.println(randomAerztleObject.getSinger());
        System.out.println(randomAerztleObject.isSingle());
    }

    private void compSongName(AerztleObject pGuess, AerztleObject pSolution) {
        if(pGuess.getSongName().equals(pSolution.getSongName())){
            gui.paintSongName(currentGuess, Color.GREEN, pGuess.getSongName());
        } else {
            gui.paintSongName(currentGuess, Color.RED, pGuess.getSongName());
        }
    }

    private void compAlbum(AerztleObject pGuess, AerztleObject pSolution) {
        if(pGuess.getAlbum().equals(pSolution.getAlbum())){
            gui.paintAlbum(currentGuess, Color.GREEN, pGuess.getAlbum());
        } else {
            gui.paintAlbum(currentGuess, Color.RED, pGuess.getAlbum());
        }
    }

    private void compReleaseYear(AerztleObject pGuess, AerztleObject pSolution) {
        if(pGuess.getReleaseYear() == pSolution.getReleaseYear()) {
            gui.paintReleaseYear(currentGuess, Color.GREEN, pGuess.getReleaseYear() + "");
        } else {
            if(pGuess.getReleaseYear() - pSolution.getReleaseYear() > 0) {
                gui.paintReleaseYear(currentGuess, Color.RED, pGuess.getReleaseYear() + " ⬇️");
            } else {
                gui.paintReleaseYear(currentGuess, Color.RED, pGuess.getReleaseYear() + " ⬆️");
            }
        }
    }

    private void compStreams(AerztleObject pGuess, AerztleObject pSolution) {
        NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
        String streamsMaximum = nf.format(pGuess.getStreamsAsInteger() + 100000);
        if(pGuess.getStreamsAsInteger() == pSolution.getStreamsAsInteger()) {
            gui.paintStreams(currentGuess, Color.GREEN, pGuess.getStreamsAsInteger() == 0 ? "< 100.000" : pGuess.getStreamsAsText() + " - " + streamsMaximum);
        } else {
            if(pGuess.getStreamsAsInteger() - pSolution.getStreamsAsInteger() > 0) {
                gui.paintStreams(currentGuess, pGuess.getStreamsAsInteger() - 100000 == pSolution.getStreamsAsInteger() ? Color.YELLOW : Color.RED,
                pGuess.getStreamsAsInteger() == 0 ? "< 100.000" + " ⬇️" : pGuess.getStreamsAsText() + " - " + streamsMaximum + " ⬇️");
            } else {
                gui.paintStreams(currentGuess, pGuess.getStreamsAsInteger() + 100000 == pSolution.getStreamsAsInteger() ? Color.YELLOW : Color.RED,
                pGuess.getStreamsAsInteger() == 0 ? "< 100.000" + " ⬆️" : pGuess.getStreamsAsText() + " - " + streamsMaximum + " ⬆️");
            }
        }
    }

    private void compDuration(AerztleObject pGuess, AerztleObject pSolution) {
        if(pGuess.getFullDurationInSecs() == pSolution.getFullDurationInSecs()) {
            gui.paintDuration(currentGuess, Color.GREEN, String.format("%d:%02d", pGuess.getDurationMinutes(), pGuess.getDurationSeconds()));
        } else {
            if(pGuess.getFullDurationInSecs() - pSolution.getFullDurationInSecs() > 0) {
                gui.paintDuration(currentGuess, pGuess.getFullDurationInSecs() - pSolution.getFullDurationInSecs() < 5 ? Color.YELLOW : Color.RED, String.format("%d:%02d", pGuess.getDurationMinutes(), pGuess.getDurationSeconds()) + " ⬇️");
            } else {
                gui.paintDuration(currentGuess, pSolution.getFullDurationInSecs() - pGuess.getFullDurationInSecs() < 5 ? Color.YELLOW : Color.RED, String.format("%d:%02d", pGuess.getDurationMinutes(), pGuess.getDurationSeconds()) + " ⬆️");
            }
        }
    }

    private void compLivePlays(AerztleObject pGuess, AerztleObject pSolution) {
        if(pGuess.getLivePlays() == pSolution.getLivePlays()) {
            gui.paintLivePlays(currentGuess, Color.GREEN, pGuess.getLivePlays() + "");
        } else {
            if(pGuess.getLivePlays() - pSolution.getLivePlays() > 0) {
                gui.paintLivePlays(currentGuess, pGuess.getLivePlays() - pSolution.getLivePlays() < 5 ? Color.YELLOW : Color.RED, pGuess.getLivePlays() + " ⬇️");
            } else {
                gui.paintLivePlays(currentGuess, pSolution.getLivePlays() - pGuess.getLivePlays() < 5 ? Color.YELLOW : Color.RED, pGuess.getLivePlays() + " ⬆️");
            }
        }
    }

    private void compSinger(AerztleObject pGuess, AerztleObject pSolution) {
        if(pGuess.getSinger().equals(pSolution.getSinger())) {
            gui.paintSinger(currentGuess, Color.GREEN, pGuess.getSinger() + "");
        } else {
            if(pSolution.getSinger().contains(pGuess.getSinger())) {
                gui.paintSinger(currentGuess, Color.YELLOW, pGuess.getSinger());
            } else {
                gui.paintSinger(currentGuess, Color.RED, pGuess.getSinger());
            }
        }
    }

    private void compIsSingle(AerztleObject pGuess, AerztleObject pSolution) {
        if(pGuess.isSingle() == pSolution.isSingle()) {
            gui.paintSingle(currentGuess, Color.GREEN, pGuess.isSingle() == true ? "Single" : "Keine Single");
        } else {
            gui.paintSingle(currentGuess, Color.RED, pGuess.isSingle() == true ? "Single" : "Keine Single");
        }
    }

    /**
     * Searches for a matching song to the user's input in a JSON file
     * @param pFilepath File path to the JSON file that contains the data as a String
     * @param pSelection User's input, name of the song to search for as a String
     * @return If a match was found, return AerztleObject with data. Else return AerztleObject with placeholder data
     */
    private AerztleObject getSelectedSong(String pFilepath, String pSelection) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(pFilepath)));
            JSONArray arr = new JSONArray(content);
            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if(pSelection.equals(obj.getString("song")))
                return new AerztleObject(obj.getString("song"), obj.getString("album"), obj.getInt("release"),
                obj.getString("streams"), obj.getInt("durationMin"), obj.getInt("durationSec"), obj.getInt("livePlays"),
                obj.getString("singer"), obj.getBoolean("single"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // If no match was found:
        System.out.println("Song name wasn't found");
        return new AerztleObject(null, null, 0, null, 0, 0, 0, null, false);
    }

    /**
     * Handles the event, when the submit button on the GUI gets pressed
     * Compares the user's input with the searched song and checks if the game ends
     * @param pGuess User's guess for the searched song
     */
    public void submitButtonPressed(String pGuess) {
        AerztleObject guess = getSelectedSong("data\\aerztleData.json", pGuess);

        // If no match was found search in Farin library
        if(guess.getSongName() == null & settings.isFarinEnabled())
        guess = getSelectedSong("data\\aerztleDataFarin.json", pGuess);

        // If no match was found search in Bela library
        if(guess.getSongName() == null & settings.isBelaEnabled())
        guess = getSelectedSong("data\\aerztleDataBela.json", pGuess);
        
        // If no match was found search in Sahnie library
        if(guess.getSongName() == null & settings.isSahnieEnabled())
        guess = getSelectedSong("data\\aerztleDataSahnie.json", pGuess);


        // Check all categories
        if(guess.getSongName() != null) {
            compSongName(guess, randomAerztleObject);
            compAlbum(guess, randomAerztleObject);
            compReleaseYear(guess, randomAerztleObject);
            compStreams(guess, randomAerztleObject);
            compDuration(guess, randomAerztleObject);
            compLivePlays(guess, randomAerztleObject);
            compSinger(guess, randomAerztleObject);
            compIsSingle(guess, randomAerztleObject);
            currentGuess++;
        } else {
            return;
        }
        
        if(randomAerztleObject.getSongName().equals(guess.getSongName())) {
            Object[] options = {"Neues Spiel", "Beenden"};
            int n = JOptionPane.showOptionDialog(
                gui,
                "Du hast den Song erraten!\nNochmal spielen?",
                "Ärztle",
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION,
                null,
                options,
                options[0]
            );
            switch(n) {
                case 0:
                    restart();
                    break;    
                case 1:
                    gui.dispose();                 // Close the GUI & exit the game
                    System.exit(0);
                    break;
            }
        }
        if(currentGuess > TRIES) {
            Object[] options = {"Neues Spiel", "Beenden"};
            int n = JOptionPane.showOptionDialog(
                gui,
                "Du hast keine Versuche mehr, der Song war: " + randomAerztleObject.getSongName() + "\nNochmal spielen?",
                "Ärztle",
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION,
                null,
                options,
                options[0]
            );
            switch(n) {
                case 0:
                    gui.dispose();         // Close the current GUI
                    new AerztleGame(settings);     // Restart the game
                case 1:
                    gui.dispose();         // Close the GUI & exit the game
                    System.exit(0);
            }
        }
    }

    private void restart() {
        gui.resetGui();
        currentGuess = 1;
        int randomIndex = (int) (Math.random() * aerztleObjects.size());
        randomAerztleObject =  aerztleObjects.get(randomIndex);
    }

    /**
     * Initializes a linked list with AerztleObjects from given path to a JSON file
     * @param pFilepath path to the JSON file with AerztleObjects
     * @param pLinkedList LinkedList to add the objects to
     * @return Initialized LinkedList with AerztleObjects from the given file path
     */
    private LinkedList<AerztleObject> readSongsFromJson(String pFilepath, LinkedList<AerztleObject> pLinkedList) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(pFilepath)));
            JSONArray arr = new JSONArray(content);
            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                pLinkedList.add(new AerztleObject(obj.getString("song"), obj.getString("album"),
                obj.getInt("release"), obj.getString("streams"), obj.getInt("durationMin"),
                obj.getInt("durationSec"), obj.getInt("livePlays"), obj.getString("singer"), obj.getBoolean("single")));
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pLinkedList;
    }
}
