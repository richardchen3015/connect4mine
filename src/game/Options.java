package game;

import javax.swing.*;
import java.awt.*;

public class Options {
    //the overall difficulty variable will always store either the currentDifficulty or currentModerateDifficulty variables once a game has been entered
    //the minimax difficulty is based on the overall difficulty variable
    //when the easy mode is pressed, the overall difficulty variable changes into the currentEasyDifficulty variable while if the hard mode is pressed it will change into the currentModerate difficulty
    public static int overallDifficulty;
    //current color of pieces - this will correspond to a file in the assets folder
    static String player1CurrentColor = "BLACK.png";
    static String player2CurrentColor = "RED.png";
    //default minimax depth for the easy mode and hard mode
    static int currentEasyDifficulty = 2;
    static int currentModerateDifficulty = 4;
    //default maximum move amount allowed
    public static int moveAmount = 42;
    JFrame options = new JFrame();
    public Options(){
        //label selectors
        JLabel colorLabel = new JLabel("Please select the color for player 1:");
        JLabel color2Label = new JLabel("Please select the color for player 2/AI");
        JLabel difficultyLabel = new JLabel("Pl" + "ease select the difficulty for the easy mode:");
        JLabel difficultyLabelModerate = new JLabel("Pl" + "ease select the difficulty for the moderate mode:");

        //options available in the combo box for colors and difficulty (depth of minmax ai)
        String[] colors = {"BLACK", "ORANGE", "GREEN", "YELLOW", "RED"};
        Integer [] difficulty = {1,2,3,4,5};

        /**
         * Reason a method was not used for the repetitive code below is because they are of different data types
         * and all have specific checks in them. (eg. one must add .png to the color name to access the file
         */

        //creation of new combobox for the user to select
        JComboBox<String> player1Color = new JComboBox<String>(colors);
        //selects the current value that the user has enabled for them
        for (int i = 0; i < colors.length; i++){
            if ((colors[i]+".png").equals(player1CurrentColor)){
                player1Color.setSelectedIndex(i);
            }
        }
        //creation of new combobox for user
        JComboBox<String> player2Color = new JComboBox<String>(colors);
        //selects current value
        for (int i = 0; i < colors.length; i++){
            if ((colors[i]+".png").equals(player2CurrentColor)){
                player2Color.setSelectedIndex(i);
            }
        }
        //creation of new combobox for user
        JComboBox<Integer> aiDifficulty = new JComboBox<Integer>(difficulty);
        //selects current value
        for (int i = 0; i < difficulty.length; i++){
            if (difficulty[i] == currentEasyDifficulty){
                aiDifficulty.setSelectedIndex(i);
            }
        }
        //creation of new combobox for user
        JComboBox<Integer> ai2Difficulty = new JComboBox<Integer>(difficulty);
        //selects current value
        for (int i = 0; i < difficulty.length; i++){
            if (difficulty[i] == currentModerateDifficulty){
                ai2Difficulty.setSelectedIndex(i);
            }
        }

        //creation of textField where they can enter the maximum move count
        JTextField moveEntry = new JTextField(String.valueOf(moveAmount), 5);

        //following 5 blocks of code are adding the label and combobox for a respective to topic to a panel
        JPanel color1Panel = new JPanel();
        colorLabel.setForeground(new Color(228,255,255));
        color1Panel.add(colorLabel);
        color1Panel.add(player1Color);
        color1Panel.setBackground(new Color(0,0,0,0));

        JPanel color2Panel = new JPanel();
        color2Panel.add(color2Label);
        color2Label.setForeground(new Color(228,255,255));
        color2Panel.add(player2Color);
        color2Panel.setBackground(new Color(0,0,0,0));


        JPanel difficultyPanel = new JPanel();
        difficultyLabel.setForeground(new Color(228,255,255));
        difficultyPanel.add(difficultyLabel);
        difficultyPanel.add(aiDifficulty);
        difficultyPanel.setBackground(new Color(0,0,0,0));


        JPanel difficulty2 = new JPanel();
        difficultyLabelModerate.setForeground(new Color(228,255,255));
        difficulty2.add(difficultyLabelModerate);
        difficulty2.add(ai2Difficulty);
        difficulty2.setBackground(new Color(0,0,0,0));


        JPanel maxNumberMoves = new JPanel();
        JLabel maxMovesLabel = new JLabel("Please select the max number of turns");
        maxMovesLabel.setForeground(new Color(228,255,255));
        maxNumberMoves.setBackground(new Color(0,0,0,0));
        maxNumberMoves.add(maxMovesLabel);
        maxNumberMoves.add(moveEntry);

        //creation of exit button which will also put into effect the changes made using the combobox
        JButton saveAndExit = new JButton("Save and Exit");
        //action listener using lambada function
        saveAndExit.addActionListener(
                actionEvent -> {
                    if (player1Color.getSelectedItem().equals(player2Color.getSelectedItem())){
                        JOptionPane.showMessageDialog(new JFrame(), "Player 1 and 2 cannot have same color");
                        return;
                    }
                    try {
                        if (Integer.parseInt(moveEntry.getText()) < 43){
                            moveAmount = Integer.parseInt(moveEntry.getText());
                        }
                        else{
                            JOptionPane.showMessageDialog(new JFrame(), "There is a maximum of 42 moves in connect 4");
                            return;
                        }
                    } catch (Exception e){
                        JOptionPane.showMessageDialog(new JFrame(), "Invalid amount of moves");
                        return;
                    }
                    //saves the current selected values into the static variables declared at the top of this page
                    player1CurrentColor = player1Color.getSelectedItem() + ".png";
                    player2CurrentColor = player2Color.getSelectedItem() + ".png";
                    currentEasyDifficulty = Integer.parseInt(String.valueOf(aiDifficulty.getSelectedItem())) + 1;
                    currentModerateDifficulty = Integer.parseInt(String.valueOf(ai2Difficulty.getSelectedItem())) + 1;
                    //makes it invisible so nothing can happen
                    options.setVisible(false);
                }
        );
        //sets layout and adds all of the different panels needed
        options.getContentPane().setBackground(new Color(22,31,59));
        options.setLayout(new GridLayout(6,1));
        options.add(color1Panel);
        options.add(color2Panel);
        options.add(difficultyPanel);
        options.add(difficulty2);
        options.add(maxNumberMoves);
        options.add(saveAndExit);
        options.setSize(350,550);
        ImageIcon icon = new ImageIcon("./src/Assets/connect4icon.png");
        options.setIconImage(icon.getImage());
        options.setLocationRelativeTo(null);
        options.setVisible(true);
    }
}
