/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.transmission;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Interface to implement the receiving side of the transfer protcol.
 * 
 * @author Alexander Forell
 */
public interface ReadMessage {
    /**
     * Method called to read a message from an input stream. parameters are 
     * passed by reference and will be filled by method.
     * 
     * @param message       Message
     * @param messageType   Type of Message
     * @param in            InputStream
     * @throws IOException  will be thrown if socket/inputstream is not available.
     */
    void readMessage(ByteArrayOutputStream message, ByteArrayOutputStream messageType, InputStream in) throws IOException;
}
