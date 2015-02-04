/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.requests;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import v2gcommunication.commonclasses.tasks.StartTaskTransmit;
import v2gcommunication.commoninterfaces.TaskWorker;


/**
 * Class manages Requests.
 * 
 * It holds an ExecutorService and a List of Taskworkers. The request received 
 * method initializes the TaskWorker and and runs it.
 * 
 * In the List of TaskWorkers the individual server functions are registered.
 * If a Request is received, a new TaskWorker is instanciated, initialized and 
 * run in the ExecutorService.
 * 
 * The class holds fields for AddRequest and StartTaskTransmi for the 
 * TaskWorkers being able to answer.
 * 
 * It also holds a list of answering requests, in case the receiving end is 
 * offline.
 * 
 * @author Alexander Forell
 */
public class RequestManagement implements AddRequest, NewSessionGetRequests, RequestTransmitted, RequestReceived{
    
    private ArrayList<Request> requestsToTransmit;
    private Lock requestLock;
    private ArrayList<Class<? extends TaskWorker>> classList;
    private ExecutorService executor;
    private AddRequest requestReply;
    private StartTaskTransmit taskReply;
    
    /**
     * Construtor initializes ExecutorService, ArrayList of TaskWorkers
     * and ArrayList of Tasks, as well as Lock, for the list of Tasks in order 
     * to make them usable thread safe. 
     */
    public RequestManagement(){
        this.requestsToTransmit = new ArrayList<Request>();
        this.executor = Executors.newCachedThreadPool();
        this.classList = new ArrayList<Class<? extends TaskWorker>>();
        requestLock = new ReentrantLock();
    }
    
    /**
     * Method to add a new TaskWorker.
     * 
     * @param tW 
     */
    public void addTaskWorker(Class<? extends TaskWorker> tW){
        classList.add(tW);
    }
    /**
     * Set class from which new tasks to transmit can be obtained.
     * 
     * @param taskReply 
     */
    public void setStartTaskTransmit(StartTaskTransmit taskReply){
        this.taskReply = taskReply;
    }
    /**
     * class to which new Requests (replies) are send (Session Management) 
     * 
     * @param requestReply 
     */
    public void setAddRequest(AddRequest requestReply){
        this.requestReply = requestReply;
    }
    /**
     * Method processes new incoming Request. It iterates through the List
     * of TaskWorkers and checks whether the classname equals the the function
     * name provided in Request. If so, a new Instance of the TaskWorker is 
     * build and run.
     * 
     * @param request 
     */
    @Override
    public void requestReceived(Request request){
        for (Class<? extends TaskWorker> ctW:classList){
            if(ctW.getSimpleName().equals(request.getFunction())){
                try {
                    TaskWorker tW = ctW.newInstance();
                    tW.inputForRunnable(request, this, taskReply);
                    executor.execute(tW);
                } catch (InstantiationException | IllegalAccessException ex) {
                    Logger.getLogger(RequestManagement.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * Method to add new Request
     * 
     * @param request 
     */
    @Override
    public void addRequest(Request request) {
        requestLock.lock();
        try {
            this.requestsToTransmit.add(request);
        } finally {
            requestLock.unlock();
        }
        this.requestReply.addRequest(request);
    }

    /**
     * If a new Session is instanciated, this method is called. It takes 
     * requests from the buffer and passes them to the Sessions 
     * {@code addRequests} method.
     * 
     */
    @Override
    public void newSessionGetRequests(GetRequests session) {
        String usernameVIN = session.getVIN();
        ArrayList<Request> req = new ArrayList<Request>();
        requestLock.lock();
        try {
            for (Request re:requestsToTransmit){
                if (re.getUserNameVIN().equals(usernameVIN)){
                    req.add(re);
                }
            }
            session.addRequests(req);
        } finally {
            requestLock.unlock();
        }
    }

    /**
     * Called if request is transmitted so it can be used from the buffer.
     * 
     * @param re 
     */
    @Override
    public void requestTransmitted(Request re) {
        requestLock.lock();
        try {
            requestsToTransmit.remove(re);
        } finally {
            requestLock.unlock();
        }
    }
    
    
}
