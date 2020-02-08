package com.iscas.smarthome.controller;

import com.iscas.smarthome.homeassistant.AttributesName;
import com.iscas.smarthome.homeassistant.Caller;
import com.iscas.smarthome.stateautomaton.graph.OuterGraph;
import com.iscas.smarthome.test.CheckDataPhase;
import com.iscas.smarthome.websocket.CustomWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "*")
public class StartController {

    OuterGraph graph;
    String entityName;
    int getAttrTimeGap = 5;
    int callServiceTimeGap = 90;
    int getAttrAfterCallingTimeGap = 60;

    @Autowired
    private CustomWebSocket webSocket;

    @RequestMapping("/start")
    public String start() {
        System.out.println("start");
        return "Hello, World!";
    }

    @RequestMapping("/getAttributes")
    public String getAttributes(HttpServletRequest request) {
        String entityName = request.getParameter("entityName");
        this.entityName = entityName;
        //TODO: 通过HA获取所有属性
        String res = AttributesName.getAttributes(entityName).toString();
        return res.substring(1, res.length() - 1);
    }

    @RequestMapping("/buildModel")
    public void buildModel(HttpServletRequest request) {
        String checkList = request.getParameter("checkList");
        this.graph = Caller.init(entityName, getAttrTimeGap, callServiceTimeGap, getAttrAfterCallingTimeGap);
        //TODO: 要改成建模过程中实时发送图片位置
//        Thread buildGraphThread = new Thread(new BuildGraphPhase(entityName, graph));
//        buildGraphThread.start();
//        try {
//            buildGraphThread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //TODO: 信息还要加以区分，区分成建模的信息还是异常检测的信息
        webSocket.sendAllMessage("/img/1.jpg");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        webSocket.sendAllMessage("/img/2.jpg");
    }

    @RequestMapping("/checkData")
    public void checkData(HttpServletRequest request) {
        //TODO: 检测过程中实时发送异常信息
        new Thread(new CheckDataPhase(entityName)).start();
    }
}
