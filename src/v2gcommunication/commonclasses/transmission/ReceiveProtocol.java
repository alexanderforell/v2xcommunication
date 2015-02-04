/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.transmission;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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
public class ReceiveProtocol implements ReadMessage{ 

    /**
     * Method called to read a message from an input stream. parameters are 
     * passed by reference and will be filled by method.
     * 
     * @param message       Message
     * @param messageType   Type of Message
     * @param in            InputStream
     * @throws IOException  will be thrown if socket/inputstream is not available.
     */
    @Override
    public void readMessage(ByteArrayOutputStream message, ByteArrayOutputStream messageType, InputStream in) throws IOException{
        byte[] messageTypeLength = new byte[4];
        byte[] messageLength = new byte[4];
        int i;
        for (i = 0;i<4;i++){

                messageTypeLength[i]=(byte)in.read();

        }
        for (i = 0;i<4;i++){
            messageLength[i]=(byte)in.read();
        }
        int msgTypeLength = getInts(messageTypeLength);
        if (msgTypeLength<0) {
            throw new IOException();
        }
        int msgLength = getInts(messageLength);
        for (i = 0;i<msgTypeLength;i++){
            messageType.write(in.read());
        }
        for (i = 0;i<msgLength;i++){
            message.write(in.read());
        }
    }
    
    /**
     * Converts bytes into integer.
     * 
     * @param bytes
     * @return int 
     */
    private int getInts(byte[] bytes){
        ByteBuffer b = ByteBuffer.wrap(bytes);
        int i = b.getInt();
        return i;
    }    
}
    

