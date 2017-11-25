package kore.ntnu.no.safespace.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kore.ntnu.no.safespace.data.Image;

/**
 * Class description..
 *
 * @author Kristoffer
 */
public class ImageUtils {

    public static Bitmap getBitmap(Image image) throws IOException {
        return getBitmap(image, 1);
    }
    public static Bitmap getBitmap(File image) throws IOException {
        return getBitmap(image, 1);
    }

    public static Bitmap getBitmap(Image image, int scaleFactor) throws IOException {
        return getBitmap(image.getImageFile(), scaleFactor);
    }

    private static Bitmap getBitmap(File imageFile, int scaleFactor) throws IOException {
        if (scaleFactor < 1) {
            scaleFactor = 1 / scaleFactor;
        }
        ExifInterface exif = null;
        int rotation = 0;
        try{
            exif = new ExifInterface(imageFile.getAbsolutePath());
            rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e){
            Log.e(ImageUtils.class.getSimpleName(), "Invalid image type for Exif");
        }
        rotation = exifToDegrees(rotation);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = scaleFactor;
        Bitmap imageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), opts);
        if(imageBitmap != null) {
            Matrix matrix = new Matrix();
            matrix.setRotate(rotation, (float) imageBitmap.getWidth() / 2, (float) imageBitmap.getHeight() / 2);
//        matrix.postScale(-1,1,(float) imageBitmap.getWidth() / 2, (float) imageBitmap.getHeight() / 2);
            imageBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);
        }
        return imageBitmap;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    public static boolean saveImage(Image image){
        return saveImage(image.getImageFile());
    }

    private static boolean saveImage(File imageFile) {
        Thread saver = new Thread(()->{
        FileOutputStream out = null;
        try {
            Bitmap map = ImageUtils.getBitmap(imageFile);
            out = new FileOutputStream(imageFile);
            map.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
            map.recycle();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }});
        saver.start();
        return true;
    }


    public static File createImageFile(File storageDir, String prefix) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = prefix + "_" + timeStamp + ".jpg";
        File image = new File(storageDir, prefix + "/" + imageFileName);
        if (!image.getParentFile().exists()) {
            image.getParentFile().mkdirs();
        }
        image.createNewFile();
        return image;
    }

    public static File createImageFile(String storageDir, String prefix) throws IOException {
        return createImageFile(new File(storageDir), prefix);
    }

    public static List<Image> getStoredImages(String storageDir){
        List<Image> list = new ArrayList<>();
        List<File> directories = StorageUtils.getDirectories(storageDir);
        for(File dir: directories){
            File imgPath = new File(storageDir, dir.getAbsolutePath());
            File[] images = imgPath.listFiles();
            for(File img: images){
                list.add(new Image(img));
            }
        }
        return list;
    }
    public static List<Image> getStoredImages(File storageDir){
        return getStoredImages(storageDir.getAbsolutePath());
    }

    public static byte[] getRawImageData(Image image) throws IOException {
        FileInputStream in = new FileInputStream(image.getImageFile());
        byte[] rawData = new byte[in.available()];
        in.read(rawData);
        return rawData;
    }
    public static String getB64ImageData(Image image) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        Bitmap bitmap = ImageUtils.getBitmap(image);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);
        bitmap.recycle();
        return Base64.encodeToString(byteStream.toByteArray(), Base64.NO_WRAP);
    }
}
