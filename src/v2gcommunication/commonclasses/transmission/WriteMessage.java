/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.transmission;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Interface to implement the transmiting side of the transfer protcol.
 * 
 * @author Alexander Forell
 */
public interface WriteMessage {
    /**
     * Method called to write a message to an output stream.
     * 
     * @param message       Message
     * @param messageType   Type of Message
     * @param out           OutputStream
     * @throws IOException  will be thrown if socket/inputstream is not available.
     */
    public void writeMessage(ByteArrayOutputStream message, ByteArrayOutputStream messageType, OutputStream out) throws IOException;
}
