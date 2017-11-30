package kore.ntnu.no.safespace.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import kore.ntnu.no.safespace.data.Documentation;
import kore.ntnu.no.safespace.data.Image;
import kore.ntnu.no.safespace.data.IncidentReport;
import kore.ntnu.no.safespace.data.Report;

/**
 * Handles everything that has to do with writing, deleting and reading from disk.
 *
 * @author Kristoffer
 */
public class StorageUtils {


    public static boolean saveReportToFile(Report report, String storageDir) throws IOException {
        return saveReportToFile(report, new File(storageDir));
    }

    private static boolean saveIncident(IncidentReport report, File storageDir) throws IOException {
        File doc = getIncidentFile(storageDir, "Incident");
        FileOutputStream outputStream = new FileOutputStream(doc);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(report);
        return false;
    }

    private static File getIncidentFile(File storageDir, String prefix) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = prefix + "_" + timeStamp + ".txt";
        File incident = new File(storageDir, prefix + "/" + imageFileName);
        if (!incident.getParentFile().exists()) {
            incident.getParentFile().mkdirs();
        }
        incident.createNewFile();
        return incident;
    }

    private static boolean saveDocumentation(Documentation report, File storageDir) throws IOException {
        File doc = getDocumentFile(storageDir, "Documentation");
        FileOutputStream outputStream = new FileOutputStream(doc);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(report);
        return true;
    }

    public static File getDocumentFile(File storageDir, String prefix) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = prefix + "_" + timeStamp + ".txt";
        File document = new File(storageDir, prefix + "/" + imageFileName);
        if (!document.getParentFile().exists()) {
            document.getParentFile().mkdirs();
        }
        document.createNewFile();
        return document;
    }

    public static boolean saveReportToFile(Report report, File storageDir) throws IOException {
        if (report instanceof Documentation) {
            return saveDocumentation((Documentation) report, storageDir);
        } else if (report instanceof IncidentReport) {
            return saveIncident((IncidentReport) report, storageDir);
        } else {
            return false;
        }
    }

    public static Documentation readDocumentFromFile(File doc) throws IOException, ClassNotFoundException {
        FileInputStream inputStream = new FileInputStream(doc);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Documentation documentation = (Documentation) objectInputStream.readObject();
        inputStream.close();
        objectInputStream.close();
        return documentation;
    }

    public static List<File> getDocumentations(File storageDir) {
        List<File> list = new ArrayList<>();
        List<File> dirs = getDirectories(storageDir);
        for (File dir : dirs) {
            if (dir.getName().toLowerCase().contains("document")) {
                {
                    File root = new File(storageDir, dir.getAbsolutePath());
                    File[] documentations = root.listFiles();
                    list.addAll(Arrays.asList(documentations));
                }
            }
        }
        return list;
    }

    public static IncidentReport readIncidentFromFile(File incident) throws IOException, ClassNotFoundException {
        FileInputStream inputStream = new FileInputStream(incident);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        IncidentReport incidentReport = (IncidentReport) objectInputStream.readObject();
        return incidentReport;
    }

    public static List<File> getDirectories(String storageDir) {
        return getDirectories(new File(storageDir));
    }

    public static List<File> getDirectories(File storageDir) {
        List<File> list = new ArrayList<>();
        String[] content = storageDir.list();
        for (String s : content) {
            if (!s.contains(".")) {
                list.add(new File(s));
            }
        }
        return list;
    }

    public static void removeReport(Report report, File filepath) {
        for (Image i : report.getImages()){
            new File(i.getFilePath()).delete();
        }
        filepath.delete();
    }

    public static List<File> getIncidents(File externalFilesDir) {
        List<File> list = new ArrayList<>();
        List<File> dirs = getDirectories(externalFilesDir);
        for (File dir : dirs) {
            if (dir.getName().toLowerCase().contains("incident")) {
                {
                    File root = new File(externalFilesDir, dir.getAbsolutePath());
                    File[] documentations = root.listFiles();
                    list.addAll(Arrays.asList(documentations));
                }
            }
        }
        return list;
    }

    public static void deleteImage(Image image) {
        File f = image.getImageFile();
        boolean result = f.delete();
    }

    public static File saveToDisk(byte[] rawData, String name, String fileExtension) throws IOException {
        File path = ApplicationContext.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(path, name+"."+fileExtension);
        FileOutputStream out = new FileOutputStream(imageFile);
        out.write(rawData);
        out.close();
        return imageFile;
    }

    public static void deleteTempImages() {
        File path = ApplicationContext.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] files = path.listFiles();
        for(File f: files){
            if(f.getAbsolutePath().contains("jpg")){
                f.delete();
            }
        }
    }

    public static void removeUnusedImages(File externalFilesDir) {
        List<File> dirs = getDirectories(externalFilesDir);
        for(File folder : dirs){
            File currentDir = new File(externalFilesDir, folder.getAbsolutePath());
            String[] content = currentDir.list();
            for(String s : content){
                File imageFile = new File(currentDir, s);
                imageFile.delete();
            }
        }
    }
}
