package de.legoshi.panes;

import javafx.concurrent.Task;
import de.legoshi.util.Dir;
import de.legoshi.util.TitelObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Archiver extends Task<Void> {
    
    public double dirCount;
    public double dirChecked;
    public File startFile;
    
    public Archiver(File startFile) {
        dirCount = 0;
        dirChecked = 1;
        this.startFile = startFile;
    }
    
    @Override
    protected Void call() {
        File[] files = startFile.listFiles();
        if (files != null) {
            List<File> directories = Dir.getDirectories(files);
            this.dirCount = directories.size();
            for (File dir : directories) {
                List<TitelObject> titelObjects = getTitelObjects(dir);
                updateMessage("Archivieren von " + dir.getAbsolutePath());
                if (!titelObjects.isEmpty()) {
                    addArchive(dir);
                    checkForDuplicates(titelObjects, Paths.get(dir.getAbsolutePath() + "/Archiv"));
                }
                updateProgress((int) Math.round((this.dirChecked / this.dirCount) * 100), 100);
                this.dirChecked = 1 + this.dirChecked;
            }
            updateMessage("Erfolgreich alle Dateien archiviert.");
        }
        return null;
    }
    
    private void addArchive(File file) {
        if (!file.getAbsoluteFile().getName().contains("Archiv")) {
            File archiv = new File(file.getAbsolutePath() + "/Archiv");
            if (!archiv.exists()) archiv.mkdir();
        }
    }
    
    //returns all files with the endings of .ecw .jgw .jpg .asc .las .dgn
    public List<TitelObject> getTitelObjects(File directory) {
        File[] files = directory.listFiles();
        List<TitelObject> titelObjects = new ArrayList<>();
        for (File file : files) {
            if (file.getName().length() > 3) {
                
                String endingName = file.getName().substring(file.getName().length() - 4);
                if (endingName.equals(".ecw") || endingName.equals(".jgw") || endingName.equals(".jpg")
                        || endingName.equals(".asc") || endingName.equals(".las") || endingName.equals(".dgn")) {
                    titelObjects.add(new TitelObject(file, file.getName()));
                    updateMessage("Füge Dateien hinzu: " + file.getAbsolutePath());
                }
            }
        }
        return titelObjects;
    }
    
    //checks for duplicates
    public void checkForDuplicates(List<TitelObject> titelObjectArrayList, Path archivpath) {
        titelObjectArrayList.sort(Comparator.comparing(TitelObject::getW1String).thenComparing(TitelObject::getW2String));
        String first, second;
        //de.legoshi.logger.Logger de.legoshi.logger = new de.legoshi.logger.Logger();
        //de.legoshi.logger.createLogFile(archivpath.toString());
        for (int i = 0; i < titelObjectArrayList.size() - 1; i++) {
            TitelObject t1 = titelObjectArrayList.get(i);
            TitelObject t2 = titelObjectArrayList.get(i + 1);
            first = t1.getW1String();
            second = t2.getW1String();
            if (first.equals(second)) moveFile(t1, t2, archivpath, i);
        }
    }
    
    public void moveFile(TitelObject t1, TitelObject t2, Path archivpath, int i) {
        int date1 = Integer.parseInt(t1.getW2String());
        int date2 = Integer.parseInt(t2.getW2String());
        if (date1 < date2) {
            Path newLocation = Paths.get(archivpath + "/" + t1.getOriginal());
            try {
                Files.move(t1.getFile().toPath(), newLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //de.legoshi.logger.writeToLogFile("Moved " + t1.getFile().toString() + " to " + newLocation);
        } else {
            Path newLocation = Paths.get(archivpath + "/" + t2.getOriginal());
            try {
                Files.move(t2.getFile().toPath(), newLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //de.legoshi.logger.writeToLogFile("Moved " + t2.getFile().toString() + " to " + newLocation);
        }
    }
}
