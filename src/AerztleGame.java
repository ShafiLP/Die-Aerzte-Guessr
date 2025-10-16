import java.awt.Color;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

public class AerztleGame implements GameMode {
    private Settings settings;

    private AerztleGui gui;
    private AerztleObject currentSong;
    private LinkedList<AerztleObject> aerztleObjects;

    private final int TRIES;
    private int currentGuess = 1;

    public AerztleGame (Settings pSettings) {
        settings = pSettings;
        gui = new AerztleGui(this, settings);

        // Load from settings
        TRIES = pSettings.getAeTries();

        aerztleObjects = new LinkedList<>();
        aerztleObjects = readSongsFromJson("data\\aerztleData.json", aerztleObjects);

        // Load bonus libraries if enabled
        if(settings.isFarinEnabled())
        aerztleObjects = readSongsFromJson("data\\aerztleDataFarin.json", aerztleObjects);

        if(settings.isBelaEnabled())
        aerztleObjects = readSongsFromJson("data\\aerztleDataBela.json", aerztleObjects);

        if(settings.isSahnieEnabled())
        aerztleObjects = readSongsFromJson("data\\aerztleDataSahnie.json", aerztleObjects);

        int randomIndex = (int) (Math.random() * aerztleObjects.size());
        currentSong =  aerztleObjects.get(randomIndex);

        // DEBUG
        System.out.println(currentSong.getSongName());
        System.out.println(currentSong.getAlbum());
        System.out.println(currentSong.getReleaseYear());
        System.out.println(currentSong.getStreamsAsText());
        System.out.println(currentSong.getDurationMinutes());
        System.out.println(currentSong.getDurationSeconds());
        System.out.println(currentSong.getLivePlays());
        System.out.println(currentSong.getSinger());
        System.out.println(currentSong.isSingle());
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
                if(pGuess.getReleaseYear() - 1 == pSolution.getReleaseYear()) {
                    gui.paintReleaseYear(currentGuess, Color.YELLOW, pGuess.getReleaseYear() + " ⬇️");
                } else {
                    gui.paintReleaseYear(currentGuess, Color.RED, pGuess.getReleaseYear() + " ⬇️");
                }
            } else {
                if(pGuess.getReleaseYear() + 1 == pSolution.getReleaseYear()) {
                    gui.paintReleaseYear(currentGuess, Color.YELLOW, pGuess.getReleaseYear() + " ⬆️");
                } else {
                    gui.paintReleaseYear(currentGuess, Color.RED, pGuess.getReleaseYear() + " ⬆️");
                }
            }
        }
    }

    private void compStreams(AerztleObject pGuess, AerztleObject pSolution) {
        if(pGuess.getStreamsAsInteger() == pSolution.getStreamsAsInteger()) {
            gui.paintStreams(currentGuess, Color.GREEN, pGuess.getStreamsAsInteger() == 0 ? "≥ 100.000" : "≥" + pGuess.getStreamsAsText());
        } else {
            if(pGuess.getStreamsAsInteger() - pSolution.getStreamsAsInteger() > 0) {
                gui.paintStreams(currentGuess, pGuess.getStreamsAsInteger() - 100000 == pSolution.getStreamsAsInteger() ? Color.YELLOW : Color.RED,
                pGuess.getStreamsAsInteger() == 0 ? "< 100.000" + " ⬇️" : "≥" + pGuess.getStreamsAsText() + " ⬇️");
            } else {
                gui.paintStreams(currentGuess, pGuess.getStreamsAsInteger() + 100000 == pSolution.getStreamsAsInteger() ? Color.YELLOW : Color.RED,
                pGuess.getStreamsAsInteger() == 0 ? "< 100.000" + " ⬆️" : "≥" + pGuess.getStreamsAsText() + " ⬆️");
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

    public void openEndingScreen(String pRow1, String pRow2) {
        gui.setInteractable(false);
        new EndingScreen(this, "Ärztle", settings.isColourfulGuiEnabled() ? new Color(220, 255, 220) : Color.WHITE,
        new Color(100, 150, 100) , pRow1, pRow2, settings);
    }

    public void restartGame() {
        gui.resetGui();
        currentGuess = 1;
        int randomIndex = (int) (Math.random() * aerztleObjects.size());
        currentSong =  aerztleObjects.get(randomIndex);
        gui.setInteractable(true);
    }

    public void closeGame() {
        gui.dispose();
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
            compSongName(guess, currentSong);
            compAlbum(guess, currentSong);
            compReleaseYear(guess, currentSong);
            compStreams(guess, currentSong);
            compDuration(guess, currentSong);
            compLivePlays(guess, currentSong);
            compSinger(guess, currentSong);
            compIsSingle(guess, currentSong);
            currentGuess++;
        } else {
            return;
        }
        
        // If guess is correct
        if(currentSong.getSongName().equals(guess.getSongName())) {
            openEndingScreen("Du hast das gesuchte Lied erraten!", "Du hast das Lied mit " + (currentGuess - 1) + " Versuch(en) erraten.");
            return;
        }

        // If user runs out of tries
        if(currentGuess > TRIES) {
            openEndingScreen("Du hast keine Versuche mehr!", "Das gesuchte Lied war: \"" + currentSong.getSongName() + "\"");
            return;
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
                obj.getInt("durationSec"), obj.getInt("livePlays"), obj.getString("singer"), obj.getBoolean("single")));
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pLinkedList;
    }
}
