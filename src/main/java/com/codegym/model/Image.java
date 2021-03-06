package com.codegym.model;

public class Image {
    private int id;
    private String name;
    private String src;
    private int productId;

    public Image() {
    }

    public Image(int id, String name, String src, int productId) {
        this.id = id;
        this.name = name;
        this.src = src;
        this.productId = productId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
