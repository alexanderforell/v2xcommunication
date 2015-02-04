/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.tasks;


/**
 * Interface used to inform TaskManagement that all data in a Task has been
 * stored.
 * 
 * @author Alexander Forell
 */
public interface DataStored {
    public void dataStored(Task ta);
}
