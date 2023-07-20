package de.legoshi.util;

import javafx.scene.control.Label;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Dir {
    //returns a list of all sub folders
    public static List<File> getDirectories(File[] files) {
        List<File> fileArrayList = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                fileArrayList.add(file);
                // System.out.println("Ordner: " + file.getName());
                if(!(file.listFiles().length <= 1)) {
                    fileArrayList.addAll(getDirectories(file.listFiles()));
                }
            }
        }
        return fileArrayList;
    }

    public static boolean fileExists(Label label, File startFile) {
        if (!startFile.exists()) {
            label.setText("FEHLER! Dateipfad existiert nicht.");
            return false;
        }
        else {
            label.setText("FEHLER! Dateipfad existiert!");
            return true;
        }
    }

}