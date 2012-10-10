package gps.augmentat;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;

public class AugmentedSurfaceView extends GLSurfaceView{
	public AugmentedSurfaceView(Context context,GLSurfaceView.Renderer renderer) {        
        super(context);  
        cr  = new SageataRenderer(true); 
        //POIAugmentatRenderer poirend=new POIAugmentatRenderer(context);
        this.setEGLConfigChooser(8, 8, 8, 8, 16, 0); 
        this.setRenderer(cr); 
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);   
        this.getHolder().setFormat(PixelFormat.TRANSPARENT); 
 
        }      
 
    
    public  SageataRenderer cr ;  
}
