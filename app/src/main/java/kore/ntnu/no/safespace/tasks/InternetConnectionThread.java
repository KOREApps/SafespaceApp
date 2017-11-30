package kore.ntnu.no.safespace.tasks;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.NotificationCompat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
    private HashMap<File, AsyncTask> currentlySending = new HashMap<>();
    private static boolean running = false;

    public InternetConnectionThread(Context context) {
        this.context = context;
        running = true;
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void run() {
        List<File> docs = StorageUtils.getDocumentations(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS));
        List<File> reports = StorageUtils.getIncidents(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS));
        while(running) {
            if(docs.isEmpty() && reports.isEmpty()){
                StorageUtils.removeUnusedImages(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
            }
            if (!ConnectionUtil.isConnected(context)) {
                try {
                    sleep(60000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                while (ConnectionUtil.isConnected(context)) {
                    try {
                        if (!docs.isEmpty()) {
                            for (File docFile : docs) {
                                if (currentlySending.get(docFile) == null) {
                                    Documentation doc = StorageUtils.readDocumentFromFile(docFile);
                                    SendDocumentationTask sdt = new SendDocumentationTask(result -> {
                                        if (result != null) {
                                            StorageUtils.removeReport(doc, docFile);
                                            currentlySending.remove(docFile);
                                            fileSentNotification(result.getResult().getTitle(), result.getResult().getDescription().substring(0, Math.min(20, result.getResult().getDescription().length())), "Documentation");
                                        }
                                    });
                                    currentlySending.put(docFile, sdt);
                                    sdt.execute(doc);
                                }
                            }
                        }
                        if (!reports.isEmpty()) {
                            for (File report : reports) {
                                if (currentlySending.get(report) == null) {
                                    IncidentReport incident = StorageUtils.readIncidentFromFile(report);
                                    SendReportTask srt = new SendReportTask(result -> {
                                        if (result != null) {
                                            StorageUtils.removeReport(incident, report);
                                            currentlySending.remove(report);
                                            fileSentNotification(result.getResult().getTitle(), result.getResult().getDescription().substring(0, Math.min(20, result.getResult().getDescription().length())), "Report");
                                        }
                                    });
                                    currentlySending.put(report, srt);
                                    srt.execute(incident);
                                }
                            }
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void stopSender(){
        running = false;
    }

    public void fileSentNotification(String title, String message, String project) {
        NotificationCompat.Builder b = new NotificationCompat.Builder(this.context);
        b.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_complete_symbol)
                .setContentInfo(project)
                //.setTicker("Project successfully sent!")
                .setContentTitle('"' + title + '"' + " has been sent!")
                .setContentText(message + "...").setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager nm = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, b.build());
    }

}
