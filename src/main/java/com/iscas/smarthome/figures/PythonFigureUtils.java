package com.iscas.smarthome.figures;

import com.iscas.smarthome.data.IQR;
import com.iscas.smarthome.homeassistant.AttributesName;
import com.iscas.smarthome.homeassistant.Caller;
import com.iscas.smarthome.homeassistant.EntityName;
import com.iscas.smarthome.stateautomaton.attribute.Attribute;
import com.iscas.smarthome.utils.Constants;
import com.iscas.smarthome.utils.CurveUtils;
import com.iscas.smarthome.utils.DateUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PythonFigureUtils {

    /**
     * 读取日志信息，生成TimeData格式的数据（用于画图）
     */
    private static List<TimeData> readLogFile(String fileName) {
        List<TimeData> res = new ArrayList<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(fileName)));
            String s = null;
            String startTime = null;

            while ((s = reader.readLine()) != null) {
                String curTime = s.split("\\s")[1].trim();
                if(startTime == null) startTime = curTime;
                int time = DateUtils.twoTimeDiff(startTime, curTime);

                String attr = s.substring(s.lastIndexOf("["));
                String[] attrs = attr.substring(1, attr.length() - 1).split(",");
                double[] attributes = new double[attrs.length];
                for(int i = 0; i < attrs.length; i++)
                    attributes[i] = Double.parseDouble(attrs[i].trim());
                res.add(new TimeData(Integer.toString(time), new Attribute(attributes)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * 画单条线
     */
    private static void drawLineChartSingle(List<TimeData> data, String entityId, String curFile, String saveFile) {
        int dimension = data.get(0).getAttribute().getDimension();
        List<String> attrNames = AttributesName.getAttributes(entityId);

        String[] args = new String[8];
        args[0] = "python";
//        args[1] = "/Users/kk/Repositories/HuaweiSmartHome/src/main/java/com/iscas/smarthome/figures/linechart_single.py";
        args[1] = Constants.DRAW_FIGURE_LOCATION + "linechart_single.py";
        args[2] = "time(s)";

        String[] times = new String[data.size()];
        for(int i = 0; i < data.size(); i++)
            times[i] = data.get(i).getTime();
        args[3] = Arrays.toString(times);
        args[3] = args[3].substring(1, args[3].length() - 1);

        for(int i = 0; i < dimension; i++) {
            args[4] = attrNames.get(i);
            args[5] = curFile;
            double[] values = new double[data.size()];
            for(int j = 0; j < data.size(); j++) {
                values[j] = data.get(j).getAttribute().getAttribute(i);
            }
            args[6] = Arrays.toString(values);
            args[6] = args[6].substring(1, args[6].length() - 1);
            args[7] = saveFile + "_" + attrNames.get(i) + ".png";
            Process proc = null;
            try {
                proc = Runtime.getRuntime().exec(args);
                BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
                in.close();
                proc.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 画多条线
     */
    public static void drawLineChartTogether(List<String> legends, List<List<TimeData>> data, String entityId, String saveFile) {
        List<String> attrNames = AttributesName.getAttributes(entityId);
        int dimension = attrNames.size();

        String[] args = new String[8];
        args[0] = "python";
//        args[1] = "/Users/kk/Repositories/HuaweiSmartHome/src/main/java/com/iscas/smarthome/figures/linechart_two.py";
        args[1] = Constants.DRAW_FIGURE_LOCATION + "linechart_two.py";
        args[2] = "time(s)";

        List<List<String>> times = new ArrayList<>();
        for(int i = 0; i < legends.size(); i++) {
            List<String> t = new ArrayList<>();
            for(TimeData td : data.get(i))
                t.add(td.getTime());
            times.add(t);
        }
        args[3] = times.toString();
        args[4] = legends.toString();
        args[4] = args[4].substring(1, args[4].length() - 1);

        for(int i = 0; i < dimension; i++) {
            args[5] = attrNames.get(i);
            List<List<Double>> values = new ArrayList<>();
            for(List<TimeData> d : data) {
                List<Double> v = new ArrayList<>();
                for(TimeData td : d)
                    v.add(td.getAttribute().getAttribute(i));
                values.add(v);
            }
            args[6] = values.toString();
            args[7] = saveFile + "_" + attrNames.get(i) + ".png";

            Process proc = null;
            try {
                proc = Runtime.getRuntime().exec(args);
                BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    System.out.println(line);
                }
                in.close();
                proc.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void getSingleFigure(String logFileName, String entityID, String saveFileName) {
        List<TimeData> data = readLogFile(logFileName);
        String curFile = logFileName.substring(logFileName.lastIndexOf("/") + 1);
        drawLineChartSingle(data, entityID, curFile, saveFileName);
    }

    public static void getTogetherFigure(List<String> logFileNames, String entityId, String saveFileName) {
        List<String> legends = new ArrayList<>();
        List<List<TimeData>> data = new ArrayList<>();

        for(String file : logFileNames) {
            String legend = file.substring(file.lastIndexOf("/") + 1);
            legends.add(legend);
            data.add(readLogFile(file));
        }
        drawLineChartTogether(legends, data, entityId, saveFileName);
    }


    public static List<Double> getFileData(String fileName, int attrIndex) {
        List<TimeData> timeData = readLogFile(fileName);
        List<Double> attributes = new ArrayList<>();
        for(TimeData d : timeData) {
            attributes.add(d.getAttribute().getAttribute(attrIndex));
        }
        return attributes;
    }

    /**
     * 计算统计值
     */
    public static void getAllStatistics(List<Double> data) {
        IQR slopeIQR = CurveUtils.calSlopeIQR(data), valueIQR = CurveUtils.calValueIQR(data);
        double valueAvg = CurveUtils.calValueAvg(data), slopeAvg = CurveUtils.calSlopeAvg(data);
        double valueMedian = CurveUtils.calValueMedian(data), slopeMedian = CurveUtils.calSlopeMedian(data);
        double valueDelta = CurveUtils.calValueDelta(data), slopeDelta = CurveUtils.calSlopeDelta(data);
        System.out.println("Value:");
        System.out.println("Avg: " + valueAvg);
        System.out.println("Delta: " + valueDelta);
        System.out.println("Median: " + valueMedian);
        System.out.println("IQR: " + valueIQR.getIQR() + ", Q1: " + valueIQR.getQ1() + ", Q3: " + valueIQR.getQ3());
        System.out.println("LowBoundary: " + valueIQR.getLowerBoundary() + " UpBoundary: " + valueIQR.getUpperBoundary());

        System.out.println("Slope:");
        System.out.println("Avg: " + slopeAvg);
        System.out.println("Delta: " + slopeDelta);
        System.out.println("Median: " + slopeMedian);
        System.out.println("IQR: " + slopeIQR.getIQR() + ", Q1: " + slopeIQR.getQ1() + ", Q3: " + slopeIQR.getQ3());
        System.out.println("LowBoundary: " + slopeIQR.getLowerBoundary() + " UpBoundary: " + slopeIQR.getUpperBoundary());
    }

    /**
     * 计算两条曲线的相似度
     */
    public static void getSimilarity(List<Double> data1, List<Double> data2) {
        IQR slopeIQR1 = CurveUtils.calSlopeIQR(data1), valueIQR1 = CurveUtils.calValueIQR(data1);
        double valueAvg1 = CurveUtils.calValueAvg(data1), slopeAvg1 = CurveUtils.calSlopeAvg(data1);
        double valueMedian1 = CurveUtils.calValueMedian(data1), slopeMedian1 = CurveUtils.calSlopeMedian(data1);

        IQR slopeIQR2 = CurveUtils.calSlopeIQR(data2), valueIQR2 = CurveUtils.calValueIQR(data2);
        double valueAvg2 = CurveUtils.calValueAvg(data2), slopeAvg2 = CurveUtils.calSlopeAvg(data2);
        double valueMedian2 = CurveUtils.calValueMedian(data2), slopeMedian2 = CurveUtils.calSlopeMedian(data2);

        double valueS1 = getSimilarity(valueIQR1.getQ1(), valueIQR1.getQ3(), valueMedian1, valueAvg2);
        double valueS2 = getSimilarity(valueIQR2.getQ1(), valueIQR2.getQ3(), valueMedian2, valueAvg1);
        System.out.println("Value Similarity: " + valueS1 + " " + valueS2);

        double slopeS1 = getSimilarity(slopeIQR1.getQ1(), slopeIQR1.getQ3(), slopeMedian1, slopeAvg2);
        double slopeS2 = getSimilarity(slopeIQR2.getQ1(), slopeIQR2.getQ3(), slopeMedian2, slopeAvg1);
        System.out.println("Slope Similarity: " + slopeS1 + " " + slopeS2);

//        double valueS1 = getSimilarity(valueIQR1.getQ1(), valueIQR1.getQ3(), valueMedian1, valueMedian2);
//        double valueS2 = getSimilarity(valueIQR2.getQ1(), valueIQR2.getQ3(), valueMedian2, valueMedian1);
//        System.out.println("Value Similarity: " + valueS1 + " " + valueS2);
//
//        double slopeS1 = getSimilarity(slopeIQR1.getQ1(), slopeIQR1.getQ3(), slopeMedian1, slopeMedian2);
//        double slopeS2 = getSimilarity(slopeIQR2.getQ1(), slopeIQR2.getQ3(), slopeMedian2, slopeMedian1);
//        System.out.println("Slope Similarity: " + slopeS1 + " " + slopeS2);
    }

    public static double getSimilarity(double q1, double q3, double median, double statistics) {
        if(q3 == q1) return statistics == (q1 + q3) / 2 ? 1.0 : 0.0;
        if(statistics >= q1 && statistics <= q3)
            return 1.0 - Math.abs(statistics - (q1 + q3) / 2) / (q3 - q1);
        if(statistics < q1 && statistics >= q1 - 1.5 * (q3 - q1))
            return 0.5 - (q1 - statistics) / (3.0 * (q3 - q1));
        if(statistics > q3 && statistics <= q3 + 1.5 * (q3 - q1))
            return 0.5 - (statistics - q3) / (3.0 * (q3 - q1));
        return 0.0;
    }

    /**
     * 测试用
     * @param args
     */
    public static void main(String[] args) {
//        String deviceName = EntityName.AIR_HUMIDIFIER_NAME;
        String deviceName = EntityName.AIR_PURIFIER_NAME;
        Caller.init(deviceName);
        AttributesName.init();
        String dir = Constants.GRAPH_DIR + deviceName + "/";

//        getSingleFigure(dir + "normal", deviceName, dir + "pythonFigures/normal");
//        getSingleFigure(dir + "block_in", deviceName, dir + "pythonFigures/block_in");
//        getSingleFigure(dir + "sensor_broke", deviceName, dir + "pythonFigures/sensor_broke");
//        getSingleFigure(dir + "filter_broke", deviceName, dir + "pythonFigures/filter_broke");

        List<String> files = new ArrayList<>();
        files.add(dir + "normal");
//        files.add(dir + "block_in");
//        files.add(dir + "sensor_broke");
//        files.add(dir + "filter_broke");
        files.add(dir + "normal1");
        getTogetherFigure(files, deviceName, dir + "pythonFigures/together");

//        getAllStatistics(getFileData(dir + "sensor_broke", 2));
//        getSimilarity(getFileData(dir + "normal", 2), getFileData(dir + "sensor_broke", 2));
    }
}
