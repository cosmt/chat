package com.ztf.talk;

import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.tools.ToolManager;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by 赵腾飞 on 7/5/17.
 */
public class Manager {

    private static int message = 0;

    //udp port
    private static final int port = 5321;
    //udp socket
    private static DatagramSocket socket;
    //user name
    private static String userName;
    //host address
    private static String hostAddress;
    //find user message .
    private static Message findUser;

    private static Set<User> users = new HashSet<>();



    static {
        try {
            socket = new DatagramSocket(port);
            userName = InetAddress.getLocalHost().getHostName();
            hostAddress = InetAddress.getLocalHost().getHostAddress();

            findUser = new Message();
            findUser.setSendHost(hostAddress);
            findUser.setSendUser(userName);
            findUser.setFindUser(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //发送寻找用户请求
    public static void findUser() {
        try {
            // Broadcast address
            InetAddress broadcastAddress = InetAddress.getByName("255.255.255.255");

            //build message
            byte[] bytes = SerializeUtils.serialize(findUser);

            //此处不用分包处理。序列化后只有200
            DatagramPacket out = new DatagramPacket(bytes, bytes.length, broadcastAddress, port);
            System.out.println("send finduser message");
            socket.send(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(Message message, InetAddress address) {
        try {
            message.setSendHost(hostAddress);
            message.setSendUser(userName);
            System.out.println("send message: " + message);
            //build message
            byte[] bytes = SerializeUtils.serialize(message);

            DatagramPacket out = new DatagramPacket(bytes, bytes.length, address, port);

            socket.send(out);
            //发送成功后，缓存聊天记录
            StringBuffer msgs = TalkMessage.getMessages().get(message.getReceiveHost());
            if (msgs == null) {
                TalkMessage.getMessages().put(message.getReceiveHost(), new StringBuffer(message.toString()));
            } else {
                msgs.append('\n').append(message.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getUserName() {
        return userName;
    }

    public static StringBuffer getMessage(String host) {
        return TalkMessage.getMessages().get(host);
    }

    public static Set<User> getUsers() {
        return users;
    }

    public static DatagramSocket getSocket() {
        return socket;
    }
}
