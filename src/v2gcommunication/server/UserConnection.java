/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import v2gcommunication.server.interfaces.RequestReceived;

/**
 *
 * @author alexander
 */
public class UserConnection extends Thread {
    private Socket socket = null;
    List <RequestReceived> listener;
    String userName; 
    
    public UserConnection(Socket socket, EventEvaluation listener) {
        super("UserConnection");
        this.socket = socket;
        this.listener = new ArrayList <>();
        this.listener.add(listener);
        UUID sampleUserName = UUID.randomUUID();
        userName = sampleUserName.toString();
    }
    @Override public void run() {
        try{
            JsonReader in = Json.createReader(
                    new InputStreamReader(socket.getInputStream()));
            JsonObject data;
             while ((data = in.readObject()) != null) {
                for (RequestReceived li1:listener) li1.requestReceived(userName,data);
             }
        } catch (IOException ex) {
            Logger.getLogger(UserConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
