package com.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// TODO: Preference selector after logging in
// TODO: JUST A CSV WILL WORK JUST FINE AS IT'S JUST ONE USER
public class Preferences extends JPanel implements ActionListener{
    // canvas for other GUI widgets
    JFrame frame;

    JButton button;
    JLabel usernameLabel;
    JTextField usernameTextField;
    JLabel passwordLabel;
    JPasswordField passwordField;

    GUICaller guiCaller;

    public Preferences(int width, int height, JFrame frame, GUICaller guiCaller) {
        this.frame = frame;
        this.guiCaller = guiCaller;

        System.out.println("SEQUENCE: Preferences");
        this.setPreferredSize(new Dimension(width, height));
        setLayout(null);


        usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(40,20, 80, 25);

        usernameTextField = new JTextField();
        usernameTextField.setBounds(130,20, 165, 25);

        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(40,50, 80, 25);

        passwordField = new JPasswordField();
        passwordField.setBounds(130,50, 165, 25);

        button = new JButton("Submit");
        button.addActionListener(this);
        button.setBounds(40,85, 80, 25);

        add(usernameLabel);
        add(usernameTextField);
        add(passwordLabel);
        add(passwordField);
        add(button);
    }

    @Override
    public void actionPerformed(ActionEvent e){
        String inputUsername = usernameTextField.getText();
        String inputPassword = passwordField.getText();

//        if (inputUsername.equals(username) && inputPassword.equals(password)){
//            System.out.print("User Authenticated...");
//            try {
//                guiCaller.setHomeScreenVisible(true);
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            }
//            frame.dispose();
//
//        } else { // if authentication credentials invalid...
//            JOptionPane.showMessageDialog(this, "Invalid Credentials, Try Again");
//            passwordField.setText(""); // Clears the password field...
//        }

    }
}