package com.cloud.blog.dao;

import com.cloud.blog.dataObject.Pictures;

public interface PicturesMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pictures
     *
     * @mbg.generated Tue Feb 05 18:54:36 EST 2019
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pictures
     *
     * @mbg.generated Tue Feb 05 18:54:36 EST 2019
     */
    int insert(Pictures record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pictures
     *
     * @mbg.generated Tue Feb 05 18:54:36 EST 2019
     */
    int insertSelective(Pictures record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pictures
     *
     * @mbg.generated Tue Feb 05 18:54:36 EST 2019
     */
    Pictures selectByPrimaryKey(Integer id);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pictures
     *
     * @mbg.generated Tue Feb 05 18:54:36 EST 2019
     */
    int updateByPrimaryKeySelective(Pictures record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pictures
     *
     * @mbg.generated Tue Feb 05 18:54:36 EST 2019
     */
    int updateByPrimaryKeyWithBLOBs(Pictures record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table pictures
     *
     * @mbg.generated Tue Feb 05 18:54:36 EST 2019
     */
    int updateByPrimaryKey(Pictures record);
}