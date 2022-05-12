package ru.gb.file.gb_cloud;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;


public class Connect {

    public Channel getChannel() {
        return channel;
    }

    public static final int MB_20 = 20 * 1_000_000;
    private Channel channel;

    public Connect(){
        new Thread(() -> {
            EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.remoteAddress("localhost", 45001);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) {
                    socketChannel.pipeline().addLast(
                            new ObjectDecoder(MB_20, ClassResolvers.cacheDisabled(null)),
                            new ObjectEncoder(),
                            new ClientHandler()
                    );
                }
            });
            ChannelFuture channelFuture = null;
            try {
                channelFuture = bootstrap.connect().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            channel = channelFuture.channel();
            System.out.println("Клиент подключен");
            try {
                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

    }
}
