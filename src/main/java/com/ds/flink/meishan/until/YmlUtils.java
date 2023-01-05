package com.ds.flink.meishan.until;

import org.apache.flink.shaded.jackson2.org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

/**
 * @ClassName: YmlUtils
 * @Description: yml 文件读取
 *  application_pro.yml 生产环境的配置
 *  application_test.yml 测试环境的配置
 * @author: ds-longju
 * @Date: 2022-09-13 14:31
 * @Version 1.0
 **/
public class YmlUtils {
    public static String getYmlValue(String key){
        Map<String,Object> obj =null;
        try {
            Yaml yaml = new Yaml();
            InputStream resourceAsStream = YmlUtils.class.getClassLoader().getResourceAsStream("application_pro.yml");
            obj = (Map) yaml.load(resourceAsStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String,Object> spring=(Map<String,Object>)obj.get("mysql_connect");
        String value=(String)spring.get(key);
        return value;
    }
}
