package com.example.minesweeper;

public class Cell {

    boolean unCovered;
    boolean mine;
    boolean marked;

    public void initCell() {
        unCovered = false;
        mine = false;
        marked = false;
    }

}
