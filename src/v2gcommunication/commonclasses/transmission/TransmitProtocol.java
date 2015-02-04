/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.transmission;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Implements {@code ReadMessage} and provides a basic transsion protocol.
 * 
 * The protocol consists to of the messagetype length, the message length,
 * the messageType and the message
 * 
 * @author Alexander Forell
 */
public class TransmitProtocol implements WriteMessage{

    /**
     * Method called to write a message to an output stream.
     * 
     * @param message       Message
     * @param messageType   Type of Message
     * @param out           OutputStream
     * @throws IOException  will be thrown if socket/inputstream is not available.
     */
    @Override
    public void writeMessage(ByteArrayOutputStream message, ByteArrayOutputStream messageType, OutputStream out) throws IOException{
        byte[] messageByte = message.toByteArray();
        byte[] messageTypeByte = messageType.toByteArray();
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bos.write(getBytes(messageTypeByte.length));
        bos.write(getBytes(messageByte.length));
        bos.write(messageTypeByte);
        bos.write(messageByte);
        out.write(bos.toByteArray());
            
    }
    
    /**
     * Converts integer into bytes.
     * 
     * @param bytes
     * @return int 
     */
    private byte[] getBytes(int i){
        ByteBuffer b = ByteBuffer.allocate(4);
        b.putInt(i);
        return b.array();
    }
    

}
