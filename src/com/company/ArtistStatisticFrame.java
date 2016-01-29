package com.company;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

/**
 * Created by alexander on 28.12.15.
 */
public class ArtistStatisticFrame {
    JFrame statFrame ;
    JPanel btnPanel;
    JButton button1 ;
    JButton sexClientStatisticButton;
    JButton customQueryButton;
    JTable resultTable;
    JScrollPane scrollPane;
    ArtistQueryTableModel queryTableModel;
    JTextField searchTextField;
    JTextField queryTextField;

    ArtistStatisticFrame(final String login, String password) {
        statFrame = new JFrame("Statistic");
        statFrame.setLayout(new BorderLayout());
        queryTableModel = new ArtistQueryTableModel(login, password);

        resultTable = new JTable(queryTableModel);
        scrollPane = new JScrollPane(resultTable);
        btnPanel = new JPanel();
        BoxLayout btnLayout = new BoxLayout(btnPanel, BoxLayout.Y_AXIS);
        btnPanel.setLayout(btnLayout);

        button1 = new JButton("Simple Stat");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/musicshop","root","admin")) {
                    CallableStatement callableStatement = connection.prepareCall("{call simpleStatistic(?)}");
                    callableStatement.setString(1, login);
                    queryTableModel.setPrepareCall(callableStatement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        sexClientStatisticButton = new JButton("Sex client Statistic");
        sexClientStatisticButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/musicshop","root","admin")) {
                    CallableStatement callableStatement = connection.prepareCall("{call statisticOverStatistic(?)}");
                    callableStatement.setString(1, login);
                    queryTableModel.setPrepareCall(callableStatement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        });
        customQueryButton = new JButton("Go to the glorious sql adventure");
        customQueryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost/musicshop", "root", "admin"))
                {
                    queryTableModel.setQuery(queryTextField.getText());

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        btnPanel.add(button1);
        btnPanel.add(sexClientStatisticButton);
        btnPanel.add(customQueryButton);
        JPanel textPanel = new JPanel();
        searchTextField = new JTextField();
        queryTextField = new JTextField();
        textPanel.setLayout(new GridLayout(2, 2));
        JLabel searchLabel = new JLabel("search");
        JLabel queryLabel = new JLabel("query");
        textPanel.add(searchLabel);
        textPanel.add(searchTextField);
        textPanel.add(queryLabel);
        textPanel.add(queryTextField);

        statFrame.add(btnPanel, BorderLayout.EAST);
        statFrame.add(scrollPane, BorderLayout.CENTER);
        statFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        statFrame.setVisible(true);
        statFrame.add(textPanel, BorderLayout.NORTH);
        statFrame.pack();
    }
}
class ArtistQueryTableModel extends AbstractTableModel {
    Vector cache; // will hold String[] objects . . .
    String login;
    String password;

    int colCount;

    String[] headers;



    Statement statement;


    public ArtistQueryTableModel(String login,String password) {
        cache = new Vector();
        this.login=login;
        this.password = password;
    }

    public String getColumnName(int i) {
        return headers[i];
    }

    public int getColumnCount() {
        return colCount;
    }

    public int getRowCount() {
        return cache.size();
    }

    public Object getValueAt(int row, int col) {
        return ((String[]) cache.elementAt(row))[col];
    }



    // All the real work happens here; in a real application,
    // we'd probably perform the query in a separate thread.
    public void setQuery(String q) {
        cache = new Vector();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/musicshop" ,"root","admin")){
            // Execute the query and store the result set and its metadata
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(q);
            ResultSetMetaData meta = rs.getMetaData();
            colCount = meta.getColumnCount();

            // Now we must rebuild the headers array with the new column names
            headers = new String[colCount];
            for (int h = 1; h <= colCount; h++) {
                headers[h - 1] = meta.getColumnName(h);
            }

            // and file the cache with the records from our query. This would
            // not be
            // practical if we were expecting a few million records in response
            // to our
            // query, but we aren't, so we can do this.
            while (rs.next()) {
                String[] record = new String[colCount];
                for (int i = 0; i < colCount; i++) {
                    record[i] = rs.getString(i + 1);
                }
                cache.addElement(record);
            }
            fireTableChanged(null); // notify everyone that we have a new table.
        } catch (Exception e) {
            cache = new Vector(); // blank it out and keep going.
            e.printStackTrace();
        }
    }
    public void setPrepareCall(String query){
        cache = new Vector();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/musicshop","root","admin")){
            // Execute the query and store the result set and its metadata
            CallableStatement callStatement = connection.prepareCall(query);
            ResultSet rs = callStatement.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            colCount = meta.getColumnCount();

            headers = new String[colCount];
            for (int h = 1; h <= colCount; h++) {
                headers[h - 1] = meta.getColumnName(h);
            }


            while (rs.next()) {
                String[] record = new String[colCount];
                for (int i = 0; i < colCount; i++) {
                    record[i] = rs.getString(i + 1);
                }
                cache.addElement(record);
            }
            fireTableChanged(null); // notify everyone that we have a new table.
        } catch (Exception e) {
            cache = new Vector(); // blank it out and keep going.
            e.printStackTrace();
        }
    }

    public void setPrepareCall(CallableStatement callableStatement) throws SQLException {
        ResultSet rs = callableStatement.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();
        cache = new Vector();

        colCount = meta.getColumnCount();
        System.out.println(colCount);
        headers = new String[colCount];
        for (int h = 1; h <= colCount; h++) {
            headers[h - 1] = meta.getColumnName(h);
        }


        while (rs.next()) {
            String[] record = new String[colCount];
            for (int i = 0; i < colCount; i++) {
                record[i] = rs.getString(i + 1);
            }
            cache.addElement(record);
        }
        fireTableChanged(null);

    }

}




