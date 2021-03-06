package com.cloud.blog.dao;

import com.cloud.blog.dataObject.UserInfo;

public interface UserInfoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    int insert(UserInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    int insertSelective(UserInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    UserInfo selectByPrimaryKey(Long id);
    UserInfo selectByMailAddress(String email);
    UserInfo selectByUserName(String username);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    int updateByPrimaryKeySelective(UserInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    int updateByPrimaryKeyWithBLOBs(UserInfo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_info
     *
     * @mbg.generated Sat Jan 12 21:33:54 EST 2019
     */
    int updateByPrimaryKey(UserInfo record);
}