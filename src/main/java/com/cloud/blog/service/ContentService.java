package com.cloud.blog.service;

import com.cloud.blog.error.BusinessException;
import com.cloud.blog.service.model.ArticleModel;
import com.cloud.blog.service.model.ContentModel;
import org.json.JSONObject;

import java.util.List;

public interface ContentService {
    List<ContentModel> getContentByTimeUserId(Integer userId);
    List<ContentModel> getContentByTime();

    int postContent(ContentModel contentModel) throws BusinessException;
    void updateComment(Integer contentId, String comment, List<Integer> updateRoute, Integer userId) throws BusinessException;
    void likeContent(ContentModel contentModel);
    void addViewContent(ContentModel contentModel);

    ArticleModel getArticleById(Integer id);
}
