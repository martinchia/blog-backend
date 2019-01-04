package com.cloud.blog.service.implementation;

import com.cloud.blog.service.Utils;
import com.cloud.blog.dao.UserInfoMapper;
import com.cloud.blog.dataObject.UserInfo;
import com.cloud.blog.service.UserService;
import com.cloud.blog.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplement implements UserService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserModel getUserById(Long id) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(id);
        return convertFromDataObject(userInfo);
    }

    public UserModel convertFromDataObject(UserInfo userInfo) {
        if(userInfo == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userInfo, userModel, Utils.getNullPropertyNames(userInfo));
        return userModel;
    }
}
