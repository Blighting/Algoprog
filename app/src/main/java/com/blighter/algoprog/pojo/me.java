package com.blighter.algoprog.POJO;

public class me {
    Level level;
    String _id;
    String username;
    Integer informaticsId;
    String informaticsUsername;
    String aboutme;
    Boolean admin;
    Integer __v;

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

