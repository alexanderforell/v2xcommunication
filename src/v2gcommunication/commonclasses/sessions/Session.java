/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.sessions;

import v2gcommunication.commonclasses.tasks.NewReceiveTask;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import v2gcommunication.commonclasses.requests.Request;
import v2gcommunication.commonclasses.tasks.ParameterSet;
import v2gcommunication.commonclasses.tasks.TypeOfMessage;
import v2gcommunication.commonclasses.tasks.Task;
import v2gcommunication.commonclasses.tasks.TaskState;
import v2gcommunication.commonclasses.tasks.TransmissionState;
import v2gcommunication.commonclasses.requests.AddRequest;
import v2gcommunication.commonclasses.encryption.Decode;
import v2gcommunication.commonclasses.encryption.Encode;
import v2gcommunication.commonclasses.transmission.MessageBuilder;
import v2gcommunication.commonclasses.tasks.NewSessionGetTasks;
import v2gcommunication.commonclasses.transmission.ReadMessage;
import v2gcommunication.commonclasses.tasks.DataTransmitted;
import v2gcommunication.commonclasses.requests.GetRequests;
import v2gcommunication.commonclasses.requests.NewSessionGetRequests;
import v2gcommunication.commonclasses.requests.RequestReceived;
import v2gcommunication.commonclasses.requests.RequestTransmitted;
import v2gcommunication.commonclasses.transmission.WriteMessage;


/**
 * Class {@code Session} handles communication between client and server, or 
 * vehicle and Server respectively. There is exactly one session for each 
 * connection vehicle and server or client and server.
 * 
 * The Class includes two Runnables, one to transmit data and one to recieve 
 * data. The data to be transmitted is taken from 3 Lists in Total
 * - List of Task, which hold data to transmit
 * - List or Requests which need to be transmitted.
 * - List of Tasks which are currently received.
 * 
 * It is not needed to have List of Reqests being received, since received 
 * Request always consist of only one message, and are directly passed on to
 * the {@code RequestReceived} Object by its {@code requestReceived} Method.
 * 
 * The class includes for constructors.
 * 1. Constructor for the vehicle or the client with encryption
 * 2. Constructor for the vehicle or the client w/o encryption
 * 3. Constructor for the server with encryption
 * 4. Constructor for the server w/o encryption
 * 
 * The constructors must be used in pairs. Either both sides with encryption or 
 * both sides w/o encryption.
 * 
 * The two Runnables iterate through the Task, btw. Request list and attempt to
 * receive or write data from or onto the stream.
 * 
 * The Runnable to used to receive data blocks automatically if there is no 
 * data to be read from the stream. This is due to the fact that the java.io.*
 * classes read in bloked mode.
 * 
 * The Runnable for transmit would always try to transmit 0 bytes of data and
 * therefore constantly iterate through all Tasks and Requests. There mechanisms
 * for the transmit runnable to wait if there is no data available need to 
 * implemented.
 * 
 * Session needs to fullfil the following provide the following interfaces in 
 * order to operate properly.
 * 
 * - A possiblity to add new tasks to transmit
 * - A possiblity to add new Requests to transmit.
 * - A possiblity to inform when tasks are completely transmitted
 * - A possibitly to inform the session management, that data has been added to
 *      the a task.
 * - A possiblity to inform if a Request is transmitted.
 * - A possiblity to inform when new tasks are received, and start a new 
 *      receive task.
 * - A possiblity to get all tasks which are buffered once the session is 
 *      started.
 * - A possiblity to get all requests buffed once a a sessiion is started.
 * 
 * @author Alexander Forell
 */
public class Session implements GetTasks, NewTransmitTask, DataAdded, AddRequest, GetRequests{
    
