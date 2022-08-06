package nio.buffer;

import java.nio.ByteBuffer;

/**
 * 一个使用java.nio Buffer的简单例子
 */
public class Demo1 {
    public static void main(String[] args) {
        //1.创建缓冲区，写入数据到buffer中
        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        buffer.put((byte) 'h');
//        buffer.put((byte) 'i');
        //优化1：
        buffer.put("hi,dude".getBytes());
        //2.调用flip()方法将缓冲区切换为读取模式
        buffer.flip();
        //3.从Buffer中读取数据
//        while (buffer.hasRemaining()) {
//            byte temp = buffer.get();
//            System.out.println((char) temp);
//        }
        //优化2：
        //通过limit()方法获取缓冲区中数据的容量，以此为size创建一个byte数组
        byte[] data = new byte[buffer.limit()];
        //指定一个byte数据，将缓冲区中的数据读取到数组中
        buffer.get(data);
        System.out.println(new String(data));
        //4.重置缓冲区
//        buffer.clear();
        //优化3：
        buffer.compact();//也可以使用compact()方法
    }
}
