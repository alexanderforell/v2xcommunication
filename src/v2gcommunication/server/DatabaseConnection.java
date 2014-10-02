/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author alexander
 */
public class DatabaseConnection {
    Connection conn;
    DatabaseConnection(String dbServerName, int dbServerPort, String dbScheme, String dbUserName, String dbPassword){
        try {
            // Der Aufruf von newInstance() ist ein Workaround
            // für einige misslungene Java-Implementierungen

            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } 
        catch (Exception ex) {
            System.err.println("Unable to load connector/J");
            System.exit(-1);
        }
        try {
            conn = DriverManager.getConnection("jdbc:mysql://"+dbServerName+":"+dbServerPort+"/"+dbScheme+"?user="+dbUserName+"&password="+dbPassword);
        } 
        catch (SQLException ex) {
            // Fehler behandeln
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            System.exit(-1);
        }
    }
    
    String getVehicleVIN(String VIN){
        return "TEST";
    }
    
}
