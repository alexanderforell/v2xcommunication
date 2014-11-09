/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.vehicle;

import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import v2gcommunication.vehicle.interfaces.DataReceived;
import javax.json.*;
import v2gcommunication.commonclasses.DESEncode;
import v2gcommunication.commonclasses.DESDecode;
import v2gcommunication.commonclasses.ReceiveProtocol;
import v2gcommunication.commonclasses.TransmitProtocol;




/**
 *
 * @author alexander
 */
public class ServerConnection extends Thread{
    private Socket socket;
    private TransmitProtocol out;
    private ReceiveProtocol in;
     List <DataReceived> listener;
     
    ServerConnection(EventEvaluation listener){
        try {
            socket = new Socket("localhost",25001);
            DESEncode cEnc = new DESEncode();
            cEnc.initializeCipher();
            out = new TransmitProtocol(socket.getOutputStream(), cEnc);
            DESDecode cDec = new DESDecode();
            cDec.initializeCipher();
            in = new ReceiveProtocol(socket.getInputStream(), cDec); 
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
            
            
            
    }
    
    @Override public void run(){
        while (true) {
            StringBuffer message = new StringBuffer();
            StringBuffer messageType = new StringBuffer();
            in.readMessage(message, messageType);
            if ("JSON".equals(messageType.toString())) {
                JsonReader jsonReader = Json.createReader( new StringReader(message.toString()));
                JsonObject jsonData = jsonReader.readObject();
                for (DataReceived li1:listener) li1.dataReceived(jsonData);
            }
            
        }
    }
    
    public void transmitMessage(byte[] message){
        out.writeMessage(message, "JSON".getBytes());
            
    }
    
}   
