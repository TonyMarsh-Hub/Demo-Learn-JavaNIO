package nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件的复制
 */
public class Demo3 {
    public static void main(String[] args) throws IOException {
        //准备好要复制的源文件和目标文件
        File file = new File("channel01.txt");
        File fileCopy = new File("channel01Copy.txt");

        //复制分为两步：
        // 1.读取原数据到内存    (inputStream -> channel -> buffer)
        // 2.从内存写入目标文件  (buffer -> channel -> outputStream)
        // 所以需要两个io流的通道都要

        //创建输入和输出流
        FileInputStream fileInputStream = new FileInputStream(file);
        FileOutputStream fileOutputStream = new FileOutputStream(fileCopy);
        //获取两个通道
        FileChannel inChannel = fileInputStream.getChannel();
        FileChannel outChannel = fileOutputStream.getChannel();
        //创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        //每次读取一个capacity的数据到buffer缓冲区中，然后写入到目标文件
        //注意：1.使用flip切换读写模式 2.使用clear清空缓冲区
        int len;
        while (true) {
            //clear的作用是将标志位重置,即重置position和limit的值，从而可以重新读取数据
            buffer.clear();
            len = inChannel.read(buffer);
            System.out.println("len=" + len);
            if (len == -1) {
                break;
            }
            //读写切换
            buffer.flip();
            //将buffer中的数据写入到了通道中
            outChannel.write(buffer);
        }

        System.out.println("复制完毕！OK!");
        inChannel.close();
        outChannel.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
