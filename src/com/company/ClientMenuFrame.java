package com.company;

import javax.swing.*;
import java.sql.Connection;
import java.sql.Statement;

/**
 * Created by alexander on 19.11.15.
 */
public class ClientMenuFrame extends JFrame{


    String login;
    String password;

    public ClientMenuFrame(String login,String password) {
        super("Client Frame");
        this.login = login;
        this.password = password;
        this.setSize(600, 800);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    private void prepareFrame() {

    }

}
