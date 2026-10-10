package com.aerzteguessr.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.aerzteguessr.models.LyricSnippet;
import com.aerzteguessr.ui.DropdownItem;

public class JsonParser {

    private static JsonParser instance;

    private JsonParser() {}

    public static synchronized JsonParser getInstance() {
        if (instance == null) instance = new JsonParser();
        return instance;
    }

    public LinkedList<LyricSnippet> readSongsFromJson(URL filepath) {
        LinkedList<LyricSnippet> songList = new LinkedList<>();
        try {
            String content = Files.readString(Paths.get(filepath.toURI()), StandardCharsets.UTF_8);
            JSONArray arr = new JSONArray(content);
            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                songList.add(new LyricSnippet(obj.getString("song"), obj.getString("lyric")));
            }
        }
        catch (IOException | URISyntaxException | JSONException e) {
            e.printStackTrace();
        }
        return songList;
    }

    public LinkedList<DropdownItem> getSongsAsDropdownItems() {
        LinkedList<DropdownItem> dropdownItems = new LinkedList<>();
        try {
            String content = Files.readString(Paths.get(getClass().getResource("/json/songs.json").toURI()), StandardCharsets.UTF_8);
            JSONArray arr = new JSONArray(content);
            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String songName = obj.getString("song");
                String iconPath = getClass().getResource("/images/albumCover/") + obj.getString("icon") + ".png";
                dropdownItems.add(new DropdownItem(songName, iconPath));
            }
        }
        catch (IOException | URISyntaxException | JSONException e) {
            e.printStackTrace();
        }
        
        return dropdownItems;
    }
}
