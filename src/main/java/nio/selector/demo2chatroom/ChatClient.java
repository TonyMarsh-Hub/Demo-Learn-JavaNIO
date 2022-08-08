package nio.selector.demo2chatroom;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class ChatClient {
    private SocketChannel socketChannel;
    private Selector selector;
    private String userName;//用于区别谁在聊天,直接用LocalAddress赋值

    public ChatClient() {
        try {
            //初始化
            String HOSTNAME = "127.0.0.1";
            int PORT = 10086;
            socketChannel = SocketChannel.open(new InetSocketAddress(HOSTNAME, PORT));
            socketChannel.configureBlocking(false);
            selector = Selector.open();
            socketChannel.register(selector, SelectionKey.OP_READ);
            userName = socketChannel.getLocalAddress().toString();
            System.out.println(userName + " is ready!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInfo(String info) {
        try {
            info = userName + ":" + info;
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readInfo() {
        try {
            int readyChannel = selector.select();
            if (readyChannel > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        socketChannel.read(buffer);

                        //Bug fixed: 避免使用使用 new String(buffer.array() 来获取数据，因为可能会包含空数据。例如数据就1个字节，但是Buffer的size有1024字节，所以会包含1023个空数据。
                        //解决方式：根据在读模式下，Buffer的limit属性就是整个数据的长度的特性,先获取Buffer的limit属性，创建一个大小和limit相等的数组，然后将buffer的数据拷贝到新数组
                        buffer.flip();
                        byte[] data = new byte[buffer.limit()];
                        buffer.get(data);
                        String msg = new String(data);

                        System.out.println(msg);
                    }
                    iterator.remove();
                }
            } else {
                System.out.println("没有准备就绪的通道！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
        new Thread(() -> {
            while (true) {
                chatClient.readInfo();
            }
        }).start();

        Scanner input = new Scanner(System.in);
        while (input.hasNextLine()) {
            String msg = input.nextLine();
            chatClient.sendInfo(msg);
        }
    }
}
