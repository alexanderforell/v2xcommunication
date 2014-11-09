/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

/**
 *
 * @author alexander
 */
abstract class Decode {
    Cipher c;
    abstract void initializeCipher();
    
    public byte[] decode(byte[] bytes){
        try {
            return c.doFinal(bytes);
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(Decode.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
