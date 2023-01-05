package com.ds.flink.meishan.proto;

/**
 * @ClassName: CRData
 * @Description: 桥吊设备信息
 * @author: ds-longju
 * @Date: 2022-11-10 20:28
 * @Version 1.0
 **/
public class CRData {

    // 桥吊名称
    private String name;
    // 吊具尺寸 20
    private boolean spreaderFT20;
    // 吊具尺寸 40
    private boolean spreaderFT40;
    // 吊具尺寸 45
    private boolean spreaderFT45;
    // 吊具尺寸
    private boolean spreaderFT2020;
    // 吊具着箱
    private boolean spreaderLand;
    // 吊具闭锁
    private boolean spreaderLock;
    // 吊具开锁
    private boolean spreaderUnlock;
    // 起升位置Z，坐标原点需指定
    private int zCraneState_MHPosition;
    // 小车位置Y，坐标原点需指定
    private int zCraneState_MTPosition;
    // 俯仰位置(单位度，0-90)
    private int zCraneState_BoomPosition;
    // 打车位置坐标X，坐标远点需指定
    private int zLocation_X;
    // 俯仰上升
    private boolean boomUp;
    // 俯仰下降
    private boolean boomDown;
    // 俯仰速度
    private int boomSpd;
    // 起升上升
    private boolean hoistUp;
    // 起升下降
    private boolean hoistDown;
    // 起升速度
    private int hoistSpd;
    // 大车左行
    private boolean gantryLeft;
    // 大车右行
    private boolean gantryRight;
    // 大车速度
    private int gantrySpd;
    // 小车前进
    private boolean trolleyFwd;
    // 小车后退
    private boolean trolleyBwd;
    // 小车速度
    private int trolleySpd;
    // 舱盖板模式
    private boolean hatchCoverMode;
    // 模式  1远程   西门子系统：true为远程
    private boolean mode;
    // 安川系统：true为远程模式
    private boolean remoteMode;
    // 西门子系统：true为远程模式
    private boolean localMode;

    public String getName() {
        return name;
    }

    public boolean isSpreaderFT20() {
        return spreaderFT20;
    }

    public boolean isSpreaderFT40() {
        return spreaderFT40;
    }

    public boolean isSpreaderFT45() {
        return spreaderFT45;
    }

    public boolean isSpreaderFT2020() {
        return spreaderFT2020;
    }

    public boolean isSpreaderLand() {
        return spreaderLand;
    }

    public boolean isSpreaderLock() {
        return spreaderLock;
    }

    public boolean isSpreaderUnlock() {
        return spreaderUnlock;
    }

    public int getzCraneState_MHPosition() {
        return zCraneState_MHPosition;
    }

    public int getzCraneState_MTPosition() {
        return zCraneState_MTPosition;
    }

    public int getzCraneState_BoomPosition() {
        return zCraneState_BoomPosition;
    }

    public int getzLocation_X() {
        return zLocation_X;
    }

    public boolean isBoomUp() {
        return boomUp;
    }

    public boolean isBoomDown() {
        return boomDown;
    }

    public int getBoomSpd() {
        return boomSpd;
    }

    public boolean isHoistUp() {
        return hoistUp;
    }

    public boolean isHoistDown() {
        return hoistDown;
    }

    public int getHoistSpd() {
        return hoistSpd;
    }

    public boolean isGantryLeft() {
        return gantryLeft;
    }

    public boolean isGantryRight() {
        return gantryRight;
    }

    public int getGantrySpd() {
        return gantrySpd;
    }

    public boolean isTrolleyFwd() {
        return trolleyFwd;
    }

    public boolean isTrolleyBwd() {
        return trolleyBwd;
    }

    public int getTrolleySpd() {
        return trolleySpd;
    }

    public boolean isHatchCoverMode() {
        return hatchCoverMode;
    }

    public boolean isMode() {
        return mode;
    }

    public boolean isRemoteMode() {
        return remoteMode;
    }

    public boolean isLocalMode() {
        return localMode;
    }


}
