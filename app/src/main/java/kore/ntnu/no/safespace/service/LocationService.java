package kore.ntnu.no.safespace.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by OscarWika on 19.11.2017.
 */

public class LocationService extends IntentService {

    private LocationListener listener;
    private LocationManager locationManager;

    public LocationService() {
        super("LocationService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onCreate() {

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent intent = new Intent("location_update");
                intent.putExtra("coordinates", location.getLongitude() + " " + location.getLatitude());
                sendBroadcast(intent);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        };
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates("gps", 3000, 0, listener);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {

        Toast.makeText(this, "Starting GPS", Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        Toast.makeText(this, "GPS Stopped", Toast.LENGTH_SHORT).show();
        if(locationManager != null) {
            locationManager.removeUpdates(listener);
            // TODO Find out how to remove the listener when user leaves the report activity.
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    public void stopLocationListener() {
        locationManager.removeUpdates(listener);
    }
}
