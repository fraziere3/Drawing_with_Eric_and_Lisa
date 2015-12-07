package com.example.eric.drawingfuneric;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends Activity implements OnClickListener {

    //custom drawing view
    private DrawingView drawView;
    private int progress1;
    //buttons
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn, colorBtn, opacityBtn, settings_btn;
    private ImageButton howToBtn, aboutBtn;

    //sizes
    private float smallBrush, mediumBrush, largeBrush;

    private boolean isSet = false;

    private TextView textView;

    private SharedPreferences prefs;

    public boolean getAntiAlias(){
        return isSet;
    }
    public boolean getNoAntiAlias(){
        return isSet = false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //get drawing view
        drawView = (DrawingView) findViewById(R.id.drawing);


        //get the palette and first color button
        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
        currPaint = (ImageButton) paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        //sizes from dimensions
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        //draw button
        drawBtn = (ImageButton) findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);

        //set initial size
        drawView.setBrushSize(smallBrush);

        //erase button
        eraseBtn = (ImageButton) findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        //new button
        newBtn = (ImageButton) findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        //save button
        saveBtn = (ImageButton) findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        //color wheel button
        colorBtn = (ImageButton) findViewById(R.id.color_picker);
        colorBtn.setOnClickListener(this);

        //opacity button
        opacityBtn = (ImageButton)findViewById(R.id.opacity_btn);
        opacityBtn.setOnClickListener(this);

        //settings button
        settings_btn = (ImageButton)findViewById(R.id.settings_btn);
        settings_btn.setOnClickListener(this);

        //about
        aboutBtn = (ImageButton)findViewById(R.id.aboutBtn);
        aboutBtn.setOnClickListener(this);

        //howto
        howToBtn = (ImageButton)findViewById(R.id.howToBtn);
        howToBtn.setOnClickListener(this);

        //preference
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
    }



    //user clicked paint
    public void paintClicked(View view) {
        //use chosen color

        //set erase false
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());

        if (view != currPaint) {
            ImageButton imgView = (ImageButton) view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            //update ui
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint = (ImageButton) view;
        }
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.draw_btn){
                //draw button clicked
                final Dialog brushDialog = new Dialog(this);
                brushDialog.setTitle("Brush size:");
                brushDialog.setContentView(R.layout.brush_chooser);

                textView = (TextView)brushDialog.findViewById(R.id.textView);
                SeekBar seekBar = (SeekBar)brushDialog.findViewById(R.id.seekBar);

                float currLevel = drawView.getLastBrushSize();
                seekBar.setProgress((int)currLevel);

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                        drawView.setBrushSize(progress1);
                        drawView.setLastBrushSize(progress1);
                        drawView.setErase(false);
                        seekBar.setProgress(progress1);
                        brushDialog.dismiss();

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // TODO Auto-generated method stub
                        //drawView.setLastBrushSize(progress1);
                       // drawView.setErase(false);
                        //drawView.setErase(true);
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // TODO Auto-generated method stub

                        //textView.setTextSize(progress);
                        progress1 = progress;

                        //Toast.makeText(getApplicationContext(), String.valueOf(progress), Toast.LENGTH_LONG).show();

                    }
                });

                brushDialog.show();
            }

         else if (view.getId() == R.id.erase_btn) {
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            //size buttons
            textView = (TextView)brushDialog.findViewById(R.id.textView);
            SeekBar seekBar = (SeekBar)brushDialog.findViewById(R.id.seekBar);

            float currLevel = drawView.getLastBrushSize();
            seekBar.setProgress((int)currLevel);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                    drawView.setBrushSize(progress1);
                    drawView.setLastBrushSize(progress1);
                    //drawView.setErase(true);
                    seekBar.setProgress(progress1);
                    brushDialog.dismiss();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                    //drawView.setLastBrushSize(progress1);

                    drawView.setErase(true);

                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // TODO Auto-generated method stub

                    //textView.setTextSize(progress);

                    progress1 = progress;

                    //Toast.makeText(getApplicationContext(), String.valueOf(progress), Toast.LENGTH_LONG).show();

                }
            });

            brushDialog.show();

        } else if (view.getId() == R.id.new_btn) {
            //new canvas button
            //sets a dialog for creating a new canvas
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            newDialog.show();

        } else if (view.getId() == R.id.save_btn) {
            //save drawing
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    //save drawing
                    drawView.setDrawingCacheEnabled(true);
                    //attempt to save
                    String imgSaved = MediaStore.Images.Media.insertImage(
                            getContentResolver(), drawView.getDrawingCache(),
                            UUID.randomUUID().toString() + ".png", "drawing");

                    //feedback
                    if (imgSaved != null) {
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    } else { //if it is unable to save
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    drawView.destroyDrawingCache();
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            saveDialog.show();

        } else if (view.getId() == R.id.color_picker) {
            colorpicker();

        }else if(view.getId()==R.id.opacity_btn){
            //launch opacity chooser
            final Dialog seekDialog = new Dialog(this);
            seekDialog.setTitle("Opacity level:");
            seekDialog.setContentView(R.layout.opacity_chooser);

            final TextView seekTxt = (TextView)seekDialog.findViewById(R.id.opq_txt);
            final SeekBar seekOpq = (SeekBar)seekDialog.findViewById(R.id.opacity_seek);

            seekOpq.setMax(100);

            int currLevel = drawView.getPaintAlpha();
            seekTxt.setText(currLevel+"%");
            seekOpq.setProgress(currLevel);
            seekOpq.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    seekTxt.setText(Integer.toString(progress) + "%");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    Button opqBtn = (Button) seekDialog.findViewById(R.id.opq_ok);
                    opqBtn.setOnClickListener(new OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            drawView.setPaintAlpha(seekOpq.getProgress());
                            seekDialog.dismiss();
                        }
                    });
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
            seekDialog.show();

        }else if (view.getId()==R.id.settings_btn){
            Intent intent = new Intent(MainActivity.this,ResourceActivity.class);
            startActivity(intent);
        }

        else if (view.getId() == R.id.aboutBtn){
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            //finish();
        }
        else if(view.getId() == R.id.howToBtn){
            Intent intent = new Intent(MainActivity.this, HowToActivity.class);
            startActivity(intent);

        }

    }

    public void colorpicker() {
        //     initialColor is the initially-selected color to be shown in the rectangle on the left of the arrow.
        //     for example, 0xff000000 is black, 0xff0000ff is blue. Please be aware of the initial 0xff which is the alpha.

        AmbilWarnaDialog dialog = new AmbilWarnaDialog(this, 0xff0000ff, new AmbilWarnaDialog.OnAmbilWarnaListener() {

            // Executes, when user click Cancel button
            @Override
            public void onCancel(AmbilWarnaDialog dialog){
            }

            // Executes, when user click OK button
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                Toast.makeText(getBaseContext(), "Selected Color : " + color, Toast.LENGTH_LONG).show();
                drawView.setColor(color);
            }
        });
        dialog.show();
    }


}