package de.legoshi.panes;

import de.legoshi.util.LoadingState;
import javafx.concurrent.Task;
import de.legoshi.main.Main;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import de.legoshi.util.TableObjectLC;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Setter
public class DataLoader extends Task<LoadingState> {
    
    // return 0 - all failed
    // return 1 - DGN success
    // return 2 - KM success
    // return 3 - all success
    
    private File fileDGN;
    private File fileKM;
    private boolean dgn = false;
    private boolean km = false;
    
    public LoadingState readTableKM(File tFile) {
        List<TableObjectLC> list = new ArrayList<>();
        try {
            // obtaining bytes from the file
            updateProgress(0, 100);
            updateMessage("Lese KM Strecke + Kilometer!");
            XSSFSheet sheet = getSheet(tFile);
            
            if (sheet == null) return LoadingState.FAIL;
            
            double size = sheet.getLastRowNum();
            AtomicReference<Double> progress = new AtomicReference<>((double) 0);
            
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                String dgnNUM = row.getCell(2).getStringCellValue();
                double km = trimKM(row.getCell(4).getStringCellValue());
                int x = (int) row.getCell(5).getNumericCellValue();
                int y = (int) row.getCell(6).getNumericCellValue();
                Point p1 = new Point(x + 50, y + 50);
                Point p2 = new Point(x + 50, y - 50);
                Point p3 = new Point(x - 50, y + 50);
                Point p4 = new Point(x - 50, y - 50);
                list.add(new TableObjectLC(dgnNUM, p1, p2, p3, p4, km));
                
                progress.getAndSet(progress.get() + 1);
                updateProgress((progress.get() / size) * 100, 100);
            }
        } catch (Exception e) {
            e.printStackTrace();
            updateMessage("FEHLER! Fehler mit .xlsx Datei");
            return LoadingState.FAIL;
        }
        Main.fileData.setIdentityHashMapKM(list);
        updateMessage("Fertig geladen!");
        return LoadingState.KM_SUCCESS;
    }
    
    public LoadingState readTableDGN(File tFile) {
        List<TableObjectLC> list = new ArrayList<>();
        try {
            //obtaining bytes from the file
            updateMessage("Lese Blattschnitte als Input Stream!");
            XSSFSheet sheet = getSheet(tFile);
            
            if (sheet == null) return LoadingState.FAIL;
            
            //iterating over excel file
            double size = sheet.getLastRowNum();
            AtomicReference<Double> progress = new AtomicReference<>((double) 0);
            
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                String dgnTXT = row.getCell(2).getStringCellValue();
                Point p1 = new Point((int) row.getCell(4).getNumericCellValue(), (int) row.getCell(5).getNumericCellValue());
                Point p2 = new Point((int) row.getCell(6).getNumericCellValue(), (int) row.getCell(7).getNumericCellValue());
                Point p3 = new Point((int) row.getCell(8).getNumericCellValue(), (int) row.getCell(9).getNumericCellValue());
                Point p4 = new Point((int) row.getCell(10).getNumericCellValue(), (int) row.getCell(11).getNumericCellValue());
                list.add(new TableObjectLC(dgnTXT, p1, p2, p3, p4));
                
                progress.getAndSet(progress.get() + 1);
                updateProgress((progress.get() / size) * 100, 100);
            }
        } catch (Exception e) {
            e.printStackTrace();
            updateMessage("FEHLER! Fehler mit .xlsx Datei");
            return LoadingState.FAIL;
        }
        
        Main.fileData.setIdentityHashMap(list);
        updateMessage("Fertig geladen!");
        return LoadingState.DGN_SUCCESS;
    }
    
    private double trimKM(String s) {
        double val;
        if (s.contains("+")) {
            String[] str = s.split("[+]");
            val = Double.parseDouble(str[0].trim()) + Double.parseDouble(str[1].trim());
        } else val = Double.parseDouble(s);
        return val;
    }
    
    private XSSFSheet getSheet(File tFile) {
        FileInputStream fis;
        XSSFWorkbook wb;
        try {
            fis = new FileInputStream(tFile);
            wb = new XSSFWorkbook(fis);
        } catch (IOException e) {
            updateMessage("FEHLER! Datei existiert nicht!");
            return null;
        }
        XSSFSheet sheet = wb.getSheetAt(0);
        updateMessage("Lade .xlsx Datei ins Programm!");
        return sheet;
    }
    
    @Override
    protected LoadingState call() {
        LoadingState dgnState = dgn ? readTableDGN(fileDGN) : LoadingState.FAIL;
        LoadingState kmState = km ? readTableKM(fileKM) : LoadingState.FAIL;
        
        LoadingState success = determineSuccess(dgnState, kmState);
        
        updateProgress(0, 100);
        return success;
    }
    
    private LoadingState determineSuccess(LoadingState dgnState, LoadingState kmState) {
        if (dgnState == LoadingState.DGN_SUCCESS && kmState == LoadingState.KM_SUCCESS) {
            return LoadingState.SUCCESS;
        }
        if (dgnState == LoadingState.DGN_SUCCESS) {
            return LoadingState.DGN_SUCCESS;
        }
        if (kmState == LoadingState.KM_SUCCESS) {
            return LoadingState.KM_SUCCESS;
        }
        return LoadingState.FAIL;
    }
}
