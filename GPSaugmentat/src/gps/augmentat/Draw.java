package gps.augmentat;

import java.util.Calendar;
import java.util.Vector;

import gps.augmentat.GPSnavigatie.CountDownRunner;
import gps.augmentat.util.Anuntator;
import gps.augmentat.util.IncarcaResurse;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Draw extends Activity{
	 DrawView drawView;
	 LocationManager manager;
		LocationListener locListener;
		long timp=1000;
	    float distanta=30;
	    Location locatiecurenta;
	    Vector<POIobiect> listapoi;
	    Vector<POIobiect> poiapropiat;
	    
	    Dialog dialog;
	    Button filtreazapoi;
	    Button anuleazafiltru;
	    Spinner tippoi;
	    String filtrutippoi="";
	    
	    TextView datebusolatv;
	    TextView poisprestanga;
	    TextView poispredreapta;
	    Preview previewcam;
	    SensorManager ManagerAccelerometru;
	    SensorManager ManagerBusola;
	    Sensor Busola;
	    Sensor Accelerometru;
	    View panou;
	    TextView distantatv;
	    //Context context;
	    boolean incarcat=false;
	    TextView data;
	    AugmentedSurfaceView mGLSurfaceView;
	    Location nodulurmator;
	    Anuntator anunt;
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState); 
		 // Set full screen view 
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
	        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	        super.onCreate(savedInstanceState);
		  previewcam = new Preview(this);
		 setContentView(R.layout.gps);
		 LayoutInflater linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			panou = linflater.inflate(R.layout.datepoiaugmentat, null);
		 previewcam = new Preview(this);
			((FrameLayout)findViewById(R.id.preview)).addView(previewcam);
			mGLSurfaceView = new AugmentedSurfaceView(this,new SageataRenderer(true));
			((FrameLayout) findViewById(R.id.preview)).addView(mGLSurfaceView);
			
			mGLSurfaceView.cr.mTranslateX=1000;
			mGLSurfaceView.requestRender();
			
			anunt=new Anuntator(this);
			((FrameLayout)findViewById(R.id.preview)).addView(panou);
			listapoi=new Vector<POIobiect>();
			poiapropiat=new Vector<POIobiect>();
			drawView=new DrawView(this, poiapropiat,previewcam);
			((FrameLayout)findViewById(R.id.preview)).addView(drawView);
			drawView.requestFocus();
			locatiecurenta=new Location("fals");
			locatiecurenta.setLatitude(44.199472);
			locatiecurenta.setLongitude(28.652188);
			locatiecurenta.setAltitude(80.0);
			nodulurmator=new Location("false");
			nodulurmator.setLatitude(44.199153);
			nodulurmator.setLongitude(28.651107);
			
			//locatiecurenta.setLatitude(44.197585269182);
			//locatiecurenta.setLongitude(28.647857454462);
			drawView.schimbalocatie(locatiecurenta);
			incarcat=true;
			IncarcaResurse incarcaResurse=new IncarcaResurse();
			listapoi=incarcaResurse.punctedeinteres(this);
			//Location locatiepoi=new Location("fals");
			//locatiepoi.setLatitude(44.198936);
			//locatiepoi.setLongitude(28.652419);
			//locatiepoi.setLatitude(44.197585269182);
			//locatiepoi.setLongitude(28.647857454462);
			
			//locatiepoi.setLatitude(44.198913);
			//locatiepoi.setLongitude(28.65243);
			//locatiepoi.setLatitude(44.198936);
			//locatiepoi.setLongitude(28.652419);
			//POIobiect po=new POIobiect("id", "De test", "Cultura", "faceam teste", locatiepoi, "200", "timp");
			//listapoi.add(po);
			//locatiecurenta=new Location("fals");
			//locatiepoi.setLatitude(44.198005);
			//locatiepoi.setLongitude(28.647881);
			//po=new POIobiect("id", "De test", "Cultura", "faceam teste", locatiepoi, "200", "timp");
			//listapoi.add(po);

			poispredreapta=(TextView)findViewById(R.id.punctedeinteresspredreapta);
			poisprestanga=(TextView)findViewById(R.id.punctedeinteressprestanga);
			
			
		//FILTRARE
			
			
		    
			dialog=new Dialog(this);
			
		    dialog.setContentView(R.layout.dialogfiltrarepoiaugmentat);
		    dialog.setTitle("Selectati criteriu de filtrare");
		    dialog.setCancelable(false);
		    
		    anuleazafiltru=(Button)dialog.findViewById(R.id.anuleazafiltrarepoiaugmentat1);
		    filtreazapoi=(Button)dialog.findViewById(R.id.executafiltrarepoiaugmentat1);
		    tippoi=(Spinner)dialog.findViewById(R.id.spinnertippoiaugmentat);
		    dialog.show();
		    
		    //FILTRARE
		    
		    
		    
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
					emitemesaj("Tip: "+filtrutippoi+" Distanta: 1000 m");
					filtreazapoi();
				}
			});
		    
			//----------------FILTRARE
		    
	
		    
		 
		 datebusolatv=(TextView)panou.findViewById(R.id.busoladatepoiaugmentat);
		 
		 drawView.mergila.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(getApplicationContext(),GPSnavigatie.class);
				i.putExtra("lat", Double.parseDouble((String) drawView.tvLat.getText()));
				i.putExtra("lon",Double.parseDouble((String) drawView.tvLong.getText()));
			//emitemesaj("merge");	
