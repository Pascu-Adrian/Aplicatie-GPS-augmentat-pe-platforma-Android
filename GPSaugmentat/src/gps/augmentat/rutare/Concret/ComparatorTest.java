/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gps.augmentat.rutare.Concret;

import gps.augmentat.rutare.object.Ruta;

import java.util.Comparator;



public class ComparatorTest implements Comparator<Ruta> {

 
    public int compare(Ruta o1, Ruta o2) {
        
        if(o1.GetCostTotal()<o2.GetCostTotal()) return -1;
        if(o1.GetCostTotal()>o2.GetCostTotal()) return 1;
        return 0;
    }
    
}
