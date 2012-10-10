package gps.augmentat;

import java.util.Vector;

import android.R.anim;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.GeomagneticField;
import android.location.Location;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

public class DrawView extends View implements OnTouchListener {
	WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
	Display display = wm.getDefaultDisplay(); 
	int width;
	int height;
	Location locatiedispozitiv;
	GeomagneticField gmf;
	Vector<POIobiect> listapoi;
	Vector<AfisajPOI> listaicoane;
	Context context;
	float diferenta;
	GeomagneticField gomag;
	long timer=-1;
	float ultimavaloarebusola=-1;
	final Dialog dialog;
	Button mergila;
	Button anuleaza;
	TextView nume;
	TextView tvLong,tvLat;
	ImageView imagine;
	EditText descriere;
	Paint paint = new Paint();
	Intent nextScreen;
	Preview previewcam;
	int countspredreapta=0;
	int countsprestanga=0;
	Vector<Animator> listaanimatori;
	public DrawView(Context context, Preview previewcam) {
		// TODO Auto-generated constructor stub
		super(context);
		setFocusable(true);
		setFocusableInTouchMode(true);
       this.setOnTouchListener(this);
       paint.setAntiAlias(true);
       paint.setColor(Color.RED);
       paint.setFakeBoldText(true);
       this.previewcam=previewcam;
       this.listaanimatori=new Vector<DrawView.Animator>();
       nextScreen = new Intent(context, GPSnavigatie.class);
		listapoi=new Vector<POIobiect>();
		listaicoane=new Vector<AfisajPOI>();
		locatiedispozitiv=new Location("fals");
		dialog=new Dialog(context);
	    dialog.setContentView(R.layout.dialogpoiiselectarepoiaugmentat);
	    dialog.setCancelable(true);
	    nume=(TextView)dialog.findViewById(R.id.numedialogpoiaugmentat);
	    imagine=(ImageView)dialog.findViewById(R.id.imaginedialogpoiaugmentat);
	    descriere=(EditText)dialog.findViewById(R.id.descrieredialogpoiaugmentat);
	    mergila=(Button)dialog.findViewById(R.id.mergiladialogpoiaugmentat);
	    anuleaza=(Button)dialog.findViewById(R.id.inchidedialogpoiaugmentat);
	    
		gomag=new GeomagneticField((float)(locatiedispozitiv.getLatitude()), (float)(locatiedispozitiv.getLongitude()), (float)(locatiedispozitiv.getAltitude()), System.currentTimeMillis());
	}
	public DrawView(Context context,Vector<POIobiect> listapoi, Preview previewcam) {
		super(context);
		this.context=context;
		this.listapoi=listapoi;
		nextScreen = new Intent(context, GPSnavigatie.class);
		listaicoane=new Vector<AfisajPOI>();
		dialog=new Dialog(context);
	    dialog.setContentView(R.layout.dialogpoiiselectarepoiaugmentat);
	    dialog.setCancelable(true);
	    tvLat.setText(R.id.tvLat);
	    tvLong.setText(R.id.tvLong);
	    this.listaanimatori=new Vector<DrawView.Animator>();
	    nume=(TextView)dialog.findViewById(R.id.numedialogpoiaugmentat);
	    imagine=(ImageView)dialog.findViewById(R.id.imaginedialogpoiaugmentat);
	    descriere=(EditText)dialog.findViewById(R.id.descrieredialogpoiaugmentat);
	    mergila=(Button)dialog.findViewById(R.id.mergiladialogpoiaugmentat);
	    anuleaza=(Button)dialog.findViewById(R.id.inchidedialogpoiaugmentat);
	    this.previewcam=previewcam;
		//Point size = new Point();  
		width =display.getWidth(); 
		height = display.getHeight();
		setFocusable(true);
		setFocusableInTouchMode(true);
       this.setOnTouchListener(this);
       paint.setAntiAlias(true);
       paint.setColor(Color.RED);
       paint.setFakeBoldText(true);

       } 
	public void setListaicoane(Vector<AfisajPOI> listaicoane) {
		this.listaicoane = listaicoane;
	}
	public void setListapoi(Vector<POIobiect> listapoi) {
		this.listapoi = listapoi;
		
		afiseazapoi();
		Toast.makeText( context,"SUNT:"+listapoi.size(), Toast.LENGTH_LONG ).show(); 
	}
	public void afiseazapoi() {
		listaicoane=new Vector<AfisajPOI>();
		float x=0, y = 1,deviatie = 0;
		int size = 1;
		AfisajPOI afjpoi;
		for (POIobiect poi : listapoi) {
			afjpoi=new AfisajPOI();
			if(locatiedispozitiv.distanceTo(poi.getLocatie())>20&&locatiedispozitiv.distanceTo(poi.getLocatie())<=100){
	    		  // y=height-(7*height)/8;
				y=height/2;
	    		   size=60;
	    	   }
	    	   if(locatiedispozitiv.distanceTo(poi.getLocatie())>100&&locatiedispozitiv.distanceTo(poi.getLocatie())<=300){
	    		  // y=height-(3*height)/4;
	    		   y=height/2;
	    		   size=50;
	    	   }
	    	   if(locatiedispozitiv.distanceTo(poi.getLocatie())>300&&locatiedispozitiv.distanceTo(poi.getLocatie())<=600){
	    		  // y=height-(5*height)/8;
	    		   size=30;
	    		   y=height/2;
	    	   }
	    	   if(locatiedispozitiv.distanceTo(poi.getLocatie())>600&&locatiedispozitiv.distanceTo(poi.getLocatie())<=1000){
	    		  // y=height-(9*height)/16;
	    		   size=20;
	    		   y=height/2;
	    	   }
	    	   if(locatiedispozitiv.distanceTo(poi.getLocatie())<20){
	    		  // y=height-(7*height)/8;
	    		   y=height/2;
	    		   size=100;
	    	   }
	    	   /*else{
	    		   //y=height-(9*height)/16;
	    		   y=height/2;
	    		   size=1;
	    	   }*/
			deviatie=locatiedispozitiv.bearingTo(poi.getLocatie());	
			if(deviatie<0)
				deviatie=360+deviatie;
			afjpoi=new AfisajPOI(x, y, size, deviatie, poi);
			afjpoi.getIcoanaafisata().setBounds(width/2,
					Math.round(height/2),
					Math.round(width/2+afjpoi.getSize()),
					Math.round(height/2+afjpoi.getSize()));
			listaicoane.add(afjpoi);
		}
		invalidate();	
	}


