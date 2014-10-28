/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.vehicle;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * Class {@code CypheredReadWrite} provides static methods to encrypt and write 
 * Strings to an OutputStream, as well as read and decrypt strings from an output
 * stream
 * 
 * @author alexander
 */
public class CypheredReadWrite {
    
    /**
     * Method takes the listed parameters and adds the String "$EOF$" to the 
     * message to indicate the end of the transmission.
     * 
     * String is then ciphered and witten to the output stream.
     * 
     * 
     * @param message       String containing the message to be transmitted
     * @param out           Output Stream String is written on
     * @param pass          String used to Create DES key
     * @throws Exception    Cipher.getInstance may Throw exception if DES is not
     *                      available
     *                      I/O exceptions for OutputStream
     */
    public static void writeDES (String message, OutputStream out, String pass) throws Exception{
        // Add $EOF$ to message, to inidcate message end, convert to byte array
        byte[] bytes = (message + "$END$").getBytes();
        // create a cipher instance using "DES"
        Cipher c = Cipher.getInstance( "DES" );
        // Create the Secret key for ciphering form {@param pass}
        Key k = new SecretKeySpec( pass.getBytes(), "DES" );
        // Initiate cipher to encrypt
        c.init( Cipher.ENCRYPT_MODE, k );
        
        //Create new CipherOutputStream
        OutputStream cos = new CipherOutputStream( out, c );
        //write message and encrypt
        System.out.println(new String(bytes));
        cos.write(bytes);
        //close stream
        //cos.close();
    }
    /**
     * Method reads transmitted cyphered message from Input Stream, decodes it
     * and bufferes until String "$EOF$". then returns the String w/o "$EOF"
     *
     * @param is            InputStream to read from
     * @param pass          String used to Create DES key
     * @return              String containing message (decoded)
     * @throws Exception    Cipher.getInstance may Throw exception if DES is not
     *                      available
     *                      I/O exceptions for InputStream
     */
    public static String readDES (InputStream is, String pass) throws Exception{
        // create a cipher instance using "DES"
        Cipher c = Cipher.getInstance( "DES" );
        // Create the Secret key for ciphering form {@param pass}
        Key k = new SecretKeySpec( pass.getBytes(), "DES" );
        // Initiate cipher to decrypt
        c.init( Cipher.DECRYPT_MODE, k );
        //Create new CipherInputStream
        CipherInputStream cis = new CipherInputStream( is, c );
        //Create new ByteArrayOutputStream to read byte by byte
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        
        // 5 Bytes to build fifo buffer
        // reader needs to buffer 5 bytes ahead, in order to find "$EOF$".
        byte b = -1;
        byte b1= -1;
        byte b2= -1;
        byte b3= -1;
        byte b4;
        while (true) {
            //shift buffer
            b4 = b3;
            b3 = b2;
            b2 = b1;
            b1 = b;
            //read new byte
            if ((b = (byte) cis.read())!=-1){
                // if last 5 Bytes are EOF then return message
                if (b4=='$' && (char)b3=='E' && (char)b2=='N' && (char)b1=='D' && (char)b=='$'){
                    return bos.toString();
                }
                // else add b4 to message IMPORTANT if (b4!=-1) is needed to 
                // skip the first 4 bytes from the initialisation. 
                else {
                    if (b4 != -1) {
                        bos.write(b4);
                    }
                }
            
            }
        } 
    }
    
}
