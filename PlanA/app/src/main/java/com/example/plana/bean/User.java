package com.example.plana.bean;

import androidx.annotation.Nullable;

import java.util.Objects;

/**
 * @program: PlanA
 * @description:
 */
public class User {
    private int    _id;
    private String phone;
    private String pwd;
    private String name;
    private String email;
    private String birth;

    public User() {
    }

    public User(int _id, String phone, String name) {
        this._id = _id;
        this.phone = phone;
        this.name = name;
    }

    public User(int _id, String phone, String pwd, String name, String email, String birth) {
        this._id = _id;
        this.phone = phone;
        this.pwd = pwd;
        this.name = name;
        this.email = email;
        this.birth = birth;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + _id +
                ", phone='" + phone + '\'' +
                ", pwd='" + pwd + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birth='" + birth + '\'' +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        //使用 instanceof 判断obj的类型是否正确
        if (!(obj instanceof User)) {
            return false;
        }
        return ((User) obj).get_id() == this.get_id()
                && ((User) obj).getPhone().equals(this.getPhone())
                && ((User) obj).getPwd().equals(this.getPwd())
                && ((User) obj).getName().equals(this.getName())
                && ((User) obj).getEmail().equals(this.getEmail())
                &&((User) obj).getBirth().equals(this.getBirth());
    }

}
