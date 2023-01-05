package com.ds.flink.meishan.proto;

import java.io.Serializable;

/**
 * @ClassName: Pow
 * @Description: Redis中的 Pow类
 * @author: ds-longju
 * @Date: 2022-11-10 09:49
 * @Version 1.0
 **/
public class Pow implements Serializable {
    public Pow() {
    }

    public Pow(String powName, String poolName, String dispatchMode, Integer powMaxPMS, Integer powMinPMS, Integer powRelativePriority, String cycleMode, String truck220, String production, String dualProduction, Integer laneNo, Integer truckToPowMaxPMS) {
        this.powName = powName;
        this.poolName = poolName;
        this.dispatchMode = dispatchMode;
        this.powMaxPMS = powMaxPMS;
        this.powMinPMS = powMinPMS;
        this.powRelativePriority = powRelativePriority;
        this.cycleMode = cycleMode;
        this.truck220 = truck220;
        this.production = production;
        this.dualProduction = dualProduction;
        this.laneNo = laneNo;
        this.truckToPowMaxPMS = truckToPowMaxPMS;
    }

    //工作点
    private String powName;

    //工作池
    private String poolName;

    //指令分发模式 '0':MANL-人工指派; '1':PRTT-全场调度; '2':TRUCK-将箱子确认到车上时，集卡才收到指令;
    private String dispatchMode;

    //最多分配集卡个数
    private Integer powMaxPMS;

    //最少分配集卡个数
    private Integer powMinPMS;

    //相对优先级 0-10，值越大优先级高
    private Integer powRelativePriority;


    //'S'-单循环, 默认;'D'-双循环
    private String cycleMode;

    //在装卸船过程中,设置是否允许1车2箱: '0' - off; '1'- 2X20's for Load; '2'-2X20's for Discharge; '3' - for Load & Discharge
    private String truck220;

    //single cycle rate：设定的单循环作业效率
    private String production;

    //dual cycle rate：设定的双循环作业效率
    private String dualProduction;

    //车道号
    private Integer laneNo;

    private Integer truckToPowMaxPMS;


    @Override
    public String toString() {
        return "{" +
                "\"powName\": \"" + powName + '\"' +
                ", \"poolName\": \"" + poolName + '\"' +
                ", \"dispatchMode\": \"" + dispatchMode + '\"' +
                ", \"powMaxPMS\": " + powMaxPMS +
                ", \"powMinPMS\": " + powMinPMS +
                ", \"powRelativePriority\": " + powRelativePriority +
                ", \"cycleMode\": \"" + cycleMode + '\"' +
                ", \"truck220\": \"" + truck220 + '\"' +
                ", \"production\": \"" + production + '\"' +
                ", \"dualProduction\": \"" + dualProduction + '\"' +
                ", \"laneNo\": " + laneNo +
                ", \"truckToPowMaxPMS\": " + truckToPowMaxPMS +
                '}';
    }
}
