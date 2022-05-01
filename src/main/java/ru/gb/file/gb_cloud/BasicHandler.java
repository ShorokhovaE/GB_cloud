package ru.gb.file.gb_cloud;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.gb.file.gb_cloud.dto.*;


public class BasicHandler extends ChannelInboundHandlerAdapter {

    private final static String LOGIN_OK = "login_ok";
    private final static String LOGIN_NO = "login_no";
    private final static String REG_OK = "reg_ok";
    private final static String REG_NO = "reg_no";


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
