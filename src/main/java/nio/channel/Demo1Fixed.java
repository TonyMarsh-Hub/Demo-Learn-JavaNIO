package nio.channel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 通过javaNIO.channel写入数据到本地文件中
 */
public class Demo1Fixed {
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

        //对缓冲区读写切换
        buffer.flip();
        /*
         * 为什么要加上 buffer.flip() 这行代码?
         *
         * 因为缓冲区的读写模式是可变的，可以读取或写入，但是不能同时读取和写入，
         * 如果要切换读取和写入，则需要先执行flip操作，这与其底层的position和limit的操作有关。
         *
         * 写的时候，position指向缓冲区的写入位置，limit指向缓冲区的最后一个可写入的位置。
         * 从position开始，到limit结束，就是要写入的数据。
         * 在进行flip操作后，position指向缓冲区的开始位置，limit指向缓冲区的(最后一个数据的)写入位置。
         * 从position开始，到limit结束，此时这些数据就是要读取的数据。
         *
         * 图示：(https://seasidetown.notion.site/NIO-Buffer-67a8479c11544111948280bf68650339#569175ddaa2a4c239795c5946cb22fb9)
         *
         * 而如果不进行flip，就进行读取操作
         * 那么读取的就全都是null
         *
         */

        //将缓冲区中的数据写到到通道中
        int num = channel.write(buffer);
        System.out.println("写入完毕！" + num);
        fileOutputStream.close();
        /*
         * 重点就是Buffer不能同时读取和写入，需要进行flip操作来实现两种模式的切换
         * 一开始Buffer在写，而当Channel要读Buffer的时候，就需要进行flip操作来使Buffer变为读模式
         */
    }
}
