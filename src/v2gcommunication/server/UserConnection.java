/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.server;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexander
 */
public class UserConnection extends Thread {
    private Socket socket = null;
    
    public UserConnection(Socket socket) {
        super("UserConnection");
        this.socket = socket;
    }
    @Override public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            String fromClient;
             while ((fromClient = in.readLine()) != null) {
                System.out.println("Server: " + fromClient);
                if (fromClient.equals("Bye."))
                    break;
             }
        } catch (IOException ex) {
            Logger.getLogger(UserConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