	public void schimbalocatie(Location locatie){
		this.locatiedispozitiv=locatie;
		
	}
	public void schimbadirectie(float valoarebusola){
		gomag=new GeomagneticField((float)(locatiedispozitiv.getLatitude()), (float)(locatiedispozitiv.getLongitude()), (float)(locatiedispozitiv.getAltitude()), System.currentTimeMillis());

		
		if(timer==-1)
			timer=System.currentTimeMillis();
		if(ultimavaloarebusola==-1)
			ultimavaloarebusola=valoarebusola;
		if(System.currentTimeMillis()-timer>1000&&Math.abs(ultimavaloarebusola-valoarebusola)>5){
			timer=System.currentTimeMillis();
			ultimavaloarebusola=valoarebusola;
			countspredreapta=0;
			countsprestanga=0;
		for (AfisajPOI icoana : listaicoane) {
			diferenta=valoarebusola-icoana.getDeviatie()+gomag.getDeclination();
			int unghimaxim=30;
			int unghiminim=-30;
			//Animator anima=new Animator();
			if(diferenta<=unghimaxim+1&& diferenta>=unghiminim-1){
				//anima=new Animator(icoana,((width/(2*unghimaxim))*diferenta*-1)+(width/2));
				//if(!listaanimatori.contains(anima)){
				//listaanimatori.add(anima);
				//anima.start();
				//}
				//icoana.setX(((width/90)*(90-diferenta))+icoana.getSize());
				//float distanta=locatiedispozitiv.distanceTo(icoana.getPunctdeinteres().getLocatie());
				//icoana.setX(width-(float) (((width/distanta)*diferenta)+(width/2)));
				//icoana.setX((width/(2*diferenta))+(width/2));
				icoana.setX(((width/(2*unghimaxim))*diferenta*-1)+(width/2));
				//icoana.setX((float)((Math.sin(diferenta)*width/2*Math.sin(45))+width/2));
				//icoana.setX((float)(((Math.sin(diferenta)*width)/(2*Math.sin(45)))+width/2));
				//float distanta=locatiedispozitiv.distanceTo(icoana.getPunctdeinteres().getLocatie());
				//double sin45=Math.sin(40);
				//icoana.setX(width-(float) (((width*diferenta)+(sin45*distanta))/(2*sin45*distanta)));
				icoana.getIcoanaafisata().setBounds(Math.round(icoana.getX()),
						Math.round(icoana.getY()),
						Math.round(icoana.getX()+icoana.getSize()),
						Math.round(icoana.getY()+icoana.getSize()));
				//icoana.setX(((width/90)*(90-diferenta))+icoana.getSize());
			}
			if(diferenta<unghiminim&&diferenta>-180) {
				//anima=new Animator(icoana,-200);
				//anima.start();
				countspredreapta++;
				icoana.setX(width+150);
				icoana.getIcoanaafisata().setBounds(Math.round(icoana.getX()),
						Math.round(icoana.getY()),
						Math.round(icoana.getX()+icoana.getSize()),
						Math.round(icoana.getY()+icoana.getSize()));
				//icoana.setX(-100);
			}	
			if(diferenta>unghimaxim&&diferenta<180) {
				//anima=new Animator(icoana,-200);
				//anima.start();
				countsprestanga++;
				icoana.setX(-150);
				icoana.getIcoanaafisata().setBounds(Math.round(icoana.getX()),
						Math.round(icoana.getY()),
						Math.round(icoana.getX()+icoana.getSize()),
						Math.round(icoana.getY()+icoana.getSize()));
				//icoana.setX(-100);
			}
			}
	}
			
			invalidate();
	}
	public float inclinatiedindistanta(POIobiect poipoi){
		float diferenta=0;
		if(locatiedispozitiv.distanceTo(poipoi.getLocatie())>20&&locatiedispozitiv.distanceTo(poipoi.getLocatie())<=100){
			diferenta= height-(7*height)/8;
  	   }
  	   if(locatiedispozitiv.distanceTo(poipoi.getLocatie())>100&&locatiedispozitiv.distanceTo(poipoi.getLocatie())<=300){
  		 diferenta= height-(3*height)/4;
  		   
  	   }
  	   if(locatiedispozitiv.distanceTo(poipoi.getLocatie())>300&&locatiedispozitiv.distanceTo(poipoi.getLocatie())<=600){
  		 diferenta= height-(5*height)/8;
  		   
  	   }
  	   if(locatiedispozitiv.distanceTo(poipoi.getLocatie())>600&&locatiedispozitiv.distanceTo(poipoi.getLocatie())<=1000){
  		 diferenta= height-(9*height)/16;
  		   
  	   }
  	   if(locatiedispozitiv.distanceTo(poipoi.getLocatie())<20){
  		 diferenta= height-(7*height)/9;
  		   
  	   }
	return diferenta;

	}
	public void schimbainclinatie(float valoareaccelerometru){
		float corectie=0;
		for (AfisajPOI icoana : listaicoane) {
			//Animator anima=new Animator();
		if(valoareaccelerometru<0){
			corectie=2*icoana.getSize();
		}
		if(valoareaccelerometru>0){
			corectie=2*icoana.getSize()*-1;
		}
		//anima=new Animator(height-(((height/10)*valoareaccelerometru)+(height/2))-inclinatiedindistanta(icoana.getPunctdeinteres()),icoana);
		//anima.start();
		icoana.setY(height-(((height/10)*valoareaccelerometru)+(height/2))-inclinatiedindistanta(icoana.getPunctdeinteres())+height/4);
		icoana.getIcoanaafisata().setBounds(Math.round(icoana.getX()),
				Math.round(icoana.getY()),
				Math.round(icoana.getX()+icoana.getSize()),
				Math.round(icoana.getY()+icoana.getSize()));
		}
		invalidate();
	}


