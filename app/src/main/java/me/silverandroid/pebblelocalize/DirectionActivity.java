package me.silverandroid.pebblelocalize;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

import me.silverandroid.pebblelocalize.listeners.CompassListener;
import me.silverandroid.pebblelocalize.listeners.GPSListener;

public class DirectionActivity extends AppCompatActivity {

    private static final String TAG = "DirectionActivity";
    private static final int REQUEST_LOCATION = 0;
    private static SensorManager sensorService;
    private static ImageView arrow;
    private static TextView distance;
    private static TextView bearing;
    private LocationManager locationManager;
    private Sensor magnetometer;
    private Sensor accelerometer;
    private SensorEventListener sensorListener;

    public static void updateScreen(float distanceF, float angle) {
        distance.setText(String.format(Locale.getDefault(), "%fm", distanceF));
        updateArrow(angle);
    }

    private static void updateArrow(float angle) {
        Log.i(TAG, "updateArrow: " + angle);
        //TODO: Make arrow rotation smoother
        Matrix matrix = new Matrix();
        matrix.postRotate(angle, arrow.getDrawable().getBounds().width() / 2, arrow.getDrawable()
                .getBounds().height() / 2);
        arrow.setImageMatrix(matrix);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        arrow = (ImageView) findViewById(R.id.direction);
        distance = (TextView) findViewById(R.id.distance);
        bearing = (TextView) findViewById(R.id.bearing);

        initializeSensors();
        initializeLocationAcquiring();
    }

    protected void onResume() {
        super.onResume();
        sensorService.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorService.registerListener(sensorListener, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sensorService.unregisterListener(sensorListener, accelerometer);
        sensorService.unregisterListener(sensorListener, magnetometer);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorService.unregisterListener(sensorListener, accelerometer);
        sensorService.unregisterListener(sensorListener, magnetometer);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocationManagerUpdates();
                }
        }
    }

    private boolean requestLocationUpdates() {
        final String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(locationPermission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(locationPermission)) {
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.direction_layout);
            Snackbar.make(layout, "Location permission is required to determine how far you are from the " +
                    "target", Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(DirectionActivity.this, new
                            String[]{locationPermission}, REQUEST_LOCATION);
                }
            });
        } else {
            ActivityCompat.requestPermissions(DirectionActivity.this, new String[]{locationPermission},
                    REQUEST_LOCATION);
        }

        return false;
    }

    private void initializeSensors() {
        sensorService = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        magnetometer = sensorService.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        accelerometer = sensorService.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (magnetometer != null && accelerometer != null) {
            sensorListener = new CompassListener(arrow);
            sensorService.registerListener(sensorListener, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
            sensorService.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            Log.i(TAG, "initializeSensors: Registered Magnetic Field Sensor and Accelerometer");
        } else {
            if (magnetometer == null)
                Log.e(TAG, "initializeSensors: Registered Magnetic Field Sensor");
            else
                Log.e(TAG, "initializeSensors: Registered Accelerometer");
        }
    }

    private void initializeLocationAcquiring() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (requestLocationUpdates()) {
                requestLocationManagerUpdates();
                Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (locationGPS != null)
                    Log.i(TAG, "onCreate: (locationGPS) " + locationGPS.getLatitude() + ", " + locationGPS
                            .getLongitude());
                else if (locationNet != null)
                    Log.i(TAG, "onCreate: (locationNet) " + locationNet.getLatitude() + ", " + locationNet
                            .getLongitude());
            }
        }
    }

    private void requestLocationManagerUpdates() {
        LocationListener locationListener = new GPSListener(45.43f, -75.7f);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListener);
    }
}
