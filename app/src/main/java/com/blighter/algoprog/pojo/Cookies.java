package com.blighter.algoprog.pojo;

public class Cookies {
    private static Boolean cookies;

    public Cookies(Boolean cookies) {
        Cookies.cookies = cookies;
    }

    public Boolean getCookies() {
        return cookies;
    }
}
