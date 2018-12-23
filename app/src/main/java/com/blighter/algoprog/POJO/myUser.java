package com.blighter.algoprog.POJO;

public class myUser {
    Level level;
    String _id;
    String name;
    String userList;
    Integer rating;
    Double activity;
    Double ratingSort;
    Boolean active;


    public String getName() {
        return name;
    }

    public Double getActivity() {
        return activity;
    }

    public Boolean getActive() {
        return active;
    }

    public Integer getRating() {
        return rating;
    }

    public Level getLevel() {
        return level;
    }

    public class Level {
        String current;
        String start;

        public String getCurrent() {
            return current;
        }

        public String getStart() {
            return start;
        }
    }

}
