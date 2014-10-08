package v2gCommunication.client;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.*;
import java.io.*;
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
            System.out.println("Connection to Server not possible");
        }
    }
    public void sendData(String fileName, String fileType, String path, Integer packageSize){
        
    }
}
