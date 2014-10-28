/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.vehicle;

import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
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
     List <DataReceived> listener;
     
    ServerConnection(EventEvaluation listener){
        try {
            socket = new Socket("localhost",25001);
        }
        catch (Exception e){
            System.out.println("Couldn't connect to server");
        }
            
    }
    
    @Override public void run(){
        while (true) {
            String message ="";
            try {
                message = CypheredReadWrite.readDES(socket.getInputStream(),"01234567");
            } catch (Exception ex) {
                Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
            JsonReader jsonReader = Json.createReader( new StringReader(message));
            JsonObject jsonData = jsonReader.readObject();
            for (DataReceived li1:listener) li1.dataReceived(jsonData);
        }
    }
    
    public void transmitData(Long[] code, Double[] value){
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy.MM.dd 'at' HH:mm:ss.SSS ");
        
        int i;
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder dataCodes = Json.createArrayBuilder();
        JsonArrayBuilder dataValues = Json.createArrayBuilder();
        for (i = 0; i<code.length; i++){
            dataCodes.add(Long.toHexString(code[i]));
            dataValues.add(formatter.format(currentTime));
        } 
        message.add("Function-Name", "transmitData");
        message.add("Data-Points",dataCodes);
        message.add("Data-Values",dataValues);
        transmit = message.build();
        System.out.println(transmit.toString());
        try {
            CypheredReadWrite.writeDES(transmit.toString(), socket.getOutputStream(), "01234567");
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void close(){
        try {
            this.interrupt();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}   
