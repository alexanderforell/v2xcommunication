package v2gCommunication.client;

import v2gCommunication.client.ui.LoginDialog;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author alexander
 */
public class ClientMain {
    public static void main(String[] args) {
        ClientMethods conn = new ClientMethods(25002);
        ClientMainAPI clientMain = new ClientMainAPI();
        clientMain.setVisible(true);
        
   
    }
    
}
