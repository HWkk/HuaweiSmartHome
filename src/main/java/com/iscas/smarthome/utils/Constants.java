package com.iscas.smarthome.utils;

import com.iscas.smarthome.homeassistant.EntityName;

/**
 * 一些常量信息
 */
public class Constants {

    //拉取设备属性的时间间隔，单位：秒
    public static int GET_ATTRIBUTE_TIME_GAP = 2;

    //调用服务的时间间隔，单位：秒
    public static int CALL_SERVICE_TIME_GAP = 15;

    //在调用服务一定时间间隔后，再获取属性，时间间隔为该值
    public static int GET_ATTRIBUTE_AFTER_CALL_SERVICE_GAP = 5;

    //属性值数量
    public static int ATTRIBUTE_DIMENSION = 10;

    //衰减参数
    public static double DECAY_FACTOR = 0.5;

    //异常检测时的溢出阈值
    public static double ABNORMAL_THRESHOLD = 0.5;

    //状态边界的上下边界
    public static double UP_BORDER_ABNORMAL_THRESHOLD = 1.2;
    public static double DOWN_BORDER_ABNORMAL_THRESHOLD = 0.8;

    //曲线相似度比较中，取值相似度的阈值
    public static double VALUE_THRESHOLD = 0.25;

    //曲线相似度比较中，斜率相似度的阈值
    public static double SLOPE_THRESHOLD = 0.25;

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

    //每获取一定量数据，再进行(异常检测与)发送实时数据图
    public static int CHECK_STEP_GAP = 5;

    //数据条数达到此阈值后，才开始进行检测过程，否则结果不准确
    public static int CHECK_DATA_MIN_THRESHOLD = 50;

    //用于展示的模型图片目录
    public static String MODEL_PNG_DIR = "/Users/kk/Repositories/HuaweiSmartHome/frontend/public/img/model/";

    //用于展示的属性图片目录
    public static String ATTRIBUTE_PNG_DIR = "/Users/kk/Repositories/HuaweiSmartHome/frontend/public/img/attribute/";

    //脚本地址（项目地址）
    public static String SCRIPT_LOCATION = "/Users/kk/Repositories/HuaweiSmartHome/src/main/java/com/iscas/smarthome/homeassistant/";
}
