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
/**
 *
 * @author alexander
 */
public class ClientVehicleMain {
    public static void main(String[] args) {
        Socket socket;
        PrintWriter out;
        BufferedReader in;
        try 
        {
            socket = new Socket("localhost",25001);
            out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader stdIn =
                new BufferedReader(new InputStreamReader(System.in));
            String fromUser;
            while (true) {
                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
            }
        } 
        catch (IOException ex) {
            Logger.getLogger(ClientVehicleMain.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Could not connect to Server");
        }
    }
    
}
