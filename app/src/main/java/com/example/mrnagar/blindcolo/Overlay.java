package com.example.mrnagar.blindcolo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

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
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(10);
        canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2,50, paint);
    }


}
