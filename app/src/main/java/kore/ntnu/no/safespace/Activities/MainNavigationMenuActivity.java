package kore.ntnu.no.safespace.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import kore.ntnu.no.safespace.R;

public class MainNavigationMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_navigation_menu);


        Button reportBtn = findViewById(R.id.rapportBtn);
        Button documentBtn = findViewById(R.id.dokumBtn);
        Button latestRepBtn = findViewById(R.id.latestBtn);
        Button helpBtn = findViewById(R.id.helpBtn);
        Button settingsBtn = findViewById(R.id.settingsBtn);

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainNavigationMenuActivity.this, ReportActivity.class);
                startActivity(intent);
            }
        });
        documentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainNavigationMenuActivity.this, DocumentActivity.class);
                startActivity(intent);
            }
        });


        //TODO GPS BUTTON LISTENER


        latestRepBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainNavigationMenuActivity.this, LatestReportActivity.class);
                startActivity(intent);
            }
        });

        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainNavigationMenuActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainNavigationMenuActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }
}
