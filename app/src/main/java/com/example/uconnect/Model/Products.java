package com.example.uconnect.Model;

public class Products    {

    private String Pname,Description,Price,Image,Category,pid,Date,Time,Verified;

    public Products(){}

    public Products(String pname, String description, String price, String image, String category, String pid, String date, String time, String verified) {
        Pname = pname;
        Description = description;
        Price = price;
        Image = image;
        Category = category;
        this.pid = pid;
        Date = date;
        Time = time;
        Verified = verified;
    }

    public String getPname() {
        return Pname;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getVerified() {
        return Verified;
    }

    public void setVerified(String verified) {
        Verified = verified;
    }
}
