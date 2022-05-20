package ru.gb.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import ru.gb.dto.BasicResponse;
import ru.gb.dto.TextOfResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    PrimaryController pr;
    private final static String LOGIN_OK = "login_ok";
    private final static String LOGIN_NO = "login_no";
    private final static String REG_OK = "reg_ok";
    private final static String REG_NO = "reg_no";
    private final static String LOG_OFF = "log_off";
    private final static String LOAD_OK = "load_ok";
    private final static String DOWNLOAD_OK = "download_ok";
    private final static String DELETE_OK = "delete_ok";


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

        if(TextOfResponse.LOGIN_OK.equals(responseText)){
            pr.loginOk();

        } if(TextOfResponse.LOGIN_NO.equals(responseText)){
            pr.viewMsg(pr.LoginNo);

        } if(TextOfResponse.REG_NO.equals(responseText)){
            pr.viewMsg(pr.RegNo);

        } if(TextOfResponse.REG_OK.equals(responseText)){
            pr.viewMsg(pr.RegOk);

        } if(TextOfResponse.LOG_OFF.equals(responseText)) {
            System.out.println("Клиент вышел");
            Connect.getEventLoopGroup().shutdownGracefully();

        }  if(TextOfResponse.LOAD_OK.equals((responseText))){
            ServerFilePanelController serverPanel =
                    (ServerFilePanelController) ControllerRegistry.getControllerObject(ServerFilePanelController.class);
            serverPanel.updatePath(Path.of(serverPanel.pathField.getText()));

        } if(TextOfResponse.DOWNLOAD_OK.equals(responseText)) {
            LocalFilePanelController localPanel =
                    (LocalFilePanelController) ControllerRegistry.getControllerObject(LocalFilePanelController.class);
            String path = String.format(localPanel.pathField.getText() + "/%s", response.getFileName());
            FileOutputStream fos = new FileOutputStream(path);
            fos.write((response).getData());

            localPanel.updatePath(Path.of(localPanel.pathField.getText()));
        } if(TextOfResponse.DELETE_OK.equals(responseText)){
            getServerPanel().updatePath(Path.of(getServerPanel().pathField.getText()));
            alertForDeleteFile();
        } if(TextOfResponse.DELETE_NO.equals(responseText)){
            System.out.println("С удалением накладочка вышла");
        }
    }

    private void alertForDeleteFile(){

        Platform.runLater(() ->{
            getServerPanel().alertInfo("Файл удален!");
        });
    }

    private ServerFilePanelController getServerPanel(){
        ServerFilePanelController serverPanel =
                (ServerFilePanelController) ControllerRegistry.getControllerObject(ServerFilePanelController.class);
        return serverPanel;
    }
}
