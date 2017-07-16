package com.company;

import java.util.*;

public class Main {

    private static final String GAME_WON_MESSAGE = "Game Won";
    private static final String GAME_LOSS_MESSAGE = "Game Lost";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter rows, cols and mines");
        Board board;
        try {
            int rows = scanner.nextInt();
            int cols = scanner.nextInt();
            int mines = scanner.nextInt();
            int firstChoiceX = scanner.nextInt();
            int firstChoiceY = scanner.nextInt();
            board = new Board(rows, cols, mines, firstChoiceX, firstChoiceY);

            board.setOnGameOverListener(new Board.OnGameOverListener() {
                @Override
                public void onGameOver(int gameStatus) {
                    if (gameStatus == Board.GAME_WON_STATUS) {
                        System.out.print(GAME_WON_MESSAGE);
                    } else if (gameStatus == Board.GAME_LOST_STATUS) {
                        System.out.print(GAME_LOSS_MESSAGE);
                    }
                }
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}