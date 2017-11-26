package kore.ntnu.no.safespace.tasks;

import android.os.AsyncTask;

import java.io.IOException;

import kore.ntnu.no.safespace.data.KnownLocation;
import kore.ntnu.no.safespace.service.KnownLocationService;
import kore.ntnu.no.safespace.service.ServiceResult;

/**
 * The purpose of this class is to retrieve the nearest location from the server.
 *
 * @author Oscar
 */
public class GetNearestLocationTask extends AsyncTask<KnownLocation, Integer, AsyncTaskResult<KnownLocation>> {

    private KnownLocationService knownLocationService;
    private AsyncOnPostExecute<KnownLocation> callback;

    public GetNearestLocationTask(AsyncOnPostExecute<KnownLocation> callback) {
        this.knownLocationService = new KnownLocationService();
        this.callback = callback;
    }

    @Override
    protected AsyncTaskResult<KnownLocation> doInBackground(KnownLocation... locations) {
        KnownLocation location = locations[0];
        try {
            ServiceResult<KnownLocation> serviceResult = knownLocationService.getNearestLocations(location);
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