/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.tasks;

import v2gcommunication.commonclasses.sessions.GetTasks;

/**
 * Interface implemented by Task Management, called by a session to get Tasks 
 * from Buffer.
 * 
 * Session must implement {@code GetTasks}
 * 
 * @author Alexander Forell
 */
public interface NewSessionGetTasks {
    public void newSessionGetTasks(GetTasks session);
    
}
