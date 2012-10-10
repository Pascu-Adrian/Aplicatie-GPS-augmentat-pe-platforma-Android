/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gps.augmentat.rutare.Concret;

import gps.augmentat.rutare.abstracte.ICalculDistantaNod;
import gps.augmentat.rutare.object.NodObiect;



public class CalculDistanta implements ICalculDistantaNod {

  
    public double distantaLa(NodObiect nodBegin, NodObiect nodEnd) {
       //double valoareaMea=Math.sqrt(Math.pow(nodBegin.getLatitudine()-nodEnd.getLatitudine(), 2)+Math.pow(nodBegin.getLongitudine()-nodEnd.getLongitudine(), 2));
    	double valoareaMea=Math.sqrt(Math.pow(nodBegin.getLocatie().getLatitude()-nodEnd.getLocatie().getLatitude(), 2)+Math.pow(nodBegin.getLocatie().getLongitude()-nodEnd.getLocatie().getLongitude(), 2));
        return valoareaMea;
    }
    
}
