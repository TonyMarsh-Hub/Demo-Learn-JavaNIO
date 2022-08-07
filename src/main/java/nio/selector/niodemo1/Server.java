package nio.selector.niodemo1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {
    public static void main(String[] args) throws IOException {
        //创建一个ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //绑定服务器端口
        serverSocketChannel.bind(new InetSocketAddress(10086));
        //将Channel设置为非堵塞
        serverSocketChannel.configureBlocking(false);

        //创建Selector
        Selector selector = Selector.open();
        //将通道注册到selector中,并监听的请求连接事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //保持服务器运行
        while (true) {

            //如果等3000毫秒没等到就绪的Channel，则做其他事 --- 非阻塞
            if (selector.select(3000) == 0) {
                System.out.println("Server: 等的花儿都谢了.....我也先忙一会儿");
                continue;
            }

            //通过selectedKeys方法获取已就绪的的 通道注册信息
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            //对每一个就绪事件进行处理
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {//如果是连接事件
                    //获取连接到服务器的客户端Channel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //将其设置为非堵塞
                    socketChannel.configureBlocking(false);
                    System.out.println("有客户端连接!连接的socketchannel：" + socketChannel.hashCode());
                    //注册客户端Channel到selector中，并监听该通道中的读事件(读就绪事件)
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if (key.isReadable()) {//如果是OP_READ事件
                    //获取该Channel
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    //数据读取操作
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    socketChannel.read(buffer);
                    System.out.println("来自客户端的消息是：" + new String(buffer.array()));
                }
                //事件处理完成后，删除该事件，防止重复处理进入死循环
                iterator.remove();
            }
        }
    }
}
