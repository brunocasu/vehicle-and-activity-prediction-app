package com.example.vehicledetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import java.io.IOException;
import java.util.NoSuchElementException;

public class DataCollectionActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private Sensor sAcceleration;
    private SensorManager sm;
    private boolean recording = false;
    private Context c;
    private File[] tempFiles;
    private SeekBar windowSizeBar, overlappingBar;
    private int currentVehicle = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collection);
        setSensors();
        c = this.getBaseContext();
        initTempFiles();
        initListeners();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.recordButton) {
            if (!recording) { sm.registerListener(this, sAcceleration, SensorManager.SENSOR_DELAY_GAME); recording = true; }
            else { sm.unregisterListener(this); recording = false; }
        } else if (v.getId() == R.id.busButton) {
            removeButtonBorder();
            Button b = v.findViewById(R.id.busButton);
            b.setBackgroundResource(R.drawable.button_focus);
            currentVehicle = 0;
        } else if (v.getId() == R.id.carButton) {
            removeButtonBorder();
            Button b = v.findViewById(R.id.carButton);
            b.setBackgroundResource(R.drawable.button_focus);
            currentVehicle = 1;
        } else if (v.getId() == R.id.walkButton) {
            removeButtonBorder();
            Button b = v.findViewById(R.id.walkButton);
            b.setBackgroundResource(R.drawable.button_focus);
            currentVehicle = 3;
        } else if (v.getId() == R.id.trainButton) {
            removeButtonBorder();
            Button b = v.findViewById(R.id.trainButton);
            b.setBackgroundResource(R.drawable.button_focus);
            currentVehicle = 4;
        } else if (v.getId() == R.id.motoButton) {
            removeButtonBorder();
            Button b = v.findViewById(R.id.motoButton);
            b.setBackgroundResource(R.drawable.button_focus);
            currentVehicle = 2;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            writeOnFile(x, y, z);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) { }

    public void setSensors() {
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sAcceleration = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
    }

    public void initListeners() {
        windowSizeBar = findViewById(R.id.windowSizeBar);
        windowSizeBar.setOnSeekBarChangeListener(this);
        windowSizeBar.setMin(1);
        overlappingBar = findViewById(R.id.overlappingBar);
        overlappingBar.setOnSeekBarChangeListener(this);
        overlappingBar.setMin(25);
    }

    public void removeButtonBorder() {
        // TODO
    }

    public void initTempFiles() {
        tempFiles = new File[5];
        try {
            tempFiles[0] = File.createTempFile("bus_data", ".xml", c.getCacheDir());
            tempFiles[1] = File.createTempFile("car_data", ".xml", c.getCacheDir());
            tempFiles[2] = File.createTempFile("motorbike_data", ".xml", c.getCacheDir());
            tempFiles[3] = File.createTempFile("walk_data", ".xml", c.getCacheDir());
            tempFiles[4] = File.createTempFile("train_data", ".xml", c.getCacheDir());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeOnFile(float x, float y, float z) {
        try {
            FileWriter fw = new FileWriter(tempFiles[currentVehicle], true);
            fw.write("x: " + x + " y: " + y + " z: " + z + "\n");
            fw.close();

            // ----------- Remove after seen that write is done successfully ------- //
            FileReader fr = new FileReader(tempFiles[currentVehicle]);
            int content;
            StringBuffer sb = new StringBuffer();
            while ((content = fr.read()) != -1) {
                sb.append((char) content);
            }
            Log.i("PRINT"+currentVehicle, sb.toString());
            TextView t = findViewById(R.id.auxiliarText);
            t.setText(tempFiles[currentVehicle].getPath());
            // --------------------------------------------------------------------- //

        } catch (IOException e) {
            Log.i("ERROR", e.toString());
        }
    }

    // SEEK BAR EVENT HANDLERS
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        if (seekBar.getId() == R.id.windowSizeBar) {
            TextView t = findViewById(R.id.windowSizeText);
            t.setText("Window Size    =   " + windowSizeBar.getProgress());
        } else if (seekBar.getId() == R.id.overlappingBar) {
            progress = progress / 25;
            progress = progress * 25;
            ((TextView)findViewById(R.id.overlappingText)).setText("Overlapping    =   " + progress);
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
