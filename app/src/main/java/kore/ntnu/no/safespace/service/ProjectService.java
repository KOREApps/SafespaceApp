package kore.ntnu.no.safespace.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import kore.ntnu.no.safespace.activities.MainActivity;
import kore.ntnu.no.safespace.data.Project;
import kore.ntnu.no.safespace.data.ValidCheckResult;

/**
 * Created by robert on 11/13/17.
 */

public class ProjectService implements RestClient<Project, Long> {

    private static final String URL = MainActivity.URL + "/projects";
    private static final Type LIST_TYPE = new TypeToken<List<Project>>(){}.getType();

    private HttpService http;
    private Gson gson;

    public ProjectService() {
        this.http = new HttpService();
        this.gson = new Gson();
    }

    @Override
    public List<Project> getAll() throws IOException {
        return gson.fromJson(http.get(URL).getResponse(), LIST_TYPE);
    }

    @Override
    public Project getOne(Long id) throws IOException {
        return gson.fromJson(http.get(URL + "/" + id).getResponse(), Project.class);
    }

    @Override
    public Project add(Project project) throws IOException {
        HttpResponse response = http.post(URL, gson.toJson(project));
        if (response.getCode() == 200) {
            Project newProject = gson.fromJson(response.getResponse(), Project.class);
            return newProject;
        } else {
            ValidCheckResult result = gson.fromJson(response.getResponse(), ValidCheckResult.class);
            throw new IOException(result.getMessage());
        }
    }

    @Override
    public Project update(Project project) throws IOException {
        return null;
    }
}
