package com.blighter.algoprog.pojo;

public class UserData {
    String username;
    String password;

    public UserData(String username, String password) {
        this.password = password;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
