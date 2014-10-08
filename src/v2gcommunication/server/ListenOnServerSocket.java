/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.server;
import v2gcommunication.server.interfaces.ConnectionList;
import java.net.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author alexander
 */
public class ListenOnServerSocket extends Thread {
    private final ConnectionList conn;
    private ServerSocket serverSocket;
    private boolean listening = true;
    private final EventEvaluation listener;
    ListenOnServerSocket(int portNumber,ConnectionList conn, EventEvaluation listener ){
        super("ListenOnServerSocket");
        this.conn=conn;
        this.listener=listener;
        try {
            serverSocket = new ServerSocket(portNumber);
        }
        catch (IOException e) {
            System.err.println("Could not listen on port " + portNumber);
            System.exit(-1);
        }
    }
    @Override public void run() {
        while (listening){
            try {
                conn.newConnection(serverSocket.accept(),listener);
            } catch (IOException ex) {
                Logger.getLogger(ListenOnServerSocket.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public void close(){
        try {
            listening = false;
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ListenOnServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
