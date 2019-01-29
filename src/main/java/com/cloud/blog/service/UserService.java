package com.cloud.blog.service;

import com.cloud.blog.error.BusinessException;
import com.cloud.blog.service.model.UserModel;

public interface UserService {
    UserModel getUserById(Long id);
    UserModel getUserByName(String username);
    UserModel getUserByEmail(String email);

    void register(UserModel userModel) throws BusinessException;
    void updateUserInfo(UserModel userModel) throws BusinessException;
    UserModel loginValidate(String email, String password) throws BusinessException;
}
