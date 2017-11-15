package kore.ntnu.no.safespace;

import org.junit.Test;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import kore.ntnu.no.safespace.data.Image;
import kore.ntnu.no.safespace.service.ImageService;

import static org.junit.Assert.*;

/**
 * Created by robert on 11/2/17.
 */

public class ImageServiceTest {

    ImageService imageService;
    Image someImage = null;

    public ImageServiceTest() {
        imageService = new ImageService();
    }

    @Test
    public void getImages() throws Exception {
        List<Image> images = imageService.getAll();
        assertTrue(images.size() > 0);
    }

    @Test
    public void getImage() throws Exception {
        Image image = imageService.getOne(1L);
        assertNotNull(image);
        assertNotNull(image.getName());
        assertNotNull(image.getFileExtension());
    }

    @Test
    public void getImageWithData() throws Exception {
        Image image = imageService.getOneWithData(1L);
        assertNotNull(image.getData());
    }

    @Test
    public void addImageWithB64String() throws Exception {
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL resource = classLoader.getResource("b64");
        Path path = Paths.get("/", resource.getPath().substring(1));
        String b64String = Files.readAllLines(path).get(0);
        //byte[] data = Files.readAllBytes(path);
        Image image = new Image(null, "ASDF", "1234", "jpg", "an asdf image", b64String);
        Image postedImage = imageService.add(image);

        assertEquals(image.getData(), postedImage.getData());
    }

    @Test
    public void addImage() throws Exception {
        Image image = new Image(null, "ASDF", "1234", "jpg", "an asdf image", null);
        Image postedImage = imageService.add(image);
        assertEquals(image.getName(), postedImage.getName());
        assertEquals(image.getFileExtension(), postedImage.getFileExtension());
    }

    @Test
    public void addImageWithData() throws  Exception {
        Image image = new Image(null, "ASDF", "1234", "jpg", "an asdf image", null);
        Image postedImage = imageService.add(image);
        assertEquals(image.getName(), postedImage.getName());
        assertEquals(image.getFileExtension(), postedImage.getFileExtension());
    }

}
