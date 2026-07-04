package com.ecommerce.project.security.loginResponse;

import java.util.List;

public class UserInfoResponse {
    private Long id;
    private String userName;
    private String JwtToken;
    private List<String> roles;

    public UserInfoResponse(Long id, String userName, String jwtToken, List<String> roles) {
        JwtToken = jwtToken;
        this.id = id;
        this.userName = userName;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getJwtToken() {
        return JwtToken;
    }

    public void setJwtToken(String jwtToken) {
        JwtToken = jwtToken;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
