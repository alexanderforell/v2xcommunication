/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexander
 */
public class TransmitProtocol extends WriteMessage{

    public TransmitProtocol(OutputStream out) {
        super(out);
    }
    public TransmitProtocol(OutputStream out, Encode c) {
        super(out, c);
    }
    @Override
    public void writeMessage(byte[] message, byte[] messageType) {
        byte[] messageProcessed;
        byte[] messageTypeProcessed;
        if (c==null){
            messageProcessed = message;
            messageTypeProcessed = messageType;
        } else{
            messageProcessed = c.encode(message);
            messageTypeProcessed = c.encode(messageType);
        }
        
        byte[] messageLength = getBytes(messageProcessed.length);
        byte[] messageTypeLength = getBytes(messageTypeProcessed.length);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bos.write(messageTypeLength);
            bos.write(messageLength);
            bos.write(messageTypeProcessed);
            bos.write(messageProcessed);
            System.out.println(bos.toByteArray().length);
            out.write(bos.toByteArray());
        } catch (IOException ex) {
            Logger.getLogger(v2gcommunication.commonclasses.deprecated.TransmitProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private byte[] getBytes(int i){
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(i);
        return b.array();
    }
    
}
