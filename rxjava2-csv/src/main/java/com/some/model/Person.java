package com.some.model;

public class Person {
    private String color;
    private String firstname;
    private String lastname;
    private String phonenumber;
    private String zipcode;

    public Person() {
    }

    public Person(String color, String firstname, String lastname, String phonenumber, String zipcode) {
        this.color = color;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phonenumber = phonenumber;
        this.zipcode = zipcode;
    }

    public String getColor() {
        return color;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getZipcode() {
        return zipcode;
    }
}
