package kore.ntnu.no.safespace.service;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import kore.ntnu.no.safespace.data.Documentation;
import kore.ntnu.no.safespace.data.Image;
import kore.ntnu.no.safespace.data.ValidCheckResult;
import kore.ntnu.no.safespace.dto.DocumentDTO;
import kore.ntnu.no.safespace.service.http.HttpResponse;
import kore.ntnu.no.safespace.service.http.HttpService;
import kore.ntnu.no.safespace.utils.IdUtils;

/**
 * Class that handles communication with backend when performing Create, Read or Update operations
 * on documentations.
 *
 * @author Robert
 */
public class DocumentationService implements RestClient<Documentation, Long> {

    private static final String URL = IdUtils.URL  + "/documentations";
    private static final Type LIST_TYPE = new TypeToken<List<DocumentDTO>>(){}.getType();
    private static final Type IMAGE_LIST_TYPE = new TypeToken<List<Image>>(){}.getType();

    private HttpService http;
    private Gson gson;

    public DocumentationService() {
        this.http = new HttpService();
        this.gson = new Gson();
    }

    @Override
    public ServiceResult<List<Documentation>> getAll() throws IOException {
        HttpResponse response = http.get(URL);
        List<DocumentDTO> documentDTOs = gson.fromJson(response.getResponse(), LIST_TYPE);
        List<Documentation> documentations = new ArrayList<>();
        for (DocumentDTO reportDTO : documentDTOs) {
            documentations.add(getReport(reportDTO));
        }
        return new ServiceResult<>(documentations, true, "OK");
    }

    @Override
    public ServiceResult<Documentation> getOne(Long id) throws IOException {
        HttpResponse response = http.get(URL + "/" + id);
        DocumentDTO documentDTO = gson.fromJson(response.getResponse(), DocumentDTO.class);
        return new ServiceResult<>(getReport(documentDTO), true, "OK");
    }

    @Override
    public ServiceResult<Documentation> add(Documentation documentation) throws IOException {
        DocumentDTO newDocumentDTO = getDto(documentation);
        HttpResponse response = http.post(URL, gson.toJson(newDocumentDTO));
        if (response.getCode() == 200) {
            DocumentDTO documentDTO = gson.fromJson(response.getResponse(), DocumentDTO.class);
            return new ServiceResult<>(getReport(documentDTO), true, "OK");
        } else {
            ValidCheckResult result = gson.fromJson(response.getResponse(), ValidCheckResult.class);
            throw new IOException(result.getMessage());
        }
    }

    @Override
    public ServiceResult<Documentation> update(Documentation documentation) throws IOException {
        return null;
    }

    /**
     * Retrieve all images that belong to given documentation
     * @param documentationId id of documentation to retrieve images for
     * @return list of images for given documentation
     */
    public ServiceResult<List<Image>> getImagesForDocumentation(Long documentationId){
        try {
            final String url = URL + "/" + documentationId + "/images";
            HttpResponse response = http.get(url);
            return new ServiceResult<>(gson.fromJson(response.getResponse(), IMAGE_LIST_TYPE), true, "OK");
        } catch (IOException ex) {
            Log.e(ImageService.class.getSimpleName(), "Failed to post image");
            return null;
        }
    }

    private Documentation getReport(DocumentDTO dto) {
        return new Documentation(dto.getId(), dto.getTitle(), dto.getDescription(), dto.getUser());
    }

    private DocumentDTO getDto(Documentation documentation) {
        return new DocumentDTO(documentation.getId(), documentation.getTitle(), documentation.getDescription(), documentation.getProject(), documentation.getSubmitter());
    }
}
