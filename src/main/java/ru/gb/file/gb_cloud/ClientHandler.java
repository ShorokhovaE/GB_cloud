package ru.gb.file.gb_cloud;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.gb.file.gb_cloud.dto.BasicResponse;
import ru.gb.file.gb_cloud.dto.GetFileListRequest;
import javax.inject.Inject;

import java.io.File;

public class ClientHandler extends ChannelInboundHandlerAdapter {
//
//    @Inject
    private PrimaryController primaryController;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        BasicResponse response = (BasicResponse) msg;
        System.out.println(response.getResponse());
        String responseText = response.getResponse();

        if("login ok".equals(responseText)){
           new PrimaryController().switchToSecondary();
        } if("login no".equals(responseText)){

        }

//        if ("file list....".equals(responseText)) {
//            ctx.close();
//            return;
//        }
//        ctx.writeAndFlush(new GetFileListRequest());
    }
}
