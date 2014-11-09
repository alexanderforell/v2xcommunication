/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.deprecated;

import v2gcommunication.commonclasses.deprecated.Decode;

/**
 *
 * @author alexander
 */
public abstract class ReadMessage {
    Decode in;
    public ReadMessage(Decode in){
        this.in = in;
    }
    
    abstract void readMessage(StringBuffer message, StringBuffer messageType);
    
}
