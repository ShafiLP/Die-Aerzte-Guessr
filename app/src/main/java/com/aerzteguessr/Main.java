package com.aerzteguessr;

import java.util.List;

import com.aerzteguessr.models.Song;
import com.aerzteguessr.services.Database;

/**
 * Main class
 * 
 * @author ShafiLP
 * @version 0.6.1.2
 */
public class Main {
    public static void main(String[] args) {
        // Initialize database on application startup
        Database.initializeDatabase();

        // DEBUG: Print all songs
        List<Song> songs = Database.getAllSongs();
        for (Song s : songs) {
            System.out.println(
                s.id + " | " +
                s.name + " | " +
                s.albumId + " | " +
                s.releaseYear + " | " +
                s.streams + " | " +
                s.duration + " | " +
                s.liveplays + " | " +
                s.singer + " | " +
                s.hasSingle
            );
        }  
        
        // Start GUI
        new MainMenu();
    }
}
