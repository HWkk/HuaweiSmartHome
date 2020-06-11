package com.iscas.smarthome.utils;

/**
 * 定时器，单位：秒
 */
public class Timer {

    public static void waitTimeGap(int timeGap) {
        long start = System.currentTimeMillis();
        while((System.currentTimeMillis() - start) / 1000.0 < timeGap) {
            try {
                Thread.sleep(timeGap * 1000 / 10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
