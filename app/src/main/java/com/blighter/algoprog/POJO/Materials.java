package com.blighter.algoprog.POJO;

import java.util.ArrayList;

public class Materials {
    ArrayList<Materials> materials;
    String _id;
    Integer order;
    Integer indent;
    String type;
    String title;
    String content;

    public ArrayList<Materials> getMaterials() {
        return materials;
    }

    public void setMaterials(ArrayList<Materials> materials) {
        this.materials = materials;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getIndent() {
        return indent;
    }

    public void setIndent(Integer indent) {
        this.indent = indent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
