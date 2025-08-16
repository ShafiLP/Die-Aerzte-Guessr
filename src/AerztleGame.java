import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

public class AerztleGame {
    private AerztleGui gui;

    public AerztleGame() {
        gui = new AerztleGui(this);

        LinkedList<AerztleObject> aerztleObjects = new LinkedList<>();
        aerztleObjects = readSongsFromJson("data\\aerztleData.json", aerztleObjects);

        int randomIndex = (int) (Math.random() * aerztleObjects.size());
        AerztleObject randomAerztleObject =  aerztleObjects.get(randomIndex);
        //pListWithData.remove(randomLyricIndex); // Removes the object from the List to avoid reputition
        //return randomSongTextWithGap;

        System.out.println(randomAerztleObject.getSongName());
        System.out.println(randomAerztleObject.getAlbum());
        System.out.println(randomAerztleObject.getReleaseYear());
        System.out.println(randomAerztleObject.getStreams());
        System.out.println(randomAerztleObject.getDurationMinutes());
        System.out.println(randomAerztleObject.getDurationSeconds());
        System.out.println(randomAerztleObject.getWordCount());
        System.out.println(randomAerztleObject.getSinger());
        System.out.println(randomAerztleObject.isSingle());
    }
    
    private boolean compSongName(AerztleObject pGuess, AerztleObject pSolution) {
        if(pGuess.getSongName().equals(pSolution.getSongName()))
        return true;
        return false;
    }

    private void compAlbum(AerztleObject pGuess, AerztleObject pSolution) {
        if(pGuess.getAlbum().equals(pSolution.getAlbum())){
            // Color category green
        } else {
            // Color category red
        }
    }

    private void compReleaseYear(AerztleObject pGuess, AerztleObject pSolution) {
        if(pGuess.getReleaseYear() == pSolution.getReleaseYear()) {
            // Color category green
        } else {
            if(pGuess.getReleaseYear() - pSolution.getReleaseYear() > 0) {
                // Color category red, guess too high
            } else {
                // Color category red, guess too low
            }
        }
    }

    private void compStreams(AerztleObject pGuess, AerztleObject pSolution) {
        if(pGuess.getStreams() == pSolution.getStreams()) {
            // Color category green
        } else {
            if(pGuess.getStreams() - pSolution.getStreams() > 0) {
                // Color category red, guess too high
            } else {
                // Color category red, guess too low
            }
        }
    }

    private void compDuration(AerztleObject pGuess, AerztleObject pSolution) {
        if(pGuess.getDurationMinutes() * 60 + pGuess.getDurationSeconds() == pSolution.getDurationMinutes() * 60 + pSolution.getDurationSeconds()) {
            // Color category green
        } else {
            if((pGuess.getDurationMinutes() * 60 + pGuess.getDurationSeconds()) - (pSolution.getDurationMinutes() * 60 + pSolution.getDurationSeconds()) > 0) {
                // Color category red, guess too high
            } else {
                // Color category red, guess too low
            }
        }
    }

    private void compWordCount(AerztleObject pGuess, AerztleObject pSolution) {
        if(pGuess.getWordCount() == pSolution.getWordCount()) {
            // Color category green
        } else {
            if(pGuess.getWordCount() - pSolution.getWordCount() > 0) {
                // Color category red, guess too high
            } else {
                // Color category red, guess too low
            }
        }
    }

    private void compSinger(AerztleObject pGuess, AerztleObject pSolution) {
        if(pGuess.getSinger().equals(pSolution.getSinger())) {
            // Color category green
        } else {
            if(pSolution.getSinger().contains(pGuess.getSinger())) {
                // Color category yellow, guess partly right
            } else {
                // Color category red, guess wrong
            }
        }
    }

    private void compIsSingle(AerztleObject pGuess, AerztleObject pSolution) {
        if(pGuess.isSingle() == pSolution.isSingle()) {
            // Color category green
        } else {
            // Color category red
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
                obj.getInt("release"), obj.getInt("streams"), obj.getInt("durationMin"),
                obj.getInt("durationSec"), obj.getInt("wordCount"), obj.getString("singer"), obj.getBoolean("single")));
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pLinkedList;
    }
}
