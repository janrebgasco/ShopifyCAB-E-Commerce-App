package com.clickawaybuying.shopify.classes;

public class Product {
    private String product_name, image,description,brand,product_name1, image1,date,seller,message,reply,color,size,color1,size1,product_name2,image2,description2,brand2
            ,seller2,color2,size2,address1,img,username,feedback,sellerImage;
    private double rating,rating2,rating3;
    private  int id,sold,stocks,price,price1,total,quantity,cartQuan,id1,price2,sold2,stocks2,ProdID,quantity1;

    public Product (String product_name, int price,double rating, String image,String description,String brand,int stocks,int sold,int id,String seller, String color, String size,int quantity1){

        this.product_name = product_name;
        this.image = image;
        this.rating = rating;
        this.price = price;
        this.description = description;
        this.brand = brand;
        this.stocks = stocks;
        this.sold = sold;
        this.id=id;
        this.seller = seller;
        this.color = color;
        this.size = size;
        this.quantity1 = quantity1;
    }
    public Product (String product_name, int price,double rating, String image,String description,String brand,int stocks,int sold,int id,String seller, String color, String size,String sellerImage){

        this.product_name = product_name;
        this.image = image;
        this.rating = rating;
        this.price = price;
        this.description = description;
        this.brand = brand;
        this.stocks = stocks;
        this.sold = sold;
        this.id=id;
        this.seller = seller;
        this.color = color;
        this.size = size;
        this.sellerImage = sellerImage;
    }

    public Product(String product_name1, int price1, int quantity, String image1, int total, int id1,String date,String color1,String size1,String address1,int ProdID,String emp,String empty) {
        this.product_name1 = product_name1;
        this.image1 = image1;
        this.price1 = price1;
        this.quantity = quantity;
        this.total = total;
        this.date = date;
        this.id1 = id1;
        this.color1 = color1;
        this.size1 = size1;
        this.address1 = address1;
        this.ProdID = ProdID;
    }

    public Product(int cartQuan) {
        this.cartQuan = cartQuan;
    }

    public Product(String message) {
        this.message = message;
    }
    public Product(String addParam, String reply) {
        this.reply = reply;
    }

    public Product(String product_name2, int price2, String image2, double rating2, String description2, String brand2, int stocks2, int sold2, String seller2, String color2, String size2) {
        this.product_name2 = product_name2;
        this.image2 = image2;
        this.rating2 = rating2;
        this.price2 = price2;
        this.description2 = description2;
        this.brand2 = brand2;
        this.stocks2 = stocks2;
        this.sold2 = sold2;
        this.seller2 = seller2;
        this.color2 = color2;
        this.size2 = size2;
    }

    public Product(String img, String username, Double rating, String feedback) {
        this.img = img;
        this.username = username;
        this.rating3 = rating;
        this.feedback = feedback;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getTitle() {
        return product_name;
    }

    public void setTitle(String product_name) {
        this.product_name = product_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public float getRating() {
        return (float) rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand(){
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getStocks() {
        return stocks;
    }

    public void setStocks(int stocks) {
        this.stocks = stocks;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public String getTitle1() {
        return product_name1;
    }

    public void setTitle1(String product_name1) {
        this.product_name1 = product_name1;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public int getPrice1() {
        return price1;
    }

    public void setPrice1(int price1) {
        this.price1 = price1;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCartQuan() {
        return cartQuan;
    }

    public void setCartQuan(int cartQuan) {
        this.cartQuan = cartQuan;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId1() {
        return id1;
    }

    public void setId1(int id1) {
        this.id1=id1;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor1() {
        return color1;
    }

    public void setColor1(String color1) {
        this.color1 = color1;
    }

    public String getSize1() {
        return size1;
    }

    public void setSize1(String size1) {
        this.size1 = size1;
    }

    public String getTitle2() {
        return product_name2;
    }

    public void setTitle2(String product_name2) {
        this.product_name2 = product_name2;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public int getPrice2() {
        return price2;
    }

    public void setPrice2(int price2) {
        this.price2 = price2;
    }

    public float getRating2() {
        return (float) rating2;
    }

    public void setRating2(float rating2) {
        this.rating2 = rating2;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getBrand2(){
        return brand2;
    }

    public void setBrand2(String brand2) {
        this.brand2 = brand2;
    }

    public int getStocks2() {
        return stocks2;
    }

    public void setStocks2(int stocks2) {
        this.stocks2 = stocks2;
    }

    public double getSold2() {
        return sold2;
    }

    public void setSold2(int sold2) {
        this.sold2 = sold2;
    }

    public String getSeller2() {
        return seller2;
    }

    public void setSeller2(String seller2) {
        this.seller2 = seller2;
    }

    public String getColor2() {
        return color2;
    }

    public void setColor2(String color2) {
        this.color2 = color2;
    }

    public String getSize2() {
        return size2;
    }

    public void setSize2(String size2) {
        this.size2 = size2;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public int getProdID() {
        return ProdID;
    }

    public void setProdID(int ProdID) {
        this.ProdID = ProdID;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public float getRating3() {
        return (float) rating3;
    }

    public void setRating3(float rating3) {
        this.rating3 = rating3;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getQuantity1() {
        return quantity1;
    }

    public void setQuantity1(int quantity1) {
        this.quantity1 = quantity1;
    }

    public String getSellerImage() {
        return sellerImage;
    }

    public void setSellerImage(String sellerImage) {
        this.sellerImage = sellerImage;
    }

}
