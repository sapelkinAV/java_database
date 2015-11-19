package com.company;

import javax.swing.*;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by alexander on 19.11.15.
 */
public class RegistrationMenuFrame extends JFrame {
    Connection connection;
    Statement statement;

    public RegistrationMenuFrame(Connection connection, Statement statement) {
        super("Registration Frame");
        this.connection = connection;
        this.statement = statement;
        this.setSize(600, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}
