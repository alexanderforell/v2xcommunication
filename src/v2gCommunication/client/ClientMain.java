package v2gCommunication.client;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.*;
import java.io.*;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author alexander
 */
public class ClientMain {
    public static void main(String[] args) {
        Socket socket;
        PrintWriter out;
        BufferedReader in;
        boolean read=true;
        try 
        {
            socket = new Socket("localhost",25002);
            ServerConnection conn = new ServerConnection(socket);
            conn.start();
            BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
            String fromUser;
            while (read) {
                fromUser = stdIn.readLine();
                String[] segs = fromUser.split( Pattern.quote( " " ) );

                switch (segs[0]) {
                    case "createUser":
                        ClientMethods.createUser(conn, segs[1],segs[2]);
                        break;
                    case "deleteUser":
                        ClientMethods.deleteUser(conn, segs[1]);
                        break;
                    
                    case "exit":
                        read = false;
                        break;
                    default:
                        
                }
            }
        } 
        catch (IOException ex) {
            Logger.getLogger(ClientMain.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Could not connect to Server");
        }
    }
    
}
