/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.awt.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainMenu extends javax.swing.JFrame {

    //static jframe that will be passed around to all the gui classes
    static JFrame frame = new JFrame();
    //creation of panels to organize the main menu for the frame

    JPanel selection = new JPanel();
    static boolean statsLoaded = false;

    public void setUpMenu() throws IOException {
        //loads statistics from file if they have not been already, this will only take place in the first initialization of the main menu from the main method
        if (!statsLoaded) {
            Statistics.importStats();
            statsLoaded = !statsLoaded;
        }
        frame.getContentPane().removeAll();
        //create a new menu bar
        JMenuBar menuBar = new JMenuBar(){
//                public void paintComponent(Graphics g){
//                    g.drawImage(Toolkit.getDefaultToolkit().getImage("./src/Assets/menuBackground.png"),0,0,this);
//                }
        };
        //creation of various menuitems to put into the menu Bar
        JMenu settings = new JMenu("Settings");
        JMenuItem exit = new JMenuItem("Exit");
        JMenuItem instructions = new JMenuItem("Instructions");
        JMenuItem statistics = new JMenuItem("Statistics");
        JMenuItem options = new JMenuItem("Options");

        //makes the exit button exit when it is clicked
        exit.addActionListener(
                actionEvent -> {
                    try {
                        Statistics.saveStats();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
        );
        //tries to open notepad to open the instructions file if the user selects this menu option
        instructions.addActionListener(
                actionEvent -> {
                    //try-catch here used because we can't throw exceptions using a lambda function
                    try {
                        Desktop.getDesktop().open(new File("./src/Assets/Instructions.txt"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        //action listener to open the options jframe whenever the options button is pressed
        options.addActionListener(
                actionEvent -> {
                    Options s = new Options();
                }
        );
        //action listener to open the statistics jfram whenever the statistics button is pressed
        statistics.addActionListener(
                actionEvent -> {
                    try {
                        Statistics s = new Statistics();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        );
        //adds various components to the menu bar
        settings.add(instructions);
        settings.add(statistics);
        settings.add(options);
        settings.add(exit);
        menuBar.add(settings);
        frame.setJMenuBar(menuBar);
        frame.revalidate();
        frame.repaint();
    }

    public void setUpButtons() throws IOException {
        GridLayout buttonPanelLayout = new GridLayout(3, 1);
        buttonPanelLayout.setVgap(40);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(buttonPanelLayout);
        buttonPanel.setBackground(new Color(0,0,0, 0));
        //buttons to enter each specific mode
        JButton twoPlayer = new JButton("Two Player");
        //if the two player mode is pressed, we are taken to the two player game mode
        twoPlayer.addActionListener(
                actionEvent -> {
                    try {
                        TwoPlayer tp = new TwoPlayer(frame);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        JButton easyAI = new JButton("Singleplayer - Easy");
        easyAI.addActionListener(actionEvent -> {
            try {
                Options.overallDifficulty = Options.currentEasyDifficulty;
                RandomAI rai = new RandomAI(frame);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        JButton hardAI = new JButton("Singleplayer - Hard");
        hardAI.addActionListener(actionEvent -> {
            try {
                Options.overallDifficulty = Options.currentModerateDifficulty;
                RandomAI rai = new RandomAI(frame);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        //adds title label to the titlebar

        //adds mode selection buttons to the specified jpanel
        buttonPanel.add(twoPlayer);
        buttonPanel.add(easyAI);
        buttonPanel.add(hardAI);
        frame.add(buttonPanel);
    }

    public MainMenu(JFrame frame) throws IOException, FontFormatException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        setUpMenu();
        frame.setContentPane(new JLabel(new ImageIcon("./src/Assets/background.gif")));
        frame.setLayout(new FlowLayout());
        //title message
        JLabel titleLabel = new JLabel();
        titleLabel.setIcon(new ImageIcon("./src/Assets/title.png"));

        JLabel blank = new JLabel();
        blank.setIcon(new ImageIcon("./src/Assets/blank.png"));
        frame.add(titleLabel);
        setUpButtons();
        setUpVisuals();

        //sets size of the application
        frame.setSize(589, 599);
        frame.setSize(590, 600);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon("./src/Assets/connect4icon.png");
        frame.setIconImage(icon.getImage());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private void setUpVisuals() throws IOException, FontFormatException {
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        Font montserrat = Font.createFont(Font.TRUETYPE_FONT, new File("./src/Assets/Montserrat-BoldItalic.ttf")).deriveFont(40f);
//        ge.registerFont(montserrat);
//        UIManager.put("Label.font", montserrat);
//        UIManager.put("Button.font", "Arial");
    }

    public static void main(String[] args) throws IOException, FontFormatException, ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        try {
            UIManager.setLookAndFeel( new FlatDarculaLaf() );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }        //creates instance of the application
        MainMenu m = new MainMenu(frame);
    }
}
