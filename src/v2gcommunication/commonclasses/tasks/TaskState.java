/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.tasks;

/**
 * State of a Task can be either TRANSMT if there will still be data added,
 * or END if all has been added.
 * 
 * @author Alexander Forell
 */
public enum TaskState {
    TRANSMIT, END
}
