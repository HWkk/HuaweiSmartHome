package com.iscas.smarthome.stateautomaton.state;

import com.iscas.smarthome.homeassistant.AttrAndHardRelation;
import com.iscas.smarthome.homeassistant.AttributesName;
import com.iscas.smarthome.homeassistant.ServiceName;
import com.iscas.smarthome.stateautomaton.attribute.Attribute;
import com.iscas.smarthome.stateautomaton.graph.Edge;
import com.iscas.smarthome.stateautomaton.graph.InnerGraph;
import com.iscas.smarthome.utils.Constants;
import com.iscas.smarthome.utils.CurveUtils;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.*;

/**
 * 外部状态表示
 */
public class OuterState implements State, Externalizable {

    //邻接图形式记录图信息
    private HashMap<OuterState, Edge> outNeighbors;

    //概率图信息使用
    private HashMap<OuterState, Integer> inNeighborsCount;
    private int totalInCount;

    //内部状态转换
    private InnerGraph innerGraph;
    private String name;

    //记录当前状态的操作调用情况，用于判断测试过程是否收敛
    private boolean[] serviceCalled;

    public OuterState(String name, String deviceName) {
        outNeighbors = new HashMap<>();
        inNeighborsCount = new HashMap<>();
        innerGraph = new InnerGraph();
        this.name = name;
        totalInCount = 0;
        serviceCalled = new boolean[ServiceName.getServices(deviceName).size()];
    }

    public OuterState() {
        outNeighbors = null;
        inNeighborsCount = null;
        innerGraph = null;
        name = null;
        totalInCount = 0;
        serviceCalled = null;
    }

    public HashMap<OuterState, Edge> getOutNeighbors() {
        return outNeighbors;
    }

    public HashMap<OuterState, Integer> getInNeighborsCount() {
        return inNeighborsCount;
    }

    public InnerGraph getInnerGraph() {
        return innerGraph;
    }

    public String getName() {
        return name;
    }

    public int getTotalInCount() {
        return totalInCount;
    }

    /**
     * 添加新的邻居外部状态
     */
    public void addOutNeighbor(Edge e) {
        if(!outNeighbors.containsKey(e.getTarget()))
            outNeighbors.put(e.getTarget(), e);
    }

    /**
     * 统计信息，用于概率计算
     */
    public void addInNeighbor(Edge e) {
        inNeighborsCount.put(e.getSource(), inNeighborsCount.getOrDefault(e.getSource(), 0) + 1);
        totalInCount++;
    }

    /**
     * 统计信息，用于概率计算
     */
    public double getInNeighborPercent(OuterState state) {
        return (double)inNeighborsCount.getOrDefault(state, 0) / totalInCount;
    }

    /**
     * 添加内部状态
     */
    public void addInnerState(InnerState state, String deviceName) {
        innerGraph.addInnerStateByRelativeTime(state);
//        innerGraph.addInnerStateByAbsoluteTime(state);
    }

//    public boolean checkNormal(InnerState state, String deviceName) {
//        if(innerGraph.getStateSize() == 0) return true;
//        Attribute avg = innerGraph.getAvg();
//
//        boolean normal = true;
//        for(int i = 0; i < avg.getDimension(); i++) {
//            double curAttr = state.getAttribute().getAttribute(i);
//            double avgAttr = avg.getAttribute(i);
//            if(Math.abs(curAttr - avgAttr) / avgAttr > Constants.ABNORMAL_THRESHOLD) {
//                normal = false;
//                String attrName = AttributesName.getAttributes(deviceName).get(i);
//                StringBuilder sb = new StringBuilder(attrName + " is too ");
//                sb.append(curAttr > avgAttr ? "high!" : "low!");
//                sb.append(" Most possible reason is: " + findAbnormalReason(i, state));
//                System.out.println(sb.toString());
//            }
//        }
//        return normal;
//    }

//    public int isCurveSimilar(List<Attribute> data) {
//        if(innerGraph.getStateSize() == 0) return -1;
//        List<CurveStatistics> normalStatistics = innerGraph.getCurveStatistics();
//
//        for(int i = 0; i < normalStatistics.size(); i++) {
//            List<Double> curAttr = new ArrayList<>();
//            for(Attribute attribute : data)
//                curAttr.add(attribute.getAttribute(i));
//            double valueMedian = CurveUtils.calValueMedian(curAttr), slopeMedian = CurveUtils.calSlopeMedian(curAttr);
//            if(normalStatistics.get(i).getSlopeIQR().isOutlier(slopeMedian) || normalStatistics.get(i).getValueIQR().isOutlier(valueMedian)) {
//                return i;
//            }
//        }
//        return -1;
//    }

