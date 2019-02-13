package com.cloud.blog.service;

import com.cloud.blog.error.BusinessException;
import com.cloud.blog.service.model.ArticleModel;
import com.cloud.blog.service.model.ContentModel;
import com.cloud.blog.service.model.PictureModel;
import com.cloud.blog.service.model.UserModel;
import org.apache.catalina.User;
import org.json.JSONObject;

import java.util.List;

public interface ContentService {
    List<ContentModel> getContentByTimeUserId(Integer userId);
    List<ContentModel> getContentByTime();

    int postContent(ContentModel contentModel) throws BusinessException;
    void updateComment(Integer contentId, String comment, List<Integer> updateRoute, Integer userId) throws BusinessException;
    boolean ifLiked(ContentModel contentModel, UserModel userModel) throws BusinessException;
    void likeContent(ContentModel contentModel, UserModel currentUser) throws BusinessException;
    void cancelLikeContent(ContentModel contentModel, UserModel currentUser) throws BusinessException;
    void addViewContent(ContentModel contentModel);
    ContentModel getContentById(Integer id) throws BusinessException;
    ArticleModel getArticleById(Integer id);
    PictureModel getPictureById(Integer id);
    void updateArticle(ArticleModel articleModel);
    void deleteContent(Integer id);
    void deleteArticle(Integer id);
    void deletePicture(Integer id);

}
