package nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 从本地文件中读取数据
 */
public class Demo2 {
    public static void main(String[] args) throws IOException {
        File file = new File("channel01.txt");
        //创建输入流
        FileInputStream fileInputStream = new FileInputStream(file);
        //获取通道
        FileChannel channel = fileInputStream.getChannel();
        //创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
        //将通道中的数据读取buffer中
        channel.read(buffer);
        //将buffer中的字节数组转换为字符串输出
        System.out.println(new String(buffer.array()));
        fileInputStream.close();
    }
}
