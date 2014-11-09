/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gCommunication.client;
import java.util.UUID;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import v2gCommunication.client.interfaces.DataReceived;

/**
 *
 * @author alexander
 */
public class ClientMethods implements DataReceived{
    
    static ServerConnection conn;
    
    public ClientMethods(int port){
        this.conn = new ServerConnection(port, this);
        this.conn.start();
    }
    
    @Override
    public void dataReceived(JsonObject jsonObject) {
        switch (jsonObject.getString("functionName")) {
            case "logon":
                break;
            case "createUser":
                break;
            case "changePassword":
                break;
            case "deleteUser":
                break;
            case "logoff":
                break;
            case "getUserRights":
                break;
        }
    }
    
    
    public static void logon (String userName, String passWord){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder parameters = Json.createArrayBuilder();
        message.add("functionName", "logon");
        message.add("Request-ID", reQuestID.toString());
        parameters.add(userName).add(passWord);       
        message.add("Parameters",parameters);
        transmit = message.build();
        conn.transmitData(transmit.toString().getBytes());
        
    }
    public static void createUser(String userName, String password){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        message.add("functionName", "createUser");
        message.add("requestId", reQuestID.toString());
        message.add("userName", userName);
        message.add("password", password);
        transmit = message.build();
        conn.transmitData(transmit.toString().getBytes());
    }
    public static void changePassword(String userName, String passwordOld, String passwordNew){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        message.add("functionName", "changePassword");
        message.add("requestId", reQuestID.toString());
        message.add("userName", userName);
        message.add("passwordOld", passwordOld);
        message.add("passwordNew", passwordNew);
        transmit = message.build();
        conn.transmitData(transmit.toString().getBytes());
    }
    public static void deleteUser(String userName){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        message.add("functionName", "deleteUser");
        message.add("requestId", reQuestID.toString());
        message.add("userName", userName);
        transmit = message.build();
        conn.transmitData(transmit.toString().getBytes());
    }
    public static void logoff (String userName){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        message.add("functionName", "logoff");
        message.add("requestId", reQuestID.toString());
        message.add("userName", userName);
        transmit = message.build();
        conn.transmitData(transmit.toString().getBytes());
    }
    public static void getUsers(){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        message.add("functionName", "getUsers");
        message.add("requestId", reQuestID.toString());
        transmit = message.build();
        conn.transmitData(transmit.toString().getBytes());
    }
    public static void getUserRights(String userName){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        message.add("functionName", "getUserRights");
        message.add("requestId", reQuestID.toString());
        message.add("userName", userName);
        transmit = message.build();
        conn.transmitData(transmit.toString().getBytes());
    }
    public static void addVehicle(String vin){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder parameters = Json.createArrayBuilder();
        message.add("functionName", "addVehicle");
        message.add("Request-ID", reQuestID.toString());
        parameters.add(vin);       
        message.add("Parameters",parameters);
        transmit = message.build();
        conn.transmitData(transmit.toString().getBytes());
    }

    public static void deleteVehicle(String vin){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder parameters = Json.createArrayBuilder();
        message.add("functionName", "addVehicle");
        message.add("Request-ID", reQuestID.toString());
        parameters.add(vin);       
        message.add("Parameters",parameters);
        transmit = message.build();
        conn.transmitData(transmit.toString().getBytes());
    }
    static public void getVehicles(){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder parameters = Json.createArrayBuilder();
        message.add("functionName", "getVehicles");
        message.add("Request-ID", reQuestID.toString());
        parameters.add("");       
        message.add("Parameters",parameters);
        transmit = message.build();
        conn.transmitData(transmit.toString().getBytes());
    }

    static public void getActiveVehicles(){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder parameters = Json.createArrayBuilder();
        message.add("functionName", "getActiveVehicles");
        message.add("Request-ID", reQuestID.toString());
        parameters.add("");       
        message.add("Parameters",parameters);
        transmit = message.build();
        conn.transmitData(transmit.toString().getBytes());
    }
    static public void readData(){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder parameters = Json.createArrayBuilder();
        message.add("functionName", "readData");
        message.add("Request-ID", reQuestID.toString());
        parameters.add("");       
        message.add("Parameters",parameters);
        transmit = message.build();
        conn.transmitData(transmit.toString().getBytes());
    }
    static public void readData(String vin){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder parameters = Json.createArrayBuilder();
        message.add("functionName", "readData");
        message.add("Request-ID", reQuestID.toString());
        parameters.add("vin");       
        message.add("Parameters",parameters);
        transmit = message.build();
        conn.transmitData(transmit.toString().getBytes());
    }
    static public void getSessions(){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder parameters = Json.createArrayBuilder();
        message.add("functionName", "getSessions");
        message.add("Request-ID", reQuestID.toString());
        parameters.add("");       
        message.add("Parameters",parameters);
        transmit = message.build();
        conn.transmitData(transmit.toString().getBytes());
    }
    static public void getSessions(String vin){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder parameters = Json.createArrayBuilder();
        message.add("functionName", "getSessions");
        message.add("Request-ID", reQuestID.toString());
        parameters.add("vin");       
        message.add("Parameters",parameters);
        transmit = message.build();
        conn.transmitData(transmit.toString().getBytes());
    }
    static public void requestData(String[] request, String vin){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder parameters = Json.createArrayBuilder();
        message.add("functionName", "getSessions");
        message.add("Request-ID", reQuestID.toString());
        parameters.add("vin");       
        message.add("Parameters",parameters);
        transmit = message.build();
        conn.transmitData(transmit.toString().getBytes());
    }
    static public void requestData(String[] request){
        
    }
    static public void schduleDataRequest(String[] request, String vin, Long intervall){
        
    }
    static public void schduleDataRequest(String[] request, Long intervall){
        
    }

    
   
    
}
