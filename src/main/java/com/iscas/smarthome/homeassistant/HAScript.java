package com.iscas.smarthome.homeassistant;

import com.iscas.smarthome.data.Data;
import com.iscas.smarthome.utils.InputUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class HAScript {

    private static int ATTR_ARGS_PREFIX = 4;
    private static Logger logger= Logger.getLogger(HAScript.class.getName());

    public static Data getAttribute(String entityId) {
        Data data = null;
        try {
            List<String> attributes = AttributesName.getAttributes(entityId);
            String[] args = new String[ATTR_ARGS_PREFIX + attributes.size()];
            args[0] = "python";
            args[1] = "/Users/kk/Repositories/HuaweiSmartHome/src/main/java/com/iscas/smarthome/homeassistant/get_attribute.py";
            args[2] = entityId;
            args[3] = AttributesName.getModeName(entityId);
            for(int i = ATTR_ARGS_PREFIX; i < args.length; i++)
                args[i] = attributes.get(i - ATTR_ARGS_PREFIX);

            Process proc = Runtime.getRuntime().exec(args);// 执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            int lineCount = 1;
            String line = null;
            data = new Data();
            while ((line = in.readLine()) != null) {
                if(lineCount == 1) {
                    data.setMode(line.trim());
                    lineCount++;
                } else {
                    data.setAttribute(InputUtils.strToAttribute(line));
                }
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("Get Attribute: " + entityId + " " + data.toString());
        return data;
    }

    public static String getAllAttributes(String entityId) {
        String res = "";
        try {
            List<String> attributes = AttributesName.getAttributes(entityId);
            String[] args = new String[3];
            args[0] = "python";
            args[1] = "/Users/kk/Repositories/HuaweiSmartHome/src/main/java/com/iscas/smarthome/homeassistant/get_all.py";
            args[2] = entityId;

            Process proc = Runtime.getRuntime().exec(args);// 执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                res = line;
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void getAllServices() {
        try {
//            ArrayList<String> attributes = AttributesName.getAttributes(entityId);
            String[] args = new String[2];
            args[0] = "python";
            args[1] = "/Users/kk/Repositories/HuaweiSmartHome/src/main/java/com/iscas/smarthome/homeassistant/get_service.py";

            Process proc = Runtime.getRuntime().exec(args);// 执行py文件
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void callService(String entityId, String serviceName) {
        try {
//            ArrayList<String> attributes = AttributesName.getAttributes(entityId);
            String[] args = new String[4];
            args[0] = "python";
            args[1] = "/Users/kk/Repositories/HuaweiSmartHome/src/main/java/com/iscas/smarthome/homeassistant/call_service.py";
            args[2] = entityId;
            args[3] = serviceName;

            logger.info("Call Service: " + entityId + " turn-on speed " + serviceName);
            ServiceName.preService = "turn_speed_" + serviceName;
            Process proc = Runtime.getRuntime().exec(args);// 执行py文件
//            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//
//            String line = null;
//            while ((line = in.readLine()) != null) {
//                System.out.println(line);
//            }
//            in.close();
            proc.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
