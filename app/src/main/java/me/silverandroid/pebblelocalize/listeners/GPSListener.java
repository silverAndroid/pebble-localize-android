package me.silverandroid.pebblelocalize.listeners;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Rushil Perera on 4/2/2016.
 */
public class GPSListener implements LocationListener {
    private final static double AVERAGE_RADIUS_OF_EARTH = 6371;
    private static final String TAG = "GPSListener";
    private final float destinationLatitude;
    private final float destinationLongitude;

    public GPSListener(float destinationLatitude, float destinationLongitude) {
        this.destinationLatitude = destinationLatitude;
        this.destinationLongitude = destinationLongitude;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged: Latitude: " + location.getLatitude());
        Log.i(TAG, "onLocationChanged: Longitude: " + location.getLongitude());
        calculateDistance(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void calculateDistance(double latitude, double longitude) {

        //parameters are point 1, fields are point 2
        float phi1 = (float) Math.toRadians(latitude);
        float phi2 = (float) Math.toRadians(destinationLatitude);

        float deltaPhi = (float) Math.toRadians(destinationLatitude - latitude);
        float deltaLambda = (float) Math.toRadians(destinationLongitude - longitude);

        float earthRadius = 6371000; //metres

        float a = (float) (Math.sin(deltaPhi / 2) * Math.sin(deltaPhi / 2) + Math.cos(phi1) * Math.cos
                (phi2) * Math.sin(deltaLambda / 2) * Math.sin(deltaLambda / 2));
        float c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
        float d = earthRadius * c;

        float y = (float) (Math.sin(destinationLongitude) - Math.cos(phi2));
        float x = (float) (Math.cos(phi1) * Math.sin(phi2) - Math.sin(phi1) * Math.cos(phi2) * Math.cos
                        (destinationLongitude));
        float brng = (float) Math.toDegrees(Math.atan2(y, x));

        CompassListener.updateLocation(d, brng);
    }
}