//				((FrameLayout)findViewById(R.id.preview)).removeView(panou);
//		        ((FrameLayout)findViewById(R.id.preview)).removeView(drawView);
//				
//		        drawView.dialog.hide();
//				
//				  mGLSurfaceView.cr.mTranslateX=0;
//		        
//		        LayoutInflater linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		        View informatii = linflater.inflate(R.layout.informatiigps, null); 
//		        ((FrameLayout) findViewById(R.id.preview)).addView(informatii);
//		        anunt.mergetilaprimaintersectie();
//		        Toast.makeText(getApplicationContext(), "28-27-22-16-12-10-8-6-4-2-1", Toast.LENGTH_LONG).show();
//		        
//		        datebusolatv=(TextView)informatii.findViewById(R.id.busola);
//		        data = (TextView)informatii.findViewById(R.id.data);
//		        distantatv = (TextView)informatii.findViewById(R.id.distanta1);
//		        distantatv.setText("1011 m");
//		        TextView viteza1 = (TextView)informatii.findViewById(R.id.viteza1);
//		        
//		        TextView durata = (TextView)informatii.findViewById(R.id.durata);
//		        ToggleButton setaricamera = (ToggleButton)informatii.findViewById(R.id.setaricamera);
//		        TextView strada = (TextView)informatii.findViewById(R.id.strada);
//		        strada.setText("Str. Muncel");
//		        ImageView imagineanunt = (ImageView)informatii.findViewById(R.id.imagineanunt);
//		        TextView distantapanalaintersectie = (TextView)informatii.findViewById(R.id.distantapanalaintersectie);
//		        
//		        durata.setText("Infinity MINUTE");
//		    	distantapanalaintersectie.setText("14 m");
//		    	viteza1.setText("0.0"+" KM/H");
//		    	strada.setText("Muncel");
//		        
		        
		      //DATA SI ORA
			    Thread threadora = null;
			    Runnable runnable = new CountDownRunner();
			    threadora= new Thread(runnable);   
			    threadora.start();
			    //--------------------------------------DATA SI ORA
			}
		});
		//GPS
			manager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
			locListener = new MyLocationListener();
		    manager.requestLocationUpdates( 
		    		LocationManager.GPS_PROVIDER, 
		    		timp, 
		    		distanta, 
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
			final SensorEventListener ListenerAccelerometru = new SensorEventListener() {				
				public void onSensorChanged(SensorEvent event) {
					if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){	
						if(incarcat)
						drawView.schimbainclinatie(event.values[2]);
						if(event.values[2]>0&&event.values[0]*9.0f>0&&locatiecurenta.getSpeed()<10){									
							mGLSurfaceView.cr.mAngleX=(90-(int)(event.values[0]*9.0f))/10;
							 mGLSurfaceView.requestRender();
						}
						else if(event.values[2]<0&&event.values[0]>0&&locatiecurenta.getSpeed()<10){
							mGLSurfaceView.cr.mAngleX=-((90-(int)(event.values[0]*9.0f))/10);
							 mGLSurfaceView.requestRender();
						}		
						else{
							mGLSurfaceView.cr.mAngleX=0;
							 mGLSurfaceView.requestRender();
						}
					}
				}
				
				public void onAccuracyChanged(Sensor sensor, int accuracy) {
					// TODO Auto-generated method stub
				}};
				ManagerAccelerometru.registerListener(ListenerAccelerometru, Accelerometru, SensorManager.SENSOR_ACCELEROMETER);
		 
				final SensorEventListener ListenerBusola = new SensorEventListener() {
					public void onSensorChanged(SensorEvent event) {	
						if(event.sensor.getType() == Sensor.TYPE_ORIENTATION){	
							poispredreapta.setText(drawView.countspredreapta+">");
							poisprestanga.setText("<"+drawView.countsprestanga);
							if(event.values[0]>=338||event.values[0]<23){
								datebusolatv.setTextColor( 
										((250 << 24) & 0xFF000000) 
										+ ((0 << 16) & 0x00FF0000) 
										+ ((250 << 8) & 0x0000FF00)
										+ (0 & 0x000000FF)); 
								if(event.values[0]>=338){
									schimbaculoare(event.values[0]-270, 90,"E");
									if(incarcat)
									drawView.schimbadirectie(event.values[0]-270);
									mGLSurfaceView.cr.mAngleY=(event.values[0]-270-locatiecurenta.bearingTo(nodulurmator))/10;
									 mGLSurfaceView.requestRender();
								}
								if(event.values[0]<23){
									datebusolatv.setTextColor( 
											((250 << 24) & 0xFF000000) 
											+ ((11*((int)event.values[0]) << 16) & 0x00FF0000) 
											+ ((250-(11*(int)event.values[0]) << 8) & 0x0000FF00)
											+ (0 & 0x000000FF));
									datebusolatv.setText( "<E\n"+((int)event.values[0]+90)+"�");
									if(incarcat)
									drawView.schimbadirectie(event.values[0]+90);
									mGLSurfaceView.cr.mAngleY=(event.values[0]+90-locatiecurenta.bearingTo(nodulurmator))/10;
									 mGLSurfaceView.requestRender();
								}
							}
							if(event.values[0]>=23&&event.values[0]<68){		
								schimbaculoare(event.values[0]+90, 135,"SE");
								if(incarcat)
								drawView.schimbadirectie(event.values[0]+90);
								mGLSurfaceView.cr.mAngleY=(event.values[0]+90-locatiecurenta.bearingTo(nodulurmator))/10;
								 mGLSurfaceView.requestRender();
							}
							if(event.values[0]>=68&&event.values[0]<113){
								schimbaculoare(event.values[0]+90, 180,"S");
								if(incarcat)
								drawView.schimbadirectie(event.values[0]+90);
								mGLSurfaceView.cr.mAngleY=(event.values[0]+90-locatiecurenta.bearingTo(nodulurmator))/10;
								 mGLSurfaceView.requestRender();
							}
							if(event.values[0]>=113&&event.values[0]<158){
								schimbaculoare(event.values[0]+90, 225,"SV");
								if(incarcat)
								drawView.schimbadirectie(event.values[0]+90);
								mGLSurfaceView.cr.mAngleY=(event.values[0]+90-locatiecurenta.bearingTo(nodulurmator))/10;
								 mGLSurfaceView.requestRender();
							}
							if(event.values[0]>=158&&event.values[0]<203){
								schimbaculoare(event.values[0]+90, 270,"V");
								if(incarcat)
								drawView.schimbadirectie(event.values[0]+90);
								mGLSurfaceView.cr.mAngleY=(event.values[0]+90-locatiecurenta.bearingTo(nodulurmator))/10;
								 mGLSurfaceView.requestRender();
							}
							if(event.values[0]>=203&&event.values[0]<248){
								schimbaculoare(event.values[0]+90, 315,"NV");
								if(incarcat)
								drawView.schimbadirectie(event.values[0]+90);
								mGLSurfaceView.cr.mAngleY=(event.values[0]+90-locatiecurenta.bearingTo(nodulurmator))/10;
								 mGLSurfaceView.requestRender();
							}
							if(event.values[0]>=248&&event.values[0]<293){	
								if(event.values[0]<=270){
								schimbaculoare(event.values[0]+90, 359,"N");
								if(incarcat)
								drawView.schimbadirectie(event.values[0]+90);
								mGLSurfaceView.cr.mAngleY=(event.values[0]+90-locatiecurenta.bearingTo(nodulurmator))/10;
								 mGLSurfaceView.requestRender();
								}
								else{
									schimbaculoare(event.values[0]-270, 359,"N");
									if(incarcat)
									drawView.schimbadirectie(event.values[0]-270);
									mGLSurfaceView.cr.mAngleY=(event.values[0]-270-locatiecurenta.bearingTo(nodulurmator))/10;
									 mGLSurfaceView.requestRender();
								}	
							}
							if(event.values[0]>=293&&event.values[0]<338){
								schimbaculoare(event.values[0]-270, 45,"NE");
								if(incarcat)
								drawView.schimbadirectie(event.values[0]-270);
								mGLSurfaceView.cr.mAngleY=(event.values[0]-270-locatiecurenta.bearingTo(nodulurmator))/10;
								 mGLSurfaceView.requestRender();
							}
						}
					}
					public void onAccuracyChanged(Sensor sensor, int accuracy) {
						// TODO Auto-generated method stub				
					}   
					  };
					  ManagerBusola.registerListener(ListenerBusola, Busola,SensorManager.SENSOR_DELAY_FASTEST);
	 
	 }
	 @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		previewcam.surfaceDestroyed(previewcam.mHolder);

	}


	public class MyLocationListener implements LocationListener { 

			public void onLocationChanged(Location loc) {   
			loc.getLatitude(); 
			loc.getLongitude();

			locatiecurenta=loc;
			if(!filtrutippoi.equals("")){
			drawView.schimbalocatie(locatiecurenta);
			filtreazapoi();
			}
			
			//filtreazapoi();
			//drawView.setListapoi(poiapropiat);
			//drawView.schimbalocatie(loc);
			//drawView.afiseazapoi();
			
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
	private void filtreazapoi() {
		// TODO Auto-generated method stub
		poiapropiat=new Vector<POIobiect>();
		for (POIobiect poi : listapoi) {
			if(poi.getTippoi().equals(filtrutippoi)||filtrutippoi.equals("Toate")){
				if(poi.getLocatie().distanceTo(locatiecurenta)<=1000){
				poiapropiat.add(poi);
				}
			}
		}
		drawView.setListapoi(poiapropiat);
		//drawView.afiseazapoi();
		
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
		}
	 
	 private void emitemesaj(String mesaj) {
			// TODO Auto-generated method stub
			Toast.makeText( getApplicationContext(),mesaj, Toast.LENGTH_LONG ).show(); 
		}
	 public void afiseazadialog(String id){
		 	for (POIobiect punctdeinteres : listapoi) {
				//afiseaza dialog
		 		break;
			}
	 }
	 public void afiseazaora() {
		    runOnUiThread(new Runnable() {
		        public void run() {
		            try{
		                
		                    Calendar c = Calendar.getInstance();                 
							data.setText(c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)
		                    		+" "+c.get(Calendar.DAY_OF_MONTH)+"."+c.get(Calendar.MONTH)+"."+c.get(Calendar.YEAR));
		            }catch (Exception e) {}
		        }
		    });
		}
	 class CountDownRunner implements Runnable{
		    public void run() {
		            while(!Thread.currentThread().isInterrupted()){
		                try {
		                	afiseazaora();
		                    Thread.sleep(1000);
		                } catch (InterruptedException e) {
		                        Thread.currentThread().interrupt();
		                }catch(Exception e){
		                }
		            }
		    }
		}
	 }

