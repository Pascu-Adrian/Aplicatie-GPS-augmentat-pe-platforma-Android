package gps.augmentat.util;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.Media;

public class Anuntator {
private MediaPlayer player;
Context context;
public Anuntator(Context context) {
	// TODO Auto-generated constructor stub
	player=new MediaPlayer();
	this.context=context;

	
}
public void limitadeviteza(){
	try{
	FileDescriptor fd=new FileDescriptor();
	FileInputStream fis = new FileInputStream("/sdcard/gpsaugmentat/media/sunete/avertizari/atidepasitlimitadeviteza.mp3"); 
    fd = fis.getFD(); 
    player.reset();
    player.setDataSource(fd);
    player.prepare();
    player.start();
	}
	catch(Exception e){
		
	}

}
public void atiajunsladestinatie(){
	try{
		FileDescriptor fd=new FileDescriptor();
		FileInputStream fis = new FileInputStream("/sdcard/gpsaugmentat/media/sunete/avertizari/atiajunsladestinatie.mp3"); 
	    fd = fis.getFD();
	    player.reset();
	    player.setDataSource(fd);
	    player.prepare();
	    player.start();
		}
		catch(Exception e){
			
		}
}
public void reconfigurareatraseului(){
	try{
		FileDescriptor fd=new FileDescriptor();
		FileInputStream fis = new FileInputStream("/sdcard/gpsaugmentat/media/sunete/avertizari/reconfigurareatraseului.mp3"); 
	    fd = fis.getFD(); 
	    player.reset();
	    player.setDataSource(fd);
	    player.prepare();
	    player.start();
		}
		catch(Exception e){
			
		}
}
public void privitiinstanga() {
	// TODO Auto-generated method stub
	try{
		FileDescriptor fd=new FileDescriptor();
		FileInputStream fis = new FileInputStream("/sdcard/gpsaugmentat/media/sunete/avertizari/privitiinstanga.mp3"); 
	    fd = fis.getFD(); 
	    player.reset();
	    player.setDataSource(fd);
	    player.prepare();
	    player.start();
		}
		catch(Exception e){
			
		}
}
public void privitiindreapta() {
	// TODO Auto-generated method stub
	try{
		FileDescriptor fd=new FileDescriptor();
		FileInputStream fis = new FileInputStream("/sdcard/gpsaugmentat/media/sunete/avertizari/privitiinstanga.mp3"); 
	    fd = fis.getFD(); 
	    player.reset();
	    player.setDataSource(fd);
	    player.prepare();
	    player.start();
		}
		catch(Exception e){
			
		}
}
public void inosutademetriiviratilastanga() {
	// TODO Auto-generated method stub
	try{
		FileDescriptor fd=new FileDescriptor();
		FileInputStream fis = new FileInputStream("/sdcard/gpsaugmentat/media/sunete/directionare/in100demetriiviratilastanga.mp3"); 
	    fd = fis.getFD(); 
	    player.reset();
	    player.setDataSource(fd);
	    player.prepare();
	    player.start();
		}
		catch(Exception e){
			
		}
}
public void mergetimaideparte() {
	// TODO Auto-generated method stub
	try{
		FileDescriptor fd=new FileDescriptor();
		FileInputStream fis = new FileInputStream("/sdcard/gpsaugmentat/media/sunete/directionare/mergetimaideparte.mp3"); 
	    fd = fis.getFD(); 
	    player.reset();
	    player.setDataSource(fd);
	    player.prepare();
	    player.start();
		}
		catch(Exception e){
			
		}
}
public void inosutademetriiviratiladreapta() {
	try{
		FileDescriptor fd=new FileDescriptor();
		FileInputStream fis = new FileInputStream("/sdcard/gpsaugmentat/media/sunete/directionare/mergetimaideparte.mp3"); 
	    fd = fis.getFD(); 
	    player.reset();
	    player.setDataSource(fd);
	    player.prepare();
	    player.start();
		}
		catch(Exception e){
			
		}
	// TODO Auto-generated method stub
	//try{
		/*FileDescriptor fd=new FileDescriptor();
		FileInputStream fis = new FileInputStream("/sdcard/gpsaugmentat/media/sunete/distante/in.mp3"); 
	    fd = fis.getFD(); 
	    player.setDataSource(fd);
	    player.prepare();
	    player.start();
	    
	    fis = new FileInputStream("/sdcard/gpsaugmentat/media/sunete/distante/o.mp3");
	    fd = fis.getFD(); 
	    player.setDataSource(fd);
	    player.prepare();
	    player.start();
	    fis = new FileInputStream("/sdcard/gpsaugmentat/media/sunete/distante/suta.mp3"); 
	    fd = fis.getFD(); 
	    player.setDataSource(fd);
	    player.prepare();
	    player.start();
	    fis = new FileInputStream("/sdcard/gpsaugmentat/media/sunete/distante/demetrii.mp3");
	    fd = fis.getFD(); 
	    player.setDataSource(fd);
	    player.prepare();
	    player.start();
	    fis = new FileInputStream("/sdcard/gpsaugmentat/media/sunete/directionare/virati.mp3");
	    fd = fis.getFD(); 
	    player.setDataSource(fd);
	    player.prepare();
	    player.start();
	    fis = new FileInputStream("/sdcard/gpsaugmentat/media/sunete/directionare/ladreapta.mp3");; 
	    fd = fis.getFD(); 
	    player.setDataSource(fd);
	    player.prepare();
	    player.start();*/
		/*AudioManager mgr = (AudioManager)context.getSystemService(context.AUDIO_SERVICE);
	    SoundPool sp = new SoundPool(1, mgr.STREAM_MUSIC, 0);
	    List<Integer> listasunete=new ArrayList<Integer>();
	    
	    listasunete.add(sp.load("/sdcard/gpsaugmentat/media/sunete/distante/in.mp3", 1));
	    listasunete.add(sp.load("/sdcard/gpsaugmentat/media/sunete/distante/o.mp3", 1));
	    listasunete.add(sp.load("/sdcard/gpsaugmentat/media/sunete/distante/suta.mp3", 1));
	    listasunete.add(sp.load("/sdcard/gpsaugmentat/media/sunete/distante/demetrii.mp3", 1));
	    listasunete.add(sp.load("/sdcard/gpsaugmentat/media/sunete/directionare/virati.mp3", 1));
	    listasunete.add(sp.load("/sdcard/gpsaugmentat/media/sunete/directionare/ladreapta.mp3", 1));
	    
	    
	    

	    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC); 
	    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  
	    float volume = streamVolumeCurrent / streamVolumeMax;   
	    for (Integer integer : listasunete) {
	    	sp.play(integer, streamVolumeMax, streamVolumeMax, 1, 1, 1);
		}
	}
		catch(Exception e){
			
		}*/
}
public void mergetimaideparte(float distanceTo) {
	// TODO Auto-generated method stub
	/*try{
		FileDescriptor fd=new FileDescriptor();
		FileInputStream fis = new FileInputStream("/sdcard/gpsaugmentat/media/sunete/avertizari/atidepasitlimitadeviteza.mp3"); 
	    fd = fis.getFD(); 
	    player.setDataSource(fd);
	    player.prepare();
	    player.start();
		}
		catch(Exception e){
			
		}*/
}
public void inosutademetriiajungetiladestinatie() {
	// TODO Auto-generated method stub
	try{
		FileDescriptor fd=new FileDescriptor();
		FileInputStream fis = new FileInputStream("/sdcard/gpsaugmentat/media/sunete/avertizari/in100demetriiajungetiladestinatie.mp3"); 
	    fd = fis.getFD(); 
	    player.reset();
	    player.setDataSource(fd);
	    player.prepare();
	    player.start();
		}
		catch(Exception e){
			
		}
}
public void close() {
	// TODO Auto-generated method stub
	player.release();
}
public void mergetilaprimaintersectie() {
	// TODO Auto-generated method stub
	try{
		FileDescriptor fd=new FileDescriptor();
		FileInputStream fis = new FileInputStream("/sdcard/gpsaugmentat/media/sunete/directionare/mergetilaprimaintersectie.mp3"); 
	    fd = fis.getFD(); 
	    player.reset();
	    player.setDataSource(fd);
	    player.prepare();
	    player.start();
		}
		catch(Exception e){
			
		}
}
public void incincizecidemetriiviratiladreapta() {
	// TODO Auto-generated method stub
	try{
		FileDescriptor fd=new FileDescriptor();
		FileInputStream fis = new FileInputStream("/sdcard/gpsaugmentat/media/sunete/directionare/in50demetriiviratiladreapta.mp3");  
	    fd = fis.getFD(); 
	    player.reset();
	    player.setDataSource(fd);
	    player.prepare();
	    player.start();
		}
		catch(Exception e){
			
		}
}
public void incincizecidemetriiviratilastanga() {
	// TODO Auto-generated method stub
	try{
		FileDescriptor fd=new FileDescriptor();
		FileInputStream fis = new FileInputStream("/sdcard/gpsaugmentat/media/sunete/directionare/in50demetriiviratilastanga.mp3");  
	    fd = fis.getFD(); 
	    player.reset();
	    player.setDataSource(fd);
	    player.prepare();
	    player.start();
		}
		catch(Exception e){
			
		}
}

}
