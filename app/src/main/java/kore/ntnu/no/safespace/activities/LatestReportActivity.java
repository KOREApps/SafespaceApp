package kore.ntnu.no.safespace.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.adapters.DocumentationAdapter;
import kore.ntnu.no.safespace.data.Documentation;
import kore.ntnu.no.safespace.utils.StorageUtils;

/**
 * Created by OscarWika on 31.10.2017.
 */

public class LatestReportActivity extends AppCompatActivity {
    DocumentationAdapter adapter;
    public static final String REPORT = "kore.ntnu.safespace.REPORT";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latestreport);

        adapter = new DocumentationAdapter(this);

        RecyclerView rv = findViewById(R.id.rv_reports);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv.setAdapter(adapter);
        fillReports();

        adapter.setListener(new DocumentationAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(LatestReportActivity.this, DisplayReportActivity.class);
                intent.putExtra(REPORT, adapter.getReportFromList(position));
                startActivity(intent);
            }
        });
    }

    void fillReports() {
        try {
            List<Documentation> list = StorageUtils.getDocumentationsFromFile(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS));
            for(Documentation d : list){
                adapter.addReport(d);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
