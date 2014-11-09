/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses;

import java.io.OutputStream;

/**
 *
 * @author alexander
 */
abstract class WriteMessage {
    OutputStream out;
    Encode c;
    public WriteMessage(OutputStream out){
        this.out = out;
        this.c=null;
    }
    
    public WriteMessage(OutputStream out, Encode c){
        this.out = out;
        this.c = c;
    }
    
    abstract void writeMessage(byte[] message, byte[] messageType);
    
}
