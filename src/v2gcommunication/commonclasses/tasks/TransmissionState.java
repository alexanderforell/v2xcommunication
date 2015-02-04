/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.tasks;

/**
 * State can be either KNWON if receiving end already knows the take, i.e. a 
 * start task message has been sent, or UNKNOWN when reciever doesn't know the 
 * task i.e. start task message has not been transmitted.
 * 
 * @author Alexander Forell
 */
public enum TransmissionState {
    KNOWN, UNKNOWN
}
