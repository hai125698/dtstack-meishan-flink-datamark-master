package com.ds.flink.meishan.proto;


/**
 * @ClassName: Position
 * @Description: 点位信息
 * @author: ds-longju
 * @Date: 2022-11-08 14:28
 * @Version 1.0
 **/
public class Position {

    //位置属性：位置类型代码(V:船; Y:场地; C:未进场已预约的直装箱 T : 集卡 O : 已出门)
    private String mQual;

    //堆场，则为堆场Block(1A,1B...)
    //船舶则为船舶航次参考号（APLNB1...）
    //特殊的（外集卡号等）
    //卸船 TC 直卸 toPoistion
    //装船 TOCOME/TC 直装 fromPostion
    private String mLocation;

    //也就是界面的Bay位，用1,2,3,...,79（跟界面所显示的需要*2-1，即5贝对应界面是9贝）
    //当为特殊堆场（虚拟箱区等），如：Heap Areas，则为0
    private Integer mRow2 = -1;

    //1,2,3,4,5,6 当为特殊堆场，如：Heap Areas，则为0;
    private Integer mColumn = -1;

    //1,2,3,4,5 当为特殊堆场，如：Heap Areas，则为0
    //船舶有1...,或72...
    private Integer mTier = -1;



}
