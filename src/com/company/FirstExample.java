package com.company;//STEP 1. Import required packages

import java.sql.*;

public class FirstExample {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/EMP";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "Uknmfs8ew";

    public static void main(String[] args) {
      Sqlwork sqlw=new Sqlwork();
        sqlw.createDatabase();
    }//end main
}//end FirstExample