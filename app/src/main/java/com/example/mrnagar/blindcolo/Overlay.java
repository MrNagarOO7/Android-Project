package com.example.mrnagar.blindcolo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Mr.Nagar on 27-07-2017.
 */
public class Overlay extends View {
   private Paint paint=new Paint();
    Canvas mcanvas;

     Overlay(Context context) {
         super(context);
    }
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        mcanvas=canvas;
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(5);
        int x0 = mcanvas.getWidth()/2;
        int y0 = mcanvas.getHeight()/2;
        mcanvas.drawCircle(x0,y0,45,paint);

        paint.setColor(Color.YELLOW);
        mcanvas.drawRect(x0-20,y0-20,x0+20,y0+20,paint);
    }


}
