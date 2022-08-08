package nio.selector.demo2chatroom;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class ChatServer {
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.Listening();
    }

    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;
    private static final int PORT = 10086;

    public ChatServer() {
        try {
            //完成服务器端的初始化工作
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            //创建并注册Channel到selector
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //监听事件
    public void Listening() {
        try {
            while (true) {
                int selectedNum = selector.select(3000);
                //如果有就绪的事件，则进行处理
                if (selectedNum > 0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        if (key.isAcceptable()) {
                            handleAcceptableEvent();
                        }
                        if (key.isReadable()) {
                            handleReadEvent(key);
                        }
                        //避免循环处理已处理的事件
                        iterator.remove();
                    }
                }
                //如果没有就绪的事件，则短暂等待后处理别的事情
                if (selectedNum == 0) {
                    System.out.println("Server: 等的花儿都谢了.....我也先忙一会儿");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleAcceptableEvent() throws IOException {
        //处理连接事件，获取客户端Channel，并注册到selector中
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
        System.out.println("有客户端连接!连接的socketchannel：" + socketChannel.hashCode());
    }

    private void handleReadEvent(SelectionKey key) {
        SocketChannel channel = null;
        try {
            channel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int len = channel.read(buffer);
            if (len > 0) {
                //Bug fixed: 避免使用使用 new String(buffer.array() 来获取数据，因为可能会包含空数据。例如数据就1个字节，但是Buffer的size有1024字节，所以会包含1023个空数据。
                //解决方式：根据在读模式下，Buffer的limit属性就是整个数据的长度的特性,先获取Buffer的limit属性，创建一个大小和limit相等的数组，然后将buffer的数据拷贝到新数组
                buffer.flip();
                byte[] data = new byte[buffer.limit()];
                buffer.get(data);
                String msg = new String(data);

                System.out.println("Server: 收到客户端的消息是：" + msg);
                //这是一个CharRoom的Server，所以处理完成一个客户端的读信息后，确保该消息转发给其他的客户端
                System.out.println("Server: 转发消息给其他的客户端");
                forwardMsgToTheRestClient(selector, channel, msg);
            }
        } catch (IOException e) {
            //待完善，可以还会有其他Exception出现，这种处理方案是不够的
            try {
                System.out.println(channel.getRemoteAddress() + "客户离开了聊天室");
                key.cancel(); //取消注册
                channel.close(); //关闭通道
            } catch (IOException ex) {
                System.out.println("没获取到客户端的地址");
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * 当处理完成一个客户端的读信息后，使该消息转发给其他的客户端
     * 应当在handleReadEvent方法中被调用
     *
     * @param selector                selector
     * @param channelAlreadyProcessed 已经处理过的channel
     */
    private void forwardMsgToTheRestClient(Selector selector, SocketChannel channelAlreadyProcessed, String msg) {
        for (SelectionKey key : selector.keys()) {
            SelectableChannel channel = key.channel();
            if (channel instanceof SocketChannel && channel != channelAlreadyProcessed) {
                SocketChannel socketChannel = (SocketChannel) channel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                try {
                    socketChannel.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
