/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexander
 */
public class Test {
    public static void main(String args[]){
        try {
            MessageDigest md = MessageDigest.getInstance( "SHA" );
            byte[] digest = md.digest( "123".getBytes() );
            System.out.println("Länge: "+digest.length);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
