package com.topshopspackage;

import javax.swing.*;

//UI logic
import javax.swing.*;

public class SwingUI {
    //Create GUI a show it.
    public static void CreateAndShowGUI() {

        // Create and set up the window
        JFrame frame = new JFrame("TopShops");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add ubiquitous Hello World Label
        JLabel label = new JLabel("TopShops");
        frame.getContentPane().add(label);
        //Display Window

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CreateAndShowGUI();
            }
        });
    }
}