package com.company;

import javax.swing.*;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by alexander on 19.11.15.
 */
public class ClientMenuFrame extends JFrame{

    Connection connection;
    Statement statement;

    public ClientMenuFrame(Connection connection) {
        super("Client Frame");
        this.connection=connection;
        this.setSize(600, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private void prepareFrame() {

    }

}
