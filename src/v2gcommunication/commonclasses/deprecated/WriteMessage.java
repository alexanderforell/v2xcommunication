/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.deprecated;

import v2gcommunication.commonclasses.deprecated.Encode;


/**
 *
 * @author alexander
 */
public abstract class WriteMessage {
    Encode out;
    int maxMessageLength;
    public WriteMessage(Encode out){
        this.out = out;
        this.maxMessageLength = 2^20;
    }
    
    public WriteMessage(Encode out, int maxMessageLength){
        this.out = out;
        this.maxMessageLength = maxMessageLength;
    }
    
    abstract void writeMessage(byte[] message, byte[] messageType);
    abstract void bufferedWriteMessage(byte[] message, byte[] messageType);
    
}
