package com.clickawaybuying.shopify;

public class Address {
    private String fullname,region,province,city,barangay,username,phone;
    private  int postal,id,lotNum;
    public Address (String fullname,String phone,int lotNum,String region,String province,String city,String barangay,int postal,String username,int id){

        this.fullname = fullname;
        this.phone = phone;
        this.lotNum = lotNum;
        this.region = region;
        this.province = province;
        this.city = city;
        this.barangay = barangay;
        this.postal = postal;
        this.username = username;
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBarangay() {
        return barangay;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    public int getPostal() {
        return postal;
    }

    public void setPostal(int postal) {
        this.postal = postal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLotNum() {
        return lotNum;
    }

    public void setLotNum(int lotNum) {
        this.lotNum = lotNum;
    }

}
