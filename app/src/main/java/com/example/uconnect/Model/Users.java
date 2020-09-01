package com.example.uconnect.Model;

public class Users {

private String Name,Phone,Password,Image,Address;

Users(){
}

    public Users(String name, String phone, String password, String image, String address) {
        Name = name;
        Phone = phone;
        Password = password;
        Image = image;
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
