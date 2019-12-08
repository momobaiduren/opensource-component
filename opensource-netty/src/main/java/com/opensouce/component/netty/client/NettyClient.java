package com.opensouce.component.netty.client;

import com.opensouce.component.netty.message.ClientRequestServer;
import com.opensouce.component.netty.protocol.CustomDecoder;
import com.opensouce.component.netty.protocol.CustomEncoder;
import com.opensouce.component.netty.message.ServerResponseClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.Scanner;
import java.util.function.BiConsumer;

/**
 * @author ZhangLong on 2019/12/7  1:00 下午
 * @version V1.0
 */
public class NettyClient {
    /**
     * description 引导类
     */
    private final Bootstrap bootstrap;
    /**
     * description 非阻塞事件处理的线程管理组件
     */
    private final NioEventLoopGroup workerGroup;
    private BiConsumer<ChannelHandlerContext, ServerResponseClient> biConsumer;


    public NettyClient(BiConsumer<ChannelHandlerContext, ServerResponseClient> biConsumer) {
        bootstrap = new Bootstrap();
        workerGroup = new NioEventLoopGroup();
        bootstrap.group(workerGroup)
                //设置channel的类型为非阻塞
                .channel(NioSocketChannel.class);
        this.biConsumer = biConsumer;
    }

    public void connect(InetSocketAddress socketAddress) {
        try {
            bootstrap.remoteAddress(socketAddress)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    //绑定 channelintializer 当连接建立时新建一个channel用来管理
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            //提供了出入站执行器链的容器
                            socketChannel.pipeline()
                                    .addLast(new CustomEncoder(ClientRequestServer.class))
                                    .addLast(new CustomDecoder(ServerResponseClient.class))
                                    //添加出入站的处理器
                                    .addLast(new SimpleClientChannelHandler(biConsumer));
                        }
                    });

            // Start the client.
            ChannelFuture channelFuture = bootstrap.connect().sync();

            //该监听器被通知连接已经建立的时候，要检查对应的状态
            //在这里可以尝试重新连接或者建立一个到另一个远程节点的连接
            channelFuture.addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    //TODO 心跳监听连接情况
                    ClientRequestServer<String> request = new ClientRequestServer<>();
                    request.setData("zhanglong");
                    future.channel().writeAndFlush(request);
                    System.out.println("connection server successful");
                } else {
                    System.out.println("connection server fail");
                    future.channel().closeFuture();
                    workerGroup.shutdownGracefully();
                    System.exit(0);
                }
            });
            while(channelFuture.isDone()){
                //TODO 事件机制触发消息处理
                Scanner scanner = new Scanner(System.in);
                ClientRequestServer<String> request = new ClientRequestServer<>();
                request.setData(scanner.next());
                channelFuture.channel().writeAndFlush(request);
            }
            // Wait until the connection is closed. sync()方法是用于阻塞直到结果返回才会继续向下执行
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                workerGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
            ;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        new NettyClient((channelHandlerContext, response) -> {
            System.out.println("server::" + response.getData());
            ClientRequestServer<String> request = new ClientRequestServer<>();
            request.setData("success");
            request.sendMsg(channelHandlerContext);
        }).connect(new InetSocketAddress("localhost", 8112));

    }
}
