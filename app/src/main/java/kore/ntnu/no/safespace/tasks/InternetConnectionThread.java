package kore.ntnu.no.safespace.tasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.List;

import kore.ntnu.no.safespace.utils.StorageUtils;

/**
 * Created by Kristoffer on 2017-11-15.
 */

public class InternetConnectionThread extends Thread {
    private Context context;

    public InternetConnectionThread(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        if (!hasConnection()) {
            try {
                sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            while (hasConnection()) {
                //TODO: send Images.
                try {
                    List<File> docs = StorageUtils.getDocumentations(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                    if (docs.isEmpty()) {
                        for (File doc : docs) {
                            new SendDocumentationTask(result -> {
                                if (result != null) {
                                    StorageUtils.removeFile(doc);
                                }
                            }).execute(StorageUtils.readDocumentFromFile(doc));
                        }
                    }
                    List<File> reports = StorageUtils.getIncidents(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                    if (reports.isEmpty()) {
                        for (File report : reports) {
                            new SendReportTask(result -> {
                                if (result != null) {
                                    StorageUtils.removeFile(report);
                                }
                            }).execute(StorageUtils.readIncidentFromFile(report));
                        }
                    }
                    docs = StorageUtils.getDocumentations(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                    reports = StorageUtils.getIncidents(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                    if(docs.isEmpty() && reports.isEmpty()){
                        sleep(120000);
                    }
                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * Checks if the device has Internet connection.
     *
     * @return <code>true</code> if the phone is connected to the Internet.
     */
    public boolean hasConnection() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;
    }
}
