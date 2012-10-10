package gps.augmentat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import gps.augmentat.CapturaPOI.MyLocationListener;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class POIAUGMENTAT extends Activity{
	
	Dialog dialog;
    Button filtreazapoi;
    Button anuleazafiltru;
    Spinner tippoi;
	
	Preview previewcam;
	View panou;
	TextView datebusolatv;
	Location locatiecurenta;
	String directiecurenta;
	
	LocationManager manager;
	LocationListener locListener;
	String timp="0";
    String distanta="0";
    
    SensorManager ManagerAccelerometru;
    SensorManager ManagerBusola;
    Sensor Busola;
    Sensor Accelerometru;
	int datebusola=0;
	
	float lastrot;
	
	String filtrutippoi;
    double filtrudistantapoi;

	public String xacc;
	
	Vector<ImageView> listapoi;
	FrameLayout layoutimagini;
	
	AugmentedSurfaceView vedereaugmentata;
	
	ImageView img;
	
	AugmentedSurfaceView mGLSurfaceView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
 
		
		
		
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poiaugmentat);
        
        listapoi=new Vector<ImageView>();
        
        
        //PREVIEW
        
        previewcam = new Preview(this);
		((FrameLayout)findViewById(R.id.prvcam)).addView(previewcam);
		
        
        //-----------------------------------PREVIEW
		
		//PANOU DATE POI
		LayoutInflater linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		panou = linflater.inflate(R.layout.datepoiaugmentat, null);
		((FrameLayout)findViewById(R.id.prvcam)).addView(panou);
		mGLSurfaceView = new AugmentedSurfaceView(this,new SageataRenderer(true));  
        ((FrameLayout) findViewById(R.id.preview)).addView(mGLSurfaceView);
		datebusolatv=(TextView)panou.findViewById(R.id.busoladatepoiaugmentat);
		img=(ImageView)panou.findViewById(R.id.imageView1);
		
		
		
		//----------------------PANOU DATE POI
        
		
		//GPS
		manager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locListener = new MyLocationListener();
	    manager.requestLocationUpdates( 
	    		LocationManager.GPS_PROVIDER, 
	    		Long.parseLong(timp), 
	    		Float.valueOf(distanta), 
	    		locListener);
        //--------------------------GPS
	    
	  //ORIENTARE
	    ManagerBusola = (SensorManager)getSystemService(SENSOR_SERVICE);
		Busola = ManagerBusola.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		
	    //---------------------ORIENTARE
		
        //ACCELEROMETRU
		ManagerAccelerometru=(SensorManager)getSystemService(SENSOR_SERVICE);
		Accelerometru=ManagerAccelerometru.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		//------------------ACCELEROMETRU
        //FILTRARE
		
		
	    
		dialog=new Dialog(this);
	    dialog.setContentView(R.layout.dialogfiltrarepoiaugmentat);
	    dialog.setTitle("Selectati criteriu de filtrare");
	    dialog.setCancelable(false);
	    
	    anuleazafiltru=(Button)dialog.findViewById(R.id.anuleazafiltrarepoiaugmentat1);
	    filtreazapoi=(Button)dialog.findViewById(R.id.executafiltrarepoiaugmentat1);
	    tippoi=(Spinner)dialog.findViewById(R.id.spinnertippoiaugmentat);
	    dialog.show();
	    
	    
		//----------------FILTRARE
        //afiseaza
        //anima
	    
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
				emitemesaj("Tip:"+filtrutippoi+" Distanta:2000 m");
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
				        while (eventType != XmlPullParser.END_DOCUMENT) { 
				        	 
				        	if(eventType == XmlPullParser.START_TAG) { 
				        		
				        	if(xpp.getName().equals("poi")){
				        		Location locatie=new Location("falsa");
				        	     locatie.setLatitude(Double.parseDouble(xpp.getAttributeValue(4)));
				        	     locatie.setLongitude(Double.parseDouble(xpp.getAttributeValue(5)));
				        		if(locatiecurenta.distanceTo(locatie)<=filtrudistantapoi||true){
				        			if(filtrutippoi.equals(xpp.getAttributeValue(2))||filtrutippoi.equals("Toate")||true){
				        				
				        				
				        				ImageView imagine=new ImageView(panou.getContext());
				        				
				        				final String id=xpp.getAttributeValue(0);
						        	     Bitmap bmImg = BitmapFactory.decodeFile("/sdcard/gpsaugmentat/harti/romania/constanta/poi/pozemici/"+id+".jpg"); 
						        	     imagine.setImageBitmap(bmImg);
						        	     imagine.setOnClickListener(new OnClickListener() {
											
											public void onClick(View v) {
												// TODO Auto-generated method stub
												emitemesaj("MERGE"+id);
											}
										});
						        	     
						        	     listapoi.add(imagine);
						        	     layoutimagini.addView(imagine);
						        	     
						        	     
						        	    /* ImageView image = new ImageView(panou.getContext());
						        	     LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
						        	     image.setLayoutParams(vp);
						        	     image.setScaleType(ImageView.ScaleType.CENTER_CROP);
						        	     image.setMaxHeight(50);
						        	     image.setMaxWidth(50);
						        	     //final String id=xpp.getAttributeValue(0);
						        	     Bitmap bmImg = BitmapFactory.decodeFile("/sdcard/gpsaugmentat/harti/romania/constanta/poi/pozemici/"+id+".jpg"); 
						        	     image.setImageBitmap(bmImg);
						        	     layoutimagini.addView(image);*/
						        	     
						        	     
						        	     
				        		 /*View vedere = linflater.inflate(R.layout.poiitem, null); 
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
				        	     distanta.setText(String.valueOf(locatiecurenta.distanceTo(locatie)));
				        	     
				        	     
				        	     final String id=xpp.getAttributeValue(0);
				        	     Bitmap bmImg = BitmapFactory.decodeFile("/sdcard/gpsaugmentat/harti/romania/constanta/poi/pozemici/"+id+".jpg"); 
				        	     imagine.setImageBitmap(bmImg);
				        	     send.setText("Mergi la "+numepoi.getText());
				        	     send.setOnClickListener(new OnClickListener() {
				        			
				        			public void onClick(View v) {
				        				// TODO Auto-generated method stub 

				        				
				        				nextScreen.putExtra("lat", latitudinepoi.getText());
				        				nextScreen.putExtra("lon", longitudinepoi.getText());
				        				nextScreen.putExtra("id", id);

				        
				        				startActivity(nextScreen);
				        			}
				        		});

				        	     l.addView(vedere);*/
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
				        /*for (ImageView imView : listapoi) {
				        	TranslateAnimation trs=new TranslateAnimation(0.0f, 50.0f, 0.0f, 50.0f);
				        	trs.setDuration(10);
							imView.startAnimation(trs);
							

						}*/

						}
						catch (Exception e) {
							// TODO: handle exception
						}
			}
		});
	    
		final SensorEventListener ListenerAccelerometru = new SensorEventListener() {				
			public void onSensorChanged(SensorEvent event) {
				if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
					
				//xacc=Float.toString(event.values[0]*9.0f).substring(0, 2);
					if(event.values[2]>0&&event.values[0]*9.0f>0){
					/*	xsageata.setText("-"+(90-(int)(event.values[0]*9.0f)));					
						mGLSurfaceView.cr.mAngleX=(90-(int)(event.values[0]*9.0f))/10;
						 mGLSurfaceView.requestRender();*/
						/*TranslateAnimation tla=new TranslateAnimation(img.getTop(), img.getTop()+event.values[0], img.getLeft(), img.getLeft());
						tla.setDuration(10);
						tla.setFillAfter(true);
						img.startAnimation(tla);*/
					
					}
					if(event.values[2]<0&&event.values[0]>0){
						/*xsageata.setText("+"+(90-(int)(event.values[0]*9.0f)));
						mGLSurfaceView.cr.mAngleX=-((90-(int)(event.values[0]*9.0f))/10);
						 mGLSurfaceView.requestRender();*/
						/*TranslateAnimation tla=new TranslateAnimation(img.getTop(), img.getTop()-event.values[0], img.getLeft(), img.getLeft());
						tla.setDuration(10);
						tla.setFillAfter(true);
						img.startAnimation(tla);*/
					}			
				}
			}
			
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub
			}};
			
		
		
	    final SensorEventListener ListenerBusola = new SensorEventListener() {
			public void onSensorChanged(SensorEvent event) {	
				if(event.sensor.getType() == Sensor.TYPE_ORIENTATION){	
					TranslateAnimation tra=new TranslateAnimation(lastrot, event.values[0],0, 0);
					lastrot=event.values[0];
					tra.setDuration(1);
					
					tra.setFillAfter(true);
					img.startAnimation(tra);
					if(event.values[0]>=338||event.values[0]<23){
						datebusolatv.setTextColor( 
								((250 << 24) & 0xFF000000) 
								+ ((0 << 16) & 0x00FF0000) 
								+ ((250 << 8) & 0x0000FF00)
								+ (0 & 0x000000FF)); 
						if(event.values[0]>=338){
							schimbaculoare(event.values[0]-270, 90,"E");
							datebusola=(int)event.values[0]-270;
						}
						if(event.values[0]<23){
							datebusolatv.setTextColor( 
									((250 << 24) & 0xFF000000) 
									+ ((11*((int)event.values[0]) << 16) & 0x00FF0000) 
									+ ((250-(11*(int)event.values[0]) << 8) & 0x0000FF00)
									+ (0 & 0x000000FF));
							datebusolatv.setText( "<E\n"+((int)event.values[0]+90)+"�");
							datebusola=(int)event.values[0]+90;
						}
					}
					if(event.values[0]>=23&&event.values[0]<68){		
						schimbaculoare(event.values[0]+90, 135,"SE");
						datebusola=(int)event.values[0]+90;
					}
					if(event.values[0]>=68&&event.values[0]<113){
						schimbaculoare(event.values[0]+90, 180,"S");
						datebusola=(int)event.values[0]+90;
					}
					if(event.values[0]>=113&&event.values[0]<158){
						schimbaculoare(event.values[0]+90, 225,"SV");
						datebusola=(int)event.values[0]+90;
					}
					if(event.values[0]>=158&&event.values[0]<203){
						schimbaculoare(event.values[0]+90, 270,"V");
						datebusola=(int)event.values[0]+90;
					}
					if(event.values[0]>=203&&event.values[0]<248){
						schimbaculoare(event.values[0]+90, 315,"NV");
						datebusola=(int)event.values[0]+90;
					}
					if(event.values[0]>=248&&event.values[0]<293){	
						if(event.values[0]<=270){
						schimbaculoare(event.values[0]+90, 359,"N");
						datebusola=(int)event.values[0]+90;
						}
						else{
							schimbaculoare(event.values[0]-270, 359,"N");
						datebusola=(int)event.values[0]-270;
						}	
					}
					if(event.values[0]>=293&&event.values[0]<338){
						schimbaculoare(event.values[0]-270, 45,"NE");
						datebusola=(int)event.values[0]-270;
					}
				}
			}
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub				
			}   
			  };
			  ManagerBusola.registerListener(ListenerBusola, Busola,SensorManager.SENSOR_DELAY_NORMAL);
	    
	    //---------------------------LISTENERI
			  ManagerAccelerometru.registerListener(ListenerAccelerometru, Accelerometru, SensorManager.SENSOR_ACCELEROMETER);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		previewcam.surfaceDestroyed(null);
		super.onDestroy();
	}

