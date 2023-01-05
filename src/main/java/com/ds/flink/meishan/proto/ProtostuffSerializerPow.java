package com.ds.flink.meishan.proto;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @ClassName: ProtostuffSerializerPow
 * @Description: Pow序列化
 * @author: ds-longju
 * @Date: 2022-11-02 10:51
 * @Version 1.0
 **/
public class ProtostuffSerializerPow {
    private Schema<Pow> schema = RuntimeSchema.createFrom(Pow.class);

    public byte[] serialize(final Pow pow) {
        final LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            return serializeInternal(pow, schema, buffer);
        } catch (final Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    public Pow deserialize(final byte[] bytes) {
        try {
            Pow pow = deserializeInternal(bytes, schema.newMessage(), schema);
            if (pow != null) {
                return pow;
            }
        } catch (final Exception e) {
            System.out.println(e);
            throw new IllegalStateException(e.getMessage(), e);
        }
        return null;
    }

    private <T> byte[] serializeInternal(final T source, final Schema<T>
            schema, final LinkedBuffer buffer) {
        return ProtostuffIOUtil.toByteArray(source, schema, buffer);
    }

    private <T> T deserializeInternal(final byte[] bytes, final T result, final
    Schema<T> schema) {
        ProtostuffIOUtil.mergeFrom(bytes, result, schema);
        return result;
    }

}
