package com.example.mrnagar.blindcolo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by Mr.Nagar on 27-07-2017.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback,Camera.PreviewCallback {

    private Camera ycam;
    static int fheight,fwidth,r = 0,b = 0,g = 0,pixel = 0;
    private SurfaceHolder yholder;
    int[] clist=new int[3];
    static Bitmap bitmap;
    static Context kaka;
    boolean flag = true;
    byte[] Data;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        kaka = context;
        ycam=camera;
        yholder=getHolder();
        yholder.addCallback(this);
        yholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            yholder=getHolder();
            ycam.setPreviewDisplay(yholder);
            ycam.getParameters().setPreviewFormat(ImageFormat.NV21);
            ycam.setPreviewCallback(this);
            ycam.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
                ycam.setPreviewDisplay(holder);
            //ycam.getParameters().setPreviewFormat(ImageFormat.NV21);
            //ycam.setPreviewCallback(this);
                ycam.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        try {
                Data = data;
                if(flag == true) {
                    fheight = camera.getParameters().getPreviewSize().height;
                    fwidth = camera.getParameters().getPreviewSize().width;
                    flag = false;
                }
          } catch (Exception e) {
        }
    }

    public Bitmap getBitmap(){

        YuvImage yuvImage = new YuvImage(Data, ImageFormat.NV21, fwidth, fheight, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect((fwidth / 2) - 20, (fheight / 2) - 20, (fwidth / 2) + 20, (fheight / 2) + 20), 100, out);
        byte[] bytes = out.toByteArray();
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        return bitmap;
    }

}
