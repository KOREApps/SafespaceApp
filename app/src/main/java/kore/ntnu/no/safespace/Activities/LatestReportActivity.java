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
        adapter.addReport(new IncidentReport(null,"One", null, null, null));
        adapter.addReport(new IncidentReport(null,"Two", null, null, null));
        adapter.addReport(new IncidentReport(null,"Three", null, null, null));
        adapter.addReport(new IncidentReport(null,"Four", null, null, null));
        adapter.addReport(new IncidentReport(null,"Five", null, null, null));
        adapter.addReport(new IncidentReport(null,"Six", null, null, null));
        adapter.addReport(new IncidentReport(null,"Seven", null, null, null));
        adapter.addReport(new IncidentReport(null,"Eight", null, null, null));
        adapter.addReport(new IncidentReport(null,"Nine", null, null, null));
        adapter.addReport(new IncidentReport(null,"Ten", null, null, null));
    }
}