    /**
     * 判断曲线是否相似
     */
    public int isCurveSimilar(List<Attribute> data) {
        if(innerGraph.getStateSize() == 0 || data.size() == 0) return -1;
        List<Attribute> normalAttributes = new ArrayList<>();
        for(int i = 0; i < innerGraph.getStateSize(); i++)
            normalAttributes.add(innerGraph.getState(i).getAttribute());

        /**
         * 每个属性都进行检测
         */
        for(int i = 0; i < data.get(0).getDimension(); i++) {
            List<Double> curAttr = new ArrayList<>();
            for(Attribute attribute : data)
                curAttr.add(attribute.getAttribute(i));

            List<Double> normalAttr = new ArrayList<>();
            for(Attribute attribute : normalAttributes)
                normalAttr.add(attribute.getAttribute(i));

            //得到相似度
            double valueSim = CurveUtils.getValueSimilarity(curAttr, normalAttr);
            double slopeSim = CurveUtils.getSlopeSimilarity(curAttr, normalAttr);
            if(valueSim < Constants.VALUE_THRESHOLD || slopeSim < Constants.SLOPE_THRESHOLD)
                return i;
        }
        return -1;
    }

    /**
     * 异常检测，先用模式边界判断，再判断曲线相似度
     */
    public int checkNormal(List<Attribute> data) {
        int stateBorderRes = overStateBorder(data.get(data.size() - 1));
        if(stateBorderRes != -1) return stateBorderRes;
        int curveSimilarRes = isCurveSimilar(data);
        if(curveSimilarRes != -1) return curveSimilarRes;
        return -1;
    }

    /**
     * 判断是否超出模式边界
     */
    public int overStateBorder(Attribute data) {
        Attribute[] border = getStateBorder();
        int dimension = data.getDimension();
        for(int i = 0; i < dimension; i++) {
            if(data.getAttribute(i) > Constants.UP_BORDER_ABNORMAL_THRESHOLD * border[1].getAttribute(i))
                return i;
            if(data.getAttribute(i) < Constants.DOWN_BORDER_ABNORMAL_THRESHOLD * border[0].getAttribute(i))
                return i;
        }
        return -1;
    }

    /**
     * 计算模式边界
     */
    public Attribute[] getStateBorder() {
        int dimension = innerGraph.getState(0).getAttribute().getDimension();
        Attribute max = new Attribute(dimension), min = new Attribute(dimension);
        for(int i = 0; i < dimension; i++) {
            max.setAttribute(i, Double.MIN_VALUE);
            min.setAttribute(i, Double.MAX_VALUE);
        }

        for(int i = 0; i < innerGraph.getStateSize(); i++) {
            Attribute cur = innerGraph.getState(i).getAttribute();
            for(int j = 0; j < dimension; j++) {
                double value = cur.getAttribute(j);
                if(value > max.getAttribute(j))
                    max.setAttribute(j, value);
                if(value < min.getAttribute(j))
                    min.setAttribute(j, value);
            }
        }
        return new Attribute[]{min, max};
    }

