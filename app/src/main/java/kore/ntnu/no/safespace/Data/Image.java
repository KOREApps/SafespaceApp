package kore.ntnu.no.safespace.Data;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Kristoffer on 2017-11-01.
 */

public class Image  implements Serializable {
    private final Long id;
    private final String name;
    private final String filePath;
    private final String fileExtension;
    private final String description;
    private final String data;
    private Bitmap bitmapImage;

    public Image(Long id, String name, String filePath, String fileExtension, String description, String data) {
        this.id = id;
        this.name = name;
        this.filePath = filePath;
        this.fileExtension = fileExtension;
        this.description = description;
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image)) return false;

        Image image = (Image) o;

        if (getId() != null ? !getId().equals(image.getId()) : image.getId() != null) return false;
        if (getName() != null ? !getName().equals(image.getName()) : image.getName() != null)
            return false;
        if (getFilePath() != null ? !getFilePath().equals(image.getFilePath()) : image.getFilePath() != null)
            return false;
        if (getFileExtension() != null ? !getFileExtension().equals(image.getFileExtension()) : image.getFileExtension() != null)
            return false;
        return getDescription() != null ? getDescription().equals(image.getDescription()) : image.getDescription() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getFilePath() != null ? getFilePath().hashCode() : 0);
        result = 31 * result + (getFileExtension() != null ? getFileExtension().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        return result;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getDescription() {
        return description;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getData(){
        return data;
    }

    public Bitmap getBitmapImage() {
        return bitmapImage;
    }

    public void setBitmapImage(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }
}
