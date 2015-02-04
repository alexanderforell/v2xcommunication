/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.requests;


/**
 * Interface used to interprete received requests.
 * 
 * @author Alexander Forell
 */
public interface RequestReceived {
    public void requestReceived(Request request);
}
