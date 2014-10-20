/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.vehicle;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import v2gcommunication.vehicle.interfaces.DataReceived;
import javax.json.*;


/**
 *
 * @author alexander
 */
public class ServerConnection extends Thread{
    Socket socket;
    PrintWriter out;
     List <DataReceived> listener;
    ServerConnection(EventEvaluation listener){
        try {
            socket = new Socket("localhost",25001);
            out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch (Exception e){
            System.out.println("Couldn't connect to server");
        }
            
    }
    @Override public void run(){
        try {
            JsonReader in = Json.createReader(
                    new InputStreamReader(socket.getInputStream()));
            JsonObject data;
             while ((data = in.readObject()) != null) {
                for (DataReceived li1:listener) li1.dataReceived(data);
             }
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void transmitData(Long[] code, Double[] value){
        int i;
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder dataCodes = Json.createArrayBuilder();
        JsonArrayBuilder dataValues = Json.createArrayBuilder();
        for (i = 0; i<code.length; i++){
            dataCodes.add(Long.toHexString(code[i]));
            dataValues.add(Long.toHexString(Double.doubleToLongBits(value[i])));
        } 
        message.add("Function-Name", "transmitData");
        message.add("Data-Points",dataCodes);
        message.add("Data-Values",dataValues);
        transmit = message.build();
        
        out.println(transmit.toString());
    }
    
}   
