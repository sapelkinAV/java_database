package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Created by alexander on 19.11.15.
 */
public class LoginMenuFrame extends JFrame {




    Connection connection;
    Statement statement;

    JButton loginButton = new JButton("Sign In");
    JButton regButton = new JButton("Sign Up");

    JLabel warningLabel = new JLabel("Start your glorious database session here!");
    JTextField userField = new JTextField();
    JPasswordField passwordField = new JPasswordField();

    public LoginMenuFrame() {
        super("Login Frame");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 150);

        prepareLoginFrame();


    }
    private void prepareLoginFrame(){
        JPanel authorisationLoginFramePanel = new JPanel();
        JPanel buttonsFlowPanel = new JPanel();
        buttonsFlowPanel.setLayout(new FlowLayout());
        authorisationLoginFramePanel.setLayout(new BoxLayout(authorisationLoginFramePanel,BoxLayout.Y_AXIS));
        authorisationLoginFramePanel.add(userField);
        authorisationLoginFramePanel.add(passwordField);
        this.add(authorisationLoginFramePanel, BorderLayout.CENTER);
        this.add(warningLabel, BorderLayout.NORTH);

        loginButton.addActionListener(new ButtonClickListener(this));
        regButton.addActionListener(new ButtonClickListener(this));
        buttonsFlowPanel.add(loginButton);
        buttonsFlowPanel.add(regButton);
        this.add(buttonsFlowPanel, BorderLayout.SOUTH);

        this.setVisible(true);




    }
    private class ButtonClickListener implements ActionListener {
        JFrame myFrame;
        public ButtonClickListener(JFrame myFrame) {
            this.myFrame = myFrame;
        }
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if( command.equals( "Sign In" ))  {


                try {
                    Class.forName(Sqlwork.JDBC_DRIVER);
                    connection = DriverManager.getConnection(Sqlwork.DB_URL, userField.getText().toString(), String.valueOf(passwordField.getPassword()));
                    statement=connection.createStatement();
                    myFrame.setVisible(false);
                    (new ClientMenuFrame(connection)).setVisible(true);
                    myFrame.dispose();

                } catch (ClassNotFoundException e1) {
                    warningLabel.setText("Bad try");
                } catch (SQLException e1) {
                    warningLabel.setText("Bad try");
                }


            }
            else if( command.equals( "Sign Up" ) )  {
                myFrame.setVisible(false);
                (new RegistrationMenuFrame()).setVisible(true);
                myFrame.dispose();

            }
            else  {

            }
        }
    }


}
