/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gps.augmentat.rutare.abstracte;

import gps.augmentat.rutare.object.NodObiect;

import java.util.Vector;


public interface IIncarcareDate {
    
   public Vector<NodObiect> incarcaDinXml(String fileName);
    
}
