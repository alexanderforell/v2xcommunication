/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses;

import v2gcommunication.commonclasses.deprecated.DESEncode;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author alexander
 */
public class DESDecode extends Decode{

    @Override
    public void initializeCipher() {
        String key="01234567";
        Key k = new SecretKeySpec( key.getBytes(), "DES" );
        try {
            c = Cipher.getInstance( "DES/CBC/PKCS5Padding" );
            IvParameterSpec iv2 = new IvParameterSpec(new byte[8]);
            c.init(Cipher.DECRYPT_MODE, k, iv2);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException  ex) {
            Logger.getLogger(DESEncode.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
