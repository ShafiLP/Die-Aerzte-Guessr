package com.aerzteguessr.services;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.aerzteguessr.models.Artist;
import com.aerzteguessr.models.Release;
import com.aerzteguessr.models.ReleaseType;
import com.aerzteguessr.models.Song;

public class Database {

    private static final String URL = "jdbc:sqlite:aerzte.db";

    /**
     * Builds a connection to the local database.
     */
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /**
     * Creates database for songs in cmd, if doesn't exist yet.
     */
    public static void initializeDatabase() {
        try (Connection conn = Database.connect()) {
            Statement stmt = conn.createStatement();

            // DEBUG: Drop tables if they already exist (for development purposes)
            //! TODO: DELETE THIS LATER
            stmt.execute("""
                DELETE * FROM artists;
                DELETE * FROM releases;
                DELETE * FROM songs;
                DELETE * FROM lyrics;
                DELETE * FROM snippets;
                DROP TABLE IF EXISTS artists;
                DROP TABLE IF EXISTS releases;
                DROP TABLE IF EXISTS songs;
                DROP TABLE IF EXISTS lyrics;
                DROP TABLE IF EXISTS snippets;
            """);
            Log.Success("Existing tables dropped successfully (for development purposes).");

            // ARTISTS
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS artists (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL
                );
            """);
            Log.Success("Database \"artists\" created successfully");

            // ALBUMS/RELEASES
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS releases (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    artist_id INTEGER,
                    release_year INTEGER,
                    release_type TEXT,
                    imgPath TEXT,
                    FOREIGN KEY (artist_id) REFERENCES artists(id)
                );
            """);
            Log.Success("Database \"releases\" created successfully");
               
            // SONGS
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS songs (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    album_id INTEGER,
                    release_year INTEGER,
                    streams INTEGER,
                    duration INTEGER,
                    liveplays INTEGER,
                    singer TEXT,
                    hasSingle INTEGER NOT NULL,
                    FOREIGN KEY (album_id) REFERENCES releases(id)
                );
            """);
            Log.Success("Database \"songs\" created successfully");

            // FULL LYRICS
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS lyrics (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    song_id INTEGER,
                    lyrics TEXT NOT NULL,
                    FOREIGN KEY (song_id) REFERENCES songs(id)
                );
            """);
            Log.Success("Database \"lyrics\" created successfully");

            // LYRIC SNIPPETS
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS snippets (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    song_id INTEGER NOT NULL,
                    snippet TEXT NOT NULL,
                    FOREIGN KEY (song_id) REFERENCES songs(id)
                );
            """);
            Log.Success("Database \"snippets\" created successfully");

            // CSV IMPORTS
            insertArtistsFromCSV();
            insertReleasesFromCSV();
            insertSongsFromCSV();
        } catch (Exception e) {
            Log.Error("Error while initializing databases: " + e.getMessage());
        }
    }

    // ==============================
    // CSV IMPORTS
    // ==============================

    private static void insertArtistsFromCSV() {
        try (Connection conn = Database.connect()) {
            List<String> lines = Files.readAllLines(Paths.get("app/src/main/csv/artists.csv"));

            String sql = """
                INSERT INTO artists (id, name)
                VALUES (?, ?)
            """;

            PreparedStatement ps = conn.prepareStatement(sql);

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("id;")) continue;
                
                String[] data = line.split(";");

                int id = Integer.parseInt(data[0].trim());
                String name = data[1].trim();

                ps.setInt(1, id);
                ps.setString(2, name);

                ps.addBatch();
            }

            ps.executeBatch();
        }
        catch (Exception e) {
            Log.Error("Error while inserting artists from CSV: " + e.getMessage());
        }
    }

    private static void insertReleasesFromCSV() {
        try (Connection conn = Database.connect()) {
            List<String> lines = Files.readAllLines(Paths.get("app/src/main/csv/releases.csv"));

            String sql = """
                INSERT INTO releases (id, name, artist_id, release_year, release_type)
                VALUES (?, ?, ?, ?, ?)
            """;

            PreparedStatement ps = conn.prepareStatement(sql);

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("id;")) continue;
                
                String[] data = line.split(";");

                int id = Integer.parseInt(data[0].trim());
                String name = data[1].trim();
                int artistId = Integer.parseInt(data[2].trim());
                int year = Integer.parseInt(data[3].trim());
                String type = data[4].trim();

                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setInt(3, artistId);
                ps.setInt(4, year);
                ps.setString(5, type);

                ps.addBatch();
            }

            ps.executeBatch();
        }
        catch (Exception e) {
            Log.Error("Error while inserting releases from CSV: " + e.getMessage());
        }
    }

    private static void insertSongsFromCSV() {
        try (Connection conn = Database.connect()) {
            List<String> lines = Files.readAllLines(Paths.get("app/src/main/csv/songs.csv"));

            String sql = """
                INSERT INTO songs (id, name, album_id, release_year, streams, duration, liveplays, singer, hasSingle)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

