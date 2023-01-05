package com.ds.flink.meishan.proto;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: BridgeCrane
 * @Description:
 * @author: ds-longju
 * @Date: 2022-11-08 14:23
 * @Version 1.0
 **/
public class BridgeCrane {

    //桥吊名
    private String craneName;

    //当前所在贝位
    private String currBay;

    //当前桥吊状态
    //'F': 登录后处于激活状态且可给指令为Free; 'B': Busy; 'U':登录后Unavailable; '0'(零):退出登录;其他为未登录或timeout; - 对Dispatch指令有关
    //对于室内理货，进入工作点输入界面为'F',工作点登陆成功后为'B'
    private String rdtStatus;

    //作业阶段: 计算不同阶段的剩余工作时长（离散值）
    //1：未派发，2：已派发未动吊具，3：已动吊具未抓箱（未闭锁）4：已抓箱（闭锁）未放箱（开锁），5：已放箱（开锁）未完成，6：已完成
    private int jobStatus;

//    //贝位无可操作装卸箱 空闲
//    private Boolean isFree;

    //司机
    private String userId;

    //捆扎工工号
    private String bDUserID;

    //故障状态 可移动/不可移动
    private Boolean canMove;

    //最多分配集卡个数
    private Integer maxTruck;

    //最少分配集卡个数
    private Integer minTruck;

    //当前使用的虚拟集卡数（指令的CarryCHE为虚拟集卡，并且是装船指令，并且不是解捆箱、
    //舱盖板、销锁箱，大箱计数1，小箱两个计数1）
    private Integer curFakeTruck = 0;

    //当前使用的集卡数（不包含虚拟集卡）
    private Integer curTruck;

    //当前使用的集卡（不包含虚拟集卡）
    private ArrayList<String> curTruckIDs;

    //船舶参考号
    private String vessel;

    //设备类型(0=远控/1=人工)
    private Integer craneType = 1;

    //作业车道
    private Integer laneNo = -1;

    //桥吊是否在后大梁作业(空值为未知)
    private Boolean isOnRearGirder;

    //桥吊在各船舶已完成指令（主键1：船舶参考号、主键2：指令编号、值：完成时间）
    private ConcurrentHashMap<String, ConcurrentHashMap<Integer, Date>> wIsCompleted = new ConcurrentHashMap<>();

    //操作类型

    //桥吊高度
    private Integer craneHeight;

    //是否解捆
    private boolean isUnbunding = false;

    //解捆明细
    private AuxiliaryJobNew auxiliaryJob;

    // 桥吊移动起始位置 add by taoshuai 20210914
    private Integer craneFromPosition = 0;

    // 桥吊移动结束位置 add by taoshuai 20210914
    private Integer craneToPosition = 0;

    //最后执行指令时间
    private Integer lastWITime;

    //前箱指令 如果大箱只有前箱指令
    private Integer preWINo = 0;

    //后箱指令
    private Integer nextWINo = 0;

    //上次执行指令
    private Integer lastWINo;

    //上条指令完成时间
    private Integer lastWIFinishTime;


    // 是iECS可用的          add by taoshuai 20211011
    private boolean isIECSUse = false;

    // 桥吊下所有工作队列，以及工作队列里的指令集集合
    private LinkedHashMap<String, List<Integer>> workQueue2Instructions = new LinkedHashMap<>();

    // 桥吊所在电子围栏ID
    private String curFenceID;

    // 故障
    private FaultInfo faultInfo;

    // 故障
    private Boolean isFault = false;

    // 指令分发模式 '0':MANL-人工指派; '1':PRTT-全场调度; '2':TRUCK-将箱子确认到车上时，集卡才收到指令;
    private String dispatchMode;

    // 相对优先级 0-10，值越大优先级高
    private Integer powRelativePriority;

    // 'S'-单循环, 默认;'D'-双循环
    private String cycleMode;

    // 在装卸船过程中,设置是否允许1车2箱: '0' - off; '1'- 2X20's for Load; '2'-2X20's for Discharge; '3' - for Load & Discharge
    private String truck220;

    // single cycle rate：设定的单循环作业效率
    private Integer production;

    // dual cycle rate：设定的双循环作业效率
    private Integer dualProduction;

    //桥吊下所有工作队列
    private List<String> workQueues;

    //自动换贝预估时间
    //自动舱内舱外预估时间


    // 虚拟
    private Boolean isFake = false;

    //ZL暂落箱数量
    private int zlContainerCount = 0;

    //最多允许上码头面的集卡数
    private int maxDockTruckNum = 4;

    //桥吊是否有可派发指令
    private boolean isAbleDispatch = false;

    //桥吊堆高机指令
    private Map<String, Map<Integer,List<Integer>>> forkLiftLoadWIMap = new HashMap<>();


    public void setCraneName(String craneName) {
        this.craneName = craneName;
    }

    public void setCurrBay(String currBay) {
        this.currBay = currBay;
    }

