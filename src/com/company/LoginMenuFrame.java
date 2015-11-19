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

    LoginButton loginButton = new LoginButton(this);
    RegisterButton regButton = new RegisterButton(this);

    JLabel warningLabel = new JLabel("Start your glorious database session here!");
    JTextField userField = new JTextField();
    JPasswordField passwordField = new JPasswordField();

    public LoginMenuFrame(Connection connection,Statement statement) {
        super("Login Frame");
        this.connection = connection;
        this.statement = statement;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400,150);

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

        loginButton.addActionListener(new ButtonClickListener());
        regButton.addActionListener(new ButtonClickListener());
        buttonsFlowPanel.add(loginButton);
        buttonsFlowPanel.add(regButton);
        this.add(buttonsFlowPanel, BorderLayout.SOUTH);

        this.setVisible(true);




    }
    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if( command.equals( "Sign In" ))  {
                loginButton.run();
            }
            else if( command.equals( "Sign Up" ) )  {
                regButton.run();
            }
            else  {

            }
        }
    }
    class LoginButton extends JButton implements Runnable{
        JFrame myJframe;
        public LoginButton(JFrame myJframe) {
            super("Sign In");
            this.myJframe=myJframe;
        }
        @Override
        public void run() {
            try {
                Class.forName(Sqlwork.JDBC_DRIVER);
                connection = DriverManager.getConnection(Sqlwork.DB_URL, userField.toString(), passwordField.toString());
                statement=connection.createStatement();


            } catch (ClassNotFoundException e) {
                warningLabel.setText("bad connection");
            } catch (SQLException e) {
                warningLabel.setText("bad connection");
            }

            myJframe.setVisible(false);
        }
    }
    class RegisterButton extends JButton implements Runnable{

        JFrame myFrame;

        public RegisterButton(JFrame myFrame) {
            super("Sign Up");
            this.myFrame = myFrame;
        }

        @Override
        public void run() {
                myFrame.setVisible(false);
                (new RegistrationMenuFrame(connection, statement)).setVisible(true);
            myFrame.dispose();



        }
    }
}
