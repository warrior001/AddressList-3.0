package com.example.nir.addresslist30;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;


@IgnoreExtraProperties
public class User {

    private String name;
    private String email;
    private String address;
    private String phone;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {  }

    public User(String name, String email, String address, String phone) {
        this.name = name;
        this.email = email;
        this.address=address;
        this.phone=phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUser(User user) //not Needed atm....
    {
        this.name=user.getName();
        this.address=user.getAddress();
        this.email=user.getEmail();
        this.phone=user.phone;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("address", address);
        result.put("name", name);
        result.put("email", email);
        result.put("phone", phone);

        return result;
    }
}
