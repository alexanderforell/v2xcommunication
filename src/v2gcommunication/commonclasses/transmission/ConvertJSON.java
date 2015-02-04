/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.transmission;

import java.io.StringReader;
import java.util.ArrayList;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import v2gcommunication.commonclasses.requests.Request;
import v2gcommunication.commonclasses.requests.RequestType;
import v2gcommunication.commonclasses.tasks.ParameterSet;
import v2gcommunication.commonclasses.tasks.TypeOfMessage;

/**
 * Class implements interface {@code MessageBuilder} and uses JSON to transfer 
 * the data between client and server or vehicle and server. 
 * 
 * @author Alexander Forell
 */
public class ConvertJSON implements MessageBuilder{
    
    /**
     * Builds a start session message for a vehicle or a user
     * 
     * @param VIN       username or VIN 
     * @param password  password of the user
     * @return          StringBuffer message
     */
    @Override
    public StringBuffer buildStartSessionMessage(StringBuffer VIN, StringBuffer password){
        StringBuffer msg = new StringBuffer();
        JsonObjectBuilder objBuilder = Json.createObjectBuilder();
        objBuilder.add("functionName", "startSession");
        objBuilder.add("VIN", VIN.toString());
        objBuilder.add("password", password.toString());
        JsonObject message = objBuilder.build();
        msg.append(message.toString());
        return msg;
    }
    
    /**
     * Builds a request message for a client
     * 
     * @param VIN       username or VIN
     * @param taskId    ID of the request
     * @param re        Request to be transmitted.
     * @return          StringBuffer message
     */
    @Override
    public StringBuffer buildRequestMessage(StringBuffer VIN, StringBuffer taskId, Request re) {
        StringBuffer msg = new StringBuffer();
        JsonObjectBuilder objBuilder = Json.createObjectBuilder();
        objBuilder.add("functionName", "Request");
        objBuilder.add("VIN", re.getUserNameVIN());
        objBuilder.add("taskID", re.getRequestID());
        objBuilder.add("function", re.getFunction());
        objBuilder.add("requestType", re.getRequestType().toString());
        JsonArrayBuilder paramenterName = Json.createArrayBuilder();
        JsonArrayBuilder parameterValue = Json.createArrayBuilder();
        JsonArrayBuilder parameterType = Json.createArrayBuilder();
        JsonArrayBuilder timeStamps = Json.createArrayBuilder();
        for (ParameterSet data:re.getParameterSet()){
            paramenterName.add(data.parameterName);
            parameterValue.add(data.parameterValue);
            parameterType.add(data.parameterType);
            timeStamps.add(String.valueOf(data.timeStamp));
        }
        objBuilder.add("parameterName", paramenterName);
        objBuilder.add("parameterValue", parameterValue);
        objBuilder.add("parameterType", parameterType);
        objBuilder.add("timeStamps", timeStamps);
        JsonObject message = objBuilder.build();
        msg.append(message.toString());
        return msg;
    }
    
    /**
     * Build a start Task message. 
     * 
     * @param VIN       vin or username of sender
     * @param taskId    Task ID
     * @return          StringBuffer message
     */
    @Override
    public StringBuffer buildStartTaskMessage(StringBuffer VIN, StringBuffer taskId){
        StringBuffer msg = new StringBuffer();
        JsonObjectBuilder objBuilder = Json.createObjectBuilder();
        objBuilder.add("functionName", "startTask");
        objBuilder.add("VIN", VIN.toString());
        objBuilder.add("taskID", taskId.toString());
        JsonObject message = objBuilder.build();
        msg.append(message.toString());
        return msg;
    }
    
