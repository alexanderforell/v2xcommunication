/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commoninterfaces;

import v2gcommunication.commonclasses.tasks.Task;

/**
 * Interface to add Task to data storing object.
 * 
 * @author Alexander Forell
 */
public interface NewTaskToStore {
    /**
     * Method passes on a Task
     * 
     * @param ta new Receive Task
     */
    public void newTaskToStore(Task ta);
}
