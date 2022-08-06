package nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 文件复制优化
 * 原流程是：InputStream -> channel -> buffer -> channel -> outputStream
 * 其实channel实现类提供了实现好的方法来简化 channel之间的数据传输，
 * 只需要调用channel的transferTo和transferFrom方法即可完成数据的复制，这样可以省略对buffer的操作
 */
public class Demo3Improved {
    public static void main(String[] args) throws IOException {
        //要复制的文件和目标文件
        File file = new File("channel01.txt");
        File fileCopy = new File("channel01Copy.txt");
        //创建输入流和输出流
        FileInputStream fileInputStream = new FileInputStream(file);
        FileOutputStream fileOutputStream = new FileOutputStream(fileCopy);
        //获取两个通道
        FileChannel inChannel = fileInputStream.getChannel();
        FileChannel outChannel = fileOutputStream.getChannel();
        //使用transferFrom复制--两个方式适合大文件的复制
        outChannel.transferFrom(inChannel, 0, inChannel.size());
        //使用transferTo复制---
        //注意 to和from的方向是不同的 以及其方法参数也是不同的
        //inChannel.transferTo(0, inChannel.size(),outChannel);
        System.out.println("复制完毕！OK");
        inChannel.close();
        outChannel.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
