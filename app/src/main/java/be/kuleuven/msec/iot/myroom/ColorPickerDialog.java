package be.kuleuven.msec.iot.myroom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.SimpleColorSelectionListener;

import be.kuleuven.msec.iot.iotframework.callbackinterfaces.OnRequestCompleted;
import be.kuleuven.msec.iot.iotframework.generic.devicelayer.Lamp;

/**
 * Created by ilsebohe on 18/01/2018.
 */

public class ColorPickerDialog extends Dialog {
    final String TAG = "ColorPickerDialog";

    Context context;
    Lamp lamp;
    LampExpandableListAdapter listAdapter;

    public ColorPickerDialog(@NonNull Context context,Lamp  lamp, LampExpandableListAdapter listAdapter) {
        super(context);
        this.context=context;
        this.lamp = lamp;
        this.listAdapter = listAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.color_picker_dialog);
        setTitle("Pick a color!");

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        final ImageView colorView = (ImageView) findViewById(R.id.imageView_color);



        final CountDownTimer c = new CountDownTimer(1500, 1000) {

            public void onTick(long millisUntilFinished) {
                System.out.println("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                lamp.changeColor(lamp.getColor(), new OnRequestCompleted() {
                    @Override
                    public void onSuccess(Object response) {
                        Log.i(TAG, "Light color in room changed to " + lamp.getColor());
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listAdapter.notifyDataSetChanged();
                            }
                        });


                    }
                });
            }
        };


        final HSLColorPicker colorPicker = (HSLColorPicker) findViewById(R.id.colorPicker);
        colorPicker.setColorSelectionListener(new SimpleColorSelectionListener() {
            @Override
            public void onColorSelected(final int color) {
                // Do whatever you want with the color
                c.cancel();
                c.start();

               lamp.setColor(String.format("%06X", (0xFFFFFF & color)));
               colorView.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
            }
        });

        colorView.getBackground().setColorFilter(Color.parseColor("#"+lamp.getColor()), PorterDuff.Mode.MULTIPLY);
        colorPicker.setColor(Color.parseColor("#"+lamp.getColor()));



    }


}
