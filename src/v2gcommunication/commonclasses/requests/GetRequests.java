/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.requests;

import java.util.ArrayList;

/**
 * Interface which allows to add Requests
 *
 * @author alexander
 */
public interface GetRequests {
    public String getVIN();
    public void addRequests(ArrayList<Request> req);
}
