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
                        String msg = new String(buffer.array());
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
