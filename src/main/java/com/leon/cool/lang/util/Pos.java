package com.leon.cool.lang.util;

/**
 * Created by leon on 15-11-1.
 */
public class Pos {
    public int column;
    public int row;

    public Pos(int column, int row) {
        this.column = column;
        this.row = row;
    }

    @Override
    public String toString() {
        return "Pos{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }
}
