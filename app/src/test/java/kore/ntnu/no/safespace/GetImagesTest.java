package kore.ntnu.no.safespace;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import kore.ntnu.no.safespace.data.Image;
import kore.ntnu.no.safespace.service.DocumentationService;
import kore.ntnu.no.safespace.service.ReportService;

import static org.junit.Assert.*;

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
        List<Image> images = service.getImagesForDocumentation(id);
        for (Image image : images) {
            assertEquals(id, image.getDocumentation().getId());
        }
    }


}
