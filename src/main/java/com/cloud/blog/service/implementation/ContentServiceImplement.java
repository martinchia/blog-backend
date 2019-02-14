package com.cloud.blog.service.implementation;

import com.cloud.blog.dao.*;
import com.cloud.blog.dataObject.*;
import com.cloud.blog.error.BusinessException;
import com.cloud.blog.error.EmBusinessError;
import com.cloud.blog.service.ContentService;
import com.cloud.blog.service.UserService;
import com.cloud.blog.service.Utils;
import com.cloud.blog.service.model.*;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ContentServiceImplement implements ContentService {
    @Autowired
    private ContentMapper contentMapper;
    @Autowired
    private ArticlesMapper articlesMapper;
    @Autowired
    private PicturesMapper picturesMapper;
    @Autowired
    private VideosMapper videosMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private LikedMapper likedMapper;

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
    public List<ContentModel> getContentByQuery(String query) {
        List<ContentModel> contents = contentMapper.conditionalSearch("%" + query + "%");
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
        else if (contentModel instanceof PictureModel) {
            Pictures pictures = convertPictureFromModel((PictureModel) contentModel);
            picturesMapper.insertSelective(pictures);
            return pictures.getId();
        }
        else if (contentModel instanceof VideoModel) {
            Videos videos = convertVideosFromModel((VideoModel) contentModel);
            videosMapper.insertSelective(videos);
            return videos.getId();
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
                route += "[" + i + "].subcomment";
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
    public boolean ifLiked(ContentModel contentModel, UserModel userModel) throws BusinessException {
        if (contentModel == null || userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        LikedKey likedKey = likedMapper.selectRelations(userModel.getId().intValue(), contentModel.getId());
        return likedKey != null;
    }

    @Override
    public void likeContent(ContentModel contentModel, UserModel currentuser) throws BusinessException {
        if (contentModel == null || currentuser == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        int like = contentModel.getLike();
        contentModel.setLike(like + 1);
        contentMapper.updateByPrimaryKeySelective(convertContentFromModel(contentModel));
        LikedKey likedKey = new LikedKey();
        likedKey.setContentid(contentModel.getId());
        likedKey.setUserid(currentuser.getId().intValue());
        likedMapper.insertSelective(likedKey);
    }

    @Override
    public void cancelLikeContent(ContentModel contentModel, UserModel currentUser) throws BusinessException {
        if (contentModel == null || currentUser == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        int like = contentModel.getLike();
        contentModel.setLike(like - 1);
        contentMapper.updateByPrimaryKeySelective(convertContentFromModel(contentModel));
        LikedKey likedKey = likedMapper.selectRelations(currentUser.getId().intValue(), contentModel.getId());
        if (likedKey == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        likedMapper.deleteByPrimaryKey(likedKey);
    }

    @Override
    public void addViewContent(ContentModel contentModel) {
        int viewers = contentModel.getView();
        contentModel.setView(viewers + 1);
        contentMapper.updateByPrimaryKeySelective(convertContentFromModel(contentModel));
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

    @Override
    public PictureModel getPictureById(Integer id) {
        Pictures pictures = picturesMapper.selectByPrimaryKey(id);
        return convertModelFromPicture(pictures);
    }

    @Override
    public VideoModel getVideoById(Integer id) {
        Videos videos = videosMapper.selectByPrimaryKey(id);
        return convertModelFromVideo(videos);
    }

    @Override
    public void updateArticle(ArticleModel articleModel) {
        if (articleModel == null) {
            return;
        }
        Articles articles = convertArticleFromModel(articleModel);
        articlesMapper.updateByPrimaryKey(articles);
    }

    @Override
    public void deleteContent(Integer id) {
        contentMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteArticle(Integer id) {
        articlesMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deletePicture(Integer id) {
        picturesMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteVideo(Integer id) {
        videosMapper.deleteByPrimaryKey(id);
    }

    private Videos convertVideosFromModel(VideoModel videoModel) {
        if (videoModel == null) {
            return null;
        }
        Videos videos = new Videos();
        BeanUtils.copyProperties(videoModel, videos);
        return videos;
    }

    private VideoModel convertModelFromVideo(Videos video) {
        if (video == null) {
            return null;
        }
        VideoModel videoModel = new VideoModel();
        BeanUtils.copyProperties(video, videoModel);
        return videoModel;
    }
    private Pictures convertPictureFromModel(PictureModel pictureModel) {
        if (pictureModel == null) {
            return null;
        }
        Pictures pictures = new Pictures();
        BeanUtils.copyProperties(pictureModel, pictures);
        List<JSONObject> tmp = new ArrayList<>();
        pictures.setSrc(pictureModel.getSrc().toString());
        return pictures;
    }

    private PictureModel convertModelFromPicture(Pictures pictures) {
        if (pictures == null) {
            return null;
        }
        PictureModel pictureModel = new PictureModel();
        BeanUtils.copyProperties(pictures, pictureModel);
        List<Object> src = new JSONArray(pictures.getSrc()).toList();
        pictureModel.setSrc(src);
        return pictureModel;
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
