package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Created by alexander on 19.11.15.
 */
public class ArtistMenuFrame extends JFrame{
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/";
    static final String DATABASE_NAME = "musicshop";

    String login;
    String password;

    public ArtistMenuFrame(String login, String password) {
        super("Artist Frame");
        this.login = login;
        this.password = password;
        this.setSize(600, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        prepareFrame();
    }



    private void prepareFrame() {
        this.setLayout(new BorderLayout());

        JButton addAlbumButton = new JButton("Add album");
        JButton addTrackButton = new JButton("Add track");
        JButton addClipButton = new JButton("Add Clip");
        JButton showStatisticsButton = new JButton("Show Statistics");

        addAlbumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                (new AddAlbumFrame(login, password)).setVisible(true);
            }
        });
        addTrackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                (new AddTrackFrame(login, password)).setVisible(true);
            }
        });
        addClipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                (new AddClipFrame(login, password)).setVisible(true);
            }
        });
        showStatisticsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                (new ShowStatisticsFrame(login, password)).setVisible(true);
            }
        });



        JPanel buttonRequestPanel = new JPanel();
        buttonRequestPanel.setLayout(new BoxLayout(buttonRequestPanel, BoxLayout.Y_AXIS));
        buttonRequestPanel.add(addAlbumButton);
        buttonRequestPanel.add(addTrackButton);
        buttonRequestPanel.add(addClipButton);
        buttonRequestPanel.add(showStatisticsButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                (new LoginMenuFrame()).setVisible(true);
                closeFrame();
            }
        });


        this.add(buttonRequestPanel, BorderLayout.EAST);
        this.add(backButton, BorderLayout.SOUTH);
    }

    class BaseRequestFrameClass extends JFrame{
        protected String login;
        protected String password;

        BaseRequestFrameClass(String login, String password) {
            this.login = login;
            this.password = password;
            this.setSize(600, 800);
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        }
    }

    class AddAlbumFrame extends BaseRequestFrameClass {
        AddAlbumFrame(String login, String password) {
            super(login, password);
            prepareFrame();
        }

        void prepareFrame() {
            JLabel nameLabel = new JLabel("Album name");
            final JTextField nameTextField = new JTextField();
            nameTextField.setSize(300,30);


            JLabel priceLabel = new JLabel("Price");
            final JTextField priceTextField = new JTextField();
            priceTextField.setSize(300,30);


            JPanel albumFillPanel = new JPanel();


            albumFillPanel.setLayout(new GridLayout(2,2,30,30));
            albumFillPanel.add(nameLabel);
            albumFillPanel.add(nameTextField);
            albumFillPanel.add(priceLabel);
            albumFillPanel.add(priceTextField);


            JButton confirmButton = new JButton("Add Album");

            class confirmListener implements ActionListener{
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    try {
                        int artistId=0;
                        Class.forName(JDBC_DRIVER);
                        Connection connection = DriverManager.getConnection(DB_URL + DATABASE_NAME, login, password);
                        Statement statement = connection.createStatement();
                        String idQuery = "SELECT id from Artists where login = " + "'"+login+"'";
                        ResultSet resultSet = statement.executeQuery(idQuery);
                        if (resultSet.next()) {
                            artistId = resultSet.getInt("id");
                            System.out.println(artistId);
                        }
                        String insertQuery = "INSERT INTO Albums (Name, Price,Maker ) VALUES (" + "'" + nameTextField.getText().toString() + "', '" + priceTextField.getText().toString() + "', '" + artistId + "')";
                        System.out.println(insertQuery);
                        statement.executeUpdate(insertQuery);

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                }
            }

            confirmButton.addActionListener(new confirmListener());

            this.setLayout(new BorderLayout());
            this.add(confirmButton, BorderLayout.SOUTH);
            this.add(albumFillPanel, BorderLayout.CENTER);
        }


    }

    class AddTrackFrame extends BaseRequestFrameClass {
        AddTrackFrame(String login, String password) {
            super(login,password);
        }

        void prepareFrame() {
            JLabel nameLabel = new JLabel("Track name");
            final JTextField nameTextField = new JTextField();
            nameTextField.setSize(300,30);


            JLabel priceLabel = new JLabel("Duration(seconds)");
            final JTextField priceTextField = new JTextField();
            priceTextField.setSize(300,30);


            JPanel albumFillPanel = new JPanel();


            albumFillPanel.setLayout(new GridLayout(2,2,30,30));
            albumFillPanel.add(nameLabel);
            albumFillPanel.add(nameTextField);
            albumFillPanel.add(priceLabel);
            albumFillPanel.add(priceTextField);


            JButton confirmButton = new JButton("Add Album");

            class confirmListener implements ActionListener{
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    try {
                        int artistId=0;
                        Class.forName(JDBC_DRIVER);
                        Connection connection = DriverManager.getConnection(DB_URL + DATABASE_NAME, login, password);
                        Statement statement = connection.createStatement();
                        String idQuery = "SELECT id from Artists where login = " + "'"+login+"'";
                        ResultSet resultSet = statement.executeQuery(idQuery);
                        if (resultSet.next()) {
                            artistId = resultSet.getInt("id");
                            System.out.println(artistId);
                        }
                        String insertQuery = "INSERT INTO Albums (Name, Price,Maker ) VALUES (" + "'" + nameTextField.getText().toString() + "', '" + priceTextField.getText().toString() + "', '" + artistId + "')";
                        System.out.println(insertQuery);
                        statement.executeUpdate(insertQuery);

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                }
            }

            confirmButton.addActionListener(new confirmListener());

            this.setLayout(new BorderLayout());
            this.add(confirmButton, BorderLayout.SOUTH);
            this.add(albumFillPanel, BorderLayout.CENTER);
        }

    }

    class AddClipFrame extends BaseRequestFrameClass {
        AddClipFrame(String login, String password){
            super(login,password);
        }
    }

    class ShowStatisticsFrame extends BaseRequestFrameClass {
        ShowStatisticsFrame(String login, String password) {
            super(login,password);
        }
    }

    private void closeFrame() {
        this.setVisible(false);

        this.dispose();

    }

}
