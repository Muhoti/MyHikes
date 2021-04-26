 package com.strongmuhoti.myhikes;

public class Features {

    private String imageUrl;
    private String Name;
    private String About;
    private String Latitude;
    private String Longitude;

    public Features(){

    }

    public Features(String Name, String About, String Latitude, String Longitude, String imageUrl){
        this.imageUrl = imageUrl;
        this.Name = Name;
        this.About = About;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }



    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}

