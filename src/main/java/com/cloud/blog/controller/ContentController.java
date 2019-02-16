package com.cloud.blog.controller;

import com.cloud.blog.dataObject.Content;
import com.cloud.blog.error.BusinessException;
import com.cloud.blog.error.EmBusinessError;
import com.cloud.blog.response.CommonReturnType;
import com.cloud.blog.service.ContentService;
import com.cloud.blog.service.UserService;
import com.cloud.blog.service.Utils;
import com.cloud.blog.service.model.*;
import com.cloud.blog.view.ArticleView;
import com.cloud.blog.view.ContentView;
import com.cloud.blog.view.PicturesView;
import com.cloud.blog.view.VideoView;
import com.github.pagehelper.PageHelper;
import org.apache.catalina.User;
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

    @RequestMapping(value = "/addViewer", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType addViewer(
            @RequestParam(name = "contentId", required = true) int contentId) throws BusinessException {
        ContentModel contentModel = contentService.getContentById(contentId);
        contentService.addViewContent(contentModel);
        return CommonReturnType.create(null);
    }

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
                    contentService.deleteArticle(articleModel.getId());
                }
                contentService.deleteContent(contentId);
                return CommonReturnType.create(null);
            case 2:
                // picture
                PictureModel pictureModel = contentService.getPictureById(contentModel.getMappingId());
                if (pictureModel != null) {
                    contentService.deletePicture(pictureModel.getId());
                }
                contentService.deleteContent(contentId);
                return CommonReturnType.create(null);
            case 3:
            default:
                // video
                VideoModel videoModel = contentService.getVideoById(contentModel.getMappingId());
                if (videoModel != null) {
                    contentService.deleteVideo(videoModel.getId());
                }
                contentService.deleteContent(contentId);
                return CommonReturnType.create(null);
        }
    }

    @RequestMapping(value = "/like", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType like(
            @RequestParam(name = "contentId") int contentId) throws BusinessException {
        UserModel currentUser = userController.currentUser();
        if (currentUser == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_SIGNUP);
        }
        ContentModel contentModel = contentService.getContentById(contentId);
        if (contentModel == null) {
            throw new BusinessException(EmBusinessError.CONTENT_NOT_EXIST);
        }
        if (contentService.ifLiked(contentModel, currentUser)) {
            throw new BusinessException(EmBusinessError.USER_ALREADY_LIKED);
        }
        contentService.likeContent(contentModel, currentUser);
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/cancelLike", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType cancelLike(
            @RequestParam(name = "contentId") int contentId) throws BusinessException {
        UserModel currentUser = userController.currentUser();
        if (currentUser == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_SIGNUP);
        }
        ContentModel contentModel = contentService.getContentById(contentId);
        if (contentModel == null) {
            throw new BusinessException(EmBusinessError.CONTENT_NOT_EXIST);
        }
        if (contentService.ifLiked(contentModel, currentUser)) {
            contentService.cancelLikeContent(contentModel, currentUser);
        }
        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType search(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) throws BusinessException {
        if (!StringUtils.isNotEmpty(query)) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<ContentModel> contents = contentService.getContentByQuery(query);
        UserModel userModel = userController.currentUser();
        List<ContentView> res = new ArrayList<>();
        if (userModel == null) {
            for(ContentModel contentModel: contents) {
                res.add(convertFromContentModel(contentModel));
            }
        }
        else {
            for(ContentModel contentModel: contents) {
                res.add(convertFromContentModel(contentModel, userModel));
            }
        }
        return CommonReturnType.create(res);
    }

    @RequestMapping(value = "/getContent", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getContent(
            @RequestParam(name = "userid", required = false, defaultValue = "-1") int creator,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10") int pageSize) throws BusinessException {
        UserModel userModel = userController.currentUser();
        if (creator == -1){
            // no condition
            PageHelper.startPage(pageNumber, pageSize);
            List<ContentModel> contentModels = contentService.getContentByTime();

            List<ContentView> res = new ArrayList<>();
            if (userModel == null) {
                for(ContentModel contentModel: contentModels) {
                    res.add(convertFromContentModel(contentModel));
                }
            }
            else {
                for(ContentModel contentModel: contentModels) {
                    res.add(convertFromContentModel(contentModel, userModel));
                }
            }
            return CommonReturnType.create(res);
        }
        else if(creator == -2) {
            // current user
            if (userModel == null) {
                throw new BusinessException(EmBusinessError.USER_NOT_SIGNUP);
            }
            PageHelper.startPage(pageNumber, pageSize);
            List<ContentModel> contentModels = contentService.getContentByTimeUserId(userModel.getId().intValue());

            List<ContentView> res = new ArrayList<>();
            for(ContentModel contentModel: contentModels) {
                res.add(convertFromContentModel(contentModel, userModel));
            }
            return CommonReturnType.create(res);
        }
        else {
            // conditional selection
            PageHelper.startPage(pageNumber, pageSize);
            List<ContentModel> contentModels = contentService.getContentByTimeUserId(creator);

            List<ContentView> res = new ArrayList<>();
            if (userModel == null) {
                for(ContentModel contentModel: contentModels) {
                    res.add(convertFromContentModel(contentModel));
                }
            }
            else {
                for(ContentModel contentModel: contentModels) {
                    res.add(convertFromContentModel(contentModel, userModel));
                }
            }
            return CommonReturnType.create(res);
        }

    }

    @RequestMapping(value = "/getArticle", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getArticle(
            @RequestParam(name = "id") Integer articleId) {
        ArticleModel articleModel = contentService.getArticleById(articleId);
        return CommonReturnType.create(convertFromArticleModel(articleModel));
    }

    @RequestMapping(value = "/postArticle", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType postArticle(@RequestBody Map<String, String> data) throws BusinessException {
        UserModel userModel = userController.currentUser();
        String title = data.get("title");
        String context = data.get("context");
        // validation input parameters
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_SIGNUP);
        }
        if (title == null || context == null) {
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

    @RequestMapping(value = "/postPictures", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType uploadPictures(@RequestBody Map<String, Object> data) throws BusinessException {
        String discription = data.get("discription").toString();
        List<Object> imageList = (ArrayList) data.get("imageList");
        UserModel currentUser = userController.currentUser();
        if (currentUser == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_SIGNUP);
        }
        if (discription == null || imageList == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        PictureModel pictureModel = new PictureModel();
        pictureModel.setDiscription(discription);
        pictureModel.setSrc(imageList);
        int pictureId = contentService.postContent(pictureModel);

        ContentModel contentModel = new ContentModel();
        contentModel.setAuthorId(currentUser.getId().intValue());
        contentModel.setAuthorName(currentUser.getNickname());
        contentModel.setMappingId(pictureId);
        contentModel.setContentType(2);
        contentModel.setCreatedate(new Date());
        contentModel.setTimestamp((int) (System.currentTimeMillis() / 1000L));
        contentService.postContent(contentModel);

        return CommonReturnType.create(null);
    }

    @RequestMapping(value = "/getPictures", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getPictures(
            @RequestParam(name = "id") Integer pictureID) {
        PictureModel pictureModel = contentService.getPictureById(pictureID);
        return CommonReturnType.create(convertFromPictureModel(pictureModel));
    }

    @RequestMapping(value = "/postVideo", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType postVideo(@RequestBody Map<String, String> data) throws BusinessException {
        // TODO: now just support Youtube source, which is #1
        String discription = data.get("discription");
        String src = data.get("src");
        UserModel currentUser = userController.currentUser();

        // check parameters
        if (currentUser == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_SIGNUP);
        }
        if (StringUtils.isNotEmpty(discription) && StringUtils.isNotEmpty(src) &&
                src.matches("^www.youtube.com/watch\\?v=[a-zA-Z0-9_]{11}$")) {
            // Parameters are legal
            // write in Video
            VideoModel videoModel = new VideoModel();
            videoModel.setDiscription(discription);
            videoModel.setSource(1); // Youtube: 1
            videoModel.setSrc(src.split("v=")[1]);
            int videoId = contentService.postContent(videoModel);

            // write in Content
            ContentModel contentModel = new ContentModel();
            contentModel.setAuthorId(currentUser.getId().intValue());
            contentModel.setAuthorName(currentUser.getNickname());
            contentModel.setMappingId(videoId);
            contentModel.setContentType(3);
            contentModel.setCreatedate(new Date());
            contentModel.setTimestamp((int) (System.currentTimeMillis() / 1000L));
            contentService.postContent(contentModel);
            return CommonReturnType.create(null);
        }
        else {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
    }

    @RequestMapping(value = "/getVideo", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getVideo(
            @RequestParam(name = "id") Integer videoId) {
        VideoModel videoModel = contentService.getVideoById(videoId);
        return CommonReturnType.create(convertFromVideoModel(videoModel));
    }

    private VideoView convertFromVideoModel(VideoModel videoModel) {
        if (videoModel == null) {
            return null;
        }
        VideoView videoView = new VideoView();
        BeanUtils.copyProperties(videoModel, videoView);
        return videoView;
    }

    private PicturesView convertFromPictureModel(PictureModel pictureModel) {
        if (pictureModel == null) {
            return null;
        }
        PicturesView picturesView = new PicturesView();
        BeanUtils.copyProperties(pictureModel, picturesView);
        return picturesView;
    }

    private ContentView convertFromContentModel(ContentModel contentModel){
        if (contentModel == null) {
            return null;
        }
        ContentView contentView = new ContentView();
        BeanUtils.copyProperties(contentModel, contentView, Utils.getNullPropertyNames(contentModel));
        return contentView;
    }

    private ContentView convertFromContentModel(ContentModel contentModel, UserModel currentUser) throws BusinessException {
        ContentView contentView = convertFromContentModel(contentModel);
        contentView.setLiked(contentService.ifLiked(contentModel, currentUser));
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