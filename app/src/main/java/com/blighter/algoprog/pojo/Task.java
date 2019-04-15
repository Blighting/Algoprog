package com.blighter.algoprog.pojo;

import java.util.List;

public class Task {
    public List<Paths> path;
    public String _id;
    public Integer order;
    public String title;
    public String content;

    public List<Paths> getPath() {
        return path;
    }

    public String getContent() {
        return content;
    }

    private class Paths {
        String _id;
        String title;

        public String get_id() {
            return _id;
        }

        public String getTitle() {
            return title;
        }
    }
}
