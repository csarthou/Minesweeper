package com.orange.olps;

import com.orange.olps.application.BoardGameInterface;
import com.orange.olps.application.impl.BoardGame;

public class Main {

    public static void main(String[] args) {

        BoardGameInterface boardGame = new BoardGame();
        boardGame.startGame();

    }
}
