/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ics4u_connect4;

import java.util.*;

/**
 *
 * @author victorgao
 */
public class Minimax_Console {

    public static int[][] board = new int[6][7]; //2D array of ints representing the board
    public static int[][] directions = {{0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}}; //Array of directions

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in); //Scanner to take user input
        MinimaxAI ai = new MinimaxAI(3, 1, 2); //3 is the depth (difficult of the ai, 1 represents the player piece, 2 represents the AI piece, AI always goes second

        boolean p1Win = false; //Initializing win conditions to false
        boolean p2Win = false;
        int countMoves = 0; //Counter which counts the total number of moves made
        showBoard(board); //Display board

        do {

            int col = sc.nextInt() - 1; //User selects which column to drop the piece
            int row = placePiece(col, 1, board); //Calls placePiece method to determine which row the piece is placed

            while (row == -1) { //Ensures each move is legal
                System.out.println("Column is full. Try another column.");
                col = sc.nextInt() - 1;
                row = placePiece(col, 1, board);
            }

            for (int i = 0; i < 4; i++) { //For loop to iterate through each direction
                if (checkWin(col, row, 1, i) + checkWin(col, row, 1, i + 4) == 3) { //Condition is met if the number of consecutive pieces in the ith and opposite to ith direction is 3
                    System.out.println("Player 1 wins");
                    p1Win = true; //Setting win condition
                    break; //Break for loop
                }
            }
            countMoves++;
            if (p1Win) {
                break; //Break while loop
            }

            col = ai.makeMove(ai.scores, ai.move(3, true, board), board); //User selects which column to drop the piece

            row = placePiece(col, 2, board); //Calls placePiece method to determine which row the piece is placed
            for (int i = 0; i < 4; i++) { //For loop to iterate through each direction
                if (checkWin(col, row, 2, i) + checkWin(col, row, 2, i + 4) == 3) { //Condition is met if the number of consecutive pieces in the ith and opposite to ith direction is 3
                    System.out.println("AI wins");
                    p2Win = true; //Setting win condition
                    break; //Break for loop
                }
            }
            countMoves++;
            showBoard(board);

        } while (!p2Win && (countMoves < board.length * board[0].length)); //Ensures when max number of moves is reached, the game progresses to a tie

        if (!p2Win && !p1Win) {
            System.out.println("Draw Game"); //Condition for a draw game
        }

    }

    //Below are methods used in PVP_Console.java as well (Methods that are common to both will be in a parent class Gamemode.java)
    /**
     * Displays the board
     */
    public static void showBoard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j] + " ");

            }
            System.out.println("");
        }
        System.out.println("-------------"
                + "\n1 2 3 4 5 6 7");

    }

    /**
     * @param col: the column the user selects
     * @param value: the value of the piece (1 for player 1, 2 for player 2)
     * @return the row where the piece should be, -1 if the column is filled
     */
    public static int placePiece(int col, int value, int[][] board) {
        int row = 0;
        for (int i = board.length - 1; i >= 0; i--) { //For loop iterates through each y position
            if (board[i][col] == 0) { //Finds the lowest y position and returns that row
                board[i][col] = value;
                row = i;
                return row;
            }
        }
        return -1;
    }

    public static int checkWin(int x, int y, int value, int dcount) {

        x += directions[dcount][0]; //x and y are updated to be an adjacent piece
        y += directions[dcount][1];

        try {

            if (board[y][x] == value) { //If the piece adjacent has the same value
                return 1 + checkWin(x, y, value, dcount); //Add 1 and then check the next adjacent piece
            } else {
                return 0; //Return 0 if the adjacent piece does not have the same value
            }

        } catch (ArrayIndexOutOfBoundsException e) { //Catch the array out of bounds exception
            return 0; //If OutOfBounds, return 0 (since there is no piece adjacent)
        }
    }

}
