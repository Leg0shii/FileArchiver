package util;

import javafx.scene.control.Label;

import java.io.File;
import java.util.ArrayList;

public class Dir {
    //returns a list of all sub folders
    public static ArrayList<File> getDirectories(File[] files) {

        ArrayList<File> fileArrayList = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                fileArrayList.add(file);
                System.out.println("Dir: " + file.getName());
                if(!(file.listFiles().length <= 1)) {
                    fileArrayList.addAll(getDirectories(file.listFiles()));
                }
            }
        }
        return fileArrayList;
    }

    public static boolean fileExists(Label label, File startFile) {
        if (!startFile.exists()) {
            label.setText("ERR: Dateipfad existiert nicht.");
            return false;
        }
        else {
            label.setText("SUCC: Dateipfad existiert!");
            return true;
        }
    }

    public static String getFileEnding(File file) {
        return file.getName().substring(file.getName().length()-4);
    }

}