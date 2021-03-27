package zero.mgy.netty.echo.test_transport;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class NettyNioServer {
    public void server(int port) throws Exception {
        final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
        NioEventLoopGroup boossGroup = new NioEventLoopGroup(1);    //2使用 NioEventLoopGroup 允许非阻塞模式（NIO）
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.localAddress(new InetSocketAddress(port))
                    .channel(NioServerSocketChannel.class)
                    .group(boossGroup, workerGroup)
                    .childHandler(new ChannelInitializer<SocketChannel>() {//3  指定 ChannelInitializer 将给每个接受的连接调用
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() { //4   添加的 ChannelInboundHandlerAdapter() 接收事件并进行处理

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    ctx.writeAndFlush(buf.duplicate())  //5 写信息到客户端，并添加 ChannelFutureListener 当一旦消息写入就关闭连接
                                            .addListener(ChannelFutureListener.CLOSE);
                                }
                            });
                        }
                    });
            ChannelFuture f = b.bind().sync();  //6 绑定服务器来接受连接
            f.channel().closeFuture().sync();
        }finally {
            boossGroup.shutdownGracefully().sync(); //7 释放所有资源
            workerGroup.shutdownGracefully().sync();
        }
    }
}
