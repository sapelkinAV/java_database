package com.company;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by alexander on 19.11.15.
 */
public class ClientMenuFrame extends JFrame{
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/";
    static final String DATABASE_NAME = "musicshop";


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
                (new buyAlbumFrame("Buy Frame",login,password)).setVisible(true);
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
        JPanel buttonControlPanel;
        BaseClientFrameClass(String frameName,String login, String password) {
            super(frameName);
            this.login = login;
            this.password = password;
            confirmButton = new JButton("Confirm");
            backButton = new JButton("Back");
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setSize(800,600);



            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    closeBaseFrame();
                }
            });




            buttonControlPanel = new JPanel();
            buttonControlPanel.setLayout(new FlowLayout());
            buttonControlPanel.add(confirmButton);
            buttonControlPanel.add(backButton);
            this.setLayout(new BorderLayout());
            this.add(buttonControlPanel, BorderLayout.SOUTH);
        }

        void closeBaseFrame() {
            this.dispose();
        }
    }

    class buyAlbumFrame extends BaseClientFrameClass {
        JLabel searchLabel = new JLabel("Search for...");
        JTextField searchTextField = new JTextField();
        JTable searchResultTable;
        AlbumDataModel albumDataModel;


        buyAlbumFrame(String frameName,String login, String password) {
            super(frameName,login, password);
            prepareFrame();
        }

        void prepareFrame() {
            albumDataModel = new AlbumDataModel();
            searchResultTable = new JTable();
            JScrollPane tableScrollPane = new JScrollPane();
            tableScrollPane.setViewportView(searchResultTable);

            albumDataModel.addColumn("Album Title");
            albumDataModel.addColumn("Artist");
            albumDataModel.addColumn("Price");
            albumDataModel.addColumn("Select");

            searchResultTable.setModel(albumDataModel);

            for (int i = 0; i < 10; i++) {
                albumDataModel.addRow(new Object[0]);
                albumDataModel.setValueAt("Test Table row 1" + i, i, 0);
                albumDataModel.setValueAt("Test Table row 2" + i, i, 1);
                albumDataModel.setValueAt("Test Table row 3" + i, i, 2);
                albumDataModel.setValueAt(false, i, 3);


            }
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    Boolean checked = false;
                    for (int i = 0; i < 10; i++) {
                         checked = Boolean.valueOf(searchResultTable.getValueAt(i, 3).toString());
                        if (checked) {
                            JOptionPane.showMessageDialog(null, "something Selected");
                        }
                    }

                }
            });

            this.add(tableScrollPane,BorderLayout.CENTER);


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
