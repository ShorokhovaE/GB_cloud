package ru.gb.file.gb_cloud.dto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

public class DownloadFileRequest implements BasicRequest{

    private Path pathOfFile;

    public Path getPathOfFile() {
        return pathOfFile;
    }

    public DownloadFileRequest(Path pathOfFile) {
        this.pathOfFile = pathOfFile;
    }

    @Override
    public String getType() {
        return "download";
    }
}
