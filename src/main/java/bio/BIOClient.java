package bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class BIOClient {
    public static void main(String[] args) {
        try {
            //创建客户端
            Socket client = new Socket("localhost", 10086);
            String msg = "hi,buddy";
            OutputStream outputStream = client.getOutputStream();
            outputStream.write(msg.getBytes(), 0, msg.length());
            outputStream.close();
            System.in.read();//让客户端与服务器保持连接
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
