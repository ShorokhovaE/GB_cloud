package ru.gb.file.gb_cloud.dto;

public class LoadFileRequest implements BasicRequest {
    @Override
    public String getType() {
        return "load";
    }
}
