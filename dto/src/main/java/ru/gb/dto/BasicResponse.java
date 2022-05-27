package ru.gb.dto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

public class BasicResponse implements Serializable {

    private String response;
    private File file;

    private String fileName;

    public BasicResponse(String response) {
        this.response = response;
    }

    public BasicResponse(File downloadFileResponse, String fileName, String response) {
        this.file = downloadFileResponse;
        this.fileName = fileName;
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }

    public byte[] getData() throws IOException {
        byte[] fileInArray = new byte[(int)file.length()];
        FileInputStream f = new FileInputStream(file.getPath());
        f.read(fileInArray);
        return fileInArray;
    }

}
