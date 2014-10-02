/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.server;

import java.io.*;

/**
 *
 * @author Alexander Forell
 * 
 * Class reads and stores Configuration parameters
 * 
 */
public class Configuration {
    private int portVehicleCommunication;
    private int portUserCommunication;
    private String databaseServer;
    private int databasePort;
    private String databaseScheme;
    private String databaseUserName;
    private String databasePassword;
    /**
     * Constructor read File ServerConfig.txt and stores the class attributes
     */
    Configuration(){
        int i = 1;
        try { 
            BufferedReader  br = new BufferedReader(new FileReader("build/classes/v2gcommunication/server/ServerConfig.txt"));
            String line = br.readLine();
            while (null!=line) {
                String Param[] = line.split(":");
                if ("PortVehicleCommunication".equals(Param[0])){
                    portVehicleCommunication = Integer.parseInt(Param[1]);
                }
                if ("PortUserCommunication".equals(Param[0])){
                    portUserCommunication = Integer.parseInt(Param[1]);
                }
                if ("databaseServer".equals(Param[0])){
                    databaseServer = Param[1];
                }
                if ("databasePort".equals(Param[0])){
                    databasePort = Integer.parseInt(Param[1]);
                }
                if ("databaseScheme".equals(Param[0])){
                    databaseScheme = Param[1];
                }
                if ("databaseUserName".equals(Param[0])){
                    databaseUserName = Param[1];
                }
                if ("databasePassWord".equals(Param[0])){
                    databasePassword = Param[1];
                }
                i=i+1;
                line = br.readLine();
            }
            br.close();
            
        }
        catch (IOException  e){
            System.err.println("File ServerConfig.txt not found");
        }
        catch ( NumberFormatException e) {
            System.err.println("Wrong number format in line " + i);
        }
    }
    /**
     * 
     * @return Returns vehicle communication port 
     */
    int getPortVehicleCommunication(){
        return portVehicleCommunication; 
    }
    /**
     * 
     * @return Returns user communication port 
     */
    int getPortUserCommunication(){
        return portUserCommunication; 
    }
    String databaseServer(){
        return databaseServer; 
    }
    int databasePort(){
        return databasePort; 
    }
    String databaseScheme(){
        return databaseScheme; 
    }
    String databaseUserName(){
        return databaseUserName; 
    }
    String databasePassword(){
        return databasePassword; 
    }
}
