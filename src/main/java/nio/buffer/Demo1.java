package nio.buffer;

import java.nio.ByteBuffer;

/**
 * 一个使用java.nio Buffer的简单例子
 */
public class Demo1 {
    public static void main(String[] args) {
        //1.创建缓冲区，写入数据到buffer中
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put((byte) 'h');
        buffer.put((byte) 'i');
        //2.调用flip()方法将缓冲区切换为读取模式
        buffer.flip();
        //3.从Buffer中读取数据
        while (buffer.hasRemaining()) {
            byte temp = buffer.get();
            System.out.println((char) temp);
        }
        //4.重置缓冲区
        buffer.clear();
    }
}
