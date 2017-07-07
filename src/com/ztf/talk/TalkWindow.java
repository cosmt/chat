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
                            System.out.println(TalkMessage.user);
                            talkMessage.show(() -> {
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
        Content content = contentFactory.createContent(talkWindowContent, "", false);
        toolWindow.getContentManager().addContent(content);
    }

}
