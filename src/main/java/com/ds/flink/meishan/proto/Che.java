package com.ds.flink.meishan.proto;

import java.io.Serializable;

/**
 * @ClassName: Che
 * @Description:
 * @author: ds-longju
 * @Date: 2022-11-02 10:41
 * @Version 1.0
 **/

public class Che implements Serializable {
    public Che() {
    }

    public Che(String ioShortName, Integer ioCheKind, Integer ioMaxWeight, Integer ioMaxTEU, Integer ioCheOperatingMode, String allowedLength, String poolName, String powName, Integer lastWIRefNo, Integer currWIRefNo, Integer currWIRefNo2, Integer planTEU, Integer planWeight, Byte dispatchBits, String ioLastPos, Integer ioCheStatus, String rDTStatus, String rDTUserID, Integer truckSEQ, String statusInfo, Byte workStatusBits, String crUserID, String bdUserID, String ioWorkPos, String ioHandleHaz) {
        this.ioShortName = ioShortName;
        this.ioCheKind = ioCheKind;
        this.ioMaxWeight = ioMaxWeight;
        this.ioMaxTEU = ioMaxTEU;
        this.ioCheOperatingMode = ioCheOperatingMode;
        this.allowedLength = allowedLength;
        this.poolName = poolName;
        this.powName = powName;
        this.lastWIRefNo = lastWIRefNo;
        this.currWIRefNo = currWIRefNo;
        this.currWIRefNo2 = currWIRefNo2;
        this.planTEU = planTEU;
        this.planWeight = planWeight;
        this.dispatchBits = dispatchBits;
        this.ioLastPos = ioLastPos;
        this.ioCheStatus = ioCheStatus;
        this.rDTStatus = rDTStatus;
        this.rDTUserID = rDTUserID;
        this.truckSEQ = truckSEQ;
        this.statusInfo = statusInfo;
        this.workStatusBits = workStatusBits;
        this.crUserID = crUserID;
        this.bdUserID = bdUserID;
        this.ioWorkPos = ioWorkPos;
        this.ioHandleHaz = ioHandleHaz;
    }

    //设备名
    private String ioShortName;

    //1:MAN(Inventory/Hatch Clerk)理货员 2:RTxx龙门吊 3:Txx集卡 4:CRx桥吊 5:Fxx/Pxx堆高机正面吊 6:Hxx场地理货 7:RHxxx手持理货  8:IHXX(智能理货)  9：IRTxx(远控龙门吊)
    private Integer ioCheKind;

    //设备起重能力或集卡最大承载重量 0.1吨
    private Integer ioMaxWeight;

    //最多容纳TEU数 小箱
    private Integer ioMaxTEU;

    // 1(RTxx类的):Default-Require Handler; 2:Self-Complete(自完成模式，例如堆高机); 3(Txx类的):Default-TRUCK; 4(CRx类的):Default-Crane; -- 非字符，十进制 参考SPARCS吧。
    // 5-(IRT类)IRT-Manual 6-(IRT类)IRT-Auto 8-无人集卡
    private Integer ioCheOperatingMode;

    // '0': None; '1':20ft; '2':40ft; '3':Any (Fixed Spreader); '4':Any (Hydraulic Spreader); '5': No half heights - 20ft
    private String allowedLength;

    //工作池
    private String poolName;

    //工作点
    private String powName;

    //上次完成的指令号
    private Integer lastWIRefNo;

    //当前在执行的指令号，集卡第一个装箱指令
    private Integer currWIRefNo;

    //集卡装第二个箱子的时候，第二个装箱指令
    private Integer currWIRefNo2;

    //集卡内部已经计划的TEU数
    private Integer planTEU;

    //预配装载的箱子重量
    private Integer planWeight;

    // 0-CurrWIRefNo状态；即pWI(CurrWIRefNo)->DispatchWIBits[2]的值
    private Byte dispatchBits;

