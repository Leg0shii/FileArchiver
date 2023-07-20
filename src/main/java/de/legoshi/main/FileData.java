package de.legoshi.main;

import de.legoshi.util.TableObjectLC;

import java.util.ArrayList;
import java.util.List;

public class FileData {

    private List<TableObjectLC> arrayList;
    private List<TableObjectLC> arrayListKM;

    public FileData() {
        this.arrayList = new ArrayList<>();
    }

    public List<TableObjectLC> getArrayList() {
        return arrayList;
    }

    public List<TableObjectLC> getArrayListKM() {
        return arrayListKM;
    }

    public void setIdentityHashMap(List<TableObjectLC> arrayList) {
        this.arrayList = arrayList;
    }

    public void setIdentityHashMapKM(List<TableObjectLC> arrayListKM) {
        this.arrayListKM = arrayListKM;
    }
}
