/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.tasks;

/**
 * TypeOfMessage can have the following values:
 * STARTSESSION:    Message is a start session message
 * START:           Message is a start task message
 * TRANSMIT:        Message is transmit data message for a certain task
 * END:             Message is a end task message
 * REQUEST:         Message is a request
 * UNDEFINED:       Message is not defined.
 * 
 * @author Alexander Forell
 */
public enum TypeOfMessage {
    START, TRANSMIT, END, STARTSESSION, REQUEST, UNDEFINED
}
