package com.ztf.talk;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by 赵腾飞 on 7/5/17.
 */
public class TalkWindow implements ToolWindowFactory {
    private JPanel talkWindowContent;
    private JButton refursh;
    private JList users;
    private JLabel label;
    private JScrollPane usersScrollPane;
    //实例化 ToolWindowManager
    private ToolWindowManager instance;
    public static TalkWindow talkWindow;

    public TalkWindow() {
        refursh.addActionListener(e -> {
            //刷新 user
            Manager.findUser();
            try {
                Thread.sleep(500);
                //刷新 用户列表
                users.setListData(Manager.getUsers().toArray());
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        });

        users.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (users.getSelectedIndex() != -1) {
                    //鼠标双击 事件
                    if (e.getClickCount() == 2) {
                        //打开聊天窗口
                        ToolWindow talkMessage = instance.getToolWindow("TalkMessage");
                        if (talkMessage != null) {
                            TalkMessage.user = (User) users.getSelectedValue();
                            talkMessage.show(() -> {
                                TalkMessage message = TalkMessage.talkMessage;
                                if (message != null) {
                                    StringBuffer buffer = TalkMessage.getMessages().get(TalkMessage.user.getHost());
                                    if (buffer != null) {
                                        message.getOldMessage().setText(buffer.toString());
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        //实例化
        instance = ToolWindowManager.getInstance(project);

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(talkWindowContent, "talkwindow", false);
        toolWindow.getContentManager().addContent(content);
        talkWindow = this;
    }

    public JPanel getTalkWindowContent() {
        return talkWindowContent;
    }

    public void setTalkWindowContent(JPanel talkWindowContent) {
        this.talkWindowContent = talkWindowContent;
    }

    public JButton getRefursh() {
        return refursh;
    }

    public void setRefursh(JButton refursh) {
        this.refursh = refursh;
    }

    public JList getUsers() {
        return users;
    }

    public void setUsers(JList users) {
        this.users = users;
    }

    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    public JScrollPane getUsersScrollPane() {
        return usersScrollPane;
    }

    public void setUsersScrollPane(JScrollPane usersScrollPane) {
        this.usersScrollPane = usersScrollPane;
    }

    public ToolWindowManager getInstance() {
        return instance;
    }

    public void setInstance(ToolWindowManager instance) {
        this.instance = instance;
    }

    public static TalkWindow getTalkWindow() {
        return talkWindow;
    }

    public static void setTalkWindow(TalkWindow talkWindow) {
        TalkWindow.talkWindow = talkWindow;
    }
}
