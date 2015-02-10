package com.orange.olps.dao.impl;

import com.orange.olps.dao.BoardCasesManagerInterface;
import com.orange.olps.data.Case;
import com.orange.olps.data.CaseCoordinates;

import java.util.*;

/**
 * Created by Christophe Sarthou-Camy on 10/02/15.
 */
public class BoardCasesManager implements BoardCasesManagerInterface {

    private int boardSize = 0;
    private int boardWidth = 0;
    private int boardHeight = 0;
    private HashMap<Integer,Case> boardCases;
    private Random random;


    public BoardCasesManager() {
        random = new Random();
    }


    public void createBoardCases(CaseCoordinates gridMaxCaseCoordinates) {

        boardWidth = gridMaxCaseCoordinates.getX();
        boardHeight = gridMaxCaseCoordinates.getY();
        boardSize = gridMaxCaseCoordinates.getX() * gridMaxCaseCoordinates.getY();
        boardCases = new HashMap<Integer,Case>();

        int caseId = 1;

        for (int y=1; y<=boardHeight; y++) {

            for (int x=1; x<=boardWidth; x++) {

                boardCases.put(caseId, new Case(caseId,x,y));
                caseId++;

            }

        }

    }

    public void createMines(int numberOfMines) {

        int randomCaseId = random.nextInt(boardSize) + 1;

        Case randomCase = boardCases.get(randomCaseId);

        if ( randomCase.hidesMine() ) {
            // This case was already a mine
            createMines(numberOfMines);
        } else {
            randomCase.setHidesMine(true);
            if (numberOfMines > 1) {
                createMines(numberOfMines-1);
            }
        }

    }

    public Case getCase(CaseCoordinates caseCoordinates) {
        int caseId = (caseCoordinates.getY()-1)*boardWidth + caseCoordinates.getX();
        return boardCases.get(caseId);
    }

    public List<Case> getAdjacentCases(Case refCase) {
        ArrayList<Case> returnedList = new ArrayList<Case>();

        CaseCoordinates topLeftCaseCoordinates = new CaseCoordinates(refCase.getX()-1,refCase.getY()-1);
        if (isCaseCoordinatesInBoard(topLeftCaseCoordinates)) {
            returnedList.add(getCase(topLeftCaseCoordinates));
        }

        CaseCoordinates topMiddleCaseCoordinates = new CaseCoordinates(refCase.getX(),refCase.getY()-1);
        if (isCaseCoordinatesInBoard(topMiddleCaseCoordinates)) {
            returnedList.add(getCase(topMiddleCaseCoordinates));
        }

        CaseCoordinates topRightCaseCoordinates = new CaseCoordinates(refCase.getX()+1,refCase.getY()-1);
        if (isCaseCoordinatesInBoard(topRightCaseCoordinates)) {
            returnedList.add(getCase(topRightCaseCoordinates));
        }

        CaseCoordinates centerLeftCaseCoordinates = new CaseCoordinates(refCase.getX()-1,refCase.getY());
        if (isCaseCoordinatesInBoard(centerLeftCaseCoordinates)) {
            returnedList.add(getCase(centerLeftCaseCoordinates));
        }

        CaseCoordinates centerRightCaseCoordinates = new CaseCoordinates(refCase.getX()+1,refCase.getY());
        if (isCaseCoordinatesInBoard(centerRightCaseCoordinates)) {
            returnedList.add(getCase(centerRightCaseCoordinates));
        }

        CaseCoordinates bottomLeftCaseCoordinates = new CaseCoordinates(refCase.getX()-1,refCase.getY()+1);
        if (isCaseCoordinatesInBoard(bottomLeftCaseCoordinates)) {
            returnedList.add(getCase(bottomLeftCaseCoordinates));
        }

        CaseCoordinates bottomMiddleCaseCoordinates = new CaseCoordinates(refCase.getX(),refCase.getY()+1);
        if (isCaseCoordinatesInBoard(bottomMiddleCaseCoordinates)) {
            returnedList.add(getCase(bottomMiddleCaseCoordinates));
        }

        CaseCoordinates bottomRightCaseCoordinates = new CaseCoordinates(refCase.getX()+1,refCase.getY()+1);
        if (isCaseCoordinatesInBoard(bottomRightCaseCoordinates)) {
            returnedList.add(getCase(bottomRightCaseCoordinates));
        }

        return returnedList;
    }

    public int numberOfCoveredCases() {
        int returnedNumberOfCoveredCases = 0;

        Collection<Case> casesCollection = boardCases.values();
        if (casesCollection != null) {
            Iterator<Case> it = casesCollection.iterator();
            while (it.hasNext()) {
                if (! it.next().isUncovered()) {
                    returnedNumberOfCoveredCases ++;
                }
            }

        }
        return returnedNumberOfCoveredCases;
    }

    public void uncoverAllMines() {

        Collection<Case> casesCollection = boardCases.values();
        if (casesCollection != null) {
            Iterator<Case> it = casesCollection.iterator();
            while (it.hasNext()) {
                Case tempCase = it.next();
                if (tempCase.hidesMine() && !tempCase.isUncovered()) {
                    tempCase.setUncovered(true);
                }
            }

        }
    }

    private boolean isCaseCoordinatesInBoard(CaseCoordinates caseCoordinates) {

        if ( (caseCoordinates.getX()>=1)
                && (caseCoordinates.getX()<=boardWidth)
                && (caseCoordinates.getY()>=1)
                && (caseCoordinates.getY()<=boardHeight) ) {
            return true;
        }

        return false;
    }
}
