package kore.ntnu.no.safespace;

import org.junit.Test;

import java.util.List;

import kore.ntnu.no.safespace.Data.Image;
import kore.ntnu.no.safespace.service.ImageService;

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
        for(Image image : images) {
            System.out.println(image.getName());
        }
    }

    @Test
    public void getImage() throws Exception {
        Image image = imageService.getOne(1L);
        System.out.println(image.getId());
        System.out.println(image.getName());
    }

    @Test
    public void addImage() throws Exception {
        Image image = new Image(null, "ASDF", "1234", "jpg", "an asdf image");
        Image postedImage = imageService.add(image);
        System.out.println(postedImage.getId());
        System.out.println(postedImage.getName());
    }

    @Test
    public void addImageWithData() throws  Exception {
        Image image = new Image(null, "ASDF", "1234", "jpg", "an asdf image");
        Image postedImage = imageService.add(image);
        System.out.println(postedImage.getId());
        System.out.println(postedImage.getName());
    }

}