    /**
     * Build a message to end a Task
     * 
     * @param VIN       vin or username of sender
     * @param taskId    Task ID
     * @return          StringBuffer message
     */ 
    @Override
    public StringBuffer buildEndTaskMessage(StringBuffer VIN, StringBuffer taskId){
        StringBuffer msg = new StringBuffer();
        JsonObjectBuilder objBuilder = Json.createObjectBuilder();
        objBuilder.add("functionName", "endTask");
        objBuilder.add("VIN", VIN.toString());
        objBuilder.add("taskID", taskId.toString());
        JsonObject message = objBuilder.build();
        msg.append(message.toString());
        return msg;
    }
    /**
     * Builds a Transmit Message for a Task
     * 
     * @param VIN               vin or username of sender
     * @param taskId            Task ID
     * @param dataElements      Data Elements on the Task 
     * @return                  StringBuffer message
     */
    @Override
    public StringBuffer buildTransmitTaskMessage(StringBuffer VIN, StringBuffer taskId, ArrayList<ParameterSet> dataElements){
        StringBuffer msg = new StringBuffer();
        JsonObjectBuilder objBuilder = Json.createObjectBuilder();
        objBuilder.add("functionName", "transmitTask");
        objBuilder.add("VIN", VIN.toString());
        objBuilder.add("taskID", taskId.toString());
        JsonArrayBuilder paramenterName = Json.createArrayBuilder();
        JsonArrayBuilder parameterValue = Json.createArrayBuilder();
        JsonArrayBuilder parameterType = Json.createArrayBuilder();
        JsonArrayBuilder timeStamps = Json.createArrayBuilder();
        for (ParameterSet data:dataElements){
            paramenterName.add(data.parameterName);
            parameterValue.add(data.parameterValue);
            if (data.parameterType != null) parameterType.add(data.parameterType);
            else parameterType.add("");
            timeStamps.add(data.timeStamp);
        }
        objBuilder.add("parameterName", paramenterName);
        objBuilder.add("parameterValue", parameterValue);
        objBuilder.add("parameterType", parameterType);
        objBuilder.add("timeStamps", timeStamps);
        JsonObject message = objBuilder.build();
        msg.append(message.toString());
        return msg;
    }
    
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
    @Override
    public TypeOfMessage readMessage(StringBuffer message,
            ArrayList<ParameterSet> dataElements, 
            ArrayList<Request> re, StringBuffer VIN, StringBuffer password, StringBuffer taskID){
        JsonReader jsonReader = Json.createReader(new StringReader(message.toString()));
        JsonObject jsonData = jsonReader.readObject();
        JsonArray parameterName;
        JsonArray parameterValue;
        JsonArray parameterType;
        JsonArray timeStamps;
        RequestType requestType;
        String function;
        int i;
        switch (jsonData.getString("functionName")) {
            case "startSession":
                VIN.append(jsonData.getString("VIN"));
                password.append(jsonData.getString("password"));
                return TypeOfMessage.STARTSESSION;
            
            case "startTask":
                VIN.append(jsonData.getString("VIN"));
                taskID.append(jsonData.getString("taskID"));
                return TypeOfMessage.START;
                
            case "transmitTask":
                
                VIN.append(jsonData.getString("VIN"));
                taskID.append(jsonData.getString("taskID"));
                parameterName = jsonData.getJsonArray("parameterName");
                parameterValue = jsonData.getJsonArray("parameterValue");
                parameterType = jsonData.getJsonArray("parameterType");
                timeStamps = jsonData.getJsonArray("timeStamps");
                
                for (i=0; i<parameterType.size(); i++){
                    ParameterSet dataElement = new ParameterSet();
                    dataElement.parameterName = parameterName.getString(i);
                    dataElement.parameterValue = parameterValue.getString(i);
                    dataElement.parameterType = parameterType.getString(i);
                    dataElement.timeStamp = timeStamps.getString(i);
                    dataElements.add(dataElement);
                }
                return TypeOfMessage.TRANSMIT;
                
            case "endTask":
                VIN.append(jsonData.getString("VIN"));
                taskID.append(jsonData.getString("taskID"));
                return TypeOfMessage.END;
                
            case "Request":
                VIN.append(jsonData.getString("VIN"));
                taskID.append(jsonData.getString("taskID"));
                requestType = RequestType.valueOf(jsonData.getString("requestType"));
                function = jsonData.getString("function");
                
                
                parameterName = jsonData.getJsonArray("parameterName");
                parameterValue = jsonData.getJsonArray("parameterValue");
                parameterType = jsonData.getJsonArray("parameterType");
                timeStamps = jsonData.getJsonArray("timeStamps");
                
                
                ArrayList<ParameterSet> parameters = new ArrayList<ParameterSet>();
                for (i=0; i<parameterType.size(); i++){
                    ParameterSet parameterSet = new ParameterSet();
                    parameterSet.parameterName = parameterName.getString(i);
                    parameterSet.parameterValue = parameterValue.getString(i);
                    parameterSet.parameterType = parameterType.getString(i);
                    parameterSet.timeStamp = String.valueOf(timeStamps.getString(i));
                    parameters.add(parameterSet);
                }
                Request request = new Request(requestType, VIN.toString(), 
                        taskID.toString(), function, parameters);
                re.add(request);
                return TypeOfMessage.REQUEST;
            default:
                return TypeOfMessage.UNDEFINED;
            
        }
        
    }

    
    
    
}
