package com.example.uconnect.Model;

public class Admins {

    private String Name,Phone,Password;

    Admins(){

    }

    public Admins(String name, String phone, String password) {
        Name = name;
        Phone = phone;
        Password = password;
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
}
