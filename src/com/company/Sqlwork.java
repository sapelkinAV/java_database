package com.company;
import java.sql.*;
/**
 * Created by alexander on 28.10.15.
 */
public class Sqlwork {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/";
    static final String DATABASE_NAME = "musicshop";

    static final String USER = "root";
    static final String PASS = "Uknmfs8ew";
    Connection connection = null;
    Statement statement = null;
    void createDatabase()  {

        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting database");
            try {
                connection = DriverManager.getConnection(DB_URL, USER, PASS);
                statement=connection.createStatement();
                statement.executeUpdate("CREATE DATABASE IF NOT EXISTS "+DATABASE_NAME + ";");
                connection = DriverManager.getConnection(DB_URL+DATABASE_NAME,USER,PASS);
                statement=connection.createStatement();
                System.out.println("Database created succesfully");
                System.out.println("trying to create table");
                createTables();

            } catch (SQLException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (statement != null) {
                        statement.close();
                    }
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
   private void createTable(String name,String... fields) throws SQLException {
        String sqlCreateQuery = "CREATE TABLE IF NOT EXISTS " + name + "(";
        for (int i = 0; i < fields.length-1; i++) {
            sqlCreateQuery+=fields[i]+",";
        }
        sqlCreateQuery+=fields[fields.length-1] + ")";
       System.out.println(sqlCreateQuery);
        statement.executeUpdate(sqlCreateQuery);
    }
    void createTables()  {
        try {
            createTable("Musicians",
                    "id integer not null",
                    "balance   double");
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}
