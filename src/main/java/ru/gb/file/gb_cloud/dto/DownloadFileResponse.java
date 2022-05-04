package ru.gb.file.gb_cloud.dto;

import java.io.*;

public class DownloadFileResponse implements Serializable {

    private File file;
    private String fileName;


    public DownloadFileResponse(File downloadFileResponse) {
        this.file = downloadFileResponse;
    }

    public String getFilename() {
        return fileName;
    }

    public File getDownloadFileResponse() {
        return file;
    }

    public byte[] getData() throws IOException {
        byte[] fileInArray = new byte[(int)file.length()];
        FileInputStream f = new FileInputStream(file.getPath());
        f.read(fileInArray);
        return fileInArray;
    }

    public void downloadFileFromServer(String path) throws IOException {
        byte[] fileInArray = new byte[(int)file.length()];
        FileInputStream f = new FileInputStream(file.getPath());
        f.read(fileInArray);
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(fileInArray);
    }


}
