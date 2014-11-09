/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.deprecated;


import v2gcommunication.commonclasses.deprecated.Decode;
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
    public ReceiveProtocol(Decode in) {
        super(in);
    }
    
    
    @Override
    public void readMessage(StringBuffer message, StringBuffer messageType) {
        counter++;
        if (counter >500){
            System.out.println("Hä?");
        }
        byte[] messageTypeLength = new byte[4];
        byte[] messageLength = new byte[4];
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
                messageType.append((char)in.read());
            }
            for (i = 0;i<msgLength;i++){
                message.append((char)in.read());
            }
            
            if (true){
                System.out.println("Conter:" + counter);
                System.out.println("Message Type:" + messageType.toString());
                System.out.println("Message:" + message.toString());
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
