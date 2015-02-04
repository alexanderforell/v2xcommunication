/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.encryption;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * class {@code DESEncode} extends class {@code Eecode}
 * 
 * Implements the method {@code encode} inhertied from interface {@Encode}
 * 
 * The method used for encryption is DES
 * 
 * @author Alexander Forell
 */
public class DESEncode implements Encode {
    private Cipher c;

    public DESEncode() {
        /**
         * Secret key for DES decryption must be 8 bytes long
         */
        String key="01234567";
        /**
         * Provide a secure key, in case defined key is weak.
         */
        Key k = new SecretKeySpec( key.getBytes(), "DES" );
        try {
            /**
            * Create a Cipher instance for DES with padding
            */
            c = Cipher.getInstance( "DES/CBC/PKCS5Padding" );
            /**
            * create a startvector for the padding
            */
            IvParameterSpec iv2 = new IvParameterSpec(new byte[8]);
            /**
            * Initialize Cipher in encrypt mode
            */
            c.init(Cipher.ENCRYPT_MODE, k,iv2);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
            Logger.getLogger(DESEncode.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    /**
     * Method takes a {@code ByteArrayOutputStream} encodes it and return 
     * another {@code ByteArrayOutputStream} length of Strings might not be 
     * identical.
     * 
     * Encyrption method is DES, secret key is 01234567, should be the same 
     * on decrypting and encrypting side.
     * 
     * @param message       ByteArrayOutputStream of bytes to be decoded
     * @return              ByteArrayOutputStream of bytes to be decoded
     */
    @Override
    public ByteArrayOutputStream encode(ByteArrayOutputStream message) {
        byte[] uncoded = message.toByteArray();
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        try {
            byte[] coded = c.doFinal(uncoded);
            result.write(coded);  
        } catch (IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(DESDecode.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DESEncode.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
}
