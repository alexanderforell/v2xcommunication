/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.server;
import javax.json.JsonValue;
import javax.json.JsonArray;
import javax.json.JsonObject;
import v2gcommunication.server.interfaces.VehicleDataReceived;
import v2gcommunication.server.interfaces.ClientRequestReceived;
/**
 *
 * @author alexander
 */
public class EventEvaluation implements VehicleDataReceived, ClientRequestReceived {
    ManageVehicleConnections manageVehicleConnections;
    ManageUserConnections manageUserConnections;
    DatabaseConnection conn;
    
    EventEvaluation(ManageVehicleConnections manageVehicleConnections, ManageUserConnections manageUserConnections, DatabaseConnection conn){
        this.manageUserConnections =  manageUserConnections;
        this.manageVehicleConnections = manageVehicleConnections;
        this.conn = conn;
        
    }
    
    @Override public void vehicleDataReceived(String fin, JsonObject jsonObject){
        switch (jsonObject.getString("Function-Name")) {
            case "transmitData":
                dataReceived( fin,  jsonObject);
        }
            
    }
    @Override public void requestReceived(String sessionID, JsonObject jsonObject){
        switch (jsonObject.getString("functionName")) {
            case "transmitData":
                
                break;
            case "logon":
                
                break;
            case "createUser":
                {String userName;
                String password;
                userName = jsonObject.getString("userName");
                password = jsonObject.getString("password");
                conn.createUser(userName, password);}
                break;
            case "changePassword":
                
                break;
            case "deleteUser":
                {String userName;
                userName = jsonObject.getString("userName");
                
                conn.deleteUser(userName);}
                break;
            case "logoff":
                
                break;
            case "getUserRights":
                
                break;
            case "addVehicle":
                
                break;
            case "deleteVehicle":
                
                break;
            case "getVehicles":
                
                break;
            case "getActiveVehicles":
                
                break;
            case "readData":
                
                break;
            case "getSessions":
                
                break;
            case "requestData":
                
                break;
            case "schduleDataRequest":
                
                break;
            default:
                break;
            
        }
    }
    
    private void dataReceived(String fin, JsonObject jsonObject){
        JsonArray dataPoints = jsonObject.getJsonArray("Data-Points");
        JsonArray dataValues = jsonObject.getJsonArray("Data-Values");
        System.out.println("Test");
        for (JsonValue dataPoint:dataPoints){
            System.out.println(dataPoint);
            
        }
        for (JsonValue dataValue:dataValues){
            System.out.println(dataValue);
            
        }
    }
}
