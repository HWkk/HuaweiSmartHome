package utils;

import data.Data;
import specification.InnerEvent;
import specification.OuterEvent;
import stateautomaton.attribute.Attribute;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InputUtils {

    /**
     * 将Specification文件中的event与状态对应起来
     * 文件格式：
     * 第一行是OuterEvent，event之间用逗号分隔
     * 第二行是InnerEvent，event之间用逗号分隔
     * @param fileName 文件名
     */
    public static void readSpecificationFile(String fileName) {
        FileInputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = new FileInputStream(fileName);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String s = null;
            int line = 1;
            while((s = bufferedReader.readLine()) != null) {
                if(line == 1) {
                    String[] strs = s.split("\\s*,\\s*");
                    for (String str : strs)
                        OuterEvent.addEvent(str.trim());
                    line++;
                } else {
                    InnerEvent.addEvent(s);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(inputStream != null)
                    inputStream.close();
                if(bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取log文件，构建图
     * 文件格式：
     * Event A1,A2,A3……Ad
     * @param fileName 文件名
     */
    public static List<Data> readLogFile(String fileName) {
        List<Data> res = new ArrayList<>();
        FileInputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            inputStream = new FileInputStream(fileName);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String s = null;
            while((s = bufferedReader.readLine()) != null) {
                String[] strs = s.split("\\s+");
                Data data = new Data(strs[0], strToAttribute(strs[1]));
                res.add(data);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(inputStream != null)
                    inputStream.close();
                if(bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public static Attribute strToAttribute(String s) {
        s = s.trim();
        while(!Character.isDigit(s.charAt(0)))
            s = s.substring(1);
        while(!Character.isDigit(s.charAt(s.length() - 1)))
            s = s.substring(0, s.length() - 1);

        String[] strs = s.split("\\s*,\\s*");
        Constants.ATTRIBUTE_DIMENSION = strs.length;
        Attribute attribute = new Attribute();
        for(int i = 0; i < strs.length; i++)
            attribute.setAttribute(i, Double.parseDouble(strs[i]));
        return attribute;
    }
}
