package com.cloud.blog.controller;

import com.cloud.blog.dataObject.UserInfo;
import com.cloud.blog.error.BusinessException;
import com.cloud.blog.error.EmBusinessError;
import com.cloud.blog.service.Utils;
import com.cloud.blog.response.CommonReturnType;
import com.cloud.blog.service.UserService;
import com.cloud.blog.service.model.UserModel;
import com.cloud.blog.view.UserView;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class UserController extends GeneralController{

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    // API for modification of user info
    // API for user register
    @RequestMapping(value = "/modify", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType modify(@RequestBody Map<String, String> data) throws BusinessException {
        String username = data.get("username");
        String email = data.get("email");
        UserModel currentUser = (UserModel)this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
        if (currentUser == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_SIGNUP);
        }

        // parameter validation
        if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(email)) {
            // check if there are conflicts of username and email
            if (!StringUtils.equals(username, currentUser.getNickname())){
                if(this.userService.getUserByName(username) != null) {
                 throw new BusinessException(EmBusinessError.REGISTER_USERNAME_ALREADY_EXISTED);
                }
            }
            if (!StringUtils.equals(email, currentUser.getEmail())) {
                if (this.userService.getUserByEmail(email) != null) {
                    throw new BusinessException(EmBusinessError.REGISTER_EMAIL_ALREADY_EXISTED);
                }
            }
            currentUser.setEmail(email);
            currentUser.setNickname(username);
            this.userService.updateUserInfo(currentUser);
            return CommonReturnType.create(convertFromModel(currentUser));
        }
        else {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
    }

    // API for user update avatar
    @RequestMapping(value = "/updateAvatar", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType updateAvatar(@RequestBody Map<String, String> data) throws BusinessException {
        String imgUrl = data.get("img");
        UserModel currentUser = (UserModel)this.httpServletRequest.getSession().getAttribute("LOGIN_USER");
        if (currentUser == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_SIGNUP);
        }
        if(StringUtils.isNotEmpty(imgUrl)) {
            currentUser.setAvartar(imgUrl);
            this.userService.updateUserInfo(currentUser);
            return CommonReturnType.create(convertFromModel(currentUser));
        }
        else {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

    }

    // API for user register
    @RequestMapping(value = "/register", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestBody Map<String, Object> data) throws BusinessException {
        String email = data.get("email").toString();
        String username = data.get("username").toString();
        String password = data.get("password").toString();

        // parameter validation
        if (StringUtils.isNotEmpty(email) && StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
            // check duplicate
            UserModel userModel;
            userModel = userService.getUserByEmail(email);
            if (userModel != null) {
                throw new BusinessException(EmBusinessError.REGISTER_EMAIL_ALREADY_EXISTED);
            }
            userModel = userService.getUserByName(username);
            if (userModel != null) {
                throw new BusinessException(EmBusinessError.REGISTER_USERNAME_ALREADY_EXISTED);
            }
            // write in
            userModel = new UserModel();
            userModel.setNickname(username);
            userModel.setEmail(email);
            userModel.setPassword(UserController.crypt(password));
            userModel.setRegisterTime((int) (System.currentTimeMillis() / 1000L));

            userService.register(userModel);
            return CommonReturnType.create(convertFromModel(userModel));
        }
        else {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
    }

    @RequestMapping(value = "/login", method = {RequestMethod.POST}, consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestBody Map<String, String> data) throws BusinessException {
        String email = data.get("email");
        String password = data.get("password");

        // parameter validation
        if (StringUtils.isNotEmpty(email) && StringUtils.isNotEmpty(password)) {
            //
            UserModel userModel = userService.loginValidate(email, UserController.crypt(password));

            // put the login information into the user session
            this.httpServletRequest.getSession().setAttribute("IS_LOGIN", true);
            this.httpServletRequest.getSession().setAttribute("LOGIN_USER", userModel);
            return CommonReturnType.create(convertFromModel(userModel));
        }
        else {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }

    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType logout() {
        HttpSession session = this.httpServletRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return CommonReturnType.create(null);
    }

    // get optCode combined with email address
    @RequestMapping(value = "/getOpt", method = RequestMethod.GET)
    @ResponseBody
    public CommonReturnType getOptCode(@RequestParam(name = "email") String email) throws BusinessException {
        UserModel userModel = userService.getUserByEmail(email);
        if (userModel != null) {
            throw new BusinessException(EmBusinessError.REGISTER_EMAIL_ALREADY_EXISTED);
        }
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String optCode = String.valueOf(randomInt);
        httpServletRequest.getSession().setAttribute(email, optCode);
        return CommonReturnType.create(null);
    }

    @RequestMapping("/get")
    @ResponseBody
    @CrossOrigin
    public CommonReturnType getUser(@RequestParam(name = "id", required = false, defaultValue = "-1") Long id,
                                    @RequestParam(name = "username", required = false) String username,
                                    @RequestParam(name = "email", required = false) String email) throws BusinessException {
        if (id != -1 || username != null || email != null) {
            UserModel userModel = new UserModel();
            if (id != -1) {
                // get a user object with id and return it to the front-end
                userModel = userService.getUserById(id);
            }
            if (username != null) {
                userModel = userService.getUserByName(username);
            }
            if (email != null) {
                userModel = userService.getUserByEmail(email);
            }
            if(userModel == null) {
                throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
            }
            UserView userView = convertFromModel(userModel);
            return CommonReturnType.create(userView);
        }
        else {
            throw  new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
    }

    public static String crypt(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] hash = md.digest();
            for (int i = 0; i < hash.length; i++) {
                if ((0xff & hash[i]) < 0x10) {
                    hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
                } else {
                    hexString.append(Integer.toHexString(0xFF & hash[i]));
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexString.toString();
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
