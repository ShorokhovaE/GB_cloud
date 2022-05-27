package ru.gb.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import ru.gb.dto.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    private String login;
    ChannelHandlerContext channelHandlerContext;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {

        this.channelHandlerContext = channelHandlerContext;
        BasicRequest request = (BasicRequest) msg;
        System.out.println(request.getType());

        if (request instanceof AuthRequest) {
            if(((AuthRequest) request).checkLoginAndPassword()){
               this.login = ((AuthRequest) request).getLogin();
                basicResponse(TextOfResponse.LOGIN_OK);
            } else {
                basicResponse(TextOfResponse.LOGIN_NO);
            }

        } else if(request instanceof RegRequest){
            if(((RegRequest) request).registration()){
                ((RegRequest) request).hashPassword();
                basicResponse(TextOfResponse.REG_OK);

            } else {
                basicResponse(TextOfResponse.REG_NO);
            }

        } else if (request instanceof DisconnectRequest) {
            basicResponse(TextOfResponse.LOG_OFF);

        } else if(request instanceof LoadFileRequest){

            String pathOfFile = String.format(((LoadFileRequest) request).getPathForLoad() + "/" + ((LoadFileRequest) request).getFilename());
            FileOutputStream fos = new FileOutputStream(pathOfFile);
            fos.write(((LoadFileRequest) request).getData());
            basicResponse(TextOfResponse.LOAD_OK);

        } else if(request instanceof DownloadFileRequest){

            BasicResponse basicResponse = new BasicResponse
                    (new File(String.valueOf(((DownloadFileRequest) request).getPathOfFile())),
                            ((DownloadFileRequest) request).getFileName(),
                            "download_ok");
            channelHandlerContext.writeAndFlush(basicResponse);

        } else if(request instanceof DeleteFileRequest){

            basicResponse(checkDelete((DeleteFileRequest) request));
        }
    }

    private void basicResponse(String response){
        BasicResponse basicResponse = new BasicResponse(response);
        channelHandlerContext.writeAndFlush(basicResponse);
    }

    private String checkDelete(DeleteFileRequest request){

        if(recursiveDelete(new File(request.getPathOfFile()))){
            return TextOfResponse.DELETE_OK;
        } else {
            return TextOfResponse.DELETE_NO;
        }
    }

        private boolean recursiveDelete(File file) {
        if (!file.exists())
            return false;
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                recursiveDelete(f);
            }
        }
        file.delete();
        return true;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
