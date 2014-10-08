/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.server;
import javax.json.JsonObject;
import v2gcommunication.server.interfaces.DataReceived;
import v2gcommunication.server.interfaces.RequestReceived;
/**
 *
 * @author alexander
 */
public class EventEvaluation implements DataReceived, RequestReceived {
    ManageVehicleConnections manageVehicleConnections;
    ManageUserConnections manageUserConnections;
    DatabaseConnection conn;
    
    EventEvaluation(ManageVehicleConnections manageVehicleConnections, ManageUserConnections manageUserConnections, DatabaseConnection conn){
        this.manageUserConnections =  manageUserConnections;
        this.manageVehicleConnections = manageVehicleConnections;
        this.conn = conn;
        
    }
    
    @Override public void dataReceived(String fin, JsonObject jsonObject){
        
    }
    @Override public void requestReceived(String fin, JsonObject jsonObject){
        
    }
    
    
    
}
