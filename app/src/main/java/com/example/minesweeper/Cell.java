package com.example.minesweeper;

public class Cell {

    //possible state of a cell
    boolean unCovered;
    boolean mine;
    boolean marked;

    //initialization of a cell
    public void initCell() {
        unCovered = false;
        mine = false;
        marked = false;
    }

}