    /**
     * 回溯过程
     */
    public String backtrace(int attrIndex, InnerState state) {
        InnerState pre = null, curInnerState = state;
        OuterState curOuter = this;
        int index = state.getTime() / curOuter.getInnerGraph().getStateSize();
        if(index >= curOuter.getInnerGraph().getStateSize())
            index = curOuter.getInnerGraph().getStateSize() - 1;
        index--;
        double maxInfluence = Double.MIN_VALUE;
        String event = "工作时间太长或者环境突变";

        for(int i = 0; i < Constants.BACK_COUNT; i++) {
            String curEvent = "工作时间太长或者环境突变";
            pre = curInnerState;

            if(index < 0) {
                /**
                 * 到达内部状态链表的头部，则对当前外部状态进行转换
                 */
                OuterState newOuter = curOuter.findMaxPreOuter();
                index = newOuter.findMaxInner();
                curInnerState = newOuter.getInnerGraph().getState(index--);
                curEvent = "操作 " + newOuter.getOutNeighbors().get(curOuter).getEvent() + "异常";
                curOuter = newOuter;
            } else {
                curInnerState = curOuter.getInnerGraph().getState(index--);
                curEvent = "工作时间太长或者环境突变";
            }

            /**
             * 计算影响程度最大的
             */
            double influence = Math.abs(pre.getAttribute().getAttribute(attrIndex) - curInnerState.getAttribute().getAttribute(attrIndex));
            if (influence > maxInfluence) {
                maxInfluence = influence;
                event = curEvent;
            }
        }
        return event;
    }

    /**
     * 异常定位，首先根据对应关系查表，然后回溯
     */
    public String findAbnormalReason(int attrIndex, InnerState state, String deviceName) {
        String attrName = AttributesName.getAttributes(deviceName).get(attrIndex);
        if(AttrAndHardRelation.containsAttribute(attrName))
            return AttrAndHardRelation.getHardwareName(attrName) + "出现异常";
        return backtrace(attrIndex, state);
    }

    /**
     * 根据概率信息找到转换的外部状态
     */
    public OuterState findMaxPreOuter() {
        OuterState res = null;
        int maxFrequency = 0;
        for(Map.Entry<OuterState, Integer> entry : inNeighborsCount.entrySet()) {
            if(entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                res = entry.getKey();
            }
        }
        return res;
    }

    /**
     * 根据概率信息开始回溯的内部状态
     */
    public int findMaxInner() {
        int res = 0;
        double maxPercentage = 0.0;
        for(int i = 0; i < innerGraph.getStateSize(); i++) {
            double p = innerGraph.getLeavePercent(i);
            if(p > maxPercentage) {
                maxPercentage = p;
                res = i;
            }
        }
        return res;
    }

    /**
     * 根据index找到对应的内部状态
     */
    public InnerState findCorrespondInner(int i) {
        return innerGraph.getState(i);
    }

    /**
     * 记录跃迁，用于后续概率计算
     */
    public void leaveState(int time) {
        innerGraph.addLeaveCount(time);
    }

    /**
     * 判断操作是否都调用完了
     */
    public boolean serviceAllCalled() {
        for(boolean b : serviceCalled)
            if(!b) return false;
        return true;
    }

    /**
     * 获取下一个未调用的操作
     */
    public int getUncalled() {
        int index = 0;
        Random rand = new Random();
        while(serviceCalled[index]) {
            index = rand.nextInt(serviceCalled.length);
        }
        serviceCalled[index] = true;
        return index;
    }

    public int getServiceSize() {
        return serviceCalled.length;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OuterState)) return false;
        OuterState state = (OuterState) o;
        return name != null ? name.equals(state.name) : state.name == null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name: " + name + "\n");
        sb.append("neighbors: ");
        for(OuterState neighbor : outNeighbors.keySet())
            sb.append(neighbor.getName() + "\t");
        sb.append("\n");
        sb.append("innerStates: ");
        sb.append(innerGraph.toString() + "\n");
        return sb.toString();
    }

    /**
     * 序列化顺序
     */
    @Override
    public void writeExternal(final ObjectOutput o) throws IOException {
        o.writeUTF(name);
        o.writeInt(totalInCount);
        o.writeObject(innerGraph);
        o.writeObject(serviceCalled);
        o.writeObject(inNeighborsCount);
        o.writeObject(outNeighbors);
    }

    /**
     * 反序列化顺序
     */
    @SuppressWarnings("unchecked")
    @Override
    public void readExternal(final ObjectInput o) throws IOException, ClassNotFoundException {
        name = o.readUTF();
        totalInCount = o.readInt();
        innerGraph = (InnerGraph)o.readObject();
        serviceCalled = (boolean[])o.readObject();
        inNeighborsCount = (HashMap<OuterState, Integer>)o.readObject();
        outNeighbors = (HashMap<OuterState, Edge>)o.readObject();
    }
}
