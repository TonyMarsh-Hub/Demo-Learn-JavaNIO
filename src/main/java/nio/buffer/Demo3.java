package nio.buffer;

import java.nio.ByteBuffer;

/**
 * and another one
 * 使用asReadOnlyBuffer() 将Buffer转换为只读的
 */
public class Demo3 {
    public static void main(String[] args) {
        //创建一个buffer
        ByteBuffer buffer = ByteBuffer.allocate(10);
        //写入数据
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte) i);
        }
        //读写切换
        buffer.flip();
        //获取一个只读的buffer:普通的buffer转换为了只读的buffer
        ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
        System.out.println("readOnlyBuffer的类型：" + readOnlyBuffer.getClass());
        //读取数据
        while (readOnlyBuffer.hasRemaining()) {
            System.out.println(readOnlyBuffer.get());
        }
        //写入数据--会抛出ReadOnlyBufferException异常，只读buffer不可以写入数据
        readOnlyBuffer.put((byte) 66);//此处会抛出异常

    }
}
