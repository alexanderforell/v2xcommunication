/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.server;
/**
 *
 * @author Alexander Forell
 * 
 * Class is handlig basic server functionality
 *  - start up
 *  - initializing sockets and ports
 *  - shut down
 */
public class Server {

    /**
     * @author Alexander Forell
     * @param args the command line arguments are not used
     * Function starts up server
     * initializes sockets
     * initializes class ManageVehicleConnections
     * initializes class ManageUserConnections
     */
    public static void main(String[] args) {
        Configuration config = new Configuration();
        ManageVehicleConnections manageVehicleConnections = new ManageVehicleConnections();
        ManageUserConnections manageUserConnections = new ManageUserConnections();
        DatabaseConnection databaseConnection = new DatabaseConnection(config.databaseServer(),config.databasePort(),config.databaseScheme(),config.databaseUserName(),config.databasePassword());
        ListenOnServerSocket vehicle = new ListenOnServerSocket(config.getPortVehicleCommunication(),manageVehicleConnections);
        vehicle.start();
        ListenOnServerSocket user = new ListenOnServerSocket(config.getPortUserCommunication(),manageVehicleConnections);
        user.start();
        //vehicle.close();
        //user.close();
        
        
    }
    
}
