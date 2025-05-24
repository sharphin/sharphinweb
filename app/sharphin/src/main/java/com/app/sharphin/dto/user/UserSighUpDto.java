package com.app.sharphin.dto.user;

public class UserSighUpDto {
    private String user_id;
    private String user_name;
    private String email;
    private String password;
    private String icon_path;
    private String authority;
    public UserSighUpDto() {}
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setIcon_path(String icon_path) {
        this.icon_path = icon_path;
    }
    public void setAuthority(String authority) {
        this.authority = authority;
    }
    public String getUser_id() {
        return user_id;
    }
    public String getUser_name() {
        return user_name;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getIcon_path() {
        return icon_path;
    } 
    public String getAuthoriy() {
        return authority;
    }
}
