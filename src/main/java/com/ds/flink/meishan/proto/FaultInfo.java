package com.ds.flink.meishan.proto;

/**
 * @ClassName: FaultInfo
 * @Description: 故障信息
 * @author: ds-longju
 * @Date: 2022-11-08 14:24
 * @Version 1.0
 **/
public class FaultInfo {

    public FaultInfo() {
    }

    public FaultInfo(String type, String block, String bay, String mark) {
        this.type = type;
        this.block = block;
        this.bay = bay;
        this.mark = mark;
    }

    private long time = System.currentTimeMillis();
    // 故障类型
    private String type;
    // 所在箱区
    private String block;
    // 所在贝位
    private String bay;
    // 标记
    private String mark;
}
