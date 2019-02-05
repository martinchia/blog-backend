package com.cloud.blog.controller;

import com.cloud.blog.dataObject.Content;
import com.cloud.blog.error.BusinessException;
import com.cloud.blog.error.EmBusinessError;
import com.cloud.blog.response.CommonReturnType;
import com.cloud.blog.service.ContentService;
import com.cloud.blog.service.UserService;
import com.cloud.blog.service.Utils;
import com.cloud.blog.service.model.ArticleModel;
import com.cloud.blog.service.model.ContentModel;
import com.cloud.blog.service.model.UserModel;
import com.cloud.blog.view.ArticleView;
import com.cloud.blog.view.ContentView;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller("content")
@RequestMapping("/content")
@Scope("session")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class ContentController extends GeneralController {

    @Autowired
    private ContentService contentService;

    @Autowired
    private UserController userController;

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType deleteContent(
            @RequestParam(name = "contentId", required = true) int contentId) throws BusinessException {
        UserModel userModel = userController.currentUser();
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_SIGNUP);
        }
        ContentModel contentModel = contentService.getContentById(contentId);
        if (contentModel == null) {
            throw new BusinessException(EmBusinessError.CONTENT_NOT_EXIST);
        }

        if (userModel.getId() != contentModel.getAuthorId().intValue()) {
            throw new BusinessException(EmBusinessError.AUTHORITICATION_ERROR);
        }
        switch (contentModel.getContentType()){
            case 1:
                // article
                ArticleModel articleModel = contentService.getArticleById(contentModel.getMappingId());
                if (articleModel != null) {
                    contentService.deleteArticle(articleModel.getArticleID());
                }
                contentService.deleteContent(contentId);
                return CommonReturnType.create(null);
            case 2:
                // picture
            case 3:
            default:
                // video
                return CommonReturnType.create(null); // TODO: supplement!
        }
    }

    @RequestMapping(value = "/getContent", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getContent(
            @RequestParam(name = "userid", required = false, defaultValue = "-1") int creator,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) throws BusinessException {
        if (creator == -1){
            // no condition
            PageHelper.startPage(pageNumber, pageSize);
            List<ContentModel> contentModels = contentService.getContentByTime();

            List<ContentView> res = new ArrayList<>();
            for(ContentModel contentModel: contentModels) {
                res.add(convertFromContentModel(contentModel));
            }
            return CommonReturnType.create(res);
        }
        else if(creator == -2) {
            // current user
            UserModel userModel = userController.currentUser();
            if (userModel == null) {
                throw new BusinessException(EmBusinessError.USER_NOT_SIGNUP);
            }
            PageHelper.startPage(pageNumber, pageSize);
            List<ContentModel> contentModels = contentService.getContentByTimeUserId(userModel.getId().intValue());

            List<ContentView> res = new ArrayList<>();
            for(ContentModel contentModel: contentModels) {
                res.add(convertFromContentModel(contentModel));
            }
            return CommonReturnType.create(res);
        }
        else {
            // conditional selection
            PageHelper.startPage(pageNumber, pageSize);
            List<ContentModel> contentModels = contentService.getContentByTimeUserId(creator);

            List<ContentView> res = new ArrayList<>();
            for(ContentModel contentModel: contentModels) {
                res.add(convertFromContentModel(contentModel));
            }
            return CommonReturnType.create(res);
        }

    }

    @RequestMapping(value = "/getArticle", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getArticle(
            @RequestParam(name = "id", required = true) Integer articlaID) {
        ArticleModel articleModel = contentService.getArticleById(articlaID);
        return CommonReturnType.create(convertFromArticleModel(articleModel));
    }

    @RequestMapping(value = "/postArticle", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType postArticle(@RequestBody Map<String, String> data) throws BusinessException {
        UserModel userModel = userController.currentUser();
        String title = data.get("title");
        String context = data.get("context");
        // validation input parameters
        if (userModel == null || title == null || context == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        // write in article
        Integer userId = userModel.getId().intValue();
        ArticleModel articleModel = new ArticleModel();
        articleModel.setContext(context);
        articleModel.setTitle(title);
        int articleId = contentService.postContent(articleModel);

        // write in general content
        ContentModel contentModel = new ContentModel();
        contentModel.setAuthorId(userId);
        contentModel.setAuthorName(userModel.getNickname());
        contentModel.setContentType(1);
        contentModel.setCreatedate(new Date());
        contentModel.setMappingId(articleId);
        contentModel.setTimestamp((int) (System.currentTimeMillis() / 1000L));
        contentService.postContent(contentModel);

        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/updateComment", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType updateComment(@RequestBody Map<String, Object> data) throws BusinessException {
        Integer contentId = (Integer) data.get("contentId");
        String comment = data.get("comment").toString();
        List<Integer> updateRoute = (List) data.get("route");
        if(contentId == null || comment == null) {
            throw new BusinessException(EmBusinessError.CONTENT_NOT_EXIST);
        }
        UserModel userModel = userController.currentUser();
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_SIGNUP);
        }
        contentService.updateComment(contentId, comment, updateRoute, userModel.getId().intValue());
        ContentView contentView = convertFromContentModel(contentService.getContentById(contentId));
        return CommonReturnType.create(contentView.getComment());
    }


    @RequestMapping(value = "/updateArticle", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType updateArticle(@RequestBody Map<String, Object> data) throws BusinessException {
        Integer contentId = (Integer) data.get("contentId");
        Integer articleId = (Integer) data.get("articleId");
        String article = data.get("article").toString();
        String title = data.get("title").toString();

        UserModel userModel = userController.currentUser();
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_SIGNUP);
        }

        if (contentId != null && articleId != null &&
                StringUtils.isNotEmpty(article) && StringUtils.isNotEmpty(title)){
            ArticleModel articleModel = contentService.getArticleById(articleId);
            ContentModel contentModel = contentService.getContentById(contentId);
            if (articleModel == null || contentModel == null) {
                throw new BusinessException(EmBusinessError.CONTENT_NOT_EXIST);
            }
            if (articleModel.getId() != contentModel.getMappingId()) {
                throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
            }
            if (contentModel.getAuthorId() != userModel.getId().intValue()) {
                throw new BusinessException(EmBusinessError.AUTHORITICATION_ERROR);
            }
            articleModel.setContext(article);
            articleModel.setTitle(title);
            contentService.updateArticle(articleModel);
            return CommonReturnType.create(convertFromArticleModel(articleModel));
        }
        else {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
    }

    private ContentView convertFromContentModel(ContentModel contentModel){
        if (contentModel == null) {
            return null;
        }
        ContentView contentView = new ContentView();
        BeanUtils.copyProperties(contentModel, contentView, Utils.getNullPropertyNames(contentModel));
        return contentView;
    }

    private ArticleView convertFromArticleModel(ArticleModel articleModel){
        if (articleModel == null) {
            return null;
        }
        ArticleView articleView = new ArticleView();
        BeanUtils.copyProperties(articleModel, articleView, Utils.getNullPropertyNames(articleModel));
        return articleView;
    }
}