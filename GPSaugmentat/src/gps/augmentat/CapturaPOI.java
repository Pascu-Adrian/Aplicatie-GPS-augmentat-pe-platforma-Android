package gps.augmentat;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CapturaPOI extends Activity{
	
	Preview previewcam;
	View panou;
	
	File tempimg=new File("/sdcard/gpsaugmentat/temp/tempimg.jpg");
	
	ImageView captura;
	TextView locatie;
	TextView datasiora;
	TextView datebusolatv;
	
	Location locatiecurenta;
	String directiecurenta;
	
	LocationManager manager;
	LocationListener locListener;
	String timp="0";
    String distanta="0";
    
    private SensorManager ManagerBusola;
	private Sensor Busola;
	int datebusola=0;
	
	int id;
	
	File root;
	
	Dialog dialog;
    Button salveazapoi;
    Button anuleazapoi;
    EditText numepoi;
    EditText descrierepoi;
    Spinner tippoi;
    

    String nume;
    String tip;
    String descriere;
    Location locatiecaptura;
    String directiecapturata;
    String timpcaptura;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capturapoi);
        
        //AFISARE
        
        //PREVIEW
        previewcam = new Preview(this);
		((FrameLayout)findViewById(R.id.previewcam)).addView(previewcam);
        //////----------------------PREVIEW
		
		//DATE LOCATIE
		LayoutInflater linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		panou = linflater.inflate(R.layout.panoucapturapoi, null);
		((FrameLayout)findViewById(R.id.previewcam)).addView(panou);
		captura=(ImageView)panou.findViewById(R.id.imageView1);
		captura.setEnabled(false);
		locatie=(TextView)panou.findViewById(R.id.locatieCapturaPOI);;
		datasiora=(TextView)panou.findViewById(R.id.DataOraCapturaPOI);
		datebusolatv=(TextView)panou.findViewById(R.id.datebusolaPOI);
		//------------DATE LOCATIE
			
        ////-----------------AFISARE
		
		
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
        
        
	   
	    
        //ULTIM ID
		root = Environment.getExternalStorageDirectory();
		final File poi = new File("/sdcard/gpsaugmentat/harti/romania/constanta/poi/POI.xml");
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
        	if(xpp.getName().equals("ultimid")){
        		eventType = xpp.next();
        		id=Integer.parseInt(xpp.getText())+1;
        		break;
        	}   		
        		} 
        	else if(eventType == XmlPullParser.TEXT){      		
        		//continut.append("NUME:"+xpp.getText());  		
        	}  	        
        	eventType = xpp.next();
        	}        
		}
		catch (Exception e) {} 
	    //-----------------------------------------ULTIM ID
        
	    
	    
	  //DATA SI ORA
	    Thread threadora = null;
	    Runnable runnable = new CountDownRunner();
	    threadora= new Thread(runnable);   
	    threadora.start();
	    //--------------------------------------DATA SI ORA
	    
	    
	    //ALTELE
	    dialog=new Dialog(this);
	    dialog.setContentView(R.layout.dialogdatepoi);
        dialog.setTitle("Completati datele punctului de interes");
        dialog.setCancelable(true);
        
        salveazapoi=(Button)dialog.findViewById(R.id.butonsalveazapoi);
        anuleazapoi=(Button)dialog.findViewById(R.id.butonanuleazapoi);
        numepoi=(EditText)dialog.findViewById(R.id.numepoiinput);
        descrierepoi=(EditText)dialog.findViewById(R.id.descrierepoiinput);
        tippoi=(Spinner)dialog.findViewById(R.id.spinner1);
        
	    
	    //--------------------ALTELE
	    
        
        //LISTENERI
        
        salveazapoi.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nume=numepoi.getText().toString();
				descriere=descrierepoi.getText().toString();
				tip=tippoi.getSelectedItem().toString();
				
				Bitmap imagethumbnail=BitmapFactory.decodeFile("/sdcard/gpsaugmentat/temp/tempimg.jpg");
				imagethumbnail=Bitmap.createScaledBitmap(imagethumbnail, 60, 60, true);
				
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				imagethumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				byte[] byteArray = stream.toByteArray();
				
				FileOutputStream outStream = null;
			      try {
			        outStream = new FileOutputStream(new File("/sdcard/gpsaugmentat/harti/romania/constanta/poi/pozemici/"+id+".jpg")); // <9>
			        outStream.write(byteArray);
			        outStream.close();

			      } catch (FileNotFoundException e) { // <10>
			        e.printStackTrace();
			      } catch (IOException e) {
			        e.printStackTrace();
			      } finally {
			      }
				
				tempimg.renameTo(new File("/sdcard/gpsaugmentat/harti/romania/constanta/poi/pozemari/"+id+".jpg"));
				
				scriepoi(String.valueOf(id), nume, tip, descriere, String.valueOf(locatiecaptura.getLatitude()),
						String.valueOf(locatiecaptura.getLongitude()), directiecapturata, timpcaptura);
				dialog.hide();
			}

			private void scriepoi(String id,String nume, String tip, String descriere, String latitudine, String longitudine, String directie, String timpcaptura) {
				// TODO Auto-generated method stub
				//<poi id="1" nume="Acasa" tip="personal" descriere="Nicaieri nu-i ca acasa" latitudine="44.199436" longitudine="28.652161" directie="65" timestamp="15.8.2011--23:40:16"></poi>
				String date="id=\""+id+"\" nume=\""+nume+"\" tip=\""+tip+"\" descriere=\""+descriere+
				"\" latitudine=\""+latitudine+"\" longitudine=\""+longitudine+"\" directie=\""+directie+"\" timpcaptura=\""+timpcaptura+"\"";
					  try {    
					    	File root = Environment.getExternalStorageDirectory();
					    	if (root.canWrite()){ 
					    		File gpxfile = new File("/sdcard/gpsaugmentat/harti/romania/constanta/poi/POI.xml"); 
					    		FileWriter gpxwriter = new FileWriter(gpxfile,true);
					    		BufferedWriter out = new BufferedWriter(gpxwriter); 
					    		out.write("\n<poi "+date+"></poi>"); 
					    		out.close();
					    		}
					    	} 
					    catch (IOException e) { 	}
					  
					  
					  try {
						    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
						    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
						    Document doc = docBuilder.parse("/sdcard/gpsaugmentat/harti/romania/constanta/poi/POI.xml");
						    Node nodes = doc.getElementsByTagName("ultimid").item(0);
						    nodes.setTextContent(String.valueOf(id));

						    Transformer transformer = TransformerFactory.newInstance().newTransformer();
						    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
						    
						    StreamResult result = new StreamResult("/sdcard/gpsaugmentat/harti/romania/constanta/poi/POI.xml");
						    DOMSource source = new DOMSource(doc);
						    transformer.transform(source, result);
						    

						} catch (Exception e) {
						    e.printStackTrace();
						}
			}
		});
        
        anuleazapoi.setOnClickListener(new OnClickListener() {	
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nume="";
			    tip="";
			    descriere="";
			    locatiecaptura=new Location("fals");
			    directiecapturata="";
			    timpcaptura="";
				if(tempimg.exists())
				tempimg.delete();
				dialog.hide();
			}
		});
        
	    final SensorEventListener ListenerBusola = new SensorEventListener() {
			public void onSensorChanged(SensorEvent event) {	
				if(event.sensor.getType() == Sensor.TYPE_ORIENTATION){		
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
	    
	    
	    
		captura.setOnClickListener(new OnClickListener(){		
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Parameters par = previewcam.camera.getParameters();
				par.setPictureSize(previewcam.camera.getParameters().getSupportedPictureSizes().get(4).width,previewcam.camera.getParameters().getSupportedPictureSizes().get(4).height);
				previewcam.camera.setParameters(par);
				previewcam.camera.takePicture(shutterCallback, rawCallback, jpegCallback);
				
				locatiecaptura=locatiecurenta;
				directiecapturata=directiecurenta;
				Calendar c = Calendar.getInstance(); 
				timpcaptura=c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)+
                		"/"+c.get(Calendar.DAY_OF_MONTH)+"."+c.get(Calendar.MONTH)+"."+c.get(Calendar.YEAR);
		    	dialog.show();
			}
		});	
		//---------------------------LISTENERI
        
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

public void afiseazaora() {
    runOnUiThread(new Runnable() {
        public void run() {
            try{
                TextView dataora= (TextView)findViewById(R.id.DataOraCapturaPOI);
                    Calendar c = Calendar.getInstance(); 
                    dataora.setText(c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND)
                    		+" "+c.get(Calendar.DAY_OF_MONTH)+"."+c.get(Calendar.MONTH)+"."+c.get(Calendar.YEAR)+" ID:"+id);
            }catch (Exception e) {}
        }
    });
}


ShutterCallback shutterCallback = new ShutterCallback() { // <6>
    public void onShutter() {
     
    }
  };
  PictureCallback rawCallback = new PictureCallback() { // <7>
    public void onPictureTaken(byte[] data, Camera camera) {
    
    }
  };
  PictureCallback jpegCallback = new PictureCallback() { // <8>
    public void onPictureTaken(byte[] data, Camera camera) {
      FileOutputStream outStream = null;
      try {
        outStream = new FileOutputStream("/sdcard/gpsaugmentat/temp/tempimg.jpg"); // <9>
        outStream.write(data);
        outStream.close();

      } catch (FileNotFoundException e) { // <10>
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
      }
      previewcam.camera.startPreview();
    }
  };	
  
  
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
  
  
  //-----------------clase adiacente
  
  public class MyLocationListener implements LocationListener { 

  public void onLocationChanged(Location loc) {   
  loc.getLatitude(); 
  loc.getLongitude();
  locatiecurenta=loc;
  if(!captura.isEnabled())
  captura.setEnabled(true);
  locatie.setText("LAT:"+loc.getLatitude()+" LON:"+loc.getLatitude());
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