    // InputStream used to receive data
    private final InputStream in;
    // OutputStream used to send data
    private final OutputStream out;
    // ArrayList for Tasks to be transmitted
    private final ArrayList<Task> transmitTasks;
    // ArrayList for Requests to be trasnmitted
    private final ArrayList<Request> transmitRequest;
    // ArrayList for Tasks to be received
    private final ArrayList<Task> receiveTasks;
    // Interface to encrypt data
    private final Encode enc;
    // Interface to decrypt data
    private final Decode dec;
    // Interface to build messages
    private final MessageBuilder messageBuilder;
    // Interface to read a message on the input stream
    private final ReadMessage readMessage;
    // Interface to write a message on rhe output stream
    private final WriteMessage writeMessage;
    // Interface to infrom TaskManagement, that a new ReceiveTask has been 
    // started
    private final NewReceiveTask newReceiveTask;
    // Interface to inform if data in a Task has been transmitted
    private final DataTransmitted taskTransmitted;
    // Interface to inform if Request was transmitted
    private final RequestTransmitted requestTransmitted;
    // Interface to run authentification (only server side)
    private final AuthenticateAccess authenticateVIN;
    // Locks for concurrent use of receive, transmit lists
    private final Lock lockReceiveList;
    private final Lock lockTransmitList;
    // lock to wait for authentifications
    private final Lock lockWaitForVIN;
    // condition to await authentification, before transmitting data
    private final Condition awaitVIN;
    // condtition to await data
    private final Condition dataToTransmit;
    // interface used to get tasks from buffer
    private final NewSessionGetTasks newSessionGetTasks;
    // interface used to get request from buffer
    private final NewSessionGetRequests newSessionGetRequests;
    // interface to infrom if a new request was recieved
    private final RequestReceived requestReceived;
    // Field holding vin
    private String myVIN;
    // Field holding password
    private String password;
    // Field to indicate, that the socket conneciton is still available
    private boolean socketAvailable = true;
    // Field for sessionType
    private SessionType sessionType;
    
    /**
     * Constructor for session objects, initializes the fields and starts. 
     * Stores the passed object internally and runs transmitting and recieving
     * thread.
     * 
     * This constructor is to be called by the vehcile or client, if encryption
     * shall be used
     * 
     * @param socket                Socket object to transmit or recieve data
     * @param executor              ExecutorService object in which threads are
     *                              run
     * @param enc                   Encode Object used to encrypt data
     * @param dec                   Decode Object used to decrypt data
     * @param messageBuilder        MessageBuilder object to build messages
     * @param readMessage           ReadMessage Object used to read incoming 
     *                              data
     * @param writeMessage          WriteMessage Object used to write outgoing 
     *                              data
     * @param newReceiveTask        Object informed about new incoming tasks
     * @param taskTransmitted       Object informed when a task is transmitted
     * @param requestTransmitted    Object informed when a request is transmitted
     * @param requestReceived       Object informed when a request is recieved
     * @param newSession            Object informed when a new session is started
     *                              used to get Taks from buffer
     * @param newSessionGetRequests Object informed when a new session is started,
     *                              used to get Requests from buffer
     * @param VIN                   VIN of vehicle or username
     * @param password              password of user
     * @throws IOException          Thrown if input stream and output stream
     *                              can not be built.
     */
    public Session(Socket socket, ExecutorService executor, Encode enc, 
            Decode dec, MessageBuilder messageBuilder, ReadMessage readMessage,
            WriteMessage writeMessage, NewReceiveTask newReceiveTask, 
            DataTransmitted taskTransmitted, RequestTransmitted requestTransmitted, 
            RequestReceived requestReceived, NewSessionGetTasks newSession, 
            NewSessionGetRequests newSessionGetRequests, String VIN, String password) throws IOException{
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.transmitTasks = new ArrayList<Task>();
        this.receiveTasks = new ArrayList<Task>();
        this.transmitRequest = new ArrayList<Request>();
        this.enc = enc;
        this.dec = dec;
        this.messageBuilder = messageBuilder;
        this.readMessage = readMessage;
        this.writeMessage = writeMessage;
        this.newReceiveTask = newReceiveTask;
        this.lockReceiveList = new ReentrantLock();
        this.lockTransmitList = new ReentrantLock();
        this.dataToTransmit =  this.lockTransmitList.newCondition();
        this.lockWaitForVIN = new ReentrantLock();
        this.awaitVIN = lockWaitForVIN.newCondition();
        this.authenticateVIN = null;
        this.taskTransmitted = taskTransmitted;
        this.requestTransmitted = requestTransmitted;
        this.requestReceived = requestReceived;
        this.myVIN = VIN;
        this.password = password;
        this.newSessionGetTasks = newSession;
        this.newSessionGetRequests = newSessionGetRequests;
        executor.execute(transmit);
        executor.execute(receive);
    }

