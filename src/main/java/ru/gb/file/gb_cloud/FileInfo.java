package ru.gb.file.gb_cloud;

import java.nio.file.Path;

public class FileInfo {

    public String getFileName() {
        return fileName;
    }

    private String fileName;

    public FileInfo(Path path) {
        this.fileName = path.getFileName().toString();
    }
}
