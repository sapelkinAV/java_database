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

    public ClientMenuFrame(Connection connection, Statement statement) {
        super("Client Frame");
        this.connection=connection;
        this.statement = statement;
        this.setSize(600, 800);
    }
    private void prepareFrame() {

    }

}
