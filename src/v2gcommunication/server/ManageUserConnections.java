/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.server;
import v2gcommunication.server.interfaces.ConnectionList;
import java.net.*;
import java.net.*;
import java.util.ArrayList;
/**
 *
 * @author alexander
 */
public class ManageUserConnections implements ConnectionList{
     ArrayList<UserConnection> userConnection = new ArrayList<>();
    
    @Override public void newConnection(Socket socket){
        this.userConnection.add(new UserConnection(socket) ) ;
    }
    
}
