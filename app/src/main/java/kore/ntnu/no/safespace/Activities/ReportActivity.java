package kore.ntnu.no.safespace.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import kore.ntnu.no.safespace.R;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        EditText reportHeader = findViewById(R.id.reportHeaderText);
        ImageView takenPhoto = findViewById(R.id.reportTakenPhoto);
        ImageButton capturePhoto = findViewById(R.id.takePhotoBtn);
        ImageView takenPhotos = findViewById(R.id.reportTakenPhotos);
        EditText reportDescription = findViewById(R.id.reportDescription);
        Button sendReport = findViewById(R.id.sendReportBtn);


    }


}
