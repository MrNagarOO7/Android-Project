package com.example.mrnagar.blindcolo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


public class FullscreenActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    FrameLayout cam_prev;
    Camera mycamera=null;
    int color=0,red,blue,green,r_clo,b_clo,g_clo,closest_int;
    SurfaceView sv1;
    TextView tv1,rval,gval,bval,cname,color_closest;
    String color_val,r_val,g_val,b_val,hex,string;
    String[] color_name;
    long col;
    CameraPreview preview;
    ColorN colorname1;
    private static final int cam_per=100;
    TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        tv1=(TextView)findViewById(R.id.tv1);
        rval=(TextView)findViewById(R.id.rval);
        gval=(TextView)findViewById(R.id.gval);
        bval=(TextView)findViewById(R.id.bval);
        cname=(TextView)findViewById(R.id.C_name);
        color_closest=(TextView)findViewById(R.id.color_closest);
        cam_prev=(FrameLayout) findViewById(R.id.cam_prev);
        startCamera();
        sv1=(SurfaceView)findViewById(R.id.sv1);
        sv1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                color=preview.getColor();
                hex=Integer.toHexString(color);
                col=color;
                red= (int)((col>>16)&255);
                green= (int)((col>>8)&255);
                blue= (int)(col&255);
                try{
                    color_name = colorname1.getColorName(red, green, blue);//String[] object return with length 3
                    color_val = "COLOR :  " + color_name[0].toString().toUpperCase();
                    r_clo=Integer.parseInt(color_name[1]);//here string return
                    g_clo=Integer.parseInt(color_name[2]);
                    b_clo=Integer.parseInt(color_name[3]);
                    closest_int=r_clo*65536+g_clo*256+b_clo;//rgb to int
                    closest_int=closest_int-16777216;//here second value for color with alpha value
                    //Toast.makeText(getApplicationContext(),"original:"+color+"closest:"+closest_int,Toast.LENGTH_LONG).show();
                }catch (Exception e){}
                r_val="RED      : "+red;
                g_val="GREEN : "+green;
                b_val="BLUE    : "+blue;
                string="#"+hex;
                if(color_name!=null){
                    tv1.setBackgroundColor(color);
                    rval.setText(r_val);
                    gval.setText(g_val);
                    bval.setText(b_val);
                    cname.setText(color_val);
                    color_closest.setBackgroundColor(closest_int);
                    speakColor();
                }
                return false;
            }
        });
    }

    public  void startCamera(){
        try {
            mycamera = Camera.open();
            preview = new CameraPreview(this, mycamera);
            textToSpeech = new TextToSpeech(this, this);
            colorname1 = new ColorN();
            cam_prev.addView(preview);
            mycamera.setDisplayOrientation(90);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onStart(){
        super.onStart();
        //Overlay ov1=new Overlay(this);
        //addContentView(ov1,new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        int permission= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if(permission!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},cam_per);
        }
    }

    public void onResume(){
        super.onResume();
        startCamera();
    }

    @Override
    public void onInit(int status) {
        if(status== TextToSpeech.SUCCESS){
            int language=textToSpeech.setLanguage(Locale.ENGLISH);
            if(language!=TextToSpeech.LANG_MISSING_DATA || language!=TextToSpeech.LANG_NOT_SUPPORTED){
                speakColor();
            }
        }
    }

    private void speakColor(){
       try {
           String text = color_name[0];
           textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
       }catch (Exception e){}
    }

    public void onDestroy(){
        if(textToSpeech!=null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
