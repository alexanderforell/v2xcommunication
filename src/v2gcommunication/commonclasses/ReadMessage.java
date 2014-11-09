/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses;

import java.io.InputStream;

/**
 *
 * @author alexander
 */
abstract class ReadMessage {
    InputStream in;
    Decode c;
    public ReadMessage(InputStream in){
        this.in = in;
        this.c = null;
    }
    public ReadMessage(InputStream in, Decode c){
        this.in = in;
        this.c = c;
    }
    
    abstract void readMessage(StringBuffer message, StringBuffer messageType);
}
