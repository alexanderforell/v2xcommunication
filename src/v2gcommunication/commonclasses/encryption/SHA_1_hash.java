/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class provides a method to calculate a sha-1 hash String of any given 
 * string.
 * 
 * @author Alexander Forell
 */
public class SHA_1_hash implements DoHash{
    
    /**
     * Method {@code doHash} calculates SHA-1 hash of the text passed to the 
     * method
     * 
     * @param text String with text to be hashed
     * @return hash of the String
     */
    @Override
    public String doHash(String text){
        try {
            MessageDigest digest = MessageDigest.getInstance( "SHA" );
            StringBuilder build = new StringBuilder();
            digest.update(text.getBytes());
            byte[] sha = digest.digest();
            
            for ( byte b : sha ) build.append(String.format("%02x", b));
            
            return build.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SHA_1_hash.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
