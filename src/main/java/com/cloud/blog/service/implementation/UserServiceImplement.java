package com.cloud.blog.service.implementation;

import com.cloud.blog.error.BusinessException;
import com.cloud.blog.error.EmBusinessError;
import com.cloud.blog.service.Utils;
import com.cloud.blog.dao.UserInfoMapper;
import com.cloud.blog.dataObject.UserInfo;
import com.cloud.blog.service.UserService;
import com.cloud.blog.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
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

    @Override
    public UserModel getUserByName(String username) {
        UserInfo userInfo = userInfoMapper.selectByUserName(username);
        return convertFromDataObject(userInfo);
    }

    @Override
    public UserModel getUserByEmail(String email) {
        UserInfo userInfo = userInfoMapper.selectByMailAddress(email);
        return convertFromDataObject(userInfo);
    }

    private void writeInParamCheck(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        if (!StringUtils.isNotEmpty(userModel.getNickname() )
                || !StringUtils.isNotEmpty(userModel.getEmail())
                || !StringUtils.isNotEmpty(userModel.getPassword())){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        UserInfo userInfo = convertFromModel(userModel);
    }

    @Override
    public void register(UserModel userModel) throws BusinessException {
        this.writeInParamCheck(userModel);
        UserInfo userInfo = convertFromModel(userModel);
        userInfoMapper.insertSelective(userInfo);
    }

    @Override
    public void updateUserInfo(UserModel userModel) throws BusinessException {
        this.writeInParamCheck(userModel);
        UserInfo userInfo = convertFromModel(userModel);
        userInfoMapper.updateByPrimaryKeySelective(userInfo);
    }

    @Override
    public UserModel loginValidate(String email, String password) throws BusinessException {
        // check if this email is an existed user's email
        UserInfo userInfo = userInfoMapper.selectByMailAddress(email);
        if (userInfo == null) {
            throw new BusinessException(EmBusinessError.LOGIN_FAIL);
        }
        // check if the password is correct
        UserModel userModel = convertFromDataObject(userInfo);
        if (!StringUtils.equals(password, userModel.getPassword())) {
            throw new BusinessException(EmBusinessError.LOGIN_FAIL);
        }
        return userModel;
    }

    private UserInfo convertFromModel(UserModel userModel) {
        if(userModel == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(userModel, userInfo);
        return userInfo;
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
