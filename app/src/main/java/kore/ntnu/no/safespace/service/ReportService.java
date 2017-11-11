package kore.ntnu.no.safespace.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import kore.ntnu.no.safespace.activities.MainActivity;
import kore.ntnu.no.safespace.data.IncidentReport;
import kore.ntnu.no.safespace.data.ValidCheckResult;
import kore.ntnu.no.safespace.dto.ReportDTO;

/**
 * Created by Robert on 11-Nov-17.
 */

public class ReportService implements RestClient<IncidentReport, Long> {

    private static final String URL = MainActivity.URL  + "/reports";
    private static final Type LIST_TYPE = new TypeToken<List<ReportDTO>>(){}.getType();

    private HttpService http;
    private Gson gson;

    public ReportService() {
        this.http = new HttpService();
        this.gson = new Gson();
    }

    @Override
    public List<IncidentReport> getAll() throws IOException {
        HttpResponse response = http.get(URL);
        List<ReportDTO> reports = gson.fromJson(response.getResponse(), LIST_TYPE);
        List<IncidentReport> incidentReports = new ArrayList<>();
        for (ReportDTO reportDTO : reports) {
            incidentReports.add(getReport(reportDTO));
        }
        return incidentReports;
    }

    @Override
    public IncidentReport getOne(Long id) throws IOException {
        HttpResponse response = http.get(URL + "/" + id);
        ReportDTO report = gson.fromJson(response.getResponse(), ReportDTO.class);
        return getReport(report);
    }

    @Override
    public IncidentReport add(IncidentReport incidentReport) throws IOException {
        ReportDTO newReportDTO = getDto(incidentReport);
        HttpResponse response = http.post(URL, gson.toJson(newReportDTO));
        if (response.getCode() == 200) {
            ReportDTO reportDTO = gson.fromJson(response.getResponse(), ReportDTO.class);
            return getReport(reportDTO);
        } else {
            ValidCheckResult result = gson.fromJson(response.getResponse(), ValidCheckResult.class);
            throw new IOException(result.getMessage());
        }
    }

    @Override
    public IncidentReport update(IncidentReport incidentReport) throws IOException {
        return null;
    }

    private IncidentReport getReport(ReportDTO dto) {
        return new IncidentReport(dto.getId(), dto.getTitle(), dto.getDescription());
    }

    private ReportDTO getDto(IncidentReport report) {
        return new ReportDTO(report.getId(), report.getTitle(), report.getDescription(), report.getProject());
    }
}
