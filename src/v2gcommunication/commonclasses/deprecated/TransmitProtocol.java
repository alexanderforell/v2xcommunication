/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.deprecated;

import v2gcommunication.commonclasses.deprecated.Encode;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexander
 */
public class TransmitProtocol extends WriteMessage{

    public TransmitProtocol(Encode out) {
        super(out);
    }
    public TransmitProtocol(Encode out, int maxMessageLength) {
        super(out, maxMessageLength);
    }

    @Override
    public void writeMessage(byte[] message, byte[] messageType) {
        byte[] messageLength = getBytes(message.length);
        byte[] messageTypeLength = getBytes(messageType.length);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            bos.write(messageTypeLength);
            bos.write(messageLength);
            bos.write(messageType);
            bos.write(message);
            System.out.println(bos.toByteArray().length);
            out.write(bos.toByteArray());
        } catch (IOException ex) {
            Logger.getLogger(TransmitProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public void bufferedWriteMessage(byte[] message, byte[] messageType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private byte[] getBytes(int i){
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(i);
        return b.array();
    }
    
}
