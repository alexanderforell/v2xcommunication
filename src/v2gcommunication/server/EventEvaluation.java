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
 * The {@code EventEvaluation} class evaluates thrown by 
 * {@code ManageVehicleConnections} and  {@code ManageUserConnections}. It
 * implemtents the interface {@code VehicleDataReceived} and 
 * {@code ClientRequestReceived} in order to do so.
 * 
 * <p> The class holds fields for the classes  {@code ManageVehicleConnections},
 * {@code ManageUserConnections} and {@code DatabaseConnection}
 * 
 * @see VehicleDataReceived
 * @see ClientRequestReceived
 * @see DatabaseConnection
 * @see ManageVehicleConnections
 * @see ManageUserConnections
 * @see DatabaseConnection
 * @author Alexander Forell
 */
public class EventEvaluation implements VehicleDataReceived, ClientRequestReceived {
    /**
    * Field which holds the ManageVehicleConnections object
    */
    ManageVehicleConnections manageVehicleConnections;
    /**
    * Field which holds the ManageUserConnections object
    */
    ManageUserConnections manageUserConnections;
    /**
    * Field which holds the DatabaseConnection object
    */
    DatabaseConnection conn;
    
    
    /**
    * Constructor initializeses the fields {@code manageVehicleConnections},
    * {@code manageUserConnections} and {@code conn}.
    * 
    * @param manageVehicleConnections   {@code ManageVehicleConnections} Object 
    * which holds all active vehicle connections
    * @param manageUserConnections      {@code ManageVehicleConnections} Object
    * which holds all active client/user connections 
    * @param conn                       {@code DatabaseConnection} Object which 
    * holds the database connection and wrapper methods for database access 
    */
    EventEvaluation(ManageVehicleConnections manageVehicleConnections, ManageUserConnections manageUserConnections, DatabaseConnection conn){
        this.manageUserConnections =  manageUserConnections;
        this.manageVehicleConnections = manageVehicleConnections;
        this.conn = conn;
        
    }
    
    /**
    * Overrides {@code vehicleDataReceived} of {@code VehicleDataReceived} 
    * interface.
    * 
    * It evaluates the Function-Name field in the JsonObject and calls the 
    * respective methods to process the information.
    * @param vin        a string holding the vin of the vehicle which send the 
    * jsonObject
    * @param jsonObject a JsonObject with the message
    */
    @Override public void vehicleDataReceived(String vin, JsonObject jsonObject){
        switch (jsonObject.getString("Function-Name")) {
            case "transmitData":
                dataReceived(vin,  jsonObject);
        }
            
    }
    /**
    * Overrides {@code requestReceived} of {@code RequestReceived} 
    * interface.
    * 
    * It evaluates the Function-Name field in the JsonObject and calls the 
    * respective methods to process the information.
    * @param sessionID      a string holding the UUID of the user session 
    * @param jsonObject     a JsonObject with request transmitted by the 
    * client/user
    */
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
//        System.out.println("Test");
//        for (JsonValue dataPoint:dataPoints){
//            System.out.println(dataPoint);
//            
//        }
//        for (JsonValue dataValue:dataValues){
//            System.out.println(dataValue);
//            
//        }
    }
}
