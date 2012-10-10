package gps.augmentat;

import gps.augmentat.CapturaPOI.MyLocationListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class POIactivity extends Activity{
	
	
	Dialog dialog;
    Button filtreazapoi;
    Button anuleazafiltru;
    Spinner tippoi;
    Spinner distantapoi;
    
    
    LocationManager manager;
	LocationListener locListener;
	String timp="0";
    String distanta="0";
    Location locatiecurenta;
    
    Vector<POIobiect> listapoi;
    
    String filtrutippoi;
    double filtrudistantapoi;
	
    LinearLayout l;
    LayoutInflater linflater;
	
    Intent nextScreen;
    
    TextView countpoi;
    int count=0;
    
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.listapoi);
    
    
    l = (LinearLayout) findViewById(R.id.linearLayout1); 
	linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
	       nextScreen = new Intent(this, GPSnavigatie.class);
    countpoi=(TextView)findViewById(R.id.countpoitxt);
    
    //GPS
    manager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
	locListener = new MyLocationListener();
    manager.requestLocationUpdates( 
    		LocationManager.GPS_PROVIDER, 
    		Long.parseLong(timp), 
    		Float.valueOf(distanta), 
    		locListener);
    //----------------------GPS
    
    //filtrare
    

    dialog=new Dialog(this);
    dialog.setContentView(R.layout.dialogfiltrarepoi);
    dialog.setTitle("Selectati criteriu de filtrare");
    dialog.setCancelable(false);
    dialog.show();
    
    anuleazafiltru=(Button)dialog.findViewById(R.id.anuleazafiltrarepoi);
    filtreazapoi=(Button)dialog.findViewById(R.id.executafiltrarepoi);
    tippoi=(Spinner)dialog.findViewById(R.id.spinner1);
    distantapoi=(Spinner)dialog.findViewById(R.id.spinner2);
    
    //---------------------------------------filtrare
    
    
    //LISTENERI
    anuleazafiltru.setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	});
    
    filtreazapoi.setOnClickListener(new OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			dialog.hide();
			filtrutippoi=tippoi.getSelectedItem().toString();
			filtrudistantapoi=Double.parseDouble(distantapoi.getSelectedItem().toString());
			emitemesaj("Tip:"+filtrutippoi+" Distanta:"+filtrudistantapoi+" m");
			incarcapoi();
		}

		private void incarcapoi() {
			// TODO Auto-generated method stub
			emitemesaj("Se incarca poi");
					File root = Environment.getExternalStorageDirectory();
					File poi = new File(root, "gpsaugmentat/harti/romania/constanta/poi/POI.xml");
					FileReader filereader = null;
					try {
						filereader = new FileReader(poi);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}		
					try{
					XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			        factory.setNamespaceAware(true); 
			        final XmlPullParser xpp = factory.newPullParser();  
			        xpp.setInput( filereader ); 
			        int eventType = xpp.getEventType(); 
			        int i=0;
			        count=0;
			        while (eventType != XmlPullParser.END_DOCUMENT) { 	        	 
			        	if(eventType == XmlPullParser.START_TAG) { 
			        	if(xpp.getName().equals("poi")){
			        		Location locatie=new Location("falsa");
			        	     locatie.setLatitude(Double.parseDouble(xpp.getAttributeValue(4)));
			        	     locatie.setLongitude(Double.parseDouble(xpp.getAttributeValue(5)));
			        		if(locatiecurenta.distanceTo(locatie)<=filtrudistantapoi){
			        			if(filtrutippoi.equals(xpp.getAttributeValue(2))||filtrutippoi.equals("Toate")){
			        				
			        			
			        		 View vedere = linflater.inflate(R.layout.poiitem, null); 
			        	     Button send=(Button)vedere.findViewById(R.id.mergilapoibutton); 
			        	     
			        	     send.setId(i); 
			        	     ImageView imagine=(ImageView)vedere.findViewById(R.id.imaginePOI);
			        	     		        	     
			        	     final TextView distanta=(TextView)vedere.findViewById(R.id.distantapoivaloare);
			        	     final TextView tippoi=(TextView)vedere.findViewById(R.id.tippoivaloare);
			        	     final EditText detaliipoi=(EditText)vedere.findViewById(R.id.infopoitxtarea);
			        	     final TextView latitudinepoi=(TextView)vedere.findViewById(R.id.latitudinepoitxt);
			        	     final TextView longitudinepoi=(TextView)vedere.findViewById(R.id.longitudinepoitxt);
			        	     final TextView numepoi=(TextView)vedere.findViewById(R.id.numepoiitem);

			        	     numepoi.setText(xpp.getAttributeValue(1));
			        	     tippoi.setText(xpp.getAttributeValue(2));
			        	     detaliipoi.setText(xpp.getAttributeValue(3));
			        	     latitudinepoi.setText(xpp.getAttributeValue(4));
			        	     longitudinepoi.setText(xpp.getAttributeValue(5));
			        	     distanta.setText(String.valueOf(Math.round(locatiecurenta.distanceTo(locatie)))+" m ");
			        	     
			        	     
			        	     final String id=xpp.getAttributeValue(0);
			        	     Bitmap bmImg = BitmapFactory.decodeFile("/sdcard/gpsaugmentat/harti/romania/constanta/poi/pozemici/"+id+".jpg"); 
			        	     imagine.setImageBitmap(bmImg);
			        	     send.setText("Mergi la "+numepoi.getText());
			        	     send.setOnClickListener(new OnClickListener() {	
			        			public void onClick(View v) {
			        				nextScreen.putExtra("lat", latitudinepoi.getText());
			        				nextScreen.putExtra("lon", longitudinepoi.getText());
			        				nextScreen.putExtra("id", id);	
			        				startActivity(nextScreen);
			        			}
			        		});

			        	     l.addView(vedere);
			        	     count++;
			        		}
			        	}
			        	}
			        		
			        		
			        		} 
			        	else if(eventType == XmlPullParser.TEXT){
			        		//continut.append("NUME:"+xpp.getText());
			        		
			        	}
			        	        
			        	eventType = xpp.next();
			        	
			        	i++;
			        	}       
			        

					}
					catch (Exception e) {
						// TODO: handle exception
					}
					countpoi.setText(" "+String.valueOf(count)+" ");
		}
	});
    
    //------------LISTENERI
    
    
    
}

@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}



//-----------------clase adiacente

public class MyLocationListener implements LocationListener { 

public void onLocationChanged(Location loc) {   
loc.getLatitude(); 
loc.getLongitude();
locatiecurenta=loc;
}
public void onProviderDisabled(String provider) { 
	  emitemesaj("Gps dezactivat");
} 
public void onProviderEnabled(String provider) { 
	  emitemesaj("Gps activat");
} 
public void onStatusChanged(String provider, int status, Bundle extras){ 
	  emitemesaj("Provider: "+provider+" Status: "+status);
} 
}

private void emitemesaj(String mesaj) {
		// TODO Auto-generated method stub
		Toast.makeText( getApplicationContext(),mesaj, Toast.LENGTH_SHORT ).show(); 
} 

}
