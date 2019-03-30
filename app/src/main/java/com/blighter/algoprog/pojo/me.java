package com.blighter.algoprog.pojo;

public class me {
    Level level;
    private String _id;
    private String username;
    private Integer informaticsId;
    private String informaticsUsername;
    private String aboutme;
    private Boolean admin;
    private Integer __v;

    private String get_id() {
        return _id;
    }

    private String getUsername() {
        return username;
    }

    private Integer getInformaticsId() {
        return informaticsId;
    }

    private String getInformaticsUsername() {
        return informaticsUsername;
    }

    private String getAboutme() {
        return aboutme;
    }

    private Boolean getAdmin() {
        return admin;
    }

    private Integer get__v() {
        return __v;
    }

    private class Level {
        String current;
        String start;

        private String getStart() {
            return start;
        }

        private String getCurrent() {

            return current;
        }
    }
}

