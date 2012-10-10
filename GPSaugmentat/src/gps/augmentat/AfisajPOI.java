package gps.augmentat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class AfisajPOI {
	float x, y,deviatie;
	int size;
	POIobiect punctdeinteres;
	Drawable icoanaafisata;
public AfisajPOI() {
	// TODO Auto-generated constructor stub
}
public AfisajPOI(float x,float y,int size,float deviatie, POIobiect punctdeinteres) {
	this.x=x;
	this.y=y;
	this.size=size;
	this.deviatie=deviatie;
	this.punctdeinteres=punctdeinteres;
	this.icoanaafisata=micsoreaza(punctdeinteres.getIcoana(),size);
}
private Drawable micsoreaza(Bitmap icoana, int size) {
	// TODO Auto-generated method stub
    Bitmap bmp = Bitmap.createScaledBitmap(icoana, size, size, false);
    return new BitmapDrawable(bmp);
}
public Drawable getIcoanaafisata() {
	return icoanaafisata;
}
public float getDeviatie() {
	return deviatie;
}
public POIobiect getPunctdeinteres() {
	return punctdeinteres;
}
public float getSize() {
	return size;
}
public float getX() {
	return x;
}
public float getY() {
	return y;
}
public void setDeviatie(float deviatie) {
	this.deviatie = deviatie;
}
public void setPunctdeinteres(POIobiect punctdeinteres) {
	this.punctdeinteres = punctdeinteres;
}
public void setSize(int size) {
	this.size = size;
}
public void setX(float x) {
	this.x = x;
}
public void setY(float y) {
	this.y = y;
}
public boolean esteselectat(float xtouch,float ytouch){

	if(this.x<=xtouch&&(this.x+this.size)>=xtouch
			&&
			this.y<=ytouch&&(this.y+this.size)>=ytouch)
		return true;
	else
	return false;
	
}

}
