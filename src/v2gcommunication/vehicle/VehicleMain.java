/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.vehicle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexander
 */
public class VehicleMain{
    public static void main(String[] args) {
        VehicleSimulator vehicle1 = new VehicleSimulator("BMW 520d", 0.28, 0.85, 1700.0);
        vehicle1.start();
        EventEvaluation eventEval= new EventEvaluation();
        ServerConnection conn = new ServerConnection(eventEval);
        int i = 0;
        conn.start();
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(VehicleMain.class.getName()).log(Level.SEVERE, null, ex);
            }
            Double[] speed = new Double[1];
            speed[0] = vehicle1.getSpeed();
            Long[] code = new Long[1];
            code[0] = (long)i;
            conn.transmitData(code, speed);
            System.out.println("Data transmitted " + i);
            i++;
        }
    }
}
