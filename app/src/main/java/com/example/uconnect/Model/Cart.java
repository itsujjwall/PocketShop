package com.example.uconnect.Model;

public class Cart {
    private String PName,PPrice,Quantity,pid,Time,Date,Discount;

    Cart(){
    }

    public Cart(String PName, String PPrice, String quantity, String pid, String time, String date, String discount) {
        this.PName = PName;
        this.PPrice = PPrice;
        Quantity = quantity;
        this.pid = pid;
        Time = time;
        Date = date;
        Discount = discount;
    }

    public String getPName() {
        return PName;
    }

    public void setPName(String PName) {
        this.PName = PName;
    }

    public String getPPrice() {
        return PPrice;
    }

    public void setPPrice(String PPrice) {
        this.PPrice = PPrice;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }
}
