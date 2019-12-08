package com.opensouce.component.netty.protocol;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import com.dyuproject.protostuff.Schema;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZhangLong on 2019/12/7  9:01 下午
 * @version V1.0
 */
public class SerializationUtil {
    private static Objenesis objenesis = new ObjenesisStd(true);

    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();
    /**
     * 反序列化（字节数组 -> 对象）
     */
    public static <T> T deserialize(byte[] data, Class<T> genericClass) {
        try {
            T message = objenesis.newInstance(genericClass);
            Schema<T> schema = getSchema(genericClass);
            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static <T> Schema<T> getSchema(Class<T> genericClass) {
        return (Schema<T>) cachedSchema.computeIfAbsent(genericClass, RuntimeSchema::getSchema);
    }

    /**
     * 序列化（对象 -> 字节数组）
     */
    public static <T> byte[] serialize(T obj) {
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(cls);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }
}
