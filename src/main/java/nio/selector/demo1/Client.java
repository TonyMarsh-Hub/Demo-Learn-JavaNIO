package nio.selector.demo1;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {
    public static void main(String[] args) throws IOException {
        //创建一个客户端
        SocketChannel socketChannel = SocketChannel.open();
        //注意要将Channel设置为非堵塞
        socketChannel.configureBlocking(false);
        //准备要连接的服务器名称和端口号
        InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", 10086);
        //连接服务器
        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("Client:努力连接中.....可以先做点其他事情.......");
            }
        }
        //连接成功之后发消息给服务器
        String msg = "hi,dude";
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        socketChannel.write(buffer);
        System.in.read();
    }
}
