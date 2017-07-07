package com.ztf.talk;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by 赵腾飞 on 7/5/17.
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;


    private String name;
    private String host;
    private boolean online = false;
    private InetAddress address;


    public User(String name, String host) {
        this.name = name;
        this.host = host;
        try {
            this.address = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public User(String name, String host, boolean online) {
        this.name = name;
        this.host = host;
        try {
            this.address = InetAddress.getByName(host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.online = online;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return name + " : " + host + (online ? "(在线)" : "(离线)");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return host != null ? host.equals(user.host) : user.host == null;
    }

    @Override
    public int hashCode() {
        return host != null ? host.hashCode() : 0;
    }
}
