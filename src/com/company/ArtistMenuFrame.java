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
            prepareFrame();
        }


        void prepareFrame() {

            final JLabel warningLabel = new JLabel("Add Track, buddy");
            class WarningLabelListener implements ActionListener{
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    warningLabel.setText("Add Track");
                }
            }

            JLabel nameLabel = new JLabel("Track name");
             final JTextField nameTextField = new JTextField();
            nameTextField.addActionListener(new WarningLabelListener());
            nameTextField.setSize(300,30);


            JLabel durationLabel = new JLabel("Duration(seconds)");
            final JTextField durationTextField = new JTextField();
            durationTextField.addActionListener(new WarningLabelListener());
            durationTextField.setSize(300,30);

            JLabel albumLabel = new JLabel("Album");
            final JTextField albumTextField = new JTextField();
            albumTextField.addActionListener(new WarningLabelListener());
            albumTextField.setSize(300, 30);




            JPanel albumFillPanel = new JPanel();


            albumFillPanel.setLayout(new GridLayout(0,2,30,30));
            albumFillPanel.add(nameLabel);
            albumFillPanel.add(nameTextField);
            albumFillPanel.add(albumLabel);
            albumFillPanel.add(albumTextField);
            albumFillPanel.add(durationLabel);
            albumFillPanel.add(durationTextField);


            JButton confirmButton = new JButton("Add Track");

            class confirmListener implements ActionListener{
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    final  String addTrackQuery ="insert into Tracks(Name,Duration,Album) values(?,?,?)";
                    final  String albumIdQuery = "select id from Albums where Maker =? and name = ?";
                    final  String artistIdQuery = "select id from Artists where login = ?";

                    try (Connection connection = DriverManager.getConnection(DB_URL + DATABASE_NAME, login, password)) {
                        Class.forName(JDBC_DRIVER);
                        PreparedStatement getArtistIdStatement = connection.prepareStatement(artistIdQuery);
                        PreparedStatement getAlbumIdStatement = connection.prepareStatement(albumIdQuery);
                        PreparedStatement addTrackStatement = connection.prepareStatement(addTrackQuery);

                        String trackName = nameTextField.getText().toString();
                        String albumName = albumTextField.getText().toString();
                        String duration = durationTextField.getText().toString();
                        String artistId = "";
                        String albumId = "";


                        getArtistIdStatement.setString(1,login);
                        ResultSet artistIdResult = getArtistIdStatement.executeQuery();
                        if (artistIdResult.next()) {
                            artistId = artistIdResult.getString("id");
                        }


                        getAlbumIdStatement.setString(1,artistId);
                        getAlbumIdStatement.setString(2,albumName);
                        ResultSet albumIdResult = getAlbumIdStatement.executeQuery();
                        if (albumIdResult.next()) {
                            albumId = albumIdResult.getString("id");

                        }

                        addTrackStatement.setString(1, trackName);
                        addTrackStatement.setString(2, duration);
                        addTrackStatement.setString(3, albumId);
                        System.out.println("Izmeneno -" + addTrackStatement.executeUpdate());



                    } catch (SQLException e) {
                        warningLabel.setText("Something goes wrong. Try again.");
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                }
            }



            confirmButton.addActionListener(new confirmListener());

            this.setLayout(new BorderLayout());
            this.add(warningLabel, BorderLayout.NORTH);
            this.add(confirmButton, BorderLayout.SOUTH);
            this.add(albumFillPanel, BorderLayout.CENTER);
        }

    }

    class AddClipFrame extends BaseRequestFrameClass {
        AddClipFrame(String login, String password){
            super(login,password);
            prepareFrame();
        }

        void prepareFrame() {
            JLabel clipNameLabel = new JLabel("Clip name");
            JLabel albumNameLabel = new JLabel("Album name");
            JLabel durationLabel = new JLabel("Duration");

            final JTextField clipNameField =new JTextField();
            final JTextField albumNameField = new JTextField();
            final JTextField durationTextField = new JTextField();
            clipNameField.setSize(300, 30);



            JPanel fillPanel = new JPanel();
            fillPanel.setLayout(new GridLayout(0,2,10,10));
            fillPanel.add(clipNameLabel);
            fillPanel.add(clipNameField);
            fillPanel.add(albumNameLabel);
            fillPanel.add(albumNameField);
            fillPanel.add(durationLabel);
            fillPanel.add(durationTextField);



            class ConfirmButtonListener implements ActionListener{
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    try (Connection connection = DriverManager.getConnection(DB_URL + DATABASE_NAME, login, password)) {
                        String artistIdQuery = "select id from Artists where login =?";
                        String albumIdQuery = "select id from Albums where Maker = ? and  Name =?";
                        String addClipQuery = "insert into Clips(Name, Duration, Album) values(?,?,?)";

                        PreparedStatement artistIdStatement = connection.prepareStatement(artistIdQuery);
                        PreparedStatement albumIdStatement = connection.prepareStatement(albumIdQuery);
                        PreparedStatement addClipStatement = connection.prepareStatement(addClipQuery);

                        String albumName = albumNameField.getText().toString();
                        String clipName = clipNameField.getText().toString();
                        String clipDuration = durationTextField.getText().toString();

                        String artistId = "";
                        String albumId = "";

                        artistIdStatement.setString(1, login);
                        ResultSet artistIdResultSet =artistIdStatement.executeQuery();
                        if (artistIdResultSet.next()) {
                            artistId = artistIdResultSet.getString("id");
                        }

                        albumIdStatement.setString(1, artistId);
                        albumIdStatement.setString(2,albumName);
                        ResultSet albumIdResultSet = albumIdStatement.executeQuery();
                        if (albumIdResultSet.next()) {
                            albumId = albumIdResultSet.getString("id");
                        }

                        addClipStatement.setString(1, clipName);
                        addClipStatement.setString(2, clipDuration);
                        addClipStatement.setString(3, albumId);
                        System.out.println("Izmeneno"+ addClipStatement.executeUpdate());

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            }
            final JButton confirmButton = new JButton("Add Clip");
            confirmButton.addActionListener(new ConfirmButtonListener());


            this.setLayout(new BorderLayout());
            this.add(fillPanel, BorderLayout.CENTER);
            this.add(confirmButton, BorderLayout.SOUTH);


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
