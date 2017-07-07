package com.ztf.talk;

import java.io.Serializable;

/**
 * Created by 赵腾飞 on 7/5/17.
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 11121929535321L;

    private String sendUser;
    private String sendHost;
    private String data;
    private String receiveUser;
    private String receiveHost;
    private boolean findUser = false;
    private boolean findUserReceive = false;

    private static final Message MESSAGE = null;

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSendHost() {
        return sendHost;
    }

    public void setSendHost(String sendHost) {
        this.sendHost = sendHost;
    }

    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }

    public String getReceiveHost() {
        return receiveHost;
    }

    public void setReceiveHost(String receiveHost) {
        this.receiveHost = receiveHost;
    }

    public boolean isFindUser() {
        return findUser;
    }

    public void setFindUser(boolean findUser) {
        this.findUser = findUser;
    }

    public boolean isFindUserReceive() {
        return findUserReceive;
    }

    public void setFindUserReceive(boolean findUserReceive) {
        this.findUserReceive = findUserReceive;
    }

    @Override
    public String toString() {
        return sendUser + ":    " + data;
    }
}
