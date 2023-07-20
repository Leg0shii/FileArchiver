package de.legoshi.panes;

import javafx.concurrent.Task;
import de.legoshi.main.Main;
import de.legoshi.util.TableObjectLC;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class DataRoute extends Task<Void> {

    private double startKM = -9999;
    private double endKM = -9999;

    private final String startLC;
    private final String endLC;

    private final File searchFile;
    private final File saveFile;

    private final boolean kmVal;

    private final String[] searchFileNames = {
            "/01174_Laserdaten/ASC/",
            "/01174_Laserdaten/LAS/",
            "/01173_Orthofotos/ECW/",
            "/01173_Orthofotos/Geo_tiff/",
            "/01173_Orthofotos/jpg/",
            "/01173_Orthofotos/jgw/"
    };

    private String[] folderNames = {
            "/01174_Laserdaten/",
            "/01173_Orthofotos/",
            "/01177_Hoehenlinien/"
    };

    public DataRoute(File searchFile, File saveFile, String startLC, String endLC) {
        this.startLC = startLC.toLowerCase();
        this.endLC = endLC.toLowerCase();
        this.searchFile = searchFile;
        this.saveFile = saveFile;
        this.kmVal = false;
    }

    public DataRoute(File searchFile, File saveFile, String startLC, String endLC, String startKM, String endKM) {
        this.startLC = startLC;
        this.endLC = endLC;
        try {
            this.startKM = Double.parseDouble(startKM);
            this.endKM = Double.parseDouble(endKM);
        } catch (NumberFormatException numberFormatException) {
            updateMessage("FEHLER! Bitte gibt eine Zahl ein.");
        }
        this.searchFile = searchFile;
        this.saveFile = saveFile;
        this.kmVal = true;
    }
    
    public void startSearch() {
        if (!filesExist(searchFile)) {
            updateMessage("FEHLER! Datei existiert nicht.");
            updateProgress(0, 100);
            return;
        }

        if((startKM == -9999 || endKM == -9999) && kmVal) return;

        List<TableObjectLC> results;
        if (!kmVal) {
            updateMessage("Laden aller DGNs.");
            results = getAllDGNs(startLC, endLC);
        } else {
            updateMessage("Laden aller Order.");
            results = getAllResults();
        }
        updateMessage("Erstellen aller Ordner.");
        createFolders(saveFile);

        updateMessage("Laden aller Ordnerinhalte.");
        List<File> files = getFiles(results);
        updateMessage("Laden aller Dateien aus den Ordnern.");
        List<File> saveFiles = loadAllFiles(files, results);
        updateMessage("Speichere Dateien...");
        int amount = saveAllFiles(saveFiles);

        updateMessage("Lösche alle leeren Ordner.");
        removeEmptyFolders(files);

        updateMessage("Suche erfolgreich! " + amount + " Dateien gefunden.");
    }

    private List<TableObjectLC> getAllResults() {
        List<TableObjectLC> results = Main.fileData.getArrayListKM();
        // && startKM <= t.getKm() && endKM >= t.getKm()
        return results.stream()
                .filter(t -> startLC.equals(t.getDgnText()) && endLC.equals(t.getDgnText()) && startKM <= t.getKm() && endKM >= t.getKm())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void removeEmptyFolders(List<File> files) {
        for (File file : files) {
            file = generateSavingFile(file);
            if (!file.exists()) continue;
            if (!(file.list().length > 0)) {
                System.out.println(file.delete());
            }
        }

        for (String path : searchFileNames) {
            File file = new File(path);
            file = generateSavingFile(file);
            if (!file.exists()) continue;
            if (!(file.list().length > 0)) {
                System.out.println(file.delete());
            }
        }

        for (String path : folderNames) {
            File file = new File(path);
            file = generateSavingFile(file);
            if (!file.exists()) continue;
            if (!(file.list().length > 0)) {
                System.out.println(file.delete());
            }
        }

    }

    private File generateSavingFile(File file) {
        String[] temp = file.getAbsolutePath().split(":");
        String entirePath = saveFile + temp[1];
        return new File(entirePath);
    }

    private int saveAllFiles(List<File> filesToSave) {
        double amount = filesToSave.size();
        AtomicReference<Double> count = new AtomicReference<>((double) 0);

        for (File file : filesToSave) {
            String[] temp = file.getPath().split(":");
            String entirePath = saveFile + file.getPath().replace(searchFile.getPath(), "/");

            try {
                Files.copy(file.toPath(), Paths.get(entirePath), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            count.getAndSet(count.get()+1);
            updateMessage("Gespeichert: " + count.get().intValue() + "/" + (int)amount + " Dateien.");
            updateProgress((count.get().intValue() / amount) * 100, 100);
        }
        return (int)amount;
    }

    private List<File> loadAllFiles(List<File> folders, List<TableObjectLC> tableObjectLCS) {
        List<File> saveFiles = new ArrayList<>();

        for (File folder : folders) {
            File[] f = folder.listFiles();
            if(f != null) {
                for (File file : f) {
                    if(file.getName().length() > 12) {
                        int x = Integer.parseInt(file.getName().substring(0, 5) + "00");
                        int y = Integer.parseInt(file.getName().substring(6, 11) + "00");
                        Point p = new Point(x, y);
                        if (isPointInResult(p, tableObjectLCS)) saveFiles.add(file);
                    }
                }
            }
        }
        return saveFiles;
    }

    private List<TableObjectLC> getAllDGNs(String startLC, String endLC) {
        List<TableObjectLC> results = Main.fileData.getArrayList();
        return results.stream()
                .filter(t -> startLC.compareTo(t.getDgnText()) <= 0 && endLC.compareTo(t.getDgnText()) >= 0)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<File> getFiles(List<TableObjectLC> values) {
        List<String> fileNames = new ArrayList<>();
        String name;

        for (TableObjectLC tableObjectLC : values) {
            for (Point point : tableObjectLC.getAllPoints()) {
                name = (int) Math.floor((point.getX() / 1000000)) + "_" + (int) Math.floor((point.getY() / 10000));
                if (!fileNames.contains(name)) fileNames.add(name);

            }
        }
    
        List<File> foundFolders = new ArrayList<>();
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

    private boolean isPointInResult(Point point, List<TableObjectLC> results) {
        Polygon polySquare = new Polygon(
                new int[]{point.x, point.x + 100, point.x + 100, point.x},
                new int[]{point.y, point.y, point.y + 100, point.y + 100},
                4);
        
        for (TableObjectLC tableObjectLC : results) {
            Polygon polygon2 = new Polygon(
                    new int[]{tableObjectLC.getP1().x, tableObjectLC.getP2().x, tableObjectLC.getP3().x, tableObjectLC.getP4().x},
                    new int[]{tableObjectLC.getP1().y, tableObjectLC.getP2().y, tableObjectLC.getP3().y, tableObjectLC.getP4().y},
                    4);
            if (polySquare.intersects(polygon2.getBounds2D())) return true;

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
        createFile(path + "/01173_Orthofotos/", "Jpg");
        createFile(path + "/01173_Orthofotos/", "jgw");
        createFile(path + "/01174_Laserdaten/", "ASC");
        createFile(path + "/01174_Laserdaten/", "LAS");
        createFile(path, "/01177_Hoehenlinien/");
    }

    private void createFile(String path, String name) {
        File file = new File(path + name);
        if (!file.exists()) {
            if (!file.mkdir()) {
                updateMessage("FEHLER! Konnte folgenden Ordner nicht erstellen:  " + name);
            }
        }
    }

    private boolean filesExist(File path) {
        for (String fileName : searchFileNames) {
            File file = new File(path.getPath() + fileName);
            if (!file.exists()) {
                updateMessage("FEHLER! " + fileName + " existiert nicht!");
                return false;
            }
        }
        return true;
    }

    @Override
    protected Void call() {
        startSearch();
        return null;
    }

}
