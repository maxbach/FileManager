package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileExtension {

    private File file;

    public FileExtension(String pathname) {
        file = new File(pathname);
    }

    public FileExtension(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return file.toPath().getFileName().toString();
    }

    public File getFile() {
        return file;
    }

    public Boolean isEqualOrParent(FileExtension other) {
        String wayToFile = getFile().getAbsolutePath();
        String wayToOther = other.file.getAbsolutePath();
        if (wayToFile.length() <= wayToOther.length()) {
            String line = wayToOther.substring(0, wayToFile.length());
            if (line.equals(wayToFile)) {
                return true;
            }
        }

        return false;
    }

    public void delete() throws IOException {
        if (!file.delete()) {
            throw new IOException();
        }
    }

    public void copy(FileExtension destination) throws IOException {
        try (FileChannel sourceChannel = new FileInputStream(this.file).getChannel();
             FileChannel destChannel = new FileOutputStream(destination.file).getChannel()) {
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }
    }

    public void cut(FileExtension destination) throws IOException {
        File dest = destination.file;
        if (!file.renameTo(dest)) {
            throw new IOException();
        }
    }

    public void rename(FileExtension to) throws IOException {
        cut(to);
        file = to.file;
    }

    public FileExtension newFolder() throws IOException {
        String fileway = file.getAbsolutePath();
        fileway += "/" + "NewFolder";
        FileExtension newFolder = FileUtils.getFileWithSuffix(fileway);

        boolean success = newFolder.mkdir();
        if (!success) {
            throw new IOException();
        }
        return newFolder;
    }


    public Boolean isFile() {
        return file.isFile();
    }

    public Boolean mkdir() {
        return file.mkdir();
    }

    public String getName() {
        return file.getName();
    }

    public String getAbsolutePath() {
        return file.getAbsolutePath();
    }

    public Boolean isExist() {
        return file.exists();
    }

}