            PreparedStatement ps = conn.prepareStatement(sql);

            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("id;")) continue;
                
                String[] data = line.split(";");

                int id = Integer.parseInt(data[0].trim());
                int albumId = Integer.parseInt(data[1].trim());
                int year = Integer.parseInt(data[2].trim());
                String name = data[3].trim();
                int streams = Integer.parseInt(data[4].trim());
                int duration = Integer.parseInt(data[5].trim());
                int liveplays = Integer.parseInt(data[6].trim());
                String singer = data[7].trim();
                int hasSingle = Integer.parseInt(data[8].trim());

                ps.setInt(1, id);
                ps.setString(2, name);
                ps.setInt(3, albumId);
                ps.setInt(4, year);
                ps.setInt(5, streams);
                ps.setInt(6, duration);
                ps.setInt(7, liveplays);
                ps.setString(8, singer);
                ps.setInt(9, hasSingle);

                ps.addBatch();
            }

            ps.executeBatch();
        }
        catch (Exception e) {
            Log.Error("Error while inserting songs from CSV: " + e.getMessage());
        }
    }

    // ==============================
    // GETTERS
    // ==============================

    /**
     * Fetches all artists from the database.
     * @return List of all artists in the database.
     */
    public static List<Artist> getAllArtists() {
        try (Connection conn = Database.connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM artists");

            List<Artist> artists = new java.util.ArrayList<>();
            while (rs.next()) {
                artists.add(mapArtist(rs));
            }
            return artists;
        } catch (SQLException e) {
            Log.Error("Error while fetching artists: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Fetches all releases from the database.
     * @return List of all releases in the database.
     */
    public static List<Release> getAllReleases() {
        try (Connection conn = Database.connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM releases");

            List<Release> releases = new java.util.ArrayList<>();
            while (rs.next()) {
                releases.add(mapRelease(rs));
            }
            return releases;
        } catch (SQLException e) {
            Log.Error("Error while fetching releases: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Fetches all releases of a specific artist from the database.
     * @param artistId ID of the artist whose releases should be fetched.
     * @return List of releases of the specified artist in the database.
     */
    public static List<Release> getReleaseByArtistId(int artistId) {
        try (Connection conn = Database.connect()) {
            String sql = "SELECT * FROM releases WHERE artist_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, artistId);
            ResultSet rs = ps.executeQuery();

            List<Release> releases = new java.util.ArrayList<>();
            while (rs.next()) {
                releases.add(mapRelease(rs));
            }
            return releases;
        } catch (SQLException e) {
            Log.Error("Error while fetching releases by artist ID: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Fetches all songs from the database.
     * @return List of all songs in the database.
     */
    public static List<Song> getAllSongs() {
        try (Connection conn = Database.connect()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM songs");

            List<Song> songs = new java.util.ArrayList<>();
            while (rs.next()) {
                songs.add(mapSong(rs));
            }
            return songs;
        } catch (SQLException e) {
            Log.Error("Error while fetching songs: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    // ==============================
    // MAPPING
    // ==============================

    private static Artist mapArtist (ResultSet rs) throws SQLException {
        Artist artist = new Artist();
        artist.id = rs.getInt("id");
        artist.name = rs.getString("name");
        return artist;
    }

    private static Release mapRelease (ResultSet rs) throws SQLException {
        Release release = new Release();
        release.id = rs.getInt("id");
        release.name = rs.getString("name");
        release.artistId = rs.getInt("artist_id");
        release.releaseYear = rs.getInt("release_year");
        release.releaseType = ReleaseType.valueOf(rs.getString("release_type").toUpperCase());
        return release;
    }

    private static Song mapSong (ResultSet rs) throws SQLException {
        Song song = new Song();
        
        song.id = rs.getInt("id");
        song.name = rs.getString("name");
        song.albumId = rs.getInt("album_id");
        song.releaseYear = rs.getInt("release_year");
        song.streams = rs.getInt("streams");
        song.duration = rs.getInt("duration");
        song.liveplays = rs.getInt("liveplays");
        song.singer = rs.getString("singer");
        song.hasSingle = rs.getInt("hasSingle") == 1; // SQLite speichert booleans als 0/1
        return song;
    }

    
}
