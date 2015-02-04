/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.encryption;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

/**
 * Interface {@Encode} used in all classes for decoding data.
 * 
 * Defines the method {@code encode} which must be impemented by other classes.
 * 
 * @author Alexander Forell
 */
public interface Encode {
    /**
     * Method takes a {@code ByteArrayOutputStream} encodes it and return 
     * another {@code ByteArrayOutputStream} length of Strings might not be 
     * identical.
     * 
     * 
     * @param message       ByteArrayOutputStream of bytes to be decoded
     * @return              ByteArrayOutputStream of bytes to be decoded
     */
    public ByteArrayOutputStream encode(ByteArrayOutputStream message);
}
