package com.basti.bean;

import java.util.ArrayList;
import java.util.List;

public class MyContacts {
    private String name;
    private List<String> phone;


    public MyContacts() {
        phone = new ArrayList<>();

    }

    public MyContacts(String name, List<String> phone) {
        this.name = name;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }
}