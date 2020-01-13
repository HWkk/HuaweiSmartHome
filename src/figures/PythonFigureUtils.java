package figures;

import homeassistant.AttributesName;
import homeassistant.Caller;
import homeassistant.EntityName;
import stateautomaton.attribute.Attribute;
import utils.Constants;
import utils.DateUtils;

import java.io.*;
import java.util.*;

public class PythonFigureUtils {

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

    private static void drawLineChartSingle(List<TimeData> data, String entityId, String curFile, String saveFile) {
        int dimension = data.get(0).getAttribute().getDimension();
        List<String> attrNames = AttributesName.getAttributes(entityId);

        String[] args = new String[8];
        args[0] = "python";
        args[1] = "/Users/kk/Repositories/HuaweiSmartHome/src/figures/linechart_single.py";
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
            args[7] = saveFile + "_" + attrNames.get(i) + ".pdf";
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

    public static void drawLineChartTogether(List<String> legends, List<List<TimeData>> data, String entityId, String saveFile) {
        List<String> attrNames = AttributesName.getAttributes(entityId);
        int dimension = attrNames.size();

        String[] args = new String[8];
        args[0] = "python";
        args[1] = "/Users/kk/Repositories/HuaweiSmartHome/src/figures/linechart_together.py";
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
            args[7] = saveFile + "_" + attrNames.get(i) + ".pdf";

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

    public static void main(String[] args) {
//        String deviceName = EntityName.AIR_HUMIDIFIER_NAME;
        String deviceName = EntityName.AIR_PURIFIER_NAME;
        int getAttrTimeGap = 5;
        int callServiceTimeGap = 90;
        int getAttrAfterCallingTimeGap = 60;
        Caller.init(deviceName, getAttrTimeGap, callServiceTimeGap, getAttrAfterCallingTimeGap);
        String dir = Constants.GRAPH_DIR + deviceName + "/";

//        getSingleFigure(dir + "normal", deviceName, dir + "pythonFigures/normal");
//        getSingleFigure(dir + "block_in", deviceName, dir + "pythonFigures/block_in");
//        getSingleFigure(dir + "sensor_broke", deviceName, dir + "pythonFigures/sensor_broke");
//        getSingleFigure(dir + "filter_broke", deviceName, dir + "pythonFigures/filter_broke");

        List<String> files = new ArrayList<>();
        files.add(dir + "normal");
        files.add(dir + "block_in");
        files.add(dir + "sensor_broke");
        files.add(dir + "filter_broke");
        getTogetherFigure(files, deviceName, dir + "pythonFigures/together");
    }
}
