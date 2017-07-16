package com.company;

/**
 * Created by dam on 16/7/17.
 */
public class Solver {
    int[][] userBoard;
    int rows;
    int cols;

    public class Pair {
        /*
        * Pair class to denote points
        * */
        int x;
        int y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        boolean isValid() {
            return x < rows && x >= 0 && y < cols && y >= 0;
        }
    }

    public Solver(int[][] userBoard) {
        this.userBoard = userBoard;
        this.rows = userBoard.length;
        this.cols = userBoard[0].length;
    }

    Pair getNext() {
        /*
         * returns next optimal choice to be played
         * TODO
         * */

        return null;
    }

}
