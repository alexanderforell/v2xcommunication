/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.tasks;

/**
 * Interface used to obtain a new task for data being received.

 * @author Alexander Forell
 */
public interface NewReceiveTask {
    /**
     * Method retuns new Task.
     * 
     * 
     * @param VIN       String VIN/username
     * @param taskID    UUID of Task
     * @return          Task to which data will be added.
     */
    public Task newReceiveTask(String VIN, String taskID);
}
