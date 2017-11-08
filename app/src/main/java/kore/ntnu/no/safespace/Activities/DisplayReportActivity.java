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
import kore.ntnu.no.safespace.Data.IncidentReport;
import kore.ntnu.no.safespace.Data.Project;
import kore.ntnu.no.safespace.Data.Report;
import kore.ntnu.no.safespace.R;


public class DisplayReportActivity extends AppCompatActivity {
    private RecyclerView imagePreviewer;
    private ImageDisplayAdapter adapter;
    private Report report;
    private TextView title;
    private TextView description;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_report);
        title = findViewById(R.id.display_report_title);
        description = findViewById(R.id.display_report_description);
        description.setMovementMethod(new ScrollingMovementMethod());
        Intent intent = getIntent();
        report = (Report) intent.getSerializableExtra("ID"); //TODO: USE CORRECT ID!.
        if(report == null){
            useTemplateReport();
        }
        title.setText(report.getProject().getTitle());
        description.setText(report.getDescription());
        imagePreviewer = findViewById(R.id.display_report_recyclerView);
        imagePreviewer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new ImageDisplayAdapter(this);
        imagePreviewer.setAdapter(adapter);
        adapter.setOnClickListener(position -> openImage(adapter.getImage(position)));
        adapter.setImages(report.getImages());
        updateView();
    }

    private void useTemplateReport() {
        Report report = new IncidentReport(((long) 0),getTemplateDescription(), getTemplateImages(), null, new Project(((long) 0),"Template Project Title", getTemplateDescription(), null));
        this.report = report;
    }

    private String getTemplateDescription() {
        return "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum at urna id dolor rutrum pharetra. Integer quis mauris risus. Integer ullamcorper dolor a porttitor pellentesque. " +
                "Sed efficitur vitae nisl in feugiat. Donec interdum accumsan sem. Maecenas ut cursus odio. Quisque risus urna, hendrerit sit amet urna vel, pulvinar semper dui. Etiam vestibulum, " +
                "dui nec luctus volutpat, libero eros semper ante, sit amet lacinia lacus ipsum nec tortor. Sed eget velit vel ex dapibus euismod et nec ipsum. Vivamus porta sem turpis, molestie " +
                "rutrum nibh cursus eu. Etiam sit amet fringilla urna, id iaculis purus. Phasellus et molestie justo, id ultrices libero. Morbi ut magna eu lacus feugiat placerat. Praesent ex orci, " +
                "finibus id vulputate nec, hendrerit in dui. Cras quis lacinia nunc, vel placerat nunc.\n" +
                "\n" +
                "Maecenas sit amet placerat mauris. Praesent ut lacinia felis. Fusce fermentum purus elit, non egestas dui semper a. Praesent in eleifend felis. Sed finibus mi id dui pulvinar, " +
                "nec ultricies urna suscipit. Sed maximus quam non felis mattis, vitae rutrum odio fermentum. Vestibulum pulvinar sit amet erat egestas pretium. Praesent faucibus eros purus, " +
                "vitae faucibus nisl facilisis at. Nunc sit amet congue nisi, ut sodales dui. Nam non venenatis dolor. Fusce quis lacus vel ex iaculis viverra non id arcu. Fusce bibendum feugiat ligula nec venenatis.\n" +
                "\n" +
                "Aenean malesuada lobortis tortor in dapibus. Vivamus et vehicula nunc, id fermentum nisl. Pellentesque nec interdum magna. Vivamus scelerisque venenatis laoreet. " +
                "Nam quis massa aliquet, semper felis id, tincidunt magna. Donec a dignissim arcu. Quisque egestas, velit sit amet sollicitudin luctus, purus metus tristique sem," +
                " et sodales arcu tortor ut dui. Vivamus dapibus, quam eget rutrum sollicitudin, est magna condimentum elit, ut congue urna mauris eu sapien.\n" +
                "\n" +
                "Duis consectetur dolor sodales risus ullamcorper, tincidunt elementum elit efficitur. Nulla dui magna, laoreet ac lobortis volutpat, fermentum a odio. P" +
                "hasellus nec nunc id nisi consectetur luctus ac ac ex. Nunc non interdum justo. Donec sed metus quis dui mollis accumsan in vitae risus. Vivamus vitae " +
                "mauris efficitur, mollis felis eget, lobortis elit. Sed non condimentum sem. Proin porttitor eget tellus nec porttitor. Fusce leo sem, semper sed massa n" +
                "on, venenatis vestibulum arcu. Cras eget elit pellentesque, ornare quam id, pharetra purus. Etiam pulvinar vitae magna sed pretium. Donec aliquet, nunc s" +
                "it amet congue interdum, quam turpis fringilla lacus, eu condimentum ante leo id mauris. Curabitur rhoncus ligula id tellus condimentum, semper auctor ips" +
                "um ultricies. Maecenas ullamcorper orci eget metus pulvinar euismod ac et mauris. Curabitur bibendum bibendum orci at venenatis.\n" +
                "\n" +
                "Phasellus in mauris lobortis, posuere turpis non, accumsan magna. Duis ut fringilla augue, quis vulputate neque. Cras vitae neque sed odio viverra blandit" +
                " eget nec ex. Integer aliquam, quam at molestie scelerisque, nunc augue faucibus nulla, at consectetur purus nunc eu enim. Class aptent taciti sociosqu ad " +
                "litora torquent per conubia nostra, per inceptos himenaeos. Ut metus purus, egestas vitae tortor rutrum, tincidunt semper mi. Praesent dapibus eget ante eu laoreet.";
    }

    private List<Image> getTemplateImages() {
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
