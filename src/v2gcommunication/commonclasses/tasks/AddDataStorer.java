/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.tasks;

import v2gcommunication.commonclasses.sessions.GetTasks;

/**
 * Interface used to pass an Object that can store the data of a task.
 * The Class of this object needs to implement GetTasks
 * 
 * @author Alexander Forell
 */
public interface AddDataStorer {
    public void addDataStorer(GetTasks dataStorer);
}
