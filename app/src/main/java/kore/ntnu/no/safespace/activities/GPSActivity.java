package kore.ntnu.no.safespace.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import kore.ntnu.no.safespace.R;

/**
 * Created by OscarWika on 01.11.2017.
 */

public class GPSActivity extends AppCompatActivity {

    private TextView getLocationView;
    private TextView getAccuracyView;
    private TextView getLongitudeView;
    private TextView getLatitudeView;
    private TextView getTimeSinceLastView;
    private TextView getTimeAtLastView;

    // Location Manager
    private String locationProviderNetwork = LocationManager.NETWORK_PROVIDER;
    private String locationProviderGPS = LocationManager.GPS_PROVIDER;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location lastKnownLocation;

    // Strings
    private String currentLocation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        Button getLocationBtn = findViewById(R.id.gpsButtonYo);
        getLocationView = findViewById(R.id.locationView);
        getAccuracyView = findViewById(R.id.accuracyView);
        getLatitudeView = findViewById(R.id.latitudeView);
        getLongitudeView = findViewById(R.id.longitudeView);
        getTimeSinceLastView = findViewById(R.id.timeSinceLastView);
        getTimeAtLastView = findViewById(R.id.timeAtLastUpdateView);
        Button clearLocationBtn = findViewById(R.id.gpsClearBtn);

        getLocationBtn.setOnClickListener(view -> startGPSListener());
        clearLocationBtn.setOnClickListener(view -> stopAnyListener());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        String currentLocation = prefs.getString("CurrentLocation", "");
        this.currentLocation = currentLocation;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // This method is called every time the location is updated.
                getLatitudeView.setText(R.string.latitude);
                getLatitudeView.append("\n" + location.getLatitude());
                getLongitudeView.setText(R.string.longitude);
                getLongitudeView.append("\n" + location.getLongitude());

                editor.putFloat("CurrentLatitude", (float) location.getLatitude());
                editor.putFloat("CurrentLongitude", (float) location.getLongitude());

                if(lastKnownLocation != null) {
                    long prevTime = lastKnownLocation.getElapsedRealtimeNanos();
                    long currentTime = location.getElapsedRealtimeNanos();
                    long diffTime = currentTime - prevTime;
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

                    lastKnownLocation = locationManager.getLastKnownLocation(locationProviderGPS);

                    String myDate = format.format(new Date());

                    getAccuracyView.setText(R.string.accuracy);
                    getAccuracyView.append("\n" + location.getAccuracy());
                    getTimeSinceLastView.setText(R.string.time_since_last_update);
                    getTimeSinceLastView.append("\n" + diffTime / 1000000000.0);
                    getTimeAtLastView.setText(R.string.time_at_last_update);
                    getTimeAtLastView.append("\n" + myDate);

                } else {
                    lastKnownLocation = locationManager.getLastKnownLocation(locationProviderGPS);
                }

                // NTNU LAB
                float result[] = new float[10];
                Location.distanceBetween(62.472171, 6.233951, location.getLatitude(), location.getLongitude(), result);

                // NTNU Main Building
                float result2[] = new float[10];
                Location.distanceBetween(62.472144, 6.235740, location.getLatitude(), location.getLongitude(), result2);

                if (result[0] < 36) {
                    getLocationView.setText(R.string.ntnu_lab);
                    getLocationView.append("\nDistance from center: " + result[0]);
                    GPSActivity.this.currentLocation = "NTNU lab";
                    editor.putString("CurrentLocation", GPSActivity.this.currentLocation);
                    editor.apply();
                } else if (result2[0] < 50) {
                    getLocationView.setText(R.string.ntnu_main);
                    getLocationView.append("\nDistance from LAB center: " + result[0]);
                    GPSActivity.this.currentLocation = "Main Building";
                } else {
                    getLocationView.setText("");
                    getLocationView.append("\nYou are NOT in a building(hopefully)");
                    GPSActivity.this.currentLocation = "Lat: " + location.getLatitude() + " Long: " + location.getLongitude();
                }

                editor.putString("CurrentLocation", GPSActivity.this.currentLocation);
                editor.apply();
                System.out.println("Current Location: " + GPSActivity.this.currentLocation);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
                // This method checks if the GPS is turned off.
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, 10);
            }
            return;
        } else {
            startGPSListener();
        }

        lastKnownLocation = locationManager.getLastKnownLocation(locationProviderGPS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startGPSListener();
                return;
        }
    }

    private void startGPSListener() {
        locationManager.requestLocationUpdates("gps", 3000, 0, locationListener);

        if (lastKnownLocation == null) {
            getLocationView.setText(R.string.fetching_new_location);
        } else {
            getLocationView.setText(R.string.fetching_location);
            getLatitudeView.setText(R.string.latitude);
            getLatitudeView.append("\n" + lastKnownLocation.getLatitude() + "(last known)");
            getLongitudeView.setText(R.string.longitude);
            getLongitudeView.append("\n" + lastKnownLocation.getLongitude() + "(last known)");
        }
    }

    public void stopAnyListener() {
        getTimeSinceLastView.append(" (STOPPED)");
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Avoid consuming all the phones battery when the app is not used.
        stopAnyListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startGPSListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopAnyListener();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startGPSListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAnyListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
