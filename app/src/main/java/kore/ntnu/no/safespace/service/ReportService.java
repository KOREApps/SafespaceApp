package kore.ntnu.no.safespace.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import kore.ntnu.no.safespace.data.Image;
import kore.ntnu.no.safespace.data.IncidentReport;
import kore.ntnu.no.safespace.data.ValidCheckResult;
import kore.ntnu.no.safespace.dto.ReportDTO;
import kore.ntnu.no.safespace.service.http.HttpResponse;
import kore.ntnu.no.safespace.service.http.HttpService;
import kore.ntnu.no.safespace.utils.IdUtils;

/**
 * Created by Robert on 11-Nov-17.
 */

public class ReportService implements RestClient<IncidentReport, Long> {

    private static final String URL = IdUtils.URL  + "/reports";
    private static final Type LIST_TYPE = new TypeToken<List<ReportDTO>>(){}.getType();
    private static final Type IMAGE_LIST_TYPE = new TypeToken<List<Image>>(){}.getType();

    private HttpService http;
    private Gson gson;

    public ReportService() {
        this.http = new HttpService();
        this.gson = new Gson();
    }

    @Override
    public ServiceResult<List<IncidentReport>> getAll() throws IOException {
        HttpResponse response = http.get(URL);
        List<ReportDTO> reports = gson.fromJson(response.getResponse(), LIST_TYPE);
        List<IncidentReport> incidentReports = new ArrayList<>();
        for (ReportDTO reportDTO : reports) {
            incidentReports.add(getReport(reportDTO));
        }
        ServiceResult<List<IncidentReport>> serviceResult = new ServiceResult<>(incidentReports, true, "OK");
        return serviceResult;
    }

    @Override
    public ServiceResult<IncidentReport> getOne(Long id) throws IOException {
        HttpResponse response = http.get(URL + "/" + id);
        ReportDTO report = gson.fromJson(response.getResponse(), ReportDTO.class);
        ServiceResult<IncidentReport> serviceResult = new ServiceResult<>(getReport(report), true, "OK");
        return serviceResult;
    }

    @Override
    public ServiceResult<IncidentReport> add(IncidentReport incidentReport) throws IOException {
        ReportDTO newReportDTO = getDto(incidentReport);
        HttpResponse response = http.post(URL, gson.toJson(newReportDTO));
        if (response.getCode() == 200) {
            ReportDTO reportDTO = gson.fromJson(response.getResponse(), ReportDTO.class);
            ServiceResult<IncidentReport> serviceResult = new ServiceResult<>(getReport(reportDTO), true, "OK");
            return serviceResult;
        } else {
            ValidCheckResult result = gson.fromJson(response.getResponse(), ValidCheckResult.class);
            throw new IOException(result.getMessage());
        }
    }

    @Override
    public ServiceResult<IncidentReport> update(IncidentReport incidentReport) throws IOException {
        return null;
    }

    public List<Image> getImagesForReport(Long reportId){
        try {
            final String url = URL + "/" + reportId + "/images";
            HttpResponse response = http.get(url);
            return gson.fromJson(response.getResponse(), IMAGE_LIST_TYPE);
        } catch (IOException ex) {
            Log.e(ImageService.class.getSimpleName(), "Failed to post image");
            return null;
        }
    }

    private IncidentReport getReport(ReportDTO dto) {
        return new IncidentReport(dto.getId(), dto.getTitle(), dto.getDescription());
    }

    private ReportDTO getDto(IncidentReport report) {
        return new ReportDTO(report.getId(), report.getTitle(), report.getDescription(), report.getProject());
    }
}
