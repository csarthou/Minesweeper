package com.orange.olps.data;

/**
 * Created by Christophe Sarthou-Camy on 10/02/15.
 */
public class Case {

    private int caseId;
    private int x;
    private int y;
    private boolean hidesMine;
    private boolean isUncovered;
    private Integer adjacentMines;

    public Case(int caseId, int x, int y) {
        this.caseId = caseId;
        this.x = x;
        this.y = y;
        this.hidesMine = false;
        this.isUncovered = false;
        this.adjacentMines = null;
    }

    public int getCaseId() {
        return caseId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean hidesMine() {
        return hidesMine;
    }

    public void setHidesMine(boolean hidesMine) {
        this.hidesMine = hidesMine;
    }

    public boolean isUncovered() {
        return isUncovered;
    }

    public void setUncovered(boolean isUncovered) {
        this.isUncovered = isUncovered;
    }

    public Integer getAdjacentMines() {
        return adjacentMines;
    }

    public void setAdjacentMines(Integer adjacentMines) {
        this.adjacentMines = adjacentMines;
    }
}
