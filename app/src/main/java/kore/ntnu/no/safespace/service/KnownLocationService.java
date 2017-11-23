package kore.ntnu.no.safespace.service;

import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import kore.ntnu.no.safespace.activities.MainActivity;
import kore.ntnu.no.safespace.data.KnownLocation;
import kore.ntnu.no.safespace.data.ValidCheckResult;
import kore.ntnu.no.safespace.service.http.HttpResponse;
import kore.ntnu.no.safespace.service.http.HttpService;
import kore.ntnu.no.safespace.utils.IdUtils;

/**
 * Created by robert on 11/21/17.
 */

public class KnownLocationService implements RestClient<KnownLocation, Long> {

    private static final String URL = IdUtils.URL + "/locations";
    private static final Type LIST_TYPE = new TypeToken<List<KnownLocation>>(){}.getType();

    private HttpService httpService;
    private Gson gson;

    public KnownLocationService() {
        this.httpService = new HttpService();
        this.gson = new Gson();
    }

    /**
     * Return a ServiceResult containing a known location if the location given were
     * within the radius of a known location.
     * @param location Current location
     * @return ServiceResult containing KnownLocation if one was found.
     */
    public ServiceResult<KnownLocation> getCurrentLocation(Location location) throws IOException {
        HttpResponse response = httpService.post(URL + "/current", gson.toJson(location));
        if (response.getCode() == 200) {
            KnownLocation knownLocation = gson.fromJson(response.getResponse(), KnownLocation.class);
            return new ServiceResult<>(knownLocation, true, "OK");
        } else if (response.getCode() == 204) {
            return new ServiceResult<>(null, false, "No known locations in range");
        } else {
            ValidCheckResult checkResult = gson.fromJson(response.getResponse(), ValidCheckResult.class);
            return new ServiceResult<>(null, false, checkResult.getMessage());
        }
    }

    public ServiceResult<List<KnownLocation>> getNearbyLocations(Location location) throws IOException {
        HttpResponse response = httpService.post(URL + "/nearest", gson.toJson(location));
        if (response.isSuccess()) {
            List<KnownLocation> knownLocations = gson.fromJson(response.getResponse(), LIST_TYPE);
            return new ServiceResult<>(knownLocations, true, "OK");
        } else {
            ValidCheckResult checkResult = gson.fromJson(response.getResponse(), ValidCheckResult.class);
            return new ServiceResult<>(null, false, checkResult.getMessage());
        }
    }

    public ServiceResult<List<KnownLocation>> getNearestLocations(Location location) throws IOException {
        HttpResponse response = httpService.post(URL + "/nearest" + "?number=1", gson.toJson(location));
        if (response.isSuccess()) {
            List<KnownLocation> knownLocations = gson.fromJson(response.getResponse(), LIST_TYPE);
            return new ServiceResult<>(knownLocations, true, "OK");
        } else {
            ValidCheckResult checkResult = gson.fromJson(response.getResponse(), ValidCheckResult.class);
            return new ServiceResult<>(null, false, checkResult.getMessage());
        }
    }

    @Override
    public ServiceResult<List<KnownLocation>> getAll() throws IOException {
        HttpResponse response = httpService.get(URL);
        ServiceResult<List<KnownLocation>> serviceResult = new ServiceResult<List<KnownLocation>>(gson.fromJson(response.getResponse(), LIST_TYPE), true, "OK");
        return serviceResult;
    }

    @Override
    public ServiceResult<KnownLocation> getOne(Long id) throws IOException {
        HttpResponse response = httpService.get(URL + "/" + id);
        ServiceResult<KnownLocation> serviceResult = new ServiceResult<KnownLocation>(gson.fromJson(response.getResponse(), KnownLocation.class), true, "OK");
        return serviceResult;
    }

    @Override
    public ServiceResult<KnownLocation> add(KnownLocation knownLocation) throws IOException {
        HttpResponse response = httpService.post(URL, gson.toJson(knownLocation));
        if (response.getCode() == 200) {
            KnownLocation location = gson.fromJson(response.getResponse(), KnownLocation.class);
            ServiceResult<KnownLocation> serviceResult = new ServiceResult<>(location, true, "OK");
            return serviceResult;
        } else {
            ValidCheckResult result = gson.fromJson(response.getResponse(), ValidCheckResult.class);
            return new ServiceResult<>(null, false, result.getMessage());
        }
    }

    @Override
    public ServiceResult<KnownLocation> update(KnownLocation knownLocation) throws IOException {
        return null;
    }
}
