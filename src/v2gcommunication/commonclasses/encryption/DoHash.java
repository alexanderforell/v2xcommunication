/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.encryption;

/**
 * Interface {@code DoHash} 
 *
 * Provides method {@code doHash} to hash Strings
 * 
 * @author Alexander Forell
 */
public interface DoHash {
    /**
     * Method hashes a String and returns a hashed String.
     * 
     * @param text      String to hash
     * @return          String hashed
     */
    public String doHash(String text);
}
