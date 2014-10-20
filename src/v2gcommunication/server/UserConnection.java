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
import v2gcommunication.server.interfaces.ClientRequestReceived;

/**
 *
 * @author alexander
 */
public class UserConnection extends Thread {
    private Socket socket = null;
    List <ClientRequestReceived> listener;
    String sessionID;
    String userName=null;
    
    public UserConnection(Socket socket, EventEvaluation listener) {
        super("UserConnection");
        this.socket = socket;
        this.listener = new ArrayList <>();
        this.listener.add(listener);
        UUID seesionID = UUID.randomUUID();
        this.sessionID = seesionID.toString();
    }
    
    @Override public void run() {
        try{
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            String strData;
             while ((strData = in.readLine()) != null) {
                 System.out.println(strData);
                JsonReader jsonReader = Json.createReader( new StringReader(strData));
                JsonObject jsonData = jsonReader.readObject();
                for (ClientRequestReceived li1:listener) li1.requestReceived(sessionID, jsonData);
             }
        } catch (IOException ex) {
            Logger.getLogger(UserConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
