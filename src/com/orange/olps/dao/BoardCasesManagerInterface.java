package com.orange.olps.dao;

import com.orange.olps.data.Case;
import com.orange.olps.data.CaseCoordinates;

import java.util.List;

/**
 * Created by Christophe Sarthou-Camy on 10/02/15.
 */
public interface BoardCasesManagerInterface {

    public void createBoardCases(CaseCoordinates gridMaxCaseCoordinates);

    public void createMines(int numberOfMines);

    public Case getCase(CaseCoordinates caseCoordinates);

    public List<Case> getAdjacentCases(Case refCase);

    public int numberOfCoveredCases();

    public void uncoverAllMines();

}
