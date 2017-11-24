package kore.ntnu.no.safespace.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.IOException;
import java.util.List;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.adapters.LatestReportAdapter;
import kore.ntnu.no.safespace.tasks.GetDocumentationsTask;
import kore.ntnu.no.safespace.tasks.GetReportsTask;
import kore.ntnu.no.safespace.utils.ConnectionUtil;
import kore.ntnu.no.safespace.utils.IdUtils;
import kore.ntnu.no.safespace.utils.StorageUtils;

/**
 * Created by OscarWika on 31.10.2017.
 */

public class LatestReportActivity extends AppCompatActivity {

    private LatestReportAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latestreport);

        adapter = new LatestReportAdapter(this);

        RecyclerView rv = findViewById(R.id.rv_reports);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(adapter);
        if(ConnectionUtil.isConnected(this)) {
            new GetDocumentationsTask(c -> adapter.addReports(c.getResult()),this).execute();
            new GetReportsTask(c -> adapter.addReports(c.getResult()), this).execute();
        } else{
        localReports();
        }

        adapter.setListener(position -> {
            Intent intent = new Intent(LatestReportActivity.this, DisplayReportActivity.class);
            intent.putExtra(IdUtils.REPORT, adapter.getReportFromList(position));
            startActivity(intent);
        });
    }

    void localReports() {
        try {
            List<File> list = StorageUtils.getDocumentations(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS));
            for(File f : list){
                adapter.addReport(StorageUtils.readDocumentFromFile(f));
            }
            list.clear();
            list = StorageUtils.getIncidents(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS));
            for(File f : list){
                adapter.addReport(StorageUtils.readIncidentFromFile(f));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this add items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }
}
