package com.iscas.smarthome.controller;

import com.iscas.smarthome.homeassistant.AttrAndHardRelation;
import com.iscas.smarthome.homeassistant.AttributesName;
import com.iscas.smarthome.homeassistant.Caller;
import com.iscas.smarthome.stateautomaton.graph.OuterGraph;
import com.iscas.smarthome.test.BuildGraphPhase;
import com.iscas.smarthome.test.CheckDataPhase;
import com.iscas.smarthome.websocket.CustomWebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class StartController {

    OuterGraph graph;
    String entityName;
    int getAttrTimeGap = 2;
    int callServiceTimeGap = 10;
    int getAttrAfterCallingTimeGap = 2;

    @Autowired
    private CustomWebSocket webSocket;

    /**
     * 前后端交互demo
     */
    @RequestMapping("/start")
    public String start() {
        System.out.println("start");
        return "Hello, World!";
    }

    /**
     * 获取属性方法
     */
    @RequestMapping("/getAttributes")
    public String getAttributes(HttpServletRequest request) {
        String entityName = request.getParameter("entityName");
        this.entityName = entityName;
        String res = AttributesName.getAttributesNameFromHA(entityName).toString();
        return res.substring(1, res.length() - 1);
    }

    /**
     * 构建模型方法
     */
    @RequestMapping("/buildModel")
    public void buildModel(HttpServletRequest request) {
        String[] strs = request.getParameter("checkList").split(",");
        List<String> list = new ArrayList<>();
        for(String s : strs)
            list.add(s.trim());
        AttributesName.init(); //初始化过程
        AttributesName.addEntity(entityName, list); //将用户选择的属性更新到具体类中

        this.graph = Caller.init(entityName, getAttrTimeGap, callServiceTimeGap, getAttrAfterCallingTimeGap); //初始化过程
        Thread buildGraphThread = new Thread(new BuildGraphPhase(entityName, graph, webSocket)); //开始构建模型线程
        buildGraphThread.start();
        try {
            buildGraphThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        webSocket.sendAllMessage("/img/1.png");
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        webSocket.sendAllMessage("/img/2.png");
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        webSocket.sendAllMessage("FinishModel");
    }

    /**
     * 获取过滤后的属性
     */
    @RequestMapping("/getFilterAttributes")
    public String getFilterAttributes(HttpServletRequest request) {
        String entityName = request.getParameter("entityName");
        String res = AttributesName.getAttributes(entityName).toString();
        return res.substring(1, res.length() - 1);
    }

    /**
     * 更新属性与功能部件对应关系，然后开始异常检测定位过程
     */
    @RequestMapping("/putRelationAndStart")
    public void putRelationAndStart(HttpServletRequest request) {
        String relationList = request.getParameter("relationList");
//        System.out.println(relationList);

        String[] hardwareList = relationList.split(",");
        List<String> attributesList = AttributesName.getAttributes(entityName);
        for(int i = 0; i < hardwareList.length; i++) {
            hardwareList[i] = hardwareList[i].trim();
            if(hardwareList[i] != null && hardwareList[i].length() != 0 && !hardwareList[i].equals(""))
                AttrAndHardRelation.put(attributesList.get(i), hardwareList[i]);
        }
//        System.out.println(AttrAndHardRelation.print());
        new Thread(new CheckDataPhase(entityName, graph, webSocket)).start();
    }
}
