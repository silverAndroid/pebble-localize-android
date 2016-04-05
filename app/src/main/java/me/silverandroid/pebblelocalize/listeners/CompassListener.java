package me.silverandroid.pebblelocalize.listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.widget.ImageView;

import java.util.Arrays;

import me.silverandroid.pebblelocalize.DirectionActivity;

/**
 * Created by Rushil Perera on 4/2/2016.
 */
public class CompassListener implements SensorEventListener {
    private static final String TAG = "CompassListener";
    private static float lastKnownDistance;
    private static float lastKnownAngle;
    float[] matrixR = new float[9];
    float[] orientationMatrix = new float[3];
    private float[] magnetometerData = new float[3];
    private float[] accelerometerData = new float[3];
    private boolean magnetometerSet = false;
    private boolean accelerometerSet = false;

    public static void updateLocation(float distance, float angle) {
        lastKnownDistance = distance;
        lastKnownAngle = angle;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            Log.i(TAG, "onSensorChanged: (magnetometer)" + Arrays.toString(event.values));
            System.arraycopy(event.values, 0, magnetometerData, 0, event.values.length);
            magnetometerSet = true;
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.i(TAG, "onSensorChanged: (accelerometer)" + Arrays.toString(event.values));
            System.arraycopy(event.values, 0, accelerometerData, 0, event.values.length);
            accelerometerSet = true;
        }
        if (magnetometerSet && accelerometerSet) {
            SensorManager.getRotationMatrix(matrixR, null, accelerometerData, magnetometerData);
            SensorManager.getOrientation(matrixR, orientationMatrix);
            float azimuthInRadians = orientationMatrix[0];
            float azimuthInDegrees = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;
            float currentDegree = lastKnownAngle - azimuthInDegrees;
            DirectionActivity.updateScreen(lastKnownDistance, currentDegree);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
