package ru.gb.file.gb_cloud.dto;

public class DisconnectRequest implements BasicRequest {

    @Override
    public String getType() {
        return "log_off";
    }
}
