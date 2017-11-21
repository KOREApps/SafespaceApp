package kore.ntnu.no.safespace.tasks;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.List;

import kore.ntnu.no.safespace.data.Documentation;
import kore.ntnu.no.safespace.data.IncidentReport;
import kore.ntnu.no.safespace.utils.ConnectionUtil;
import kore.ntnu.no.safespace.utils.StorageUtils;

/**
 * Created by Kristoffer on 2017-11-15.
 */

public class InternetConnectionThread extends Thread {
    private Context context;

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
                //TODO: send Images.
                try {
                    List<File> docs = StorageUtils.getDocumentations(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS));
                    if (!docs.isEmpty()) {
                        for (File docFile : docs) {
                            Documentation doc = StorageUtils.readDocumentFromFile(docFile);
                            SendDocumentationTask sdt = new SendDocumentationTask(result -> {
                                System.out.println("Sent");
                                if (result != null) {
                                    StorageUtils.removeReport(doc, docFile);
                                }
                            });
                            sdt.execute(doc);
                            System.out.println("Executed");
                        }
                    }
                    List<File> reports = StorageUtils.getIncidents(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS));
                    if (!reports.isEmpty()) {
                        for (File report : reports) {
                            IncidentReport incident = StorageUtils.readIncidentFromFile(report);
                            new SendReportTask(result -> {
                                if (result != null) {
                                    StorageUtils.removeReport(incident, report);
                                }
                            }).execute(incident);
                        }
                    }
                    sleep(300000);
                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
