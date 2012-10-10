package gps.augmentat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
public class POIobiect {
	String id;
	String nume;
	String tippoi;
	String descriere;
	Location locatie;
	String directie;
	String timp;
	Bitmap icoana;
	Bitmap imaginemare;
	Bitmap imaginemica;
public POIobiect() {
}

public POIobiect(String id, String nume, String tippoi, String descriere, Location locatie,String directie, String timp){
	this.id=id;
	this.nume=nume;
	this.tippoi=tippoi;
	this.descriere=descriere;
	this.locatie=locatie;
	this.directie=directie;
	this.timp=timp;
	this.icoana=BitmapFactory.decodeFile("/sdcard/gpsaugmentat/media/imagini/icoane/"+tippoi+".png");
	this.imaginemare = BitmapFactory.decodeFile("/sdcard/gpsaugmentat/harti/romania/constanta/poi/pozemari/"+id+".jpg");
	this.imaginemica = BitmapFactory.decodeFile("/sdcard/gpsaugmentat/harti/romania/constanta/poi/pozemici/"+id+".jpg");
}

public String getDescriere() {
	return descriere;
}
public String getDirectie() {
	return directie;
}
public String getId() {
	return id;
}
public Location getLocatie() {
	return locatie;
}
public String getNume() {
	return nume;
}
public String getTimp() {
	return timp;
}
public String getTippoi() {
	return tippoi;
}
public void setDescriere(String descriere) {
	this.descriere = descriere;
}
public void setDirectie(String directie) {
	this.directie = directie;
}
public void setId(String id) {
	this.id = id;
}
public void setLocatie(Location locatie) {
	this.locatie = locatie;
}
public void setNume(String nume) {
	this.nume = nume;
}
public void setTimp(String timp) {
	this.timp = timp;
}
public void setTippoi(String tippoi) {
	this.tippoi = tippoi;
}
public Bitmap getIcoana() {
	return icoana;
}
public Bitmap getImaginemare() {
	return imaginemare;
}
public Bitmap getImaginemica() {
	return imaginemica;
}
}
