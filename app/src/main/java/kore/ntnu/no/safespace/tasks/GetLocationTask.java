package kore.ntnu.no.safespace.tasks;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import kore.ntnu.no.safespace.ApplicationContext;
import kore.ntnu.no.safespace.data.Location;

/**
 * Created by robert on 11/21/17.
 */

public class GetLocationTask extends AsyncTask<Void, Integer, AsyncTaskResult<Location>> {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;
    private AsyncOnPostExecute<Location> callback;
    //private final Object LOCK = new Object();

    public GetLocationTask(AsyncOnPostExecute<Location> callback) {
        this.callback = callback;
    }

    @Override
    protected AsyncTaskResult<Location> doInBackground(Void... voids) {
        while (location.getLatitude() == 0.0 || location.getAccuracy() > 50) {

            }
        return new AsyncTaskResult<>(location);
    }

    @Override
    protected void onPreExecute() {
        Context context = ApplicationContext.getContext();
        location = new Location(0.0, 0.0, 9001);
        locationListener = new TaskLocationListener(this);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates("gps", 0,0,locationListener);
    }

    @Override
    protected void onCancelled() {
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Location> locationAsyncTaskResult) {
        locationManager.removeUpdates(locationListener);
        if (callback != null) {
            callback.onPostExecute(locationAsyncTaskResult);
        }
    }

    public void setLocation(Location location){
            this.location = location;
    }

    private class TaskLocationListener implements LocationListener {

        private GetLocationTask locationTask;

        public TaskLocationListener(GetLocationTask locationTask) {
            this.locationTask = locationTask;
        }

        @Override
        public void onLocationChanged(android.location.Location location) {
            double lat = location.getLatitude();
            double log = location.getLongitude();
            int acc = (int) location.getAccuracy();
            locationTask.setLocation(new Location(lat, log, acc));
            System.out.println("Accuracy: " + acc);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i(TaskLocationListener.class.getSimpleName(), "onStatusChange");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i(TaskLocationListener.class.getSimpleName(), "onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(TaskLocationListener.class.getSimpleName(), "onProviderDisabled");
        }
    }

}