    /**
     * Constructor for session objects, initializes the fields and starts. 
     * Stores the passed object internally and runs transmitting and recieving
     * thread.
     * 
     * This constructor is to be called Server, if encryption
     * shall be used
     * 
     * @param socket                Socket object to transmit or recieve data
     * @param executor              ExecutorService object in which threads are
     *                              run
     * @param enc                   Encode Object used to encrypt data
     * @param dec                   Decode Object used to decrypt data
     * @param messageBuilder        MessageBuilder object to build messages
     * @param readMessage           ReadMessage Object used to read incoming 
     *                              data
     * @param writeMessage          WriteMessage Object used to write outgoing 
     *                              data
     * @param newReceiveTask        Object informed about new incoming tasks
     * @param taskTransmitted       Object informed when a task is transmitted
     * @param requestTransmitted    Object informed when a request is transmitted
     * @param requestReceived       Object informed when a request is recieved
     * @param authenticateVIN       Object called to define sessionType and 
     *                              autheticate VIN or username.
     * @param newSessionGetTasks    Object informed when a new session is started
     *                              used to get Taks from buffer
     * @param newSessionGetRequests Object informed when a new session is started,
     *                              used to get Requests from buffer
     * @throws IOException          Thrown if input stream and output stream
     *                              can not be built
     */
    public Session(Socket socket, ExecutorService executor, Encode enc, 
            Decode dec, MessageBuilder messageBuilder, ReadMessage readMessage,
            WriteMessage writeMessage, NewReceiveTask newReceiveTask, 
            DataTransmitted taskTransmitted, RequestTransmitted requestTransmitted, 
            RequestReceived requestReceived, AuthenticateAccess authenticateVIN, 
            NewSessionGetTasks newSessionGetTasks, 
            NewSessionGetRequests newSessionGetRequests) throws IOException{
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.transmitTasks = new ArrayList<Task>();
        this.receiveTasks = new ArrayList<Task>();
        this.transmitRequest = new ArrayList<Request>();
        this.enc = enc;
        this.dec = dec;
        this.messageBuilder = messageBuilder;
        this.readMessage = readMessage;
        this.writeMessage = writeMessage;
        this.newReceiveTask = newReceiveTask;
        this.lockReceiveList = new ReentrantLock();
        this.lockTransmitList = new ReentrantLock();
        this.dataToTransmit =  this.lockTransmitList.newCondition();
        this.lockWaitForVIN = new ReentrantLock();
        this.awaitVIN = lockWaitForVIN.newCondition();
        this.authenticateVIN = authenticateVIN;
        this.taskTransmitted = taskTransmitted;
        this.requestTransmitted = requestTransmitted;
        this.requestReceived = requestReceived;
        this.myVIN = null;
        this.newSessionGetTasks = newSessionGetTasks;
        this.newSessionGetRequests = newSessionGetRequests;
        executor.execute(transmit);
        executor.execute(receive);
    }

    /**
     * Constructor for session objects, initializes the fields and starts. 
     * Stores the passed object internally and runs transmitting and recieving
     * thread.
     * 
     * This constructor is to be called by the vehcile or client, if encryption
     * shall not be used
     * 
     * @param socket                Socket object to transmit or recieve data
     * @param executor              ExecutorService object in which threads are
     *                              run
     * @param messageBuilder        MessageBuilder object to build messages
     * @param readMessage           ReadMessage Object used to read incoming 
     *                              data
     * @param writeMessage          WriteMessage Object used to write outgoing 
     *                              data
     * @param newReceiveTask        Object informed about new incoming tasks
     * @param taskTransmitted       Object informed when a task is transmitted
     * @param requestTransmitted    Object informed when a request is transmitted
     * @param requestReceived       Object informed when a request is recieved
     * @param newSessionGetTasks    Object informed when a new session is started
     *                              used to get Taks from buffer
     * @param newSessionGetRequests Object informed when a new session is started,
     *                              used to get Requests from buffer
     * @param VIN                   VIN of vehicle or username
     * @param password              password of user
     * @throws IOException          Thrown if input stream and output stream
     *                              can not be built
     */
    public Session(Socket socket, ExecutorService executor, 
            MessageBuilder messageBuilder, ReadMessage readMessage,
            WriteMessage writeMessage, NewReceiveTask newReceiveTask, 
            DataTransmitted taskTransmitted, RequestTransmitted requestTransmitted,
            RequestReceived requestReceived, NewSessionGetTasks newSessionGetTasks, 
            NewSessionGetRequests newSessionGetRequests, 
            String VIN, String password) throws IOException{
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.transmitTasks = new ArrayList<Task>();
        this.receiveTasks = new ArrayList<Task>();
        this.transmitRequest = new ArrayList<Request>();
        this.enc = null;
        this.dec = null;
        this.messageBuilder = messageBuilder;
        this.readMessage = readMessage;
        this.writeMessage = writeMessage;
        this.newReceiveTask = newReceiveTask;
        this.lockReceiveList = new ReentrantLock();
        this.lockTransmitList = new ReentrantLock();
        this.dataToTransmit =  this.lockTransmitList.newCondition();
        this.lockWaitForVIN = new ReentrantLock();
        this.awaitVIN = lockWaitForVIN.newCondition();
        this.authenticateVIN = null;
        this.taskTransmitted = taskTransmitted;
        this.requestTransmitted = requestTransmitted;
        this.requestReceived = requestReceived;
        this.myVIN = VIN;
        this.password = password;
        this.newSessionGetTasks = newSessionGetTasks;
        this.newSessionGetRequests = newSessionGetRequests;
        executor.execute(transmit);
        executor.execute(receive);
    }

