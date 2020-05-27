/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RandomAI extends Gamemode {

    MinimaxAI ai = new MinimaxAI(Options.currentEasyDifficulty, 1, 0);

    public RandomAI(JFrame frame) throws IOException {
        super(frame);
    }

    public RandomAI(JFrame frame, File save) throws IOException, FontFormatException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        super(frame, save);
    }

    @Override
    public void setUpButtons() {
        //adds all of the buttons to the grid
        for (int i = 0; i < ROWSIZE; i++) {
            //numbers the buttons so it is more obvious where to press
            buttons[i] = new JButton(new ImageIcon("./src/Assets/arrow.png"));
            buttons[i].setBackground(col);
            buttons[i].setOpaque(true);
            buttons[i].setBorderPainted(false);
            //since i use an anonymous functional lambada, i need to duplicate the local variable to use within it
            int finalI = i;
            buttons[i].addActionListener(
                    actionEvent -> {
                        //displays status message on who's turn it is
                        //if the turn is odd, it is player 2's turn
                        if (turn % 2 == 1) {
                            frame.setTitle("Player 2 Turn");
                        } //otherwise it is player 1 turn
                        else {
                            frame.setTitle("Player 1 Turn");
                        }
                        //looks for the lowest availiabe position in the column to simulate gravity
                        for (int c = COLSIZE - 1; c >= 0; c--) {
                            //if there is a nonfilled spot
                            if (occupied[finalI][c] == -1) {
                                //mark it as filled
                                occupied[finalI][c] = turn % 2;
                                //if player is player 1
                                if (turn % 2 == 1) {
                                    slots[finalI][c].setIcon(new ImageIcon("./src/Assets/" + Options.player1CurrentColor));
                                } //if player is player 2
                                else {
                                    slots[finalI][c].setIcon(new ImageIcon("./src/Assets/" + Options.player2CurrentColor));
                                }
                                //centers the icon so that nothing bad can happen to it
                                slots[finalI][c].setHorizontalAlignment(JLabel.CENTER);
                                slots[finalI][c].setVerticalAlignment(JLabel.CENTER);
                                //checks if there is any player that has just won off of this tile placement
                                //the checkwin will automatically terminate this object if there is a winner
                                try {
                                    checkWin(finalI, c, turn % 2);
                                } catch (IOException | FontFormatException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                } catch (InstantiationException e) {
                                    e.printStackTrace();
                                } catch (UnsupportedLookAndFeelException e) {
                                    e.printStackTrace();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                                //increases the turn
                                turn++;
                                //repains and revalidates the frame
                                frame.repaint();
                                frame.revalidate();
                                //stops looking for another tile
                                break;
                            }
                            //if no tile was empty, force the user to select another column
                            if (c == 0) {
                                JOptionPane.showMessageDialog(frame, "This column is full, try another");
                            }
                        }
                        try {
                            aiMove();
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        } catch (IOException | InterruptedException | FontFormatException e) {
                            e.printStackTrace();
                        }
                    }
            );
            //add the button to the grid
            grid.add(buttons[i]);
        }
    }
    int[][] newArray = new int[occupied[0].length][occupied.length];

    public void reverseFilled() {
        for (int i = 0; i < occupied.length; i++) {
            for (int j = 0; j < occupied[0].length; j++) {
                newArray[j][i] = occupied[i][j];
            }
        }
    }

    private void aiMove() throws IOException, InterruptedException, FontFormatException {
        reverseFilled();
        if (turn % 2 == 0) {
            boolean validChoice = false;

            /**
             * for (int i = 0; i < newArray.length; i++) {
             * System.out.println(Arrays.toString(newArray[i])); }
             * System.out.println("diff " + Options.currentDifficulty);
             *
             */
            int high = MinimaxAI.move(Options.currentEasyDifficulty, true, newArray);
            //System.out.println(high);
            int choose = MinimaxAI.makeMove(high, newArray);

            frame.repaint();
            frame.revalidate();
            buttons[choose].doClick();

        }
    }

    @Override
    public void setUpMenuBar() {
        //creates new menu bar
        JMenuBar gameMenu = new JMenuBar();
        //creates exit section of menu
        JMenu exit = new JMenu("Exit");
        //creates game section of menu
        JMenu game = new JMenu("Game");
        //creates item to allow user to go back to main menu
        JMenuItem toMainMenu = new JMenuItem("Back to Main Menu");
        //add interaction which creates a new main menuobject, effectively sending the user back to the main menu
        //note usage of lambada function instead of creation of a new action listener to create a more efficent way of functional programming
        toMainMenu.addActionListener(actionEvent -> {
            try {
                //returns to main menu
                MainMenu mm = new MainMenu(frame);
            } catch (IOException | FontFormatException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException | ClassNotFoundException e) {
                //since main menu has many many different exceptions that can occur, the try catch needs to exist
                e.printStackTrace();
            }
        });
        //creates the item to exit the program entirely
        JMenuItem exitApplication = new JMenuItem("Exit Application");
        exitApplication.addActionListener(actionEvent -> {
            //creates two options for the user to choose to confirm or not
            Object[] possiblities = {"Yes", "No"};
            //creates frame for the dialog box to appear
            JFrame dialogFrame = new JFrame();
            //creates a new dialog box with the options yes and no, returns the string that was chosen
            String optionChosen = (String) JOptionPane.showInputDialog(
                    dialogFrame,
                    "Do you really want to exit?",
                    "Confirmation",
                    JOptionPane.PLAIN_MESSAGE,
                    UIManager.getIcon("FileView.fileIcon"),
                    possiblities,
                    "Yes"
            );
            //if no was selected, just close the pane
            //if the optionChosen is null, that is taken as the user not wanting to close
            if (optionChosen == null || optionChosen.equals("Cancel")) {
                dialogFrame.setVisible(false);
            } //if yes was selected, exit
            else if (optionChosen.equals("Yes")) {
                try {
                    Statistics.saveStats();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
        JMenuItem saveGame = new JMenuItem("Save Game");
        saveGame.addActionListener(actionEvent -> {
            //needs to be of type object or else the JOptionPane will not take it as a parpameter
            Object[] possiblities = {"Yes", "No"};
            //creates a new dialog frame
            JFrame dialogFrame = new JFrame();
            //creates a new dialog box with the options yes and no, returns the string that was chosen
            String optionChosen = (String) JOptionPane.showInputDialog(
                    dialogFrame,
                    "This will erase all current save states, do you want to continue?",
                    "Confirmation",
                    JOptionPane.PLAIN_MESSAGE,
                    UIManager.getIcon("FileView.fileIcon"),
                    possiblities,
                    "No"
            );
            //if yes was selected, go into the save method
            if (optionChosen.equals("Yes")) {
                try {
                    saveGame();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } //if no was selected, just close the pane
            else {
                dialogFrame.setVisible(false);
            }
        });
        //menu option to save the load a previously saved game state
        JMenuItem loadGame = new JMenuItem("Load Game");
        //calls the constructor of the
        loadGame.addActionListener((ActionEvent actionEvent) -> {
            try {
                RandomAI gm = new RandomAI(frame, new File("./src/Assets/SAVE.txt"));
            } catch (IOException | FontFormatException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        exit.add(toMainMenu);
        exit.add(exitApplication);
        game.add(saveGame);
        game.add(loadGame);
        gameMenu.add(game);
        gameMenu.add(exit);
        frame.setJMenuBar(gameMenu);
    }
}
