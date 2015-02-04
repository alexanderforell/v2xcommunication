/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.tasks;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;
import v2gcommunication.commonclasses.sessions.DataAdded;

/**
 * Class {@code Task} provides fields for the taskState, whether the task is
 * known to the receiving side, the VIN, the taskID, and buffer (ArrayList) for 
 * DataElements that shall be transmitted to receiving side, or Element which
 * have been received.
 * 
 * The class also provides methods to Manage the Task
 * 
 * @author Alexander Forell
 */
public class Task { 
    /**
     * Field for the state of the Task can either be running or end.
     * 
     * If TaskState is end no more DataElements can be added to the task.
     */
    private TaskState taskState;
    /**
     * Transmission state indicates if task is known to receiver.
     */
    private TransmissionState transmissionState;
    /**
     * ID of the task
     */
    private final String TaskID;
    /**
     * VIN of the vehcile
     */
    private final String VIN;
    /**
     * ArrayList buffer for Data.
     */
    private final ArrayList<ParameterSet> dataElements = new ArrayList<ParameterSet>();
    /**
     * Lock
     */
    private final ReentrantLock  lock = new ReentrantLock();
    
    private DataAdded dataAdded;
    
    
    /**
     * Constructor initilizes Task as sender. A Random Task ID will be generated.
     * 
     * @param VIN VIN of the vehcile
     */
    Task(TaskManagement taskManager, String VIN){
        this.VIN = VIN;
        UUID sID = UUID.randomUUID();
        this.TaskID = sID.toString();
        this.taskState = TaskState.TRANSMIT;    
        this.transmissionState = TransmissionState.UNKNOWN;
        this.dataAdded = null;
    }
     
    /**
     * Constructor initilizes Task as sender or receiver.
     * 
     * Task ID is passed to constructor. Most appropriate for
     * Task answering requests.
     * 
     * @param VIN VIN of the vehcile
     */
    Task(TaskManagement taskManager, String VIN, String taskID){
        this.VIN = VIN;
        this.TaskID = taskID;
        this.taskState = TaskState.TRANSMIT;    
        this.transmissionState = TransmissionState.UNKNOWN;
        this.dataAdded = null;
    }
    
    Task(TaskManagement taskManager, String VIN, String taskID, int minMessagesBeforeTransmit){
        this.VIN = VIN;
        this.TaskID = taskID;
        this.taskState = TaskState.TRANSMIT;    
        this.transmissionState = TransmissionState.UNKNOWN;
        this.dataAdded = null;
    } 
    
    /**
     * Adds a dataElement to the buffer.
     * 
     * @param parameterName
     * @param parameterValue
     * @param parameterType
     * @param timeStamp 
     */
    public void addDataElement(String parameterName, String parameterValue, String parameterType, String timeStamp){    
        ParameterSet message = new ParameterSet();
        message.parameterName = parameterName;
        message.parameterValue = parameterValue;
        message.parameterType = parameterType;
        message.timeStamp = timeStamp;
        lock.lock();
        try {
            dataElements.add(message);
        } finally {
            lock.unlock();
            if (dataAdded != null){
                dataAdded.dataAdded();
            }
            
        }
    }
    /**
     * Adds a dataElement to the buffer
     * 
     * @param dA 
     */
    public void addDataElement(ParameterSet dA){    
        lock.lock();
        try {
            dataElements.add(dA);
        } finally {
            lock.unlock();
            if (dataAdded != null){
                dataAdded.dataAdded();
            }
        }
    }
    
    /**
     * Returns ID of this task
     * 
     * @return task ID
     */
    public String getTaskID(){
        return TaskID;
    }
    
    /**
     * Returns VIN of this task
     * 
     * @return VIN
     */
    public String getVIN(){
        return VIN;
    }
    
    /**
     * Returns state of this task
     * 
     * @return taskState
     */
    public TaskState getTaskState(){
        return this.taskState;
    }
    
    /**
     * Returns transmissonState of this task
     * 
     * @return transmissonState
     */
    public TransmissionState getTransmissionState(){
        return this.transmissionState;
    } 
    
    /**
     * Set state of this task to end
     * 
     */
    public void endTask(){
        this.taskState = TaskState.END;
        if (dataAdded != null){
            dataAdded.dataAdded();
        }
    }
    
    /**
     * Set transmissionState of this task to known
     */
    public void setTransmissionStateKnown(){
        this.transmissionState = TransmissionState.KNOWN;
    }
    
    /**
     * Retruns number of DataElements in the buffer
     * 
     * @return  int number of dataElements.
     */
    public int size(){
        return dataElements.size();
    }
    
    /**
     * Tries to read a {@code numElements} of DataElements from the buffer. 
     * 
     * It stores the data into the ArrayList ParameterSet. In case buffer does 
     * not contain numElements Elements it returns the number of bytes read.  
     * 
     * @param messages List of Dataelements read
     * @param numElements Number of bytes to read
     * @return Number of bytes read
     */
    public int getDataElements(ArrayList<ParameterSet> messages, int numElements){
        int i;
        
        for (i=0; i<numElements && i < dataElements.size();i++){
            messages.add(dataElements.get(i)); 
        }
        
        return i;
    }
    
    /**
     * Delete {@code numElements} from buffer.
     * 
     * @param numElements Number of DataElements to be deleted.
     */
    public void deleteDataElements(int numElements){
        for (int i = 0; i < numElements;i++){
            lock.lock();
            try {
                dataElements.remove(0);
            } finally {
                lock.unlock();
            }
        }
    }
    
    /**
     * Sets the object being informed if new data is added. Used 
     * to inform transmiting Session that new data is available.
     * 
     * @param dataAdded     Object implementing dataAdded.
     */
    public void setDataAdded(DataAdded dataAdded){
        this.dataAdded = dataAdded;
    }
    

}
