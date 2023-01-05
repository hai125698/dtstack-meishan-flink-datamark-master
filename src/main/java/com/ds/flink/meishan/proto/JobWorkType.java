package com.ds.flink.meishan.proto;

import java.util.HashMap;
import java.util.Map;

public enum JobWorkType {
    /** none */
    NONE(""),

    /** 舱盖板类型 */
    HC("HC"),
    /** 销子类型 */
    PL("PL"),
    /** 残损箱类型 */
    DM("DM"),
    /** 错位箱 dislocation */
    DL("DL"),
    /** 溢卸 */
    OF("OF"),
    /** 过街 */
    GJ("GJ"),
    /** 解捆 */
    JK("JK"),
    /** 暂落 */
    ZL("ZL"),
    ;

    private String value;


    JobWorkType(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean equals(String typeStr){
        return this.value.equals(typeStr);
    }

    // 类型集合
    private static final Map<String, JobWorkType> MAP = new HashMap<>();

    static{
        for(JobWorkType f : JobWorkType.values()){
            MAP.put(f.getValue(), f);
        }
    }

    public static JobWorkType getType(String typeStr){
        JobWorkType type = JobWorkType.NONE;

        if(type == null){
            return type;
        }

        if(MAP.containsKey(typeStr)){
            type = MAP.get(typeStr);
        }

        return type;
    }
}
