/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.requests;

import java.util.ArrayList;
import java.util.UUID;
import v2gcommunication.commonclasses.tasks.ParameterSet;

/**
 * Class Request is used to call functions / exchange results between server 
 * and client as well as client and vehicle.
 * 
 * @author Alexander Forell
 */
public class Request {
    private final RequestType requestType;
    private final String userNameVIN;
    private final String requestID;
    private final String function;
    private final ArrayList<ParameterSet> parameterSet;
    
    /**
     * Constructor used to create Request w/ random ID.
     * RequestType always REQUEST.
     * 
     * @param userNameVIN       username or VIN of sender
     * @param function          function to be called on oppsite side
     * @param parameterSet      parameterset for function.
     */
    public Request(String userNameVIN, String function, ArrayList<ParameterSet> parameterSet){
        UUID sID = UUID.randomUUID();
        this.requestType = RequestType.REQUEST;
        this.userNameVIN = userNameVIN;
        this.requestID = sID.toString();
        this.function = function;
        this.parameterSet = new ArrayList<ParameterSet>();
        this.parameterSet.addAll(parameterSet);
    }
    
    /**
     * Constructor used to create Request w/ specific ID.
     * RequestType can be chosen btw. REQUEST and ANSWER.
     * 
     * @param requestType           ANSWER or REQUEST
     * @param userNameVIN           username or VIN of sender/receiver
     * @param ID                    requestID
     * @param function              function to be called on oppsite side
     * @param parameterSet          parameterset for function.
     */
    public Request(RequestType requestType, String userNameVIN, String ID, String function, ArrayList<ParameterSet> parameterSet){
        this.requestType = requestType;
        this.userNameVIN = userNameVIN;
        this.requestID = ID;
        this.function = function;
        this.parameterSet = new ArrayList<ParameterSet>();
        this.parameterSet.addAll(parameterSet);
    }

    
    public RequestType getRequestType(){
        return this.requestType;
    }
    
    public String getUserNameVIN(){
         return this.userNameVIN;
    }
    
    public String getRequestID(){
         return this.requestID;
    }
    
    public String getFunction(){
         return this.function;
    }
    
    public ArrayList<ParameterSet> getParameterSet(){
        return this.parameterSet;
    }
    
    
}
