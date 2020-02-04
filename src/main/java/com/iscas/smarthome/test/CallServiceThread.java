package com.iscas.smarthome.test;

import com.iscas.smarthome.homeassistant.Caller;
import com.iscas.smarthome.utils.Constants;
import com.iscas.smarthome.utils.Timer;

public class CallServiceThread implements Runnable{

    String deviceName;

    public CallServiceThread(String deviceName) {
        this.deviceName = deviceName;
    }

    public void run() {
        while(true) {
            Caller.callService(deviceName);
            Timer.waitTimeGap(Constants.CALL_SERVICE_TIME_GAP);
        }
    }
}
