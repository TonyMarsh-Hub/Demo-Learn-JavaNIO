package nio.channel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Demo1 {
    public static void main(String[] args) throws IOException {
        String msg = "hi,dude";
        String fileName = "channel01.txt";
        //创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        //获取同一个通道--channel的实际类型是FileChannelImpl
        FileChannel channel = fileOutputStream.getChannel();
        //创建一个缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //将信息写入缓冲区中
        buffer.put(msg.getBytes());
        //将缓冲区中的数据写到到通道中
        int num = channel.write(buffer);
        System.out.println("写入完毕！" + num);
        fileOutputStream.close();
    }
}