	@Override 
	public void onDraw(Canvas canvas) {
		for (AfisajPOI icoana : listaicoane) {			
			icoana.getIcoanaafisata().draw(canvas);
			canvas.drawText(icoana.getPunctdeinteres().getNume(), icoana.getX()-8, icoana.getY()-8, paint);
			
			
		}
		}   
	
	public boolean onTouch(View view, MotionEvent event) { 	
		for (AfisajPOI icoanapoi : listaicoane) {
			if(icoanapoi.esteselectat(event.getRawX(), event.getRawY())){
			afiseazadialog(icoanapoi.getPunctdeinteres());
			
			break;
			}
		} 		
		return true;  
		}
	
	private void afiseazadialog(final POIobiect punctdeinteres) {
		// TODO Auto-generated method stub
		dialog.show();
	    
	    dialog.setTitle(punctdeinteres.getNume()+" - "+punctdeinteres.getTippoi());
	    nume.setText(punctdeinteres.getNume()+" - "+punctdeinteres.getTippoi());
	    imagine.setImageBitmap(punctdeinteres.getImaginemica());
	    tvLat.setText(Double.toString(punctdeinteres.getLocatie().getLatitude()));
	    tvLong.setText(Double.toString(punctdeinteres.getLocatie().getLongitude()));
	    descriere.setText(punctdeinteres.getDescriere());
	    /*mergila.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.hide();
				previewcam.surfaceDestroyed(null);
				
				nextScreen.putExtra("lat", String.valueOf(punctdeinteres.getLocatie().getLatitude()));
				nextScreen.putExtra("lon", String.valueOf(punctdeinteres.getLocatie().getLongitude()));
				nextScreen.putExtra("id", punctdeinteres.getId());	
				context.startActivity(nextScreen);
				//Toast.makeText( context,"MERGI LA!"+punctdeinteres.getNume(), Toast.LENGTH_SHORT ).show();
			}
		});*/
	    anuleaza.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
			dialog.hide();	
			}
		});
	    
	}
	public class Animator extends Thread{
		AfisajPOI afp;
		float tox;
		float toy;
		int timp=5;
		public Animator() {
			// TODO Auto-generated constructor stub
		}
	public Animator(AfisajPOI afp,float tox) {
		// TODO Auto-generated constructor stub
		this.afp=afp;
		this.tox=tox;
	}

	public void setBOUNDS(){
				afp.getIcoanaafisata().setBounds(Math.round(afp.getX()),
				Math.round(afp.getY()),
				Math.round(afp.getX()+afp.getSize()),
				Math.round(afp.getY()+afp.getSize()));
				invalidate();
	}
	@Override
		public void run() {
			// TODO Auto-generated method stub
		if(afp.getX()<tox){
			while(afp.getX()<tox){
				afp.setX(afp.getX()+1);
				try {
					Thread.sleep(timp);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			invalidate();
		}
		if(afp.getX()>tox){
			while(afp.getX()>tox){
				afp.setX(afp.getX()-1);
				try {
					Thread.sleep(timp);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			invalidate();
		}
		
		}
	}
	}
