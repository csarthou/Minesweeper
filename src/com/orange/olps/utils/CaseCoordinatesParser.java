package com.orange.olps.utils;

import com.orange.olps.data.CaseCoordinates;

/**
 * Created by Christophe Sarthou-Camy on 10/02/15.
 */
public class CaseCoordinatesParser {

    private static final String COORDINATES_SEPARATOR = ",";

    public static CaseCoordinates parse(String s) {

        CaseCoordinates returnedCaseCoordinates = null;

        if ( (s != null) && (s.contains(COORDINATES_SEPARATOR)) ) {

            String[] splitedString = s.split(COORDINATES_SEPARATOR);
            if (splitedString.length == 2) {

                try {

                    int x = Integer.parseInt(splitedString[0]);
                    int y = Integer.parseInt(splitedString[1]);

                    returnedCaseCoordinates = new CaseCoordinates(x,y);

                } catch (NumberFormatException e) {
                    System.out.println("NumberFormatException :" + e);
                }

            }

        }
        return returnedCaseCoordinates;
    }

    public static String parse(CaseCoordinates cc) {

        String returnedString = "";

        if ( cc != null ) {
            returnedString = cc.getX()+COORDINATES_SEPARATOR+cc.getY();
        }

        return returnedString;
    }

}
