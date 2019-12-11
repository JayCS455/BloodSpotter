package com.example.bloodspotter.model;

public class User
{
    String name;
    String email;
    String password;
    String id,verification_code;

    String contact_no;
    String country,state,city,dob,blood_group;

    public String getVerification_code() {
        return verification_code;
    }

    public String getPassword() {
        return password;
    }

    public User( String id,String name, String email, String password, String contact_no, String country, String state, String city, String dob, String blood_group,String verification_code) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.id = id;

        this.contact_no = contact_no;
        this.country = country;
        this.state = state;
        this.city = city;
        this.dob = dob;
        this.blood_group = blood_group;
        this.verification_code=verification_code;
    }




    public String getName() {
        return name;
    }

    public User(String name, String city, String blood_group) {
        this.name = name;
        this.city = city;
        this.blood_group = blood_group;
    }



    public String getContact_no() {
        return contact_no;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public User( String id, String name, String email, String contact_no, String country, String state, String city, String dob, String blood_group) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.contact_no = contact_no;
        this.country = country;
        this.state = state;
        this.city = city;
        this.dob = dob;
        this.blood_group = blood_group;
    }

    public String getDob() {
        return dob;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public String getEmail() {
        return email;
    }



    public  User()
    {
        name = "";
        email = "";
        this.id = "";
        this.contact_no = "";
        this.country = "";
        this.state = "";
        this.city = "";
        this.dob = "";
        this.blood_group = "";
    }


}