    /**
     * Constructor for session objects, initializes the fields and starts. 
     * Stores the passed object internally and runs transmitting and recieving
     * thread.
     * 
     * This constructor is to be called Server, if encryption
     * shall not be used
     * 
     * @param socket                Socket object to transmit or recieve data
     * @param executor              ExecutorService object in which threads are
     *                              run
     * @param messageBuilder        MessageBuilder object to build messages
     * @param readMessage           ReadMessage Object used to read incoming 
     *                              data
     * @param writeMessage          WriteMessage Object used to write outgoing 
     *                              data
     * @param newReceiveTask        Object informed about new incoming tasks
     * @param taskTransmitted       Object informed when a task is transmitted
     * @param requestTransmitted    Object informed when a request is transmitted
     * @param requestReceived       Object informed when a request is recieved
     * @param authenticateVIN       Object called to define sessionType and 
     *                              autheticate VIN or username.
     * @param newSessionGetTasks    Object informed when a new session is started
     *                              used to get Taks from buffer
     * @param newSessionGetRequests Object informed when a new session is started,
     *                              used to get Requests from buffer
     * @throws IOException          Thrown if input stream and output stream
     *                              can not be built
     */
    public Session(Socket socket, ExecutorService executor, 
            MessageBuilder messageBuilder, ReadMessage readMessage,
            WriteMessage writeMessage, NewReceiveTask newReceiveTask, 
            DataTransmitted taskTransmitted, RequestTransmitted requestTransmitted, 
            RequestReceived requestReceived, AuthenticateAccess authenticateVIN, 
            NewSessionGetTasks newSessionGetTasks,
            NewSessionGetRequests newSessionGetRequests) throws IOException{
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
        this.transmitTasks = new ArrayList<Task>();
        this.receiveTasks = new ArrayList<Task>();
        this.transmitRequest = new ArrayList<Request>();
        this.enc = null;
        this.dec = null;
        this.messageBuilder = messageBuilder;
        this.readMessage = readMessage;
        this.writeMessage = writeMessage;
        this.newReceiveTask = newReceiveTask;
        this.lockReceiveList = new ReentrantLock();
        this.lockTransmitList = new ReentrantLock();
        this.dataToTransmit =  this.lockTransmitList.newCondition();
        this.lockWaitForVIN = new ReentrantLock();
        this.awaitVIN = lockWaitForVIN.newCondition();
        this.authenticateVIN = authenticateVIN;
        this.taskTransmitted = taskTransmitted;
        this.requestTransmitted = requestTransmitted;
        this.requestReceived = requestReceived;
        this.myVIN = null;
        this.newSessionGetTasks = newSessionGetTasks;
        this.newSessionGetRequests = newSessionGetRequests;
        executor.execute(transmit);
        executor.execute(receive);
    }    
    /**
     * Runnable transmitting Tasks and Requests.
     */
    private final Runnable transmit = new Runnable(){
        @Override
        public void run() {
            /**
             * if vin or username != null it was passed throuch constructor, in
             * consequence a StartSessionMessage must be transmitted.
             */
            if (myVIN != null){
                sendStartSessionMessage();
            }
            /**
             * if VIN or username equals null runnable must wait with 
             * transmitting informaion until VIN is submitted
             */
            while (myVIN == null){
                lockWaitForVIN.lock();
                try {
                    awaitVIN.await();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    lockWaitForVIN.unlock();
                }
            }
            /** 
             * Authtification successfull, get tasks and request to transmit 
             * from Buffer
             */
            getTasksAndRequests();
            
            /**
             * Looped until connection breakes down
             */
            while(socketAvailable){
                /**
                 * check if there are Tasks or requests to transmit else wait.
                 */
                lockTransmitList.lock();
                try {
                    while (transmitTasks.isEmpty() && transmitRequest.isEmpty() && socketAvailable){
                        dataToTransmit.await();
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    lockTransmitList.unlock();
                }
                /**
                 * Iterate through task list and transmit data
                 */
                lockTransmitList.lock();
                try {
                    for (Iterator<Task> i = transmitTasks.iterator(); i.hasNext(); ){
                        Task ta = i.next();
                        /**
                         * Transmit data if task is known to receiver.
                         */
                        if (ta.getTransmissionState()==TransmissionState.KNOWN){
                            /**
                             * if there is data transmit it
                             */
                            if (ta.size() > 0){
                                ArrayList<ParameterSet> dA = new ArrayList<ParameterSet>();
                                int a = 1000;
                                a = ta.getDataElements(dA, a);
                                sendTransmitDataMessage(ta.getTaskID(), dA);
                                ta.deleteDataElements(a);
                            }
                            /**
                             * if there is no data and TaskState is END. Send 
                             * end task message to complete transmission
                             */
                            else {
                                if (ta.getTaskState()==TaskState.END){
                                    sendEndTaskMessage(ta.getTaskID());
                                    ta.setDataAdded(null);
                                    //Inform that task was compelty transmitted
                                    taskTransmitted.dataTransmitted(ta);
                                    //Remove task from transmit list.
                                    i.remove();
                                }
                            }
                        }
                        /**
                         * Taks is not known to reciever send start task message
                         */
                        if (ta.getTransmissionState()==TransmissionState.UNKNOWN){
                            sendStartTaskMessage(ta.getTaskID());
                            ta.setTransmissionStateKnown();
                        }

                    }
                }
                finally {
                    lockTransmitList.unlock();
                }
                /**
                 * Iterate through list of requests to transmit. And transmit 
                 * them.
                 */
                lockTransmitList.lock();
                try {
                    for (Iterator<Request> i = transmitRequest.iterator(); i.hasNext(); ){
                        Request re = i.next();
                        sendRequestMessage(re);
                        // inform that request has been transmitted.
                        requestTransmitted.requestTransmitted(re);
                        // remove request from list 
                        i.remove();
                    }
                }
                finally {
                    lockTransmitList.unlock();
                }
                /**
                 * Check if there is still data or requests to transmit.
                 * If not wait.
                 */
                lockTransmitList.lock();
                try {
                    while(noData() && socketAvailable && transmitRequest.isEmpty()){
                        /**
                        * await() causes unlock of the object. signal() locks 
                        * the the object again, and causes continous the code.
                        * Finally unlocks the list again.
                        */
                        dataToTransmit.await();
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    lockTransmitList.unlock();
                }
            }
        }
    
    };
    
    /**
     * Runnable receiving Tasks and Requests.
     */
    private final Runnable receive = new Runnable(){

        @Override
        public void run() {
            ByteArrayOutputStream messageType = new ByteArrayOutputStream();
            ByteArrayOutputStream message = new ByteArrayOutputStream();
            /**
             * If VIN or username == null session has been started by server
             * --> Expect start session message transmitting VIN or username 
             * and password.
             */
            if (myVIN == null){
                try{
                    // read 1st meassage
                    readMessage.readMessage(message, messageType, in);
                    // decode message
                    if (dec!=null){
                        messageType = dec.decode(messageType);
                        message = dec.decode(message);
                    }
                    // initialize fields to store data
                    StringBuffer messageString = new StringBuffer();
                    messageString.append(message);
                    StringBuffer messageTypeString = new StringBuffer();
                    messageTypeString.append(messageType);
                    if (messageTypeString.toString().equals(messageBuilder.getClass().getSimpleName())){
                        TypeOfMessage typeOfMessage = TypeOfMessage.UNDEFINED; 
                        ArrayList<ParameterSet> dataElements = new ArrayList<ParameterSet>();
                        StringBuffer VIN = new StringBuffer();
                        StringBuffer password = new StringBuffer();
                        StringBuffer taskID = new StringBuffer();
                        ArrayList<Request> re = new ArrayList<Request>();
                        // extract data from message
                        typeOfMessage = messageBuilder.readMessage(messageString, dataElements, re, VIN, password, taskID);
                        // run authentification
                        SessionType st = authenticateVIN.authenticateAccess(VIN.toString(),password.toString());
                        if ((typeOfMessage == TypeOfMessage.STARTSESSION) && st != SessionType.UNKNOWN){
                            // set vin and signal transmit side
                            myVIN = VIN.toString();
                            sessionType = st;
                            lockWaitForVIN.lock();
                            try {
                                awaitVIN.signal();
                            } finally {
                                lockWaitForVIN.unlock();
                            }
                            newSession();
                        } else {
                            // VIN / username not correct close streams
                            in.close();
                            out.close();
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            /**
             * Read messages incoming if authenitfication was successful.
             */
            while(socketAvailable){
                messageType = new ByteArrayOutputStream();
                message = new ByteArrayOutputStream();
                try {
                    // read message
                    readMessage.readMessage(message, messageType, in);
                    // decrypt message
                    if (dec!=null){
                        messageType = dec.decode(messageType);
                        message = dec.decode(message);
                    }
                    StringBuffer messageString = new StringBuffer();
                    messageString.append(message);
                    StringBuffer messageTypeString = new StringBuffer();
                    messageTypeString.append(messageType);
                    if (messageTypeString.toString().equals(messageBuilder.getClass().getSimpleName())){
                        TypeOfMessage typeOfMessage = TypeOfMessage.END; 
                        ArrayList<ParameterSet> dataElements = new ArrayList<ParameterSet>();
                        StringBuffer VIN = new StringBuffer();
                        StringBuffer password = new StringBuffer();
                        StringBuffer taskID = new StringBuffer();
                        ArrayList<Request> re = new ArrayList<Request>();
                        // Read serailized message
                        typeOfMessage = messageBuilder.readMessage(messageString, dataElements, re, VIN, password, taskID);
                        switch (typeOfMessage){
                            // call private functions to deal with message es
                            case START:
                                addReceiveTask(VIN.toString(),taskID.toString());
                                break;
                            case TRANSMIT:
                                dataReceived(VIN.toString(),taskID.toString(), dataElements);
                                break;
                            case REQUEST:
                                for (Request request:re){
                                    requestReceived.requestReceived(request);
                                }
                                break;
                            case END:
                                removeReceiveTask(VIN.toString(),taskID.toString());
                                break;
                        }
                        
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
                    System.err.println("Socket closed, Socket available set false");
                    /**
                     * IOException will be thrown if socket is not available anymore
                     * it will set socketAvailable == false and signal transmit 
                     * side so that the thread can quit its operation as well.
                     * 
                     * Receive side always crahses first. It is always working 
                     * on the connection sender side might be waiting for data 
                     * and does therefor not realize that the session has 
                     * crashed.
                     *  
                     * reading data or waiting for data.
                     * 
                     */
                    socketAvailable = false;
                    lockTransmitList.lock();
                    try {
                        dataToTransmit.signal();
                    } finally {
                        lockTransmitList.unlock();
                    }
                    
                }
            }
        }
        
    };
    /**
     * Method calles when session is started, used to get tasks from bufffer.
     */
    private void newSession(){
        newSessionGetTasks.newSessionGetTasks(this);
    }
    
    /**
     * Starts a new receive task
     * 
     * @param VIN           VIN or username of vehicle or client
     * @param taskID        taskID
     */
    private void addReceiveTask(String VIN, String taskID){
        Task ta = newReceiveTask.newReceiveTask(VIN, taskID);
        lockReceiveList.lock();
        try {
            receiveTasks.add(ta);
        } finally {
            lockReceiveList.unlock();
        }
    }
    
    /**
     * Data received adds data to receivetask in List. 
     * 
     * @param VIN           VIN or username of vehicle or client
     * @param taskID        taskID
     * @param dataElements  Data
     */
    private void dataReceived(String VIN, String taskID, ArrayList<ParameterSet> dataElements){
        lockReceiveList.lock();
        try {
            for (Task ta:receiveTasks){
                if (ta.getTaskID().equals(taskID)){
                    for (ParameterSet da:dataElements){
                        ta.addDataElement(da);
                    }
                    break;
                }
            }
        } finally {
            lockReceiveList.unlock();
        }
    }
    
    /**
     * removes receive task from own list
     * 
     * @param VIN           VIN or username of vehicle or client
     * @param taskID        taskID
     */
    private void removeReceiveTask(String VIN, String taskID){
        for (Iterator<Task> i = receiveTasks.iterator(); i.hasNext(); ){
            Task ta = i.next();
            if (ta.getTaskID().equals(taskID)){
                ta.endTask();
                lockReceiveList.lock();
                try {
                    receiveTasks.remove(ta);
                } finally {
                    lockReceiveList.unlock();
                }
                break;
            }
        }
    }
    
    /**
     * Builds a start session message and transmits it on the output stream
     */
    private void sendStartSessionMessage(){
        StringBuffer VIN = new StringBuffer();
        StringBuffer sbPassword = new StringBuffer();
        StringBuffer messageString = new StringBuffer();
        StringBuffer messageTypeString = new StringBuffer();
        if (this.password != null) sbPassword.append(this.password);
        VIN.append(myVIN);
        messageString = messageBuilder.buildStartSessionMessage(VIN, sbPassword);
        messageTypeString.append(messageBuilder.getClass().getSimpleName());
        ByteArrayOutputStream message = new ByteArrayOutputStream();
        ByteArrayOutputStream messageType = new ByteArrayOutputStream();
        try {
            message.write(messageString.toString().getBytes());
            messageType.write(messageTypeString.toString().getBytes());
            if (enc!=null) {
                message = enc.encode(message);
                messageType = enc.encode(messageType);
            }
            writeMessage.writeMessage(message, messageType, out);
        } catch (IOException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Builds a start task message and transmits it on the output stream
     */
    private void sendStartTaskMessage(String taID){
        StringBuffer VIN = new StringBuffer();
        StringBuffer taskID = new StringBuffer();
        StringBuffer messageString = new StringBuffer();
        StringBuffer messageTypeString = new StringBuffer();
        VIN.append(myVIN);
        taskID.append(taID);
        messageString.append(messageBuilder.buildStartTaskMessage(VIN, taskID));
        messageTypeString.append(messageBuilder.getClass().getSimpleName());
        ByteArrayOutputStream message = new ByteArrayOutputStream();
        ByteArrayOutputStream messageType = new ByteArrayOutputStream();
        try {
            message.write(messageString.toString().getBytes());
            messageType.write(messageTypeString.toString().getBytes());
            if (enc!=null) {
                message = enc.encode(message);
                messageType = enc.encode(messageType);
            }
            writeMessage.writeMessage(message, messageType, out);
        } catch (IOException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Socket closed, Socket available set false");
            socketAvailable = false;
        }
    }
    
    /**
     * Builds a end task message and transmits it on the output stream
     */
    private void sendEndTaskMessage(String taID){
        StringBuffer VIN = new StringBuffer();
        StringBuffer taskID = new StringBuffer();
        StringBuffer messageString = new StringBuffer();
        StringBuffer messageTypeString = new StringBuffer();
        VIN.append(myVIN);
        taskID.append(taID);
        messageString.append(messageBuilder.buildEndTaskMessage(VIN, taskID));
        messageTypeString.append(messageBuilder.getClass().getSimpleName());
        ByteArrayOutputStream message = new ByteArrayOutputStream();
        ByteArrayOutputStream messageType = new ByteArrayOutputStream();
        try {
            message.write(messageString.toString().getBytes());
            messageType.write(messageTypeString.toString().getBytes());
            if (enc!=null) {
                message = enc.encode(message);
                messageType = enc.encode(messageType);
            }
            writeMessage.writeMessage(message, messageType, out);
        } catch (IOException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Socket closed, Socket available set false");
            socketAvailable = false;
            
        }
    }
    /**
     * Builds a transmit data message and puts it on the output stream
     */
    private void sendTransmitDataMessage(String taID, ArrayList<ParameterSet> dA){
        StringBuffer VIN = new StringBuffer();
        StringBuffer taskID = new StringBuffer();
        StringBuffer messageString = new StringBuffer();
        StringBuffer messageTypeString = new StringBuffer();
        
        VIN.append(myVIN);
        taskID.append(taID);
        messageString.append(messageBuilder.buildTransmitTaskMessage(VIN, taskID, dA));
        messageTypeString.append(messageBuilder.getClass().getSimpleName());
        ByteArrayOutputStream message = new ByteArrayOutputStream();
        ByteArrayOutputStream messageType = new ByteArrayOutputStream();
        try {
            message.write(messageString.toString().getBytes());
            messageType.write(messageTypeString.toString().getBytes());
            if (enc!=null) {
                message = enc.encode(message);
                messageType = enc.encode(messageType);
            }
            writeMessage.writeMessage(message, messageType, out);
        } catch (IOException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Socket closed, Socket available set false");
            socketAvailable = false;
        }
    }
    
    /**
     * Builds a request message and transmits it on the output stream
     */
    private void sendRequestMessage(Request re){
        StringBuffer VIN = new StringBuffer();
        StringBuffer taskID = new StringBuffer();
        StringBuffer messageString = new StringBuffer();
        StringBuffer messageTypeString = new StringBuffer();
        VIN.append(myVIN);
        taskID.append(re.getRequestID());
        messageString.append(messageBuilder.buildRequestMessage(VIN, taskID, re));
        messageTypeString.append(messageBuilder.getClass().getSimpleName());
        ByteArrayOutputStream message = new ByteArrayOutputStream();
        ByteArrayOutputStream messageType = new ByteArrayOutputStream();
        try {
            message.write(messageString.toString().getBytes());
            messageType.write(messageTypeString.toString().getBytes());
            if (enc!=null) {
                message = enc.encode(message);
                messageType = enc.encode(messageType);
            }
            writeMessage.writeMessage(message, messageType, out);
        } catch (IOException ex) {
            Logger.getLogger(Session.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Socket closed, Socket available set false");
            socketAvailable = false;
            
        }
    }
    
    /**
     * Private method called to identify that data has been available.
     * 
     * @return  boolean true if no data available
     */
    private boolean noData(){
        boolean noData = true;
        for (Task ta:transmitTasks){
            noData = noData && (ta.size()==0) && (ta.getTaskState() == TaskState.TRANSMIT);
        }
        noData = noData && receiveTasks.isEmpty();
        return noData;
    }

    /**
     * Returns String holding the VIN of the vehicle
     * 
     * @return 
     */
    @Override
    public String getVIN() {
        return this.myVIN;
    }
    
    /**
     * Mehtod to add transmit tasks to the list of tasks to transmit.
     * It is started called when the session is started.
     *  
     * @param taTr      ArrayList of Tasks to Transmit
     */
    @Override
    public void addTransmitTasks(ArrayList<Task> taTr) {
       lockTransmitList.lock();
        try {
            for (Task ta:taTr){
                ta.setDataAdded(this);
            }
            transmitTasks.addAll(taTr);
            dataToTransmit.signal();
        } finally {
            lockTransmitList.unlock();
        }
    }
    
    /**
     * Mehtod to add receive task to the list of tasks to transmit.
     * It is started called when the session is started.
     * 
     * @param taRe      ArrayList of Tasks to Receive
     */
    @Override
    public void addReceiveTasks(ArrayList<Task> taRe) {
        lockReceiveList.lock();
        try {
            receiveTasks.addAll(taRe);
        } finally {
            lockReceiveList.unlock();
        }
    }
    
    /**
     * Add a Transmit task.
     * 
     * @param ta        Task to be transmitted
     */
    @Override
    public void addTransmitTask(Task ta) {
        lockTransmitList.lock();
        try {
            ta.setDataAdded(this);
            transmitTasks.add(ta);
            dataToTransmit.signal();
        } finally {
            lockTransmitList.unlock();
        }
    }

    /**
     * Data added is an Interface called by Task if new data was added to it
     * it is used to restart transmission.
     */
    @Override
    public void dataAdded() {
        lockTransmitList.lock();
        try {
            dataToTransmit.signal();
        } finally {
            lockTransmitList.unlock();
        }
    }
    
    /**
     * Method to add a new request which needs to be added
     * 
     * @param request   Request to be added
     */
    @Override
    public void addRequest(Request request) {
        lockTransmitList.lock();
        try {
            transmitRequest.add(request);
            dataToTransmit.signal();
        } finally {
            lockTransmitList.unlock();
        }
    }
    
    /**
     * Method to add buffered request, called when starting the session.
     * 
     * @param req       ArrayList of requests to be added
     */
    @Override
    public void addRequests(ArrayList<Request> req) {
        lockTransmitList.lock();
        try {
            transmitRequest.addAll(req);
            dataToTransmit.signal();
        } finally {
            lockTransmitList.unlock();
        }
    }
    
    /**
     * Called authentification was successful. Gets requests and Tasks from 
     * buffers.
     */
    private void getTasksAndRequests(){
        this.newSessionGetRequests.newSessionGetRequests(this);
        this.newSessionGetTasks.newSessionGetTasks(this);
    }
            
}
