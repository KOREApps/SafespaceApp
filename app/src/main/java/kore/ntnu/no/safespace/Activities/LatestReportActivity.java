package kore.ntnu.no.safespace.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import kore.ntnu.no.safespace.Adapters.DocumentationAdapter;
import kore.ntnu.no.safespace.Data.IncidentReport;
import kore.ntnu.no.safespace.Data.Report;
import kore.ntnu.no.safespace.R;

/**
 * Created by OscarWika on 31.10.2017.
 */

public class LatestReportActivity extends AppCompatActivity {
    DocumentationAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latestreport);

        adapter = new DocumentationAdapter(this);

        RecyclerView rv = findViewById(R.id.rv_reports);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(adapter);
        fillReports();


    }

    void fillReports() {
        adapter.addReport(new IncidentReport((long) 1 ,"One", null, null, null));
        adapter.addReport(new IncidentReport((long) 2 ,"Two", null, null, null));
        adapter.addReport(new IncidentReport((long) 3 ,"Three", null, null, null));
        adapter.addReport(new IncidentReport((long) 4 ,"Four", null, null, null));
        adapter.addReport(new IncidentReport((long) 5 ,"Five", null, null, null));
        adapter.addReport(new IncidentReport((long) 6 ,"Six", null, null, null));
        adapter.addReport(new IncidentReport((long) 7 ,"Seven", null, null, null));
        adapter.addReport(new IncidentReport((long) 8 ,"Eight", null, null, null));
        adapter.addReport(new IncidentReport((long) 9 ,"Nine", null, null, null));
        adapter.addReport(new IncidentReport((long) 10 ,"Ten", null, null, null));
    }
}
