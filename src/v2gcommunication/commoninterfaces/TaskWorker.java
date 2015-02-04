/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commoninterfaces;

import v2gcommunication.commonclasses.tasks.StartTaskTransmit;
import v2gcommunication.commonclasses.requests.AddRequest;
import v2gcommunication.commonclasses.requests.Request;



/**
 * Interface for TaskWorkers, it will be called once a Request is received.
 * 
 * @author Alexander Forell
 */
public interface TaskWorker extends Runnable {
    /**
     * Method takes inputs for the TaskWorker, especially the Request iteself.
     * 
     * 
     * @param requestProcessed  Request to be processed in Runnable.
     * @param requestReply      Interface to reply with a request.
     * @param taskReply         Iterface to reply with a task
     */
    public void inputForRunnable(Request requestProcessed, AddRequest requestReply, StartTaskTransmit taskReply );
}
