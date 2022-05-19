package ru.gb.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import ru.gb.dto.BasicResponse;

import java.io.FileOutputStream;
import java.nio.file.Path;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    PrimaryController pr;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        BasicResponse response = (BasicResponse) msg;
        System.out.println(response.getResponse());
        String responseText = response.getResponse();
        pr = (PrimaryController) ControllerRegistry.getControllerObject(PrimaryController.class);

        if("login_ok".equals(responseText)){
            pr.loginOk();

        } if("login_no".equals(responseText)){
            pr.viewMsg(pr.LoginNo);

        } if("reg_no".equals(responseText)){
            pr.viewMsg(pr.RegNo);

        } if("reg_ok".equals(responseText)){
            pr.viewMsg(pr.RegOk);
        }

        if("log_off".equals(responseText)) {
            System.out.println("Клиент вышел");
            Connect.getEventLoopGroup().shutdownGracefully();

        } if("load_ok".equals((responseText))){
            ServerFilePanelController serverPanel =
                    (ServerFilePanelController) ControllerRegistry.getControllerObject(ServerFilePanelController.class);
            serverPanel.updatePath(Path.of(serverPanel.pathField.getText()));

        } if("download_ok".equals(responseText)) {
            System.out.println("Скачивание файла");
            LocalFilePanelController localPanel =
                    (LocalFilePanelController) ControllerRegistry.getControllerObject(LocalFilePanelController.class);
            String path = String.format(localPanel.pathField.getText() + "/%s", response.getFileName());
            FileOutputStream fos = new FileOutputStream(path);
            fos.write((response).getData());

            localPanel.updatePath(Path.of(localPanel.pathField.getText()));
        }

//        if ("file list....".equals(responseText)) {
//            ctx.close();
//            return;
//        }
//        ctx.writeAndFlush(new GetFileListRequest());
    }


}
