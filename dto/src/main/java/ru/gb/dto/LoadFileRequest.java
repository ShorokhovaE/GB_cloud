package ru.gb.dto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class LoadFileRequest implements BasicRequest {

    private File file;

    private String fileName;

    private String pathForLoad;


    public String getFilename() {
        return fileName;
    }

    public String getPathForLoad() {
        return pathForLoad;
    }

    public LoadFileRequest(File file, String fileName, String pathForLoad) {
        this.file = file;
        this.fileName = fileName;
        this.pathForLoad = pathForLoad;
    }

    @Override
    public String getType() {
        return "load";
    }

    public byte[] getData() throws IOException {
        byte[] fileInArray = new byte[(int)file.length()];
        FileInputStream f = new FileInputStream(file.getPath());
        f.read(fileInArray);
        return fileInArray;
    }
}
