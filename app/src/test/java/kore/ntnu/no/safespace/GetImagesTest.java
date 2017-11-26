package kore.ntnu.no.safespace;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import kore.ntnu.no.safespace.data.Image;
import kore.ntnu.no.safespace.service.DocumentationService;
import kore.ntnu.no.safespace.service.ReportService;
import kore.ntnu.no.safespace.service.ServiceResult;

import static org.junit.Assert.assertEquals;

/**
 * Created by robert on 11/18/17.
 */

public class GetImagesTest {

    @Test
    @Ignore
    public void getImagesByReportId() throws Exception {
        Long id = 29L;
        ReportService service = new ReportService();
        List<Image> images = service.getImagesForReport(id);
        for (Image image : images) {
            assertEquals(id, image.getReport().getId());
        }
    }

    @Test
    @Ignore
    public void getImagesByDocumentationId() throws Exception {
        Long id = 53L;
        DocumentationService service = new DocumentationService();
        ServiceResult<List<Image>> serviceResult = service.getImagesForDocumentation(id);
        List<Image> images = serviceResult.getObject();
        for (Image image : images) {
            assertEquals(id, image.getDocumentation().getId());
        }
    }


}
