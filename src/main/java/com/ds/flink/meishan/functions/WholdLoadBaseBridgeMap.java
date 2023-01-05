package com.ds.flink.meishan.functions;

import com.alibaba.fastjson.JSONObject;
import com.ds.flink.meishan.until.YmlUtils;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: WholdLoadBaseBridgeMap
 * @Description: 全量加载桥吊的基础数据
 * @author: ds-longju
 * @Date: 2022-11-08 14:05
 * @Version 1.0
 **/
public class WholdLoadBaseBridgeMap extends RichMapFunction<JSONObject,JSONObject> {
    private static Map<String, JSONObject> cache ;

    private static final String JDBCURL = YmlUtils.getYmlValue("jdbcURL");

    private static final String USERNAME = YmlUtils.getYmlValue("username");

    private static final String PASSWORD = YmlUtils.getYmlValue("password");

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        super.open(parameters);
        cache = new HashMap<>();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },0,1800, TimeUnit.MINUTES); //从现在开始每隔1800分钟全量查询一份数据
    }

    @Override
    public JSONObject map(JSONObject jsonObject) throws Exception {
        String name = jsonObject.getString("name");
        JSONObject baseBridgeJson = cache.get(name);
        if(baseBridgeJson != null) {
            jsonObject.putAll(baseBridgeJson);
        }
        return jsonObject;
    }


    public   void  load() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        // 查询集卡列表
        Connection con = DriverManager.getConnection(JDBCURL, USERNAME, PASSWORD);
        PreparedStatement statement = con.prepareStatement("select equipmentId,equipmentLength,equipmentHeight,equipmentWidth,loadCapability,status,useAble,craneColor,craneFromPosition,craneToPosition,doHazardFlag from dim_redis_base_bridgecrane");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            String equipmentId = rs.getString("equipmentId");
            String equipmentLength = rs.getString("equipmentLength");
            String equipmentHeight = rs.getString("equipmentHeight");
            String equipmentWidth = rs.getString("equipmentWidth");
            String loadCapability = rs.getString("loadCapability");
            String status = rs.getString("status");
            String useAble = rs.getString("useAble");
            String craneColor = rs.getString("craneColor");
            String craneFromPosition = rs.getString("craneFromPosition");
            String craneToPosition = rs.getString("craneToPosition");
            String doHazardFlag = rs.getString("doHazardFlag");

            JSONObject baseBridgeJson = new JSONObject();
            baseBridgeJson.put("equipmentLength",equipmentLength);
            baseBridgeJson.put("equipmentHeight",equipmentHeight);
            baseBridgeJson.put("equipmentWidth",equipmentWidth);
            baseBridgeJson.put("loadCapability",loadCapability);
            baseBridgeJson.put("status",status);
            baseBridgeJson.put("useAble",useAble);
            baseBridgeJson.put("craneColor",craneColor);
            baseBridgeJson.put("craneFromPosition",craneFromPosition);
            baseBridgeJson.put("craneToPosition",craneToPosition);
            baseBridgeJson.put("doHazardFlag",doHazardFlag);

            cache.put(equipmentId,baseBridgeJson);
        }
        con.close();

    }
}

