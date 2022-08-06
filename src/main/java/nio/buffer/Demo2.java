package nio.buffer;

import java.nio.ByteBuffer;

/**
 * 另一个使用java.nio Buffer的简单例子
 * 类型化写入时，注意在读取的时候要 **同步写入顺序地** 使用相应的get方法
 */
public class Demo2 {
    public static void main(String[] args) {
        //1、创建buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //2、写入数据
        // 按照类型化方式写入,在保存时会自动转换为字节数组
        buffer.putChar('K');
        buffer.putLong(1024L);
        buffer.putInt(512);
        buffer.putShort((short) 0);
        //3、读写切换
        buffer.flip();
        //4、读取数据
        //注意,需要和put时的类型化顺序保持一致，否认会乱码
        System.out.println(buffer.getChar());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getInt());
        System.out.println(buffer.getShort());
    }
}
