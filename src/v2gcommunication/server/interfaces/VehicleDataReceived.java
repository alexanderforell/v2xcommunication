/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.server.interfaces;
import javax.json.*;
/**
 *
 * @author alexander
 */
public interface VehicleDataReceived {
        public void vehicleDataReceived(String fin, JsonObject jsonObject);
        
}
