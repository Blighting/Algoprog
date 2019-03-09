package com.blighter.algoprog.POJO;
public class MaterialsInTaskList {
    private Path[] path;

    private Materials[] materials;

    private String indent;

    private String force;

    private String _id;

    private String type;

    private String title;

    private String order;

    public Path[] getPath() {
        return path;
    }

    public void setPath(Path[] path) {
        this.path = path;
    }

    public Materials[] getMaterials() {
        return materials;
    }

    public void setMaterials(Materials[] materials) {
        this.materials = materials;
    }

    public String getIndent() {
        return indent;
    }

    public void setIndent(String indent) {
        this.indent = indent;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}