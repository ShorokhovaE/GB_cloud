package ru.gb.file.gb_cloud.dto;

public class GetFileListRequest implements BasicRequest {

    @Override
    public String getType() {
        return "getFileList";
    }
}
