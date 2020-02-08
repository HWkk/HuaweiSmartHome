package com.iscas.smarthome.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket")
//此注解相当于设置访问URL
public class CustomWebSocket {

    private Session session;
    private static CopyOnWriteArraySet<CustomWebSocket> webSockets = new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println(this.session);
        webSockets.add(this);
        System.out.println("【websocket消息】有新的连接，总数为: " + webSockets.size());
    }

    @OnClose
    public void onClose() {
        webSockets.remove(this);
        System.out.println("【websocket消息】连接断开，总数为: " + webSockets.size());
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("【websocket消息】收到客户端消息: " + message);
    }

    // 此为广播消息
    public void sendAllMessage(String message) {
        for(CustomWebSocket webSocket : webSockets) {
            System.out.println("【websocket消息】广播消息: " + message);
            try {
                webSocket.session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息
    public void sendOneMessage(String message) {
        try {
            //TODO: 这样用session是null，先用上面的广播发送
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
