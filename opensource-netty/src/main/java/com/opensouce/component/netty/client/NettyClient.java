package com.opensouce.component.netty.client;

import com.opensouce.component.netty.message.ClientRequestServer;
import com.opensouce.component.netty.protocol.CustomDecoder;
import com.opensouce.component.netty.protocol.CustomEncoder;
import com.opensouce.component.netty.message.ServerResponseClient;
import com.opensouce.component.netty.task.WorkTaskQueue;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.util.function.BiConsumer;

/**
 * @author ZhangLong on 2019/12/7  1:00 下午
 * @version V1.0
 */
public class NettyClient<ReqData, RespData> {
    /**
     * description 引导类
     */
    private final Bootstrap bootstrap;
    /**
     * description 非阻塞事件处理的线程管理组件
     */
    private final NioEventLoopGroup workerGroup;
    private BiConsumer<ChannelHandlerContext, ServerResponseClient<RespData>> biConsumer;

    private ChannelClientCache<ReqData> channelClientCache;
    public NettyClient(BiConsumer<ChannelHandlerContext, ServerResponseClient<RespData>> biConsumer,
                       ChannelClientCache<ReqData> channelClientCache) {
        bootstrap = new Bootstrap();
        workerGroup = new NioEventLoopGroup();
        bootstrap.group(workerGroup)
                //设置channel的类型为非阻塞
                .channel(NioSocketChannel.class);
        this.channelClientCache = channelClientCache;
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
                    System.out.println("connection server successful");
                } else {
                    System.out.println("connection server fail");
                    future.channel().closeFuture();
                    workerGroup.shutdownGracefully();
                    System.exit(0);
                }
            }).sync();
            WorkTaskQueue<ReqData> workTaskQueue = channelClientCache.registerChannel(socketAddress, channelFuture.channel());
            while(channelFuture.isDone()){
                ReqData reqData = workTaskQueue.poll();
                if(null != reqData){
                    ClientRequestServer<ReqData> clientRequestServer = new ClientRequestServer<>();
                    clientRequestServer.setData(reqData);
                    channelFuture.channel().writeAndFlush(clientRequestServer);
                }
            }
            channelFuture.channel().closeFuture().sync();
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
}