    public void setRdtStatus(String rdtStatus) {
        this.rdtStatus = rdtStatus;
    }

    public void setJobStatus(int jobStatus) {
        this.jobStatus = jobStatus;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setbDUserID(String bDUserID) {
        this.bDUserID = bDUserID;
    }

    public void setCanMove(Boolean canMove) {
        this.canMove = canMove;
    }

    public void setMaxTruck(Integer maxTruck) {
        this.maxTruck = maxTruck;
    }

    public void setMinTruck(Integer minTruck) {
        this.minTruck = minTruck;
    }

    public void setCurFakeTruck(Integer curFakeTruck) {
        this.curFakeTruck = curFakeTruck;
    }

    public void setCurTruck(Integer curTruck) {
        this.curTruck = curTruck;
    }

    public void setCurTruckIDs(ArrayList<String> curTruckIDs) {
        this.curTruckIDs = curTruckIDs;
    }

    public void setVessel(String vessel) {
        this.vessel = vessel;
    }

    public void setCraneType(Integer craneType) {
        this.craneType = craneType;
    }

    public void setLaneNo(Integer laneNo) {
        this.laneNo = laneNo;
    }

    public void setOnRearGirder(Boolean onRearGirder) {
        isOnRearGirder = onRearGirder;
    }

    public void setwIsCompleted(ConcurrentHashMap<String, ConcurrentHashMap<Integer, Date>> wIsCompleted) {
        this.wIsCompleted = wIsCompleted;
    }

    public void setCraneHeight(Integer craneHeight) {
        this.craneHeight = craneHeight;
    }

    public void setUnbunding(boolean unbunding) {
        isUnbunding = unbunding;
    }

    public void setAuxiliaryJob(AuxiliaryJobNew auxiliaryJob) {
        this.auxiliaryJob = auxiliaryJob;
    }

    public void setCraneFromPosition(Integer craneFromPosition) {
        this.craneFromPosition = craneFromPosition;
    }

    public void setCraneToPosition(Integer craneToPosition) {
        this.craneToPosition = craneToPosition;
    }

    public void setLastWITime(Integer lastWITime) {
        this.lastWITime = lastWITime;
    }

    public void setPreWINo(Integer preWINo) {
        this.preWINo = preWINo;
    }

    public void setNextWINo(Integer nextWINo) {
        this.nextWINo = nextWINo;
    }

    public void setLastWINo(Integer lastWINo) {
        this.lastWINo = lastWINo;
    }

    public void setLastWIFinishTime(Integer lastWIFinishTime) {
        this.lastWIFinishTime = lastWIFinishTime;
    }

    public void setIECSUse(boolean IECSUse) {
        isIECSUse = IECSUse;
    }

    public void setWorkQueue2Instructions(LinkedHashMap<String, List<Integer>> workQueue2Instructions) {
        this.workQueue2Instructions = workQueue2Instructions;
    }

    public void setCurFenceID(String curFenceID) {
        this.curFenceID = curFenceID;
    }

    public void setFaultInfo(FaultInfo faultInfo) {
        this.faultInfo = faultInfo;
    }

    public void setFault(Boolean fault) {
        isFault = fault;
    }

    public void setDispatchMode(String dispatchMode) {
        this.dispatchMode = dispatchMode;
    }

    public void setPowRelativePriority(Integer powRelativePriority) {
        this.powRelativePriority = powRelativePriority;
    }

    public void setCycleMode(String cycleMode) {
        this.cycleMode = cycleMode;
    }

    public void setTruck220(String truck220) {
        this.truck220 = truck220;
    }

    public void setProduction(Integer production) {
        this.production = production;
    }

    public void setDualProduction(Integer dualProduction) {
        this.dualProduction = dualProduction;
    }

    public void setWorkQueues(List<String> workQueues) {
        this.workQueues = workQueues;
    }

    public void setFake(Boolean fake) {
        isFake = fake;
    }

    public void setZlContainerCount(int zlContainerCount) {
        this.zlContainerCount = zlContainerCount;
    }

    public void setMaxDockTruckNum(int maxDockTruckNum) {
        this.maxDockTruckNum = maxDockTruckNum;
    }

    public void setAbleDispatch(boolean ableDispatch) {
        isAbleDispatch = ableDispatch;
    }

    public void setForkLiftLoadWIMap(Map<String, Map<Integer, List<Integer>>> forkLiftLoadWIMap) {
        this.forkLiftLoadWIMap = forkLiftLoadWIMap;
    }

    public String getCraneName() {
        return craneName;
    }

    public String getCurrBay() {
        return currBay;
    }

    public String getRdtStatus() {
        return rdtStatus;
    }

    public int getJobStatus() {
        return jobStatus;
    }

    public String getUserId() {
        return userId;
    }

    public String getbDUserID() {
        return bDUserID;
    }

    public Boolean getCanMove() {
        return canMove;
    }

    public Integer getMaxTruck() {
        return maxTruck;
    }

    public Integer getMinTruck() {
        return minTruck;
    }

    public Integer getCurFakeTruck() {
        return curFakeTruck;
    }

    public Integer getCurTruck() {
        return curTruck;
    }

    public ArrayList<String> getCurTruckIDs() {
        return curTruckIDs;
    }

    public String getVessel() {
        return vessel;
    }

    public Integer getCraneType() {
        return craneType;
    }

    public Integer getLaneNo() {
        return laneNo;
    }

    public Boolean getOnRearGirder() {
        return isOnRearGirder;
    }

    public ConcurrentHashMap<String, ConcurrentHashMap<Integer, Date>> getwIsCompleted() {
        return wIsCompleted;
    }

    public Integer getCraneHeight() {
        return craneHeight;
    }

    public boolean isUnbunding() {
        return isUnbunding;
    }

    public AuxiliaryJobNew getAuxiliaryJob() {
        return auxiliaryJob;
    }

    public Integer getCraneFromPosition() {
        return craneFromPosition;
    }

    public Integer getCraneToPosition() {
        return craneToPosition;
    }

    public Integer getLastWITime() {
        return lastWITime;
    }

    public Integer getPreWINo() {
        return preWINo;
    }

    public Integer getNextWINo() {
        return nextWINo;
    }

    public Integer getLastWINo() {
        return lastWINo;
    }

    public Integer getLastWIFinishTime() {
        return lastWIFinishTime;
    }

    public boolean isIECSUse() {
        return isIECSUse;
    }

    public LinkedHashMap<String, List<Integer>> getWorkQueue2Instructions() {
        return workQueue2Instructions;
    }

    public String getCurFenceID() {
        return curFenceID;
    }

    public FaultInfo getFaultInfo() {
        return faultInfo;
    }

    public Boolean getFault() {
        return isFault;
    }

    public String getDispatchMode() {
        return dispatchMode;
    }

    public Integer getPowRelativePriority() {
        return powRelativePriority;
    }

    public String getCycleMode() {
        return cycleMode;
    }

    public String getTruck220() {
        return truck220;
    }

    public Integer getProduction() {
        return production;
    }

    public Integer getDualProduction() {
        return dualProduction;
    }

    public List<String> getWorkQueues() {
        return workQueues;
    }

    public Boolean getFake() {
        return isFake;
    }

    public int getZlContainerCount() {
        return zlContainerCount;
    }

    public int getMaxDockTruckNum() {
        return maxDockTruckNum;
    }

    public boolean isAbleDispatch() {
        return isAbleDispatch;
    }

    public Map<String, Map<Integer, List<Integer>>> getForkLiftLoadWIMap() {
        return forkLiftLoadWIMap;
    }

    @Override
    public String toString() {
        return "BridgeCrane{" +
                "craneName='" + craneName + '\'' +
                ", currBay='" + currBay + '\'' +
                ", rdtStatus='" + rdtStatus + '\'' +
                ", jobStatus=" + jobStatus +
                ", userId='" + userId + '\'' +
                ", bDUserID='" + bDUserID + '\'' +
                ", canMove=" + canMove +
                ", maxTruck=" + maxTruck +
                ", minTruck=" + minTruck +
                ", curFakeTruck=" + curFakeTruck +
                ", curTruck=" + curTruck +
                ", curTruckIDs=" + curTruckIDs +
                ", vessel='" + vessel + '\'' +
                ", craneType=" + craneType +
                ", laneNo=" + laneNo +
                ", isOnRearGirder=" + isOnRearGirder +
                ", wIsCompleted=" + wIsCompleted +
                ", craneHeight=" + craneHeight +
                ", isUnbunding=" + isUnbunding +
                ", auxiliaryJob=" + auxiliaryJob +
                ", craneFromPosition=" + craneFromPosition +
                ", craneToPosition=" + craneToPosition +
                ", lastWITime=" + lastWITime +
                ", preWINo=" + preWINo +
                ", nextWINo=" + nextWINo +
                ", lastWINo=" + lastWINo +
                ", lastWIFinishTime=" + lastWIFinishTime +
                ", isIECSUse=" + isIECSUse +
                ", workQueue2Instructions=" + workQueue2Instructions +
                ", curFenceID='" + curFenceID + '\'' +
                ", faultInfo=" + faultInfo +
                ", isFault=" + isFault +
                ", dispatchMode='" + dispatchMode + '\'' +
                ", powRelativePriority=" + powRelativePriority +
                ", cycleMode='" + cycleMode + '\'' +
                ", truck220='" + truck220 + '\'' +
                ", production=" + production +
                ", dualProduction=" + dualProduction +
                ", workQueues=" + workQueues +
                ", isFake=" + isFake +
                ", zlContainerCount=" + zlContainerCount +
                ", maxDockTruckNum=" + maxDockTruckNum +
                ", isAbleDispatch=" + isAbleDispatch +
                ", forkLiftLoadWIMap=" + forkLiftLoadWIMap +
                '}';
    }
}
