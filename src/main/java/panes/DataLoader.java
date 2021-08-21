package panes;

import javafx.concurrent.Task;
import main.Main;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import util.TableObjectLC;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;

public class DataLoader extends Task<Void> {

    public File tableFile;
    public File tableFileKM;

    public DataLoader(File tableFile, File tableFileKM) {
        this.tableFile = tableFile;
        this.tableFileKM = tableFileKM;
    }

    @Override
    protected Void call() {

        updateMessage("loading table 1 data");
        updateProgress(0, 100);

        //sets data
        ArrayList<TableObjectLC> list = readTableDGN(tableFile);
        list.sort(Comparator.comparing(TableObjectLC::getDgntxt));

        Main.fileData.setIdentityHashMap(list);

        updateMessage("loading table 2 data");
        updateProgress(0, 100);

        //sets data
        ArrayList<TableObjectLC> list2 = readTableKM(tableFileKM);
        list2.sort(Comparator.comparing(TableObjectLC::getDgntxt));

        Main.fileData.setIdentityHashMapKM(list2);

        return null;
    }

    public ArrayList<TableObjectLC> readTableKM(File tFile) {
        ArrayList<TableObjectLC> arrayList = new ArrayList<>();
        try {
            //obtaining bytes from the file
            updateMessage("1 Reading Inputstream from File!");
            FileInputStream fis = new FileInputStream(tFile);
            updateMessage("2 Reading Inputstream from File!");
            //creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            //creating a Sheet object to retrieve object
            XSSFSheet sheet = wb.getSheetAt(0);
            //iterating over excel file
            updateMessage("Loading .xlsx File into program!");
            double size = sheet.getLastRowNum();
            double progress = 0;

            for (Row row : sheet) {
                if(row.getRowNum() == 0) continue;
                String dgnNUM = row.getCell(2).getStringCellValue();
                double km = trimKM(row.getCell(4).getStringCellValue());
                int x = (int) row.getCell(5).getNumericCellValue();
                int y = (int) row.getCell(6).getNumericCellValue();
                Point p1 = new Point(x+50, y+50);
                Point p2 = new Point(x+50, y-50);
                Point p3 = new Point(x-50, y+50);
                Point p4 = new Point(x-50, y-50);
                arrayList.add(new TableObjectLC(dgnNUM, p1, p2, p3, p4, km));
                System.out.println((progress++/size)*100);
                updateProgress((progress++/size)*100, 200);
            }
        } catch(Exception e) {
            e.printStackTrace();
            updateMessage("ERR: .xlsx");
        }
        return arrayList;
    }

    private double trimKM(String s) {

        double val;
        if(s.contains("+")) {
            String[] str = s.split("[+]");
            val = Double.parseDouble(str[0].trim()) + Double.parseDouble(str[1].trim());
        } else val = Double.parseDouble(s);
        return val;
    }

    public ArrayList<TableObjectLC> readTableDGN(File tFile) {
        ArrayList<TableObjectLC> arrayList = new ArrayList<>();
        try {
            //obtaining bytes from the file
            updateMessage("1 Reading Inputstream from File!");
            FileInputStream fis = new FileInputStream(tFile);
            updateMessage("2 Reading Inputstream from File!");
            //creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            //creating a Sheet object to retrieve object
            XSSFSheet sheet = wb.getSheetAt(0);
            //iterating over excel file
            updateMessage("Loading .xlsx File into program!");
            double size = sheet.getLastRowNum();
            double progress = 0;

            for (Row row : sheet) {
                if(row.getRowNum() == 0) continue;
                String dgnTXT = row.getCell(2).getStringCellValue();
                Point p1 = new Point((int) row.getCell(4).getNumericCellValue(), (int) row.getCell(5).getNumericCellValue());
                Point p2 = new Point((int) row.getCell(6).getNumericCellValue(), (int) row.getCell(7).getNumericCellValue());
                Point p3 = new Point((int) row.getCell(8).getNumericCellValue(), (int) row.getCell(9).getNumericCellValue());
                Point p4 = new Point((int) row.getCell(10).getNumericCellValue(), (int) row.getCell(11).getNumericCellValue());
                arrayList.add(new TableObjectLC(dgnTXT, p1, p2, p3, p4));
                System.out.println((progress++/size)*100);
                updateProgress((progress++/size)*100, 200);
            }
        } catch(Exception e) {
            e.printStackTrace();
            updateMessage("ERR: .xlsx");
        }

        return arrayList;
    }
}
