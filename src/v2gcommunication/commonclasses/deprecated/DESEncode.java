/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.deprecated;

import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



/**
 *
 * @author alexander
 */
public class DESEncode extends Encode{
    public DESEncode(OutputStream out){
        initializeCypher();
        
        cout = new CipherOutputStream(out,c);
        
    }
    
    private void initializeCypher() {
        String key="01234567";
        Key k = new SecretKeySpec( key.getBytes(), "DES" );
        try {
            c = Cipher.getInstance( "DES/CBC/PKCS5Padding" );
            IvParameterSpec iv2 = new IvParameterSpec(new byte[8]);
            c.init(Cipher.ENCRYPT_MODE, k,iv2);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
            Logger.getLogger(DESEncode.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
    
    
    
}
