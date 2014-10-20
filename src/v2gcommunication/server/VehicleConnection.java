/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.server;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.StringReader;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import v2gcommunication.server.interfaces.VehicleDataReceived;
import javax.json.*;
/**
 *
 * @author alexander
 */
public class VehicleConnection extends Thread {
    private Socket socket = null;
    private final String fin;
    List <VehicleDataReceived> listener;
     
    public VehicleConnection(Socket socket, EventEvaluation listener) {
        super("VehicleConnection");
        this.listener = new ArrayList <>();
        this.listener.add(listener);
        this.socket = socket;
        UUID samplefin = UUID.randomUUID();
        fin = samplefin.toString();
        
    }
    @Override public void run() {
        try {
            BufferedReader in =
                new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            String strData;
            while ((strData = in.readLine())!=null){ 
                JsonReader jsonReader = Json.createReader( new StringReader(strData));
                JsonObject jsonData = jsonReader.readObject();
                for (VehicleDataReceived li1:listener) {
                   System.out.println(jsonData.toString());
                   li1.vehicleDataReceived(fin, jsonData);
               }
            }
        } catch (IOException ex) {
            Logger.getLogger(UserConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public void addListener(VehicleDataReceived dataReceived){
        listener.add(dataReceived);
    }
    
}
