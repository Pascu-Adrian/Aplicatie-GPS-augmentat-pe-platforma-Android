package gps.augmentat.util;

import gps.augmentat.POIobiect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

public class IncarcaResurse {
public IncarcaResurse() {
	// TODO Auto-generated constructor stub
}
public Vector<POIobiect> punctedeinteres(Context context){
	Vector<POIobiect> listapoi=new Vector<POIobiect>();
	File poi = new File("/sdcard/gpsaugmentat/harti/romania/constanta/poi/POI.xml");
	FileReader filereader = null;
	try {
		filereader = new FileReader(poi);
	} catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		emitemesaj("Fisierul POI.XML nu a fost gaist!",context);
		e1.printStackTrace();
		
	}
	try{
	XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
    factory.setNamespaceAware(true); 
    final XmlPullParser xpp = factory.newPullParser();  
    xpp.setInput( filereader ); 
    int eventType = xpp.getEventType(); 
    while (eventType != XmlPullParser.END_DOCUMENT) { 					        	 
    	if(eventType == XmlPullParser.START_TAG) { 				        		
    	if(xpp.getName().equals("poi")){					        					        					        			
    			String id=xpp.getAttributeValue(0);
    			String nume=xpp.getAttributeValue(1);
    			String tippoi=xpp.getAttributeValue(2);
    			String descriere=xpp.getAttributeValue(3);
    			Location locatie=new Location("falsa");
        	     locatie.setLatitude(Double.parseDouble(xpp.getAttributeValue(4)));
        	     locatie.setLongitude(Double.parseDouble(xpp.getAttributeValue(5)));	
        	     String directie=xpp.getAttributeValue(6);
    			String timp=xpp.getAttributeValue(7);

    			POIobiect punctdeinteres=new POIobiect(id, nume, tippoi, descriere, locatie, directie, timp);
    			listapoi.add(punctdeinteres);				        							        			   	
    	}
    		} 
    	else if(eventType == XmlPullParser.TEXT){}					        	        
    	eventType = xpp.next();
    	}    
    eventType = xpp.next();
	}
	catch (Exception e) {
		e.printStackTrace();
		emitemesaj("Eroare citire xml ",context);
	}
	return listapoi;
	
}
private void emitemesaj(String mesaj,Context context) {
	// TODO Auto-generated method stub
	Toast.makeText( context,mesaj, Toast.LENGTH_LONG ).show(); 
}
}
