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
 * The {@code DatabaseConnection} class connections to the database. And
 * wraps methods enter information into the database and read information
 * from the database
 * 
 * @author Alexander Forell
 */
public class DatabaseConnection {
    /**
    * Field which holds the vehicle connection
    */
    private Connection conn;
    
    /**
    * Costructs the database connection and strores it into the field conn;
    * 
    * @param    dbServerName    a string for the IP Adress of the database server
    * @param    dbServerPort    an integer specifying the port the database 
    * server is listening
    * @param    dbScheme        a string specifying the database scheme 
    * which is used to store information
    * @param    dbUserName      a string with the username for the database user
    * @param    dbPassword      a string with the password for the database user 
    */
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
            System.err.println("SQLException: " + ex.getMessage());
            System.err.println("SQLState: " + ex.getSQLState());
            System.err.println("VendorError: " + ex.getErrorCode());
            System.exit(-1);
        }
    }
    
    /**
    * Stores vin, code of the diagnosis item and corresponding values into 
    * the database.
    * 
    * @param    vin             a string for the IP Adress of the database 
    * server
    * @param    FunctionCode    HEX of the code of the stored diagnosis 
    * information
    * @param    FunctionValue   HEX of the value of the value of the stored 
    * diagnosis inforamtion 
    */
    public void storeData(String vin, Long FunctionCode, Long FunctionValue){
        String query = "insert into receiveddata (functionCode, data, fin)"
        + " values (?, ?, ?)";
        
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setLong(1, FunctionCode);
            preparedStmt.setLong(2, FunctionValue);
            preparedStmt.setString(3, vin);
            preparedStmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
    * Enters a new client login and password into the database
    * 
    * @param    userName    string holding the username
    * @param    password    string holding the password for the user
    */
    public void createUser(String userName, String password){
         String query = "insert into users (username, password)"
        + " values (?, ?)";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, userName);
            preparedStmt.setString(2, password);
            preparedStmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    /**
    * Deletes a client login from the database
    * 
    * @param    userName    string holding the username
    */
    public void deleteUser(String userName){
         String query = "delete from users where username =?";
        try {
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setString(1, userName);
            preparedStmt.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
