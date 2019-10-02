package test;

import homeassistant.Caller;
import utils.Constants;
import utils.Timer;

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
