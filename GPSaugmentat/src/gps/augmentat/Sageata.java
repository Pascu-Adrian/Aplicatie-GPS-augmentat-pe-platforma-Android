package gps.augmentat;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Sageata {
	public Sageata()   
    {        int one = 0x10000;  
    		int zero=0x00000;
    int vertices[] = {   
            zero, -one/2, -2*one,    
            zero, -one/2, -2*one,      
            zero,  one/2, -2*one,       
            zero,  one/2, -2*one,            
            -one, -one/2,  2*one,          
            one, -one/2,  2*one,            
            one,  one/2,  2*one,           
            -one,  one/2,  2*one, 
            zero,one/2,	2*one,
            zero,-one/2,2*one,
    
    };   
 
    float[] colors = {       
            0f,    0f,    0f,  0.5f,  
            1f ,  0f,  0f, 0.1f,     
            1f,1f,0f,0.5f,    
            0f,  1f,    0f,  0.1f,       
            0f,    0f,  1f,  0.1f,        
            1f,    0f,  1f,  0.2f,
            0f,    0f,    0f,  0.5f,  
            1f ,  0f,  0f, 0.1f,     
            1f,1f,0f,0.5f,    
            0f,  1f,    0f,  0.1f,       
            0f,    0f,  1f,  0.1f,        
            1f,    0f,  1f,  0.2f,
            1f ,  0f,  0f, 0.1f,     
            1f,1f,0f,0.5f,    
            0f,  1f,    0f,  0.1f,       
            0f,    0f,  1f,  0.1f,        
            1f,    0f,  1f,  0.2f,
            1f,  1f,  1f,  0.1f,         
            0f,  1f,  1f,  0.1f,        };  
   /* float[] colors = {       
            0.3f,    0.3f,    0.3f,  0.3f,  
            0.3f ,  0.3f,  0.3f, 0.3f,     
            0.3f,0.3f,0.3f,0.3f,    
            0.3f,  0.3f,    0.3f,  0.3f,       
            0.3f,    0.3f,  0.3f,  0.3f,        
            0.3f,    0.3f,  0.3f,  0.3f,
            0.3f,    0.3f,    0.3f,  0.3f,  
            0.3f ,  0.3f,  0.3f, 0.3f,     
            0.3f,0.3f,0.3f,0.3f,    
            0.3f,  0.3f,    0.3f,  0.3f,       
            0.3f,    0.3f,  0.3f,  0.3f,        
            0.3f,    0.3f,  0.3f,  0.3f,
            0.3f ,  0.3f,  0.3f, 0.3f,     
            0.3f,0.3f,0.3f,0.3f,    
            0.3f,  0.3f,    0.3f,  0.3f,       
            0.3f,    0.3f,  0.3f,  0.3f,        
            0.3f,    0.3f,  0.3f,  0.3f,
            0.3f,  0.3f,  0.3f,  0.3f,         
            0.3f,  0.3f,  0.3f,  0.3f,        }; */
 
    byte indices[] = {           
            0, 4, 5,    0, 5, 1,     
            1, 5, 6,    1, 6, 2,      
            2, 6, 7,    2, 7, 3,       
            3, 7, 4,    3, 4, 0,       
            4, 7, 6,    4, 6, 5,        
            3, 0, 1,    3, 1, 2 ,
            7,6,8,       4,9,5
            
    
    };    
 
 
    ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);    
    vbb.order(ByteOrder.nativeOrder());      
    mVertexBuffer = vbb.asIntBuffer();     
    mVertexBuffer.put(vertices);       
    mVertexBuffer.position(0);       
    ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);    
    cbb.order(ByteOrder.nativeOrder());      
    mColorBuffer = cbb.asFloatBuffer();       
    mColorBuffer.put(colors);      
    mColorBuffer.position(0);       
    mIndexBuffer = ByteBuffer.allocateDirect(indices.length);      
    mIndexBuffer.put(indices);      
    mIndexBuffer.position(0);    }  
    public void draw(GL10 gl)    {     
        gl.glFrontFace(GL10.GL_CW);      
        gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);   
        gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);      
        gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE, mIndexBuffer);    }  
 
    private IntBuffer   mVertexBuffer;   
    private FloatBuffer   mColorBuffer;    
    private ByteBuffer  mIndexBuffer; 
 

}
