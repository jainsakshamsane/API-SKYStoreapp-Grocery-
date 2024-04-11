package com.groceryapp.Models;

import com.google.gson.annotations.SerializedName;

public class loginModel {


    @SerializedName("id")
    private int id;
    @SerializedName("email")
    private String email;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("name")
    private NameModel name;
    @SerializedName("address")
    private AddressModel address;
    @SerializedName("phone")
    private String phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public NameModel getName() {
        return name;
    }

    public void setName(NameModel name) {
        this.name = name;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public loginModel(String email, String username, String password, NameModel name, AddressModel address, String phone) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public static class NameModel {
        @SerializedName("firstname")
        private String firstname;
        @SerializedName("lastname")
        private String lastname;

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public NameModel(String firstname, String lastname) {
            this.firstname = firstname;
            this.lastname = lastname;
        }
    }

    public static class AddressModel {
        @SerializedName("city")
        private String city;
        @SerializedName("street")
        private String street;
        @SerializedName("number")
        private String number;
        @SerializedName("zipcode")
        private String zipcode;
        @SerializedName("geolocation")
        private GeoLocationModel geolocation;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public GeoLocationModel getGeolocation() {
            return geolocation;
        }

        public void setGeolocation(GeoLocationModel geolocation) {
            this.geolocation = geolocation;
        }

        public AddressModel(String city, String street, String number, String zipcode, GeoLocationModel geolocation) {
            this.city = city;
            this.street = street;
            this.number = number;
            this.zipcode = zipcode;
            this.geolocation = geolocation;
        }
    }

    public static class GeoLocationModel {
        @SerializedName("lat")
        private String lat;
        @SerializedName("long")
        private String lon;

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public GeoLocationModel(String lat, String lon) {
            this.lat = lat;
            this.lon = lon;
        }
    }
}
