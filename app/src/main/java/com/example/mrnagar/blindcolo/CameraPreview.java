package com.example.mrnagar.blindcolo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Mr.Nagar on 27-07-2017.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback,Camera.PreviewCallback {
    private Camera ycam;
    String tag;
    int x, y,color=0,index,fheight,fwidth;
    private SurfaceHolder yholder;
    int[] clist;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        ycam=camera;
        yholder=getHolder();
        yholder.addCallback(this);
        yholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            ycam.setPreviewDisplay(holder);
            ycam.getParameters().setPreviewFormat(ImageFormat.NV21);
            ycam.setPreviewCallback(this);
            ycam.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            ycam.setPreviewDisplay(holder);
            ycam.getParameters().setPreviewFormat(ImageFormat.NV21);
            ycam.setPreviewCallback(this);
            ycam.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        try {
            fheight = camera.getParameters().getPreviewSize().height;
            fwidth = camera.getParameters().getPreviewSize().width;
            int[] rgb = new int[(fwidth * fheight)];
            clist = decodeYUV420SP(rgb, data, fwidth, fheight);
        }catch (Exception e){}
    }
    public int[] decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {

        // here we're using our own internal PImage attributes
        final int frameSize = width * height;

        for (int j = 0, yp = 0; j < height; j++) {
              int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
                for (int i = 0; i < width; i++, yp++) {
                    int y = (0xff & ((int)yuv420sp[yp])) - 16 ;
                    if (y < 0)
                    y = 0;
                    if ((i & 1) == 0) {
                        v = (0xff & yuv420sp[uvp++]) - 128;
                        u = (0xff & yuv420sp[uvp++]) - 128;
                     }
                    int y1192 = 1192 * y;
                    int r = (y1192 + 1634 * v);
                    int g = (y1192 - 833 * v - 400 * u);
                    int b = (y1192 + 2066 * u);
                    if (r < 0)
                        r = 0;
                    else if (r > 262143)
                        r = 262143;
                    if (g < 0)
                        g = 0;
                    else if (g > 262143)
                        g = 262143;
                    if (b < 0)
                        b = 0;
                    else if (b > 262143)
                        b = 262143;
                    rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }
        }

        return rgb;
    }
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction()==MotionEvent.ACTION_DOWN){

             x=(int)event.getX();
             y=(int)event.getY();
            index=(fheight*y)+x;
            try{
                color=clist[ index];
            }catch (Exception e){}
        }
        return  false;
    }
    public int getColor(){

        return   color;
    }
}
