package utils;

import homeassistant.EntityName;

public class Constants {

    //拉取设备属性的时间间隔，单位：秒
    public static int GET_ATTRIBUTE_TIME_GAP = 10;

    //调用服务的时间间隔，单位：秒
    public static int CALL_SERVICE_TIME_GAP = 10;

    //属性值数量
    public static int ATTRIBUTE_DIMENSION = 10;

    //衰减参数
    public static double DECAY_FACTOR = 0.5;

    //异常检测时的溢出阈值
    public static double ABNORMAL_THRESHOLD = 0.1;

    //设备名称（entity_id）
    public static String DEVICE_NAME = EntityName.AIR_HUMIDIFIER_NAME;

    //图可视化的txt和png所在目录
    public static String graphDir = "/Users/kk/Documents/ISCAS/HuaweiSmartHome/implementation_test/Result/";
}
