/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.vehicle;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author alexander
 */
public class VehicleMain{
    public static void main(String[] args) {
        EventEvaluation eventEval= new EventEvaluation();
        ServerConnection conn = new ServerConnection(eventEval);
        int i = 0;
        conn.start();
        while (i<1){
            Date currentTime = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat ("yyyy.MM.dd 'at' HH:mm:ss.SSS");
            JsonObject transmit;
            JsonObjectBuilder message = Json.createObjectBuilder();
            JsonArrayBuilder dataCodes = Json.createArrayBuilder();
            JsonArrayBuilder dataValues = Json.createArrayBuilder();
            dataCodes.add(Integer.toHexString(i));
            dataValues.add(formatter.format(currentTime));
            message.add("Function-Name", "transmitData");
            message.add("Data-Points",dataCodes);
            message.add("Data-Values",dataValues);
            transmit = message.build();
            System.out.println(transmit.toString());
            conn.transmitMessage(transmit.toString().getBytes());
            i++;
        }
    }
}
