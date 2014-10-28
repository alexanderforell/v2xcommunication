/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.server;
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
            while (true){
                String message ="";
                try {
                    message = CypheredReadWrite.readDES(socket.getInputStream(),"01234567");
                } catch (Exception ex) {
                    Logger.getLogger(VehicleConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(VehicleConnection.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Message: " + message);
                JsonReader jsonReader = Json.createReader( new StringReader(message));
                JsonObject jsonData = jsonReader.readObject();
                for (VehicleDataReceived li1:listener) {
                   li1.vehicleDataReceived(fin, jsonData);
               }
            }
        
    }
    public void addListener(VehicleDataReceived vehicleDataReceived){
        listener.add(vehicleDataReceived);
    }
    
}
