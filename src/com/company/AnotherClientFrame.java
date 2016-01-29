package com.company;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Created by alexander on 10.12.15.
 */
public class AnotherClientFrame {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/";
    static final String DATABASE_NAME = "musicshop";
    String login;
    String password;
    JFrame searchAlbumsFrame;
    JFrame clientStashFrame;
    JFrame clientMenuFrame;
    JLabel balanceLabel;
    JPanel balanceCheckPanel;
    AnotherClientFrame(String login, String password) {
        this.login = login;
        this.password = password;
        balanceLabel = new JLabel();

        prepareClientFrame();

    }
    void prepareSearchAlbumsFrame() {
        searchAlbumsFrame = new JFrame();
        final JTable dataTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane(dataTable);

        JPanel buttonPanel = new JPanel();
        JPanel searchTextfieldPanel = new JPanel();
        JPanel centralPanel = new JPanel();
        searchTextfieldPanel.setLayout(new FlowLayout());
        buttonPanel.setLayout(new FlowLayout());
        JLabel searchLabel = new JLabel("Search");

        final JTextField searchTextField = new JTextField();
        searchTextField.setPreferredSize(new Dimension(300, 30));

        JButton buyButton = new JButton("Buy");
        JButton searchButton = new JButton("Search");
        JButton backButton = new JButton("Back");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                searchAlbumsFrame.dispose();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try (Connection connection = DriverManager.getConnection(DB_URL + DATABASE_NAME+"?noAccessToProcedureBodies=true", "root", "admin"))
                {
                    AlbumDataModel albumDataModel = new AlbumDataModel();
                    albumDataModel.addColumn("Album Title");
                    albumDataModel.addColumn("Artist");
                    albumDataModel.addColumn("Price");
                    albumDataModel.addColumn("Select");


                    String albumName = searchTextField.getText().toString();
                    String searchTemplate = "{CALL search_albums(?)}";
                    CallableStatement searchStatement =  connection.prepareCall(searchTemplate);
                    searchStatement.setString(1, albumName);
                    ResultSet resultSet = searchStatement.executeQuery();

                    int i=0;
                    while (resultSet.next()) {
                        albumDataModel.addRow(new Object[0]);
                        albumDataModel.setValueAt(resultSet.getString(1), i, 0);
                        albumDataModel.setValueAt(resultSet.getString(2), i, 1);
                        albumDataModel.setValueAt(resultSet.getString(3), i, 2);
                        albumDataModel.setValueAt(false, i, 3);
                        i++;
                    }
                    dataTable.setModel(albumDataModel);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try(Connection connection = DriverManager.getConnection(DB_URL+DATABASE_NAME+"?noAccessToProcedureBodies=true","root","admin")) {
                    CallableStatement buyStatement;
                    String albumName;
                    String executeQuery = "{Call buy_album(?,?)}";
                    for (int i = 0; i < dataTable.getRowCount(); i++) {
                        if ((boolean) dataTable.getValueAt(i, 3)) {
                            albumName = (String) dataTable.getValueAt(i, 0);
                            buyStatement = connection.prepareCall(executeQuery);
                            buyStatement.setString(1, albumName);
                            buyStatement.setString(2, login);
                            buyStatement.executeUpdate();

                        }
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });





        searchTextfieldPanel.add(searchTextField);
        searchTextfieldPanel.add(searchLabel);

        centralPanel.setLayout(new BoxLayout(centralPanel,BoxLayout.Y_AXIS));
        centralPanel.add(searchTextfieldPanel);
        centralPanel.add(tableScrollPane);

        buttonPanel.add(backButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(buyButton);






        searchAlbumsFrame.setLayout(new BorderLayout());
        searchAlbumsFrame.setSize(800, 600);
        searchAlbumsFrame.add(buttonPanel, BorderLayout.SOUTH);
        searchAlbumsFrame.add(centralPanel, BorderLayout.CENTER);
        searchAlbumsFrame.add(balanceCheckPanel, BorderLayout.NORTH);
        searchAlbumsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        searchAlbumsFrame.setVisible(true);
        searchAlbumsFrame.pack();
    }
    void prepareClientStashFrame(){
        clientStashFrame = new JFrame();
        clientStashFrame.setLayout(new BorderLayout());
        clientStashFrame.setSize(800, 600);
        final JTable stashTable = new JTable();
        JScrollPane tablePane = new JScrollPane(stashTable);

        JLabel balanceTitleLabel =new JLabel("Balance");
        JPanel balacePanel = new JPanel();
        balacePanel.setLayout(new FlowLayout());
       balacePanel.add(balanceLabel);
        balacePanel.add(balanceTitleLabel);
        stashTable.setPreferredSize(new Dimension(300,400));
        AlbumDataModel albumDataModel = new AlbumDataModel();
        albumDataModel.addColumn("Track title");
        albumDataModel.addColumn("Album title");
        albumDataModel.addColumn("Artist");


        try (Connection connection = DriverManager.getConnection(DB_URL + DATABASE_NAME, login, password))
        {
            String stashQuery = "select tr.Name as TrackName, al.Name as AlbumName, ar.Name as ArtistName  from ClientStash as cs join Albums al on cs.OwnedAlbums=al.id join Artists ar on al.Maker = ar.id join Tracks tr on tr.Album =al.id join Clients cl on cl.id=cs.Owner where cl.login=" + "'"+login+"'";
            String balanceQuery = "select balance from Clients where login = " + "'"+login+"'";
            Statement executeStatement = connection.createStatement();
            ResultSet balanceSet = executeStatement.executeQuery(balanceQuery);
            if (balanceSet.next()) {
                balanceLabel.setText(balanceSet.getString(1));
            }
            ResultSet resultSet = executeStatement.executeQuery(stashQuery);
            int i = 0;
            while (resultSet.next()) {
                albumDataModel.addRow(new Object[0]);
                albumDataModel.setValueAt(resultSet.getString(1), i, 0);
                albumDataModel.setValueAt(resultSet.getString(2), i, 1);
                albumDataModel.setValueAt(resultSet.getString(3), i, 2);
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                clientStashFrame.dispose();
            }
        });
        stashTable.setModel(albumDataModel);


        clientStashFrame.add(balacePanel, BorderLayout.NORTH);
        clientStashFrame.add(tablePane, BorderLayout.CENTER);
        clientStashFrame.add(backButton, BorderLayout.SOUTH);
        clientStashFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        clientStashFrame.setVisible(true);


    }
    void prepareClientFrame() {
        clientMenuFrame = new JFrame();
        clientMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientMenuFrame.setLayout(new BorderLayout());
        clientMenuFrame.setSize(800, 600);
        balanceCheckPanel = new JPanel(new FlowLayout());

        try (Connection connection = DriverManager.getConnection(DB_URL+DATABASE_NAME,login,password)) {
            String balanceQuery = "select balance from Clients where login = " + "'"+login+"'";
            Statement executeStatement = connection.createStatement();
            ResultSet balanceSet = executeStatement.executeQuery(balanceQuery);
            if (balanceSet.next()) {
                balanceLabel.setText(balanceSet.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        JButton searchAlbumsButton = new JButton("Search Albums");
        JButton searchClipsButton = new JButton("Search Clips");
        JButton searchTracksButton = new JButton("Search Tracks");
        JButton stashButton = new JButton("Stash");
        JButton queriesButton = new JButton("Queries");
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                (new LoginMenuFrame()).setVisible(true);
                clientMenuFrame.dispose();
            }
        });
        queriesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new ClientQueriesFrame(login, password);
            }
        });
        stashButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                prepareClientStashFrame();
            }
        });


        searchAlbumsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                prepareSearchAlbumsFrame();

            }
        });


        JPanel buttonPanel = new JPanel();
        JPanel controlButtonPanel = new JPanel();

        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));
        buttonPanel.add(searchAlbumsButton);
        buttonPanel.add(stashButton);
        buttonPanel.add(queriesButton);

        JLabel balanceTitleLabel = new JLabel("Balance");
        balanceCheckPanel.add(balanceLabel);
        balanceCheckPanel.add(balanceTitleLabel);


        clientMenuFrame.add(buttonPanel, BorderLayout.EAST);
        clientMenuFrame.add(backButton, BorderLayout.SOUTH);
        clientMenuFrame.add(balanceCheckPanel, BorderLayout.NORTH);

        clientMenuFrame.setVisible(true);
    }
    class AlbumDataModel extends DefaultTableModel {
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            switch (columnIndex) {
                case 0:
                    return String.class;
                case 1:
                    return String.class;
                case 2:
                    return String.class;
                case 3:
                    return Boolean.class;
                default:
                    return String.class;
            }
        }

    }
}
