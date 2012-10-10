/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gps.augmentat.rutare.object;


import gps.augmentat.rutare.Concret.CalculDistanta;
import gps.augmentat.rutare.abstracte.ICalculDistantaNod;

import java.util.TreeSet;
import java.util.Vector;

import android.location.Location;


public class NodObiect {
    private String id;
    //private double latitudine;
    //private double longitudine;
    Location locatie;
    private Vector<NodObiect> listaVecini;
    private String NumeStrada;
    
    private ICalculDistantaNod dist;

    public NodObiect(String id, double latitudine, double longitudine, String NumeStrada) {
        this.id = id;
        this.locatie.setLatitude(latitudine);
        this.locatie.setLongitude(longitudine);
        this.NumeStrada = NumeStrada;
        listaVecini=new Vector<NodObiect>();
        dist=new CalculDistanta();
    }

    public NodObiect() {
        listaVecini=new Vector<NodObiect>();
        
    }

    
    
    public String getNumeStrada() {
        return NumeStrada;
    }

    public String getId() {
        return id;
    }

    public Location getLocatie() {
		return locatie;
	}
    /*public double getLatitudine() {
        return latitudine;
    }*/

    public Vector<NodObiect> getListaVecini() {
        return listaVecini;
    }

    /*public double getLongitudine() {
        return longitudine;
    }*/
    
    public double getDistanta(NodObiect nodEnd)
    {
        return dist.distantaLa(this, nodEnd);
    }

    public void setNumeStrada(String NumeStrada) {
        this.NumeStrada = NumeStrada;
    }

    public void setDist(ICalculDistantaNod dist) {
        this.dist = dist;
    }

    public void setId(String id) {
        this.id = id;
    }

    /*public void setLatitudine(double latitudine) {
        this.latitudine = latitudine;
    }*/

    public void setListaVecini(Vector<NodObiect> listaVecini) {
        this.listaVecini = listaVecini;
    }

    /*public void setLongitudine(double longitudine) {
        this.longitudine = longitudine;
    }*/
    public void setLocatie(Location locatie) {
		this.locatie = locatie;
	}

    public void adaugavecini(NodObiect noddinlista) {
       listaVecini.add(noddinlista);
    }
    
}
