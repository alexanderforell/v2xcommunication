/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.tasks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import v2gcommunication.commonclasses.sessions.GetTasks;
import v2gcommunication.commonclasses.sessions.NewTransmitTask;
import v2gcommunication.commoninterfaces.NewTaskToStore;


/**
 * Class {@code} organizes lists of tasks for receiving and transmitting data.
 * 
 * It's main purpose is to buffer these tasks and keep lists in case a session
 * collapses. 
 * 
 * It exposes methods to create Tasks, delete and pass buffered Task on to new
 * sessions.
 * 
 * @author Alexander Forell
 */
public class TaskManagement implements NewReceiveTask, DataTransmitted, 
    NewSessionGetTasks, AddDataStorer, DataStored, StartTaskTransmit{
    // List of Tasks receiving data
    private final ArrayList<Task> taskReceive;
    // List of Tasks transmitting data
    private final ArrayList<Task> taskTransmit;
    // Lock for taskTransmit
    private final Lock transmitList;
    // Lock for taskReceive
    private final Lock receiveList;
    // Interface called to pass on new transmit tasks to session
    private NewTransmitTask newTransmitTask = null;
    // Interface called to pass receiving tasks to dataStorer
    private NewTaskToStore newTaskToStore = null;
    
    /**
     * Constructor initializing fields
     */
    public TaskManagement(){
        this.taskReceive = new ArrayList<Task>();
        this.taskTransmit = new ArrayList<Task>();
        this.transmitList = new ReentrantLock();
        this.receiveList = new ReentrantLock();
    }
    
    /**
     * Starts a new Task and adds it to the classes list of Tasks to be 
     * transmitted.
     * 
     * This method creates a new Random ID.
     * 
     * @param VIN       VIN/username of vehicle or client   
     * @return          Task to which data can be added
     */
    @Override
    public Task startTaskTransmit(String VIN){
        Task task = new Task(this, VIN);
        transmitList.lock();
        try {
            this.taskTransmit.add(task);
        } finally {
            transmitList.unlock();
        }
        if (this.newTransmitTask != null){
            this.newTransmitTask.addTransmitTask(task);
        }
        return task;
    }
    
    /**
     * Starts a new Task and adds it to the classes list of Tasks to be 
     * transmitted.
     * 
     * This uses the taskID passed as the new ID for the Task.
     * This mehtod shall be used to answer Requests using its ID, so the
     * Task can be linked to the Request.
     * 
     * @param VIN       VIN/username of vehicle or client  
     * @param taskID    UUID of task
     * @return          Task to which data can be added
     */
    @Override
    public Task startTaskTransmit(String VIN, String taskID){
        Task task = new Task(this, VIN, taskID);
        transmitList.lock();
        try {
            this.taskTransmit.add(task);
        } finally {
            transmitList.unlock();
        }
        if (this.newTransmitTask != null){
            this.newTransmitTask.addTransmitTask(task);
        }
        return task;
    }
    
    /**
     * Starts a new Tasks and adds it to its list of data receiving tasks
     * This method is called by session if a new Task is transmitting data.
     * 
     * The method also calls the object storing data and passes on the Task.
     * 
     * @param VIN       VIN/username of vehicle or client 
     * @param taskID    UUID of task
     * @return          Task to which data can be added
     */
    @Override
    public Task newReceiveTask(String VIN, String taskID) {
        Task task = new Task(this, VIN, taskID);
        receiveList.lock();
        try {
            this.taskReceive.add(task);
        } finally {
            receiveList.unlock();
        }
        if (this.newTaskToStore != null){
            this.newTaskToStore.newTaskToStore(task);
        }
        return task;
    }

    /**
     * Method called by session if all data in the Task has been transmitted, 
     * and the TaskState is END.
     * 
     * It removes the Task from the list of transmitting Tasks
     * 
     * @param ta        Task to be removed from the List
     */
    @Override
    public void dataTransmitted(Task ta) {
        transmitList.lock();
        try {
            taskTransmit.remove(ta);
        } finally {
            transmitList.unlock();
        }
    }

    /**
     * Called by Session once it has been assigned a username or a vin.
     * The method will pass on the Tasks to be received and and transmitted 
     * within the session.
     * 
     * @param session   Session implementing GetTasks
     */
    @Override
    public void newSessionGetTasks(GetTasks session) {
        String VIN = session.getVIN();
        ArrayList<Task> taTr = new ArrayList<Task>();
        ArrayList<Task> taRe = new ArrayList<Task>();
        transmitList.lock();
        try {
            for (Iterator<Task> i1 = taskTransmit.iterator(); i1.hasNext();){
                Task ta = i1.next();
                if (ta.getVIN().equals(VIN)){
                    taTr.add(ta);
                }
            }
        } finally {
            transmitList.unlock();
        }
        receiveList.lock();
        try {
            for (Iterator<Task> i1 = taskReceive.iterator(); i1.hasNext();){
                Task ta = i1.next();
                if (ta.getVIN().equals(VIN)){
                    taRe.add(ta);
                }
            }
        } finally {
            receiveList.unlock();
        }
        session.addTransmitTasks(taTr);
        session.addReceiveTasks(taRe);   
    }
    
    /**
     * Object to be informed of new Transmit task is added
     * 
     * @param newTransmitTask  Object implementing NewTransmitTask
     */
    public void setNewTransmitTask(NewTransmitTask newTransmitTask){
        this.newTransmitTask = newTransmitTask;
    }
    /**
     * Object to be informed if new Task is Received and incoming data shall be
     * stored.
     * 
     * @param newTaskToStore 
     */
    public void setNewTaskToStore(NewTaskToStore newTaskToStore){
        this.newTaskToStore = newTaskToStore;
    }
    
    /**
     * Object Storing data is Added. Gets all tasks which are currently 
     * receiving data.
     * 
     * @param dataStorer 
     */
    @Override
    public void addDataStorer(GetTasks dataStorer) {
        dataStorer.addReceiveTasks(taskReceive);
    }
    
    /**
     * Mehtod is called by object stroing data if data in Tasl is stored and
     * TaskState is END.
     * 
     * It removes the Task from the list of tasks receiving data.
     * 
     * @param ta    Task to be removed from the list.
     */
    @Override
    public void dataStored(Task ta) {
        receiveList.lock();
        try {
            taskReceive.remove(ta);
        } finally {
            receiveList.unlock();
        }
    }
    
    /**
     * Method to get a receive Task. with a certain ID.
     * 
     * @param taskID    Task ID.
     * @return          receiveTask
     */
    public Task getReceiveTask(String taskID){
        receiveList.lock();
        try {
            for (Task ta:taskReceive){
                if (ta.getTaskID().equals(taskID)){
                    return ta;
                }
            }
        } finally {
            receiveList.unlock();
        }
        return null;
    }
    
    
}
