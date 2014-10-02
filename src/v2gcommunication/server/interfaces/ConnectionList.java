/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.server.interfaces;
import java.net.*;
/**
 *
 * @author alexander
 */
public interface ConnectionList {
    
    void newConnection(Socket socket);
       
}
