package com.cloud.blog.service.implementation;

import com.cloud.blog.dao.ArticlesMapper;
import com.cloud.blog.dao.ContentMapper;
import com.cloud.blog.dataObject.Articles;
import com.cloud.blog.dataObject.Content;
import com.cloud.blog.error.BusinessException;
import com.cloud.blog.error.EmBusinessError;
import com.cloud.blog.service.ContentService;
import com.cloud.blog.service.UserService;
import com.cloud.blog.service.Utils;
import com.cloud.blog.service.model.ArticleModel;
import com.cloud.blog.service.model.ContentModel;
import com.mysql.cj.xdevapi.JsonArray;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImplement implements ContentService {
    @Autowired
    private ContentMapper contentMapper;
    @Autowired
    private ArticlesMapper articlesMapper;
    @Autowired
    private UserService userService;

    @Override
    public List<ContentModel> getContentByTimeUserId(Integer userId) {
        List<ContentModel> contents = contentMapper.getContentByTimeUserId(userId);
        return contents;
    }

    @Override
    public List<ContentModel> getContentByTime() {
        List<ContentModel> contents = contentMapper.getContentByTime();
        return contents;
    }

    @Override
    public int postContent(ContentModel contentModel) throws BusinessException {
        if (contentModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        if (contentModel instanceof ArticleModel) {
            Articles article = convertArticleFromModel(contentModel);
            articlesMapper.insertSelective(article);
            return article.getId();
        }
        else {
            Content content = convertContentFromModel(contentModel);
            contentMapper.insertSelective(content);
            return content.getId();
        }
    }

    @Override
    public void updateComment(
            Integer contentId, String comment, List<Integer> updateRoute, Integer userId) throws BusinessException {
        Content content = contentMapper.selectByPrimaryKey(contentId);
        if(content == null) {
            throw new BusinessException(EmBusinessError.CONTENT_NOT_EXIST);
        }
        JSONObject object = new JSONObject();
        object.put("userId", userId);
        object.put("comment", comment);
        object.put("createDate", new Date());
        object.put("timestamp", (int) (System.currentTimeMillis() / 1000L));
        object.put("subcomment", new JSONArray());
        JSONArray res = new JSONArray();
        res = res.put(object);
        if (content.getComment() == null) {
            // need to initialize this field
            contentMapper.initializeComment(contentId, res.toString());
        }
        else {
            String route = "$";
            for(int i: updateRoute) {
                route += "[" + i + "]";
            }
            contentMapper.insertComment(
                    contentId,
                    userId,
                    comment,
                    new Date(),
                    (int)object.get("timestamp"),
                    new ArrayList<>(),
                    route);
        }
    }

    @Override
    public void likeContent(ContentModel contentModel) {

    }

    @Override
    public void addViewContent(ContentModel contentModel) {

    }

    @Override
    public ContentModel getContentById(Integer id) throws BusinessException {
        Content content = contentMapper.selectByPrimaryKey(id);
        if (content == null) {
            throw new BusinessException(EmBusinessError.CONTENT_NOT_EXIST);
        }
        return convertFromContentDO(content);
    }

    @Override
    public ArticleModel getArticleById(Integer id) {
        Articles articles = articlesMapper.selectByPrimaryKey(id);
        return convertFromArticleDO(articles);
    }

    private Content convertContentFromModel(ContentModel contentModel) {
        if (contentModel == null) {
            return null;
        }
        Content content = new Content();
        BeanUtils.copyProperties(contentModel, content);
        return content;
    }

    private Articles convertArticleFromModel(ContentModel contentModel) {
        if (contentModel == null) {
            return null;
        }
        Articles content = new Articles();
        BeanUtils.copyProperties(contentModel, content);
        return content;
    }

    private ArticleModel convertFromArticleDO(Articles articles) {
        if (articles == null) {
            return null;
        }
        ArticleModel articleModel = new ArticleModel();
        BeanUtils.copyProperties(articles, articleModel, Utils.getNullPropertyNames(articles));
        return articleModel;
    }

    private ContentModel convertFromContentDO(Content content) {
        if (content == null) {
            return null;
        }
        ContentModel contentModel = new ContentModel();

        contentModel.setTimestamp(content.getTimestamp());
        contentModel.setMappingId(content.getMappingId());
        contentModel.setCreatedate(content.getCreatedate());
        contentModel.setContentType(content.getContentType());
        contentModel.setId(content.getId());
        contentModel.setAuthorName(userService.getUserById(content.getAuthorId().longValue()).getNickname());
        contentModel.setAuthorId(content.getAuthorId());
        String comment = content.getComment();
        if (comment != null) {
            contentModel.setComment(comment);
        }
        contentModel.setLike(content.getLike());
        contentModel.setView(content.getView());

        return contentModel;
    }
}
