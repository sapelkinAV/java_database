package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by alexander on 19.11.15.
 */
public class RegistrationMenuFrame extends JFrame {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/";
    static final String DATABASE_NAME = "musicshop";
    static final String rootName = "root";
    static final String rootPassword="Uknmfs8ew";
    JPanel clientSpecificCommercials;
    JPanel artistSpecificCommercials;
    CardLayout cardLayout = new CardLayout();
    JPanel cardChosenSpecificCommercials = new JPanel(cardLayout);
    JPanel commercialsPanel;

    JRadioButton clientRadioButton ;
    JRadioButton artistRadioButton ;
    JRadioButton clientSexMaleRadioButton;
    JRadioButton clientSexFemaleRadioButton;


    JLabel loginLabel ;
    JLabel passwordLabel;
    JLabel nameLabel;
    JLabel genreLabel;


    JTextField loginTextField ;
    JTextField nameTextField ;
    JTextField artistGenreTextField;


    JPasswordField passwordField ;
    Connection connection;
    Statement statement;
    public RegistrationMenuFrame()  {
        super("Registration");

         clientRadioButton = new JRadioButton("Client");
         artistRadioButton = new JRadioButton("Artist");
         clientSexMaleRadioButton = new JRadioButton("Male");
         clientSexFemaleRadioButton = new JRadioButton("Female");
         loginLabel = new JLabel("Login");
         passwordLabel = new JLabel("Password");
         nameLabel = new JLabel("Name");
         genreLabel = new JLabel("Genre");
         loginTextField=new JTextField();
         nameTextField =new JTextField();
         artistGenreTextField=new JTextField();
         passwordField = new JPasswordField();
         commercialsPanel = new JPanel();
        clientRadioButton.setActionCommand("Client");
        artistRadioButton.setActionCommand("Artist");


        JPanel buttonPanel = new JPanel(new GridLayout(0,2,10,50));

        JButton registrate = new JButton("Confirm registration");
        JButton backToLoginMenu = new JButton("Back");
        registrate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getActionCommand().equals("Confirm registration")) {
                    try {
                        statement = connection.createStatement();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    String createUserTemplate = "Create user";
                    String identifiedByTemplate = "Identified by";
                    String login = loginTextField.getText().toString();
                    String password = String.valueOf(passwordField.getPassword());

                    String name = nameTextField.getText().toString();
                    if (clientRadioButton.isSelected()) {
                        String sex;
                        if (clientSexMaleRadioButton.isSelected()) {
                            sex = "M";
                        } else {
                            sex = "F";
                        }
                        try {
                            statement.executeUpdate(createUserTemplate+" '" + login+"'@'localhost' " + identifiedByTemplate +" '"+ password+"';");
                            String insertQuery = "INSERT INTO Clients(Name, Sex, Balance) VALUES('" + name + "', " + "'" + sex + "', " + "1000.0)";
                            System.out.println(insertQuery);
                            statement.executeUpdate(insertQuery);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }else if (artistRadioButton.isSelected()) {
                        String genre = artistGenreTextField.getText().toString();
                        try {

                            String sqlquery="INSERT INTO Artists(Name, Genre, Balance) VALUES('" + name + "', " + "'" + genre + "', " + "100.0)";
                            System.out.println(sqlquery);
                            statement.executeUpdate(sqlquery);
                            statement.executeUpdate(sqlquery);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                    }

                }
            }
        });
        backToLoginMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                (new LoginMenuFrame()).setVisible(true);
                closeFrame();


            }
        });
        buttonPanel.add(clientRadioButton);
        buttonPanel.add(artistRadioButton);
        clientRadioButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                    cardLayout.show(cardChosenSpecificCommercials, "1");
                }
            }
        });
        artistRadioButton.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
                    cardLayout.show(cardChosenSpecificCommercials, "2");
                }
            }
        });
        buttonPanel.add(registrate);
        buttonPanel.add(backToLoginMenu);



        try {
            connection = DriverManager.getConnection(DB_URL + DATABASE_NAME, rootName, rootPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        }






        clientRadioButton.setSelected(true);
        clientSexMaleRadioButton.setSelected(true);


        ButtonGroup rbGroup = new ButtonGroup();
        rbGroup.add(clientRadioButton);
        rbGroup.add(artistRadioButton);
        ButtonGroup sexRbGroup = new ButtonGroup();
        sexRbGroup.add(clientSexMaleRadioButton);
        sexRbGroup.add(clientSexFemaleRadioButton);
        prepareCommercialsPanels();
        cardChosenSpecificCommercials.add(artistSpecificCommercials, "2");
        cardChosenSpecificCommercials.add(clientSpecificCommercials, "1");
        cardLayout.show(cardChosenSpecificCommercials, "1");





        JPanel combinePanel = new JPanel();
        combinePanel.setLayout(new BoxLayout(combinePanel, BoxLayout.Y_AXIS));
        combinePanel.add(commercialsPanel);
        combinePanel.add(cardChosenSpecificCommercials);
        this.setLayout(new BorderLayout());
        this.add(buttonPanel, BorderLayout.SOUTH);
        this.add(combinePanel,BorderLayout.CENTER);
        this.pack();
        this.setSize(400, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }

    void prepareCommercialsPanels() {
        commercialsPanel = new JPanel(new GridLayout(0, 2));
        commercialsPanel.add(loginLabel);
        commercialsPanel.add(loginTextField);
        commercialsPanel.add(passwordLabel);
        commercialsPanel.add(passwordField);
        commercialsPanel.add(nameLabel);
        commercialsPanel.add(nameTextField);

        clientSpecificCommercials = new JPanel();
        clientSpecificCommercials.setLayout(new GridLayout(0, 2));
        clientSpecificCommercials.add(clientSexMaleRadioButton);
        clientSpecificCommercials.add(clientSexFemaleRadioButton);

        artistSpecificCommercials = new JPanel();
        artistSpecificCommercials.setLayout(new GridLayout(0, 2));
        artistSpecificCommercials.add(genreLabel);
        artistSpecificCommercials.add(artistGenreTextField);


    }


    private void closeFrame() {
        this.setVisible(false);
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.dispose();

    }


}
