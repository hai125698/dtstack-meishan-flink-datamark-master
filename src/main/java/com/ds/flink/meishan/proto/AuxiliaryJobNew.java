package com.ds.flink.meishan.proto;

/**
 * @ClassName: AuxiliaryJobNew
 * @Description: 所有作业类型
 * @author: ds-longju
 * @Date: 2022-11-08 14:25
 * @Version 1.0
 **/
public class AuxiliaryJobNew {
    //目的箱区
    private String vesselRef;

    // "HC" 舱盖板类型
    // "PL" 销子类型
    // “DM” 残损箱类型
    // "DL"  错位箱 dislocation
    // "OF"  溢卸
    // "GJ" 过街
    // "JK" 解捆
    // "ZL" 暂落
    private JobWorkType workType;

    //目的贝位
    private int bay;

    //机械ID
    private String rdtID;

    //任务执行开始时间(秒)
    private int jobtime;

    //起始位置
    private Position fromPos;

    //任务结束时间(秒)
    private int jobendtime;
}
