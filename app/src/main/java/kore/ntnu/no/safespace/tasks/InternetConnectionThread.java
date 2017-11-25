package kore.ntnu.no.safespace.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kore.ntnu.no.safespace.data.Documentation;
import kore.ntnu.no.safespace.data.IncidentReport;
import kore.ntnu.no.safespace.utils.ConnectionUtil;
import kore.ntnu.no.safespace.utils.StorageUtils;

/**
 * Class description..
 *
 * @author Kristoffer
 */
public class InternetConnectionThread extends Thread {
    private Context context;
    private List<AsyncTask> tasks = new ArrayList<>();

    public InternetConnectionThread(Context context) {
        this.context = context;
        start();
    }

    @Override
    public void run() {
        if (!ConnectionUtil.isConnected(context)) {
            try {
                sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            while (ConnectionUtil.isConnected(context)) {
                if (tasks.size() == 0) {
                    try {
                        List<File> docs = StorageUtils.getDocumentations(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS));
                        if (!docs.isEmpty()) {
                            for (File docFile : docs) {
                                Documentation doc = StorageUtils.readDocumentFromFile(docFile);
                                SendDocumentationTask sdt = new SendDocumentationTask(result -> {
                                    if (result != null) {
                                        StorageUtils.removeReport(doc, docFile);
                                        Toast.makeText(context, "The document: " + result.getResult().getTitle().toString() + " has been sent.", Toast.LENGTH_LONG).show();
                                    }
                                });
                                tasks.add(sdt);
                                sdt.execute(doc);
                            }
                        }
                        List<File> reports = StorageUtils.getIncidents(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS));
                        if (!reports.isEmpty()) {
                            for (File report : reports) {
                                IncidentReport incident = StorageUtils.readIncidentFromFile(report);
                                SendReportTask srt = new SendReportTask(result -> {
                                    if (result != null) {
                                        StorageUtils.removeReport(incident, report);
                                        Toast.makeText(context, "The report: " + result.getResult().getTitle().toString() + " has been sent.", Toast.LENGTH_LONG).show();
                                    }
                                });
                                tasks.add(srt);
                                srt.execute(incident);
                            }
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    for (AsyncTask task : tasks) {
                        if (task.getStatus() == AsyncTask.Status.FINISHED) {
                            tasks.remove(task);
                        }
                    }
                }

                try {
                    sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