    //最后的位置信息串，如: Y:A27103.4
    private String ioLastPos;

    //仅仅用于特殊显示使用，正常为0
    private Integer ioCheStatus;

    //对于室内理货，进入工作点输入界面为'F',工作点登陆成功后为'B'
    private String rDTStatus;

    //登录用户
    private String rDTUserID;

    //集卡排序
    private Integer truckSEQ;

    private String statusInfo;

    private Byte workStatusBits;

    private String crUserID;

    private String bdUserID;

    private String ioWorkPos;

    private String ioHandleHaz;

    @Override
    public String toString() {
        return "{" +
                "\"ioShortName\": \"" + ioShortName + '\"' +
                ", \"ioCheKind\": " + ioCheKind +
                ", \"ioMaxWeight\": " + ioMaxWeight +
                ", \"ioMaxTEU\": " + ioMaxTEU +
                ", \"ioCheOperatingMode\": " + ioCheOperatingMode +
                ", \"allowedLength\": \"" + allowedLength + '\"' +
                ", \"poolName\": \"" + poolName + '\"' +
                ", \"powName\": \"" + powName + '\"' +
                ", \"lastWIRefNo\": " + lastWIRefNo +
                ", \"currWIRefNo\": " + currWIRefNo +
                ", \"currWIRefNo2\": " + currWIRefNo2 +
                ", \"planTEU\": " + planTEU +
                ", \"planWeight\": " + planWeight +
                ", \"dispatchBits\": " + dispatchBits +
                ", \"ioLastPos\": \"" + ioLastPos + '\"' +
                ", \"ioCheStatus\": " + ioCheStatus +
                ", \"rDTStatus\": \"" + rDTStatus + '\"' +
                ", \"rDTUserID\": \"" + rDTUserID + '\"' +
                ", \"truckSEQ\": " + truckSEQ +
                ", \"statusInfo\": \"" + statusInfo + '\"' +
                ", \"workStatusBits\": " + workStatusBits +
                ", \"crUserID\": \"" + crUserID + '\"' +
                ", \"bdUserID\": \"" + bdUserID + '\"' +
                ", \"ioWorkPos\": \"" + ioWorkPos + '\"' +
                ", \"ioHandleHaz\": \"" + ioHandleHaz + '\"' +
                '}';
    }







//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {return true;}
//        if (obj == null || getClass() != obj.getClass()) {return false;}
//        Che cheo = (Che)obj;
//        return ioShortName == cheo.ioShortName &&
//                ioCheKind == cheo.ioCheKind  &&
//                ioMaxWeight == cheo.ioMaxWeight  &&
//                ioMaxTEU == cheo.ioMaxTEU  &&
//                ioCheOperatingMode == cheo.ioCheOperatingMode  &&
//                allowedLength == cheo.allowedLength   &&
//                poolName == cheo.poolName &&
//                powName == cheo.powName  &&
//                lastWIRefNo== cheo.lastWIRefNo  &&
//                currWIRefNo== cheo.currWIRefNo  &&
//                currWIRefNo2== cheo.currWIRefNo2  &&
//                planTEU == cheo.planTEU  &&
//                planWeight== cheo.planWeight  &&
//                dispatchBits== cheo.dispatchBits  &&
//                ioLastPos == cheo.ioLastPos   &&
//                ioCheStatus== cheo.ioCheStatus  &&
//                rDTStatus == cheo.rDTStatus   &&
//                rDTUserID == cheo.rDTUserID   &&
//                truckSEQ== cheo.truckSEQ  &&
//                statusInfo == cheo.statusInfo   &&
//                workStatusBits== cheo.workStatusBits  &&
//                crUserID == cheo.crUserID   &&
//                bdUserID == cheo.bdUserID  &&
//                ioWorkPos == cheo.ioWorkPos   &&
//                ioHandleHaz == cheo.ioHandleHaz
//                ;
//    }

}
