#netty_study
##一、模块
##### BOOTSTRAP
Netty 应用程序通过设置 bootstrap（引导）类的开始，该类提供了一个 用于应用程序网络层配置的容器。
##### CHANNEL
底层网络传输 API 必须提供给应用 I/O操作的接口，如读，写，连接，绑定等等。

定义了与 socket 丰富交互的操作集：

1.bind, close, config, connect, isActive, isOpen, isWritable, read, write 等等。

Netty 提供大量的 Channel 实现来专门使用。这些包括 AbstractChannel，AbstractNioByteChannel，AbstractNioChannel，EmbeddedChannel， LocalServerChannel，NioSocketChannel 等等。
