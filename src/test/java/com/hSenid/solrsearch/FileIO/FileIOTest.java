package com.hSenid.solrsearch.FileIO;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileIOTest {
    public static final String PATH1 = "/home/hasitha/hSenid/analysis/AppId.csv";
    public static final String PATH2 = "/home/hasitha/hSenid/analysis/AppId3";

    @Test
    public void readFile() {

        try {
            final ArrayList<String> strings1 = FileIO.getAppids(PATH1);
            final ArrayList<String> strings2 = FileIO.getAppids(PATH2);

//            long start = System.currentTimeMillis();
//
//
//            List<String> collect2 = strings1.stream().parallel()
//                    .filter(s1 -> strings2.stream()
//                            .anyMatch(s2 -> s1.equals(s2)))
//                    .collect(Collectors.toList());
//
//            long finish = System.currentTimeMillis();
//            long timeElapsed = finish - start;
//            System.out.println(timeElapsed);

            List<String> collect1 = strings1.stream().parallel()
                    .filter(s1 -> strings2.contains(s1))
                    .collect(Collectors.toList());

            strings2.stream()
                    .filter(e -> !collect1.contains(e)).forEach(s-> System.out.println(s));


            System.out.println("ok");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}