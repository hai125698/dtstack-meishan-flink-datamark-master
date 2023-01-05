package com.ds.flink.meishan.sink;

import com.alibaba.fastjson.JSONObject;
import com.ds.flink.meishan.until.YmlUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * @ClassName: sinkBridgeCraneToMysql
 * @Description: 桥吊基础数据写到mysql
 * @author: ds-longju
 * @Date: 2022-11-06 16:42
 * @Version 1.0
 **/
public class sinkBridgeCraneToMysql extends RichSinkFunction<JSONObject> {
    PreparedStatement ps;
    private Connection connection;

    private static final String JDBCURL = YmlUtils.getYmlValue("jdbcURL");

    private static final String USERNAME = YmlUtils.getYmlValue("username");

    private static final String PASSWORD = YmlUtils.getYmlValue("password");



    /**
     * open() 方法中建立连接，这样不用每次 invoke 的时候都要建立连接和释放连接
     *
     * @param parameters
     * @throws Exception
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        connection = getConnection();
        // dim_redis_base_bridgeCrane mysql 桥吊基本信息
        String sql = "REPLACE into  dim_redis_base_bridgeCrane (id,equipmentId,equipmentLength,equipmentHeight,equipmentWidth,loadCapability,status,useAble,craneColor,craneFromPosition,craneToPosition,doHazardFlag,updateTime) values(?, ?, ?, ?,?, ?, ?,?,?,?,?,?,?);";
        ps = this.connection.prepareStatement(sql);
    }

    @Override
    public void close() throws Exception {
        super.close();
        //关闭连接和释放资源
        if (connection != null) {
            connection.close();
        }
        if (ps != null) {
            ps.close();
        }
    }

    /**
     * 每条数据的插入都要调用一次 invoke() 方法
     * @param jsonObject
     * @param context
     * @throws Exception
     */
    @Override
    public void invoke(JSONObject jsonObject, SinkFunction.Context context) throws Exception {
        ps.setString(1, jsonObject.getString("id"));
        ps.setString(2, jsonObject.getString("equipmentId"));
        ps.setString(3, jsonObject.getString("equipmentLength"));
        ps.setString(4, jsonObject.getString("equipmentHeight"));
        ps.setString(5, jsonObject.getString("equipmentWidth"));
        ps.setString(6, jsonObject.getString("loadCapability"));
        ps.setString(7, jsonObject.getString("status"));
        ps.setString(8, jsonObject.getString("useAble"));
        ps.setString(9, jsonObject.getString("craneColor"));
        ps.setString(10, jsonObject.getString("craneFromPosition"));
        ps.setString(11, jsonObject.getString("craneToPosition"));
        ps.setString(12, jsonObject.getString("doHazardFlag"));
        ps.setString(13, jsonObject.getString("updateTime"));
        ps.executeUpdate();
    }

    private static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(JDBCURL,USERNAME,PASSWORD);
        } catch (Exception e) {
            System.out.println("-----------mysql get connection has exception , msg = "+ e.getMessage());
        }
        return con;
    }
}
