package ru.gb.client;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileInfo {


    private String fileName;

    private Path path;

    private long size;
    private String type;
    private final static String ISDIRECTORY = "Dir";
    private final static String ISFILE = "File";


    public FileInfo(Path path) {
        this.fileName = path.getFileName().toString();
        this.path = path;
        this.size = path.toFile().length();
        this.type = Files.isDirectory(path) ? ISDIRECTORY : ISFILE;

        if (this.type.equals("Dir")) {
            this.size = -1L;
        }
    }

    public Path getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public String getFileName() {
        return fileName;
    }
}
