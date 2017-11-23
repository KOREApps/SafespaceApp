package kore.ntnu.no.safespace;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        registerLocationBtn = findViewById(R.id.registerButton);

        Intent intent = getIntent();
        locationLatText.setText(Double.toString(intent.getDoubleExtra(IdUtils.MAPS_LAT, 0.0)));
        locationLogText.setText(Double.toString(intent.getDoubleExtra(IdUtils.MAPS_LOG, 0.0)));

        registerLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "GG", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
