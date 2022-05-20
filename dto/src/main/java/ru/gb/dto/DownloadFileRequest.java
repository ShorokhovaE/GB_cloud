package ru.gb.dto;

public class DownloadFileRequest implements BasicRequest{

    private String pathOfFile;
    private String fileName;


    public DownloadFileRequest(String pathOfFile, String fileName) {
        this.pathOfFile = pathOfFile;
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
    public String getPathOfFile() {
        return pathOfFile;
    }

    @Override
    public String getType() {
        return "download";
    }
}
