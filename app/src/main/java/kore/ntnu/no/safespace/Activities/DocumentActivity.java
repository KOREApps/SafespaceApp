package kore.ntnu.no.safespace.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import kore.ntnu.no.safespace.R;

public class DocumentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        Spinner dropDown = findViewById(R.id.docProject);
        String[] projects = new String[]{"Robert blir ferdig", "Kristoffer får bank", "Oskar døde"}; //TODO erstatt dummy data med data fra server
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, projects);
        dropDown.setAdapter(adapter);

    }
}
