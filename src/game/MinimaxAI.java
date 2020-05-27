/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.*;

/**
 *
 * @author victorgao
 */
public class MinimaxAI {

    public static int[][] directions = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}}; //Array of directions

    public static final int lineOf2 = 2; //Score for a line of 2 (AI)
    public static final int lineOf3 = 5; //Score for a line of 3 (AI)
    public static final int lineOf4 = 1000; ///Score for a line of 4 (AI)

    public static final int lineOf2Opp = 2; //Score for a line of 2 (OPP)
    public static final int lineOf3Opp = 7; //Score for a line of 3 (OPP)
    public static final int lineOf4Opp = 1000; //Score for a line of 4 (OPP)

    public static final int center = 5; //Score for a center piece

    public static int initialDepth; //Depth of the minimax algorithm

    public static int player1; //Integer that represents player1 (human)
    public static int player2; //Integer that represents player 2 (AI)

    public static ArrayList<Integer> scores = new ArrayList(); //Arraylist that stores the scores for each move (calculated using minimax)

    /**
     *
     * @param depth: Number of moves ahead does the algorithm check
     * @param inPlayer1: Integer that represents player1 (human)
     * @param inPlayer2: Integer that represents player 2 (AI)
     */
    public MinimaxAI(int depth, int inPlayer1, int inPlayer2) {
        initialDepth = depth;
        player1 = inPlayer1;
        player2 = inPlayer2;
    }

    /**
     *
     * MINIMAX ALGORITHM
     *
     * @param depth: Number of moves ahead does the algorithm check
     * @param aiTurn: True if its the AI's turn (Always true when method is
     * first called)
     * @param board: The current boardstate that is passed
     * @return the score of a certain move in the future
     */
    public static int move(int depth, boolean aiTurn, int[][] board) {

        //System.out.println("start of move method, depth " + depth); //->DEBUG
        if (depth == 0 || isOver(board)) { //Terminating condition
            //System.out.println(aiTurn); ->DEBUG
            int score = getScore(board, player2, player1); //Calls getScore method to return a static evaluation of the boardstate
            if (depth == initialDepth - 1) { //When the second to last moves are calculated, store the scores in the arraylist
                scores.add(score);
            }
            //System.out.println("score: " + score); //->DEBUG
            return score; //Return that score
        }

        if (aiTurn) { //If its the AI's turn, chooses the score is the biggest
            int maxEval = Integer.MIN_VALUE; //Initializing maxEval to the lowest possible value

            for (int i : legalMoves(board)) { //For loop iterating through all the LEGAL MOVES the AI can take
                //System.out.println(Arrays.toString(legalMoves(board))); ->DEBUG
                //System.out.println(i); ->DEBUG

                int eval = move(depth - 1, !aiTurn, updateBoard(board, i, player2)); //Recursive call to get the scores of the next possible future moves
                if (eval > maxEval) { //If checked boardstate has a higher value, update maxEval
                    maxEval = eval;
                }
            }
            //System.out.println("maxEval" + maxEval); ->DEBUG
            return maxEval;

        } else { //If its the opponent's turn, AI assumes the opponent will choose the lowest score for the AI (minimizing player)
            int minEval = Integer.MAX_VALUE; //Initializing minEval to the lowest possible value

            for (int i : legalMoves(board)) { //Same alogrithm as the max player, only player values are swapped
                //System.out.println(Arrays.toString(legalMoves(board))); ->DEBUG
                //System.out.println(i); ->DEBUG

                int eval = move(depth - 1, !aiTurn, updateBoard(board, i, player1));
                if (eval < minEval) {
                    minEval = eval;
                }

            }
            if (depth == initialDepth - 1) { //When the second to last moves are calculated, store the scores in the arraylist
                scores.add(minEval);
            }
            //System.out.println("minEval" + minEval); ->DEBUG
            return minEval;
        }

    }

    /**
     *
     * @param scores: The arrayList that stores all the scores of the second to
     * last move
     * @param score: The score that is the largest in scores
     * @param board: The boardstate
     * @return the COLUMN NUMBER (0-6) for which the best move can be played
     */
    public static int makeMove(int score, int[][] board) {
        ArrayList<Integer> missing = new ArrayList<Integer>(); //Arraylist for legal moves

        for (int i = 0; i < legalMoves(board).length; i++) {
            missing.add(legalMoves(board)[i]); //Copy the legal move indexes to the arrayList
        }
        for (int i = 0; i < board[0].length; i++) {
            //System.out.println("scores before add: " + scores); //->DEBUG
            if (!missing.contains(i)) { //If a value is considered illegal (not in missing)
                scores.add(i, Integer.MIN_VALUE); //Add a placeholder value
            }
        } //This ensures that all score arrayLists have 7 elements and the proper column is returned

        //System.out.println("target: " + score); //->DEBUG
        //System.out.println("score: " + scores); //->DEBUG
        int index = scores.indexOf(score); //Finds the index of the score

        scores.clear(); //Clear the array for the next move
        return index;
    }

    /**
     * @param target: The target to be searched for
     * @param arr: The array to be searched
     * @return the first occurrence of the target
     */
    public int indexOf(int target, int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (target == arr[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param board: The old boardstate
     * @param col: The column for which the piece is dropped in
     * @param value: The player value
     * @return newBoard: The updated board state
     */
    public static int[][] updateBoard(int[][] board, int col, int value) {
        int[][] newBoard = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                newBoard[i][j] = board[i][j];
            }
        }
        int ypos = getRow(col, board); //Calls getRow to get the row of the piece

        newBoard[ypos][col] = value; //Updates value

        /*
        System.out.println("Updated Board: ");
        for (int i = 0; i < 6; i++) {
            System.out.println(Arrays.toString(newBoard[i]));
        }
         */
        //->DEBUG
        return newBoard;

    }

    /**
     * @param board: The boardstate
     * @return legMoves the array containing the columns of the legal moves
     */
    public static int[] legalMoves(int[][] board) {
        ArrayList<Integer> legal = new ArrayList(); //Copy to arrayList
        for (int i = 0; i < board[0].length; i++) {
            if (board[0][i] == -1) { //If the top column is empty, add the col
                legal.add(i);
            }
        }
        int[] legMoves = new int[legal.size()];
        for (int i = 0; i < legal.size(); i++) {
            legMoves[i] = legal.get(i); //Copy back to array
        }

        return legMoves;
    }

    /**
     * @param board: The boardstate
     * @param value1: The player value that earns points (2 for AI)
     * @param value2: The player value that substracts points (opponent)
     * @return score, the score of the boardstate
     */
    public static int getScore(int[][] board, int value1, int value2) {
        int score = 0;

        for (int i = board.length - 1; i >= 0; i--) {
            for (int j = 0; j < board[0].length; j++) {
                Point p = new Point(j, i); //Checking every possible point on the board

                //System.out.println("Point: " + j + ", " + i); ->DEBUG
                score += linesOf(p, value1, value2, board, lineOf2, lineOf3, lineOf4); //Calls linesOf method to calculate score
                score -= linesOf(p, value2, value1, board, lineOf2Opp, lineOf3Opp, lineOf4Opp); //Calls linesOf method to calculate score

            }
        }
        for (int i = 0; i < board.length; i++) { //Adding score for any center pieces
            if (board[i][board[0].length / 2] == value1) {
                score += center;
            } else if (board[i][board[0].length / 2] == value2) {
                score -= center;
            }
        }
        return score;
    }

    /**
     * @param p: The point to be checked
     * @param value1: The player value that earns points
     * @param value2: The player value that subtracts points
     * @param board: The boardstate
     * @param lineOf2: Point value that is earned from a line of 2
     * @param lineOf3: Point value that is earned from a line of 3
     * @param lineOf4: Point value that is earned from a line of 4
     * @return totalScore, the score point p gets
     */
    public static int linesOf(Point p, int value1, int value2, int[][] board, int lineOf2, int lineOf3, int lineOf4) {

        int totalScore = 0;
        for (int i = 0; i < 4; i++) { //Checking each direction

            int count = 0;

            for (int k = 0; k < 4; k++) {
                int posX = p.getX() + k * directions[i][0];
                int posY = p.getY() + k * directions[i][1];

                try {
                    if (board[posY][posX] == value1) { //If consecutive value is found
                        count++;
                    } else if (board[posY][posX] == value2) { //If opposite value is found, break
                        count = 0;
                        break;
                    }

                } catch (ArrayIndexOutOfBoundsException e) { //If array out of bounds, break
                    count = 0;
                    break;
                }

            }
            if (count == 2) {
                totalScore += lineOf2; //Line of 2
            } else if (count == 3) {
                totalScore += lineOf3; //Line of 3
            } else if (count == 4) {
                totalScore += lineOf4; //Line of 4
            }
        }
        return totalScore;

    }

    /**
     * @param col: the column the user selects
     * @param board: the board the game is played on
     * @return the row where the piece should be, -1 if the column is filled
     */
    public static int getRow(int col, int[][] board) {
        int row;
        for (int i = board.length - 1; i >= 0; i--) { //For loop iterates through each y position
            if (board[i][col] == -1) { //Finds the lowest y position and returns that row
                row = i;

                return row;
            }
        }
        return -1;
    }

    
    /**
     * 
     * @param board: The board state
     * @return true if any player has won the game
     */
    public static boolean isOver(int[][] board) {
        for (int i = board.length - 1; i >= 0; i--) {
            for (int j = 0; j < board[0].length; j++) {

                for (int k = 0; k < 4; k++) {

                    if ((checkWin(j, i, player1, k, board) + checkWin(j, i, player1, k + 4, board) >= 3 && (board[i][j] == player1)) //Checking lines of 4 or more
                            || (checkWin(j, i, player2, k, board) + checkWin(j, i, player2, k + 4, board) >= 3 && (board[i][j] == player2))) {
                        //System.out.println("over"); //->DEBUG
                        return true;
                    }
                }

            }
        }
        return false;
    }

    public static int checkWin(int x, int y, int value, int dcount, int[][] board) {

        x += directions[dcount][0]; //x and y are updated to be an adjacent piece
        y += directions[dcount][1];

        try {

            if (board[y][x] == value) { //If the piece adjacent has the same value
                return 1 + checkWin(x, y, value, dcount, board); //Add 1 and then check the next adjacent piece
            } else {
                return 0; //Return 0 if the adjacent piece does not have the same value
            }

        } catch (ArrayIndexOutOfBoundsException e) { //Catch the array out of bounds exception
            return 0; //If OutOfBounds, return 0 (since there is no piece adjacent)
        }

    }
    /*
    public static void main(String[] args) {
        int[][] preset = {{-1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1},
        {-1, -1, -1, -1, -1, -1, -1},
        {2, -1, 2, 2, 2, 1, -1}};

        System.out.println(makeMove(move(initialDepth, true, preset), preset));

    }
     */
    //->DEBUG
}
