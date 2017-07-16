package com.company;


import java.util.Scanner;

import java.util.concurrent.ThreadLocalRandom;


/**
 * Created by dam on 15/7/17.
 */
public class Board {

    public static final int GAME_WON_STATUS = 1;
    public static final int GAME_LOST_STATUS = 0;
    public static final int USER_BOARD_CLOSED = -1;
    public static final int USER_BOARD_MARK_SURE = -2;
    public static final int USER_BOARD_MARK_CAN_CHANGE = -3;

    private static final String LOT_OF_MINES_EXCEPTION_MESSAGE = "You want an easy game don't you";
    private static final String INVALID_MINE_OPENED_MESSAGE = "invalid mine opened";
    private static final String INVALID_MINE_MARKED_MESSAGE = "invalid mine marked";
    private static final String MINE_CELL = "*";
    private static final String MINE_SURE_CELL = "[S]";
    private static final String MINE_CAN_CHANGE_CELL = "[C]";
    private static final String CLOSED_CELL = "[ ]";

    private static final char MARK_MINE_TYPE = 'm';
    private static final char OPEN_MINE_TYPE = 'o';

    private static final int MINE = -1;
    private static final int MINE_SURE = -2;
    private static final int MINE_CAN_CHANGE = -3;
    private static final int OPENED = 1;
    private final int CLOSED = 0;

    private OnGameOverListener onGameOverListener;
    private int rows;
    private int cols;
    private int mines;
    private int[][] board;
    private int[][] opened;
    private int firstChoiceX;
    private int firstChoiceY;

    public void setOnGameOverListener(OnGameOverListener onGameOverListener) {
        this.onGameOverListener = onGameOverListener;
    }

    public Board(int rows, int cols, int mines, int firstChoiceX, int firstChoiceY) throws Exception {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.firstChoiceX = firstChoiceX;
        this.firstChoiceY = firstChoiceY;
        if (mines >= 0.5 * rows * cols) {
            throw new Exception(LOT_OF_MINES_EXCEPTION_MESSAGE);
        }
        board = new int[rows][cols];
        opened = new int[rows][cols];
        initialize();
    }


    private void initialize() {
        /*
         * Initializes board keeping firstChoice (first entered by user),
         * rows, cols, mines entered by user.
         * */
        for (int minesPlacedSoFar = 0; minesPlacedSoFar < mines; ++minesPlacedSoFar) {
            int randX = ThreadLocalRandom.current().nextInt(1, rows);
            int randY = ThreadLocalRandom.current().nextInt(1, cols);
            while (isMine(randX, randY) || (randX == firstChoiceX && randY == firstChoiceY)) {
                randX = ThreadLocalRandom.current().nextInt(1, rows);
                randY = ThreadLocalRandom.current().nextInt(1, cols);
            }
            board[randX][randY] = MINE;
        }
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (!isMine(i, j)) {
                    for (int k = -1; k <= 1; ++k) {
                        for (int l = -1; l <= 1; ++l) {
                            if (isMine(i + k, j + l)) {
                                board[i][j]++;
                            }
                        }
                    }
                }
            }
        }
        openMine(firstChoiceX, firstChoiceY);
        printBoard();
        takeInput();

    }

    private void takeInput() {
        /*
        * Takes input regularly and updates board
        * */

        while (!isGameCompleted()) {
            Scanner scanner = new Scanner(System.in);
            char type = scanner.next().charAt(0);
            int mineX = scanner.nextInt();
            int mineY = scanner.nextInt();
            if (type == MARK_MINE_TYPE) {
                markMine(mineX, mineY);
            }
            if (type == OPEN_MINE_TYPE) {
                openMine(mineX, mineY);
            }
            printBoard();
        }
    }

    private boolean isMine(int i, int j) {
        /*
         *  returns true if there is a mine on index i, j
         * */
        return isValid(i, j) && board[i][j] == MINE;
    }

    private boolean isValid(int i, int j) {
         /*
          *  returns true if index i, j is valid
          * */
        return i >= 0 && i < rows && j >= 0 && j < cols;
    }

    private boolean isGameCompleted() {
        /*
        * returns true if game is completed
        * */
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (opened[i][j] != OPENED && !isMine(i, j))
                    return false;
            }
        }
        return true;
    }

    private void printBoard() {
        /*
        * Output the board on STDOUT
        * */
        System.out.print("  ");
        for (int i = 0; i < rows; ++i)
            System.out.print(" " + i + " ");
        System.out.println();
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (j == 0)
                    System.out.print(i + " ");
                if (opened[i][j] == OPENED) {
                    System.out.print(" " + board[i][j] + " ");
                } else if (opened[i][j] == CLOSED) {
                    System.out.print(CLOSED_CELL);
                } else if (opened[i][j] == MINE_CAN_CHANGE)
                    System.out.print(MINE_CAN_CHANGE_CELL);
                else if (opened[i][j] == MINE_SURE)
                    System.out.print(MINE_SURE_CELL);

            }
            System.out.println();
        }
    }

    private void markMine(int x, int y) {
        /*
        * puts flag on block
        * */
        if (!isValid(x, y) || opened[x][y] == OPENED) {
            System.out.println(INVALID_MINE_MARKED_MESSAGE);
        }
        if (opened[x][y] == MINE_CAN_CHANGE)
            opened[x][y] = MINE_SURE;
        else if (opened[x][y] == MINE_SURE)
            opened[x][y] = CLOSED;
        else if (opened[x][y] == CLOSED)
            opened[x][y] = MINE_CAN_CHANGE;

    }

    private void openMine(int x, int y) {
        /*
        * opens a mine at given index
        * */
        if (!isValid(x, y) || opened[x][y] != CLOSED) {
            System.out.println(INVALID_MINE_OPENED_MESSAGE);
            printBoard();
            return;
        }
        if (board[x][y] == 0)
            openBoardFromThisPoint(x, y);
        if (isMine(x, y)) {
            onGameOverListener.onGameOver(GAME_LOST_STATUS);
            return;
        }
        opened[x][y] = OPENED;
        if (isGameCompleted()) {
            onGameOverListener.onGameOver(GAME_WON_STATUS);
        }

    }


    private void openBoardFromThisPoint(int x, int y) {
        /*
        *  opens board from this point if (0) is found
        * */
        if (board[x][y] == MINE)
            return;
        opened[x][y] = OPENED;
        if (board[x][y] == 0) {
            for (int i = -1; i <= 1; ++i) {
                for (int j = -1; j <= 1; ++j) {
                    if (isValid(x + i, y + j) && opened[x + i][y + j] == CLOSED) {
                        openBoardFromThisPoint(x + i, y + j);
                    }
                }
            }
        }
    }

    private int[][] getUserBoard() {
        /*
        * User Board if user wants to get board displayed
        * If opened than int val
        * otherwise MARK TYPE
        * or
        * CLOSED_USER_BOARD
        * */
        int[][] userBoard = new int[rows][cols];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                switch (opened[i][j]) {
                    case OPENED: {
                        userBoard[i][j] = board[i][j];
                        break;
                    }
                    case CLOSED: {
                        userBoard[i][j] = USER_BOARD_CLOSED;
                    }
                    case MINE_CAN_CHANGE: {
                        userBoard[i][j] = USER_BOARD_MARK_CAN_CHANGE;
                    }
                    case MINE_SURE: {
                        userBoard[i][j] = USER_BOARD_MARK_SURE;
                    }
                }
            }
        }
        return userBoard;
    }

    public interface OnGameOverListener {
        /*
        * Override this interface to deal with game lost or won.
        * */
        void onGameOver(int gameStatus);
    }
}
