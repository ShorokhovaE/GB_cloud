package ru.gb.file.gb_cloud;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import ru.gb.file.gb_cloud.dto.AuthRequest;
import ru.gb.file.gb_cloud.dto.DBConnect;

public class Server {

    private static final int MB_20 = 20 * 1_000_000;

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline inbound = socketChannel.pipeline();
                            inbound.addLast(
                                    new ObjectDecoder(MB_20, ClassResolvers.cacheDisabled(null)),
                                    new ObjectEncoder(),
                                    new BasicHandler()
                            );
                        }
                    });

            // подключаемся к БД
            try {
                DBConnect.connect();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            ChannelFuture channelFuture = serverBootstrap.bind(45001).sync(); // (7)
            channelFuture.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
