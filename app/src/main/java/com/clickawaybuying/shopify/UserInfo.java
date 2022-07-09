package com.clickawaybuying.shopify;

public class UserInfo {
    private String fullname,image,username,email,birthday,gender;
    private  int phone;

    public UserInfo(String fullname, int phone, String image, String email, String birthday, String gender, String username) {
        this.fullname = fullname;
        this.phone = phone;
        this.image = image;
        this.email = email;
        this.birthday = birthday;
        this.gender = gender;
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    public int getPhone() {
        return phone;
    }
    public void setPhone(int phone) {
        this.phone = phone;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String city) {
        this.birthday = birthday;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
