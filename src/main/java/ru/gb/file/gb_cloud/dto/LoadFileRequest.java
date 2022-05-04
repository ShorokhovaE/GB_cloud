package ru.gb.file.gb_cloud.dto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

public class LoadFileRequest implements BasicRequest {

    private File file;

    private String path;

    private byte[] data;

    public String getPath() {
        return file.getPath();
    }

    public LoadFileRequest(File file) {
        this.file = file;
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
