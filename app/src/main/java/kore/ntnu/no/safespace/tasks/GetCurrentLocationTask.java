package kore.ntnu.no.safespace.tasks;

import android.os.AsyncTask;

import java.io.IOException;

import kore.ntnu.no.safespace.data.KnownLocation;
import kore.ntnu.no.safespace.data.Location;
import kore.ntnu.no.safespace.service.KnownLocationService;
import kore.ntnu.no.safespace.service.ServiceResult;

/**
 * The purpose of this class is to get your current location from the server.
 * If you are within the radius of a location that is stored on the server, you will receive a
 * location, else you will get back latitude/longitude coordinates.
 *
 * @author Robert
 */
public class GetCurrentLocationTask extends AsyncTask<Location, Integer, AsyncTaskResult<KnownLocation>>{

    private KnownLocationService knownLocationService;
    private AsyncOnPostExecute<KnownLocation> callback;

    public GetCurrentLocationTask(AsyncOnPostExecute<KnownLocation> callback){
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
