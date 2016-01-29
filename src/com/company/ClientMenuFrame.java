package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Created by alexander on 19.11.15.
 */
public class ClientMenuFrame extends JFrame{
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/";
    static final String DATABASE_NAME = "musicshop";

    BuyAlbumFrame bFrame;

    String login;
    String password;


    public ClientMenuFrame(String login,String password) {
        super("Client Frame");
        this.login = login;
        this.password = password;
        this.setSize(600, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        prepareFrame();
    }
    private void prepareFrame() {
        JButton buyButton = new JButton("Buy albums");
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                (bFrame =new BuyAlbumFrame("Buy Frame",login,password)).setVisible(true);
            }
        });
        this.setLayout(new BorderLayout());
        this.add(buyButton, BorderLayout.EAST);
    }

    class BaseClientFrameClass extends JFrame {
        String login;
        String password;
        JButton confirmButton;
        JButton backButton;
        JButton searchButton;
        JButton queriesButton;
        JPanel buttonControlPanel;
        BaseClientFrameClass(String frameName, final String login, final String password) {
            super(frameName);
            this.login = login;
            this.password = password;
            confirmButton = new JButton("Confirm");
            backButton = new JButton("Back");
            searchButton = new JButton("Search!");
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setSize(800,600);



            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    closeBaseFrame();
                }
            });
            queriesButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    new ClientQueriesFrame(login, password);
                }
            });




            buttonControlPanel = new JPanel();
            buttonControlPanel.setLayout(new FlowLayout());
            buttonControlPanel.add(confirmButton);
            buttonControlPanel.add(backButton);
            buttonControlPanel.add(queriesButton);
            this.setLayout(new BorderLayout());
            this.add(buttonControlPanel, BorderLayout.SOUTH);
        }

        void closeBaseFrame() {
            this.dispose();
        }
    }

    class BuyAlbumFrame extends BaseClientFrameClass {

        JTable searchResultTable;
        AlbumDataModel albumDataModel;
        JButton searchShowButton = new JButton("Search");
        JPanel centralPanel;
        JTextField searchTextField;


        BuyAlbumFrame(String frameName, String login, String password) {
            super(frameName,login, password);
            prepareFrame();
        }

        void prepareFrame() {
            albumDataModel = new AlbumDataModel();
            searchResultTable = new JTable();
            JPanel searchTextFieldPanel = new JPanel(new FlowLayout());
            searchTextFieldPanel.setPreferredSize(new Dimension(300,10));
            JLabel searchLabel = new JLabel("Search for...");
            searchTextField = new JTextField();
            searchTextField.setPreferredSize(new Dimension(300,30));
            searchTextField.setSize(300, 30);
            searchTextFieldPanel.add(searchLabel);
            searchTextFieldPanel.add(searchTextField);

            JScrollPane tableScrollPane = new JScrollPane();
            tableScrollPane.setViewportView(searchResultTable);


            albumDataModel.addColumn("Album Title");
            albumDataModel.addColumn("Artist");
            albumDataModel.addColumn("Price");
            albumDataModel.addColumn("Select");


            searchResultTable.setModel(albumDataModel);
            centralPanel = new JPanel();





            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    searchAndFillTable();
                }
            });







            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {

                    try (Connection connection = DriverManager.getConnection(DB_URL + DATABASE_NAME, login, password)){


                        PreparedStatement searchStatement = connection.prepareStatement("");


                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    Boolean checked = false;
                    for (int i = 0; i < 10; i++) {
                         checked = Boolean.valueOf(searchResultTable.getValueAt(i, 3).toString());
                        if (checked) {
                            JOptionPane.showMessageDialog(null, "something Selected");
                        }
                    }

                }
            });
            JPanel buttonPanel = new JPanel(new FlowLayout());
            buttonPanel.add(backButton);
            buttonPanel.add(searchButton);
            buttonPanel.add(confirmButton);

            JPanel searchAcivityPanel = new JPanel(new GridLayout(0, 1, 10, 10));
            searchAcivityPanel.add(searchTextFieldPanel);
            searchAcivityPanel.add(tableScrollPane);
            this.add(searchAcivityPanel, BorderLayout.CENTER);
            this.add(buttonPanel, BorderLayout.SOUTH);
            this.pack();


        }
        void searchAndFillTable(){
            System.out.println("rows -" + albumDataModel.getRowCount());
            for (int i = 0; i < albumDataModel.getRowCount()-1; i++) {
                albumDataModel.removeRow(i);
                System.out.println("remove " + i);
            }

            String searchQuery = "SELECT * FROM Albums WHERE Name LIKE ?";
            String searchArtistQuery = "SELECT Name FROM Artists WHERE id = ?";
            try (Connection connection = DriverManager.getConnection(DB_URL + DATABASE_NAME, login, password)) {
                PreparedStatement searchStatement = connection.prepareStatement(searchQuery);
                PreparedStatement searchArtistStatement = connection.prepareStatement(searchArtistQuery);

                searchStatement.setString(1, "'%" + searchTextField.getText().toString() + "%'");
                ResultSet resultSet = searchStatement.executeQuery();

                int k = 0;
                ResultSet artistResultSet;
                while (resultSet.next()) {
                    searchArtistStatement.setString(1, resultSet.getString("Maker"));
                    artistResultSet = searchArtistStatement.executeQuery();
                    albumDataModel.addRow(new Object[0]);
                    albumDataModel.setValueAt(resultSet.getString("Name"), k, 0);
                    albumDataModel.setValueAt(artistResultSet.getString("Name"), k, 1);
                    albumDataModel.setValueAt(resultSet.getString("Price"), k, 2);
                    albumDataModel.setValueAt(false, k, 3);
                    k++;

                }


            } catch (SQLException e) {
                e.printStackTrace();
            }

        }



    }

    class AlbumDataModel extends DefaultTableModel{
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
