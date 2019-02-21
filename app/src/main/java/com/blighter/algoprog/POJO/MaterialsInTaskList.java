package com.blighter.algoprog.POJO;

import java.util.ArrayList;

public class MaterialsInTaskList {
    ArrayList<Materials> materials;

    public ArrayList<Materials> getMaterials() {
        return materials;
    }

    private class Materials {
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
