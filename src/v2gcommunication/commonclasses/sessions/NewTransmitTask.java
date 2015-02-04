/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.sessions;

import v2gcommunication.commonclasses.tasks.Task;

/**
 * Interface used to add a new task to Transmit to session.
 * 
 * @author Alexander Forell
 */
public interface NewTransmitTask {
    /**
     * Adds a Task to transmit to the session
     * 
     * @param ta    Task to transmit
     */
    public void addTransmitTask(Task ta);
}
