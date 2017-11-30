package kore.ntnu.no.safespace.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.adapters.LatestReportAdapter;
import kore.ntnu.no.safespace.data.Report;
import kore.ntnu.no.safespace.tasks.GetDocumentationsTask;
import kore.ntnu.no.safespace.tasks.GetReportsTask;
import kore.ntnu.no.safespace.utils.ConnectionUtil;
import kore.ntnu.no.safespace.utils.IdUtils;
import kore.ntnu.no.safespace.utils.StorageUtils;

/**
 * This purpose of this activity is to display latest reports/documents in a list.
 *
 * @author Erik
 */
public class LatestReportActivity extends AppCompatActivity {

    private LatestReportAdapter adapter;
    private ArrayList<Report> arrayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latestreport);

        adapter = new LatestReportAdapter(this);
        arrayList = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.rv_reports);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getApplicationContext().getResources().getDrawable(R.drawable.border_recyclerview));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        if(ConnectionUtil.isConnected(this)) {
            new GetDocumentationsTask(c -> {
                arrayList.addAll(c.getResult());
                adapter.addReports(c.getResult());
            },this).execute();
            new GetReportsTask(c -> {
                arrayList.addAll(c.getResult());
                adapter.addReports(c.getResult());
            }, this).execute();
        } else{
        localReports();
        }

        adapter.setListener(position -> {
            Intent intent = new Intent(LatestReportActivity.this, DisplayReportActivity.class);
            intent.putExtra(IdUtils.REPORT, adapter.getReportFromList(position));
            startActivity(intent);
        });

    }

    private void filter(String text) {
        List<Report> filteredList = new ArrayList<>();

        for(Report report : arrayList) {
            if(report.getTitle().toLowerCase().contains(text.toLowerCase()) || report.getDescription().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(report);
            }
        }
        adapter.filterList(filteredList);
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
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);

                return false;
            }
        });
        return true;
    }

}
