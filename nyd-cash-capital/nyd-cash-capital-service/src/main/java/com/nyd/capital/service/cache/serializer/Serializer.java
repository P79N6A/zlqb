package com.nyd.capital.service.cache.serializer;



 /**
 * Cong Yuxiang
 * 2017/11/21
 */

public interface Serializer<T> {

    /**
     * 将制定对象序列化为二进制数据
     */
    byte[] serialize(T t) throws SerializationException;

    /**
     * 将二进制数据反序列化为对象
     */
    T deserialize(byte[] bytes) throws SerializationException;

}
