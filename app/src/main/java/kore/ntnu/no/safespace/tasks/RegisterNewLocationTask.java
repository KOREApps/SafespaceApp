package kore.ntnu.no.safespace.tasks;

import android.os.AsyncTask;

import java.io.IOException;

import kore.ntnu.no.safespace.data.KnownLocation;
import kore.ntnu.no.safespace.service.KnownLocationService;
import kore.ntnu.no.safespace.service.ServiceResult;

/**
 * Class description..
 *
 * @author Robert
 */
public class RegisterNewLocationTask extends AsyncTask<KnownLocation, Integer, AsyncTaskResult<KnownLocation>> {

    private KnownLocationService locationService;
    private AsyncOnPostExecute<KnownLocation> callback;

    public RegisterNewLocationTask(AsyncOnPostExecute<KnownLocation> callback) {
        this.locationService = new KnownLocationService();
        this.callback = callback;
    }

    @Override
    protected AsyncTaskResult<KnownLocation> doInBackground(KnownLocation... knownLocations) {
        KnownLocation location = knownLocations[0];
        try {
            ServiceResult<KnownLocation> serviceResult = locationService.add(location);
            return new AsyncTaskResult<>(serviceResult.getObject());
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
