package com.metz;

public class Location {
    private int row;
    private int col;

    public Location (int i, int j) {
        row = i;
        col = j;
    }
    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public int getCol() {
        return col;
    }
    public void setCol(int col) {
        this.col = col;
    }
    @Override
    public String toString() {
        return "[" + row + ", " + col + "]";
    }

    public boolean equals (Location other) {
        return this.col == other.col && this.row == other.row;
    }

}


