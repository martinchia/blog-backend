package com.cloud.blog.view;

import java.util.Date;

public class UserView {
    private Long id;
    private String nickname;
    private String email;
    private String phoneNumber;
    private Integer type;
    private Date birthday;
    private String avartar;

    public Long getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Integer getType() {
        return type;
    }


    public Date getBirthday() {
        return birthday;
    }

    public String getAvartar() {
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

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setAvartar(String avartar) {
        this.avartar = avartar;
    }
}
