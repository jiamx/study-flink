package com.jmx.processfunction;

/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/5/4
 *  @Time: 21:48
 *  
 */
class UserBehaviors {
    Long userId;
    String behavior;
    String item;

    public UserBehaviors(Long userId, String behavior, String item) {
        this.userId = userId;
        this.behavior = behavior;
        this.item = item;
    }

    @Override
    public String toString() {
        return "UserBehaviors{" +
                "userId=" + userId +
                ", behavior='" + behavior + '\'' +
                ", item='" + item + '\'' +
                '}';
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}