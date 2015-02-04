/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.sessions;

import java.util.ArrayList;
import v2gcommunication.commonclasses.tasks.Task;

/**
 * Interface used to get Data from task management.
 * 
 * 
 * @author Alexander Forell
 */
public interface GetTasks {
    /**
     * Get VIN or username of session
     * 
     * @return      Username or VIN
     */
    public String getVIN();
    /**
     * Passes a list of Task to transmit to session
     * 
     * @param taTr  ArrayList of Tasks to transmit
     */
    public void addTransmitTasks(ArrayList<Task> taTr);
    /**
     * Passes a list of tasks to receive to session.
     * 
     * @param taRe  ArrayList of Tasks to receive
     */
    public void addReceiveTasks(ArrayList<Task> taRe);
}
