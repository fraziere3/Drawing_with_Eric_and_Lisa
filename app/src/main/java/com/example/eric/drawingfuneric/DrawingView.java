package com.example.eric.drawingfuneric;

import android.graphics.Color;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;
import android.widget.Toast;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by eric on 11/9/2015.
 */
public class DrawingView extends View {


    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = 0xFF000000;
    //background color
    private int backgoundColor = 0xFF000000;
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;

    private int tempColor;

    private float brushSize, lastBrushSize;

    private boolean erase=false;

    private int paintAlpha = 255;

    //private Bitmap mergedLayersBitmap = null;
    //private Canvas mergeLayersCanvas = null;



    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }


    public void setColor(int newColor){
        invalidate();
        paintColor = newColor;
        drawPaint.setColor(paintColor);
        tempColor = paintColor;
    }

    public void changeColor(int newColor){
        backgoundColor = newColor;
        invalidate();
    }

    private void setupDrawing(){


        brushSize = getResources().getInteger(R.integer.small_size);
        lastBrushSize = brushSize;
//get drawing area setup for interaction

        drawPath = new Path();
        drawPaint = new Paint();

        drawPaint.setColor(paintColor);

        drawPaint.setAntiAlias(true);
        //drawPaint.setStrokeWidth(20);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);


        canvasPaint = new Paint(Paint.DITHER_FLAG);

        //MainActivity activity = new MainActivity();
        //if(activity.getAntiAlias() == true){
            //drawPaint.setAntiAlias(true);
        //}else
            //drawPaint.setAntiAlias(false);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);

        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }


    @Override
    protected void onDraw(Canvas canvas) {
    //draw view

        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
//detect user touch
        float touchX = event.getX();
        float touchY = event.getY();


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate(); //causes onDraw method to happen
        return true;
    }


    public void setBrushSize(float newSize){
    //update size

        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setColor(String newColor){
    //set color
        invalidate();

        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    public float getLastBrushSize(){
        return lastBrushSize;
    }

    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }

    public void setErase(boolean isErase){
    //set erase true or false
        erase=isErase;
        if(erase)
            this.setColor("#FFFFFFFF");//set the color to white
        else
        //drawPaint.setXfermode(null);
            this.setColor(paintColor); //if erase is set to false, it will use the previous color.
    }

    public void putOverlay(Bitmap bitmap, Bitmap overlay){
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(overlay, 0, 0, paint);
    }
    public void startNew(){

        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public int getPaintAlpha(){
        return Math.round((float)paintAlpha/255*100);
    }

    public void setPaintAlpha(int newAlpha){
        paintAlpha=Math.round((float)newAlpha/100*255);
        drawPaint.setColor(paintColor);
        drawPaint.setAlpha(paintAlpha);
    }


}

