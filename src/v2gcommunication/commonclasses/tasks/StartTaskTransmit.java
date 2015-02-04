/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.tasks;

/**
 * Interface implemented by TaskManagement to obtain new Task through which data
 * shall be transmitted.
 * 
 * @author Alexander Forell
 */
public interface StartTaskTransmit {
    /**
     * Creates a Task with a random UUID
     * 
     * @param VIN       VIN or username of Sender
     * @return          Task
     */
    public Task startTaskTransmit(String VIN);
    /**
     * Creates a Task with given UUID. This mehtod shall be used to answer 
     * incoming Requests.
     * 
     * @param VIN       VIN or username of Sender  
     * @param taskID    TaskID
     * @return 
     */
    public Task startTaskTransmit(String VIN, String taskID);
}
