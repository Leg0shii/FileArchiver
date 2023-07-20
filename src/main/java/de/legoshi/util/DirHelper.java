package de.legoshi.util;

import javafx.scene.control.Label;

import java.io.File;

public class DirHelper {
    
    public static boolean fileExists(Label label, File startFile) {
        if (!startFile.exists()) {
            label.setText("FEHLER! Dateipfad existiert nicht.");
            return false;
        } else {
            label.setText("FEHLER! Dateipfad existiert!");
            return true;
        }
    }
    
}