package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Created by alexander on 19.11.15.
 */
public class LoginMenuFrame extends JFrame {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/";
    static final String DATABASE_NAME = "musicshop";


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
                    final String clientExistanceQuery = "select count(*) as logintest from Clients where login='"+userField.getText().toString()+"'";
                    final String artistExistanceQuery = "select count(*) as logintest from Artists where login='"+userField.getText().toString()+"'";

                    Class.forName(Sqlwork.JDBC_DRIVER);
                    String loginName = userField.getText().toString();
                    String password = String.valueOf(passwordField.getPassword());

                    connection = DriverManager.getConnection(DB_URL+DATABASE_NAME, loginName, password);
                    statement=connection.createStatement();
                    ResultSet queryResult = statement.executeQuery(clientExistanceQuery);
                    if(queryResult.next()) {
                        if (queryResult.getInt("logintest") > 0) {

                            (new ClientMenuFrame(loginName, password)).setVisible(true);
                        } else {
                            queryResult = statement.executeQuery(artistExistanceQuery);
                            if(queryResult.next()) {
                                if (queryResult.getInt("logintest") > 0) {
                                    (new ArtistMenuFrame(loginName, password)).setVisible(true);
                                }
                            }
                        }
                        closeFrame();
                    }




                } catch (ClassNotFoundException e1) {
                    warningLabel.setText("Bad try");
                } catch (SQLException e1) {
                    warningLabel.setText("Bad try");
                    e1.printStackTrace();
                }
                finally {
                    try {
                        connection.close();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }


            }
            else if( command.equals( "Sign Up" ) )  {
                myFrame.setVisible(false);
                (new RegistrationMenuFrame()).setVisible(true);
                closeFrame();
            }

        }
    }

    private void closeFrame() {
        this.setVisible(false);

        this.dispose();

    }

}
