package v2gCommunication.client;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.*;
import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import v2gCommunication.client.interfaces.DataReceived;
import v2gcommunication.commonclasses.DESDecode;
import v2gcommunication.commonclasses.DESEncode;
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
    
    public ServerConnection(int port, DataReceived li){
        try {
            socket = new Socket("localhost",port);
            DESEncode cEnc = new DESEncode();
            cEnc.initializeCipher();
            out = new TransmitProtocol(socket.getOutputStream(), cEnc);
            DESDecode cDec = new DESDecode();
            cDec.initializeCipher();
            in = new ReceiveProtocol(socket.getInputStream(), cDec); 
            listener.add(li);
        } catch (IOException ex) {
            Logger.getLogger(ServerConnection.class.getName()).log(Level.SEVERE, null, ex);
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
                for (DataReceived li1:listener) li1.dataReceived(jsonData);
            }
            message.delete(0, message.length());
            messageType.delete(0, messageType.length());   
        } 
    }
    public void transmitData(byte[] message){
        out.writeMessage(message, "JSON".getBytes());
      
    }
}
