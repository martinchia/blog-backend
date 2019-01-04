package com.cloud.blog.controller;

import com.cloud.blog.error.BusinessException;
import com.cloud.blog.error.EmBusinessError;
import com.cloud.blog.service.Utils;
import com.cloud.blog.response.CommonReturnType;
import com.cloud.blog.service.UserService;
import com.cloud.blog.service.model.UserModel;
import com.cloud.blog.view.UserView;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller("user")
@RequestMapping("/user")
public class UserController extends GeneralController{

    @Autowired
    private UserService userService;

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Long id) throws BusinessException {
        // get a user object with id and return it to the front-end
        UserModel userModel = userService.getUserById(id);
        if(userModel == null) {
            userModel.setEmail("sdf");
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        UserView userView = convertFromModel(userModel);
        return CommonReturnType.create(userView);
    }

    public UserView convertFromModel(UserModel userModel) {
        if(userModel == null) {
            return null;
        }
        UserView userView = new UserView();
        BeanUtils.copyProperties(userModel, userView, Utils.getNullPropertyNames(userModel));
        return userView;
    }
}
