package com.ds.flink.meishan.functions;


import com.alibaba.fastjson.JSONObject;
import com.ds.flink.meishan.proto.FaultInfo;
import com.ds.flink.meishan.until.TimeUntil;
import org.apache.flink.api.common.functions.MapFunction;


/**
 * @ClassName: MarkBridgeCraneMap
 * @Description: 对桥吊数据进行打标
 * @author: ds-longju
 * @Date: 2022-11-08 14:46
 * @Version 1.0
 **/
public class MarkBridgeCraneMap  implements MapFunction<JSONObject, JSONObject> {

    @Override
    public JSONObject map(JSONObject js) throws Exception {

        //打标结果
        JSONObject markJSON = new JSONObject();

        // 基础打标
        markJSON.put("userId",js.getString("crUserID"));
        markJSON.put("craneName",js.getString("name"));
        markJSON.put("preWINo",js.getString("currWIRefNo"));
        markJSON.put("cextWINo",js.getString("currWIRefNo2"));
        markJSON.put("rdtStatus",js.getString("rDTStatus"));
        markJSON.put("lastWINo",js.getString("lastWIRefNo"));
        markJSON.put("bdUserId",js.getString("bdUserID"));
        markJSON.put("craneHeight",js.getString("equipmentHeight"));
        markJSON.put("craneFromPosition",js.getString("craneFromPosition"));
        markJSON.put("craneToPosition",js.getString("craneToPosition"));





        // 获取桥吊故障
        String state = js.getString("statusInfo");
        if (state != null && state != "null" && !"".equals(state)) {
            String[] arr = state.split("-");
            //故障桥吊使用传入的船舶参考号
            if (arr.length == 4) {
                String type = arr[0]; // 故障类型
                String block = arr[3]; // 所在船舶
                String bay = arr[1];    // 所在贝位
                if (arr[1].length() > 2) {

                }
                String mark = arr[2];   // 标记
                // 设定龙门吊故障信息
                markJSON.put("faultInfo",new FaultInfo(type, block, bay, mark)) ;

            }
            markJSON.put("isFault",true);
        }

        // 虚拟打标
        String workStatusBits = js.getString("workStatusBits");
        if (workStatusBits != null && workStatusBits.getBytes().length >= 3) {
            // 虚拟设备打标
            if ((workStatusBits.getBytes()[2] & 0x01) == 0x01) {
                markJSON.put("isFake",true);
                markJSON.put("isIECSUse",false);
            }
        }


        // 作业阶段: 计算不同阶段的剩余工作时长（离散值）
        markJSON.put("jobStatus",2);
        Integer trolleySpd = js.getInteger("trolleySpd");
        Boolean spreaderUnlock = js.getBoolean("spreaderUnlock");
        if (trolleySpd != null &&  spreaderUnlock != null){
            if(trolleySpd > 0 && spreaderUnlock){
                markJSON.put("jobStatus",3);
            }
            if(spreaderUnlock){
                markJSON.put("jobStatus",4);
            }
        }

        // OPTIM iECS可用打标
        // 虚拟设备不用
        String dispatchMode = js.getString("dispatchMode");

        // 非IECS模式，不用
        if (dispatchMode != null && "1" != dispatchMode ) {
            markJSON.put("isIECSUse",false);
        }
        // 车道号
        markJSON.put("laneNo",js.getInteger("laneNo"));

        // 前后大梁
        if(js.getInteger("laneNo") != null && js.getInteger("laneNo") >= 8 ){
            markJSON.put("isOnRearGirder",true);
        }else{
            markJSON.put("isOnRearGirder",false);
        }

        //指令分发模式 '0':MANL-人工指派; '1':PRTT-全场调度; '2':TRUCK-将箱子确认到车上时，集卡才收到指令;
        markJSON.put("dispatchMode",js.getString("dispatchMode"));

        // 相对优先级 0-10，值越大优先级
        markJSON.put("powRelativePriority",js.getInteger("powRelativePriority"));

        // 'S'-单循环, 默认;'D'-双循环
        markJSON.put("cycleMode",js.getString("cycleMode"));

        // 在装卸船过程中,设置是否允许1车2箱: '0' - off; '1'- 2X20's for Load; '2'-2X20's for Discharge; '3' - for Load & Discharge
        markJSON.put("truck220",js.getString("truck220"));

        // single cycle rate：设定的单循环作业效率
        markJSON.put("production",js.getString("production"));

        // dual cycle rate：设定的双循环作业效率
        markJSON.put("dualProduction",js.getString("dualProduction"));

        //最多分配集卡个数
        markJSON.put("maxTruck",js.getInteger("powMaxPMS"));

        //最少分配集卡个数
        markJSON.put("minTruck",js.getInteger("powMinPMS"));

        //最多允许上码头面的集卡数
        markJSON.put("maxDockTruckNum",js.getInteger("truckToPowMaxPMS"));

        // 数据进入时间
        markJSON.put("dataStartTime",js.getString("dataStartTime"));

        // 数据产生时间
        markJSON.put("collectTime",js.getString("collectTime"));

        // 数据打标结束时间
        markJSON.put("endTime", TimeUntil.getCurrentStamp());



        return markJSON;




    }
}
