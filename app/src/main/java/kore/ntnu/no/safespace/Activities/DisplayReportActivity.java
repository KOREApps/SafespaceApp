package kore.ntnu.no.safespace.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import kore.ntnu.no.safespace.Adapters.ImageDisplayAdapter;
import kore.ntnu.no.safespace.Data.Image;
import kore.ntnu.no.safespace.R;


public class DisplayReportActivity extends AppCompatActivity {
    private RecyclerView imagePreviewer;
    private ImageDisplayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_report);
        TextView textView = findViewById(R.id.display_report_description);
        textView.setMovementMethod(new ScrollingMovementMethod());
        imagePreviewer = findViewById(R.id.display_report_recyclerView);
        imagePreviewer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new ImageDisplayAdapter(this);
        imagePreviewer.setAdapter(adapter);
        adapter.setListener(position -> openImage(adapter.getImage(position)));
        adapter.setImages(getImages());
        updateView();
    }

    private List<Image> getImages() {
        //TODO: get images from database
        //This will (most likely) not produce any images on any other phone than Krstoffers Z3 (the images are stored on disk)
        //<Test section>
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        List<Image> list = new ArrayList<>();
        for(int i = 1; i<4; i++) {
            File imgFile = new File(storageDir, "img" + i + ".jpg");
            list.add(new Image(imgFile));
        }
        //</Test section>
        return list;
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
}
