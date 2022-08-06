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

        /*
        clear() 将position将被置为0，limit被设置成capacity的值。
        可以理解为Buffer被标记为清空了，但实际上Buffer中的数据并未清除，只是position和limit的值被重置了。
        如果Buffer中有一些未读的数据，调用clear()方法，未读数据将“被遗忘”，意味着不再有任何标记会告诉你哪些数据被读过，哪些还没有.

        compact() 将所有未读的数据拷贝到Buffer起始处。然后将position设到最后一个未读元素正后面。
        limit属性依然像clear()方法一样，设置成capacity。
        现在Buffer准备好写数据了，但是不会覆盖未读的数据。
        */

    }
}
