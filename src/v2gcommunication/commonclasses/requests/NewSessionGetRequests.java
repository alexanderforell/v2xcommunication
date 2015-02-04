/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.requests;

/**
 * Interface to inform if new session has been started and to get Requests from 
 * Buffer
 * 
 * @author Alexander Forell
 */
public interface NewSessionGetRequests {
    public void newSessionGetRequests(GetRequests session);
}
