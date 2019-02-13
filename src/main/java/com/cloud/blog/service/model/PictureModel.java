package com.cloud.blog.service.model;

import org.json.JSONArray;

import java.util.List;

public class PictureModel extends ContentModel{
    private Integer pictureId;

    public Integer getpictureId() {
        return pictureId;
    }

    public void setpictureId(Integer id) {
        this.pictureId = id;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public JSONArray getSrc() {
        return src;
    }

    public void setSrc(List<Object> src) {
        this.src = new JSONArray(src);
    }

    private String discription;
    private JSONArray src;
}
