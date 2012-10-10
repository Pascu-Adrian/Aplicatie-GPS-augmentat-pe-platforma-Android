package gps.augmentat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageButton;


public class GPSaugmentatActivity extends Activity {
    /** Called when the activity is first created. */
	ImageButton inchide;
	ImageButton capturapoi;
	ImageButton POI;
	ImageButton GPS;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        //initializare
        
        inchide=(ImageButton)findViewById(R.id.INCHIDEBUTTON);
    	capturapoi=(ImageButton)findViewById(R.id.SETARIBUTTON);
        POI=(ImageButton)findViewById(R.id.POIBUTTON);
        GPS=(ImageButton)findViewById(R.id.GPSBUTTON);
        
        //-------------------------------------------------------------------------initializare
        
        
        
        
        
        
        //listeneri
        POI.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent nextScreen = new Intent(getApplicationContext(), POIactivity.class);
				startActivity(nextScreen);
			}
		});
        
GPS.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent nextScreen = new Intent(getApplicationContext(), POIAUGMENTAT.class);
				//Intent nextScreen = new Intent(getApplicationContext(), GPSnavigatie.class);
				Intent nextScreen = new Intent(getApplicationContext(), Draw.class);	
				startActivity(nextScreen);
			}
		});
        
        capturapoi.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stu
				Intent nextScreen = new Intent(getApplicationContext(), CapturaPOI.class);
				startActivity(nextScreen);
			}
		});
        
        inchide.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				// TODO Auto-generated method stub			
				finish();
			}
		}); 
        
        //---------------------------------------------------------------listeneri
        
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
}