/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.transmission;

import java.util.ArrayList;
import v2gcommunication.commonclasses.requests.Request;
import v2gcommunication.commonclasses.tasks.ParameterSet;
import v2gcommunication.commonclasses.tasks.TypeOfMessage;


/**
 * Interface {@code MessageBuilder} to transfer 
 * the data between client and server or vehicle and server. 
 * 
 * @author Alexander Forell
 */
public interface MessageBuilder {
    /**
     * Builds a start session message for a vehicle or a user
     * 
     * @param VIN       username or VIN 
     * @param password  password of the user
     * @return          StringBuffer message
     */
    public StringBuffer buildStartSessionMessage(StringBuffer VIN, StringBuffer password);
    /**
     * Builds a request message for a client
     * 
     * @param VIN       username or VIN
     * @param taskId    ID of the request
     * @param re        Request to be transmitted.
     * @return          StringBuffer message
     */
    public StringBuffer buildRequestMessage(StringBuffer VIN, StringBuffer taskId, Request re);
    /**
     * Build a start Task message. 
     * 
     * @param VIN       vin or username of sender
     * @param taskId    Task ID
     * @return          StringBuffer message
     */
    public StringBuffer buildStartTaskMessage(StringBuffer VIN, StringBuffer taskId);
    /**
     * Build a message to end a Task
     * 
     * @param VIN       vin or username of sender
     * @param taskId    Task ID
     * @return          StringBuffer message
     */ 
    public StringBuffer buildEndTaskMessage(StringBuffer VIN, StringBuffer taskId);
    /**
     * Builds a Transmit Message for a Task
     * 
     * @param VIN               vin or username of sender
     * @param taskId            Task ID
     * @param dataElements      Data Elements on the Task 
     * @return                  StringBuffer message
     */
    public StringBuffer buildTransmitTaskMessage(StringBuffer VIN, StringBuffer taskId, 
            ArrayList<ParameterSet> dataElements);
    /**
     * Reads a message and returns the type of message. The Paramenters 
     * {@code dataElements, re, VIN, password, taskID} are passed by 
     * reference and filled by the method
     * 
     * @param message       message passed to extract content
     * @param dataElements  passed by reference to store content
     * @param re            passed by reference to store request
     * @param VIN           passed by reference to store VIN
     * @param password      passed by reference to store password
     * @param taskID        passed by reference to task ID
     * @return              Type of message
     */
    public TypeOfMessage readMessage(StringBuffer message, 
            ArrayList<ParameterSet> dataElements, ArrayList<Request> re, StringBuffer VIN, StringBuffer password, 
            StringBuffer taskID);
}
