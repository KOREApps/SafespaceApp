package kore.ntnu.no.safespace.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import kore.ntnu.no.safespace.data.Project;
import kore.ntnu.no.safespace.data.ValidCheckResult;
import kore.ntnu.no.safespace.service.http.HttpResponse;
import kore.ntnu.no.safespace.service.http.HttpService;
import kore.ntnu.no.safespace.utils.IdUtils;

/**
 * Class that handles communication with backend when performing Create, Read or Update operations
 * on projects.
 *
 * @author Robert
 */
public class ProjectService implements RestClient<Project, Long> {

    private static final String URL = IdUtils.URL + "/projects";
    private static final Type LIST_TYPE = new TypeToken<List<Project>>(){}.getType();

    private HttpService http;
    private Gson gson;

    public ProjectService() {
        this.http = new HttpService();
        this.gson = new Gson();
    }

    @Override
    public ServiceResult<List<Project>> getAll() throws IOException {
        ServiceResult<List<Project>> serviceResult = new ServiceResult<>(
                gson.fromJson(http.get(URL).getResponse(), LIST_TYPE), true, "OK");
        return serviceResult;
    }

    @Override
    public ServiceResult<Project> getOne(Long id) throws IOException {
        ServiceResult<Project> serviceResult = new ServiceResult<>(
                gson.fromJson(http.get(URL + "/" + id).getResponse(), Project.class), true, "OK");
        return serviceResult;
    }

    @Override
    public ServiceResult<Project> add(Project project) throws IOException {
        HttpResponse response = http.post(URL, gson.toJson(project));
        if (response.getCode() == 200) {
            Project newProject = gson.fromJson(response.getResponse(), Project.class);
            ServiceResult<Project> serviceResult = new ServiceResult<>(newProject, true, "OK");
            return serviceResult;
        } else {
            ValidCheckResult result = gson.fromJson(response.getResponse(), ValidCheckResult.class);
            throw new IOException(result.getMessage());
        }
    }

    @Override
    public ServiceResult<Project> update(Project project) throws IOException {
        return null;
    }
}
