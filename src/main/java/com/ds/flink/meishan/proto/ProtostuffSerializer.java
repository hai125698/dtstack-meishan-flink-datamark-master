package com.ds.flink.meishan.proto;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * @ClassName: ProtostuffSerializer
 * @Description: Che 序列化
 * @author: ds-longju
 * @Date: 2022-11-02 10:51
 * @Version 1.0
 **/
public class ProtostuffSerializer {
    private Schema<Che> schema = RuntimeSchema.createFrom(Che.class);

    public byte[] serialize(final Che che) {
        final LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            return serializeInternal(che, schema, buffer);
        } catch (final Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    public Che deserialize(final byte[] bytes) {
        try {
            Che che = deserializeInternal(bytes, schema.newMessage(), schema);
            if (che != null) {
                return che;
            }
        } catch (final Exception e) {
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
//private Schema<User> schema = RuntimeSchema.createFrom(User.class);
//
//    public byte[] serialize(final User user) {
//        final LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
//        try {
//            return serializeInternal(user, schema, buffer);
//        } catch (final Exception e) {
//            throw new IllegalStateException(e.getMessage(), e);
//        } finally {
//            buffer.clear();
//        }
//    }
//
//    public User deserialize(final byte[] bytes) {
//        try {
//            User user = deserializeInternal(bytes, schema.newMessage(), schema);
//            if (user != null) {
//                return user;
//            }
//        } catch (final Exception e) {
//            throw new IllegalStateException(e.getMessage(), e);
//        }
//        return null;
//    }
//
//    private <T> byte[] serializeInternal(final T source, final Schema<T>
//            schema, final LinkedBuffer buffer) {
//        return ProtostuffIOUtil.toByteArray(source, schema, buffer);
//    }
//
//    private <T> T deserializeInternal(final byte[] bytes, final T result, final
//    Schema<T> schema) {
//        ProtostuffIOUtil.mergeFrom(bytes, result, schema);
//        return result;
//    }
}
