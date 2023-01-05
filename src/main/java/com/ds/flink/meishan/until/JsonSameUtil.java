package com.ds.flink.meishan.until;

import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * @ClassName: JsonSameUtil
 * @Description: 判断两个json是否一样
 * @author: ds-longju
 * @Date: 2022-11-07 20:00
 * @Version 1.0
 **/
public class JsonSameUtil {

    /**
     *JsonObject对象处理
     */
    public static boolean same(JSONObject a, JSONObject b){
        List unSameList=new ArrayList() ;
        Set<String> aSet=a.keySet();
        Set<String> bSet=b.keySet();
        if (!aSet.equals(bSet)){
            return false;
        }
        for (String aKet: aSet){
            if (!a.get(aKet).equals(b.get(aKet))){
                unSameList.add(aKet);
            }
        }
        if (!unSameList.isEmpty()){
            return false;
        }
        return true;
    }






}
