package com.example.facedetection;

import androidx.annotation.NonNull;

public class UserInfo {
    public int id,age;
    public String username,sex;
    public UserInfo(int id, String username,String sex, int age){
        this.id = id;
        this.username = username;
        this.sex = sex;
        this.age = age;
    }


    public void sedId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

    @NonNull
    @Override
    public String toString() {
        return "UserInfo{" + "id = " + id + ", username = '" + username + '\'' + ", sex = '" + sex + '\'' +  ", age = " + age + '}';
    }
}
