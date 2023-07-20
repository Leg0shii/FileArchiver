package de.legoshi.archiver;

import de.legoshi.util.DirectoryPaths;

import java.io.File;

public class FileHandler {
    
    public static boolean filesExist(File path) {
        for (String fileName : DirectoryPaths.SEARCH_FILE_NAMES) {
            File file = new File(path.getPath() + fileName);
            if (!file.exists()) {
                return false;
            }
        }
        return true;
    }
    
    protected static void createFile(String path, String name) throws Exception {
        File file = new File(path + name);
        if (!file.exists()) {
            if (!file.mkdir()) {
                throw new Exception("FEHLER! Konnte folgenden Ordner nicht erstellen:  " + name);
            }
        }
    }
    
}
