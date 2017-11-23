package kore.ntnu.no.safespace.activities;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.RegisterLocationActivity;
import kore.ntnu.no.safespace.utils.IdUtils;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MarkerOptions marker;
    private EditText mapSearchField;
    private CircleOptions circleOptions = new CircleOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapSearchField = findViewById(R.id.searchMapField);
        Button okBtn = findViewById(R.id.okBtn);
        okBtn.setOnClickListener(view -> registerLocationBtn());
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        init();
        mMap = googleMap;
        mMap.setPadding(0,150,0,0);

        LatLng alesund = new LatLng(62.47, 6.22);
        marker = new MarkerOptions();
        mMap.addMarker(marker.position(alesund).title("Marker in Ålesund"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(alesund, 10.0f));

        circleOptions.center(alesund)
                .radius(10000)
                .fillColor(adjustAlpha(0x3300ffff, 0.5f))
                .strokeWidth(4);
        mMap.addCircle(circleOptions);

        mMap.setOnMapClickListener(latLng -> {
            System.out.println(latLng.latitude + " " + latLng.longitude);
            mMap.clear();
            LatLng currentPosition = new LatLng(latLng.latitude, latLng.longitude);
            mMap.addMarker(marker.position(currentPosition).title("Where we at tho?"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 10.0f));
            circleOptions.center(currentPosition)
                    .radius(10000)
                    .fillColor(adjustAlpha(0x33FFFFFF, 1f))
                    .strokeWidth(4);
            mMap.addCircle(circleOptions);
        });
    }

    private void registerLocationBtn() {
        Intent intent = new Intent(MapsActivity.this, RegisterLocationActivity.class);
        LatLng latLng = marker.getPosition();
        intent.putExtra(IdUtils.MAPS_LAT, latLng.latitude);
        intent.putExtra(IdUtils.MAPS_LOG, latLng.longitude);
        System.out.println("Latitude: " + latLng.latitude);
        System.out.println("Longitude: " + latLng.longitude);
        startActivity(intent);
    }

    private int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    private void init() {
        mapSearchField.setOnEditorActionListener((textView, i, keyEvent) -> {
            if( i == EditorInfo.IME_ACTION_SEARCH
                    || i == EditorInfo.IME_ACTION_DONE
                    || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                    || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                findLocation();
            }
            return false;
        });
        hideKeyboard();
    }

    private void hideKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void findLocation() {
        String searchString = mapSearchField.getText().toString();
        Geocoder geoCoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geoCoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {}

        if(list.size() > 0) {
            Address address = list.get(0);

            LatLng currentPosition = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.clear();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((currentPosition), 10.0f));
            mMap.addMarker(marker.position(currentPosition).title(address.getAddressLine(0)));

            circleOptions = new CircleOptions();
            circleOptions.center(currentPosition)
                    .radius(10000)
                    .fillColor(adjustAlpha(0x33FFFFFF, 1f))
                    .strokeWidth(4);
            mMap.addCircle(circleOptions);
        }
        hideKeyboard();
    }

    private void setRadius() {
    }


}
