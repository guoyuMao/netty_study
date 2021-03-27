package zero.mgy.netty.echo.test_transport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class PlainNioServer {
    public void serve(int port) throws IOException{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        ServerSocket ss = serverSocketChannel.socket();
        ss.bind(new InetSocketAddress(port));                        //1 绑定服务器到制定端口

        Selector selector = Selector.open();    //2 打开 selector 处理 channel
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); //3 注册 ServerSocket 到 ServerSocket ，并指定这是专门意接受 连接。
        final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());
        for (; ; ) {
            try {
                selector.select();  //4 等待新的事件来处理。这将阻塞，直到一个事件是传入
            } catch (IOException exception) {
                exception.printStackTrace();
                break;
            }
            Set<SelectionKey> readyKeys = selector.selectedKeys();  //5 从收到的所有事件中 获取 SelectionKey 实例。
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                try {
                    if (key.isAcceptable()) {   //6检查该事件是一个新的连接准备好接受。
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, msg.duplicate());   //7接受客户端，并用 selector 进行注册。
                        System.out.println("Accepted connection from " + client);
                    }
                    if (key.isWritable()) { //8 检查 socket 是否准备好写数据。
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        while (buffer.hasRemaining()) {  //9 将数据写入到所连接的客户端。如果网络饱和，连接是可写的，那么这个循环将写入数据，直到该缓冲区是空的。
                            if (client.write(buffer) == 0) {
                                break;
                            }
                        }
                        client.close(); //10    关闭连接。
                    }
                } catch (IOException exception) {
                    key.channel();
                    try {
                        key.channel().close();
                    } catch (IOException exception1) {

                    }
                }
            }
        }
    }
}
