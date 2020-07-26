#netty_study
##一、模块
### BOOTSTRAP
Netty 应用程序通过设置 bootstrap（引导）类的开始，该类提供了一个 用于应用程序网络层配置的容器。
#### Bootstrap(客户端)
Bootstrap用来连接远程主机，有1个EventLoopGroup

#### ServerBootstrap（服务端）
ServerBootstrap用来绑定本地端口，有2个EventLoopGroup


##### CHANNEL
底层网络传输 API 必须提供给应用 I/O操作的接口，如读，写，连接，绑定等等。

定义了与 socket 丰富交互的操作集：bind, close, config, connect, isActive, isOpen, isWritable, read, write 等等。

Netty 提供大量的 Channel 实现来专门使用。这些包括 AbstractChannel，AbstractNioByteChannel，AbstractNioChannel，EmbeddedChannel， LocalServerChannel，NioSocketChannel 等等。
##### CHANNELHANDLER
ChannelHandler 支持很多协议，并且提供用于数据处理的容器。
我们已经知道 ChannelHandler 由特定事件触发。 
ChannelHandler 可专用于几乎所有的动作，包括将一个对象转为字节（或相反），执行过程中抛出的异常处理。

常用的一个接口是 ChannelInboundHandler，这个类型接收到入站事件（包括接收到的数据）可以处理应用程序逻辑。当你需要提供响应时，你也可以从 ChannelInboundHandler 冲刷数据。一句话，业务逻辑经常存活于一个或者多个 ChannelInboundHandler。

##### CHANNELPIPELINE
ChannelPipeline 提供了一个容器给 ChannelHandler 链并提供了一个API 用于管理沿着链入站和出站事件的流动。
每个 Channel 都有自己的ChannelPipeline，当 Channel 创建时自动创建的。
ChannelHandler安装在ChannelPipeline主要是实现了ChannelHandler 的抽象 ChannelInitializer。
ChannelInitializer子类 通过 ServerBootstrap 进行注册。当它的方法 initChannel() 被调用时，这个对象将安装自定义的 ChannelHandler 集到 pipeline。当这个操作完成时，ChannelInitializer 子类则 从 ChannelPipeline 自动删除自身。

##### EVENTLOOP
EventLoop 用于处理 Channel 的 I/O 操作。一个单一的 EventLoop通常会处理多个 Channel 事件。
一个 EventLoopGroup 可以含有多于一个的 EventLoop 和 提供了一种迭代用于检索清单中的下一个。

##### CHANNELFUTURE
Netty 所有的 I/O 操作都是 ** 异步 ** 。因为一个操作可能无法立即返回，我们需要有一种方法在以后确定它的结果。
出于这个目的，Netty 提供了接口 ChannelFuture,它的 addListener 方法注册了一个 ChannelFutureListener ，当操作完成时，可以被通知（不管成功与否）。


## 其他
Netty 实际上是使用 Threads（多线程）处理 I/O 事件，对于熟悉多线程编程的读者可能会需要关注同步代码。这样的方式不好，因为同步会影响程序的性能，
Netty 的设计保证程序处理事件不会有同步


