package com.zbw.big.study.util;

import java.util.Objects;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;


public class JsonUtil {

	private JsonUtil() {
    }
	
	private static ObjectMapper objectMapper = new ObjectMapper();
	
    /**
      *  注意以下配置，特别重要
      */
    static {
        //对象所有字段全部列入序列化
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.ALWAYS);
        /**
         * 所有日期全部格式化成时间戳
         * 因为即使指定了DateFormat，也不一定能满足所有的格式化情况，所以统一为时间戳，让使用者按需转换
         */
        objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, true);
        /**
         * 忽略空Bean转json的错误
         * 假设只是new方式创建对象，并且没有对里面的属性赋值，也要保证序列化的时候不报错
         */
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        /**
         * 忽略反序列化中json字符串中存在，但java对象中不存在的字段
         */
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 对象转换成json字符串
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2String(T obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            //即使序列化出错，也要保证程序走下去
            return null;
        }
    }

    /**
     * 对象转json字符串(带美化效果)
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String obj2StringPretty(T obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            //即使序列化出错，也要保证程序走下去
            return null;
        }
    }
}
