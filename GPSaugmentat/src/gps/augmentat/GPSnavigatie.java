package gps.augmentat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import gps.augmentat.CapturaPOI.CountDownRunner;
import gps.augmentat.CapturaPOI.MyLocationListener;
import gps.augmentat.rutare.Concret.CalculDistanta;
import gps.augmentat.rutare.Concret.Incarcare;
import gps.augmentat.rutare.abstracte.ICalculDistantaNod;
import gps.augmentat.rutare.astarrenew.AStarRenew;
import gps.augmentat.rutare.object.NodObiect;
import gps.augmentat.rutare.object.Ruta;
import gps.augmentat.rutare.*;
import gps.augmentat.util.Anuntator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera.Parameters;
import android.hardware.GeomagneticField;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class GPSnavigatie extends Activity {

	AugmentedSurfaceView mGLSurfaceView;
	ToggleButton setaricamera;
	TextView data;
	TextView durata;
	TextView distantatv;
	TextView strada;
	TextView viteza1;
	Location location;
	Location destinatie;
	String iddestinatie;
	// AStar calculruta;

	Preview previewcam;
	View panou;
	TextView datebusolatv;
	Location locatiecurenta;
	String directiecurenta;
	LocationManager manager;
	LocationListener locListener;
	String timp = "0";
	String distanta = "0";
	Vector<NodObiect> noade;
	SensorManager ManagerAccelerometru;
	SensorManager ManagerBusola;
	Sensor Busola;
	Sensor Accelerometru;
	int datebusola = 0;
	Vector<NodObiect> ruta = new Vector<NodObiect>();
	NodObiect nodulafterurmator = null;
	public String xacc;
	public String yacc;
	public String zacc;
	NodObiect nodulurmator = null;
	NodObiect nodulanterior = null;
	GeomagneticField gmfld;
	NodObiect nodBegin = null;
	NodObiect nodEnd = null;
	NodObiect nodCelMaiAproapeDeDispozitiv = null;
	NodObiect nodCelMaiAproapeDeDestinatie = null;
	
	Vector<NodObiect> noduri;
	boolean fixat = false;
	NodObiect noddestinatie;
	boolean anuntat = false;
	boolean anuntat1 = false;
	boolean anuntat2 = false;
	boolean anuntat3 = false;
	Anuntator anuntator;
	ImageView imagineanunt;
	TextView distantapanalaintersectie;
	Bitmap lastanga;
	Bitmap ladreapta;
	Bitmap limitadeviteza;
	Bitmap inainte;
	Bitmap intoarce;
	Bitmap destinatiebmp;
	Bitmap dreaptaprv;
	Bitmap stangaprv;
	boolean ladestinatie = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps);
		// GET DESTINATIE
		Bundle extras = getIntent().getExtras();
		destinatie = new Location("fals");
		if (extras != null) {
			destinatie.setLatitude(Float.parseFloat(extras.getString("lat")));
			destinatie.setLongitude(Float.parseFloat(extras.getString("lon")));
			iddestinatie = extras.getString("id");
		}
		noddestinatie = new NodObiect();
		noddestinatie.setId(iddestinatie);
		noddestinatie.setLocatie(destinatie);
		// ---------------------------GET DESTINATIE
		noade = new Vector<NodObiect>();
		ImageView asd = (ImageView) findViewById(R.id.imageView1);
		
		
		// AFISARE

		LayoutInflater linflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View informatii = linflater.inflate(R.layout.informatiigps, null);
		previewcam = new Preview(this);
		((FrameLayout) findViewById(R.id.preview)).addView(previewcam);
		mGLSurfaceView = new AugmentedSurfaceView(getApplicationContext(),
				new SageataRenderer(true));
		((FrameLayout) findViewById(R.id.preview)).addView(mGLSurfaceView);
		// emitemesaj("height:"+mGLSurfaceView.getHeight()+" width"+mGLSurfaceView.getWidth());
		((FrameLayout) findViewById(R.id.preview)).addView(informatii);
		datebusolatv = (TextView) findViewById(R.id.busola);
		data = (TextView) findViewById(R.id.data);
		distantatv = (TextView) findViewById(R.id.distanta1);
		viteza1 = (TextView) findViewById(R.id.viteza1);
		durata = (TextView) findViewById(R.id.durata);
		setaricamera = (ToggleButton) findViewById(R.id.setaricamera);
		strada = (TextView) findViewById(R.id.strada);
		strada.setVisibility(TextView.INVISIBLE);
		imagineanunt = (ImageView) findViewById(R.id.imagineanunt);
		distantapanalaintersectie = (TextView) findViewById(R.id.distantapanalaintersectie);

		strada.setText("Str. Muncel");

		// GPS
		manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locListener = new MyLocationListener();
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				Long.parseLong(timp), Float.valueOf(distanta), locListener);
		// --------------------------GPS

		// get rutare
		// get media img
		// get media audio
		// parcurge ruta
	}

	private void initializeaza() {
		anuntator = new Anuntator(this);
		// anuntator.inosutademetriiviratiladreapta();
		Bitmap bmp = BitmapFactory
				.decodeFile("/sdcard/gpsaugmentat/media/imagini/directionare/ladreapta.png");

		ladreapta = Bitmap.createScaledBitmap(bmp, 60, 60, false);
		bmp = BitmapFactory
				.decodeFile("/sdcard/gpsaugmentat/media/imagini/directionare/lastanga.png");

		lastanga = Bitmap.createScaledBitmap(bmp, 60, 60, false);
		bmp = BitmapFactory
				.decodeFile("/sdcard/gpsaugmentat/media/imagini/directionare/inainte.png");

		inainte = Bitmap.createScaledBitmap(bmp, 60, 60, false);
		bmp = BitmapFactory
				.decodeFile("/sdcard/gpsaugmentat/media/imagini/avertizari/limitadeviteza.png");
		limitadeviteza = Bitmap.createScaledBitmap(bmp, 60, 60, false);

		bmp = BitmapFactory
				.decodeFile("/sdcard/gpsaugmentat/media/imagini/avertizari/destinatie.png");
		destinatiebmp = Bitmap.createScaledBitmap(bmp, 60, 60, false);

		bmp = BitmapFactory
				.decodeFile("/sdcard/gpsaugmentat/media/imagini/avertizari/dreapta.png");
		dreaptaprv = Bitmap.createScaledBitmap(bmp, 60, 60, false);

		bmp = BitmapFactory
				.decodeFile("/sdcard/gpsaugmentat/media/imagini/avertizari/stanga.png");
		stangaprv = Bitmap.createScaledBitmap(bmp, 60, 60, false);

		// ORIENTARE
		ManagerBusola = (SensorManager) getSystemService(SENSOR_SERVICE);
		Busola = ManagerBusola.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		// ---------------------ORIENTARE

		// ACCELEROMETRU
		ManagerAccelerometru = (SensorManager) getSystemService(SENSOR_SERVICE);
		Accelerometru = ManagerAccelerometru
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		// ------------------ACCELEROMETRU

		// DATA SI ORA
		Thread threadora = null;
		Runnable runnable = new CountDownRunner();
		threadora = new Thread(runnable);
		threadora.start();
		// --------------------------------------DATA SI ORA

		// LISTENERI
		setaricamera.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (setaricamera.isChecked()) {
					Parameters parameters = previewcam.camera.getParameters();
					parameters.setColorEffect(previewcam.camera.getParameters()
							.getSupportedColorEffects().get(2));
					previewcam.camera.setParameters(parameters);

				} else {
					Parameters parameters = previewcam.camera.getParameters();
					parameters.setColorEffect(previewcam.camera.getParameters()
							.getSupportedColorEffects().get(0));
					previewcam.camera.setParameters(parameters);

				}
			}
		});
		/*
		 * unghi.setText(" X:"+Float.toString(event.values[0]*9.0f).substring(0,
		 * 2)+"º\n"+ " Y:" +Float.toString(event.values[1]*9.0f).substring(0,
		 * 2)+"º\n" + " Z:" +Float.toString(event.values[2]*9.0f).substring(0,
		 * 2)+"º\n");
		 */
		final SensorEventListener ListenerAccelerometru = new SensorEventListener() {
			public void onSensorChanged(SensorEvent event) {
				if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
					xacc = Float.toString(event.values[0] * 9.0f).substring(0,
							2);
					yacc = Float.toString(event.values[1] * 9.0f).substring(0,
							2);
					zacc = Float.toString(event.values[2] * 9.0f).substring(0,
							2);
					if (event.values[2] > 0 && event.values[0] * 9.0f > 0
							&& locatiecurenta.getSpeed() < 10) {
						mGLSurfaceView.cr.mAngleX = (90 - (int) (event.values[0] * 9.0f)) / 10;
						mGLSurfaceView.requestRender();
					} else if (event.values[2] < 0 && event.values[0] > 0
							&& locatiecurenta.getSpeed() < 10) {
						mGLSurfaceView.cr.mAngleX = -((90 - (int) (event.values[0] * 9.0f)) / 10);
						mGLSurfaceView.requestRender();
					} else {
						mGLSurfaceView.cr.mAngleX = 0;
						mGLSurfaceView.requestRender();
					}
				}
			}

			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub
			}
		};

		final SensorEventListener ListenerBusola = new SensorEventListener() {
			public void onSensorChanged(SensorEvent event) {
				if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {

					if (event.values[0] >= 338 || event.values[0] < 23) {
						datebusolatv.setTextColor(((250 << 24) & 0xFF000000)
								+ ((0 << 16) & 0x00FF0000)
								+ ((250 << 8) & 0x0000FF00) + (0 & 0x000000FF));
						if (event.values[0] >= 338) {
							schimbaculoare(event.values[0] - 270, 90, "E");
							datebusola = (int) event.values[0] - 270;
						}
						if (event.values[0] < 23) {
							datebusolatv
									.setTextColor(((250 << 24) & 0xFF000000)
											+ ((11 * ((int) event.values[0]) << 16) & 0x00FF0000)
											+ ((250 - (11 * (int) event.values[0]) << 8) & 0x0000FF00)
											+ (0 & 0x000000FF));
							datebusolatv.setText("<E\n"
									+ ((int) event.values[0] + 90) + "º");
							datebusola = (int) event.values[0] + 90;
						}
					}
					if (event.values[0] >= 23 && event.values[0] < 68) {
						schimbaculoare(event.values[0] + 90, 135, "SE");
						datebusola = (int) event.values[0] + 90;
					}
					if (event.values[0] >= 68 && event.values[0] < 113) {
						schimbaculoare(event.values[0] + 90, 180, "S");
						datebusola = (int) event.values[0] + 90;
					}
					if (event.values[0] >= 113 && event.values[0] < 158) {
						schimbaculoare(event.values[0] + 90, 225, "SV");
						datebusola = (int) event.values[0] + 90;
					}
					if (event.values[0] >= 158 && event.values[0] < 203) {
						schimbaculoare(event.values[0] + 90, 270, "V");
						datebusola = (int) event.values[0] + 90;
					}
					if (event.values[0] >= 203 && event.values[0] < 248) {
						schimbaculoare(event.values[0] + 90, 315, "NV");
						datebusola = (int) event.values[0] + 90;
					}
					if (event.values[0] >= 248 && event.values[0] < 293) {
						if (event.values[0] <= 270) {
							schimbaculoare(event.values[0] + 90, 359, "N");
							datebusola = (int) event.values[0] + 90;
						} else {
							schimbaculoare(event.values[0] - 270, 359, "N");
							datebusola = (int) event.values[0] - 270;
						}
					}
					if (event.values[0] >= 293 && event.values[0] < 338) {
						schimbaculoare(event.values[0] - 270, 45, "NE");
						datebusola = (int) event.values[0] - 270;
					}
				}
			}

			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub
			}
		};
		ManagerBusola.registerListener(ListenerBusola, Busola,
				SensorManager.SENSOR_DELAY_NORMAL);

		ManagerAccelerometru.registerListener(ListenerAccelerometru,
				Accelerometru, SensorManager.SENSOR_ACCELEROMETER);

		// ------------------LISTENERI
		// TODO Auto-generated method stub
		reconfigurareTraseu();
	}

	private void reconfigurareTraseu() {
		gmfld = new GeomagneticField((float) (locatiecurenta.getLatitude()),
				(float) (locatiecurenta.getLongitude()),
				(float) (locatiecurenta.getAltitude()),
				System.currentTimeMillis());

		// anuntator.incincizecidemetriiviratilastanga();
		AStarRenew astar = new AStarRenew();
		Incarcare i = new Incarcare();
		noade = i
				.incarcaDinXml("/sdcard/gpsaugmentat/harti/romania/constanta/noduri.xml");
		nodCelMaiAproapeDeDestinatie = noade.firstElement();
		nodCelMaiAproapeDeDispozitiv = noade.firstElement();
		for (NodObiect nodObiect : noade) {
			if (nodObiect.getLocatie().distanceTo(destinatie) < nodCelMaiAproapeDeDestinatie
					.getLocatie().distanceTo(destinatie))
				nodCelMaiAproapeDeDestinatie = nodObiect;
			if (nodObiect.getLocatie().distanceTo(locatiecurenta) < nodCelMaiAproapeDeDispozitiv
					.getLocatie().distanceTo(locatiecurenta))
				nodCelMaiAproapeDeDispozitiv = nodObiect;
		}
		emitemesaj("NODBEGIN:" + nodCelMaiAproapeDeDispozitiv.getId()
				+ " NODEND:" + nodCelMaiAproapeDeDestinatie.getId());

		nodBegin = nodCelMaiAproapeDeDispozitiv;
		nodEnd = nodCelMaiAproapeDeDestinatie;
		/*
		 * for(NodObiect n:noade) { //if(n.equals(nodCelMaiAproapeDeDispozitiv))
		 * nodBegin=n; //if(n.equals(nodCelMaiAproapeDeDestinatie)) nodEnd=n;
		 * if(n.getId().equals("64")) nodBegin=n; if(n.getId().equals("20"))
		 * nodEnd=n; if(nodBegin!=null&&nodEnd!=null) break; }
		 */
		ICalculDistantaNod del = new CalculDistanta();
		Ruta<NodObiect> ruta = astar.FindMinPath(nodBegin, nodEnd, del);
		NodObiect nod = ruta.getUltimulPas();
		String rutastring = "";
		noduri = new Vector<NodObiect>();

		while (ruta != null) {

			Location nodloc = new Location("fals");

			nod = ruta.getUltimulPas();
			if (!nod.getId().equals("1")) {
				noduri.add(nod);
				rutastring = rutastring + " - " + nod.getId();
			}
			ruta = ruta.UltimiiPasi();

		}
		Collections.reverse(noduri);

		nodulurmator = noduri.firstElement();
		anuntator.mergetilaprimaintersectie();
		emitemesaj(rutastring);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		try {
			previewcam.surfaceDestroyed(null);
			anuntator.close();
		} catch (Exception e) {
			finish();
		}
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
				try {

					Calendar c = Calendar.getInstance();
					data.setText(c.get(Calendar.HOUR_OF_DAY) + ":"
							+ c.get(Calendar.MINUTE) + ":"
							+ c.get(Calendar.SECOND) + " "
							+ c.get(Calendar.DAY_OF_MONTH) + "."
							+ c.get(Calendar.MONTH) + "."
							+ c.get(Calendar.YEAR));
				} catch (Exception e) {
				}
			}
		});
	}

	// -----------------clase adiacente

	public class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location loc) {
			if (!ladestinatie) {
				loc.getLatitude();
				loc.getLongitude();
				locatiecurenta = loc;

				if (!fixat) {
					fixat = true;
					initializeaza();
					if (nodulanterior == null) {
						nodulanterior = new NodObiect();
						nodulanterior.setLocatie(loc);
					}
				} else {

					if (anuntat
							&& loc.distanceTo(nodulurmator.getLocatie()) < 85) {
						anuntat = false;
					}
					if (anuntat1
							&& loc.distanceTo(nodulurmator.getLocatie()) < 40) {
						anuntat1 = false;
					}
					if (anuntat2
							&& loc.distanceTo(noddestinatie.getLocatie()) > 15) {
						anuntat2 = false;
					}
					if (anuntat3 && loc.getSpeed() < 50) {
						anuntat3 = false;
						imagineanunt.setImageBitmap(inainte);
					}
					if (loc.distanceTo(noddestinatie.getLocatie()) < 10
							&& !anuntat2) {
						ladestinatie = true;
						anuntat2 = true;
						anuntator.atiajunsladestinatie();
						emitemesaj("Ati ajuns la destinatie");
						/*
						 * LayoutInflater linflater = (LayoutInflater)
						 * getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						 * View pozapoi = linflater.inflate(R.layout.pozamare,
						 * null); ((FrameLayout)
						 * findViewById(R.id.preview)).addView(pozapoi);
						 * ImageView
						 * pozamarepoi=(ImageView)pozapoi.findViewById(
						 * R.id.pozamaredestinatiegui); Bitmap bmImg =
						 * BitmapFactory.decodeFile(
						 * "/sdcard/gpsaugmentat/harti/romania/constanta/poi/pozemari/"
						 * +noddestinatie.getId()+".jpg");
						 * pozamarepoi.setImageBitmap(bmImg);
						 */
						try {
							Thread.sleep(2500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						float bearingcatredestinatie = loc
								.bearingTo(noddestinatie.getLocatie());
						boolean negativ = false;
						if (bearingcatredestinatie < 0) {
							bearingcatredestinatie += 360;
							negativ = true;
						}
						if (bearingcatredestinatie < datebusola) {
							if (negativ) {
								imagineanunt.setImageBitmap(dreaptaprv);
								anuntator.privitiindreapta();
								emitemesaj("Priviti la dreapta");
							} else {
								imagineanunt.setImageBitmap(stangaprv);
								anuntator.privitiinstanga();
								emitemesaj("Priviti la stanga");
							}

						} else if (bearingcatredestinatie > datebusola) {
							if (negativ) {
								imagineanunt.setImageBitmap(stangaprv);
								anuntator.privitiinstanga();
								emitemesaj("Priviti la stanga");
							} else {
								imagineanunt.setImageBitmap(dreaptaprv);
								anuntator.privitiindreapta();
								emitemesaj("Priviti la dreapta");
							}

						} else {
							emitemesaj("Priviti inainte");
						}
						// in mod normal iau din directia punctului de interes
					}
					if (loc.distanceTo(noddestinatie.getLocatie()) <= 100 && distantatotala() > 98) {
						imagineanunt.setImageBitmap(destinatiebmp);
						anuntator.inosutademetriiajungetiladestinatie();

					}
					if (loc.distanceTo(nodulurmator.getLocatie()) <= 50
							&& loc.distanceTo(nodulurmator.getLocatie()) > 40
							&& !anuntat1 && !anuntat2) {
						NodObiect nodulafterurmator = null;
						if (noduri.lastIndexOf(nodulurmator) < noduri.size() - 1) {
							nodulafterurmator = noduri.elementAt(noduri
									.lastIndexOf(nodulurmator) + 1);
						}
						 boolean negativ=false;
						 float
						 bearingspreurmatorulpas=nodulurmator.getLocatie().bearingTo(nodulafterurmator.getLocatie());
						 float
						 bearingdelapasultrecut=nodulanterior.getLocatie().bearingTo(nodulurmator.getLocatie());
						 if(bearingspreurmatorulpas<0)
						 bearingspreurmatorulpas+=360;
						 if(bearingdelapasultrecut<0){
						 bearingdelapasultrecut+=360;
						 negativ=true;
						 }
						 if(bearingspreurmatorulpas-bearingdelapasultrecut<-30){
						 if(negativ){
						 anuntator.incincizecidemetriiviratilastanga();
						 imagineanunt.setImageBitmap(lastanga);
						 emitemesaj("In 50 m virati la stanga");
						 }
						 else{
						 anuntator.incincizecidemetriiviratiladreapta();
						 imagineanunt.setImageBitmap(ladreapta);
						 emitemesaj("In 50 m virati la dreapta");
						 }
						 }
						 else
						 if(bearingspreurmatorulpas-bearingdelapasultrecut>30){
						 if(negativ){
						 anuntator.incincizecidemetriiviratiladreapta();
						 imagineanunt.setImageBitmap(ladreapta);
						 emitemesaj("In 50 m virati la dreapta");
						 }
						 else{
						 anuntator.incincizecidemetriiviratilastanga();
						 imagineanunt.setImageBitmap(lastanga);
						 emitemesaj("In 50 m virati la stanga");
						 }
						
						 }
						 else{
						 anuntator.mergetimaideparte();
						 imagineanunt.setImageBitmap(inainte);
						 emitemesaj("Mergeti mai departe");
						 }
						 }//de aici
						 else{
						
						
						 nodulafterurmator=noddestinatie;
						 boolean negativ=false;
						 float
						 bearingspreurmatorulpas=nodulurmator.getLocatie().bearingTo(nodulafterurmator.getLocatie());
						 float
						 bearingdelapasultrecut=nodulanterior.getLocatie().bearingTo(nodulurmator.getLocatie());
						 if(bearingspreurmatorulpas<0)
						 bearingspreurmatorulpas+=360;
						 if(bearingdelapasultrecut<0){
						 bearingdelapasultrecut+=360;
						 negativ=true;
						 }
						 if(bearingspreurmatorulpas-bearingdelapasultrecut<-30){
						 if(negativ){
						 anuntator.incincizecidemetriiviratilastanga();
						 imagineanunt.setImageBitmap(lastanga);
						 emitemesaj("In 50 m virati la stanga");
						 }
						 else{
						 anuntator.incincizecidemetriiviratiladreapta();
						 imagineanunt.setImageBitmap(ladreapta);
						 emitemesaj("In 50 m virati la dreapta");
						 }
						 }
						 else
						 if(bearingspreurmatorulpas-bearingdelapasultrecut>30){
						 if(negativ){
						 anuntator.incincizecidemetriiviratilastanga();
						 imagineanunt.setImageBitmap(lastanga);
						 emitemesaj("In 512.00 m virati la stanga");
						 }
						 else{
						
						 anuntator.incincizecidemetriiviratiladreapta();
						 imagineanunt.setImageBitmap(ladreapta);
						 emitemesaj("In 50 m virati la dreapta");
						 }
						
						 }
						 else{
						
						 anuntator.mergetimaideparte();
						 imagineanunt.setImageBitmap(inainte);
						 emitemesaj("Mergeti mai departe");
						 }
						  negativ=false;
						 float
						 bearing=nodulurmator.getLocatie().bearingTo(destinatie);
						 if(bearing<0){
						 bearing+=360;
						 negativ=true;
						 }
						 if(datebusola-bearing>0){
						 anuntator.incincizecidemetriiviratilastanga();
						 imagineanunt.setImageBitmap(lastanga);
						 emitemesaj("In 100 m virati la stanga");
						 }
						 else if(datebusola-bearing<0){
						 anuntator.incincizecidemetriiviratiladreapta();
						 imagineanunt.setImageBitmap(ladreapta);
						 emitemesaj("In 100 m virati la dreapta");
						 }
						 else{
						 anuntator.mergetimaideparte();
						 imagineanunt.setImageBitmap(inainte);
						 emitemesaj("Mergeti mai departe");
						 }
						 }
						



						anuntat1 = true;
					}

				
					if (loc.distanceTo(nodulurmator.getLocatie()) <= 100
							&& loc.distanceTo(nodulurmator.getLocatie()) > 85
							&& !anuntat && !anuntat2) {
						NodObiect nodulafterurmator = null;
						if (noduri.lastIndexOf(nodulurmator) < noduri.size() - 1) {
							nodulafterurmator = noduri.elementAt(noduri
									.lastIndexOf(nodulurmator) + 1);
						}
						 boolean negativ=false;
						 float
						 bearingspreurmatorulpas=nodulurmator.getLocatie().bearingTo(nodulafterurmator.getLocatie());
						 float
						 bearingdelapasultrecut=nodulanterior.getLocatie().bearingTo(nodulurmator.getLocatie());
						 if(bearingspreurmatorulpas<0)
						 bearingspreurmatorulpas+=360;
						 if(bearingdelapasultrecut<0){
						 bearingdelapasultrecut+=360;
						 negativ=true;
						 }
						 if(bearingspreurmatorulpas-bearingdelapasultrecut<-30){
						 if(negativ){
						 anuntator.inosutademetriiviratilastanga();
						 imagineanunt.setImageBitmap(lastanga);
						 emitemesaj("In 100 m virati la stanga");
						 }
						 else{
						 anuntator.inosutademetriiviratiladreapta();
						 imagineanunt.setImageBitmap(ladreapta);
						 emitemesaj("In 100 m virati la dreapta");
						 }
						 }
						 else
						 if(bearingspreurmatorulpas-bearingdelapasultrecut>30){
						 if(negativ){
						 anuntator.inosutademetriiviratiladreapta();
						 imagineanunt.setImageBitmap(ladreapta);
						 emitemesaj("In 100 m virati la dreapta");
						 }
						 else{
						 anuntator.inosutademetriiviratilastanga();
						 imagineanunt.setImageBitmap(lastanga);
						 emitemesaj("In 100 m virati la stanga");
						 }
						
						 }
						 else{
						 anuntator.mergetimaideparte();
						 imagineanunt.setImageBitmap(inainte);
						 emitemesaj("Mergeti mai departe");
						 }
						 }//de aici
						 else{
						
						
						 nodulafterurmator=noddestinatie;
						 boolean negativ=false;
						 float
						 bearingspreurmatorulpas=nodulurmator.getLocatie().bearingTo(nodulafterurmator.getLocatie());
						 float
						 bearingdelapasultrecut=nodulanterior.getLocatie().bearingTo(nodulurmator.getLocatie());
						 if(bearingspreurmatorulpas<0)
						 bearingspreurmatorulpas+=360;
						 if(bearingdelapasultrecut<0){
						 bearingdelapasultrecut+=360;
						 negativ=true;
						 }
						 if(bearingspreurmatorulpas-bearingdelapasultrecut<-30){
						 if(negativ){
						 anuntator.inosutademetriiviratilastanga();
						 imagineanunt.setImageBitmap(lastanga);
						 emitemesaj("In 100 m virati la stanga");
						 }
						 else{
						 anuntator.inosutademetriiviratiladreapta();
						 imagineanunt.setImageBitmap(ladreapta);
						 emitemesaj("In 100 m virati la dreapta");
						 }
						 }
						 else
						 if(bearingspreurmatorulpas-bearingdelapasultrecut>30){
						 if(negativ){
						 anuntator.inosutademetriiviratiladreapta();
						 imagineanunt.setImageBitmap(ladreapta);
						 emitemesaj("In 100 m virati la dreapta");
						 }
						 else{
						 anuntator.inosutademetriiviratilastanga();
						 imagineanunt.setImageBitmap(lastanga);
						 emitemesaj("In 100 m virati la stanga");
						 }
						
						 }
						 else{
						 anuntator.mergetimaideparte();
						 imagineanunt.setImageBitmap(inainte);
						 emitemesaj("Mergeti mai departe");
						 }
						  negativ=false;
						 float
						 bearing=nodulurmator.getLocatie().bearingTo(destinatie);
						 if(bearing<0){
						 bearing+=360;
						 negativ=true;
						 }
						 if(datebusola-bearing>0){
						 anuntator.incincizecidemetriiviratilastanga();
						 imagineanunt.setImageBitmap(lastanga);
						 emitemesaj("In 100 m virati la stanga");
						 }
						 else if(datebusola-bearing<0){
						 anuntator.incincizecidemetriiviratiladreapta();
						 imagineanunt.setImageBitmap(ladreapta);
						 emitemesaj("In 100 m virati la dreapta");
						 }
						 else{
						 anuntator.mergetimaideparte();
						 imagineanunt.setImageBitmap(inainte);
						 emitemesaj("Mergeti mai departe");
						 }
						 }
						anuntat = true;
					}
					
					if (loc.distanceTo(nodulurmator.getLocatie()) <= 10
							&& !anuntat2) {
						// daca distanta pana la nodul urmato este <=50m atunci
						// schimb nodul

						NodObiect nodulafterurmator = null;
						if (noduri.lastIndexOf(nodulurmator) < noduri.size() - 1) {
							nodulafterurmator = noduri.elementAt(noduri
									.lastIndexOf(nodulurmator) + 1);
							nodulanterior = nodulurmator;
							nodulurmator = nodulafterurmator;
							// anuntator.mergetimaideparte(loc.distanceTo(nodulurmator.getLocatie()));
							imagineanunt.setImageBitmap(inainte);
							anuntator.mergetimaideparte();
							emitemesaj("Mergeti mai departe"
									+ loc.distanceTo(nodulurmator.getLocatie()));
						} else {
							nodulanterior = nodulurmator;

							nodulurmator = noddestinatie;
							anuntator.mergetimaideparte();
							imagineanunt.setImageBitmap(inainte);
							anuntator.mergetimaideparte();
							emitemesaj("Mergeti mai departe"
									+ loc.distanceTo(nodulurmator.getLocatie()));
						}
					}
					
					  if(nodulanterior!=null && nodulurmator!=null){
					  if(datebusola
					  -locatiecurenta.bearingTo(nodulurmator.getLocatie
					  ())+gmfld
					  .getDeclination()>135||datebusola-locatiecurenta.
					  bearingTo
					  (nodulurmator.getLocatie())+gmfld.getDeclination()<-135){
					  // emitemesaj("Reconfigurarea traseului"); //
					  reconfigurareTraseu();
					  anuntator.reconfigurareatraseului(); } }
					 
//					 daca distanta pana la nodul urmato este <=50m atunci
//					 schimb nodul
//					 daca distanta pana la destinatie este <=100m atunci anunt
//					 daca a ajuns la destinatie anunt
//					 calculez distanta totala
					 if(loc.getSpeed()*3.6>3){
					  float bearingDirectie1=nodulanterior.getLocatie().bearingTo(nodulurmator.getLocatie());
					  float bearingDirectie2=locatiecurenta.bearingTo(nodulurmator.getLocatie());
					  if(bearingDirectie1<0) bearingDirectie1+=360;
					  if(bearingDirectie2<0) bearingDirectie2+=360;
					 float
					 newBearing=nodulanterior.getLocatie().bearingTo(loc);
					 float
					 newBearing1=nodulanterior.getLocatie().bearingTo(nodulurmator.getLocatie());
					 if(newBearing<0) newBearing+=360;
					 if(newBearing1<0) newBearing1+=360;
					
					// mGLSurfaceView.cr.mAngleY=(newBearing1-newBearing)/10;
					// mGLSurfaceView.requestRender();
					// }

					if (loc.getSpeed() * 3.6 > 50 && !anuntat3) {
						anuntat3 = true;
						imagineanunt.setImageBitmap(limitadeviteza);
						anuntator.limitadeviteza();
						// emitemesaj("ati depasit limita de viteza");
					}
					if (!anuntat2)
						distantatv.setText(Math.round(distantatotala()) + " m");
					viteza1.setText(String.valueOf(loc.getSpeed() * 3.6)
							+ " KM/H");

					durata.setText(String.valueOf(Math.round(loc
							.distanceTo(destinatie) / (loc.getSpeed() * 60)))
							+ " MINUTE");
					distantapanalaintersectie.setText(String.valueOf(loc
							.distanceTo(nodulurmator.getLocatie())));
				}
			}
		

		public void onProviderDisabled(String provider) {
			emitemesaj("Gps dezactivat");
			fixat = false;
		}

		public void onProviderEnabled(String provider) {
			emitemesaj("Gps activat");
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			emitemesaj("Provider: " + provider + " Status: " + status);

		}

	}

	private double distantatotala() {
		double distantatotala = 0;
		try {
			if (!noduri.lastElement().equals(nodulurmator) && !anuntat2) {

				for (int i = noduri.lastIndexOf(nodulurmator); i < noduri
						.size(); i++) {
					distantatotala += noduri.elementAt(i).getLocatie()
							.distanceTo(noduri.elementAt(i + 1).getLocatie());
				}
				distantatotala += locatiecurenta.distanceTo(nodulurmator
						.getLocatie());
				distantatotala += noduri.lastElement().getLocatie()
						.distanceTo(noddestinatie.getLocatie());
			} else if (noduri.lastElement().equals(nodulurmator)) {
				distantatotala += locatiecurenta.distanceTo(nodulurmator
						.getLocatie());
				distantatotala += nodulurmator.getLocatie().distanceTo(
						noddestinatie.getLocatie());
			} else
				distantatotala = locatiecurenta.distanceTo(noddestinatie
						.getLocatie());
		} catch (Exception e) {
		}
		return distantatotala;
	}

	private void emitemesaj(String mesaj) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), mesaj, Toast.LENGTH_LONG)
				.show();
	}

	public void schimbaculoare(float unghi, int valoareoptima,
			String punctcardinal) {
		int dif = valoareoptima - (int) unghi;
		if (dif == 0) {
			datebusolatv.setTextColor(((250 << 24) & 0xFF000000)
					+ ((0 << 16) & 0x00FF0000) + ((250 << 8) & 0x0000FF00)
					+ (0 & 0x000000FF));
			datebusolatv.setText(">" + punctcardinal + "<\n" + (int) unghi
					+ "º");
			mGLSurfaceView.cr.mAngleY = (datebusola
					- locatiecurenta.bearingTo(nodulurmator.getLocatie()) + gmfld
					.getDeclination()) / 10;
			mGLSurfaceView.requestRender();
		}
		if (dif < 0) {
			datebusolatv.setTextColor(((250 << 24) & 0xFF000000)
					+ ((11 * (-dif) << 16) & 0x00FF0000)
					+ ((250 - (11 * (-dif)) << 8) & 0x0000FF00)
					+ (0 & 0x000000FF));
			datebusolatv
					.setText("<" + punctcardinal + "\n" + (int) unghi + "º");
			mGLSurfaceView.cr.mAngleY = (datebusola
					- locatiecurenta.bearingTo(nodulurmator.getLocatie()) + gmfld
					.getDeclination()) / 10;
			mGLSurfaceView.requestRender();
		}
		if (dif > 0) {
			datebusolatv
					.setTextColor(((250 << 24) & 0xFF000000)
							+ ((11 * dif << 16) & 0x00FF0000)
							+ ((250 - (11 * dif) << 8) & 0x0000FF00)
							+ (0 & 0x000000FF));
			datebusolatv.setText(punctcardinal + ">\n" + (int) unghi + "º");
			mGLSurfaceView.cr.mAngleY = (datebusola
					- locatiecurenta.bearingTo(nodulurmator.getLocatie()) + gmfld
					.getDeclination()) / 10;
			mGLSurfaceView.requestRender();
		}
		directiecurenta = String.valueOf(unghi);
	}

	class CountDownRunner implements Runnable {
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					afiseazaora();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} catch (Exception e) {
				}
			}
		}
	}
}
