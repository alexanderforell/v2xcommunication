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
import v2gcommunication.server.interfaces.RequestReceived;
/**
 *
 * @author alexander
 */
public class EventEvaluation implements VehicleDataReceived, RequestReceived {
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
    @Override public void requestReceived(String fin, JsonObject jsonObject){
        
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
