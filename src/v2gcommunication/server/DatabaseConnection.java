/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    public void storeData(String fin, Long FunctionCode, Long FunctionValue){
        String query = "insert into receiveddata (functionCode, data, fin)"
        + " values (?, ?, ?)";
        
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setLong(1, FunctionCode);
            preparedStmt.setLong(2, FunctionValue);
            preparedStmt.setString(3, fin);
            preparedStmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
