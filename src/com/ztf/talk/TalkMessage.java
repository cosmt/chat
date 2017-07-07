package com.ztf.talk;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 赵腾飞 on 7/6/17.
 */
public class TalkMessage implements ToolWindowFactory {
    private JPanel talkMessagePanel;
    private JTextArea oldMessage;
    private JTextArea newMessage;
    private JButton sendButton;
    private JLabel label;
    private JScrollPane newScrollPane;
    private JScrollPane oldScrollPane;

    //<String ,StringBuffer>  string:hostip  message:message缓存
    private static Map<String, StringBuffer> messages = new HashMap<>();
    //当前聊天窗口的user
    public static User user;
    private ToolWindow me;


    public TalkMessage() {
        //发送按钮
        sendButton.addActionListener(e -> {
            //获得new message
            String newMessage = this.newMessage.getText();
            //组装message,发送给对方.
            Message message = new Message();
            message.setData(newMessage);
            message.setReceiveHost(user.getHost());
            message.setReceiveUser(user.getName());
            Manager.sendMessage(message, user.getAddress());
            //刷新历史记录，并清空当前输入
            oldMessage.setText(Manager.getMessage(user.getHost()).toString());

            this.newMessage.setText("");
        });

        //接收 .
        new Thread(() -> {
            System.out.println("receive is works!");
            while (true) {
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                try {
                    Manager.getSocket().receive(packet);
                    Message receive = (Message) SerializeUtils.deserialize(packet.getData());
                    System.out.println("receive message :" + receive);
                    //如果发送的是 查找用户请求，返回用户名称。and receiveuser，双方添加
                    User newUser = new User(receive.getSendUser(), packet.getAddress().getHostAddress(), true);
                    Manager.getUsers().add(newUser);

                    //提示收到消息
/*
                    if (me.isActive()) {
                        //活跃状态，判断是否未当前用户，如果不是，则提示，并切换.
                        if (!user.getAddress().equals(packet.getAddress().getHostAddress())) {

                            int i = Messages.showOkCancelDialog("收到来自 " + newUser + "的新消息，是否切换显示", "new message", Messages.getQuestionIcon());
                            System.out.println(i);
                        }
                    } else {
                        //不活跃。直接强制弹出消息
                        me.show(() -> {
                        });
                    }
*/
                    if (receive.isFindUser()) {
                        if (!receive.isFindUserReceive()) {
                            //查找用户请求，返回用户名
                            receive.setFindUserReceive(true);
                            receive.setReceiveUser(Manager.getUserName());//接收到的请求
                            Manager.sendMessage(receive, packet.getAddress());
                        }
                    } else {
                        //接收到的记录 保存起来
                        StringBuffer stringBuffer = messages.get(packet.getAddress().getHostAddress());
                        if (stringBuffer == null) {
                            messages.put(packet.getAddress().getHostAddress(), new StringBuffer(receive.toString()));
                        } else {
                            stringBuffer.append('\n').append(receive.toString());
                        }
                    }
                    //刷新显示
                    for (User user : Manager.getUsers()) {
                        if (user.getHost().equals(packet.getAddress().getHostAddress())) {
                            this.user = user;
                        }
                    }
                    StringBuffer buffer = messages.get(packet.getAddress().getHostAddress());
                    oldMessage.setText(buffer != null ? buffer.toString() : "");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(talkMessagePanel, "", false);
        toolWindow.getContentManager().addContent(content);
        me = ToolWindowManager.getInstance(project).getToolWindow("TalkMessage");

    }

    public JPanel getTalkMessagePanel() {
        return talkMessagePanel;
    }

    public void setTalkMessagePanel(JPanel talkMessagePanel) {
        this.talkMessagePanel = talkMessagePanel;
    }

    public JTextArea getOldMessage() {
        return oldMessage;
    }

    public void setOldMessage(JTextArea oldMessage) {
        this.oldMessage = oldMessage;
    }

    public JTextArea getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(JTextArea newMessage) {
        this.newMessage = newMessage;
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public void setSendButton(JButton sendButton) {
        this.sendButton = sendButton;
    }

    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static Map<String, StringBuffer> getMessages() {
        return messages;
    }

    public static void setMessages(Map<String, StringBuffer> messages) {
        TalkMessage.messages = messages;
    }
}
