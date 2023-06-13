package com.boggle.game.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DictionaryLoader {

    public ArrayList<String> loadDictionary(String filePath) throws IOException {
        File file = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(file));

        ArrayList<String> dictionary = new ArrayList<>();
        String line;

        while ((line = br.readLine()) != null) {
            if (line != null && !line.trim().isEmpty()) {
                line = line.replace("\uFEFF", "");
                dictionary.add(line);
            }
        }

        return dictionary;
    }
}
