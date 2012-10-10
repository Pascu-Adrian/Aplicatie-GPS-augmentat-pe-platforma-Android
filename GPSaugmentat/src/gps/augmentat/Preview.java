package gps.augmentat;

import java.io.IOException;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager.LayoutParams;

class Preview extends SurfaceView implements SurfaceHolder.Callback{
	  private static final String TAG = "Preview";
	  public SurfaceHolder mHolder;
	  public Camera camera;
	  Preview(Context context) {
	    super(context);
	    mHolder = getHolder();
	    mHolder.addCallback(this);
	    mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	    mHolder.setFormat(PixelFormat.TRANSLUCENT|LayoutParams.FLAG_BLUR_BEHIND);
	  }
	  public void surfaceCreated(SurfaceHolder holder) {
	    camera = Camera.open();
	    try {
	      camera.setPreviewDisplay(holder);

	      camera.setPreviewCallback(new PreviewCallback() {
	        public void onPreviewFrame(byte[] data, Camera camera) {
	          Log.d(TAG, "onPreviewFrame called at: " + System.currentTimeMillis());
	          Preview.this.invalidate();
	        }
	      });
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }
	  public void surfaceDestroyed(SurfaceHolder holder) {
		  try{
	    camera.stopPreview();
	    camera.release();
		  }
		  catch (Exception e) {}
	  }
	  public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		  try{
	    camera.startPreview();
		  }
		  catch (Exception e) {}
	  }
	}
