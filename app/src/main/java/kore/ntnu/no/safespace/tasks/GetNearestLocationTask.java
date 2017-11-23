package kore.ntnu.no.safespace.tasks;

import android.location.Location;
import android.os.AsyncTask;

import java.io.IOException;

import kore.ntnu.no.safespace.data.KnownLocation;
import kore.ntnu.no.safespace.service.KnownLocationService;
import kore.ntnu.no.safespace.service.ServiceResult;

/**
 * Created by OscarWika on 23.11.2017.
 */

public class GetNearestLocationTask extends AsyncTask<Location, Integer, AsyncTaskResult<KnownLocation>> {

    private KnownLocationService knownLocationService;
    private AsyncOnPostExecute<KnownLocation> callback;

    public GetNearestLocationTask(AsyncOnPostExecute<KnownLocation> callback) {
        this.knownLocationService = new KnownLocationService();
        this.callback = callback;
    }

    @Override
    protected AsyncTaskResult<KnownLocation> doInBackground(Location... locations) {
        Location location = locations[0];
        try {
            ServiceResult<KnownLocation> serviceResult = knownLocationService.getCurrentLocation(location);
            KnownLocation knownLocation = serviceResult.getObject();
            return new AsyncTaskResult<>(knownLocation);
        } catch (IOException e) {
            return new AsyncTaskResult<>(e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<KnownLocation> knownLocationAsyncTaskResult) {
        if (callback != null) {
            callback.onPostExecute(knownLocationAsyncTaskResult);
        }
    }
}