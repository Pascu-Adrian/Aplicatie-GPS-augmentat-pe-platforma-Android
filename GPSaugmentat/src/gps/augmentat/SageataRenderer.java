package gps.augmentat;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;


public class SageataRenderer implements GLSurfaceView.Renderer{
	public SageataRenderer(boolean useTranslucentBackground) {  
        mTranslucentBackground = useTranslucentBackground;    
        mCube = new Sageata();   
mTranslateX=0;
mTranslateY=0;
       }   
 
 
    public void onDrawFrame(GL10 gl) {   
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);   
        gl.glMatrixMode(GL10.GL_MODELVIEW);      
        gl.glLoadIdentity();     
        gl.glTranslatef(0+mTranslateX,0+mTranslateY, -5.0f);     
        gl.glRotatef(0+mAngleX*10f,  1, 0, 0);
        gl.glRotatef(0+mAngleY*10f, 0, 1, 0);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);  
        
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY); 
        mCube.draw(gl);  
        }   
    public void onSurfaceChanged(GL10 gl, int width, int height) {      
        gl.glViewport(0, 0, width, height);  
        float ratio = (float) width / height;      
        gl.glMatrixMode(GL10.GL_PROJECTION);        
        gl.glLoadIdentity();      
        gl.glFrustumf(-ratio, ratio, -1, 1, 1, 100); 
        }  
 
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {       
 
        gl.glDisable(GL10.GL_DITHER);      
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);    
        if (mTranslucentBackground) {           
            gl.glClearColor(0,0,0,0);       
            } else {         
                gl.glClearColor(1,1,1,1);      
                }        
        gl.glEnable(GL10.GL_CULL_FACE);      
        gl.glShadeModel(GL10.GL_SMOOTH);      
        gl.glEnable(GL10.GL_DEPTH_TEST);    
        }   
 
    public void setAngle(float _angle){ 
 
    } 
    private boolean mTranslucentBackground;   
    private Sageata mCube;   
    public  float mAngleX;   
       public float mAngleY; 
       public float mTranslateX;
       public float mTranslateY;
       public float mTranslateZ;

}
