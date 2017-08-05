package utils;


import java.io.*;

public class FileUtils {


    public static FileExtension getResultOfMovement(FileExtension source, FileExtension folderDestination) {
        String fileName = source.getName();
        String wayToNewFile = folderDestination.getAbsolutePath() + "/" + fileName;
        return new FileExtension(new File(wayToNewFile));
    }

    public static FileExtension getFileWithSuffix(String fileway) {
        File newFile = new File(fileway);
        int i = 0;
        while (newFile.exists()) {
            i++;
            newFile = new File(fileway + " (" + i + ")");
        }
        return new FileExtension(newFile);
    }

    public static FileExtension resultOfRename(FileExtension source, String newName) {
        String way = source.getAbsolutePath();
        String newWay = way.substring(0, way.length() - source.getName().length()) + newName;
        return new FileExtension(new File(newWay));
    }


}
