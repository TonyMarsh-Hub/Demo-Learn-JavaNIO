package bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BIOServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(10086);
            System.out.println("服务器启动成功！");
            while (true) {
                //连接到服务器，这个方法会产生阻塞
                Socket client = serverSocket.accept();
                System.out.println("接收到一个连接！");
                //为每一个客户端创建一个线程用于通信
                new Thread(() -> {
                    System.out.println("线程id:" + Thread.currentThread().getId());
                    System.out.println("线程名称:" + Thread.currentThread().getName());
                    try {
                        int len = 0;
                        byte[] byteArr = new byte[1024];
                        //读取来自客户端的数据
                        InputStream inputStream = client.getInputStream();
                        while ((len = inputStream.read(byteArr)) != -1) {
                            String msg = new String(byteArr, 0, len);
                            System.out.println("来自客户端的消息：" + msg);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
