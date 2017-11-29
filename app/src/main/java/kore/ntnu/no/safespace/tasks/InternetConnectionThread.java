package kore.ntnu.no.safespace.tasks;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.NotificationCompat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kore.ntnu.no.safespace.R;
import kore.ntnu.no.safespace.data.Documentation;
import kore.ntnu.no.safespace.data.IncidentReport;
import kore.ntnu.no.safespace.utils.ConnectionUtil;
import kore.ntnu.no.safespace.utils.StorageUtils;

/**
 * The purpose of this class is to check whether internet connection is available.
 * If connection is available - get documents/reports from storage and send them to the server.
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
                                        fileSentNotification(result.getResult().getTitle(), result.getResult().getDescription().substring(0, 20), "Documentation");
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
                                        fileSentNotification(result.getResult().getTitle(), result.getResult().getDescription().substring(0, 20), "Report");
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

    public void fileSentNotification(String title, String message, String project) {
        NotificationCompat.Builder b = new NotificationCompat.Builder(this.context);
        b.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_complete_symbol)
                .setContentInfo(project)
                //.setTicker("Project successfully sent!")
                .setContentTitle('"' + title + '"' +  " has been sent!")
                .setContentText(message + "...").setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager nm = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, b.build());
    }

}
