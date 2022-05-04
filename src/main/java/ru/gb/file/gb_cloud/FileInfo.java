package ru.gb.file.gb_cloud;

import java.nio.file.Path;

public class FileInfo {

    private String fileName;

    private Path path;

    public String getFileName() {
        return fileName;
    }

    public FileInfo(Path path) {
        this.fileName = path.getFileName().toString();
        this.path = path;
    }

    public Path getPath() {
        return path;
    }
}
