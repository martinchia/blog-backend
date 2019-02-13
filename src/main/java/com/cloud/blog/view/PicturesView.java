package com.cloud.blog.view;

import org.json.JSONArray;

import java.util.List;

public class PicturesView {
    private Integer pictureId;
    private String discription;
    private List<Object> src;

    public Integer getPictureId() {
        return pictureId;
    }

    public void setPictureId(Integer pictureId) {
        this.pictureId = pictureId;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public List<Object> getSrc() {
        return src;
    }

    public void setSrc(JSONArray src) {
        this.src = src.toList();
    }
}
