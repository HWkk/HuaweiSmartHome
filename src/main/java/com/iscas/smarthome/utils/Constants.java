package com.iscas.smarthome.utils;

import com.iscas.smarthome.homeassistant.EntityName;

public class Constants {

    //拉取设备属性的时间间隔，单位：秒
    public static int GET_ATTRIBUTE_TIME_GAP = 10;

    //调用服务的时间间隔，单位：秒
    public static int CALL_SERVICE_TIME_GAP = 10;

    //在调用服务一定时间间隔后，再获取属性，时间间隔为该值
    public static int GET_ATTRIBUTE_AFTER_CALL_SERVICE_GAP = 5;

    //属性值数量
    public static int ATTRIBUTE_DIMENSION = 10;

    //衰减参数
    public static double DECAY_FACTOR = 0.5;

    //异常检测时的溢出阈值
    public static double ABNORMAL_THRESHOLD = 0.5;

    //设备名称（entity_id）
    public static String DEVICE_NAME = EntityName.AIR_HUMIDIFIER_NAME;

    //图可视化的txt、png以及model存储目录
    public static String GRAPH_DIR = "/Users/kk/Documents/ISCAS/HuaweiSmartHome/implementation_test/Result/";

    //回溯的步数
    public static int BACK_COUNT = 5;

    //四分位距判断异常点时的阈值
    public static double IQR_THRESHOLD = 1.5;

    //异常检测的时间间隔，单位：秒
    public static int CHECK_TIME_GAP = 50;
}
