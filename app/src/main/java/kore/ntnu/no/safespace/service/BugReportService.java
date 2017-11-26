package kore.ntnu.no.safespace.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import kore.ntnu.no.safespace.data.BugReport;
import kore.ntnu.no.safespace.data.ValidCheckResult;
import kore.ntnu.no.safespace.service.http.HttpResponse;
import kore.ntnu.no.safespace.service.http.HttpService;
import kore.ntnu.no.safespace.utils.IdUtils;

/**
 * Class description..
 *
 * @author Robert
 */
public class BugReportService implements RestClient<BugReport, Long> {

    private static final String URL = IdUtils.URL + "/bug-reports";
    private static final Type LIST_TYPE = new TypeToken<List<BugReport>>(){}.getType();

    private HttpService http;
    private Gson gson;

    public BugReportService() {
        this.http = new HttpService();
        this.gson = new Gson();
    }

    @Override
    public ServiceResult<List<BugReport>> getAll() throws IOException {
        HttpResponse response = http.get(URL);
        if (response.getCode() == 200) {
            List<BugReport> bugReports = gson.fromJson(response.getResponse(), LIST_TYPE);
            return new ServiceResult<>(bugReports, true, "OK");
        } else {
            ValidCheckResult result = gson.fromJson(response.getResponse(), ValidCheckResult.class);
            return new ServiceResult<>(null, false, result.getMessage());
        }
    }

    @Override
    public ServiceResult<BugReport> getOne(Long id) throws IOException {
        HttpResponse response = http.get(URL + "/" + id);
        if (response.getCode() == 200) {
            BugReport bugReport = gson.fromJson(response.getResponse(), LIST_TYPE);
            return new ServiceResult<>(bugReport, true, "OK");
        } else {
            ValidCheckResult result = gson.fromJson(response.getResponse(), ValidCheckResult.class);
            return new ServiceResult<>(null, false, result.getMessage());
        }
    }

    @Override
    public ServiceResult<BugReport> add(BugReport bugReport) throws IOException {
        HttpResponse response = http.post(URL, gson.toJson(bugReport));
        if (response.getCode() == 200) {
            BugReport newReport = gson.fromJson(response.getResponse(), BugReport.class);
            return new ServiceResult<>(newReport, true, "OK");
        } else {
            ValidCheckResult result = gson.fromJson(response.getResponse(), ValidCheckResult.class);
            return new ServiceResult<>(null, false, result.getMessage());
        }
    }

    @Override
    public ServiceResult<BugReport> update(BugReport bugReport) throws IOException {
        return null;
    }

}
