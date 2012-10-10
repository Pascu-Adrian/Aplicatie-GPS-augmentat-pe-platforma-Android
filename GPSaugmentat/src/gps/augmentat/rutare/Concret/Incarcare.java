/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gps.augmentat.rutare.Concret;


import gps.augmentat.rutare.abstracte.IIncarcareDate;
import gps.augmentat.rutare.object.NodObiect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Vector;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.location.Location;


public class Incarcare implements IIncarcareDate {


    public Vector<NodObiect> incarcaDinXml(String fileName) {

        // TODO Auto-generated method stub

        Vector<NodObiect> harta = new Vector<NodObiect>();
        File poi = new File(fileName);
        FileReader filereader = null;
        String id;
        NodObiect nod = new NodObiect();
        try {
            filereader = new FileReader(poi);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(filereader);
            int eventType = xpp.getEventType();


            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("nod")) {
                        nod = new NodObiect();

                        nod.setId(xpp.getAttributeValue(0));

                        //nod.setLatitudine(Double.parseDouble(xpp.getAttributeValue(1)));
                        //nod.setLongitudine(Double.parseDouble(xpp.getAttributeValue(2)));
                        Location locatie=new Location("false");
                        locatie.setLatitude(Double.parseDouble(xpp.getAttributeValue(1)));
                        locatie.setLongitude(Double.parseDouble(xpp.getAttributeValue(2)));
                        nod.setLocatie(locatie);

                        harta.add(nod);
                    }


                } else if (eventType == XmlPullParser.TEXT) {
                    //continut.append("NUME:"+xpp.getText());
                }

                eventType = xpp.next();

            }
             filereader = new FileReader(poi);
             factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            xpp = factory.newPullParser();
            xpp.setInput(filereader);
            
            eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals("nod")) {
                        id = xpp.getAttributeValue(0);
                        for (NodObiect noddinlista : harta) {
                            if (noddinlista.getId().equals(id)) {
                                nod = noddinlista;
                                break;
                            } else {
                                continue;
                            }
                        }
                    } else if (xpp.getName().equals("ref")) {
                        for (NodObiect noddinlista : harta) {
                            if (noddinlista.getId().equals(xpp.getAttributeValue(0))) {
                                nod.adaugavecini(noddinlista);
                            } else {
                                continue;
                            }
                        }
                    }

                }


                eventType = xpp.next();
            }



        } catch (Exception e) {
           e.printStackTrace();
        }

        return harta;

    }
}
