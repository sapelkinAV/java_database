package com.company;//STEP 1. Import required packages

public class FirstExample {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/EMP";

    //  Database credentials
    String userName;
    String userPassword;
    public static void main(String[] args) {
        if(args.length<2){
            System.out.println("Bad things happened, please input user confidentials.");
            System.exit(1);
        }
        Sqlwork sqlw = new Sqlwork(args[0],args[1]);
        sqlw.deleteDatabase();
        sqlw.createDatabase();
    }//end main
}//end FirstExample