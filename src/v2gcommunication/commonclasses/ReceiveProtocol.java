/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexander
 */
public class ReceiveProtocol extends ReadMessage{
    int counter = 0;
    
    public ReceiveProtocol(InputStream in, Decode c) {
        super(in, c);
    }
    public ReceiveProtocol(InputStream in) {
        super(in);
    }

    @Override
    public void readMessage(StringBuffer message, StringBuffer messageType) {
        byte[] messageTypeLength = new byte[4];
        byte[] messageLength = new byte[4];
        ByteArrayOutputStream boutMessageType = new ByteArrayOutputStream();
        ByteArrayOutputStream boutMessage = new ByteArrayOutputStream();
        try {
            int i;
            for (i = 0;i<4;i++){

                    messageTypeLength[i]=(byte)in.read();

            }
            for (i = 0;i<4;i++){
                messageLength[i]=(byte)in.read();
            }
            int msgTypeLength = getInts(messageTypeLength);
            int msgLength = getInts(messageLength);
            for (i = 0;i<msgTypeLength;i++){
                boutMessageType.write(in.read());
            }
            for (i = 0;i<msgLength;i++){
                boutMessage.write(in.read());
            }

            if (c!=null){
                byte[] msg = c.decode(boutMessage.toByteArray());
                byte[] msgType = c.decode(boutMessageType.toByteArray());
                for (i = 0 ; i<msg.length; i++){
                    message.append((char)msg[i]);
                }
                for (i = 0 ; i<msgType.length; i++){
                    messageType.append((char)msgType[i]);
                }
            }
            else {
                byte[] msg = boutMessage.toByteArray();
                byte[] msgType = boutMessageType.toByteArray();
                for (i = 0 ; i<msg.length; i++){
                    message.append((char)msg[i]);
                }
                for (i = 0 ; i<msgType.length; i++){
                    messageType.append((char)msgType[i]);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ReceiveProtocol.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private int getInts(byte[] bytes){
        ByteBuffer b = ByteBuffer.wrap(bytes);
        int i = b.getInt();
        return i;
    }    
}
    

