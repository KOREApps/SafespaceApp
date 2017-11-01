package kore.ntnu.no.safespace.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import kore.ntnu.no.safespace.R;


public class DisplayReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_report);
        TextView textView = findViewById(R.id.display_report_description);
        textView.setMovementMethod(new ScrollingMovementMethod());
    }
}
