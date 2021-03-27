package zero.mgy.echo.test_transport;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class PlainOioServer {
    public void server(int port) throws IOException{
        final ServerSocket socket = new ServerSocket(port);
        try {
            for (; ; ) {
                final Socket clientSorket = socket.accept();    //2
                System.out.println("Accepted connection from " + clientSorket);

                new Thread(()->{
                        OutputStream out;
                    try {
                        out = clientSorket.getOutputStream();
                        out.write("Hi!\r\n".getBytes(Charset.forName("UTF-8")));    //将消息发送到连接的客户端。
                        out.flush();
                        clientSorket.close();   //5.一旦消息被写入和刷新时就 关闭连接。

                    } catch (IOException e) {
                        e.printStackTrace();
                        try {
                            clientSorket.close();
                        } catch (IOException exception) {

                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
