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

/**
 *
 * @author alexander
 */
public class ClientMethods{
    
    
    static public void logon (ServerConnection conn,String userName, String passWord){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder parameters = Json.createArrayBuilder();
        message.add("Function-Name", "logon");
        message.add("Request-ID", reQuestID.toString());
        parameters.add(userName).add(passWord);       
        message.add("Parameters",parameters);
        transmit = message.build();
    }
    static public void createUser(ServerConnection conn,String userName, String password){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        message.add("functionName", "createUser");
        message.add("requestId", reQuestID.toString());
        message.add("userName", userName);
        message.add("password", password);
        transmit = message.build();
        conn.transmitData(transmit);
        
    }
    static public void changePassword(ServerConnection conn, String userName, String passwordOld, String passwordNew){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        message.add("functionName", "createUser");
        message.add("requestId", reQuestID.toString());
        message.add("userName", userName);
        message.add("passwordOld", passwordOld);
        message.add("passwordNew", passwordNew);
        transmit = message.build();
        conn.transmitData(transmit);
    }
    static public void deleteUser(ServerConnection conn,String userName){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        message.add("functionName", "deleteUser");
        message.add("requestId", reQuestID.toString());
        message.add("userName", userName);
        transmit = message.build();
        conn.transmitData(transmit);
    }
    static public void logoff (ServerConnection conn, String userName){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        message.add("functionName", "createUser");
        message.add("requestId", reQuestID.toString());
        message.add("userName", userName);
        transmit = message.build();
        conn.transmitData(transmit);
    }
    static public void getUserRights(ServerConnection conn, String userName){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        message.add("functionName", "createUser");
        message.add("requestId", reQuestID.toString());
        message.add("userName", userName);
        transmit = message.build();
        conn.transmitData(transmit);
    }
    static public void addVehicle(ServerConnection conn, String vin){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder parameters = Json.createArrayBuilder();
        message.add("Function-Name", "addVehicle");
        message.add("Request-ID", reQuestID.toString());
        parameters.add(vin);       
        message.add("Parameters",parameters);
        transmit = message.build();
    }

    static public void deleteVehicle(ServerConnection conn, String vin){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder parameters = Json.createArrayBuilder();
        message.add("Function-Name", "addVehicle");
        message.add("Request-ID", reQuestID.toString());
        parameters.add(vin);       
        message.add("Parameters",parameters);
        transmit = message.build();
    }
    static public void getVehicles(ServerConnection conn){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder parameters = Json.createArrayBuilder();
        message.add("Function-Name", "getVehicles");
        message.add("Request-ID", reQuestID.toString());
        parameters.add("");       
        message.add("Parameters",parameters);
        transmit = message.build();
    }

    static public void getActiveVehicles(ServerConnection conn){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder parameters = Json.createArrayBuilder();
        message.add("Function-Name", "getActiveVehicles");
        message.add("Request-ID", reQuestID.toString());
        parameters.add("");       
        message.add("Parameters",parameters);
        transmit = message.build();
            
    }
    static public void readData(ServerConnection conn){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder parameters = Json.createArrayBuilder();
        message.add("Function-Name", "readData");
        message.add("Request-ID", reQuestID.toString());
        parameters.add("");       
        message.add("Parameters",parameters);
        transmit = message.build();

    }
    static public void readData(ServerConnection conn, String vin){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder parameters = Json.createArrayBuilder();
        message.add("Function-Name", "readData");
        message.add("Request-ID", reQuestID.toString());
        parameters.add("vin");       
        message.add("Parameters",parameters);
        transmit = message.build();
    }
    static public void getSessions(ServerConnection conn){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder parameters = Json.createArrayBuilder();
        message.add("Function-Name", "getSessions");
        message.add("Request-ID", reQuestID.toString());
        parameters.add("");       
        message.add("Parameters",parameters);
        transmit = message.build();
    }
    static public void getSessions(ServerConnection conn, String vin){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder parameters = Json.createArrayBuilder();
        message.add("Function-Name", "getSessions");
        message.add("Request-ID", reQuestID.toString());
        parameters.add("vin");       
        message.add("Parameters",parameters);
        transmit = message.build();
    }
    static public void requestData(ServerConnection conn, String[] request, String vin){
        UUID reQuestID = UUID.randomUUID();
        JsonObject transmit;
        JsonObjectBuilder message = Json.createObjectBuilder();
        JsonArrayBuilder parameters = Json.createArrayBuilder();
        message.add("Function-Name", "getSessions");
        message.add("Request-ID", reQuestID.toString());
        parameters.add("vin");       
        message.add("Parameters",parameters);
        transmit = message.build();
    }
    static public void requestData(ServerConnection conn, String[] request){
        
    }
    static public void schduleDataRequest(ServerConnection conn, String[] request, String vin, Long intervall){
        
    }
    static public void schduleDataRequest(ServerConnection conn, String[] request, Long intervall){
        
    }
   
    
}
