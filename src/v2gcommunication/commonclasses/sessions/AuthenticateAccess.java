/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.sessions;

/**
 * Interface used to Authenticate incoming connection.
 * 
 * @author Alexander Forell
 */
public interface AuthenticateAccess {
    /**
     * Method is called when new Session is openedn Returns SessionType,
     * 
     * VEHICLE, CLIENT is returned in case vehicle or client register with  
     * valid credentials.
     * 
     * UNKNOWN is returned, if login credentials are invalid. Session will then
     * close the connection.
     * 
     * @param VIN
     * @param password
     * @return 
     */
    public SessionType authenticateAccess(String VIN, String password); 
}
