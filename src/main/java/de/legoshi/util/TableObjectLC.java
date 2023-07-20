package de.legoshi.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class TableObjectLC {

    private String dgnText;
    private Point p1;
    private Point p2;
    private Point p3;
    private Point p4;
    private double km;

    public TableObjectLC(String dgnText, Point p1, Point p2, Point p3, Point p4) {
        this.dgnText = dgnText;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
    }

    public List<Point> getAllPoints() {
        return new ArrayList<>() {{
            add(p1);
            add(p2);
            add(p3);
            add(p4);
        }};
    }

}
