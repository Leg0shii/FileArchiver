package main;

import util.TableObjectLC;

import java.util.ArrayList;

public class FileData {

    private ArrayList<TableObjectLC> arrayList;
    private ArrayList<TableObjectLC> arrayListKM;

    public FileData() {
        this.arrayList = new ArrayList<>();
    }

    public ArrayList<TableObjectLC> getArrayList() {
        return arrayList;
    }

    public ArrayList<TableObjectLC> getArrayListKM() {
        return arrayListKM;
    }

    public void setIdentityHashMap(ArrayList<TableObjectLC> arrayList) {
        this.arrayList = arrayList;
    }

    public void setIdentityHashMapKM(ArrayList<TableObjectLC> arrayListKM) {
        this.arrayListKM = arrayListKM;
    }
}
