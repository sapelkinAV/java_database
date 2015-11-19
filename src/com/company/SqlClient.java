package com.company;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Created by alexander on 19.11.15.
 */
public class SqlClient {
    Connection globalConnection;
    Statement globalStatement;
    JFrame loginMenuFrame;
    JFrame clientMenuFrame;
    JFrame registrationFrame;
    SqlClient() {
        new LoginMenuFrame(globalConnection, globalStatement);

    }

}
