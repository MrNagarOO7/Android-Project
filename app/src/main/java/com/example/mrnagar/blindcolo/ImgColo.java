package com.example.mrnagar.blindcolo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;


public class ImgColo extends AppCompatActivity {

    Button filebtn1;
    TextView rval1,bval1,gval1,C_name1;
    ImageView file_color_view,file_view;
    SurfaceView file_surface;
    Bitmap bitmap;
    int red=0,green=0,blue=0,pixel=0,img_color=0;
    int reqCode=2;
    String[] file_color_name = new String[3];
    ColorN colorN = new ColorN();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_colo);

        rval1 = (TextView) findViewById(R.id.rval1);
        bval1 = (TextView) findViewById(R.id.bval1);
        gval1 = (TextView) findViewById(R.id.gval1);
        filebtn1 = (Button) findViewById(R.id.file_add);
        file_color_view = (ImageView) findViewById(R.id.file_color_view);
        file_view = (ImageView) findViewById(R.id.file_view);
        C_name1 = (TextView) findViewById(R.id.C_name1);
        file_surface = (SurfaceView) findViewById(R.id.file_surface);

        filebtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,reqCode);
            }
        });

        file_surface.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                    int x = (int) event.getX();
                    int y = (int) event.getY();
               // Toast.makeText(getApplicationContext(),"x:"+bitmap.getWidth()+"y:"+bitmap.getHeight(),Toast.LENGTH_SHORT).show();
                    try {
                        pixel = bitmap.getPixel(x, y);
                    }catch (Exception e){}
                    red = Color.red(pixel);
                    green = Color.green(pixel);
                    blue = Color.blue(pixel);
                    img_color = red * 65536 + green * 256 + blue - 16777216;
                    file_color_name=colorN.getColorName(red,green,blue);
                    rval1.setText("RED      : "+red);
                    gval1.setText("GREEN : "+green);
                    bval1.setText("BLUE    : "+blue);
                    C_name1.setText("COLOR :  " + file_color_name[0].toString().toUpperCase());
                    file_color_view.setBackgroundColor(img_color);
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(requestCode == reqCode){
           Uri image = data.getData();
           String[] filepathColumn = {MediaStore.Images.Media.DATA};
           Cursor cursor = getContentResolver().query(image,filepathColumn,null,null,null);
           cursor.moveToNext();
           int index = cursor.getColumnIndex(filepathColumn[0]);
           String picpath = cursor.getString(index);
           cursor.close();
           bitmap = BitmapFactory.decodeFile(picpath);
           file_view.setImageBitmap(bitmap);
       }
    }
}
