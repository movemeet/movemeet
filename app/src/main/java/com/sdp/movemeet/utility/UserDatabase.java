package com.sdp.movemeet.utility;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class UserDatabase {
    private final String filename = "users.json";
    private Map<String, String> users = new HashMap<String, String>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public UserDatabase() throws IOException {
        if (Files.exists(new File(filename).toPath())) {
            readFromFile();
        } else {
            Files.createFile(new File(filename).toPath());
            /*
            try {
                Files.createFile(new File(filename).toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
             */
        }
    }

    public void readFromFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        users = mapper.readValue(new File(filename), Map.class);
        /*
        try {
            ObjectMapper mapper = new ObjectMapper();
            users = mapper.readValue(new File(filename), Map.class);
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Change filename.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
         */
    }

    public void writeToFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(filename), users);
        /*
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File(filename), users);
        } catch (Exception e) {
            e.printStackTrace();
        }
         */
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void cleanFile() throws IOException {
        File db = new File(filename);
        Files.deleteIfExists(db.toPath());
        /*
        try {
            Files.deleteIfExists(db.toPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
         */
    }

    public void addUser(String email, String password) throws IOException {
        if (isInUsers(email)) {
            return;
        } else {
            users.put(email, password);
            writeToFile();
        }
    }

    // checks whether a given user is in the database or not
    public boolean isInUsers(String email) {
        return users.containsKey(email);
    }
}