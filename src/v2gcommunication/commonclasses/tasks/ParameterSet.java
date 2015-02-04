/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package v2gcommunication.commonclasses.tasks;

/**
 * Class ParameterSet holds four String fields in which parameterName, 
 * parameterValue, parameterType and timeStamp can be stored.
 * 
 * @author Alexander Forell
 */
public class ParameterSet {
    
    public String parameterName;
    public String parameterValue;
    public String parameterType;
    public String timeStamp;
    public ParameterSet(){
        this.parameterName = null;
        this.parameterValue = null;
        this.parameterType = null;
        this.timeStamp = null;
    }
        
}
