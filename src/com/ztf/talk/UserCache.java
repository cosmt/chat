package com.ztf.talk;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 赵腾飞 on 7/5/17.
 */
public class UserCache {

    private static final Map<String, User> MAP = new HashMap<>();

    public static Map<String, User> getMAP() {
        return MAP;
    }

    public static Map<String, User> addUser(User user) {
        MAP.put(user.getHost(), user);
        return MAP;
    }
}
