package com.cloud.blog.service.model;

import java.util.Date;

public class UserModel {
    private Long id;
    private String nickname;
    private String email;
    private String password;
    private String phoneNumber;
    private Integer type;
    private Integer registerTime;
    private Date birthday;
    private byte[] avartar;

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Integer getType() {
        return type;
    }

    public Integer getRegisterTime() {
        return registerTime;
    }

    public Date getBirthday() {
        return birthday;
    }

    public byte[] getAvartar() {
        return avartar;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setRegisterTime(Integer registerTime) {
        this.registerTime = registerTime;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setAvartar(byte[] avartar) {
        this.avartar = avartar;
    }
}
