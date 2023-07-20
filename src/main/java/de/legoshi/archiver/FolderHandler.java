package de.legoshi.archiver;

import de.legoshi.util.DirectoryPaths;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderHandler {
    
    public static void createFolders(File file) throws Exception {
        if (!file.exists()) {
            file.mkdir();
        }
        String path = file.getPath();
        
        for (String baseFolder : DirectoryPaths.FOLDER_NAMES) {
            FileHandler.createFile(path, baseFolder);
        }
        
        for (String filename : DirectoryPaths.SEARCH_FILE_NAMES) {
            FileHandler.createFile(path, filename);
        }
    }
    
    public static void removeEmptyFolders(List<File> files, File saveFile) {
        files.forEach(file -> removeFolder(file, saveFile));
        DirectoryPaths.SEARCH_FILE_NAMES.stream().map(File::new).forEach(file -> removeFolder(file, saveFile));
        DirectoryPaths.FOLDER_NAMES.stream().map(File::new).forEach(file -> removeFolder(file, saveFile));
    }
    
    private static void removeFolder(File file, File saveFile) {
        file = generateSavingFolder(file, saveFile);
        if (!file.exists()) return;
        if (!(file.list().length > 0)) {
            System.out.println(file.delete());
        }
    }
    
    private static File generateSavingFolder(File file, File saveFile) {
        String[] temp = file.getAbsolutePath().split(":");
        String entirePath = saveFile + temp[1];
        return new File(entirePath);
    }
    
    public static List<File> getDirectories(File[] files) {
        List<File> fileArrayList = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                fileArrayList.add(file);
                if (!(file.listFiles().length <= 1)) {
                    fileArrayList.addAll(getDirectories(file.listFiles()));
                }
            }
        }
        return fileArrayList;
    }
    
}
