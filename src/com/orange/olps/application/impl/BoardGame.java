package com.orange.olps.application.impl;

import com.orange.olps.application.BoardGameInterface;
import com.orange.olps.dao.BoardCasesManagerInterface;
import com.orange.olps.dao.impl.BoardCasesManager;
import com.orange.olps.data.Case;
import com.orange.olps.data.CaseCoordinates;
import com.orange.olps.utils.CaseCoordinatesParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Christophe Sarthou-Camy on 10/02/15.
 */
public class BoardGame implements BoardGameInterface {

    private static final int GRID_MIN_SIZE = 4;
    private static final int GRID_MAX_SIZE = 4444;
    private static final float MAX_MINES_RATIO = 1f/2f;

    private CaseCoordinates maxCaseCoordinates;
    private int numberOfMines;
    private boolean onlyMinesAreHidden;
    private boolean isHideMineUncovered;

    private BoardCasesManagerInterface boardCasesManager;


    public void startGame() {

        numberOfMines = 0;
        onlyMinesAreHidden = false;
        isHideMineUncovered = false;


        maxCaseCoordinates = askGridSize();
        while(maxCaseCoordinates == null) {
            maxCaseCoordinates = askGridSize();
        }

        numberOfMines = askNumberOfMines();
        while (numberOfMines == 0) {
            numberOfMines = askNumberOfMines();
        }

        boardCasesManager = new BoardCasesManager();
        boardCasesManager.createBoardCases(maxCaseCoordinates);
        boardCasesManager.createMines(numberOfMines);


        CaseCoordinates caseCoordinatesToUncover;
        while (!isEndOfGame()) {

            showBoard();

            caseCoordinatesToUncover = askCaseToUncover();
            while(caseCoordinatesToUncover == null) {
                caseCoordinatesToUncover = askCaseToUncover();
            }

            Case caseToUncover = boardCasesManager.getCase(caseCoordinatesToUncover);
            uncoverCase(caseToUncover);

            if (boardCasesManager.numberOfCoveredCases() <= numberOfMines) {
                onlyMinesAreHidden = true;
            }

        }

        boardCasesManager.uncoverAllMines();
        showBoard();

        if (onlyMinesAreHidden && !isHideMineUncovered) {
            System.out.println("Bravo, vous avez gagné!");
        } else if (isHideMineUncovered) {
            System.out.println("Vous êtes tombé sur une mine!");
        }

    }

    private void showBoard() {

        String linesSeparator = "-";

        for (int x=1; x<=maxCaseCoordinates.getX(); x++) {
            linesSeparator += "----";
        }
        System.out.println(linesSeparator);

        for (int y=1; y<=maxCaseCoordinates.getY(); y++) {

            System.out.print("|");
            for (int x=1; x<=maxCaseCoordinates.getX(); x++) {

                Case tempCase = boardCasesManager.getCase(new CaseCoordinates(x,y));
                if (tempCase.isUncovered()) {
                    if (tempCase.hidesMine()) {
                        System.out.print(" * ");
                    } else {
                        System.out.print(" "+tempCase.getAdjacentMines()+" ");
                    }
                } else {
                    System.out.print("   ");
                }
                System.out.print("|");
            }
            System.out.println("");

            System.out.println(linesSeparator);

        }


    }

    private void uncoverCase(Case caseToUncover) {

        if (caseToUncover.isUncovered()) {
            //System.out.println("Cette case est déjà découverte!");
        } else {
            if (caseToUncover.hidesMine()) {
                isHideMineUncovered = true;
                caseToUncover.setUncovered(true);
                //System.out.println("Vous êtes tombé sur une mine - Fin du jeu!");
            } else {
                // show number of adjacent mines
                List<Case> adjacentCases = boardCasesManager.getAdjacentCases(caseToUncover);
                //System.out.println("Nombre de cases adjacentes à ("+caseToUncover.getX()+","+caseToUncover.getY()+") : "+adjacentCases.size());

                if (adjacentCases != null) {

                    Iterator<Case> it = adjacentCases.iterator();
                    int numberOfAdjacentMines = 0;
                    while(it.hasNext()) {
                        if (it.next().hidesMine()) {
                            numberOfAdjacentMines++;
                        }
                    }

                    caseToUncover.setAdjacentMines(numberOfAdjacentMines);
                    caseToUncover.setUncovered(true);

                    if (numberOfAdjacentMines == 0) {
                        System.out.println("numberOfAdjacentMines == 0 ");
                        Iterator<Case> it2 = adjacentCases.iterator();
                        while(it2.hasNext()) {
                            uncoverCase(it2.next());
                        }
                    }

                }
            }
        }


    }


    private CaseCoordinates askGridSize() {

        CaseCoordinates returnedCaseCoordinates = null;

        returnedCaseCoordinates = askCaseCoordinates("Entrez la taille du démineur au format x,y :");

        if (returnedCaseCoordinates != null) {
            int gridSize = returnedCaseCoordinates.getX()*returnedCaseCoordinates.getY();

            if (gridSize < GRID_MIN_SIZE) {
                System.out.println("Cette taille de démineur est trop petite");
                returnedCaseCoordinates = null;
            } else if (gridSize > GRID_MAX_SIZE) {
                System.out.println("Cette taille de démineur est trop grande");
                returnedCaseCoordinates = null;
            }
        }

        return returnedCaseCoordinates;

    }

    private CaseCoordinates askCaseToUncover() {

        CaseCoordinates returnedCaseCoordinates = null;

        returnedCaseCoordinates = askCaseCoordinates("Entrez la case à découvrir au format x,y :");

        if (returnedCaseCoordinates != null) {

            if ( (returnedCaseCoordinates.getX() > maxCaseCoordinates.getX()) || (returnedCaseCoordinates.getX() < 1) ) {
                System.out.println("Cette case de n'est pas dans le plateau du jeu");
                returnedCaseCoordinates = null;
            } else if ( (returnedCaseCoordinates.getY() > maxCaseCoordinates.getY()) || (returnedCaseCoordinates.getY() < 1) ) {
                System.out.println("Cette case de n'est pas dans le plateau du jeu");
                returnedCaseCoordinates = null;
            }

        }

        return returnedCaseCoordinates;

    }

    private CaseCoordinates askCaseCoordinates(String question) {

        CaseCoordinates returnedCaseCoordinates = null;

        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        System.out.println(question);

        try {

            String coordinatesEnteredByKeyboard=keyboard.readLine();
            returnedCaseCoordinates = CaseCoordinatesParser.parse(coordinatesEnteredByKeyboard);

        } catch(IOException e) {
            System.out.println("IOException :"+e);
        }

        return returnedCaseCoordinates;
    }


    private int askNumberOfMines() {

        int returnedInt = 0;

        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        int maxNumberOfMines = (int) (( maxCaseCoordinates.getX() * maxCaseCoordinates.getY() ) * MAX_MINES_RATIO);

        System.out.println("Entrez le nombre de mines (max "+maxNumberOfMines+") :");

        try {

            String numberOfMinesEnteredByKeyboard=keyboard.readLine();
            if (numberOfMinesEnteredByKeyboard != null) {
                int i = Integer.parseInt(numberOfMinesEnteredByKeyboard);
                if (i <= maxNumberOfMines) {
                    returnedInt = i;
                }
            }

        } catch(IOException e) {
            System.out.println("IOException :"+e);
        } catch(NumberFormatException e) {
            System.out.println("NumberFormatException :"+e);
        }

        return returnedInt;
    }

    private boolean isEndOfGame() {
        return onlyMinesAreHidden || isHideMineUncovered;
    }

}
