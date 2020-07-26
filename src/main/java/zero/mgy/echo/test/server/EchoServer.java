package zero.mgy.echo.test.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
        new EchoServer(8090).start();   //呼叫服务器的 start() 方法
    }

    public void start() throws Exception{
        NioEventLoopGroup group = new NioEventLoopGroup();  //创建 EventLoopGroup     处理事件的处理，如接受新的连接和读/写数据
        try{
            ServerBootstrap b = new ServerBootstrap();

            //引导服务器
            b.group(group)  ///创建 ServerBootstrap
                    .channel(NioServerSocketChannel.class)//指定使用 NIO 的传输 Channel
                    .localAddress(new InetSocketAddress(port))  //设置 socket 地址使用所选的端口
                    .childHandler(new ChannelInitializer<SocketChannel>() { //添加 EchoServerHandler 到 Channel 的 ChannelPipeline  当一个新的连接被接受，一个新的子 Channel 将被创建

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            // 绑定服务器
            ChannelFuture f = b.bind().sync();  //绑定的服务器;sync 等待服务器关闭

            System.out.println(EchoServer.class.getName() + " started and listen on " + f.channel().localAddress());
            f.channel().closeFuture().sync();   //关闭 channel 和 块，直到它被关闭
        }finally {
            group.shutdownGracefully().sync();  //关闭 EventLoopGroup，释放所有资源。
        }
    }
}
