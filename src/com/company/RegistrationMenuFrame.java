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
public class RegistrationMenuFrame extends JFrame {
    Connection connection;
    Statement statement;
    RegButton registrate = new RegButton("Confirm registration",this);
    BackButton backToLoginMenu = new BackButton("Back",this);
    JRadioButton clientRadioButton = new JRadioButton("Client");
    JRadioButton artistRadioButton = new JRadioButton("Artist");
    ButtonGroup rbGroup = new ButtonGroup();
    JTextField userField = new JTextField();
    JPasswordField passwordField = new JPasswordField();

    JLabel warningLabel = new JLabel("Take ticket into fairy database adventure now!");

    public void prepareFrame() {
        rbGroup.add(clientRadioButton);
        rbGroup.add(artistRadioButton);
        clientRadioButton.setSelected(true);
        JPanel buttonAndRadioButtonPanel = new JPanel();
        buttonAndRadioButtonPanel.setLayout(new GridLayout(2,2));
        buttonAndRadioButtonPanel.add(clientRadioButton);
        buttonAndRadioButtonPanel.add(artistRadioButton);
        buttonAndRadioButtonPanel.add(registrate);
        buttonAndRadioButtonPanel.add(backToLoginMenu);
        registrate.addActionListener(new ButtonClickListener());
        backToLoginMenu.addActionListener(new ButtonClickListener());
        this.setLayout(new BorderLayout());
        this.add(buttonAndRadioButtonPanel, BorderLayout.SOUTH);

        JPanel textfieldsPanel = new JPanel();
        textfieldsPanel.setLayout(new FlowLayout());
        textfieldsPanel.add(userField);
        textfieldsPanel.add(passwordField);
        this.add(textfieldsPanel, BorderLayout.CENTER);
        this.add(warningLabel, BorderLayout.NORTH);





    }

    public RegistrationMenuFrame() {
        super("Registration Frame");

        this.setSize(600, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            connection = DriverManager.getConnection(Sqlwork.DB_URL, DataBase.rootLogin, DataBase.rootPassword);
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("Back")) {
                backToLoginMenu.run();
            } else if (command.equals("Confirm registration")) {
                registrate.run();
            } else {

            }
        }
    }

    private class RegButton extends JButton implements Runnable {
        JFrame myFrame;

        public RegButton(String name, JFrame myFrame) {
            super(name);
            this.myFrame = myFrame;

        }

        @Override
        public void run() {

            String newUser="'"+userField.getText().toString()+"'@'localhost'";
            String regQuery="CREATE USER If not exists  "+ newUser+" Identified by "+
                    "'"+passwordField.getPassword()+"';";
            String permissionQuery;


            try {
                statement.executeUpdate(regQuery);
                if (clientRadioButton.isSelected()) {
                    permissionQuery = "Grant All privileges on musicshop.* to " + newUser + ";";

                } else {
                    permissionQuery="Grant All privileges on musicshop.* to " + newUser + ";";
                }

                statement.executeUpdate(permissionQuery);
                Connection regConnection = DriverManager.getConnection(Sqlwork.DB_URL + "musicshop", DataBase.rootLogin, DataBase.rootPassword);
                Statement regStatemenrt = regConnection.createStatement();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    private class BackButton extends JButton implements Runnable {
        JFrame myFrame;
        public BackButton(String Name,JFrame myFrame) {
            super(Name);
            this.myFrame = myFrame;
        }

        @Override
        public void run() {
            (new LoginMenuFrame()).setVisible(true);
            myFrame.dispose();

        }
    }
}
