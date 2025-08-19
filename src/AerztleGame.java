import java.awt.Color;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONObject;

public class AerztleGame {
    private AerztleGui gui;
    private AerztleObject randomAerztleObject;

    private final int TRIES = 7;
    private int currentGuess = 1;

    public AerztleGame() {
        gui = new AerztleGui(this);

        LinkedList<AerztleObject> aerztleObjects = new LinkedList<>();
        aerztleObjects = readSongsFromJson("data\\aerztleData.json", aerztleObjects);

        int randomIndex = (int) (Math.random() * aerztleObjects.size());
        randomAerztleObject =  aerztleObjects.get(randomIndex);
        //pListWithData.remove(randomLyricIndex); // Removes the object from the List to avoid reputition
        //return randomSongTextWithGap;

        // DEBUG
        System.out.println(randomAerztleObject.getSongName());
        System.out.println(randomAerztleObject.getAlbum());
        System.out.println(randomAerztleObject.getReleaseYear());
        System.out.println(randomAerztleObject.getStreamsAsText());
        System.out.println(randomAerztleObject.getDurationMinutes());
        System.out.println(randomAerztleObject.getDurationSeconds());
        System.out.println(randomAerztleObject.getWordCount());
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
        if(pGuess.getStreamsAsInteger() == pSolution.getStreamsAsInteger()) {
            gui.paintStreams(currentGuess, Color.GREEN, pGuess.getStreamsAsText() + "");
        } else {
            if(pGuess.getStreamsAsInteger() - pSolution.getStreamsAsInteger() > 0) {
                gui.paintStreams(currentGuess, Color.RED, pGuess.getStreamsAsText() + " ⬇️");
            } else {
                gui.paintStreams(currentGuess, Color.RED, pGuess.getStreamsAsText() + " ⬆️");
            }
        }
    }

    private void compDuration(AerztleObject pGuess, AerztleObject pSolution) {
        if(pGuess.getDurationMinutes() * 60 + pGuess.getDurationSeconds() == pSolution.getDurationMinutes() * 60 + pSolution.getDurationSeconds()) {
            gui.paintDuration(currentGuess, Color.GREEN, pGuess.getDurationMinutes() + ":" + pGuess.getDurationSeconds());
        } else {
            if((pGuess.getDurationMinutes() * 60 + pGuess.getDurationSeconds()) - (pSolution.getDurationMinutes() * 60 + pSolution.getDurationSeconds()) > 0) {
                gui.paintDuration(currentGuess, Color.RED, pGuess.getDurationMinutes() + ":" + pGuess.getDurationSeconds() + " ⬇️");
            } else {
                gui.paintDuration(currentGuess, Color.RED, pGuess.getDurationMinutes() + ":" + pGuess.getDurationSeconds() + " ⬆️");
            }
        }
    }

    private void compWordCount(AerztleObject pGuess, AerztleObject pSolution) {
        if(pGuess.getWordCount() == pSolution.getWordCount()) {
            gui.paintWordCount(currentGuess, Color.GREEN, pGuess.getWordCount() + "");
        } else {
            if(pGuess.getWordCount() - pSolution.getWordCount() > 0) {
                gui.paintWordCount(currentGuess, Color.RED, pGuess.getWordCount() + " ⬇️");
            } else {
                gui.paintWordCount(currentGuess, Color.RED, pGuess.getWordCount() + " ⬆️");
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
            gui.paintSingle(currentGuess, Color.GREEN, pGuess.isSingle() + "");
        } else {
            gui.paintSingle(currentGuess, Color.RED, pGuess.isSingle() + "");
        }
    }

    private AerztleObject getSelectedSong(String pFilepath, String pSelection) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(pFilepath)));
            JSONArray arr = new JSONArray(content);
            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if(pSelection.equals(obj.getString("song")))
                return new AerztleObject(obj.getString("song"), obj.getString("album"), obj.getInt("release"),
                obj.getString("streams"), obj.getInt("durationMin"), obj.getInt("durationSec"), obj.getInt("wordCount"),
                obj.getString("singer"), obj.getBoolean("single"));
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        // If no match was found:
        System.out.println("Song name wasn't found");
        return new AerztleObject("Error", "Error", 1970, "0", 0, 0, 0, "Error", false);
    }

    public void submitButtonPressed(String pGuess) {
        AerztleObject guess = getSelectedSong("data\\aerztleData.json", pGuess);

        // Check all categories
        compSongName(guess, randomAerztleObject);
        compAlbum(guess, randomAerztleObject);
        compReleaseYear(guess, randomAerztleObject);
        compStreams(guess, randomAerztleObject);
        compDuration(guess, randomAerztleObject);
        compWordCount(guess, randomAerztleObject);
        compSinger(guess, randomAerztleObject);
        compIsSingle(guess, randomAerztleObject);

        currentGuess++;
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
                    gui.dispose();         // Close the current GUI
                    new AerztleGame();     // Restart the game
                case 1:
                    gui.dispose();         // Close the GUI & exit the game
                    System.exit(0);
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
                    new AerztleGame();     // Restart the game
                case 1:
                    gui.dispose();         // Close the GUI & exit the game
                    System.exit(0);
            }
        }
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
                obj.getInt("durationSec"), obj.getInt("wordCount"), obj.getString("singer"), obj.getBoolean("single")));
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pLinkedList;
    }
}
