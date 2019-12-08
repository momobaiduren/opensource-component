package com.opensouce.component.netty.server;

import com.opensouce.component.netty.message.ServerResponseClient;
import com.opensouce.component.netty.message.ClientRequestServer;
import com.opensouce.component.netty.protocol.CustomDecoder;
import com.opensouce.component.netty.protocol.CustomEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import javax.annotation.PreDestroy;
import java.util.function.BiConsumer;

/**
 * @author ZhangLong on 2019/12/7  12:41 下午
 * @version V1.0
 */
public class NettyServer<ReqData> {

    private ServerBootstrap serverBootstrap;
    /**
     * BOSS
     */
    private EventLoopGroup boss;
    /**
     * Worker
     */
    private EventLoopGroup work;

    private BiConsumer<ChannelHandlerContext, ClientRequestServer<ReqData>> biConsumer;

    public NettyServer(BiConsumer<ChannelHandlerContext, ClientRequestServer<ReqData>> biConsumer) {
        serverBootstrap = new ServerBootstrap();
        boss = new NioEventLoopGroup();
        work = new NioEventLoopGroup();
        this.biConsumer = biConsumer;
    }


    /**
     * 关闭服务器方法
     */
    @PreDestroy
    public void close() {
        System.out.println("关闭服务器....");
        //优雅退出
        boss.shutdownGracefully();
        work.shutdownGracefully();
    }

    /**
     * 开启及服务线程
     */
    public void start(int port) {
        if (port <= 0) {
            port = 8111;
        }
        serverBootstrap.group(boss, work)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128);
        try {
            //设置事件处理
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                            .addLast(new CustomEncoder(ServerResponseClient.class))
                            .addLast(new CustomDecoder(ClientRequestServer.class))
                            .addLast(new SimpleServerChannelHandler<>(biConsumer));
                }
            });
            System.out.println(String.format("admin 服务注册端口[%s]启动监听", port));
            ChannelFuture f = serverBootstrap.bind(port).sync();
            f.addListener((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    f.channel().closeFuture().sync();
                    throw new RuntimeException("admin server is running fail");
                } else {
                    System.out.println("admin is running successful");
                }
            });
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            System.out.println("[出现异常] 释放资源");
            close();
        }
    }

    public static void main(String[] args) {
        new NettyServer<String>((channelHandlerContext, request) -> {
            System.out.println("client---->" + request.getData());
            if ("zhanglong".equals(request.getData())){
                ServerResponseClient<String> serverResponseClient = new ServerResponseClient<>();
                serverResponseClient.setData("lisi");
                serverResponseClient.sendMsg(channelHandlerContext);
            }
        }).start(8112 );
    }
   
}
