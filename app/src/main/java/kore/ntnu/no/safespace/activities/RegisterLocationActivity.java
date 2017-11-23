package kore.ntnu.no.safespace.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import kore.ntnu.no.safespace.ErrorDialog;
import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.data.KnownLocation;
import kore.ntnu.no.safespace.service.KnownLocationService;
import kore.ntnu.no.safespace.tasks.RegisterNewLocationTask;
import kore.ntnu.no.safespace.utils.IdUtils;

public class RegisterLocationActivity extends AppCompatActivity {

    private EditText locationNameText;
    private EditText locationLatText;
    private EditText locationLogText;
    private EditText locationRadius;
    private Button registerLocationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_location);

        locationNameText = findViewById(R.id.locNameInput);
        locationLatText = findViewById(R.id.locLatInput);
        locationLogText = findViewById(R.id.locLogInput);
        locationRadius = findViewById(R.id.locRadiusInput);
        registerLocationBtn = findViewById(R.id.registerLocationButton);

        Intent intent = getIntent();
        locationLatText.setText(Double.toString(intent.getDoubleExtra(IdUtils.MAPS_LAT, 0.0)));
        locationLogText.setText(Double.toString(intent.getDoubleExtra(IdUtils.MAPS_LOG, 0.0)));
        setRegisterLocationBtn();
    }

    private void setRegisterLocationBtn() {
        registerLocationBtn = findViewById(R.id.registerLocationButton);
        registerLocationBtn.setOnClickListener((View view) -> {

            KnownLocationService locationService = new KnownLocationService();
            KnownLocation newKnownLocation = getKnownLocation();
            new RegisterNewLocationTask(result -> {
                if (result.isSuccess()) {
                    Intent intent = new Intent(RegisterLocationActivity.this, MapsActivity.class);
                    startActivity(intent);
                    Toast.makeText(this, "Location registered", Toast.LENGTH_SHORT).show();
                } else {
                    ErrorDialog.showErrorDialog(this ,result.getMessage());
                }
            }).execute(newKnownLocation);
        });
    }

    private KnownLocation getKnownLocation(){
        double latitudeDouble;
        double longitudeDouble;
        int radiusInt;

        locationNameText = findViewById(R.id.locNameInput);
        String locationName = locationNameText.getText().toString();

        locationLatText = findViewById(R.id.locLatInput);
        String latitude = locationLatText.getText().toString();
        latitudeDouble = Double.parseDouble(latitude);

        locationLogText = findViewById(R.id.locLogInput);
        String longitude = locationLogText.getText().toString();
        longitudeDouble = Double.parseDouble(longitude);

        locationRadius = findViewById(R.id.locRadiusInput);
        String radius = locationRadius.getText().toString();
        radiusInt = Integer.parseInt(radius);

        return new KnownLocation(null, locationName, latitudeDouble, longitudeDouble, radiusInt);
    }
}
