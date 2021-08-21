package panes;

import javafx.concurrent.Task;
import main.Main;
import util.TableObjectLC;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class DataRoute extends Task<Void> {

    private double startKM;
    private double endKM;

    private String startLC;
    private String endLC;

    private File searchFile;
    private File saveFile;

    private boolean kmVal;

    private String[] searchFileNames = {
        "/01174_Laserdaten/ASC/", "/01174_Laserdaten/LAS/",
        "/01173_Orthofotos/ECW/", "/01173_Orthofotos/Geo_tiff/", "/01173_Orthofotos/jpg/", "/01173_Orthofotos/jgw/"
    };

    private String[] folderNames = {"/01174_Laserdaten/", "/01173_Orthofotos/", "/01177_Hoehenlinien/"};

    public DataRoute(File searchFile, File saveFile, String startLC, String endLC, String startKM, String endKM, boolean kmVal) {
        this.startLC = startLC;
        this.endLC = endLC;
        this.kmVal = kmVal;
        try {
            this.startKM = Double.parseDouble(startKM);
            this.endKM = Double.parseDouble(endKM);
        } catch (NumberFormatException numberFormatException) {
            updateMessage("ERR: Enter a real number!");
            return;
        }
        this.searchFile = searchFile;
        this.saveFile = saveFile;
    }

    @Override
    protected Void call() {

        if (!filesExist(searchFile)) {
            updateMessage("ERR: Search file");
            return null;
        }

        ArrayList<TableObjectLC> results;
        if(!kmVal) {
            updateMessage("Retrieving DGNs");
            results = getAllDGNs(startLC, endLC);
        } else {
            updateMessage("Retrieving foldernames");
            results = getAllResults();
        }
            updateMessage("Creating save folders");
            createFolders(saveFile);

        updateMessage("Getting all folders");
        ArrayList<File> files = getFiles(results);
        updateMessage("Getting all files from folders");
        ArrayList<File> saveFiles = loadAllFiles(files, results);
        updateMessage("Saving all files");
        saveAllFiles(saveFiles);

        updateMessage("Removing empty folders");
        removeEmptyFolders(files);

        updateMessage("Search Successful!");

        return null;
    }

    private ArrayList<TableObjectLC> getAllResults() {

        ArrayList<TableObjectLC> results = Main.fileData.getArrayListKM();

        // && startKM <= t.getKm() && endKM >= t.getKm()
        return results.stream()
                .filter(t -> startLC.equals(t.getDgntxt()) && endLC.equals(t.getDgntxt()) && startKM <= t.getKm() && endKM >= t.getKm())
                .collect(Collectors.toCollection(ArrayList::new));

    }

    private void removeEmptyFolders(ArrayList<File> files) {

        for(File file : files) {
            file = generateSavingFile(file);
            if(!file.exists()) continue;
            if(!(file.list().length > 0)) {
                System.out.println(file.delete());
            }
        }

        for(String path : searchFileNames) {
            File file = new File(path);
            file = generateSavingFile(file);
            if(!file.exists()) continue;
            if(!(file.list().length > 0)) {
                System.out.println(file.delete());
            }
        }

        for(String path : folderNames) {
            File file = new File(path);
            file = generateSavingFile(file);
            if(!file.exists()) continue;
            if(!(file.list().length > 0)) {
                System.out.println(file.delete());
            }
        }

    }

    private File generateSavingFile(File file) {

        String[] temp = file.getAbsolutePath().split(":");
        String entirePath = saveFile + temp[1];
        return new File(entirePath);
    }

    private void saveAllFiles(ArrayList<File> filesToSave) {

        double amount = filesToSave.size();
        double count = 0;

        for(File file : filesToSave) {
            String[] temp = file.getAbsolutePath().split(":");
            String entirePath = saveFile + temp[1];

            try {
                Files.copy(file.toPath(), Paths.get(entirePath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            count++;
            updateMessage("Saved: " + count + "/" + amount);
            updateProgress((count/amount)*100, 100);
        }
    }

    private ArrayList<File> loadAllFiles(ArrayList<File> folders, ArrayList<TableObjectLC> tableObjectLCS) {

        ArrayList<File> saveFiles = new ArrayList<>();

        for (File folder : folders) {
            for (File file : folder.listFiles()) {

                int x = Integer.parseInt(file.getName().substring(0, 5) + "00");
                int y = Integer.parseInt(file.getName().substring(6, 11) + "00");
                Point p = new Point(x, y);

                if (isPointInResult(p, tableObjectLCS)) saveFiles.add(file);
            }
        }
        return saveFiles;
    }

    private ArrayList<TableObjectLC> getAllDGNs(String startLC, String endLC) {

        ArrayList<TableObjectLC> results = Main.fileData.getArrayList();

        return results.stream()
            .filter(t -> startLC.compareTo(t.getDgntxt()) <= 0 && endLC.compareTo(t.getDgntxt()) >= 0)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    private ArrayList<File> getFiles(ArrayList<TableObjectLC> values) {

        ArrayList<String> fileNames = new ArrayList<>();
        String name;

        for (TableObjectLC tableObjectLC : values) {
            for (Point point : tableObjectLC.getAllPoints()) {
                name = (int) Math.floor((point.getX() / 1000000)) + "_" + (int) Math.floor((point.getY() / 10000));
                if (!fileNames.contains(name)) fileNames.add(name);

            }
        }

        ArrayList<File> foundFolders = new ArrayList<>();
        for (String names : fileNames) {
            for (String folders : searchFileNames) {
                File newSearchFile = new File(searchFile + folders + names);
                if (newSearchFile.exists() && !foundFolders.contains(newSearchFile)) {
                    foundFolders.add(newSearchFile);
                    new File(saveFile + folders + names).mkdir();
                }
            }
        }

        return foundFolders;
    }

    private boolean isPointInResult(Point point, ArrayList<TableObjectLC> results) {

        Polygon polySquare = new Polygon(
            new int[]{point.x, point.x+100, point.x+100, point.x},
            new int[]{point.y, point.y, point.y+100, point.y+100},
            4);

        for (TableObjectLC tableObjectLC : results) {
            Polygon polygon2 = new Polygon(
                new int[]{tableObjectLC.getP1().x, tableObjectLC.getP2().x, tableObjectLC.getP3().x, tableObjectLC.getP4().x},
                new int[]{tableObjectLC.getP1().y, tableObjectLC.getP2().y, tableObjectLC.getP3().y, tableObjectLC.getP4().y},
                4);
            if(polySquare.intersects(polygon2.getBounds2D())) return true;

        }
        return false;
    }


    private void createFolders(File file) {

        if (!file.exists()) file.mkdir();
        String path = file.getPath();

        createFile(path, "/01173_Orthofotos/");
        createFile(path, "/01174_Laserdaten/");
        createFile(path + "/01173_Orthofotos/", "ECW");
        createFile(path + "/01173_Orthofotos/", "Geo_tiff");
        createFile(path + "/01173_Orthofotos/", "jpg");
        createFile(path + "/01173_Orthofotos/", "jgw");
        createFile(path + "/01174_Laserdaten/", "ASC");
        createFile(path + "/01174_Laserdaten/", "LAS");
        createFile(path, "/01177_Hoehenlinien/");

    }

    private void createFile(String path, String name) {
        File file = new File(path + name);
        if (!file.exists()) {
            if (!file.mkdir()) {
                updateMessage("Couldnt create folder " + name);
                return;
            }
        }
    }

    private boolean filesExist(File path) {

        for (String fileName : searchFileNames) {
            File file = new File(path.getPath() + fileName);
            if (!file.exists()) {
                updateMessage(fileName + " does not exist!");
                return false;
            }
        }
        return true;
    }


}
