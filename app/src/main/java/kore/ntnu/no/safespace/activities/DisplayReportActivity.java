package kore.ntnu.no.safespace.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.adapters.ImageDisplayAdapter;
import kore.ntnu.no.safespace.data.Image;
import kore.ntnu.no.safespace.data.Report;
import kore.ntnu.no.safespace.utils.IdUtils;

/**
 * Class description..
 *
 * @author x
 */
public class DisplayReportActivity extends AppCompatActivity {

    private RecyclerView imagePreviewer;
    private ImageDisplayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_report);
        TextView title = findViewById(R.id.display_report_title);
        TextView description = findViewById(R.id.display_report_description);
        description.setMovementMethod(new ScrollingMovementMethod());
        Intent intent = getIntent();
        Report report = (Report) intent.getSerializableExtra(IdUtils.REPORT);
        title.setText(report.getTitle());
        description.setText(report.getDescription());
        imagePreviewer = findViewById(R.id.display_report_recyclerView);
        imagePreviewer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new ImageDisplayAdapter(this);
        imagePreviewer.setAdapter(adapter);
        adapter.setOnClickListener(position -> openImage(adapter.getImage(position)));
        adapter.setImages(report.getImages());
        updateView();

        findViewById(R.id.display_report_ok_button).setOnClickListener(c-> finish());
    }

    public void updateView() {
        imagePreviewer.post(() -> imagePreviewer.smoothScrollToPosition(adapter.getItemCount()/2));
    }
    private void openImage(Image image){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("file://" + image.getImageFile().getAbsolutePath());
        intent.setDataAndType(data, "image/*");
        startActivity(intent);
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
}
