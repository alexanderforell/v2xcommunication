/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.requests;



/**
 * Interface indicate that a request has been successfully transmitted.
 * 
 * @author Alexander Forell
 */
public interface RequestTransmitted {
    public void requestTransmitted(Request re);
}
