package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by alexander on 28.10.15.
 */
public class Sqlwork {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/";
    static final String DATABASE_NAME = "musicshop";

     String USER = "root";
    String PASS = "admin";
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
                connection = DriverManager.getConnection(DB_URL + DATABASE_NAME, USER, PASS);
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
        statement.executeUpdate(sqlCreateQuery);
    }


    void createTables(){
        String id_template="id int not null primary key auto_increment";
        String nameTemplate ="Name varchar(100)";
        try {
            createTable("Artists",
                    id_template,
                    nameTemplate,
                    "genre varchar(40)",
                    "balance  float"
                    );
            createTable("Albums",
                    id_template,
                    nameTemplate,
                    "Maker int not null",
                    "Price float ",
                    "foreign key(Maker) references Artists(id)");
            createTable("Tracks",
                    id_template,
                    nameTemplate,
                    "Duration TIME",
                    "Album int not null",
                    "foreign key(Album) references Albums(id)"
                    );
            createTable("Clips",
                    id_template,
                    nameTemplate,
                    "Duration time",
                    "Album int not null",
                    "foreign key(Album) references Albums(id)");
            createTable("Clients",
                    id_template,
                    nameTemplate,
                    "balance float");
            createTable("ClientStash",
                    id_template,
                    "OwnedAlbums int not null",
                    "Owner int not null",
                    "foreign key(OwnedAlbums) references Albums(id)",
                    "foreign key(Owner) references Clients(id)");
            createTable("ShopAdministrator",
                    id_template,
                    nameTemplate,
                    "balance float");




        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    void deleteDatabase(){
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            statement=connection.createStatement();
            String sqlQuery="DROP DATABASE IF EXISTS "+DATABASE_NAME;
            statement.executeUpdate(sqlQuery);



        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    Sqlwork(String username,String password){
        this.USER=username;
        this.PASS=password;
    }
}
