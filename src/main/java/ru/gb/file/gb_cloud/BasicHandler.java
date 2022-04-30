package ru.gb.file.gb_cloud;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.gb.file.gb_cloud.dto.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class BasicHandler extends ChannelInboundHandlerAdapter {

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
                BasicResponse loginOkResponse = new BasicResponse("login ok");
                channelHandlerContext.writeAndFlush(loginOkResponse);
            } else {
                BasicResponse loginNoResponse = new BasicResponse("login no");
                channelHandlerContext.writeAndFlush(loginNoResponse);
            }
        } else if(request instanceof RegRequest){
            if(((RegRequest) request).registration()){
                BasicResponse loginNoResponse = new BasicResponse("reg ok");
                channelHandlerContext.writeAndFlush(loginNoResponse);
            } else {
                BasicResponse loginNoResponse = new BasicResponse("reg no");
                channelHandlerContext.writeAndFlush(loginNoResponse);
            }
        }
        else if (request instanceof GetFileListRequest) {
            BasicResponse basicResponse = new BasicResponse("file list....");
            channelHandlerContext.writeAndFlush(basicResponse);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
