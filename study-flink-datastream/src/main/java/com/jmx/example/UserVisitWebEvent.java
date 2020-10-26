package com.jmx.example;



/**
 *  @Created with IntelliJ IDEA.
 *  @author : jmx
 *  @Date: 2020/4/23
 *  @Time: 12:55
 *  
 */

public class UserVisitWebEvent {

    /**
     * 日志的唯一 id
     */
    private String id;

    /**
     * 日期，如：20191025
     */
    private String date;

    /**
     * 页面 id
     */
    private Integer pageId;

    /**
     *  用户的唯一标识，用户 id
     */
    private String userId;

    /**
     * 页面的 url
     */
    private String url;

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public Integer getPageId() {
        return pageId;
    }

    public String getUserId() {
        return userId;
    }

    public String getUrl() {
        return url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPageId(Integer pageId) {
        this.pageId = pageId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}