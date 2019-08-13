package com.hSenid.solrsearch.FileIO;

import java.io.*;
import java.util.ArrayList;

public class FileIO {

    public static ArrayList<String> getAppids(String path) throws FileNotFoundException {
        ArrayList<String> strings = new ArrayList();
        File file = new File(path);
        FileReader fr = new FileReader(file);
        try {
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                line = line.replaceAll("\"", "");
                strings.add(line);
            }
            return strings;
        } catch (IOException var8) {
            var8.printStackTrace();
            return strings;
        }
    }
}
