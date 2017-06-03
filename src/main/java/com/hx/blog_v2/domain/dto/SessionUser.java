package com.hx.blog_v2.domain.dto;

/**
 * 存放在session中的用户信息[登录的, 非登录的]
 *
 * @author Jerry.X.He <970655147@qq.com>
 * @version 1.0
 * @date 5/25/2017 8:10 PM
 */
public class SessionUser {

    /**
     * userId
     */
    private String id;
    private String userName;
    private String email;
    private String headImgUrl;
    private String title;
    private String roleIds;

    public SessionUser(String userName, String email, String headImgUrl, String title, String roleIds) {
        this.userName = userName;
        this.email = email;
        this.headImgUrl = headImgUrl;
        this.title = title;
        this.roleIds = roleIds;
    }

    public SessionUser() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }
}