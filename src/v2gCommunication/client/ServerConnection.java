package v2gCommunication.client;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.*;
/**
 *
 * @author alexander
 */
public class ServerConnection extends Thread{
    static Socket socket;
    PrintWriter out;
    ServerConnection(Socket socket){
        super("ServerConnection");
        this.socket = socket;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            while ((in.readLine()) != null) {
                System.out.println("Listening, but doint nothing.");
            }
        } catch (IOException ex) {
            System.out.println("Connection to Server not possible");
        }
    }
    public void transmitData(JsonObject data){
        
        
        out.println(data.toString());
    }
}
