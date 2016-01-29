package com.company;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

/**
 * Created by alexander on 27.12.15.
 */
public class ClientQueriesFrame {
    String login;
    String password;
    JFrame queryFrame;
    JTable resultTab;
    QueryTableModel tableModel ;
    public JPanel buttonPanel;
    public JButton searchFavoriteGenreButton;
    public JButton sexQuery;
    public JButton buttonQuery3;
    public JButton resetButton;
    public JButton executeTextboxQuery;
    JTextField queryTextFild;
    JTextField searchTextField;
    JLabel searchLabel = new JLabel("Search ");
    JLabel queryLabel = new JLabel("Your Query");

    ClientQueriesFrame(final String login, final String password) {
        this.login=login;
        this.password = password;
        queryTextFild = new JTextField();
        tableModel = new QueryTableModel(login, password);
        resultTab = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(resultTab);


        searchTextField = new JTextField();
        queryTextFild = new JTextField();
        JPanel textfieldPanel = new JPanel();
        textfieldPanel.setLayout(new GridLayout(2, 2));
        textfieldPanel.add(searchLabel);
        textfieldPanel.add(searchTextField);
        textfieldPanel.add(queryLabel);
        textfieldPanel.add(queryTextFild);

         buttonPanel = new JPanel();
        BoxLayout buttonLayout = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
        buttonPanel.setLayout(buttonLayout);

        searchFavoriteGenreButton = new JButton("Search Favorite Genre");
        sexQuery = new JButton("Sex query");
        buttonQuery3 = new JButton("Third Query");
        resetButton = new JButton("Fourth Query");
        executeTextboxQuery = new JButton("Execute your query");
        buttonPanel.add(searchFavoriteGenreButton);
        buttonPanel.add(sexQuery);
        buttonPanel.add(buttonQuery3);
       // buttonPanel.add(resetButton);
        buttonPanel.add(executeTextboxQuery);

        searchFavoriteGenreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/musicshop","root","admin")){
                    CallableStatement callableStatement=connection.prepareCall("{call searchfavoritegenre(?)}");
                    callableStatement.setString(1, login);
                    tableModel.setPrepareCall(callableStatement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
        sexQuery.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/musicshop","root","admin")){
                    CallableStatement callableStatement=connection.prepareCall("{call sexStatisticsAlbum(?)}");
                    callableStatement.setString(1, login);
                    tableModel.setPrepareCall(callableStatement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        buttonQuery3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/musicshop","root","admin")){
                    CallableStatement callableStatement=connection.prepareCall("{call findByCreationTemproraryTable(?)}");
                    callableStatement.setString(1, login);
                    tableModel.setPrepareCall(callableStatement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        buttonQuery3.setText("Clever find");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                tableModel = new QueryTableModel(login,password );
                tableModel.fireTableChanged(null);
            }
        });
        executeTextboxQuery.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                tableModel.setQuery(queryTextFild.getText());
            }
        });
        queryFrame = new JFrame("Client queries");
        queryFrame.setLayout(new BorderLayout());
        queryFrame.add(tableScrollPane, BorderLayout.CENTER);
        queryFrame.add(buttonPanel, BorderLayout.EAST);
        queryFrame.add(textfieldPanel, BorderLayout.NORTH);
        queryFrame.pack();
        queryFrame.setVisible(true);
        queryFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    public void setSearchFavoriteGenreButton(JButton searchFavoriteGenreButton) {
        this.searchFavoriteGenreButton = searchFavoriteGenreButton;

    }

    public void setSexQuery(JButton sexQuery) {
        this.sexQuery = sexQuery;
    }

    public void setButtonQuery3(JButton buttonQuery3) {
        this.buttonQuery3 = buttonQuery3;
    }

    public void setResetButton(JButton resetButton) {
        this.resetButton = resetButton;
    }

    public void setButtonPanel(JPanel buttonPanel) {
        this.buttonPanel = new JPanel();
        this.buttonPanel = buttonPanel;
    }
    void refreshLayout(){}
}
class QueryTableModel extends AbstractTableModel {
    Vector cache; // will hold String[] objects . . .
    String login;
    String password;

    int colCount;

    String[] headers;



    Statement statement;


    public QueryTableModel(String login,String password) {
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





