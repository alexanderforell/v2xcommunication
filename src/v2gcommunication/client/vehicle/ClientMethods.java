/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.client.vehicle;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import v2gcommunication.server.UserConnection;
/**
 *
 * @author alexander
 */
public class ClientMethods extends Thread{
    Socket socket;
    ClientMethods(Socket socket){
        super("ClientMethods");
        this.socket = socket;
    }
    @Override public void run() {
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            while ((in.readLine()) != null) {
                System.out.println("Listening, but doint nothing.");
            }
        } catch (IOException ex) {
            Logger.getLogger(UserConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void sendData(String fileName, String fileType, String path, Integer packageSize){
        
    }
}
