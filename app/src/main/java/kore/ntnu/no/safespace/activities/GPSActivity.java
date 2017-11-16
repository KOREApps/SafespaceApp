package kore.ntnu.no.safespace.activities;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import kore.ntnu.no.safespace.R;

/**
 * Created by OscarWika on 01.11.2017.
 */

public class GPSActivity extends AppCompatActivity {

    String locationProviderNetwork = LocationManager.NETWORK_PROVIDER;
    String locationProviderGPS = LocationManager.GPS_PROVIDER;
    Button getLocationBtn;
    Button clearLocationBtn;
    TextView getLongitudeView;
    TextView getLatitudeView;
    TextView getLocationView;
    LocationManager locationManager;
    LocationListener locationListener;
    Location lastKnownLocation;
    Location NTNULabBuilding;
    Location NTNUMainBuilding;

    private SharedPreferences prefs;
    private String currentLocation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        getLocationBtn = findViewById(R.id.gpsButtonYo);
        getLatitudeView = findViewById(R.id.latitudeView);
        getLongitudeView = findViewById(R.id.longitudeView);
        getLocationView = findViewById(R.id.locationView);
        clearLocationBtn = findViewById(R.id.gpsClearBtn);
        clearLocationBtn.setOnClickListener(view -> stopAnyListener());

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        SharedPreferences preferencesX = PreferenceManager.getDefaultSharedPreferences(this);
        String currentLocation = preferencesX.getString("CurrentLocation", "");
        this.currentLocation = currentLocation;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // This method is called every time the location is updated.
                getLatitudeView.setText("Latitude:");
                getLatitudeView.append("\n" + location.getLatitude());
                getLongitudeView.setText("Longitude:");
                getLongitudeView.append("\n" + location.getLongitude());

                lastKnownLocation = locationManager.getLastKnownLocation(locationProviderGPS);

                float result[] = new float[10];
                NTNULabBuilding.distanceBetween(62.472171, 6.233951, location.getLatitude(), location.getLongitude(), result);

                float result2[] = new float[10];
                NTNUMainBuilding.distanceBetween(62.472144, 6.235740, location.getLatitude(), location.getLongitude(), result2);

                if(result[0] < 36){
                    getLocationView.setText("Location: You are at NTNU LAB building");
                    getLocationView.append("\nDistance from center: " + result[0]);
                    GPSActivity.this.currentLocation = "NTNU lab";
                } else if (result2[0] < 50) {
                    getLocationView.setText("Location: You are at the main building");
                    getLocationView.append("\nDistance from LAB center: " + result[0]);
                    GPSActivity.this.currentLocation = "Main Building";
                } else {
                    getLocationView.setText("");
                    getLocationView.append("\nYou are NOT in a building(hopefully)");
                    GPSActivity.this.currentLocation = "Lat: " + location.getLatitude() + "Long: " + location.getLongitude();
                }

                editor.putString("CurrentLocation", currentLocation);
                editor.apply();
                System.out.println("Current Location: " + currentLocation);
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
                requestPermissions(new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET,
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
        switch(requestCode) {
            case 10:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    startGPSListener();
                return;
        }
    }

    private void startGPSListener() {
        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if(lastKnownLocation == null) {
                    getLongitudeView.setText("Coordinates: No previous coordinates found, fetching..");
                    locationManager.requestLocationUpdates("gps", 3000, 0, locationListener);
                } else {
                    getLocationView.setText("Location: Fetching location..");
                    getLatitudeView.setText("Latitude:" + "\n" + lastKnownLocation.getLatitude() + "(last known)");
                    getLongitudeView.setText("Longitude" + "\n" + lastKnownLocation.getLongitude() + "(last known)");
                    locationManager.requestLocationUpdates("gps", 3000, 0, locationListener);
                }

            }
        });
    }

    public void stopAnyListener() {
        locationManager.removeUpdates(locationListener);
    }

    public String getLocation() { return currentLocation; }

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
}
