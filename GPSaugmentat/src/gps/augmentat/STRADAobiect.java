package gps.augmentat;

import gps.augmentat.rutare.object.NodObiect;

import java.util.Vector;

import android.location.Location;

public class STRADAobiect {
Vector<NodObiect> intersectii;
String nume;
String id;
String tip;
public STRADAobiect() {
	// TODO Auto-generated constructor stub
	intersectii=new Vector<NodObiect>();
}

public Vector<NodObiect> getIntersectii() {
	return intersectii;
}
public void setIntersectii(Vector<NodObiect> intersectii) {
	this.intersectii = intersectii;
}

public void adaugaintersectie(NodObiect intersectie){
	this.intersectii.add(intersectie);
}

public boolean estepestrada(Location locatie){
	for(int i=0;i<intersectii.size();i++){
		if(intersectii.elementAt(i).getLocatie().equals(locatie))
			return true;
		if(intersectii.elementAt(i).getLocatie().distanceTo(locatie)<=10)
			return true;
		if(i<intersectii.size()-1){
			if(Integer.parseInt(String.valueOf(intersectii.elementAt(i).getLocatie().bearingTo(locatie)))==Integer.parseInt(String.valueOf(intersectii.elementAt(i).getLocatie().bearingTo(intersectii.elementAt(i+1).getLocatie()))))
				return true;
		}
		else{
			if(Integer.parseInt(String.valueOf(intersectii.elementAt(i).getLocatie().bearingTo(locatie)))==Integer.parseInt(String.valueOf(intersectii.elementAt(i).getLocatie().bearingTo(intersectii.elementAt(i-1).getLocatie()))))
				return true;
		}
			
	}
	
	return false;
}
public boolean contineintersectia(NodObiect intersectie){
	if(intersectii.contains(intersectie))
		return true;
	else 
		return false;	
}
public String getId() {
	return id;
}
public String getNume() {
	return nume;
}
public String getTip() {
	return tip;
}
public void setId(String id) {
	this.id = id;
}
public void setNume(String nume) {
	this.nume = nume;
}
public void setTip(String tip) {
	this.tip = tip;
}
}