@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
	        while (eventType != XmlPullParser.END_DOCUMENT) { 
	        	 
	        	if(eventType == XmlPullParser.START_TAG) { 
	        		
	        	if(xpp.getName().equals("poi")){
	        		Location locatie=new Location("falsa");
	        	     locatie.setLatitude(Double.parseDouble(xpp.getAttributeValue(4)));
	        	     locatie.setLongitude(Double.parseDouble(xpp.getAttributeValue(5)));
	        		if(locatiecurenta.distanceTo(locatie)<=filtrudistantapoi){
	        			if(filtrutippoi.equals(xpp.getAttributeValue(2))||filtrutippoi.equals("Toate")){
	        			
	        		/* View vedere = linflater.inflate(R.layout.poiitem, null); 
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
	        	     distanta.setText(String.valueOf(locatiecurenta.distanceTo(locatie)));
	        	     
	        	     
	        	     final String id=xpp.getAttributeValue(0);
	        	     Bitmap bmImg = BitmapFactory.decodeFile("/sdcard/gpsaugmentat/harti/romania/constanta/poi/pozemici/"+id+".jpg"); 
	        	     imagine.setImageBitmap(bmImg);
	        	     send.setText("Mergi la "+numepoi.getText());
	        	     send.setOnClickListener(new OnClickListener() {
	        			
	        			public void onClick(View v) {
	        				// TODO Auto-generated method stub 

	        				
	        				nextScreen.putExtra("lat", latitudinepoi.getText());
	        				nextScreen.putExtra("lon", longitudinepoi.getText());
	        				nextScreen.putExtra("id", id);

	        
	        				startActivity(nextScreen);
	        			}
	        		});

	        	     l.addView(vedere);*/
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
}


//-----------------clase adiacente

public class MyLocationListener implements LocationListener { 

public void onLocationChanged(Location loc) {   
loc.getLatitude(); 
loc.getLongitude();
locatiecurenta=loc;
//locatie.setText("LAT:"+loc.getLatitude()+" LON:"+loc.getLatitude());
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
public void schimbaculoare(float unghi,int valoareoptima,String punctcardinal){
	int dif=valoareoptima-(int)unghi;
	if(dif==0){
		datebusolatv.setTextColor( 
				((250 << 24) & 0xFF000000) 
				+ ((0 << 16) & 0x00FF0000) 
				+ ((250 << 8) & 0x0000FF00)
				+ (0 & 0x000000FF)); 
		datebusolatv.setText(">"+punctcardinal+"<\n"+(int)unghi+"�");
	}
	if(dif<0){
		datebusolatv.setTextColor( 
				((250 << 24) & 0xFF000000) 
				+ ((11*(-dif) << 16) & 0x00FF0000) 
				+ ((250-(11*(-dif)) << 8) & 0x0000FF00)
				+ (0 & 0x000000FF));
		datebusolatv.setText( "<"+punctcardinal+"\n"+(int)unghi+"�");
	}
	if(dif>0){
		datebusolatv.setTextColor( 
				((250 << 24) & 0xFF000000) 
				+ ((11*dif << 16) & 0x00FF0000) 
				+ ((250-(11*dif) << 8) & 0x0000FF00)
				+ (0 & 0x000000FF));
		datebusolatv.setText( punctcardinal+">\n"+(int)unghi+"�");	
	}
	directiecurenta=String.valueOf(unghi);
}

		}
