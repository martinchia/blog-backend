package com.cloud.blog.dao;

import com.cloud.blog.dataObject.Content;
import com.cloud.blog.service.model.ContentModel;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ContentMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table content
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table content
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    int insert(Content record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table content
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    int insertSelective(Content record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table content
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    Content selectByPrimaryKey(Integer id);
    List<ContentModel> getContentByTime();
    List<ContentModel> getContentByTimeUserId(Integer userId);
    int initializeComment(@Param("id") Integer contentId, @Param("jsonData") String jsonData);
    int insertComment(@Param("id") Integer contentId,
                      @Param("userId") Integer userId,
                      @Param("comment") String comment,
                      @Param("createDate") Date createDate,
                      @Param("timestamp") Integer timestamp,
                      @Param("subcomment") List<Object> subcomment,
                      @Param("route") String route);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table content
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    int updateByPrimaryKeySelective(Content record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table content
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    int updateByPrimaryKeyWithBLOBs(Content record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table content
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    int updateByPrimaryKey(Content record);
}