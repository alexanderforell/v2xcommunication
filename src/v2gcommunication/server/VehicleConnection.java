/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.server;
import java.io.IOException;
import java.io.StringReader;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import v2gcommunication.server.interfaces.VehicleDataReceived;
import javax.json.*;
import v2gcommunication.commonclasses.DESDecode;
import v2gcommunication.commonclasses.DESEncode;
import v2gcommunication.commonclasses.ReceiveProtocol;
import v2gcommunication.commonclasses.TransmitProtocol;
/**
 *
 * @author alexander
 */
public class VehicleConnection extends Thread {
    private Socket socket = null;
    private TransmitProtocol out;
    private ReceiveProtocol in;
    private final String fin;
    List <VehicleDataReceived> listener;
    
     
    public VehicleConnection(Socket socket, EventEvaluation listener) {
        super("VehicleConnection");
        this.listener = new ArrayList <VehicleDataReceived>();
        this.listener.add(listener);
        this.socket = socket;
        UUID samplefin = UUID.randomUUID();
        fin = samplefin.toString();
        try {
            DESEncode cEnc = new DESEncode();
            cEnc.initializeCipher();
            out = new TransmitProtocol(socket.getOutputStream(), cEnc);
            DESDecode cDec = new DESDecode();
            cDec.initializeCipher();
            in = new ReceiveProtocol(socket.getInputStream(),cDec); 
        } catch (IOException ex) {
            Logger.getLogger(VehicleConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    @Override public void run() {
        StringBuffer message = new StringBuffer();
        StringBuffer messageType = new StringBuffer();
        while (true) {
            in.readMessage(message, messageType);
            if ("JSON".equals(messageType.toString())) {
                JsonReader jsonReader = Json.createReader( new StringReader(message.toString()));
                JsonObject jsonData = jsonReader.readObject();
                System.out.println(jsonData.toString());
                for (VehicleDataReceived li1:listener) li1.vehicleDataReceived(fin, jsonData);
            }
            message.delete(0, message.length());
            messageType.delete(0, messageType.length());
            
        } 
        
    }
    public void addListener(VehicleDataReceived vehicleDataReceived){
        listener.add(vehicleDataReceived);
    }
    
    public void transmitMessage(byte[] message){
        out.writeMessage(message, "JSON".getBytes());
            
    }
    
}
