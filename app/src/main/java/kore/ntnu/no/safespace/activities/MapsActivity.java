package kore.ntnu.no.safespace.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

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
import kore.ntnu.no.safespace.utils.IdUtils;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText mapSearchField;
    private EditText mapRadiusField;
    private MarkerOptions marker = new MarkerOptions();
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
        mapRadiusField = findViewById(R.id.setRadiusField);

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
        mMap = googleMap;
        mMap.setPadding(0,150,0,0);

        init();
        setUpMap();
        onMapClickListener();
        setRadiusCircleListener();

        mMap.addMarker(marker.position(myPosition()).title("Marker in Ålesund").draggable(marker.isDraggable()));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myPosition(), 10.0f));
        circleOptions.center(myPosition())
                .radius(getRadiusFieldInt())
                .fillColor(adjustAlpha(0x3300ffff, 0.5f))
                .strokeWidth(4);
        mMap.addCircle(circleOptions);
    }

    public void setUpMap(){
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
    }

    public void onMapClickListener() {
        mMap.setOnMapClickListener(latLng -> {
            System.out.println(latLng.latitude + " " + latLng.longitude);
            mMap.clear();
            LatLng currentPosition = new LatLng(latLng.latitude, latLng.longitude);
            mMap.addMarker(marker.position(currentPosition).title("Where we at tho?"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 10.0f));
            circleOptions.center(currentPosition)
                    .radius(getRadiusFieldInt())
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

        String radius = mapRadiusField.getText().toString();
        if(!radius.isEmpty()) {
            int markerRadius = Integer.parseInt(radius);
            intent.putExtra(IdUtils.MAPS_RAD, markerRadius);
            System.out.println("Radius: " + markerRadius);
        } else {
            intent.putExtra(IdUtils.MAPS_RAD, 0);
        }

        System.out.println("Latitude: " + latLng.latitude);
        System.out.println("Longitude: " + latLng.longitude);
        startActivity(intent);
    }

    private void setRadiusCircleListener() {
        mapRadiusField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                    mMap.clear();
                    mMap.addMarker(marker);
                    circleOptions
                            .radius(getRadiusFieldInt())
                            .fillColor(adjustAlpha(0x33FFFFFF, 1f))
                            .strokeWidth(4);
                    mMap.addCircle(circleOptions);

            }
        });
    }

    private Integer getRadiusFieldInt() {
        int locationRadius;
        String radius = mapRadiusField.getText().toString();
        if(!radius.isEmpty()) {
            System.out.println(radius);
            locationRadius = Integer.parseInt(radius);
        } else {
            locationRadius = 1000;
        }
        return locationRadius;
    }

    private void searchForLocation() {
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
            circleOptions.center(currentPosition)
                    .radius(getRadiusFieldInt())
                    .fillColor(adjustAlpha(0x33FFFFFF, 1f))
                    .strokeWidth(4);
            mMap.addCircle(circleOptions);
        }
        hideKeyboard();
    }

    private LatLng myPosition() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        LatLng myPosition = new LatLng(lat, lng);
        return myPosition;
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
                searchForLocation();
            }
            return false;
        });
        hideKeyboard();
    }

    private void hideKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}
