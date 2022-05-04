package ru.gb.file.gb_cloud;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.gb.file.gb_cloud.dto.*;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;


public class BasicHandler extends ChannelInboundHandlerAdapter {

    private final static String LOGIN_OK = "login_ok";
    private final static String LOGIN_NO = "login_no";
    private final static String REG_OK = "reg_ok";
    private final static String REG_NO = "reg_no";

    private String login;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object msg) throws Exception {
        BasicRequest request = (BasicRequest) msg;
        System.out.println(request.getType());

        if (request instanceof AuthRequest) {
            if(((AuthRequest) request).checkLoginAndPassword()){
                login = ((AuthRequest) request).getLogin();
                BasicResponse loginOkResponse = new BasicResponse(LOGIN_OK);
                channelHandlerContext.writeAndFlush(loginOkResponse);
            } else {
                BasicResponse loginNoResponse = new BasicResponse(LOGIN_NO);
                channelHandlerContext.writeAndFlush(loginNoResponse);
            }
        } else if(request instanceof RegRequest){
            if(((RegRequest) request).registration()){
                BasicResponse regOkResponse = new BasicResponse(REG_OK);
                channelHandlerContext.writeAndFlush(regOkResponse);
            } else {
                BasicResponse regNoResponse = new BasicResponse(REG_NO);
                channelHandlerContext.writeAndFlush(regNoResponse);
            }
        } else if (request instanceof GetFileListRequest) {
            BasicResponse basicResponse = new BasicResponse("file list....");
            channelHandlerContext.writeAndFlush(basicResponse);
        } else if (request instanceof DisconnectRequest) {
            BasicResponse basicResponse = new BasicResponse("log_off");
            channelHandlerContext.writeAndFlush(basicResponse);
        } else if(request instanceof LoadFileRequest){
            String pathOfFile = String.format("src/main/clients.directory/%s/%s", login, ((LoadFileRequest) request).getFilename());
            FileOutputStream fos = new FileOutputStream(pathOfFile);
            fos.write(((LoadFileRequest) request).getData());
        } else if(request instanceof DownloadFileRequest){
            //здесь будет проверка на доступ скачивания
            BasicResponse basicResponse = new BasicResponse(new File(String.valueOf(request)), "download_ok");
            channelHandlerContext.writeAndFlush(basicResponse);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
