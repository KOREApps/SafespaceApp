package kore.ntnu.no.safespace.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
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
    TextView getLocationView;
    LocationManager locationManager;
    LocationListener locationListener;
    Location lastKnownLocation;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        getLocationBtn = findViewById(R.id.gpsButtonYo);
        getLocationView = findViewById(R.id.gpsTextView);
        clearLocationBtn = findViewById(R.id.gpsClearBtn);

        clearLocationBtn.setOnClickListener(view -> stopGPSListener());
        getLocationView.setMovementMethod(new ScrollingMovementMethod());

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // This method is called every time the location is updated.
                getLocationView.append("\n" + location.getLatitude() + " " + location.getLongitude());
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

        lastKnownLocation = locationManager.getLastKnownLocation(locationProviderNetwork);
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



    /**
     * Starts the GPS listener and clears the textview.
     * The GPS provider sometimes take a very long time to get location, but is very precise.
     * The Network provider is quick, but not as accurate.
     */
    private void startGPSListener() {
        getLocationBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                getLocationView.setText("Coordinates:" + "\nLong: " + lastKnownLocation.getLongitude()
                                        + "\nLat: " + lastKnownLocation.getLatitude());
                locationManager.requestLocationUpdates("gps", 3000, 0, locationListener);

            }
        });
    }

    /**
     * Stops the GPS listener and clears the textview.
     */
    public void stopGPSListener() {
        locationManager.removeUpdates(locationListener);
    }

    public void stopNetworkListener() { locationManager.removeUpdates(locationListener); }
}
